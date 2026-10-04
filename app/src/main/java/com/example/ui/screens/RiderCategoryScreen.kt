package com.example.ui.screens

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
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
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.model.RiderProfile
import com.example.ui.theme.AshokaBlue
import com.example.ui.theme.IndianGreen
import com.example.ui.theme.SaffronPrimary
import com.example.viewmodel.MainViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RiderCategoryScreen(
    viewModel: MainViewModel,
    onBack: () -> Unit,
    onCreateProfile: (vehicleType: String) -> Unit
) {
    BackHandler { onBack() }

    val riders by viewModel.riders.collectAsState()
    var selectedVehicleType by remember { mutableStateOf("BIKE") }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Column {
                        Text("Rider Partner Category", fontWeight = FontWeight.Bold, fontSize = 18.sp)
                        Text("বাইক ও টোটো চালকদের পোর্টাল", fontSize = 11.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
                    }
                },
                navigationIcon = {
                    IconButton(onClick = onBack, modifier = Modifier.testTag("rider_back_btn")) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = MaterialTheme.colorScheme.surface)
            )
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = { onCreateProfile(selectedVehicleType) },
                containerColor = Color(0xFF1DB954),
                contentColor = Color.White,
                shape = RoundedCornerShape(16.dp),
                modifier = Modifier.testTag("rider_create_profile_fab")
            ) {
                Row(
                    modifier = Modifier.padding(horizontal = 16.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(Icons.Default.Add, contentDescription = "Add", modifier = Modifier.size(20.dp))
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
                .padding(horizontal = 16.dp),
            verticalArrangement = Arrangement.spacedBy(14.dp)
        ) {
            item {
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = "Select Rider Vehicle Type",
                    style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold)
                )
                Text(
                    text = "Choose your vehicle mode below to register your service profile",
                    fontSize = 12.sp,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }

            // 2 Sub-Cards: BIKE and TOTO
            item {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    // BIKE Card
                    Card(
                        modifier = Modifier
                            .weight(1f)
                            .clickable { selectedVehicleType = "BIKE" }
                            .testTag("rider_subcard_bike"),
                        shape = RoundedCornerShape(16.dp),
                        colors = CardDefaults.cardColors(
                            containerColor = if (selectedVehicleType == "BIKE") SaffronPrimary.copy(alpha = 0.12f) else MaterialTheme.colorScheme.surface
                        ),
                        border = BorderStroke(
                            2.dp,
                            if (selectedVehicleType == "BIKE") SaffronPrimary else MaterialTheme.colorScheme.outlineVariant
                        )
                    ) {
                        Column(
                            modifier = Modifier.padding(14.dp),
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Box(
                                modifier = Modifier
                                    .size(54.dp)
                                    .clip(CircleShape)
                                    .background(if (selectedVehicleType == "BIKE") SaffronPrimary else Color(0xFFF1F5F9)),
                                contentAlignment = Alignment.Center
                            ) {
                                Icon(
                                    Icons.Default.TwoWheeler,
                                    contentDescription = "Bike",
                                    tint = if (selectedVehicleType == "BIKE") Color.White else SaffronPrimary,
                                    modifier = Modifier.size(28.dp)
                                )
                            }
                            Spacer(modifier = Modifier.height(8.dp))
                            Text("BIKE", fontWeight = FontWeight.ExtraBold, fontSize = 16.sp)
                            Text("বাইক রাইডার", fontSize = 11.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
                            Spacer(modifier = Modifier.height(4.dp))
                            Surface(
                                color = if (selectedVehicleType == "BIKE") SaffronPrimary else Color(0xFFE2E8F0),
                                shape = RoundedCornerShape(6.dp)
                            ) {
                                Text(
                                    text = if (selectedVehicleType == "BIKE") "SELECTED" else "CHOOSE",
                                    fontSize = 10.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = if (selectedVehicleType == "BIKE") Color.White else Color.DarkGray,
                                    modifier = Modifier.padding(horizontal = 8.dp, vertical = 2.dp)
                                )
                            }
                        }
                    }

                    // TOTO Card
                    Card(
                        modifier = Modifier
                            .weight(1f)
                            .clickable { selectedVehicleType = "TOTO" }
                            .testTag("rider_subcard_toto"),
                        shape = RoundedCornerShape(16.dp),
                        colors = CardDefaults.cardColors(
                            containerColor = if (selectedVehicleType == "TOTO") IndianGreen.copy(alpha = 0.12f) else MaterialTheme.colorScheme.surface
                        ),
                        border = BorderStroke(
                            2.dp,
                            if (selectedVehicleType == "TOTO") IndianGreen else MaterialTheme.colorScheme.outlineVariant
                        )
                    ) {
                        Column(
                            modifier = Modifier.padding(14.dp),
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Box(
                                modifier = Modifier
                                    .size(54.dp)
                                    .clip(CircleShape)
                                    .background(if (selectedVehicleType == "TOTO") IndianGreen else Color(0xFFF1F5F9)),
                                contentAlignment = Alignment.Center
                            ) {
                                Icon(
                                    Icons.Default.ElectricRickshaw,
                                    contentDescription = "Toto",
                                    tint = if (selectedVehicleType == "TOTO") Color.White else IndianGreen,
                                    modifier = Modifier.size(28.dp)
                                )
                            }
                            Spacer(modifier = Modifier.height(8.dp))
                            Text("TOTO", fontWeight = FontWeight.ExtraBold, fontSize = 16.sp)
                            Text("ই-টোটো চালক", fontSize = 11.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
                            Spacer(modifier = Modifier.height(4.dp))
                            Surface(
                                color = if (selectedVehicleType == "TOTO") IndianGreen else Color(0xFFE2E8F0),
                                shape = RoundedCornerShape(6.dp)
                            ) {
                                Text(
                                    text = if (selectedVehicleType == "TOTO") "SELECTED" else "CHOOSE",
                                    fontSize = 10.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = if (selectedVehicleType == "TOTO") Color.White else Color.DarkGray,
                                    modifier = Modifier.padding(horizontal = 8.dp, vertical = 2.dp)
                                )
                            }
                        }
                    }
                }
            }

            // Existing Riders List Header
            item {
                Spacer(modifier = Modifier.height(8.dp))
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "Active Registered Riders (${riders.size})",
                        style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold)
                    )
                    Text("Barasat Circle", fontSize = 11.sp, color = IndianGreen, fontWeight = FontWeight.Bold)
                }
            }

            // List of Existing Riders
            items(riders) { rider ->
                RiderCardItem(rider = rider)
            }

            item {
                Spacer(modifier = Modifier.height(80.dp))
            }
        }
    }
}

