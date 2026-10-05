package com.example.ui.screens

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.provider.Settings
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.animation.core.*
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowForward
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.content.ContextCompat
import com.example.ui.components.BharatMitraHeaderLogo
import com.example.ui.theme.*
import com.example.viewmodel.MainViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    viewModel: MainViewModel,
    onNavigateToFleetMap: () -> Unit,
    onNavigateToRentCar: () -> Unit,
    onNavigateToHireDriver: () -> Unit,
    onNavigateToCreateDriver: () -> Unit,
    onNavigateToWallet: () -> Unit,
    onNavigateToAdmin: () -> Unit,
    onNavigateToAutoQr: () -> Unit = {}
) {
    val context = LocalContext.current
    val currentAddress by viewModel.currentAddress.collectAsState()
    val walletBalance by viewModel.walletBalance.collectAsState()
    val isLocationPermissionGranted by viewModel.isLocationPermissionGranted.collectAsState()

    // Runtime Permission Launcher for Location
    val locationPermissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestMultiplePermissions()
    ) { permissions ->
        val fineGranted = permissions[Manifest.permission.ACCESS_FINE_LOCATION] == true
        val coarseGranted = permissions[Manifest.permission.ACCESS_COARSE_LOCATION] == true
        val granted = fineGranted || coarseGranted
        viewModel.updateLocationPermission(granted)
    }

    LaunchedEffect(Unit) {
        val fineCheck = ContextCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED
        val coarseCheck = ContextCompat.checkSelfPermission(context, Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED
        if (fineCheck || coarseCheck) {
            viewModel.updateLocationPermission(true)
        } else {
            locationPermissionLauncher.launch(
                arrayOf(
                    Manifest.permission.ACCESS_FINE_LOCATION,
                    Manifest.permission.ACCESS_COARSE_LOCATION
                )
            )
        }
    }

    // Green pulse animation for Auto-detected Current Location indicator
    val pulseTransition = rememberInfiniteTransition(label = "location_pulse")
    val pulseAlpha by pulseTransition.animateFloat(
        initialValue = 0.35f,
        targetValue = 1.0f,
        animationSpec = infiniteRepeatable(
            animation = tween(700, easing = LinearEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "pulse_alpha"
    )

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            // Exact static logo asset lock: Image.asset('assets/logo.png', width: 120, height: 40)
                            BharatMitraHeaderLogo(width = 110.dp, height = 36.dp)
                            Spacer(modifier = Modifier.width(10.dp))
                            Column {
                                Text(
                                    text = "Hello, Aman!",
                                    style = MaterialTheme.typography.titleMedium.copy(
                                        fontWeight = FontWeight.Black,
                                        fontSize = 17.sp,
                                        color = Color.White
                                    )
                                )
                                Text(
                                    text = "Good Morning",
                                    style = MaterialTheme.typography.bodySmall.copy(
                                        color = BharatOrangeLight,
                                        fontSize = 11.sp,
                                        fontWeight = FontWeight.Medium
                                    )
                                )
                            }
                        }

                        Row(verticalAlignment = Alignment.CenterVertically) {
                            // Auto QR Payment icon button
                            IconButton(
                                onClick = onNavigateToAutoQr,
                                modifier = Modifier.testTag("home_auto_qr_btn")
                            ) {
                                Icon(
                                    imageVector = Icons.Default.QrCodeScanner,
                                    contentDescription = "Auto QR Payment",
                                    tint = BharatOrangeLight
                                )
                            }

                            // Wallet balance badge
                            Surface(
                                modifier = Modifier
                                    .clip(RoundedCornerShape(8.dp))
                                    .clickable { onNavigateToWallet() }
                                    .testTag("home_wallet_badge"),
                                color = BharatGreen.copy(alpha = 0.25f),
                                border = BorderStroke(1.dp, BharatGreen)
                            ) {
                                Row(
                                    modifier = Modifier.padding(horizontal = 6.dp, vertical = 4.dp),
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Icon(Icons.Default.AccountBalanceWallet, contentDescription = null, tint = Color.White, modifier = Modifier.size(13.dp))
                                    Spacer(modifier = Modifier.width(4.dp))
                                    Text("₹$walletBalance", color = Color.White, fontWeight = FontWeight.Bold, fontSize = 11.sp)
                                }
                            }

                            Spacer(modifier = Modifier.width(2.dp))

                            // Admin panel portal icon button
                            IconButton(
                                onClick = onNavigateToAdmin,
                                modifier = Modifier.testTag("home_admin_btn")
                            ) {
                                Icon(
                                    imageVector = Icons.Default.AdminPanelSettings,
                                    contentDescription = "Admin Dashboard",
                                    tint = Color.White
                                )
                            }
                        }
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = BharatDarkBlue)
            )
        },
        floatingActionButton = {
            // Fast profile creation for drivers
            FloatingActionButton(
                onClick = onNavigateToCreateDriver,
                containerColor = BharatGreen,
                contentColor = Color.White,
                shape = RoundedCornerShape(16.dp),
                modifier = Modifier
                    .padding(bottom = 12.dp)
                    .testTag("home_create_driver_fab")
            ) {
                Row(
                    modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(Icons.Default.Add, contentDescription = null)
                    Spacer(modifier = Modifier.width(6.dp))
                    Text("Driver Partner", fontWeight = FontWeight.Bold)
                }
            }
        }
    ) { innerPadding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .background(LightBackground)
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // Live drivers indicator banner: "Show 12 drivers available 5-7 min"
            item {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(containerColor = BharatGreen.copy(alpha = 0.10f)),
                    border = BorderStroke(1.dp, BharatGreen.copy(alpha = 0.4f))
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(14.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Box(
                                modifier = Modifier
                                    .size(10.dp)
                                    .clip(CircleShape)
                                    .background(BharatGreen)
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                text = "12 drivers available nearby",
                                fontWeight = FontWeight.Bold,
                                color = BharatGreen,
                                fontSize = 14.sp
                            )
                        }
                        Text(
                            text = "ETA 5-7 min",
                            fontWeight = FontWeight.SemiBold,
                            color = BharatDarkBlue,
                            fontSize = 12.sp
                        )
                    }
                }
            }

            // Permission Denied Card: "Enable Location" -> Opens App Settings
            if (!isLocationPermissionGranted) {
                item {
                    Card(
                        modifier = Modifier.fillMaxWidth().testTag("enable_location_card"),
                        shape = RoundedCornerShape(14.dp),
                        colors = CardDefaults.cardColors(containerColor = EmergencyRedContainer),
                        border = BorderStroke(1.dp, EmergencyRed)
                    ) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(12.dp),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Column(modifier = Modifier.weight(1f)) {
                                Text(
                                    text = "Location Permission Required",
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 13.sp,
                                    color = EmergencyRed
                                )
                                Text(
                                    text = "Enable GPS to auto-detect your exact pickup location.",
                                    fontSize = 11.sp,
                                    color = Color.DarkGray
                                )
                            }
                            Spacer(modifier = Modifier.width(8.dp))
                            Button(
                                onClick = {
                                    val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS).apply {
                                        data = Uri.fromParts("package", context.packageName, null)
                                    }
                                    context.startActivity(intent)
                                },
                                colors = ButtonDefaults.buttonColors(containerColor = EmergencyRed),
                                shape = RoundedCornerShape(8.dp),
                                modifier = Modifier.height(36.dp).testTag("enable_location_btn")
                            ) {
                                Text("Enable Location", fontSize = 11.sp, fontWeight = FontWeight.Bold)
                            }
                        }
                    }
                }
            }

            // Search Bar: "Where to?" with auto-detected address as hint
            item {
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable { onNavigateToFleetMap() }
                        .testTag("home_search_bar_card"),
                    shape = RoundedCornerShape(20.dp),
                    elevation = CardDefaults.cardElevation(defaultElevation = 8.dp),
                    border = BorderStroke(0.5.dp, CardBorderColor),
                    colors = CardDefaults.cardColors(containerColor = Color.White)
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 16.dp, vertical = 14.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            imageVector = Icons.Default.Search,
                            contentDescription = "Search",
                            tint = BharatOrange,
                            modifier = Modifier.size(24.dp)
                        )
                        Spacer(modifier = Modifier.width(12.dp))
                        Column(modifier = Modifier.weight(1f)) {
                            Text(
                                text = "Where to?",
                                style = MaterialTheme.typography.titleMedium.copy(
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 16.sp,
                                    color = BharatDarkBlue
                                )
                            )
                            Text(
                                text = if (currentAddress.isNotBlank()) currentAddress else "Where to? Search destination...",
                                style = MaterialTheme.typography.bodySmall.copy(
                                    color = Color.Gray,
                                    fontSize = 12.sp
                                ),
                                maxLines = 1,
                                overflow = TextOverflow.Ellipsis
                            )
                        }
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowForward,
                            contentDescription = null,
                            tint = BharatOrange,
                            modifier = Modifier.size(20.dp)
                        )
                    }
                }
            }

            // Below Search Bar: Row with Icon(my_location, color: Green) + Text "📍 Current Location - $currentAddress - Auto-detected" + Green dot pulse animation
            item {
                Card(
                    modifier = Modifier.fillMaxWidth().testTag("current_location_autodetect_row"),
                    shape = RoundedCornerShape(12.dp),
                    colors = CardDefaults.cardColors(containerColor = BharatGreen.copy(alpha = 0.08f)),
                    border = BorderStroke(1.dp, BharatGreen.copy(alpha = 0.35f))
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 12.dp, vertical = 10.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        // Green dot pulse animation
                        Box(
                            modifier = Modifier
                                .size(10.dp)
                                .clip(CircleShape)
                                .background(BharatGreen.copy(alpha = pulseAlpha))
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Icon(
                            imageVector = Icons.Default.MyLocation,
                            contentDescription = "Current Location",
                            tint = BharatGreen,
                            modifier = Modifier.size(18.dp)
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = "📍 Current Location - ${if (currentAddress.isNotBlank()) currentAddress else "Detecting GPS..."} - Auto-detected",
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Bold,
                            color = BharatDarkBlue,
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis,
                            modifier = Modifier.weight(1f)
                        )
                    }
                }
            }

            // Top Tagline: "Bike & Toto now available Explore new rides"
            item {
                Surface(
                    color = BharatOrange.copy(alpha = 0.10f),
                    shape = RoundedCornerShape(12.dp),
                    border = BorderStroke(1.dp, BharatOrange.copy(alpha = 0.35f)),
                    modifier = Modifier.fillMaxWidth().testTag("bike_toto_tagline_banner")
                ) {
                    Row(
                        modifier = Modifier.padding(horizontal = 14.dp, vertical = 10.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            imageVector = Icons.Default.ElectricRickshaw,
                            contentDescription = null,
                            tint = BharatGreen,
                            modifier = Modifier.size(22.dp)
                        )
                        Spacer(modifier = Modifier.width(10.dp))
                        Text(
                            text = "Bike & Toto now available • Explore new rides",
                            color = BharatDarkBlue,
                            fontWeight = FontWeight.Bold,
                            fontSize = 13.sp
                        )
                    }
                }
            }

            // Choose Service Header
            item {
                Text(
                    text = "Choose Service",
                    style = MaterialTheme.typography.titleMedium.copy(
                        fontWeight = FontWeight.Bold,
                        color = BharatDarkBlue,
                        fontSize = 16.sp
                    )
                )
            }

            // Row 1: Bike Taxi (orange), Toto E-Rickshaw (green), Auto Rickshaw (orange)
            item {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    HomeRideServiceCard(
                        modifier = Modifier.weight(1f),
                        title = "Bike Taxi",
                        subtitle = "Fast Affordable",
                        icon = Icons.Default.TwoWheeler,
                        iconColor = BharatOrange,
                        onClick = onNavigateToFleetMap
                    )
                    HomeRideServiceCard(
                        modifier = Modifier.weight(1f),
                        title = "Toto E-Rickshaw",
                        subtitle = "Eco Shared",
                        icon = Icons.Default.ElectricRickshaw,
                        iconColor = BharatGreen,
                        onClick = onNavigateToFleetMap
                    )
                    HomeRideServiceCard(
                        modifier = Modifier.weight(1f),
                        title = "Auto Rickshaw",
                        subtitle = "Popular Quick",
                        icon = Icons.Default.DirectionsTransit,
                        iconColor = BharatOrange,
                        onClick = onNavigateToFleetMap
                    )
                }
            }

            // Row 2: Mini Cab, Sedan, SUV
            item {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    HomeRideServiceCard(
                        modifier = Modifier.weight(1f),
                        title = "Mini Cab",
                        subtitle = "Budget 4 seats",
                        icon = Icons.Default.LocalTaxi,
                        iconColor = BharatDarkBlue,
                        onClick = onNavigateToFleetMap
                    )
                    HomeRideServiceCard(
                        modifier = Modifier.weight(1f),
                        title = "Sedan",
                        subtitle = "Comfort 4 seats",
                        icon = Icons.Default.DirectionsCar,
                        iconColor = BharatDarkBlue,
                        onClick = onNavigateToFleetMap
                    )
                    HomeRideServiceCard(
                        modifier = Modifier.weight(1f),
                        title = "SUV",
                        subtitle = "Spacious 6 seats",
                        icon = Icons.Default.AirportShuttle,
                        iconColor = BharatDarkBlue,
                        onClick = onNavigateToFleetMap
                    )
                }
            }

            // Section: Primary Service Category Cards (Rent A Car & Hire Driver)
            item {
                Text(
                    text = "Core Ride & Rental Services",
                    style = MaterialTheme.typography.titleMedium.copy(
                        fontWeight = FontWeight.Bold,
                        color = BharatDarkBlue,
                        fontSize = 16.sp
                    )
                )
            }

            // 1. Rent A Car Card (AC, SUV, Economy, Instant Booking)
            item {
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable { onNavigateToRentCar() }
                        .testTag("home_rent_car_btn"),
                    shape = RoundedCornerShape(20.dp),
                    elevation = CardDefaults.cardElevation(defaultElevation = 8.dp),
                    border = BorderStroke(0.5.dp, CardBorderColor),
                    colors = CardDefaults.cardColors(containerColor = Color.White)
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Box(
                            modifier = Modifier
                                .size(50.dp)
                                .clip(CircleShape)
                                .background(BharatOrange.copy(alpha = 0.15f)),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = Icons.Default.DirectionsCar,
                                contentDescription = null,
                                tint = BharatOrange,
                                modifier = Modifier.size(28.dp)
                            )
                        }
                        Spacer(modifier = Modifier.width(14.dp))
                        Column(modifier = Modifier.weight(1f)) {
                            Text(
                                text = "Rent A Car",
                                fontWeight = FontWeight.Bold,
                                fontSize = 17.sp,
                                color = BharatDarkBlue
                            )
                            Spacer(modifier = Modifier.height(2.dp))
                            Text(
                                text = "AC, SUV, Economy • Instant Booking",
                                fontSize = 13.sp,
                                color = Color.Gray
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

            // 2. Hire Driver Card (Hourly, Daily, Outstation)
            item {
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable { onNavigateToHireDriver() }
                        .testTag("home_hire_driver_btn"),
                    shape = RoundedCornerShape(20.dp),
                    elevation = CardDefaults.cardElevation(defaultElevation = 8.dp),
                    border = BorderStroke(0.5.dp, CardBorderColor),
                    colors = CardDefaults.cardColors(containerColor = Color.White)
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Box(
                            modifier = Modifier
                                .size(50.dp)
                                .clip(CircleShape)
                                .background(BharatGreen.copy(alpha = 0.15f)),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = Icons.Default.Person,
                                contentDescription = null,
                                tint = BharatGreen,
                                modifier = Modifier.size(28.dp)
                            )
                        }
                        Spacer(modifier = Modifier.width(14.dp))
                        Column(modifier = Modifier.weight(1f)) {
                            Text(
                                text = "Hire Driver",
                                fontWeight = FontWeight.Bold,
                                fontSize = 17.sp,
                                color = BharatDarkBlue
                            )
                            Spacer(modifier = Modifier.height(2.dp))
                            Text(
                                text = "Hourly, Daily, Outstation • Verified Pros",
                                fontSize = 13.sp,
                                color = Color.Gray
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

            // Section: Quick Services: Airport Transfer, Outstation, Hourly Rental
            item {
                Text(
                    text = "Quick Services",
                    style = MaterialTheme.typography.titleMedium.copy(
                        fontWeight = FontWeight.Bold,
                        color = BharatDarkBlue,
                        fontSize = 16.sp
                    )
                )
            }

            item {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    QuickServiceChip(
                        modifier = Modifier.weight(1f),
                        title = "Airport Transfer",
                        icon = Icons.Default.FlightTakeoff,
                        onClick = onNavigateToFleetMap
                    )
                    QuickServiceChip(
                        modifier = Modifier.weight(1f),
                        title = "Outstation",
                        icon = Icons.Default.Map,
                        onClick = onNavigateToFleetMap
                    )
                    QuickServiceChip(
                        modifier = Modifier.weight(1f),
                        title = "Hourly Rental",
                        icon = Icons.Default.Schedule,
                        onClick = onNavigateToRentCar
                    )
                }
            }

            // Auto QR Instant Payment Quick Banner
            item {
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable { onNavigateToAutoQr() }
                        .testTag("home_instant_auto_qr_card"),
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(containerColor = Color.White),
                    elevation = CardDefaults.cardElevation(defaultElevation = 6.dp),
                    border = BorderStroke(1.dp, BharatOrange.copy(alpha = 0.4f))
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(14.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Box(
                            modifier = Modifier
                                .size(46.dp)
                                .clip(CircleShape)
                                .background(BharatOrange.copy(alpha = 0.15f)),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(Icons.Default.QrCodeScanner, contentDescription = null, tint = BharatOrange, modifier = Modifier.size(24.dp))
                        }
                        Spacer(modifier = Modifier.width(12.dp))
                        Column(modifier = Modifier.weight(1f)) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Text("Auto QR Payment", fontWeight = FontWeight.Bold, color = BharatDarkBlue, fontSize = 15.sp)
                                Spacer(modifier = Modifier.width(6.dp))
                                Surface(
                                    color = BharatGreen.copy(alpha = 0.15f),
                                    shape = RoundedCornerShape(4.dp)
                                ) {
                                    Text("05:00 Timer", color = BharatGreen, fontSize = 9.sp, fontWeight = FontWeight.Bold, modifier = Modifier.padding(horizontal = 4.dp, vertical = 1.dp))
                                }
                            }
                            Text("Scan & Pay via GPay / PhonePe / Paytm", color = Color.Gray, fontSize = 11.sp)
                        }
                        Icon(Icons.AutoMirrored.Filled.ArrowForward, contentDescription = null, tint = BharatOrange, modifier = Modifier.size(18.dp))
                    }
                }
            }

            // Book City Ride CTA Button
            item {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(52.dp)
                        .clip(RoundedCornerShape(12.dp))
                        .background(
                            brush = Brush.horizontalGradient(
                                listOf(BharatOrange, BharatGreen)
                            )
                        )
                        .clickable { onNavigateToFleetMap() }
                        .testTag("home_book_city_ride_btn"),
                    contentAlignment = Alignment.Center
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(Icons.Default.DirectionsCar, contentDescription = null, tint = Color.White)
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = "Book Ride Now (View 12 Vehicles)",
                            color = Color.White,
                            fontWeight = FontWeight.Bold,
                            fontSize = 15.sp
                        )
                    }
                }
            }

            item {
                Spacer(modifier = Modifier.height(70.dp))
            }
        }
    }
}

