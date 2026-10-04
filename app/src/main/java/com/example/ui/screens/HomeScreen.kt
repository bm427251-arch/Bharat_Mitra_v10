package com.example.ui.screens

import android.Manifest
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Geocoder
import android.location.Location
import android.os.Looper
import android.provider.Settings
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.animation.*
import androidx.compose.animation.core.*
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowForwardIos
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.hapticfeedback.HapticFeedbackType
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.content.ContextCompat
import com.example.viewmodel.MainViewModel
import com.google.android.gms.location.*
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.compose.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    viewModel: MainViewModel,
    onNavigateToRentCar: () -> Unit = {},
    onNavigateToHireDriver: () -> Unit = {},
    onNavigateToElite: () -> Unit = {},
    onNavigateToRiderCategory: () -> Unit = {},
    onNavigateToDriverCategory: () -> Unit = {},
    onNavigateToRentCategory: () -> Unit = {}
) {
    val context = LocalContext.current
    val coroutineScope = rememberCoroutineScope()
    val haptic = LocalHapticFeedback.current
    val shimmerBrush = rememberShimmerBrush()

    // 100% Real Device Location state - NO hardcoded Barasat, NO hardcoded Ukiah
    var pickupAddress by remember { mutableStateOf("Detecting your exact GPS location...") }
    var dropAddress by remember { mutableStateOf("") }
    var isLocationLoading by remember { mutableStateOf(false) }
    var locationAccuracyMeters by remember { mutableStateOf<Float?>(null) }
    var userLatLng by remember { mutableStateOf<LatLng?>(null) }
    var selectedFleetTitle by remember { mutableStateOf<String?>("Toto (E-Rickshaw)") }
    var showGpsDialog by remember { mutableStateOf(false) }

    val fusedLocationClient = remember { LocationServices.getFusedLocationProviderClient(context) }
    val cameraPositionState = rememberCameraPositionState()

    var hasFinePermission by remember {
        mutableStateOf(
            ContextCompat.checkSelfPermission(
                context,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED
        )
    }

    // Filter out Android Studio default emulator mock locations (Ukiah, Google HQ)
    fun isEmulatorDefaultLocation(lat: Double, lng: Double): Boolean {
        val isUkiah = (lat in 39.0..39.3 && lng in -123.4..-123.0)
        val isGoogleHq = (lat in 37.3..37.5 && lng in -122.2..-122.0)
        return isUkiah || isGoogleHq
    }

    // Check if GPS hardware is turned on
    fun checkGpsEnabled(): Boolean {
        val lm = context.getSystemService(Context.LOCATION_SERVICE) as? android.location.LocationManager
        val isEnabled = lm?.isProviderEnabled(android.location.LocationManager.GPS_PROVIDER) == true ||
                lm?.isProviderEnabled(android.location.LocationManager.NETWORK_PROVIDER) == true
        if (!isEnabled) {
            showGpsDialog = true
        }
        return isEnabled
    }

    // High accuracy location request with 3000ms interval
    val locationRequest = remember {
        LocationRequest.Builder(Priority.PRIORITY_HIGH_ACCURACY, 3000L)
            .setMinUpdateIntervalMillis(1500L)
            .setWaitForAccurateLocation(true)
            .build()
    }

    // Function to get real current location
    fun getCurrentStandingLocation() {
        isLocationLoading = true

        val fineGranted = ContextCompat.checkSelfPermission(
            context,
            Manifest.permission.ACCESS_FINE_LOCATION
        ) == PackageManager.PERMISSION_GRANTED
        hasFinePermission = fineGranted

        if (!fineGranted) {
            isLocationLoading = false
            return
        }

        if (!checkGpsEnabled()) {
            isLocationLoading = false
            return
        }

        try {
            fusedLocationClient.getCurrentLocation(Priority.PRIORITY_HIGH_ACCURACY, null)
                .addOnSuccessListener { location: Location? ->
                    isLocationLoading = false
                    if (location != null) {
                        if (isEmulatorDefaultLocation(location.latitude, location.longitude)) {
                            Toast.makeText(
                                context,
                                "GPS not found on emulator, please test on real device",
                                Toast.LENGTH_LONG
                            ).show()
                            pickupAddress = "GPS not found on emulator, please test on real device"
                            return@addOnSuccessListener
                        }

                        val currentLatLng = LatLng(location.latitude, location.longitude)
                        userLatLng = currentLatLng
                        locationAccuracyMeters = location.accuracy

                        // Camera moves to exact standing currentLatLng
                        coroutineScope.launch {
                            cameraPositionState.animate(
                                CameraUpdateFactory.newLatLngZoom(currentLatLng, 16.5f),
                                durationMs = 1000
                            )
                        }

                        // Reverse geocode to exact standing address
                        coroutineScope.launch(Dispatchers.IO) {
                            try {
                                val geocoder = Geocoder(context, Locale.getDefault())
                                @Suppress("DEPRECATION")
                                val addresses = geocoder.getFromLocation(location.latitude, location.longitude, 1)
                                val resolved = addresses?.firstOrNull()?.getAddressLine(0)
                                withContext(Dispatchers.Main) {
                                    if (!resolved.isNullOrBlank()) {
                                        pickupAddress = resolved
                                        viewModel.setPickupLocation(resolved)
                                    } else {
                                        val coords = "Exact Location: ${String.format(Locale.US, "%.5f", location.latitude)}, ${String.format(Locale.US, "%.5f", location.longitude)}"
                                        pickupAddress = coords
                                        viewModel.setPickupLocation(coords)
                                    }
                                }
                            } catch (e: Exception) {
                                withContext(Dispatchers.Main) {
                                    val coords = "Exact Location: ${String.format(Locale.US, "%.5f", location.latitude)}, ${String.format(Locale.US, "%.5f", location.longitude)}"
                                    pickupAddress = coords
                                    viewModel.setPickupLocation(coords)
                                }
                            }
                        }
                    } else {
                        Toast.makeText(
                            context,
                            "GPS not found on emulator, please test on real device",
                            Toast.LENGTH_LONG
                        ).show()
                        if (pickupAddress.contains("Detecting")) {
                            pickupAddress = "GPS not found on emulator, please test on real device"
                        }
                    }
                }
                .addOnFailureListener {
                    isLocationLoading = false
                    Toast.makeText(
                        context,
                        "GPS not found on emulator, please test on real device",
                        Toast.LENGTH_SHORT
                    ).show()
                }
        } catch (e: SecurityException) {
            isLocationLoading = false
        } catch (e: Exception) {
            isLocationLoading = false
        }
    }

    // Permission launcher
    val locationPermissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestMultiplePermissions()
    ) { permissions ->
        val isGranted = permissions[Manifest.permission.ACCESS_FINE_LOCATION] == true ||
                permissions[Manifest.permission.ACCESS_COARSE_LOCATION] == true
        hasFinePermission = permissions[Manifest.permission.ACCESS_FINE_LOCATION] == true
        if (isGranted) {
            getCurrentStandingLocation()
        } else {
            pickupAddress = "Location permission denied. Please grant location access."
        }
    }

    // On HomeScreen launch: check permission ACCESS_FINE_LOCATION and fetch
    LaunchedEffect(Unit) {
        val fineGranted = ContextCompat.checkSelfPermission(
            context,
            Manifest.permission.ACCESS_FINE_LOCATION
        ) == PackageManager.PERMISSION_GRANTED
        if (fineGranted) {
            getCurrentStandingLocation()
        } else {
            locationPermissionLauncher.launch(
                arrayOf(
                    Manifest.permission.ACCESS_FINE_LOCATION,
                    Manifest.permission.ACCESS_COARSE_LOCATION
                )
            )
        }
    }

    // Continuous location updates when permission is granted
    DisposableEffect(hasFinePermission) {
        if (hasFinePermission) {
            val locationCallback = object : LocationCallback() {
                override fun onLocationResult(result: LocationResult) {
                    val loc = result.lastLocation ?: return
                    if (isEmulatorDefaultLocation(loc.latitude, loc.longitude)) return

                    val currentLatLng = LatLng(loc.latitude, loc.longitude)
                    userLatLng = currentLatLng
                    locationAccuracyMeters = loc.accuracy

                    coroutineScope.launch(Dispatchers.IO) {
                        try {
                            val geocoder = Geocoder(context, Locale.getDefault())
                            @Suppress("DEPRECATION")
                            val addresses = geocoder.getFromLocation(loc.latitude, loc.longitude, 1)
                            val resolved = addresses?.firstOrNull()?.getAddressLine(0)
                            if (!resolved.isNullOrBlank()) {
                                withContext(Dispatchers.Main) {
                                    pickupAddress = resolved
                                    viewModel.setPickupLocation(resolved)
                                }
                            }
                        } catch (e: Exception) {
                            // ignore
                        }
                    }
                }
            }

            try {
                fusedLocationClient.requestLocationUpdates(
                    locationRequest,
                    locationCallback,
                    Looper.getMainLooper()
                )
            } catch (e: SecurityException) {
                // ignore
            }

            onDispose {
                fusedLocationClient.removeLocationUpdates(locationCallback)
            }
        } else {
            onDispose {}
        }
    }

    // Dialog: "Please enable GPS"
    if (showGpsDialog) {
        AlertDialog(
            onDismissRequest = { showGpsDialog = false },
            title = {
                Text(
                    text = "Please enable GPS",
                    style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold)
                )
            },
            text = {
                Text("GPS / Location is currently turned off. Please turn on device GPS to detect your real standing location.")
            },
            confirmButton = {
                Button(
                    onClick = {
                        showGpsDialog = false
                        try {
                            context.startActivity(Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS))
                        } catch (e: Exception) {
                            // ignore
                        }
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF0D1B68))
                ) {
                    Text("Open Settings")
                }
            },
            dismissButton = {
                TextButton(onClick = { showGpsDialog = false }) {
                    Text("Cancel")
                }
            }
        )
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = "BHARAT MITRA",
                        style = MaterialTheme.typography.titleLarge.copy(
                            color = Color.White,
                            fontWeight = FontWeight.Black,
                            letterSpacing = 1.5.sp
                        )
                    )
                },
                actions = {
                    IconButton(
                        onClick = onNavigateToElite,
                        modifier = Modifier.testTag("home_top_shield_btn")
                    ) {
                        Icon(
                            imageVector = Icons.Default.Shield,
                            contentDescription = "Elite SOS",
                            tint = Color(0xFFFA8520),
                            modifier = Modifier.size(28.dp)
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Color(0xFF0D1B68)
                )
            )
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = {
                    haptic.performHapticFeedback(HapticFeedbackType.LongPress)
                    onNavigateToRiderCategory()
                },
                containerColor = Color(0xFF138808),
                contentColor = Color.White,
                modifier = Modifier
                    .padding(bottom = 80.dp)
                    .testTag("home_create_profile_fab")
            ) {
                Row(
                    modifier = Modifier.padding(horizontal = 16.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(Icons.Default.Add, contentDescription = null)
                    Spacer(modifier = Modifier.width(8.dp))
                    Text("Create Profile", fontWeight = FontWeight.Bold)
                }
            }
        }
    ) { innerPadding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(14.dp)
        ) {
            // West Bengal Active Transport Hub Card (Gradient [#0D1B68, #1E3A8A])
            item {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(16.dp),
                    elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
                ) {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .background(
                                brush = Brush.linearGradient(
                                    colors = listOf(Color(0xFF0D1B68), Color(0xFF1E3A8A))
                                )
                            )
                            .padding(16.dp)
                    ) {
                        Column(
                            horizontalAlignment = Alignment.Start,
                            verticalArrangement = Arrangement.spacedBy(6.dp)
                        ) {
                            Text(
                                text = "West Bengal Active Transport Hub",
                                style = MaterialTheme.typography.titleMedium.copy(
                                    color = Color.White,
                                    fontSize = 18.sp,
                                    fontWeight = FontWeight.Bold
                                )
                            )
                            Text(
                                text = "Zero Commission • Verified Local Drivers • Instant SOS Protection",
                                style = MaterialTheme.typography.bodyMedium.copy(
                                    color = Color.White.copy(alpha = 0.8f),
                                    fontSize = 13.sp
                                )
                            )
                        }
                    }
                }
            }

            // Pickup Radar with GPS Accuracy & Auto-detect
            item {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(20.dp),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                    elevation = CardDefaults.cardElevation(defaultElevation = 8.dp),
                    border = BorderStroke(0.5.dp, Color(0xFFE0E0E0))
                ) {
                    Column(
                        modifier = Modifier.padding(16.dp),
                        verticalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        // Shimmer loading line while GPS fetching
                        if (isLocationLoading) {
                            Box(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(4.dp)
                                    .clip(RoundedCornerShape(2.dp))
                                    .background(shimmerBrush)
                            )
                        }
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Box(
                                    modifier = Modifier
                                        .size(8.dp)
                                        .clip(CircleShape)
                                        .background(Color(0xFF138808))
                                )
                                Spacer(modifier = Modifier.width(6.dp))
                                Text(
                                    text = "Pickup Location Radar",
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 13.sp,
                                    color = Color(0xFF0D1B68)
                                )
                            }

                            // Accuracy badge
                            locationAccuracyMeters?.let { acc ->
                                Surface(
                                    color = if (acc <= 30f) Color(0xFF138808).copy(alpha = 0.15f) else Color(0xFFFA8520).copy(alpha = 0.15f),
                                    shape = RoundedCornerShape(6.dp)
                                ) {
                                    Text(
                                        text = if (acc <= 30f) "GPS: ±${acc.toInt()}m (High Precision)" else "GPS: ±${acc.toInt()}m",
                                        fontSize = 10.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = if (acc <= 30f) Color(0xFF138808) else Color(0xFFFA8520),
                                        modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                                    )
                                }
                            }
                        }

                        // Pickup Address Text Field + GPS Refetch Button with Spinner
                        OutlinedTextField(
                            value = pickupAddress,
                            onValueChange = {
                                pickupAddress = it
                                viewModel.setPickupLocation(it)
                            },
                            label = { Text("Exact Standing Pickup Address") },
                            leadingIcon = {
                                Icon(Icons.Default.MyLocation, contentDescription = null, tint = Color(0xFF138808))
                            },
                            trailingIcon = {
                                IconButton(
                                    onClick = { getCurrentStandingLocation() },
                                    modifier = Modifier.testTag("refresh_gps_btn")
                                ) {
                                    if (isLocationLoading) {
                                        CircularProgressIndicator(
                                            modifier = Modifier.size(18.dp),
                                            strokeWidth = 2.dp,
                                            color = Color(0xFFFA8520)
                                        )
                                    } else {
                                        Icon(
                                            Icons.Default.Refresh,
                                            contentDescription = "Refetch Current GPS",
                                            tint = Color(0xFFFA8520)
                                        )
                                    }
                                }
                            },
                            singleLine = true,
                            modifier = Modifier.fillMaxWidth().testTag("pickup_address_input"),
                            shape = RoundedCornerShape(10.dp)
                        )

                        // Drop Location Field
                        OutlinedTextField(
                            value = dropAddress,
                            onValueChange = {
                                dropAddress = it
                                viewModel.setDropLocation(it)
                            },
                            label = { Text("Where to? (Destination)") },
                            placeholder = { Text("Enter destination address") },
                            leadingIcon = {
                                Icon(Icons.Default.LocationOn, contentDescription = null, tint = Color(0xFFFA8520))
                            },
                            singleLine = true,
                            modifier = Modifier.fillMaxWidth().testTag("drop_address_input"),
                            shape = RoundedCornerShape(10.dp)
                        )
                    }
                }
            }

            // Real Google Map with camera moving to exact standing location
            item {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(200.dp)
                        .clip(RoundedCornerShape(14.dp))
                        .background(Color(0xFFE2E8F0))
                ) {
                    GoogleMap(
                        modifier = Modifier.fillMaxSize(),
                        cameraPositionState = cameraPositionState,
                        properties = MapProperties(
                            isMyLocationEnabled = hasFinePermission && userLatLng != null
                        ),
                        uiSettings = MapUiSettings(
                            zoomControlsEnabled = false,
                            myLocationButtonEnabled = true
                        )
                    ) {
                        userLatLng?.let { latLng ->
                            Marker(
                                state = MarkerState(position = latLng),
                                title = "Your Exact Standing Location",
                                snippet = pickupAddress
                            )
                        }
                    }

                    if (userLatLng == null) {
                        Box(
                            modifier = Modifier
                                .fillMaxSize()
                                .background(Color(0x990F172A)),
                            contentAlignment = Alignment.Center
                        ) {
                            Column(
                                horizontalAlignment = Alignment.CenterHorizontally,
                                verticalArrangement = Arrangement.spacedBy(8.dp)
                            ) {
                                if (isLocationLoading) {
                                    CircularProgressIndicator(
                                        color = Color(0xFFFA8520),
                                        modifier = Modifier.size(28.dp)
                                    )
                                    Text(
                                        text = "Detecting real standing GPS...",
                                        color = Color.White,
                                        fontSize = 12.sp,
                                        fontWeight = FontWeight.Medium
                                    )
                                } else {
                                    Icon(
                                        imageVector = Icons.Default.LocationOff,
                                        contentDescription = null,
                                        tint = Color(0xFFFA8520),
                                        modifier = Modifier.size(32.dp)
                                    )
                                    Text(
                                        text = "Real device GPS required\nTap refresh icon to detect",
                                        color = Color.White,
                                        fontSize = 12.sp,
                                        textAlign = TextAlign.Center
                                    )
                                }
                            }
                        }
                    }
                }
            }

            // Available Fleet Header
            item {
                Text(
                    text = "Available Fleet",
                    style = MaterialTheme.typography.titleMedium.copy(
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold
                    )
                )
            }

            // 1. Toto (E-Rickshaw)
            item {
                FleetVehicleCard(
                    title = "Toto (E-Rickshaw)",
                    subtitle = "₹10 base + ₹5/km",
                    icon = Icons.Default.ElectricRickshaw,
                    isSelected = selectedFleetTitle == "Toto (E-Rickshaw)",
                    onClick = {
                        selectedFleetTitle = "Toto (E-Rickshaw)"
                        Toast.makeText(context, "Selected Toto (E-Rickshaw) • ₹10 base + ₹5/km", Toast.LENGTH_SHORT).show()
                    },
                    testTag = "fleet_item_toto"
                )
            }

            // 2. Auto Rickshaw
            item {
                FleetVehicleCard(
                    title = "Auto Rickshaw",
                    subtitle = "₹15 base + ₹7/km",
                    icon = Icons.Default.TwoWheeler,
                    isSelected = selectedFleetTitle == "Auto Rickshaw",
                    onClick = {
                        selectedFleetTitle = "Auto Rickshaw"
                        Toast.makeText(context, "Selected Auto Rickshaw • ₹15 base + ₹7/km", Toast.LENGTH_SHORT).show()
                    },
                    testTag = "fleet_item_auto"
                )
            }

            // 3. Bike Taxi
            item {
                FleetVehicleCard(
                    title = "Bike Taxi",
                    subtitle = "₹15 base + ₹6/km",
                    icon = Icons.Default.TwoWheeler,
                    isSelected = selectedFleetTitle == "Bike Taxi",
                    onClick = {
                        selectedFleetTitle = "Bike Taxi"
                        Toast.makeText(context, "Selected Bike Taxi • ₹15 base + ₹6/km", Toast.LENGTH_SHORT).show()
                    },
                    testTag = "fleet_item_bike"
                )
            }

            // 4. Mini Cab (AC/Non-AC)
            item {
                FleetVehicleCard(
                    title = "Mini Cab (AC/Non-AC)",
                    subtitle = "₹40 base + ₹12/km",
                    icon = Icons.Default.LocalTaxi,
                    isSelected = selectedFleetTitle == "Mini Cab (AC/Non-AC)",
                    onClick = {
                        selectedFleetTitle = "Mini Cab (AC/Non-AC)"
                        Toast.makeText(context, "Selected Mini Cab • ₹40 base + ₹12/km", Toast.LENGTH_SHORT).show()
                    },
                    testTag = "fleet_item_minicab"
                )
            }

            // Instant Book Fleet Action (Premium Gradient)
            item {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(48.dp)
                        .clip(RoundedCornerShape(12.dp))
                        .background(
                            brush = Brush.horizontalGradient(
                                listOf(Color(0xFFFA8520), Color(0xFF138808))
                            )
                        )
                        .clickable {
                            if (dropAddress.isNotBlank()) {
                                Toast.makeText(context, "Searching nearby verified drivers for $selectedFleetTitle from $pickupAddress...", Toast.LENGTH_LONG).show()
                            } else {
                                Toast.makeText(context, "Please enter your destination drop location", Toast.LENGTH_SHORT).show()
                            }
                        }
                        .testTag("book_fleet_ride_btn"),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "Book Ride ($selectedFleetTitle)",
                        color = Color.White,
                        fontWeight = FontWeight.Bold,
                        fontSize = 14.sp
                    )
                }
            }

            item {
                Spacer(modifier = Modifier.height(16.dp))
            }
        }
    }
}

