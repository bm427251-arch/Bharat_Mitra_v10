package com.example.ui.screens

import android.widget.Toast
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
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
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.model.FleetItem
import com.example.ui.components.BharatMitraHeaderLogo
import com.example.ui.components.InteractiveMapCanvas
import com.example.ui.theme.*
import com.example.viewmodel.MainViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AvailableFleetMapScreen(
    viewModel: MainViewModel,
    onBack: () -> Unit,
    onConfirmBooking: () -> Unit
) {
    BackHandler { onBack() }
    val context = LocalContext.current
    val fleetList by viewModel.availableFleet.collectAsState()
    val currentAddress by viewModel.currentAddress.collectAsState()
    val selectedDestination by viewModel.selectedDestination.collectAsState()
    val nearbyLandmarks by viewModel.nearbyLandmarks.collectAsState()

    var selectedFilter by remember { mutableStateOf("All Services") }
    var selectedFleetId by remember { mutableStateOf(fleetList.firstOrNull()?.id ?: "fl_sedan") }

    val filterOptions = listOf("All Services", "Bike", "Toto", "Auto", "Mini", "Sedan", "SUV")

    val filteredList = remember(selectedFilter, fleetList) {
        if (selectedFilter == "All Services") {
            fleetList
        } else {
            fleetList.filter { it.name.contains(selectedFilter, ignoreCase = true) }
        }
    }

    val selectedItem = fleetList.firstOrNull { it.id == selectedFleetId } ?: fleetList.firstOrNull()

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column {
                            Text("Available Fleet Map", fontWeight = FontWeight.Bold, fontSize = 17.sp, color = Color.White)
                            Text("6 Services • 12 nearby vehicles", fontSize = 11.sp, color = BharatOrangeLight)
                        }
                        BharatMitraHeaderLogo(width = 90.dp, height = 28.dp)
                    }
                },
                navigationIcon = {
                    IconButton(onClick = onBack, modifier = Modifier.testTag("fleet_map_back_btn")) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back", tint = Color.White)
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = BharatDarkBlue)
            )
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .background(LightBackground)
        ) {
            // Filter chips horizontally scrollable
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .horizontalScroll(rememberScrollState())
                    .background(Color.White)
                    .padding(horizontal = 14.dp, vertical = 10.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                filterOptions.forEach { filter ->
                    val isSelected = selectedFilter == filter
                    FilterChip(
                        selected = isSelected,
                        onClick = { selectedFilter = filter },
                        label = { Text(filter, fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal) },
                        colors = FilterChipDefaults.filterChipColors(
                            selectedContainerColor = BharatDarkBlue,
                            selectedLabelColor = Color.White,
                            containerColor = LightSurfaceVariant,
                            labelColor = BharatDarkBlue
                        ),
                        shape = RoundedCornerShape(20.dp)
                    )
                }
            }

            // Nearby Landmark Quick Suggestions Row
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .horizontalScroll(rememberScrollState())
                    .background(Color(0xFFF1F5F9))
                    .padding(horizontal = 14.dp, vertical = 6.dp),
                horizontalArrangement = Arrangement.spacedBy(6.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text("Nearby: ", fontSize = 11.sp, fontWeight = FontWeight.Bold, color = BharatDarkBlue)
                nearbyLandmarks.forEach { landmark ->
                    val isSel = selectedDestination == landmark.name
                    SuggestionChip(
                        onClick = { viewModel.selectDestination(landmark.name) },
                        label = { Text("${landmark.name} (${landmark.distance})", fontSize = 11.sp, fontWeight = if (isSel) FontWeight.Bold else FontWeight.Normal) },
                        colors = SuggestionChipDefaults.suggestionChipColors(
                            containerColor = if (isSel) BharatOrange else Color.White,
                            labelColor = if (isSel) Color.White else BharatDarkBlue
                        ),
                        border = SuggestionChipDefaults.suggestionChipBorder(
                            enabled = true,
                            borderColor = if (isSel) BharatOrange else Color.LightGray
                        ),
                        shape = RoundedCornerShape(16.dp)
                    )
                }
            }

            // Interactive Map View with Kolkata Street Canvas
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f)
                    .clip(RoundedCornerShape(bottomStart = 20.dp, bottomEnd = 20.dp))
                    .testTag("fleet_interactive_map")
            ) {
                InteractiveMapCanvas(
                    pickupLocation = if (currentAddress.isNotBlank()) currentAddress else "Current Live Location",
                    dropLocation = selectedDestination ?: "Select Nearby Landmark",
                    isDropConfirmed = true
                )
            }

            // Bottom List: Choose Your Ride (All 6 Services in 2 Rows / list)
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(topStart = 24.dp, topEnd = 24.dp),
                elevation = CardDefaults.cardElevation(defaultElevation = 12.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White)
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
                        Text(
                            text = "Choose Your Ride (6 Services)",
                            style = MaterialTheme.typography.titleMedium.copy(
                                fontWeight = FontWeight.Bold,
                                color = BharatDarkBlue,
                                fontSize = 16.sp
                            )
                        )
                        Text(
                            text = "Fair Price • No Surge",
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Bold,
                            color = BharatGreen
                        )
                    }

                    // Scrollable vehicles list
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .heightIn(max = 240.dp)
                            .verticalScroll(rememberScrollState()),
                        verticalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        filteredList.forEach { item ->
                            val isSelected = selectedFleetId == item.id
                            val vehicleIcon = when {
                                item.name.contains("Bike", ignoreCase = true) -> Icons.Default.TwoWheeler
                                item.name.contains("Toto", ignoreCase = true) -> Icons.Default.ElectricRickshaw
                                item.name.contains("Auto", ignoreCase = true) -> Icons.Default.DirectionsTransit
                                item.name.contains("Mini", ignoreCase = true) -> Icons.Default.LocalTaxi
                                item.name.contains("SUV", ignoreCase = true) -> Icons.Default.AirportShuttle
                                else -> Icons.Default.DirectionsCar
                            }

                            Card(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .clickable { selectedFleetId = item.id }
                                    .testTag("fleet_item_${item.id}"),
                                shape = RoundedCornerShape(14.dp),
                                colors = CardDefaults.cardColors(
                                    containerColor = if (isSelected) BharatOrange.copy(alpha = 0.10f) else LightSurfaceVariant
                                ),
                                border = BorderStroke(
                                    if (isSelected) 1.5.dp else 0.5.dp,
                                    if (isSelected) BharatOrange else CardBorderColor
                                )
                            ) {
                                Row(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(12.dp),
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Box(
                                        modifier = Modifier
                                            .size(42.dp)
                                            .clip(CircleShape)
                                            .background(if (isSelected) BharatOrange else BharatDarkBlue),
                                        contentAlignment = Alignment.Center
                                    ) {
                                        Icon(
                                            imageVector = vehicleIcon,
                                            contentDescription = item.name,
                                            tint = Color.White,
                                            modifier = Modifier.size(22.dp)
                                        )
                                    }
                                    Spacer(modifier = Modifier.width(12.dp))
                                    Column(modifier = Modifier.weight(1f)) {
                                        Row(verticalAlignment = Alignment.CenterVertically) {
                                            Text(
                                                text = item.name,
                                                fontWeight = FontWeight.Bold,
                                                fontSize = 15.sp,
                                                color = BharatDarkBlue
                                            )
                                            Spacer(modifier = Modifier.width(6.dp))
                                            Surface(
                                                color = BharatGreen.copy(alpha = 0.15f),
                                                shape = RoundedCornerShape(4.dp)
                                            ) {
                                                Text(
                                                    text = "${item.availableCount} near",
                                                    color = BharatGreen,
                                                    fontWeight = FontWeight.Bold,
                                                    fontSize = 10.sp,
                                                    modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                                                )
                                            }
                                        }
                                        Spacer(modifier = Modifier.height(2.dp))
                                        Text(
                                            text = "${item.category} • ETA ${item.etaMinutes} min • ${item.rating}★",
                                            fontSize = 11.sp,
                                            color = Color.Gray
                                        )
                                    }
                                    Column(horizontalAlignment = Alignment.End) {
                                        Text(
                                            text = "₹${item.price}",
                                            fontWeight = FontWeight.Black,
                                            fontSize = 17.sp,
                                            color = BharatDarkBlue
                                        )
                                        Text(
                                            text = "All-inclusive",
                                            fontSize = 9.sp,
                                            color = BharatGreen,
                                            fontWeight = FontWeight.Bold
                                        )
                                    }
                                }
                            }
                        }
                    }

                    // Confirm & Book Button
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(48.dp)
                            .clip(RoundedCornerShape(12.dp))
                            .background(
                                brush = Brush.horizontalGradient(
                                    listOf(BharatOrange, BharatGreen)
                                )
                            )
                            .clickable {
                                val pickup = if (currentAddress.isNotBlank()) currentAddress else "Current Live Location"
                                val drop = selectedDestination ?: "Nearby Landmark (${selectedItem?.name ?: "Ride"})"
                                viewModel.confirmBooking(
                                    pickup = pickup,
                                    drop = drop,
                                    fare = selectedItem?.price ?: 100
                                )
                                onConfirmBooking()
                            }
                            .testTag("fleet_confirm_and_book_btn"),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = "Confirm & Book ${selectedItem?.name ?: "Ride"} (₹${selectedItem?.price ?: 100})",
                            color = Color.White,
                            fontWeight = FontWeight.Bold,
                            fontSize = 15.sp
                        )
                    }
                }
            }
        }
    }
}
