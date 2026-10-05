package com.example.ui.screens

import android.widget.Toast
import androidx.activity.compose.BackHandler
import androidx.compose.animation.*
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
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
import com.example.ui.theme.*
import com.example.viewmodel.MainViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AdminDashboardScreen(
    viewModel: MainViewModel,
    onBack: () -> Unit
) {
    BackHandler { onBack() }
    val context = LocalContext.current

    val pendingCount by viewModel.pendingVerificationCount.collectAsState()
    val silentNotification by viewModel.silentNotification.collectAsState()
    val driversCount by viewModel.adminDriversCount.collectAsState()
    val verifiedCount by viewModel.adminVerifiedCount.collectAsState()
    val blockedCount by viewModel.adminBlockedCount.collectAsState()
    val earningsToday by viewModel.adminEarningsToday.collectAsState()
    val bookingsToday by viewModel.adminBookingsToday.collectAsState()

    var showBlockDriverDialog by remember { mutableStateOf(false) }

    if (showBlockDriverDialog) {
        AlertDialog(
            onDismissRequest = { showBlockDriverDialog = false },
            title = { Text("Confirm Driver Block", color = EmergencyRed, fontWeight = FontWeight.Bold) },
            text = { Text("Are you sure you want to block driver Rajesh Kumar (DL-04-2015-8819)? They will be temporarily suspended from accepting rides pending investigation.") },
            confirmButton = {
                Button(
                    onClick = {
                        viewModel.adminBlockedCount.value += 1
                        showBlockDriverDialog = false
                        Toast.makeText(context, "Driver suspended successfully.", Toast.LENGTH_SHORT).show()
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = EmergencyRed)
                ) {
                    Text("Confirm Block")
                }
            },
            dismissButton = {
                TextButton(onClick = { showBlockDriverDialog = false }) {
                    Text("Cancel")
                }
            }
        )
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Column {
                        Text("Admin Dashboard", fontWeight = FontWeight.Bold, fontSize = 18.sp, color = Color.White)
                        Text("bm427251@gmail.com • Internal System", fontSize = 11.sp, color = BharatOrangeLight)
                    }
                },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back", tint = Color.White)
                    }
                },
                actions = {
                    IconButton(onClick = {
                        Toast.makeText(context, "Admin settings & security logs opened", Toast.LENGTH_SHORT).show()
                    }) {
                        Icon(Icons.Default.Settings, contentDescription = "Settings", tint = Color.White)
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = BharatDarkBlue)
            )
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
            // Special Requirement B: Silent Push Notification Banner (Arrives within 7s)
            item {
                AnimatedVisibility(
                    visible = silentNotification != null,
                    enter = fadeIn() + expandVertically(),
                    exit = fadeOut() + shrinkVertically()
                ) {
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(16.dp),
                        colors = CardDefaults.cardColors(containerColor = BharatOrange.copy(alpha = 0.15f)),
                        border = BorderStroke(1.dp, BharatOrange)
                    ) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(14.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Box(
                                modifier = Modifier
                                    .size(36.dp)
                                    .clip(CircleShape)
                                    .background(BharatOrange),
                                contentAlignment = Alignment.Center
                            ) {
                                Icon(Icons.Default.NotificationsActive, contentDescription = null, tint = Color.White, modifier = Modifier.size(20.dp))
                            }
                            Spacer(modifier = Modifier.width(12.dp))
                            Column(modifier = Modifier.weight(1f)) {
                                Text(
                                    text = "Silent Push: Partner Submitted (7s)",
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 13.sp,
                                    color = BharatDarkBlue
                                )
                                Text(
                                    text = silentNotification?.message ?: "",
                                    fontSize = 11.sp,
                                    color = Color.DarkGray
                                )
                            }
                            IconButton(onClick = { viewModel.dismissSilentNotification() }) {
                                Icon(Icons.Default.Close, contentDescription = "Dismiss", tint = Color.Gray, modifier = Modifier.size(18.dp))
                            }
                        }
                    }
                }
            }

            // Internal Earnings & 15% Commission Split (ONLY HERE - NEVER IN CUSTOMER UI)
            item {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(20.dp),
                    colors = CardDefaults.cardColors(containerColor = BharatDarkBlue),
                    elevation = CardDefaults.cardElevation(defaultElevation = 8.dp)
                ) {
                    Column(
                        modifier = Modifier.padding(18.dp),
                        verticalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = "Today's Gross Platform Turnover",
                                color = Color.White.copy(alpha = 0.8f),
                                fontSize = 13.sp
                            )
                            Surface(
                                color = BharatOrange,
                                shape = RoundedCornerShape(6.dp)
                            ) {
                                Text(
                                    text = "INTERNAL 15%",
                                    color = Color.White,
                                    fontSize = 10.sp,
                                    fontWeight = FontWeight.Bold,
                                    modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                                )
                            }
                        }

                        Text(
                            text = "₹$earningsToday",
                            fontSize = 32.sp,
                            fontWeight = FontWeight.Black,
                            color = Color.White
                        )

                        HorizontalDivider(color = Color.White.copy(alpha = 0.2f))

                        // Internal Commission Breakdown
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Column {
                                Text("Company (15% Commission)", fontSize = 11.sp, color = BharatOrangeLight)
                                Text("₹${viewModel.companyCommission}", fontSize = 18.sp, fontWeight = FontWeight.Bold, color = Color.White)
                            }
                            Column(horizontalAlignment = Alignment.End) {
                                Text("Driver Payout (85%)", fontSize = 11.sp, color = BharatGreenLight)
                                Text("₹${viewModel.driverPayout}", fontSize = 18.sp, fontWeight = FontWeight.Bold, color = Color.White)
                            }
                        }
                    }
                }
            }

            // Stats Cards: Drivers 24, Verified 22, Pending 2, Blocked 1
            item {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    AdminStatCard(
                        modifier = Modifier.weight(1f),
                        title = "Total Drivers",
                        count = "$driversCount",
                        badge = "Registered",
                        badgeColor = BharatDarkBlue
                    )
                    AdminStatCard(
                        modifier = Modifier.weight(1f),
                        title = "Verified",
                        count = "$verifiedCount",
                        badge = "92% Active",
                        badgeColor = BharatGreen
                    )
                }
            }

            item {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    AdminStatCard(
                        modifier = Modifier.weight(1f),
                        title = "Pending Review",
                        count = "$pendingCount",
                        badge = "Review Now",
                        badgeColor = BharatOrange
                    )
                    AdminStatCard(
                        modifier = Modifier.weight(1f),
                        title = "Blocked Drivers",
                        count = "$blockedCount",
                        badge = "Actioned",
                        badgeColor = EmergencyRed
                    )
                }
            }

            // Bookings Today: 18 Bar Chart Simulation
            item {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(20.dp),
                    colors = CardDefaults.cardColors(containerColor = Color.White),
                    elevation = CardDefaults.cardElevation(defaultElevation = 6.dp),
                    border = BorderStroke(0.5.dp, CardBorderColor)
                ) {
                    Column(
                        modifier = Modifier.padding(16.dp),
                        verticalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text("Bookings Today ($bookingsToday completed)", fontWeight = FontWeight.Bold, fontSize = 15.sp, color = BharatDarkBlue)
                            Text("Hourly volume", fontSize = 11.sp, color = Color.Gray)
                        }

                        // Simulated Hourly Bar Chart
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(90.dp),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.Bottom
                        ) {
                            val barHeights = listOf(20.dp, 35.dp, 60.dp, 80.dp, 65.dp, 45.dp, 75.dp)
                            val barLabels = listOf("8am", "10am", "12pm", "2pm", "4pm", "6pm", "Now")
                            barHeights.forEachIndexed { i, h ->
                                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                    Box(
                                        modifier = Modifier
                                            .width(24.dp)
                                            .height(h)
                                            .clip(RoundedCornerShape(topStart = 6.dp, topEnd = 6.dp))
                                            .background(if (i == 3 || i == 6) BharatGreen else BharatDarkBlue)
                                    )
                                    Spacer(modifier = Modifier.height(4.dp))
                                    Text(barLabels[i], fontSize = 10.sp, color = Color.Gray)
                                }
                            }
                        }
                    }
                }
            }

            // Driver Management & Compliance Actions
            item {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(20.dp),
                    colors = CardDefaults.cardColors(containerColor = Color.White),
                    elevation = CardDefaults.cardElevation(defaultElevation = 6.dp),
                    border = BorderStroke(0.5.dp, CardBorderColor)
                ) {
                    Column(
                        modifier = Modifier.padding(16.dp),
                        verticalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        Text("Driver Management & Compliance", fontWeight = FontWeight.Bold, fontSize = 15.sp, color = BharatDarkBlue)

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Column {
                                Text("Rajesh Kumar (Sedan)", fontWeight = FontWeight.Bold, fontSize = 14.sp)
                                Text("Flagged for cancellation • DL: DL-04-2015-8819", fontSize = 11.sp, color = Color.Gray)
                            }
                            Button(
                                onClick = { showBlockDriverDialog = true },
                                colors = ButtonDefaults.buttonColors(containerColor = EmergencyRed),
                                shape = RoundedCornerShape(8.dp),
                                modifier = Modifier.height(36.dp).testTag("block_driver_action_btn")
                            ) {
                                Text("Block", fontSize = 12.sp, fontWeight = FontWeight.Bold)
                            }
                        }

                        HorizontalDivider(color = CardBorderColor)

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Icon(Icons.Default.Security, contentDescription = null, tint = BharatGreen, modifier = Modifier.size(18.dp))
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                text = "Background check & police verification 100% compliant",
                                fontSize = 12.sp,
                                color = BharatGreen,
                                fontWeight = FontWeight.SemiBold
                            )
                        }
                    }
                }
            }

            item {
                Spacer(modifier = Modifier.height(30.dp))
            }
        }
    }
}

@Composable
private fun AdminStatCard(
    modifier: Modifier = Modifier,
    title: String,
    count: String,
    badge: String,
    badgeColor: Color
) {
    Card(
        modifier = modifier,
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
        border = BorderStroke(0.5.dp, CardBorderColor)
    ) {
        Column(
            modifier = Modifier.padding(14.dp),
            verticalArrangement = Arrangement.spacedBy(6.dp)
        ) {
            Text(title, fontSize = 12.sp, color = Color.Gray, fontWeight = FontWeight.Medium)
            Text(count, fontSize = 24.sp, fontWeight = FontWeight.Black, color = BharatDarkBlue)
            Surface(
                color = badgeColor.copy(alpha = 0.15f),
                shape = RoundedCornerShape(4.dp)
            ) {
                Text(
                    text = badge,
                    color = badgeColor,
                    fontSize = 10.sp,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                )
            }
        }
    }
}
