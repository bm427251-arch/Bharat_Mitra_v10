package com.example.ui.screens

import android.content.Intent
import android.net.Uri
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
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
import com.example.model.ScreenState
import com.example.ui.components.InteractiveMapCanvas
import com.example.ui.theme.EmergencyRed
import com.example.ui.theme.IndianGreen
import com.example.ui.theme.SaffronPrimary
import com.example.viewmodel.MainViewModel

@Composable
fun ActiveRideTrackingScreen(
    viewModel: MainViewModel,
    onBackToHome: () -> Unit
) {
    val context = LocalContext.current
    val activeDriver by viewModel.activeRideDriver.collectAsState()
    val rideOtp by viewModel.rideOtp.collectAsState()
    val pickup by viewModel.pickupLocation.collectAsState()
    val drop by viewModel.dropLocation.collectAsState()
    val rideOption by viewModel.selectedRideOption.collectAsState()
    val calculatedFare by viewModel.calculatedFare.collectAsState()

    val driver = activeDriver ?: return

    Scaffold(
        bottomBar = {
            Surface(
                color = MaterialTheme.colorScheme.surface,
                shadowElevation = 8.dp,
                shape = RoundedCornerShape(topStart = 20.dp, topEnd = 20.dp)
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    // Status & OTP Header
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column {
                            Text(
                                text = "Driver Arriving in 3 mins",
                                style = MaterialTheme.typography.titleMedium.copy(
                                    fontWeight = FontWeight.ExtraBold,
                                    color = IndianGreen
                                )
                            )
                            Text(
                                text = "${rideOption.name} • Total Fare: ₹$calculatedFare",
                                fontSize = 12.sp,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }

                        // Ride OTP Badge
                        Surface(
                            color = SaffronPrimary.copy(alpha = 0.15f),
                            shape = RoundedCornerShape(10.dp),
                            border = androidx.compose.foundation.BorderStroke(1.5.dp, SaffronPrimary)
                        ) {
                            Column(
                                modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp),
                                horizontalAlignment = Alignment.CenterHorizontally
                            ) {
                                Text("START OTP", fontSize = 9.sp, fontWeight = FontWeight.Bold, color = SaffronPrimary)
                                Text(rideOtp, fontSize = 16.sp, fontWeight = FontWeight.ExtraBold, color = SaffronPrimary)
                            }
                        }
                    }

                    HorizontalDivider()

                    // Driver details
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Box(
                            modifier = Modifier
                                .size(48.dp)
                                .clip(CircleShape)
                                .background(MaterialTheme.colorScheme.primaryContainer),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = driver.avatarInitials,
                                fontWeight = FontWeight.Bold,
                                color = MaterialTheme.colorScheme.onPrimaryContainer
                            )
                        }

                        Spacer(modifier = Modifier.width(12.dp))

                        Column(modifier = Modifier.weight(1f)) {
                            Text(driver.name, fontWeight = FontWeight.Bold, fontSize = 14.sp)
                            Text(
                                text = "${driver.vehicleModel} • ${driver.rcNumber}",
                                fontSize = 12.sp,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Icon(Icons.Default.Star, contentDescription = null, tint = Color(0xFFD97706), modifier = Modifier.size(13.dp))
                                Spacer(modifier = Modifier.width(2.dp))
                                Text("${driver.rating}", fontSize = 11.sp, fontWeight = FontWeight.Bold)
                            }
                        }

                        // Call Driver Button
                        IconButton(
                            onClick = {
                                val intent = Intent(Intent.ACTION_DIAL, Uri.parse("tel:${driver.phone}"))
                                context.startActivity(intent)
                            },
                            modifier = Modifier
                                .size(42.dp)
                                .clip(CircleShape)
                                .background(IndianGreen.copy(alpha = 0.15f))
                                .testTag("active_ride_call_driver")
                        ) {
                            Icon(Icons.Default.Phone, contentDescription = "Call", tint = IndianGreen, modifier = Modifier.size(20.dp))
                        }
                    }

                    // Route Summary
                    Surface(
                        color = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f),
                        shape = RoundedCornerShape(10.dp),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Column(modifier = Modifier.padding(10.dp), verticalArrangement = Arrangement.spacedBy(4.dp)) {
                            Text("Pickup: $pickup", fontSize = 11.sp, maxLines = 1)
                            Text("Drop: $drop", fontSize = 11.sp, fontWeight = FontWeight.Bold, color = SaffronPrimary, maxLines = 1)
                        }
                    }

                    // Cancel Ride Button
                    OutlinedButton(
                        onClick = { viewModel.cancelActiveRide() },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(44.dp)
                            .testTag("cancel_active_ride_btn"),
                        colors = ButtonDefaults.outlinedButtonColors(contentColor = EmergencyRed),
                        border = androidx.compose.foundation.BorderStroke(1.dp, EmergencyRed)
                    ) {
                        Text("CANCEL RIDE", fontWeight = FontWeight.Bold)
                    }
                }
            }
        }
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            InteractiveMapCanvas(
                userGpsTitle = "Barasat",
                showEliteDots = false
            )
        }
    }
}
