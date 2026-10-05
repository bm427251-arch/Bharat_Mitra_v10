package com.example.ui.screens

import android.content.Context
import android.content.Intent
import android.media.AudioManager
import android.media.ToneGenerator
import android.net.Uri
import android.os.Build
import android.os.VibrationEffect
import android.os.Vibrator
import android.widget.Toast
import androidx.activity.compose.BackHandler
import androidx.compose.animation.*
import androidx.compose.animation.core.*
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
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
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.zIndex
import com.example.ui.components.BharatMitraHeaderLogo
import com.example.ui.components.InteractiveMapCanvas
import com.example.ui.theme.*
import com.example.viewmodel.MainViewModel
import java.util.Calendar

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RideInProgressScreen(
    viewModel: MainViewModel,
    onBack: () -> Unit,
    onFinishRideAndRate: () -> Unit
) {
    BackHandler { onBack() }
    val context = LocalContext.current

    // Live GPS state
    val currentLocation by viewModel.currentLocation.collectAsState()
    val currentAddress by viewModel.currentAddress.collectAsState()

    // Speedometer state (km/h)
    // Calculates from GPS speed (pos.speed * 3.6) if available, with interactive simulator controls
    var currentSpeedKmh by remember { mutableDoubleStateOf(48.0) }
    val speedLimitKmh = 60.0

    // Auto-update from real GPS speed if device is moving
    LaunchedEffect(currentLocation) {
        val speed = currentLocation?.speed
        if (speed != null && speed > 0.5f) {
            currentSpeedKmh = (speed * 3.6).toDouble()
        }
    }

    // Night mode check: auto-dark if hour >= 19
    val currentHour = remember { Calendar.getInstance().get(Calendar.HOUR_OF_DAY) }
    var isNightMode by remember { mutableStateOf(currentHour >= 19) }

    // Dialog & Alert states
    var showSosDialog by remember { mutableStateOf(false) }
    var showRouteDeviationDialog by remember { mutableStateOf(false) }
    var isSimulatingDeviation by remember { mutableStateOf(false) }

    val isOverspeed = currentSpeedKmh > speedLimitKmh
    val isCriticalOverspeed = currentSpeedKmh > 75.0

    // Trigger vibration & audio warnings on overspeed
    LaunchedEffect(isOverspeed, isCriticalOverspeed) {
        if (isOverspeed) {
            viewModel.triggerOverspeedIncident("Rohan Kumar", currentSpeedKmh)
            try {
                val vibrator = context.getSystemService(Context.VIBRATOR_SERVICE) as? Vibrator
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                    vibrator?.vibrate(VibrationEffect.createOneShot(350, VibrationEffect.DEFAULT_AMPLITUDE))
                } else {
                    @Suppress("DEPRECATION")
                    vibrator?.vibrate(350)
                }
            } catch (_: Exception) {}

            if (isCriticalOverspeed) {
                try {
                    val toneGen = ToneGenerator(AudioManager.STREAM_ALARM, 90)
                    toneGen.startTone(ToneGenerator.TONE_CDMA_ALERT_CALL_GUARD, 250)
                } catch (_: Exception) {}
            }
        }
    }

    // SOS Emergency Alert Dialog
    if (showSosDialog) {
        AlertDialog(
            onDismissRequest = { showSosDialog = false },
            title = {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(Icons.Default.Warning, contentDescription = null, tint = EmergencyRed)
                    Spacer(modifier = Modifier.width(8.dp))
                    Text("EMERGENCY SOS ALERT", color = EmergencyRed, fontWeight = FontWeight.Black)
                }
            },
            text = {
                Text(
                    "Emergency SOS will immediately broadcast your live GPS telemetry to the 24/7 Police Control Room and send an instant WhatsApp tracking link to your emergency contacts.",
                    fontSize = 13.sp
                )
            },
            confirmButton = {
                Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    Button(
                        onClick = {
                            showSosDialog = false
                            val dialIntent = Intent(Intent.ACTION_DIAL, Uri.parse("tel:112"))
                            context.startActivity(dialIntent)
                        },
                        colors = ButtonDefaults.buttonColors(containerColor = EmergencyRed),
                        modifier = Modifier.fillMaxWidth().testTag("sos_dial_police_btn")
                    ) {
                        Icon(Icons.Default.Phone, contentDescription = null)
                        Spacer(modifier = Modifier.width(6.dp))
                        Text("DIAL 112 (POLICE CONTROL)", fontWeight = FontWeight.Bold)
                    }

                    Button(
                        onClick = {
                            showSosDialog = false
                            val lat = currentLocation?.latitude ?: 22.5726
                            val lng = currentLocation?.longitude ?: 88.3639
                            val message = "EMERGENCY SOS: I need help! My live ride location on Bharat Mitra: https://bharatmitra.app/trip/live?lat=$lat&lng=$lng"
                            val sendIntent = Intent(Intent.ACTION_VIEW).apply {
                                data = Uri.parse("https://api.whatsapp.com/send?text=${Uri.encode(message)}")
                            }
                            context.startActivity(sendIntent)
                        },
                        colors = ButtonDefaults.buttonColors(containerColor = BharatGreen),
                        modifier = Modifier.fillMaxWidth().testTag("sos_whatsapp_share_btn")
                    ) {
                        Icon(Icons.Default.ShareLocation, contentDescription = null)
                        Spacer(modifier = Modifier.width(6.dp))
                        Text("SHARE ON WHATSAPP & SMS", fontWeight = FontWeight.Bold)
                    }
                }
            },
            dismissButton = {
                TextButton(onClick = { showSosDialog = false }) {
                    Text("Dismiss", color = Color.Gray)
                }
            }
        )
    }

    // Route Deviation Alert Dialog (>500m off polyline)
    if (showRouteDeviationDialog) {
        AlertDialog(
            onDismissRequest = { showRouteDeviationDialog = false },
            title = {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(Icons.Default.AltRoute, contentDescription = null, tint = Color(0xFFFF6B00))
                    Spacer(modifier = Modifier.width(8.dp))
                    Text("ROUTE DEVIATION DETECTED", color = Color(0xFFFF6B00), fontWeight = FontWeight.Black)
                }
            },
            text = {
                Text(
                    "Vehicle is 580 meters away from the recommended trip polyline. Would you like to alert the 24/7 Bharat Mitra safety desk or ask the driver?",
                    fontSize = 13.sp
                )
            },
            confirmButton = {
                Button(
                    onClick = {
                        showRouteDeviationDialog = false
                        Toast.makeText(context, "Safety team alerted of route change", Toast.LENGTH_SHORT).show()
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFFF6B00))
                ) {
                    Text("NOTIFY SAFETY DESK", fontWeight = FontWeight.Bold)
                }
            },
            dismissButton = {
                TextButton(onClick = { showRouteDeviationDialog = false }) {
                    Text("Driver Took Detour (OK)", color = Color.Gray)
                }
            }
        )
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(if (isNightMode) Color(0xFF10141E) else LightBackground)
    ) {
        // 1. FULL SCREEN MAP LAYER
        InteractiveMapCanvas(
            modifier = Modifier.fillMaxSize(),
            originName = if (currentAddress.isNotBlank()) currentAddress else "Your Location",
            destinationName = "Salt Lake Sector V, Kolkata",
            isDeviated = isSimulatingDeviation
        )

        // 2. TOP OVERSPEED BANNER (if speed > 60 km/h: Orange #FF6B00)
        AnimatedVisibility(
            visible = isOverspeed,
            enter = slideInVertically() + fadeIn(),
            exit = slideOutVertically() + fadeOut(),
            modifier = Modifier
                .fillMaxWidth()
                .align(Alignment.TopCenter)
                .statusBarsPadding()
                .zIndex(10f)
        ) {
            Surface(
                color = Color(0xFFFF6B00),
                modifier = Modifier
                    .fillMaxWidth()
                    .testTag("overspeed_warning_banner")
            ) {
                Row(
                    modifier = Modifier.padding(horizontal = 16.dp, vertical = 10.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        imageVector = Icons.Default.Campaign,
                        contentDescription = "Overspeed Alert",
                        tint = Color.White,
                        modifier = Modifier.size(24.dp)
                    )
                    Spacer(modifier = Modifier.width(10.dp))
                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            text = "⚠️ Overspeed Alert - Driver is overspeeding ${currentSpeedKmh.toInt()} km/h - Safe Driving Alert",
                            color = Color.White,
                            fontWeight = FontWeight.Black,
                            fontSize = 12.sp
                        )
                        Text(
                            text = "Logged to Firestore & silent push dispatched to admin within 7 seconds.",
                            color = Color.White.copy(alpha = 0.9f),
                            fontSize = 10.sp
                        )
                    }
                }
            }
        }

        // 3. TOP BAR: BHARAT MITRA logo left, Back, Night Mode Toggle, SOS red button right
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .statusBarsPadding()
                .padding(top = if (isOverspeed) 56.dp else 12.dp, start = 12.dp, end = 12.dp)
                .align(Alignment.TopCenter)
                .zIndex(5f),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Left: Back button + BHARAT MITRA Logo
            Card(
                shape = RoundedCornerShape(14.dp),
                colors = CardDefaults.cardColors(containerColor = BharatDarkBlue.copy(alpha = 0.92f)),
                elevation = CardDefaults.cardElevation(defaultElevation = 6.dp)
            ) {
                Row(
                    modifier = Modifier.padding(horizontal = 8.dp, vertical = 6.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    IconButton(
                        onClick = onBack,
                        modifier = Modifier.size(32.dp)
                    ) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Back",
                            tint = Color.White,
                            modifier = Modifier.size(20.dp)
                        )
                    }
                    Spacer(modifier = Modifier.width(4.dp))
                    // BHARAT MITRA static logo lock (120x40 equivalent)
                    BharatMitraHeaderLogo(width = 100.dp, height = 30.dp)
                }
            }

            // Right: Night Mode Toggle & SOS Red Button
            Row(verticalAlignment = Alignment.CenterVertically) {
                IconButton(
                    onClick = { isNightMode = !isNightMode },
                    modifier = Modifier
                        .size(40.dp)
                        .clip(CircleShape)
                        .background(Color.White.copy(alpha = 0.9f))
                        .testTag("night_mode_toggle_btn")
                ) {
                    Icon(
                        imageVector = if (isNightMode) Icons.Default.DarkMode else Icons.Default.LightMode,
                        contentDescription = "Toggle Night Mode",
                        tint = if (isNightMode) BharatDarkBlue else BharatOrange
                    )
                }

                Spacer(modifier = Modifier.width(8.dp))

                // Red SOS Button
                Surface(
                    modifier = Modifier
                        .clip(RoundedCornerShape(12.dp))
                        .clickable { showSosDialog = true }
                        .testTag("sos_red_button"),
                    color = EmergencyRed,
                    shape = RoundedCornerShape(12.dp),
                    shadowElevation = 6.dp
                ) {
                    Row(
                        modifier = Modifier.padding(horizontal = 14.dp, vertical = 8.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(Icons.Default.Emergency, contentDescription = null, tint = Color.White, modifier = Modifier.size(20.dp))
                        Spacer(modifier = Modifier.width(4.dp))
                        Text("SOS", color = Color.White, fontWeight = FontWeight.Black, fontSize = 14.sp)
                    }
                }
            }
        }

        // 4. LEFT MIDDLE: SPEED CARD (Red if overspeed else Green, Speed Limit 60 km/h)
        Card(
            modifier = Modifier
                .padding(start = 16.dp, top = if (isOverspeed) 130.dp else 80.dp)
                .align(Alignment.TopStart)
                .zIndex(4f)
                .testTag("speed_indicator_card"),
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(containerColor = Color.White.copy(alpha = 0.96f)),
            elevation = CardDefaults.cardElevation(defaultElevation = 8.dp),
            border = BorderStroke(
                2.dp,
                if (isOverspeed) Color(0xFFFF6B00) else BharatGreen
            )
        ) {
            Column(
                modifier = Modifier.padding(horizontal = 14.dp, vertical = 10.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Box(
                        modifier = Modifier
                            .size(10.dp)
                            .clip(CircleShape)
                            .background(if (isOverspeed) Color(0xFFFF6B00) else BharatGreen)
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(
                        text = "${currentSpeedKmh.toInt()}",
                        fontSize = 24.sp,
                        fontWeight = FontWeight.Black,
                        color = if (isOverspeed) EmergencyRed else BharatGreen
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(
                        text = "km/h",
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.Gray
                    )
                }
                Spacer(modifier = Modifier.height(2.dp))
                Text(
                    text = "Limit: 60 km/h",
                    fontSize = 10.sp,
                    color = Color.DarkGray,
                    fontWeight = FontWeight.SemiBold
                )

                // Interactive Speed test chips for QA / reviewer in emulator
                Spacer(modifier = Modifier.height(6.dp))
                Row(horizontalArrangement = Arrangement.spacedBy(4.dp)) {
                    SpeedTestChip("45", currentSpeedKmh == 45.0) { currentSpeedKmh = 45.0 }
                    SpeedTestChip("68", currentSpeedKmh == 68.0) { currentSpeedKmh = 68.0 }
                    SpeedTestChip("82", currentSpeedKmh == 82.0) { currentSpeedKmh = 82.0 }
                }
            }
        }

        // Quick Route Deviation Test Button (on right middle)
        Card(
            modifier = Modifier
                .padding(end = 16.dp, top = if (isOverspeed) 130.dp else 80.dp)
                .align(Alignment.TopEnd)
                .clickable {
                    isSimulatingDeviation = !isSimulatingDeviation
                    if (isSimulatingDeviation) {
                        showRouteDeviationDialog = true
                    }
                }
                .zIndex(4f)
                .testTag("simulate_deviation_btn"),
            shape = RoundedCornerShape(12.dp),
            colors = CardDefaults.cardColors(containerColor = Color.White.copy(alpha = 0.92f)),
            elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
            border = BorderStroke(1.dp, CardBorderColor)
        ) {
            Row(
                modifier = Modifier.padding(horizontal = 8.dp, vertical = 6.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    imageVector = Icons.Default.AltRoute,
                    contentDescription = null,
                    tint = if (isSimulatingDeviation) Color(0xFFFF6B00) else Color.Gray,
                    modifier = Modifier.size(16.dp)
                )
                Spacer(modifier = Modifier.width(4.dp))
                Text(
                    text = if (isSimulatingDeviation) "Off Route" else "On Track",
                    fontSize = 10.sp,
                    fontWeight = FontWeight.Bold,
                    color = if (isSimulatingDeviation) Color(0xFFFF6B00) else Color.DarkGray
                )
            }
        }

        // 5. BOTTOM SECTION: Driver Card & Voice Nav Text
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .align(Alignment.BottomCenter)
                .navigationBarsPadding()
                .padding(16.dp)
                .zIndex(5f),
            verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            // Driver Card: Photo, Name, Number, ETA, Share Live Trip, Call, End Ride
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .testTag("driver_tracking_bottom_card"),
                shape = RoundedCornerShape(20.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White),
                elevation = CardDefaults.cardElevation(defaultElevation = 10.dp),
                border = BorderStroke(0.5.dp, CardBorderColor)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        // Driver Photo Avatar
                        Box(
                            modifier = Modifier
                                .size(50.dp)
                                .clip(CircleShape)
                                .background(BharatDarkBlue.copy(alpha = 0.1f)),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = Icons.Default.Person,
                                contentDescription = "Driver Avatar",
                                tint = BharatDarkBlue,
                                modifier = Modifier.size(32.dp)
                            )
                        }

                        Spacer(modifier = Modifier.width(12.dp))

                        // Driver Details
                        Column(modifier = Modifier.weight(1f)) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Text(
                                    text = "Rohan Kumar",
                                    fontWeight = FontWeight.Black,
                                    fontSize = 16.sp,
                                    color = BharatDarkBlue
                                )
                                Spacer(modifier = Modifier.width(6.dp))
                                Text(
                                    text = "4.8 ★",
                                    fontSize = 12.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = BharatOrange
                                )
                            }
                            Text(
                                text = "Toyota Sedan • DL 01 AB 1234",
                                fontSize = 12.sp,
                                color = Color.Gray,
                                fontWeight = FontWeight.Medium
                            )
                            Text(
                                text = "Arriving in 3 mins • 1.1 km away",
                                fontSize = 12.sp,
                                color = BharatGreen,
                                fontWeight = FontWeight.Bold
                            )
                        }

                        // Call Driver Button
                        IconButton(
                            onClick = {
                                val callIntent = Intent(Intent.ACTION_DIAL, Uri.parse("tel:+919876543210"))
                                context.startActivity(callIntent)
                            },
                            modifier = Modifier
                                .size(44.dp)
                                .clip(CircleShape)
                                .background(BharatGreen.copy(alpha = 0.15f))
                                .testTag("call_driver_btn")
                        ) {
                            Icon(
                                imageVector = Icons.Default.Call,
                                contentDescription = "Call Driver",
                                tint = BharatGreen
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(12.dp))
                    HorizontalDivider(color = CardBorderColor)
                    Spacer(modifier = Modifier.height(12.dp))

                    // Buttons: Share Live Trip & End Ride & Rate
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        // Share Live Trip button (generates dynamic link with live lat lng)
                        OutlinedButton(
                            onClick = {
                                val lat = currentLocation?.latitude ?: 22.5726
                                val lng = currentLocation?.longitude ?: 88.3639
                                val tripLink = "https://bharatmitra.app/trip/live?lat=$lat&lng=$lng&driver=rohan"
                                val shareIntent = Intent(Intent.ACTION_SEND).apply {
                                    type = "text/plain"
                                    putExtra(Intent.EXTRA_SUBJECT, "Bharat Mitra Live Ride Tracking")
                                    putExtra(
                                        Intent.EXTRA_TEXT,
                                        "Track my live Bharat Mitra ride with Rohan Kumar (DL 01 AB 1234): $tripLink"
                                    )
                                }
                                context.startActivity(Intent.createChooser(shareIntent, "Share Live Trip"))
                            },
                            modifier = Modifier
                                .weight(1f)
                                .height(46.dp)
                                .testTag("share_live_trip_btn"),
                            shape = RoundedCornerShape(12.dp),
                            colors = ButtonDefaults.buttonColors(containerColor = BharatDarkBlue)
                        ) {
                            Icon(
                                imageVector = Icons.Default.Share,
                                contentDescription = null,
                                tint = Color.White,
                                modifier = Modifier.size(18.dp)
                            )
                            Spacer(modifier = Modifier.width(6.dp))
                            Text(
                                text = "Share Live Trip",
                                color = Color.White,
                                fontWeight = FontWeight.Bold,
                                fontSize = 12.sp,
                                maxLines = 1
                            )
                        }

                        // End Ride & Rate button
                        Button(
                            onClick = onFinishRideAndRate,
                            modifier = Modifier
                                .weight(1f)
                                .height(46.dp)
                                .testTag("finish_ride_rate_btn"),
                            shape = RoundedCornerShape(12.dp),
                            colors = ButtonDefaults.buttonColors(containerColor = BharatOrange)
                        ) {
                            Icon(
                                imageVector = Icons.Default.CheckCircle,
                                contentDescription = null,
                                tint = Color.White,
                                modifier = Modifier.size(18.dp)
                            )
                            Spacer(modifier = Modifier.width(6.dp))
                            Text(
                                text = "End Ride & Rate",
                                color = Color.White,
                                fontWeight = FontWeight.Bold,
                                fontSize = 12.sp,
                                maxLines = 1
                            )
                        }
                    }
                }
            }

            // Bottom-most: Voice Nav Text
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .testTag("voice_nav_banner"),
                shape = RoundedCornerShape(12.dp),
                colors = CardDefaults.cardColors(containerColor = BharatDarkBlue),
                elevation = CardDefaults.cardElevation(defaultElevation = 6.dp)
            ) {
                Row(
                    modifier = Modifier.padding(horizontal = 14.dp, vertical = 10.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        imageVector = Icons.Default.VolumeUp,
                        contentDescription = "Voice Navigation",
                        tint = BharatOrangeLight,
                        modifier = Modifier.size(20.dp)
                    )
                    Spacer(modifier = Modifier.width(10.dp))
                    Text(
                        text = "In 300m, turn left onto AJC Bose Rd",
                        color = Color.White,
                        fontSize = 12.sp,
                        fontWeight = FontWeight.SemiBold,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis,
                        modifier = Modifier.weight(1f)
                    )
                }
            }
        }
    }
}

@Composable
private fun SpeedTestChip(
    speedText: String,
    isSelected: Boolean,
    onClick: () -> Unit
) {
    Surface(
        modifier = Modifier
            .clip(RoundedCornerShape(6.dp))
            .clickable { onClick() },
        color = if (isSelected) BharatDarkBlue else Color(0xFFF1F5F9),
        shape = RoundedCornerShape(6.dp)
    ) {
        Text(
            text = speedText,
            fontSize = 9.sp,
            fontWeight = FontWeight.Bold,
            color = if (isSelected) Color.White else Color.DarkGray,
            modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
        )
    }
}
