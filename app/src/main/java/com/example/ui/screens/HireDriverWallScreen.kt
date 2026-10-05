package com.example.ui.screens

import android.widget.Toast
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
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.model.HireDriverItem
import com.example.ui.theme.*
import com.example.viewmodel.MainViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HireDriverWallScreen(
    viewModel: MainViewModel,
    onBack: () -> Unit,
    onBecomeHireDriver: () -> Unit
) {
    BackHandler { onBack() }
    val context = LocalContext.current
    val driversList by viewModel.hireDrivers.collectAsState()

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Column {
                        Text("Hire Driver", fontWeight = FontWeight.Bold, fontSize = 18.sp, color = Color.White)
                        Text("Verified Commercial Drivers On-Demand", fontSize = 11.sp, color = BharatOrangeLight)
                    }
                },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back", tint = Color.White)
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = BharatDarkBlue)
            )
        },
        floatingActionButton = {
            // Become Hire Driver (+) FAB
            FloatingActionButton(
                onClick = onBecomeHireDriver,
                containerColor = BharatGreen,
                contentColor = Color.White,
                shape = RoundedCornerShape(16.dp),
                modifier = Modifier
                    .padding(bottom = 12.dp)
                    .testTag("become_hire_driver_fab")
            ) {
                Row(
                    modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(Icons.Default.Add, contentDescription = null)
                    Spacer(modifier = Modifier.width(6.dp))
                    Text("Join as Driver", fontWeight = FontWeight.Bold)
                }
            }
        }
    ) { innerPadding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .background(LightBackground)
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(14.dp)
        ) {
            // Header Info Card
            item {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(containerColor = BharatGreen.copy(alpha = 0.08f)),
                    border = BorderStroke(1.dp, BharatGreen.copy(alpha = 0.3f))
                ) {
                    Row(
                        modifier = Modifier.padding(14.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(Icons.Default.VerifiedUser, contentDescription = null, tint = BharatGreen)
                        Spacer(modifier = Modifier.width(10.dp))
                        Text(
                            text = "Hire trusted professional drivers for your own personal car or commercial fleet. Transparent hourly pricing.",
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Medium,
                            color = BharatDarkBlue
                        )
                    }
                }
            }

            // Cards: Amit Sharma, Priya Kumari, Rahul Mehta
            items(driversList) { driver ->
                Card(
                    modifier = Modifier.fillMaxWidth().testTag("hire_card_${driver.id}"),
                    shape = RoundedCornerShape(20.dp),
                    elevation = CardDefaults.cardElevation(defaultElevation = 8.dp),
                    border = BorderStroke(0.5.dp, CardBorderColor),
                    colors = CardDefaults.cardColors(containerColor = Color.White)
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        // Driver Photo Avatar with Green Verified Badge
                        Box(modifier = Modifier.size(54.dp)) {
                            Box(
                                modifier = Modifier
                                    .size(48.dp)
                                    .clip(CircleShape)
                                    .background(BharatDarkBlue),
                                contentAlignment = Alignment.Center
                            ) {
                                Icon(Icons.Default.Person, contentDescription = null, tint = Color.White, modifier = Modifier.size(28.dp))
                            }
                            Box(
                                modifier = Modifier
                                    .align(Alignment.BottomEnd)
                                    .clip(RoundedCornerShape(4.dp))
                                    .background(BharatGreen)
                                    .padding(horizontal = 4.dp, vertical = 1.dp)
                            ) {
                                Text("✓", color = Color.White, fontSize = 9.sp, fontWeight = FontWeight.Bold)
                            }
                        }

                        Spacer(modifier = Modifier.width(12.dp))

                        Column(modifier = Modifier.weight(1f)) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Text(
                                    text = driver.name,
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 16.sp,
                                    color = BharatDarkBlue
                                )
                                Spacer(modifier = Modifier.width(6.dp))
                                Surface(
                                    color = BharatOrange.copy(alpha = 0.15f),
                                    shape = RoundedCornerShape(4.dp)
                                ) {
                                    Text(
                                        text = "${driver.rating} ★",
                                        color = BharatOrange,
                                        fontWeight = FontWeight.Bold,
                                        fontSize = 11.sp,
                                        modifier = Modifier.padding(horizontal = 4.dp, vertical = 1.dp)
                                    )
                                }
                            }

                            Spacer(modifier = Modifier.height(2.dp))
                            Text(
                                text = "${driver.badge} • ${driver.city}",
                                fontSize = 12.sp,
                                color = Color.Gray
                            )

                            Spacer(modifier = Modifier.height(4.dp))
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Text(
                                    text = "₹${driver.ratePerHour}/hr",
                                    fontWeight = FontWeight.Black,
                                    fontSize = 14.sp,
                                    color = BharatGreen
                                )
                                Spacer(modifier = Modifier.width(10.dp))
                                Text(
                                    text = "• ${driver.distanceKm} km away",
                                    fontSize = 12.sp,
                                    color = Color.DarkGray
                                )
                            }
                        }

                        // Hire Button
                        Box(
                            modifier = Modifier
                                .clip(RoundedCornerShape(12.dp))
                                .background(
                                    brush = Brush.horizontalGradient(
                                        listOf(BharatOrange, BharatGreen)
                                    )
                                )
                                .clickable {
                                    Toast.makeText(context, "Hiring requested for ${driver.name}! Driver will call you shortly.", Toast.LENGTH_LONG).show()
                                }
                                .padding(horizontal = 16.dp, vertical = 10.dp)
                                .testTag("hire_btn_${driver.id}"),
                            contentAlignment = Alignment.Center
                        ) {
                            Text("Hire", color = Color.White, fontWeight = FontWeight.Bold, fontSize = 14.sp)
                        }
                    }
                }
            }

            item {
                Spacer(modifier = Modifier.height(70.dp))
            }
        }
    }
}