@Composable
private fun RiderCardItem(rider: RiderProfile) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .testTag("rider_card_${rider.id}"),
        shape = RoundedCornerShape(14.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(14.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(48.dp)
                    .clip(CircleShape)
                    .background(if (rider.vehicleType == "BIKE") SaffronPrimary.copy(alpha = 0.2f) else IndianGreen.copy(alpha = 0.2f)),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = if (rider.vehicleType == "BIKE") Icons.Default.TwoWheeler else Icons.Default.ElectricRickshaw,
                    contentDescription = rider.vehicleType,
                    tint = if (rider.vehicleType == "BIKE") SaffronPrimary else IndianGreen,
                    modifier = Modifier.size(26.dp)
                )
            }

            Spacer(modifier = Modifier.width(12.dp))

            Column(modifier = Modifier.weight(1f)) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(rider.name, fontWeight = FontWeight.Bold, fontSize = 14.sp)
                    Spacer(modifier = Modifier.width(6.dp))
                    if (rider.isVerified) {
                        Icon(Icons.Default.Verified, contentDescription = "Verified", tint = IndianGreen, modifier = Modifier.size(15.dp))
                    }
                }
                Text(
                    text = "${rider.vehicleType} • ${rider.vehicleNumber}",
                    fontSize = 12.sp,
                    color = AshokaBlue,
                    fontWeight = FontWeight.SemiBold
                )
                Text(
                    text = rider.address,
                    fontSize = 11.sp,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    maxLines = 1
                )
                if (rider.drivingLicence.isNotBlank()) {
                    Text(
                        text = "DL: ${rider.drivingLicence}",
                        fontSize = 10.sp,
                        color = Color.Gray
                    )
                }
            }

            Column(horizontalAlignment = Alignment.End) {
                Surface(
                    color = Color(0xFFFFFBEB),
                    shape = RoundedCornerShape(6.dp)
                ) {
                    Row(
                        modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(Icons.Default.Star, contentDescription = null, tint = Color(0xFFD97706), modifier = Modifier.size(12.dp))
                        Spacer(modifier = Modifier.width(2.dp))
                        Text("${rider.rating}", fontSize = 11.sp, fontWeight = FontWeight.Bold, color = Color(0xFFD97706))
                    }
                }
                Spacer(modifier = Modifier.height(4.dp))
                Text(rider.phone, fontSize = 10.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
            }
        }
    }
}
