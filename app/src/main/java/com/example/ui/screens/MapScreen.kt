package com.example.ui.screens

import android.Manifest
import android.content.pm.PackageManager
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.content.ContextCompat
import com.example.data.SampleData
import com.example.ui.theme.AshokaBlue
import com.example.ui.theme.IndianGreen
import com.example.ui.theme.SaffronPrimary
import com.example.viewmodel.MainViewModel
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.compose.*
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MapScreen(
    viewModel: MainViewModel,
    onBack: () -> Unit
) {
    BackHandler { onBack() }
    val context = LocalContext.current
    val coroutineScope = rememberCoroutineScope()

    val currentLocation by viewModel.currentLocation.collectAsState()
    val currentAddress by viewModel.pickupLocation.collectAsState()
    val dropLocation by viewModel.dropLocation.collectAsState()
    val isDropConfirmed by viewModel.isDropConfirmed.collectAsState()
    val accuracyMeters by viewModel.accuracyMeters.collectAsState()
    val isLocationLoading by viewModel.isLocationLoading.collectAsState()

    val hasLocationPermission = remember {
        ContextCompat.checkSelfPermission(
            context,
            Manifest.permission.ACCESS_FINE_LOCATION
        ) == PackageManager.PERMISSION_GRANTED
    }

    val userLatLng = remember(currentLocation) {
        currentLocation?.let { LatLng(it.latitude, it.longitude) }
    }

    val cameraPositionState = rememberCameraPositionState {
        position = CameraPosition.fromLatLngZoom(userLatLng ?: LatLng(0.0, 0.0), if (userLatLng != null) 16f else 2f)
    }

    // Requirement 5: Camera should animate to current location on load
    var hasAnimatedToLocation by remember { mutableStateOf(false) }
    LaunchedEffect(currentLocation) {
        if (currentLocation != null && !hasAnimatedToLocation) {
            hasAnimatedToLocation = true
            cameraPositionState.animate(
                update = CameraUpdateFactory.newLatLngZoom(
                    LatLng(currentLocation!!.latitude, currentLocation!!.longitude),
                    16.5f
                ),
                durationMs = 1200
            )
        }
    }

    // Find destination LatLng
    val dropLatLng = remember(dropLocation) {
        val matched = SampleData.locationSuggestions.find { it.title.equals(dropLocation, ignoreCase = true) }
        if (matched != null) LatLng(matched.lat, matched.lng) else null
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Column {
                        Text("Live Satellite Map & Route", fontWeight = FontWeight.Bold, fontSize = 17.sp)
                        Text(
                            text = if (accuracyMeters != null) "GPS Accuracy: ±${accuracyMeters!!.toInt()}m (High Precision)" else "Detecting GPS satellite nodes...",
                            fontSize = 11.sp,
                            color = if ((accuracyMeters ?: 0f) > 50f) Color(0xFFD97706) else IndianGreen
                        )
                    }
                },
                navigationIcon = {
                    IconButton(onClick = onBack, modifier = Modifier.testTag("map_back_btn")) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
                    }
                },
                actions = {
                    IconButton(
                        onClick = {
                            viewModel.refreshAccurateLocation()
                            userLatLng?.let { target ->
                                coroutineScope.launch {
                                    cameraPositionState.animate(
                                        CameraUpdateFactory.newLatLngZoom(target, 17f),
                                        1000
                                    )
                                }
                            }
                        },
                        modifier = Modifier.testTag("map_refresh_btn")
                    ) {
                        if (isLocationLoading) {
                            CircularProgressIndicator(
                                modifier = Modifier.size(20.dp),
                                color = SaffronPrimary,
                                strokeWidth = 2.dp
                            )
                        } else {
                            Icon(Icons.Default.MyLocation, contentDescription = "Re-center GPS", tint = SaffronPrimary)
                        }
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = MaterialTheme.colorScheme.surface)
            )
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = {
                    viewModel.refreshAccurateLocation()
                    userLatLng?.let { target ->
                        coroutineScope.launch {
                            cameraPositionState.animate(
                                CameraUpdateFactory.newLatLngZoom(target, 17f),
                                1000
                            )
                        }
                    }
                },
                containerColor = SaffronPrimary,
                contentColor = Color.White,
                shape = CircleShape,
                modifier = Modifier.testTag("map_my_location_fab")
            ) {
                Icon(Icons.Default.GpsFixed, contentDescription = "My Location")
            }
        }
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            // Google Map with isMyLocationEnabled = true
            GoogleMap(
                modifier = Modifier.fillMaxSize().testTag("google_map_view"),
                cameraPositionState = cameraPositionState,
                properties = MapProperties(
                    isMyLocationEnabled = hasLocationPermission,
                    isTrafficEnabled = true
                ),
                uiSettings = MapUiSettings(
                    myLocationButtonEnabled = false,
                    zoomControlsEnabled = false,
                    compassEnabled = true
                )
            ) {
                // User Current Pickup Marker
                userLatLng?.let { target ->
                    Marker(
                        state = MarkerState(position = target),
                        title = "Pickup: $currentAddress",
                        snippet = "Accuracy: ±${accuracyMeters?.toInt() ?: 12}m",
                        icon = BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_AZURE)
                    )
                }

                // Key Landmark suggestions markers
                Marker(
                    state = MarkerState(position = LatLng(22.7222, 88.4812)),
                    title = "Barasat Court",
                    snippet = "District & Sessions Court, Kachhari Road",
                    icon = BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_ORANGE)
                )

                Marker(
                    state = MarkerState(position = LatLng(22.7235, 88.4825)),
                    title = "Colony More",
                    snippet = "Jessore Road Junction, Barasat",
                    icon = BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN)
                )

                Marker(
                    state = MarkerState(position = LatLng(22.7198, 88.4841)),
                    title = "Barasat Station",
                    snippet = "Station Road, Sealdah North",
                    icon = BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_VIOLET)
                )

                // Elite users marker Orange #FA8520, Group users Green #138808
                userLatLng?.let { target ->
                    Marker(
                        state = MarkerState(position = LatLng(target.latitude + 0.0025, target.longitude + 0.0020)),
                        title = "★ Elite VIP Driver",
                        snippet = "Elite Member • Rating 5.0 ★ • Zero Cancellation",
                        icon = BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_ORANGE)
                    )

                    Marker(
                        state = MarkerState(position = LatLng(target.latitude - 0.0020, target.longitude - 0.0018)),
                        title = "✓ Group Partner Driver",
                        snippet = "Verified Community Driver • Zero Commission",
                        icon = BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN)
                    )
                }

                // Destination Drop Marker & Polyline (if drop confirmed)
                if (isDropConfirmed && dropLatLng != null && userLatLng != null) {
                    Marker(
                        state = MarkerState(position = dropLatLng),
                        title = "Destination: $dropLocation",
                        icon = BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED)
                    )

                    Polyline(
                        points = listOf(userLatLng, dropLatLng),
                        color = SaffronPrimary,
                        width = 12f
                    )
                }
            }

            // Floating Location Accuracy Card at the bottom
            Card(
                modifier = Modifier
                    .align(Alignment.BottomCenter)
                    .fillMaxWidth()
                    .padding(16.dp)
                    .testTag("map_bottom_location_card"),
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface.copy(alpha = 0.95f)),
                elevation = CardDefaults.cardElevation(defaultElevation = 6.dp)
            ) {
                Column(modifier = Modifier.padding(14.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Box(modifier = Modifier.size(10.dp).clip(CircleShape).background(IndianGreen))
                            Spacer(modifier = Modifier.width(8.dp))
                            Text("PICKUP POINT", fontSize = 11.sp, fontWeight = FontWeight.Bold, color = IndianGreen)
                        }

                        Surface(
                            color = if ((accuracyMeters ?: 0f) > 50f) Color(0xFFFEF3C7) else Color(0xFFDCFCE7),
                            shape = RoundedCornerShape(6.dp)
                        ) {
                            Text(
                                text = "±${accuracyMeters?.toInt() ?: 12}m Accuracy",
                                fontSize = 10.sp,
                                fontWeight = FontWeight.Bold,
                                color = if ((accuracyMeters ?: 0f) > 50f) Color(0xFFB45309) else Color(0xFF15803D),
                                modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = currentAddress,
                        fontSize = 13.sp,
                        fontWeight = FontWeight.SemiBold,
                        maxLines = 2
                    )

                    if (isDropConfirmed && dropLocation.isNotBlank()) {
                        Spacer(modifier = Modifier.height(6.dp))
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Box(modifier = Modifier.size(10.dp).clip(CircleShape).background(SaffronPrimary))
                            Spacer(modifier = Modifier.width(8.dp))
                            Text("DROP: $dropLocation", fontSize = 12.sp, fontWeight = FontWeight.Bold, color = SaffronPrimary)
                        }
                    }
                }
            }
        }
    }
}
