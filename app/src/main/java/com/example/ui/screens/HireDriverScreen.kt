package com.example.ui.screens

import android.widget.Toast
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.WorkspacePremium
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.hapticfeedback.HapticFeedbackType
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.viewmodel.MainViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HireDriverScreen(
    viewModel: MainViewModel,
    onBack: () -> Unit = {},
    onNavigateToDriverCategory: () -> Unit = {}
) {
    val context = LocalContext.current
    val haptic = LocalHapticFeedback.current

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = "Hire Driver",
                        style = MaterialTheme.typography.titleLarge.copy(
                            fontWeight = FontWeight.Bold,
                            color = Color.White
                        )
                    )
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Color(0xFF0D1B68)
                )
            )
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = {
                    haptic.performHapticFeedback(HapticFeedbackType.LongPress)
                    onNavigateToDriverCategory()
                },
                containerColor = Color(0xFF138808),
                contentColor = Color.White,
                modifier = Modifier
                    .padding(bottom = 80.dp)
                    .testTag("driver_create_profile_fab")
            ) {
                Row(
                    modifier = Modifier.padding(horizontal = 16.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(Icons.Default.Add, contentDescription = null)
                    Spacer(modifier = Modifier.width(8.dp))
                    Text("Driver - Create Profile", fontWeight = FontWeight.Bold)
                }
            }
        }
    ) { innerPadding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(14.dp)
        ) {
            item {
                DriverHireCard(
                    name = "Subhasish Mondal",
                    rating = "4.9 ★",
                    details = "12 yrs experience • Manual/Auto • Police Verified",
                    zone = "Barasat / Salt Lake",
                    onHire = {
                        Toast.makeText(context, "Driver hired: Subhasish Mondal", Toast.LENGTH_SHORT).show()
                    },
                    testTag = "hire_subhasish_btn"
                )
            }

            item {
                DriverHireCard(
                    name = "Debashis Roy",
                    rating = "4.8 ★",
                    details = "8 yrs experience • Night Specialist • Outstation Ready",
                    zone = "New Town / Kolkata",
                    onHire = {
                        Toast.makeText(context, "Driver hired: Debashis Roy", Toast.LENGTH_SHORT).show()
                    },
                    testTag = "hire_debashis_btn"
                )
            }

            item {
                DriverHireCard(
                    name = "Rajesh Das",
                    rating = "5.0 ★",
                    details = "15 yrs experience • Luxury Cars / SUVs",
                    zone = "Howrah / Airport Circle",
                    onHire = {
                        Toast.makeText(context, "Driver hired: Rajesh Das", Toast.LENGTH_SHORT).show()
                    },
                    testTag = "hire_rajesh_btn"
                )
            }
        }
    }
}

@Composable
private fun DriverHireCard(
    name: String,
    rating: String,
    details: String,
    zone: String,
    onHire: () -> Unit,
    testTag: String
) {
    val isEliteDriver = rating.contains("4.9") || rating.contains("5.0")

    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        elevation = CardDefaults.cardElevation(defaultElevation = 8.dp),
        border = BorderStroke(0.5.dp, Color(0xFFE0E0E0))
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Driver photo avatar with Verified badge at bottom-end
            Box(
                modifier = Modifier.size(54.dp)
            ) {
                Box(
                    modifier = Modifier
                        .size(48.dp)
                        .clip(CircleShape)
                        .background(Color(0xFF0D1B68)),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Default.Person,
                        contentDescription = null,
                        tint = Color.White,
                        modifier = Modifier.size(28.dp)
                    )
                }

                // Trust badge: small box bottom-end with Green background #138808, text "✓ Verified" white 10sp
                Box(
                    modifier = Modifier
                        .align(Alignment.BottomEnd)
                        .clip(RoundedCornerShape(4.dp))
                        .background(Color(0xFF138808))
                        .padding(horizontal = 4.dp, vertical = 1.dp)
                ) {
                    Text(
                        text = "✓ Verified",
                        color = Color.White,
                        fontSize = 10.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
            }

            Spacer(modifier = Modifier.width(14.dp))

            Column(modifier = Modifier.weight(1f)) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text(
                            text = name,
                            style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold)
                        )
                        if (isEliteDriver) {
                            Spacer(modifier = Modifier.width(4.dp))
                            // Elite crown icon near 4.9 rated drivers
                            Icon(
                                imageVector = Icons.Default.WorkspacePremium,
                                contentDescription = "Elite Driver Crown",
                                tint = Color(0xFFFA8520),
                                modifier = Modifier.size(18.dp)
                            )
                        }
                    }

                    Text(
                        text = rating,
                        style = MaterialTheme.typography.bodyMedium.copy(
                            color = Color(0xFFFA8520),
                            fontWeight = FontWeight.Bold
                        )
                    )
                }

                Spacer(modifier = Modifier.height(4.dp))

                Text(
                    text = details,
                    style = MaterialTheme.typography.bodySmall.copy(fontSize = 12.sp)
                )

                Spacer(modifier = Modifier.height(2.dp))

                Text(
                    text = "Zone: $zone",
                    style = MaterialTheme.typography.bodySmall.copy(fontSize = 12.sp, color = Color.Gray)
                )
            }

            Spacer(modifier = Modifier.width(10.dp))

            // Premium gradient button
            Box(
                modifier = Modifier
                    .clip(RoundedCornerShape(12.dp))
                    .background(
                        brush = Brush.horizontalGradient(
                            listOf(Color(0xFFFA8520), Color(0xFF138808))
                        )
                    )
                    .clickable { onHire() }
                    .padding(horizontal = 16.dp, vertical = 10.dp)
                    .testTag(testTag),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "Hire",
                    color = Color.White,
                    fontWeight = FontWeight.Bold,
                    fontSize = 14.sp
                )
            }
        }
    }
}
