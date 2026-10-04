package com.example.ui.screens

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.BorderStroke
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
import androidx.compose.ui.hapticfeedback.HapticFeedbackType
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.model.DriverProfile
import com.example.model.VehicleCategory
import com.example.ui.theme.AshokaBlue
import com.example.ui.theme.IndianGreen
import com.example.ui.theme.NavySecondary
import com.example.ui.theme.SaffronPrimary
import com.example.viewmodel.MainViewModel

data class VehicleTypeOption(
    val id: String,
    val name: String,
    val category: VehicleCategory,
    val subtitle: String,
    val exampleModel: String
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DriverCategoryScreen(
    viewModel: MainViewModel,
    onBack: () -> Unit,
    onCreateProfile: (vehicleType: String) -> Unit
) {
    BackHandler { onBack() }
    val haptic = LocalHapticFeedback.current

    val drivers by viewModel.verifiedDrivers.collectAsState()

    val vehicleOptions = remember {
        listOf(
            VehicleTypeOption("sedan", "Sedan", VehicleCategory.SEDAN, "Comfort 4-Seater", "Maruti Dzire, Honda City"),
            VehicleTypeOption("suv", "SUV", VehicleCategory.SUV, "Spacious 6-7 Seater", "Mahindra Scorpio-N, Bolero"),
            VehicleTypeOption("innova", "Innova", VehicleCategory.SUV, "Luxury VIP MUV", "Toyota Innova Crysta ZX"),
            VehicleTypeOption("hatchback", "Hatchback", VehicleCategory.HATCHBACK, "City Compact", "WagonR, Tiago, i20")
        )
    }

    var selectedVehicle by remember { mutableStateOf<VehicleTypeOption?>(vehicleOptions[0]) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Column {
                        Text("Driver Partner Category", fontWeight = FontWeight.Bold, fontSize = 18.sp)
                        Text("গাড়ি চালক ও সারথিদের জন্য", fontSize = 11.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
                    }
                },
                navigationIcon = {
                    IconButton(onClick = onBack, modifier = Modifier.testTag("driver_back_btn")) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = MaterialTheme.colorScheme.surface)
            )
        },
        floatingActionButton = {
            // Requirement 3: User selects vehicle type -> then FAB Green "Create Profile" appears
            if (selectedVehicle != null) {
                FloatingActionButton(
                    onClick = {
                        haptic.performHapticFeedback(HapticFeedbackType.LongPress)
                        onCreateProfile(selectedVehicle!!.name)
                    },
                    containerColor = Color(0xFF138808),
                    contentColor = Color.White,
                    shape = RoundedCornerShape(16.dp),
                    modifier = Modifier.testTag("driver_create_profile_fab")
                ) {
                    Row(
                        modifier = Modifier.padding(horizontal = 16.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(Icons.Default.Add, contentDescription = "Add", modifier = Modifier.size(20.dp))
                        Spacer(modifier = Modifier.width(8.dp))
                        Text("Create Profile (${selectedVehicle!!.name})", fontWeight = FontWeight.Bold)
                    }
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
                    text = "Step 1: Select Your Vehicle Type",
                    style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold)
                )
                Text(
                    text = "Choose your vehicle category to load verified tariff rates and registration requirements",
                    fontSize = 12.sp,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }

            // Vehicle Selection Grid (Sedan, SUV, Innova, Hatchback)
            item {
                Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        VehicleSelectionCard(
                            option = vehicleOptions[0],
                            isSelected = selectedVehicle?.id == vehicleOptions[0].id,
                            onClick = { selectedVehicle = vehicleOptions[0] },
                            modifier = Modifier.weight(1f)
                        )
                        VehicleSelectionCard(
                            option = vehicleOptions[1],
                            isSelected = selectedVehicle?.id == vehicleOptions[1].id,
                            onClick = { selectedVehicle = vehicleOptions[1] },
                            modifier = Modifier.weight(1f)
                        )
                    }
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        VehicleSelectionCard(
                            option = vehicleOptions[2],
                            isSelected = selectedVehicle?.id == vehicleOptions[2].id,
                            onClick = { selectedVehicle = vehicleOptions[2] },
                            modifier = Modifier.weight(1f)
                        )
                        VehicleSelectionCard(
                            option = vehicleOptions[3],
                            isSelected = selectedVehicle?.id == vehicleOptions[3].id,
                            onClick = { selectedVehicle = vehicleOptions[3] },
                            modifier = Modifier.weight(1f)
                        )
                    }
                }
            }

            // List of Existing Registered Drivers
            item {
                Spacer(modifier = Modifier.height(8.dp))
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "Verified Commercial Drivers (${drivers.size})",
                        style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold)
                    )
                    Text("Verified Fleet", fontSize = 11.sp, color = IndianGreen, fontWeight = FontWeight.Bold)
                }
            }

            items(drivers) { driver ->
                DriverCardItem(driver = driver)
            }

            item {
                Spacer(modifier = Modifier.height(80.dp))
            }
        }
    }
}

