package com.example.ui.screens

import android.widget.Toast
import androidx.activity.compose.BackHandler
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
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.components.PremiumSuccessDialog
import com.example.ui.theme.*
import com.example.viewmodel.MainViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RatingScreen(
    viewModel: MainViewModel,
    onBack: () -> Unit,
    onFinished: () -> Unit
) {
    BackHandler { onBack() }
    val context = LocalContext.current
    var selectedTab by remember { mutableStateOf(0) } // 0 = Rider to Driver, 1 = Driver to Passenger

    // Tab 1 state: Rider Feedback
    var riderRating by remember { mutableStateOf(5) }
    var cleanVehicle by remember { mutableStateOf(true) }
    var politeDriver by remember { mutableStateOf(true) }
    var onTime by remember { mutableStateOf(true) }
    var safeDriving by remember { mutableStateOf(true) }
    var goodRoute by remember { mutableStateOf(true) }
    var selectedTip by remember { mutableStateOf(50) }
    var riderComment by remember { mutableStateOf("") }

    // Tab 2 state: Driver to Customer Feedback
    var driverRating by remember { mutableStateOf(5) }
    var passengerPunctual by remember { mutableStateOf(true) }

    var showSuccessDialog by remember { mutableStateOf(false) }

    if (showSuccessDialog) {
        PremiumSuccessDialog(
            title = "Feedback Submitted!",
            message = "Thank you for rating with Bharat Mitra. Your feedback helps maintain trusted 5-star standard rides.",
            onDismiss = {
                showSuccessDialog = false
                onFinished()
            }
        )
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Trip Feedback & Rating", fontWeight = FontWeight.Bold, color = Color.White) },
                navigationIcon = {
                    IconButton(onClick = onBack) {
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
            // Tab Switcher: Screen 1 Rider Feedback / Screen 2 Driver to Customer
            TabRow(
                selectedTabIndex = selectedTab,
                containerColor = Color.White,
                contentColor = BharatDarkBlue
            ) {
                Tab(
                    selected = selectedTab == 0,
                    onClick = { selectedTab = 0 },
                    text = { Text("Rider Feedback", fontWeight = if (selectedTab == 0) FontWeight.Bold else FontWeight.Normal) }
                )
                Tab(
                    selected = selectedTab == 1,
                    onClick = { selectedTab = 1 },
                    text = { Text("Driver to Customer", fontWeight = if (selectedTab == 1) FontWeight.Bold else FontWeight.Normal) }
                )
            }

            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                if (selectedTab == 0) {
                    // SCREEN 1: RIDER FEEDBACK
                    // Ride Completed Green Tick
                    item {
                        Column(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalAlignment = Alignment.CenterHorizontally,
                            verticalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            Box(
                                modifier = Modifier
                                    .size(60.dp)
                                    .clip(CircleShape)
                                    .background(BharatGreen.copy(alpha = 0.15f)),
                                contentAlignment = Alignment.Center
                            ) {
                                Icon(Icons.Default.CheckCircle, contentDescription = null, tint = BharatGreen, modifier = Modifier.size(38.dp))
                            }
                            Text("Ride Completed", fontWeight = FontWeight.Black, fontSize = 20.sp, color = BharatDarkBlue)
                            Text("Driver Rohan • Toyota Sedan DL 01 AB 1234", fontSize = 13.sp, color = Color.Gray)
                        }
                    }

                    // How was your ride? 5 Stars Large
                    item {
                        Card(
                            modifier = Modifier.fillMaxWidth(),
                            shape = RoundedCornerShape(20.dp),
                            colors = CardDefaults.cardColors(containerColor = Color.White),
                            elevation = CardDefaults.cardElevation(defaultElevation = 6.dp),
                            border = BorderStroke(0.5.dp, CardBorderColor)
                        ) {
                            Column(
                                modifier = Modifier.padding(18.dp),
                                horizontalAlignment = Alignment.CenterHorizontally,
                                verticalArrangement = Arrangement.spacedBy(12.dp)
                            ) {
                                Text("How was your ride?", fontWeight = FontWeight.Bold, fontSize = 16.sp, color = BharatDarkBlue)

                                Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                                    for (star in 1..5) {
                                        val isFilled = star <= riderRating
                                        Icon(
                                            imageVector = if (isFilled) Icons.Default.Star else Icons.Default.StarBorder,
                                            contentDescription = "$star stars",
                                            tint = if (isFilled) BharatOrange else Color.Gray,
                                            modifier = Modifier
                                                .size(40.dp)
                                                .clickable { riderRating = star }
                                                .testTag("rider_star_$star")
                                        )
                                    }
                                }

                                Text(
                                    text = if (riderRating == 5) "Excellent 5-Star Experience!" else "$riderRating Stars",
                                    fontWeight = FontWeight.Bold,
                                    color = BharatOrange,
                                    fontSize = 14.sp
                                )
                            }
                        }
                    }

                    // What went well? Checkboxes with Green Tick
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
                                verticalArrangement = Arrangement.spacedBy(10.dp)
                            ) {
                                Text("What went well?", fontWeight = FontWeight.Bold, fontSize = 15.sp, color = BharatDarkBlue)

                                RatingCheckboxRow("Clean Vehicle", cleanVehicle) { cleanVehicle = !cleanVehicle }
                                RatingCheckboxRow("Polite Driver", politeDriver) { politeDriver = !politeDriver }
                                RatingCheckboxRow("On Time", onTime) { onTime = !onTime }
                                RatingCheckboxRow("Safe Driving", safeDriving) { safeDriving = !safeDriving }
                                RatingCheckboxRow("Good Route", goodRoute) { goodRoute = !goodRoute }
                            }
                        }
                    }

                    // Tip Driver: ₹20, ₹50, ₹100 Orange Chips
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
                                verticalArrangement = Arrangement.spacedBy(10.dp)
                            ) {
                                Text("Tip Driver (100% goes to Rohan)", fontWeight = FontWeight.Bold, fontSize = 14.sp, color = BharatDarkBlue)
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                                ) {
                                    listOf(20, 50, 100).forEach { tip ->
                                        val isSelected = selectedTip == tip
                                        FilterChip(
                                            selected = isSelected,
                                            onClick = { selectedTip = if (isSelected) 0 else tip },
                                            label = { Text("₹$tip", fontWeight = FontWeight.Bold, fontSize = 14.sp) },
                                            colors = FilterChipDefaults.filterChipColors(
                                                selectedContainerColor = BharatOrange,
                                                selectedLabelColor = Color.White
                                            ),
                                            shape = RoundedCornerShape(12.dp),
                                            modifier = Modifier.weight(1f)
                                        )
                                    }
                                }
                            }
                        }
                    }

                    // Comments Box: Write your experience
                    item {
                        OutlinedTextField(
                            value = riderComment,
                            onValueChange = { riderComment = it },
                            label = { Text("Write your experience (Optional)") },
                            placeholder = { Text("The car was spotless and driver was very helpful...") },
                            modifier = Modifier.fillMaxWidth(),
                            minLines = 3,
                            shape = RoundedCornerShape(14.dp)
                        )
                    }

                    // Submit Rating Orange Button
                    item {
                        Button(
                            onClick = { showSuccessDialog = true },
                            colors = ButtonDefaults.buttonColors(containerColor = BharatOrange),
                            shape = RoundedCornerShape(12.dp),
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(50.dp)
                                .testTag("submit_rider_rating_btn")
                        ) {
                            Text("Submit Rating", fontWeight = FontWeight.Bold, fontSize = 15.sp, color = Color.White)
                        }
                    }
                } else {
                    // SCREEN 2: DRIVER TO CUSTOMER
                    item {
                        Card(
                            modifier = Modifier.fillMaxWidth(),
                            shape = RoundedCornerShape(20.dp),
                            colors = CardDefaults.cardColors(containerColor = Color.White),
                            elevation = CardDefaults.cardElevation(defaultElevation = 6.dp),
                            border = BorderStroke(0.5.dp, CardBorderColor)
                        ) {
                            Column(
                                modifier = Modifier.padding(18.dp),
                                horizontalAlignment = Alignment.CenterHorizontally,
                                verticalArrangement = Arrangement.spacedBy(14.dp)
                            ) {
                                Box(
                                    modifier = Modifier
                                        .size(54.dp)
                                        .clip(CircleShape)
                                        .background(BharatDarkBlue),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Icon(Icons.Default.Person, contentDescription = null, tint = Color.White, modifier = Modifier.size(32.dp))
                                }

                                Text("Rate your passenger Aman", fontWeight = FontWeight.Bold, fontSize = 17.sp, color = BharatDarkBlue)

                                Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                                    for (star in 1..5) {
                                        val isFilled = star <= driverRating
                                        Icon(
                                            imageVector = if (isFilled) Icons.Default.Star else Icons.Default.StarBorder,
                                            contentDescription = "$star stars",
                                            tint = if (isFilled) BharatGreen else Color.Gray,
                                            modifier = Modifier
                                                .size(40.dp)
                                                .clickable { driverRating = star }
                                        )
                                    }
                                }

                                RatingCheckboxRow("Passenger was punctual & respectful", passengerPunctual) {
                                    passengerPunctual = !passengerPunctual
                                }
                            }
                        }
                    }

                    // Additional Actions: Block User (Orange), Report (Red)
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
                                Text("Additional Safety Actions", fontWeight = FontWeight.Bold, fontSize = 14.sp, color = BharatDarkBlue)

                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                                ) {
                                    Button(
                                        onClick = {
                                            Toast.makeText(context, "Passenger Aman has been marked to prevent future matching.", Toast.LENGTH_SHORT).show()
                                        },
                                        colors = ButtonDefaults.buttonColors(containerColor = BharatOrange),
                                        shape = RoundedCornerShape(10.dp),
                                        modifier = Modifier.weight(1f).height(44.dp).testTag("driver_block_user_btn")
                                    ) {
                                        Icon(Icons.Default.Block, contentDescription = null, modifier = Modifier.size(16.dp))
                                        Spacer(modifier = Modifier.width(6.dp))
                                        Text("Block User", fontWeight = FontWeight.Bold, fontSize = 12.sp)
                                    }

                                    Button(
                                        onClick = {
                                            Toast.makeText(context, "Incident report submitted to Bharat Mitra Trust & Safety team.", Toast.LENGTH_SHORT).show()
                                        },
                                        colors = ButtonDefaults.buttonColors(containerColor = EmergencyRed),
                                        shape = RoundedCornerShape(10.dp),
                                        modifier = Modifier.weight(1f).height(44.dp).testTag("driver_report_user_btn")
                                    ) {
                                        Icon(Icons.Default.Report, contentDescription = null, modifier = Modifier.size(16.dp))
                                        Spacer(modifier = Modifier.width(6.dp))
                                        Text("Report", fontWeight = FontWeight.Bold, fontSize = 12.sp)
                                    }
                                }
                            }
                        }
                    }

                    // Submit Rating Green Button
                    item {
                        Button(
                            onClick = { showSuccessDialog = true },
                            colors = ButtonDefaults.buttonColors(containerColor = BharatGreen),
                            shape = RoundedCornerShape(12.dp),
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(50.dp)
                                .testTag("submit_driver_rating_btn")
                        ) {
                            Text("Submit Rating", fontWeight = FontWeight.Bold, fontSize = 15.sp, color = Color.White)
                        }
                    }
                }

                item {
                    Spacer(modifier = Modifier.height(20.dp))
                }
            }
        }
    }
}

@Composable
private fun RatingCheckboxRow(
    title: String,
    isChecked: Boolean,
    onToggle: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onToggle() }
            .padding(vertical = 4.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Checkbox(
            checked = isChecked,
            onCheckedChange = { onToggle() },
            colors = CheckboxDefaults.colors(checkedColor = BharatGreen)
        )
        Spacer(modifier = Modifier.width(6.dp))
        Text(title, fontSize = 13.sp, fontWeight = FontWeight.Medium, color = BharatDarkBlue)
    }
}
