package com.example.ui.screens

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
import com.example.model.DriverProfile
import com.example.ui.components.DriverProfileWallDialog
import com.example.ui.theme.AshokaBlue
import com.example.ui.theme.IndianGreen
import com.example.ui.theme.SaffronPrimary
import com.example.viewmodel.MainViewModel

/**
 * Requirement 4: Hire A Driver Section
 * - Profile Wall: A clean list of verified drivers (e.g., 6 drivers).
 * - Details per driver: 8-hour Fixed Fee (₹800) + Overtime Fee (₹100/hr) + Documents Verified Badge.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HireDriverScreen(
    viewModel: MainViewModel,
    onBack: () -> Unit
) {
    val drivers by viewModel.verifiedDrivers.collectAsState()
    val profileWallDriver by viewModel.profileWallDriver.collectAsState()

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Column {
                        Text("Hire A Verified Driver", fontWeight = FontWeight.Bold, fontSize = 18.sp)
                        Text("Chauffeurs for private, commercial & outstation trips", fontSize = 11.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
                    }
                },
                navigationIcon = {
                    IconButton(onClick = onBack, modifier = Modifier.testTag("hire_driver_back_btn")) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = MaterialTheme.colorScheme.surface)
            )
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(horizontal = 16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            // Standard Tariff Overview Header
            Card(
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.secondaryContainer.copy(alpha = 0.5f)),
                shape = RoundedCornerShape(14.dp)
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(14.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column {
                        Text(
                            text = "Standard Uniform Tariff",
                            style = MaterialTheme.typography.labelMedium.copy(fontWeight = FontWeight.Bold, color = AshokaBlue)
                        )
                        Text(
                            text = "Fixed ₹800 for 8 Hours • ₹100/hr Overtime",
                            style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.Bold)
                        )
                    }
                    Surface(
                        color = IndianGreen.copy(alpha = 0.15f),
                        shape = RoundedCornerShape(8.dp)
                    ) {
                        Text(
                            text = "100% Verified",
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Bold,
                            color = IndianGreen,
                            modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                        )
                    }
                }
            }

            Text(
                text = "Available Verified Drivers (${drivers.size})",
                style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold)
            )

            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                items(drivers) { driver ->
                    DriverCardItem(
                        driver = driver,
                        onViewProfile = {
                            viewModel.openDriverProfileWall(driver)
                        }
                    )
                }
                item {
                    Spacer(modifier = Modifier.height(16.dp))
                }
            }
        }
    }

    // Driver Profile Wall Dialog
    if (profileWallDriver != null) {
        DriverProfileWallDialog(
            driver = profileWallDriver!!,
            onDismiss = { viewModel.closeDriverProfileWall() },
            onBookNow = {
                viewModel.closeDriverProfileWall()
                viewModel.bookCurrentRide()
            }
        )
    }
}

@Composable
private fun DriverCardItem(
    driver: DriverProfile,
    onViewProfile: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onViewProfile() }
            .testTag("driver_card_${driver.id}"),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.5.dp)
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Avatar
                Box(
                    modifier = Modifier
                        .size(54.dp)
                        .clip(CircleShape)
                        .background(MaterialTheme.colorScheme.primaryContainer),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = driver.avatarInitials,
                        fontWeight = FontWeight.ExtraBold,
                        fontSize = 18.sp,
                        color = MaterialTheme.colorScheme.onPrimaryContainer
                    )
                }

                Spacer(modifier = Modifier.width(12.dp))

                Column(modifier = Modifier.weight(1f)) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text(
                            text = driver.name,
                            style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold)
                        )
                        Spacer(modifier = Modifier.width(6.dp))
                        Icon(
                            Icons.Default.Verified,
                            contentDescription = "Verified",
                            tint = IndianGreen,
                            modifier = Modifier.size(16.dp)
                        )
                    }

                    Text(
                        text = "${driver.experienceYears} Years Exp • ${driver.vehicleModel}",
                        fontSize = 12.sp,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )

                    // Rating
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.padding(top = 2.dp)
                    ) {
                        Icon(Icons.Default.Star, contentDescription = null, tint = Color(0xFFD97706), modifier = Modifier.size(14.dp))
                        Spacer(modifier = Modifier.width(3.dp))
                        Text("${driver.rating}", fontSize = 12.sp, fontWeight = FontWeight.Bold)
                        Spacer(modifier = Modifier.width(6.dp))
                        Text("(${driver.totalTrips} trips)", fontSize = 11.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
                    }
                }

                // Documents Verified Badge
                Surface(
                    color = IndianGreen.copy(alpha = 0.12f),
                    shape = RoundedCornerShape(8.dp),
                    border = androidx.compose.foundation.BorderStroke(1.dp, IndianGreen.copy(alpha = 0.4f))
                ) {
                    Column(
                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Icon(Icons.Default.Security, contentDescription = null, tint = IndianGreen, modifier = Modifier.size(14.dp))
                        Text(
                            text = "DOCS VERIFIED",
                            fontSize = 9.sp,
                            fontWeight = FontWeight.ExtraBold,
                            color = IndianGreen
                        )
                    }
                }
            }

            HorizontalDivider(color = MaterialTheme.colorScheme.surfaceVariant)

            // Pricing & Badges Row (MANDATORY: 8-hour Fixed Fee ₹800 + Overtime Fee ₹100/hr)
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                    Column {
                        Text("8-Hour Fixed Fee", fontSize = 10.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
                        Text("₹${driver.fixed8HrFee}", fontSize = 15.sp, fontWeight = FontWeight.ExtraBold, color = SaffronPrimary)
                    }
                    Column {
                        Text("Overtime Rate", fontSize = 10.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
                        Text("₹${driver.overtimePerHourRate}/hr", fontSize = 15.sp, fontWeight = FontWeight.Bold, color = AshokaBlue)
                    }
                }

                Button(
                    onClick = onViewProfile,
                    modifier = Modifier
                        .height(38.dp)
                        .testTag("driver_view_profile_${driver.id}"),
                    colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primaryContainer),
                    contentPadding = PaddingValues(horizontal = 12.dp)
                ) {
                    Text("View Profile Wall", fontSize = 12.sp, fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.onPrimaryContainer)
                }
            }
        }
    }
}