@Composable
private fun FleetVehicleCard(
    title: String,
    subtitle: String,
    icon: ImageVector,
    isSelected: Boolean,
    onClick: () -> Unit,
    testTag: String
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() }
            .testTag(testTag),
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(
            containerColor = if (isSelected) Color(0xFFFA8520).copy(alpha = 0.08f) else MaterialTheme.colorScheme.surface
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 8.dp),
        border = BorderStroke(0.5.dp, Color(0xFFE0E0E0))
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Orange tinted Circle Avatar
            Box(
                modifier = Modifier
                    .size(46.dp)
                    .clip(CircleShape)
                    .background(Color(0xFFFA8520).copy(alpha = 0.15f)),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = icon,
                    contentDescription = title,
                    tint = Color(0xFFFA8520),
                    modifier = Modifier.size(26.dp)
                )
            }

            Spacer(modifier = Modifier.width(14.dp))

            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = title,
                    style = MaterialTheme.typography.titleMedium.copy(
                        fontWeight = FontWeight.Bold,
                        fontSize = 16.sp
                    )
                )
                Spacer(modifier = Modifier.height(2.dp))
                Text(
                    text = subtitle,
                    style = MaterialTheme.typography.bodyMedium.copy(
                        color = Color.Gray,
                        fontSize = 13.sp
                    )
                )
            }

            Icon(
                imageVector = Icons.AutoMirrored.Filled.ArrowForwardIos,
                contentDescription = null,
                tint = Color.Gray,
                modifier = Modifier.size(16.dp)
            )
        }
    }
}

@Composable
fun rememberShimmerBrush(): Brush {
    val shimmerColors = listOf(
        Color(0xFFCBD5E1).copy(alpha = 0.6f),
        Color.White.copy(alpha = 0.9f),
        Color(0xFFCBD5E1).copy(alpha = 0.6f)
    )
    val transition = rememberInfiniteTransition(label = "shimmer_transition")
    val translateAnim by transition.animateFloat(
        initialValue = 0f,
        targetValue = 1000f,
        animationSpec = infiniteRepeatable(
            animation = tween(durationMillis = 1200, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Restart
        ),
        label = "shimmer_anim"
    )
    return Brush.linearGradient(
        colors = shimmerColors,
        start = androidx.compose.ui.geometry.Offset.Zero,
        end = androidx.compose.ui.geometry.Offset(x = translateAnim, y = translateAnim)
    )
}