@Composable
private fun VehicleSelectionCard(
    option: VehicleTypeOption,
    isSelected: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier
            .clickable { onClick() }
            .testTag("vehicle_card_${option.id}"),
        shape = RoundedCornerShape(14.dp),
        colors = CardDefaults.cardColors(
            containerColor = if (isSelected) SaffronPrimary.copy(alpha = 0.1f) else MaterialTheme.colorScheme.surface
        ),
        border = BorderStroke(
            2.dp,
            if (isSelected) SaffronPrimary else MaterialTheme.colorScheme.outlineVariant
        )
    ) {
        Column(
            modifier = Modifier.padding(12.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Box(
                modifier = Modifier
                    .size(44.dp)
                    .clip(CircleShape)
                    .background(if (isSelected) SaffronPrimary else Color(0xFFF1F5F9)),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    Icons.Default.DirectionsCar,
                    contentDescription = option.name,
                    tint = if (isSelected) Color.White else SaffronPrimary,
                    modifier = Modifier.size(24.dp)
                )
            }
            Spacer(modifier = Modifier.height(6.dp))
            Text(option.name, fontWeight = FontWeight.Bold, fontSize = 15.sp)
            Text(option.subtitle, fontSize = 10.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
            Text(option.exampleModel, fontSize = 9.sp, color = AshokaBlue, maxLines = 1)
        }
    }
}

@Composable
private fun DriverCardItem(driver: DriverProfile) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .testTag("driver_card_${driver.id}"),
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
                    .background(NavySecondary),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = driver.avatarInitials,
                    fontWeight = FontWeight.Bold,
                    color = Color.White,
                    fontSize = 16.sp
                )
            }

            Spacer(modifier = Modifier.width(12.dp))

            Column(modifier = Modifier.weight(1f)) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(driver.name, fontWeight = FontWeight.Bold, fontSize = 14.sp)
                    Spacer(modifier = Modifier.width(6.dp))
                    if (driver.isCommercialDlVerified) {
                        Icon(Icons.Default.Verified, contentDescription = "Verified DL", tint = IndianGreen, modifier = Modifier.size(15.dp))
                    }
                }
                Text(
                    text = "${driver.vehicleCategory.displayName} • ${driver.vehicleModel}",
                    fontSize = 12.sp,
                    color = AshokaBlue,
                    fontWeight = FontWeight.SemiBold
                )
                Text(
                    text = "RC: ${driver.rcNumber} • ${driver.experienceYears} yrs exp",
                    fontSize = 11.sp,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
                Text(
                    text = "Ins: ${driver.insuranceValidity}",
                    fontSize = 10.sp,
                    color = Color.Gray
                )
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
                        Text("${driver.rating}", fontSize = 11.sp, fontWeight = FontWeight.Bold, color = Color(0xFFD97706))
                    }
                }
                Spacer(modifier = Modifier.height(4.dp))
                Text(driver.phone, fontSize = 10.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
            }
        }
    }
}
