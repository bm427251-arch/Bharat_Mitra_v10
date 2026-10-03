package com.example.ui.screens

import android.widget.Toast
import androidx.compose.animation.*
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.awaitEachGesture
import androidx.compose.foundation.gestures.awaitFirstDown
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import coil.compose.AsyncImage
import com.example.data.SampleData
import com.example.model.RideOption
import com.example.ui.components.BharatMitraLogo
import com.example.ui.components.InteractiveMapCanvas
import com.example.ui.theme.AshokaBlue
import com.example.ui.theme.IndianGreen
import com.example.ui.theme.NavySecondary
import com.example.ui.theme.SaffronPrimary
import com.example.viewmodel.MainViewModel

@Composable
fun HomeScreen(
    viewModel: MainViewModel,
    onNavigateToRentCar: () -> Unit,
    onNavigateToHireDriver: () -> Unit,
    onNavigateToElite: () -> Unit
) {
    val context = LocalContext.current
    val pickupLocation by viewModel.pickupLocation.collectAsState()
    val dropLocation by viewModel.dropLocation.collectAsState()
    val isDropConfirmed by viewModel.isDropConfirmed.collectAsState()
    val selectedRideOption by viewModel.selectedRideOption.collectAsState()
    val isAcSelected by viewModel.isAcSelected.collectAsState()
    val calculatedFare by viewModel.calculatedFare.collectAsState()
    val estimatedDistanceKm by viewModel.estimatedDistanceKm.collectAsState()
    val mapDots by viewModel.mapDots.collectAsState()
    val customLogoUri by viewModel.customLogoUri.collectAsState()
    val appAnnouncement by viewModel.appAnnouncement.collectAsState()
    val adminMessages by viewModel.adminMessages.collectAsState()

    var dropSearchQuery by remember { mutableStateOf("") }
    var isSearchingDrop by remember { mutableStateOf(false) }
    var showUserInboxDialog by remember { mutableStateOf(false) }
    var showComplaintDialog by remember { mutableStateOf(false) }

    val filteredSuggestions = remember(dropSearchQuery) {
        if (dropSearchQuery.isBlank()) {
            SampleData.locationSuggestions
        } else {
            SampleData.locationSuggestions.filter {
                it.title.contains(dropSearchQuery, ignoreCase = true) ||
                it.subtitle.contains(dropSearchQuery, ignoreCase = true)
            }
        }
    }

    Scaffold(
        topBar = {
            Surface(
                color = MaterialTheme.colorScheme.surface,
                shadowElevation = 3.dp
            ) {
                Column(modifier = Modifier.fillMaxWidth().statusBarsPadding()) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 16.dp, vertical = 10.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        // Secret Admin Entry: Long-pressing app title or logo continuously for 7 seconds without counter
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier
                                .clip(RoundedCornerShape(8.dp))
                                .pointerInput(Unit) {
                                    awaitEachGesture {
                                        awaitFirstDown(requireUnconsumed = false)
                                        // Start 7-second continuous silent press
                                        viewModel.startAdminLongPress()
                                        do {
                                            val event = awaitPointerEvent()
                                            val isPressed = event.changes.any { it.pressed }
                                            if (!isPressed) {
                                                viewModel.cancelAdminLongPress()
                                            }
                                        } while (event.changes.any { it.pressed })
                                        viewModel.cancelAdminLongPress()
                                    }
                                }
                                .testTag("top_app_title_logo_area")
                        ) {
                            if (!customLogoUri.isNullOrBlank()) {
                                AsyncImage(
                                    model = customLogoUri,
                                    contentDescription = "Logo",
                                    modifier = Modifier
                                        .size(36.dp)
                                        .clip(CircleShape)
                                )
                            } else {
                                Box(
                                    modifier = Modifier
                                        .size(36.dp)
                                        .clip(CircleShape)
                                        .background(Color(0xFF0D1B68)),
                                    contentAlignment = Alignment.Center
                                ) {
                                    BharatMitraLogo(
                                        size = 32.dp,
                                        showText = false,
                                        animateGlow = false
                                    )
                                }
                            }

                            Spacer(modifier = Modifier.width(10.dp))

                            Column {
                                Text(
                                    text = "BHARAT MITRA",
                                    style = MaterialTheme.typography.titleMedium.copy(
                                        fontWeight = FontWeight.ExtraBold,
                                        letterSpacing = 1.sp
                                    )
                                )
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Box(
                                        modifier = Modifier
                                            .size(7.dp)
                                            .clip(CircleShape)
                                            .background(IndianGreen)
                                    )
                                    Spacer(modifier = Modifier.width(4.dp))
                                    Text(
                                        text = "Live GPS Active • WB Circle",
                                        fontSize = 11.sp,
                                        color = MaterialTheme.colorScheme.onSurfaceVariant
                                    )
                                }
                            }
                        }

                        // Top Actions: Inbox Messages & Elite SOS
                        Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                            // Direct Messages Inbox Icon
                            IconButton(
                                onClick = { showUserInboxDialog = true },
                                modifier = Modifier.size(36.dp).testTag("user_inbox_btn")
                            ) {
                                BadgedBox(
                                    badge = {
                                        if (adminMessages.isNotEmpty()) {
                                            Badge { Text("${adminMessages.size}") }
                                        }
                                    }
                                ) {
                                    Icon(Icons.Default.Notifications, contentDescription = "Messages", tint = AshokaBlue)
                                }
                            }

                            // Quick SOS Button
                            OutlinedButton(
                                onClick = onNavigateToElite,
                                modifier = Modifier
                                    .height(36.dp)
                                    .testTag("top_bar_sos_btn"),
                                colors = ButtonDefaults.outlinedButtonColors(contentColor = SaffronPrimary),
                                border = androidx.compose.foundation.BorderStroke(1.dp, SaffronPrimary),
                                contentPadding = PaddingValues(horizontal = 10.dp, vertical = 4.dp)
                            ) {
                                Icon(Icons.Default.Shield, contentDescription = null, modifier = Modifier.size(16.dp))
                                Spacer(modifier = Modifier.width(4.dp))
                                Text("Elite SOS", fontSize = 12.sp, fontWeight = FontWeight.Bold)
                            }
                        }
                    }

                    // Dynamic Admin Announcement Banner
                    if (appAnnouncement.isNotBlank()) {
                        Surface(
                            color = MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.7f),
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Row(
                                modifier = Modifier.padding(horizontal = 14.dp, vertical = 5.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Icon(Icons.Default.Campaign, contentDescription = null, tint = SaffronPrimary, modifier = Modifier.size(16.dp))
                                Spacer(modifier = Modifier.width(6.dp))
                                Text(
                                    text = appAnnouncement,
                                    fontSize = 11.sp,
                                    fontWeight = FontWeight.Medium,
                                    color = MaterialTheme.colorScheme.onPrimaryContainer,
                                    maxLines = 1
                                )
                            }
                        }
                    }
                }
            }
        },
        bottomBar = {
            // Requirement 3 & 4: Dedicated 4-Tab Navigation: Home, Rent A Car, Hire Driver, Elite ₹29
            // (Dedicated Elite Profile & Form resides exclusively inside the Elite section)
            NavigationBar(
                containerColor = MaterialTheme.colorScheme.surface,
                tonalElevation = 6.dp,
                modifier = Modifier.testTag("main_bottom_nav_bar")
            ) {
                NavigationBarItem(
                    selected = true,
                    onClick = { /* Already on Home */ },
                    icon = { Icon(Icons.Default.Home, contentDescription = "Home") },
                    label = { Text("Home", fontWeight = FontWeight.Bold) },
                    colors = NavigationBarItemDefaults.colors(
                        selectedIconColor = SaffronPrimary,
                        selectedTextColor = SaffronPrimary,
                        indicatorColor = SaffronPrimary.copy(alpha = 0.15f)
                    ),
                    modifier = Modifier.testTag("nav_item_home")
                )

                NavigationBarItem(
                    selected = false,
                    onClick = onNavigateToRentCar,
                    icon = { Icon(Icons.Default.CarRental, contentDescription = "Rent A Car") },
                    label = { Text("Rent Car") },
                    modifier = Modifier.testTag("nav_item_rent_car")
                )

                NavigationBarItem(
                    selected = false,
                    onClick = onNavigateToHireDriver,
                    icon = { Icon(Icons.Default.PersonSearch, contentDescription = "Hire Driver") },
                    label = { Text("Hire Driver") },
                    modifier = Modifier.testTag("nav_item_hire_driver")
                )

                NavigationBarItem(
                    selected = false,
                    onClick = onNavigateToElite,
                    icon = { Icon(Icons.Default.Shield, contentDescription = "Elite ₹29") },
                    label = { Text("Elite ₹29") },
                    modifier = Modifier.testTag("nav_item_elite")
                )
            }
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            // Live Interactive Map Dashboard (Taking top half of the screen)
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(if (isDropConfirmed) 0.85f else 1.15f)
                    .padding(horizontal = 12.dp, vertical = 6.dp)
            ) {
                InteractiveMapCanvas(
                    userGpsTitle = "Barasat",
                    mapDots = mapDots,
                    selectedRadiusKm = 3,
                    onSelectLandmark = { landmark ->
                        viewModel.setDropLocation(landmark)
                        dropSearchQuery = landmark
                        viewModel.confirmDropLocation()
                    }
                )
            }

            // Booking Form Section
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(if (isDropConfirmed) 1.25f else 0.95f),
                shape = RoundedCornerShape(topStart = 22.dp, topEnd = 22.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                elevation = CardDefaults.cardElevation(defaultElevation = 8.dp)
            ) {
                LazyColumn(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(horizontal = 16.dp, vertical = 12.dp),
                    verticalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    // Pickup Location Box (Auto-detected from GPS)
                    item {
                        Surface(
                            shape = RoundedCornerShape(12.dp),
                            color = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.6f),
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(horizontal = 12.dp, vertical = 10.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Box(
                                    modifier = Modifier
                                        .size(10.dp)
                                        .clip(CircleShape)
                                        .background(IndianGreen)
                                )
                                Spacer(modifier = Modifier.width(10.dp))
                                Column(modifier = Modifier.weight(1f)) {
                                    Text("PICKUP (AUTO-DETECTED GPS)", fontSize = 10.sp, fontWeight = FontWeight.Bold, color = IndianGreen)
                                    Text(
                                        text = pickupLocation,
                                        fontSize = 13.sp,
                                        fontWeight = FontWeight.SemiBold,
                                        maxLines = 1
                                    )
                                }
                                IconButton(
                                    onClick = { viewModel.autoDetectPickupLocation() },
                                    modifier = Modifier.size(32.dp).testTag("gps_detect_btn")
                                ) {
                                    Icon(Icons.Default.MyLocation, contentDescription = "Detect Live GPS", tint = SaffronPrimary, modifier = Modifier.size(18.dp))
                                }
                            }
                        }
                    }

                    // Drop Location Input with Live Suggestions
                    item {
                        OutlinedTextField(
                            value = dropSearchQuery,
                            onValueChange = {
                                dropSearchQuery = it
                                viewModel.setDropLocation(it)
                                isSearchingDrop = true
                            },
                            label = { Text("Where to? (Type destination)") },
                            placeholder = { Text("e.g., Barasat Court, Colony More, Madhyamgram") },
                            leadingIcon = {
                                Box(modifier = Modifier.size(10.dp).clip(CircleShape).background(SaffronPrimary))
                            },
                            trailingIcon = {
                                if (dropSearchQuery.isNotBlank()) {
                                    IconButton(onClick = {
                                        dropSearchQuery = ""
                                        viewModel.resetDropLocation()
                                    }) {
                                        Icon(Icons.Default.Clear, contentDescription = "Clear")
                                    }
                                }
                            },
                            singleLine = true,
                            modifier = Modifier
                                .fillMaxWidth()
                                .testTag("drop_location_input"),
                            shape = RoundedCornerShape(12.dp)
                        )
                    }

                    // Live Suggestions List
                    if (!isDropConfirmed) {
                        item {
                            Text(
                                text = "Quick Destination Suggestions:",
                                fontSize = 11.sp,
                                fontWeight = FontWeight.Bold,
                                color = MaterialTheme.colorScheme.onSurfaceVariant,
                                modifier = Modifier.padding(top = 4.dp)
                            )
                        }

                        items(filteredSuggestions.take(4)) { suggestion ->
                            Surface(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .clip(RoundedCornerShape(8.dp))
                                    .clickable {
                                        dropSearchQuery = suggestion.title
                                        viewModel.setDropLocation(suggestion.title)
                                        viewModel.confirmDropLocation()
                                        isSearchingDrop = false
                                    }
                                    .testTag("suggestion_${suggestion.title.replace(" ", "_")}"),
                                color = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.35f)
                            ) {
                                Row(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(horizontal = 12.dp, vertical = 8.dp),
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Icon(Icons.Default.Place, contentDescription = null, tint = SaffronPrimary, modifier = Modifier.size(18.dp))
                                    Spacer(modifier = Modifier.width(10.dp))
                                    Column(modifier = Modifier.weight(1f)) {
                                        Text(suggestion.title, fontSize = 13.sp, fontWeight = FontWeight.SemiBold)
                                        Text(suggestion.subtitle, fontSize = 11.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
                                    }
                                    Text("${suggestion.distanceKmFromCenter} km", fontSize = 11.sp, fontWeight = FontWeight.Bold, color = SaffronPrimary)
                                }
                            }
                        }

                        item {
                            Button(
                                onClick = { viewModel.confirmDropLocation() },
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(48.dp)
                                    .padding(top = 6.dp)
                                    .testTag("confirm_drop_location_btn"),
                                colors = ButtonDefaults.buttonColors(containerColor = SaffronPrimary),
                                shape = RoundedCornerShape(12.dp)
                            ) {
                                Text("CONFIRM DESTINATION & VIEW FARES", fontWeight = FontWeight.Bold, fontSize = 13.sp)
                                Spacer(modifier = Modifier.width(6.dp))
                                Icon(Icons.AutoMirrored.Filled.ArrowForward, contentDescription = null, modifier = Modifier.size(16.dp))
                            }
                        }
                    }

                    // Requirement 2: Fare cards must NOT appear before selecting drop location; show ONLY after confirmation!
                    if (isDropConfirmed) {
                        item {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Column {
                                    Text(
                                        text = "Select Ride & Standard Fare",
                                        style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold)
                                    )
                                    Text(
                                        text = "Distance: ~${String.format("%.1f", estimatedDistanceKm)} km to $dropLocation",
                                        fontSize = 11.sp,
                                        color = MaterialTheme.colorScheme.onSurfaceVariant
                                    )
                                }

                                TextButton(
                                    onClick = { viewModel.resetDropLocation() },
                                    modifier = Modifier.testTag("change_destination_btn")
                                ) {
                                    Text("Change Destination", fontSize = 11.sp, color = SaffronPrimary)
                                }
                            }
                        }

                        // Ride Options Horizontal Selector
                        item {
                            LazyRow(
                                horizontalArrangement = Arrangement.spacedBy(10.dp),
                                modifier = Modifier.fillMaxWidth()
                            ) {
                                items(SampleData.rideOptions) { option ->
                                    val isSelected = selectedRideOption.id == option.id
                                    val fare = if (isAcSelected && option.hasAcOption) option.baseFareAc else option.baseFareNonAc

                                    Card(
                                        modifier = Modifier
                                            .width(135.dp)
                                            .clickable { viewModel.selectRideOption(option) }
                                            .testTag("ride_card_${option.id}"),
                                        shape = RoundedCornerShape(14.dp),
                                        colors = CardDefaults.cardColors(
                                            containerColor = if (isSelected) SaffronPrimary.copy(alpha = 0.12f) else MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.4f)
                                        ),
                                        border = if (isSelected) androidx.compose.foundation.BorderStroke(2.dp, SaffronPrimary) else null
                                    ) {
                                        Column(
                                            modifier = Modifier.padding(10.dp),
                                            verticalArrangement = Arrangement.spacedBy(4.dp)
                                        ) {
                                            Row(
                                                modifier = Modifier.fillMaxWidth(),
                                                horizontalArrangement = Arrangement.SpaceBetween,
                                                verticalAlignment = Alignment.CenterVertically
                                            ) {
                                                Text(
                                                    text = when (option.id) {
                                                        "bike" -> "🏍️"
                                                        "toto" -> "🛺"
                                                        "auto" -> "🛺"
                                                        "four_seater" -> "🚗"
                                                        else -> "🚙"
                                                    },
                                                    fontSize = 20.sp
                                                )
                                                Surface(
                                                    color = IndianGreen.copy(alpha = 0.2f),
                                                    shape = RoundedCornerShape(6.dp)
                                                ) {
                                                    Text(
                                                        "${option.etaMinutes}m",
                                                        fontSize = 10.sp,
                                                        fontWeight = FontWeight.Bold,
                                                        color = IndianGreen,
                                                        modifier = Modifier.padding(horizontal = 4.dp, vertical = 2.dp)
                                                    )
                                                }
                                            }

                                            Text(
                                                text = option.name,
                                                fontWeight = FontWeight.Bold,
                                                fontSize = 13.sp
                                            )
                                            Text(
                                                text = option.capacity,
                                                fontSize = 10.sp,
                                                color = MaterialTheme.colorScheme.onSurfaceVariant
                                            )
                                            Text(
                                                text = "₹$fare",
                                                fontWeight = FontWeight.ExtraBold,
                                                fontSize = 16.sp,
                                                color = SaffronPrimary
                                            )
                                        }
                                    }
                                }
                            }
                        }

                        // AC / Non-AC Switch for 4-Seater & 7-Seater
                        if (selectedRideOption.hasAcOption) {
                            item {
                                Surface(
                                    shape = RoundedCornerShape(10.dp),
                                    color = MaterialTheme.colorScheme.secondaryContainer.copy(alpha = 0.5f),
                                    modifier = Modifier.fillMaxWidth()
                                ) {
                                    Row(
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .padding(horizontal = 12.dp, vertical = 8.dp),
                                        horizontalArrangement = Arrangement.SpaceBetween,
                                        verticalAlignment = Alignment.CenterVertically
                                    ) {
                                        Row(verticalAlignment = Alignment.CenterVertically) {
                                            Icon(Icons.Default.AcUnit, contentDescription = null, tint = AshokaBlue, modifier = Modifier.size(18.dp))
                                            Spacer(modifier = Modifier.width(8.dp))
                                            Column {
                                                Text("Air Conditioning (AC)", fontSize = 12.sp, fontWeight = FontWeight.Bold)
                                                Text(if (isAcSelected) "AC Active (+₹${selectedRideOption.baseFareAc - selectedRideOption.baseFareNonAc})" else "Non-AC Standard", fontSize = 10.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
                                            }
                                        }
                                        Switch(
                                            checked = isAcSelected,
                                            onCheckedChange = { viewModel.toggleAc(it) },
                                            modifier = Modifier.testTag("ac_toggle_switch")
                                        )
                                    }
                                }
                            }
                        }

                        // Confirm & Book Ride Button
                        item {
                            Button(
                                onClick = { viewModel.bookCurrentRide() },
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(52.dp)
                                    .padding(top = 4.dp)
                                    .testTag("book_ride_button"),
                                colors = ButtonDefaults.buttonColors(containerColor = SaffronPrimary),
                                shape = RoundedCornerShape(14.dp)
                            ) {
                                Text(
                                    text = "BOOK ${selectedRideOption.name.uppercase()} • ₹$calculatedFare",
                                    fontSize = 14.sp,
                                    fontWeight = FontWeight.ExtraBold,
                                    letterSpacing = 0.5.sp
                                )
                            }
                        }
                    }

                    // Help & Complaints Desk button
                    item {
                        TextButton(
                            onClick = { showComplaintDialog = true },
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Icon(Icons.Default.HelpOutline, contentDescription = null, modifier = Modifier.size(16.dp))
                            Spacer(modifier = Modifier.width(6.dp))
                            Text("Need Assistance? File a Complaint with Bharat Mitra Admin", fontSize = 11.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
                        }
                    }
                }
            }
        }
    }

    // Direct Messages & Inbox Dialog
    if (showUserInboxDialog) {
        Dialog(onDismissRequest = { showUserInboxDialog = false }) {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .fillMaxHeight(0.7f),
                shape = RoundedCornerShape(20.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
            ) {
                Column(modifier = Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(10.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text("Official Announcements & Inbox", fontWeight = FontWeight.Bold, fontSize = 16.sp)
                        IconButton(onClick = { showUserInboxDialog = false }) {
                            Icon(Icons.Default.Close, contentDescription = "Close")
                        }
                    }
                    HorizontalDivider()
                    LazyColumn(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                        items(adminMessages) { msg ->
                            Card(
                                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f))
                            ) {
                                Column(modifier = Modifier.padding(12.dp), verticalArrangement = Arrangement.spacedBy(4.dp)) {
                                    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                                        Text(msg.title, fontWeight = FontWeight.Bold, fontSize = 13.sp)
                                        Text(msg.sentAt, fontSize = 10.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
                                    }
                                    Text(msg.body, fontSize = 12.sp)
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    // User Complaint Dialog
    if (showComplaintDialog) {
        var issueType by remember { mutableStateOf("Service / Ride Issue") }
        var complaintDetails by remember { mutableStateOf("") }

        Dialog(onDismissRequest = { showComplaintDialog = false }) {
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(20.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
            ) {
                Column(modifier = Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(10.dp)) {
                    Text("File a Complaint to Admin", fontWeight = FontWeight.Bold, fontSize = 16.sp)
                    Text("Complaints are monitored 24/7 by the Bharat Mitra Admin Team.", fontSize = 11.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)

                    OutlinedTextField(
                        value = issueType,
                        onValueChange = { issueType = it },
                        label = { Text("Issue Category") },
                        singleLine = true,
                        modifier = Modifier.fillMaxWidth()
                    )

                    OutlinedTextField(
                        value = complaintDetails,
                        onValueChange = { complaintDetails = it },
                        label = { Text("Describe the issue in detail") },
                        minLines = 3,
                        modifier = Modifier.fillMaxWidth()
                    )

                    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.End) {
                        TextButton(onClick = { showComplaintDialog = false }) {
                            Text("Cancel")
                        }
                        Button(
                            onClick = {
                                if (complaintDetails.isNotBlank()) {
                                    viewModel.submitUserComplaint(issueType, complaintDetails)
                                    showComplaintDialog = false
                                } else {
                                    Toast.makeText(context, "Please describe your issue", Toast.LENGTH_SHORT).show()
                                }
                            },
                            colors = ButtonDefaults.buttonColors(containerColor = SaffronPrimary)
                        ) {
                            Text("Submit Complaint")
                        }
                    }
                }
            }
        }
    }
}
