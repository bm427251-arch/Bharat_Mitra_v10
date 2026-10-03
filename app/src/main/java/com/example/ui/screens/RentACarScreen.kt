package com.example.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
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
import com.example.model.ScreenState
import com.example.model.VehicleCategory
import com.example.model.VehicleVariant
import com.example.ui.components.DriverProfileWallDialog
import com.example.ui.theme.AshokaBlue
import com.example.ui.theme.IndianGreen
import com.example.ui.theme.SaffronPrimary
import com.example.viewmodel.MainViewModel

/**
 * Requirement 3: Rent A Car Section
 * - Top Filter Tabs: Toto, Auto, Hatchback, Sedan, SUV.
 * - Sub-options: Each vehicle category must have 3 options: Non-AC, AC, and Premium.
 * - Profile Wall: Clicking any vehicle instantly opens a Driver Profile Wall displaying
 *   the driver's Photo, RC, Insurance, and Rating.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RentACarScreen(
    viewModel: MainViewModel,
    onBack: () -> Unit
) {
    val selectedCategory by viewModel.selectedRentCategory.collectAsState()
    val selectedVariant by viewModel.selectedRentVariant.collectAsState()
    val rentalVehicles by viewModel.rentalVehicles.collectAsState()
    val profileWallDriver by viewModel.profileWallDriver.collectAsState()
    val profileWallRentalInfo by viewModel.profileWallRentalInfo.collectAsState()

    // Filter vehicles by category and variant
    val filteredVehicles = remember(selectedCategory, selectedVariant, rentalVehicles) {
        rentalVehicles.filter {
            it.category == selectedCategory && it.variant == selectedVariant
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Column {
                        Text("Rent A Car & Fleet", fontWeight = FontWeight.Bold, fontSize = 18.sp)
                        Text("Daily & hourly rental with verified driver", fontSize = 11.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
                    }
                },
                navigationIcon = {
                    IconButton(onClick = onBack, modifier = Modifier.testTag("rent_car_back_btn")) {
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
            // Category Tabs (Toto, Auto, Hatchback, Sedan, SUV)
            Text(
                text = "Vehicle Category",
                style = MaterialTheme.typography.labelMedium.copy(fontWeight = FontWeight.Bold)
            )
            LazyRow(
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                items(VehicleCategory.values()) { category ->
                    val isSelected = selectedCategory == category
                    FilterChip(
                        selected = isSelected,
                        onClick = { viewModel.selectRentCategory(category) },
                        label = {
                            Text(
                                text = when (category) {
                                    VehicleCategory.TOTO -> "🛺 Toto"
                                    VehicleCategory.AUTO -> "🛺 Auto"
                                    VehicleCategory.HATCHBACK -> "🚗 Hatchback"
                                    VehicleCategory.SEDAN -> "🚙 Sedan"
                                    VehicleCategory.SUV -> "🚐 SUV"
                                },
                                fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal
                            )
                        },
                        colors = FilterChipDefaults.filterChipColors(
                            selectedContainerColor = SaffronPrimary,
                            selectedLabelColor = Color.White
                        ),
                        modifier = Modifier.testTag("category_chip_${category.name}")
                    )
                }
            }

            // Sub-options: Non-AC, AC, Premium
            Text(
                text = "Comfort Tier (Sub-options)",
                style = MaterialTheme.typography.labelMedium.copy(fontWeight = FontWeight.Bold)
            )
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                VehicleVariant.values().forEach { variant ->
                    val isSelected = selectedVariant == variant
                    Surface(
                        modifier = Modifier
                            .weight(1f)
                            .clip(RoundedCornerShape(10.dp))
                            .clickable { viewModel.selectRentVariant(variant) }
                            .testTag("variant_tab_${variant.name}"),
                        color = if (isSelected) MaterialTheme.colorScheme.primaryContainer else MaterialTheme.colorScheme.surfaceVariant,
                        border = if (isSelected) androidx.compose.foundation.BorderStroke(1.5.dp, SaffronPrimary) else null
                    ) {
                        Column(
                            modifier = Modifier.padding(vertical = 10.dp, horizontal = 8.dp),
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Text(
                                text = when (variant) {
                                    VehicleVariant.NON_AC -> "🌿 Non-AC"
                                    VehicleVariant.AC -> "❄️ AC"
                                    VehicleVariant.PREMIUM -> "👑 Premium"
                                },
                                fontSize = 12.sp,
                                fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium,
                                color = if (isSelected) MaterialTheme.colorScheme.onPrimaryContainer else MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }
                    }
                }
            }

            HorizontalDivider(modifier = Modifier.padding(vertical = 4.dp))

            // Vehicle Listing
            Text(
                text = "Available ${selectedCategory.displayName} (${selectedVariant.displayName})",
                style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold)
            )

            if (filteredVehicles.isEmpty()) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(1f),
                    contentAlignment = Alignment.Center
                ) {
                    Text("No fleets currently available in this combination. Please choose another tier.", color = MaterialTheme.colorScheme.onSurfaceVariant)
                }
            } else {
                LazyColumn(
                    modifier = Modifier.weight(1f),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    items(filteredVehicles) { vehicle ->
                        Card(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clickable {
                                    viewModel.openDriverProfileWall(
                                        vehicle.driver,
                                        rentalTitle = "${vehicle.title} (${selectedVariant.displayName})",
                                        rentalPrice = "₹${vehicle.ratePerHour}/hr • ₹${vehicle.ratePerDay}/day"
                                    )
                                }
                                .testTag("rental_vehicle_card_${vehicle.id}"),
                            shape = RoundedCornerShape(16.dp),
                            elevation = CardDefaults.cardElevation(defaultElevation = 3.dp),
                            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
                        ) {
                            Column(
                                modifier = Modifier.padding(16.dp),
                                verticalArrangement = Arrangement.spacedBy(10.dp)
                            ) {
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.SpaceBetween,
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Column {
                                        Text(
                                            text = vehicle.title,
                                            style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold)
                                        )
                                        Text(
                                            text = "${vehicle.seats} Seats • Fuel: ${vehicle.fuelType} • ${vehicle.category.displayName}",
                                            fontSize = 12.sp,
                                            color = MaterialTheme.colorScheme.onSurfaceVariant
                                        )
                                    }

                                    Column(horizontalAlignment = Alignment.End) {
                                        Text(
                                            text = "₹${vehicle.ratePerHour}/hr",
                                            style = MaterialTheme.typography.titleMedium.copy(
                                                fontWeight = FontWeight.ExtraBold,
                                                color = SaffronPrimary
                                            )
                                        )
                                        Text(
                                            text = "₹${vehicle.ratePerDay}/day",
                                            fontSize = 11.sp,
                                            color = MaterialTheme.colorScheme.onSurfaceVariant
                                        )
                                    }
                                }

                                HorizontalDivider(color = MaterialTheme.colorScheme.surfaceVariant)

                                // Driver Summary Row (Instant click triggers Profile Wall)
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Box(
                                        modifier = Modifier
                                            .size(42.dp)
                                            .clip(CircleShape)
                                            .background(MaterialTheme.colorScheme.primaryContainer),
                                        contentAlignment = Alignment.Center
                                    ) {
                                        Text(
                                            text = vehicle.driver.avatarInitials,
                                            fontWeight = FontWeight.Bold,
                                            color = MaterialTheme.colorScheme.onPrimaryContainer
                                        )
                                    }
                                    Spacer(modifier = Modifier.width(10.dp))
                                    Column(modifier = Modifier.weight(1f)) {
                                        Row(verticalAlignment = Alignment.CenterVertically) {
                                            Text(
                                                text = vehicle.driver.name,
                                                fontWeight = FontWeight.Bold,
                                                fontSize = 13.sp
                                            )
                                            Spacer(modifier = Modifier.width(4.dp))
                                            Icon(Icons.Default.CheckCircle, contentDescription = null, tint = IndianGreen, modifier = Modifier.size(14.dp))
                                        }
                                        Text(
                                            text = "RC: ${vehicle.driver.rcNumber} • Rating: ${vehicle.driver.rating} ★",
                                            fontSize = 11.sp,
                                            color = MaterialTheme.colorScheme.onSurfaceVariant
                                        )
                                    }

                                    Surface(
                                        color = AshokaBlue.copy(alpha = 0.1f),
                                        shape = RoundedCornerShape(8.dp)
                                    ) {
                                        Row(
                                            modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
                                            verticalAlignment = Alignment.CenterVertically
                                        ) {
                                            Text("Profile Wall", fontSize = 11.sp, fontWeight = FontWeight.Bold, color = AshokaBlue)
                                            Icon(Icons.Default.ChevronRight, contentDescription = null, tint = AshokaBlue, modifier = Modifier.size(14.dp))
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    // Driver Profile Wall Dialog
    if (profileWallDriver != null) {
        DriverProfileWallDialog(
            driver = profileWallDriver!!,
            rentalTitle = profileWallRentalInfo?.first,
            rentalPriceInfo = profileWallRentalInfo?.second,
            onDismiss = { viewModel.closeDriverProfileWall() },
            onBookNow = {
                viewModel.closeDriverProfileWall()
                viewModel.bookCurrentRide()
            }
        )
    }
}