@Composable
private fun HomeRideServiceCard(
    modifier: Modifier = Modifier,
    title: String,
    subtitle: String,
    icon: ImageVector,
    iconColor: Color,
    onClick: () -> Unit
) {
    Card(
        modifier = modifier
            .clickable { onClick() }
            .testTag("service_card_${title.replace(" ", "_").lowercase()}"),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 6.dp),
        border = BorderStroke(0.5.dp, CardBorderColor)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 12.dp, horizontal = 4.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Box(
                modifier = Modifier
                    .size(42.dp)
                    .clip(CircleShape)
                    .background(iconColor.copy(alpha = 0.15f)),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = icon,
                    contentDescription = title,
                    tint = iconColor,
                    modifier = Modifier.size(24.dp)
                )
            }
            Spacer(modifier = Modifier.height(6.dp))
            Text(
                text = title,
                fontSize = 12.sp,
                fontWeight = FontWeight.Bold,
                color = BharatDarkBlue,
                maxLines = 1,
                textAlign = TextAlign.Center
            )
            Spacer(modifier = Modifier.height(2.dp))
            Text(
                text = subtitle,
                fontSize = 10.sp,
                color = Color.Gray,
                maxLines = 1,
                fontWeight = FontWeight.Medium,
                textAlign = TextAlign.Center
            )
        }
    }
}

@Composable
private fun QuickServiceChip(
    modifier: Modifier = Modifier,
    title: String,
    icon: ImageVector,
    onClick: () -> Unit
) {
    Card(
        modifier = modifier.clickable { onClick() },
        shape = RoundedCornerShape(14.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
        border = BorderStroke(0.5.dp, CardBorderColor)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 12.dp, horizontal = 6.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Icon(
                imageVector = icon,
                contentDescription = title,
                tint = BharatDarkBlue,
                modifier = Modifier.size(24.dp)
            )
            Spacer(modifier = Modifier.height(6.dp))
            Text(
                text = title,
                fontSize = 11.sp,
                fontWeight = FontWeight.Bold,
                color = BharatDarkBlue,
                maxLines = 1
            )
        }
    }
}
