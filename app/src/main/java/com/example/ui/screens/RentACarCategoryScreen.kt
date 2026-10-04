package com.example.ui.screens

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.background
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
import com.example.model.RentACarOwnerProfile
import com.example.ui.theme.AshokaBlue
import com.example.ui.theme.IndianGreen
import com.example.ui.theme.NavySecondary
import com.example.ui.theme.SaffronPrimary
import com.example.viewmodel.MainViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RentACarCategoryScreen(
    viewModel: MainViewModel,
    onBack: () -> Unit,
    onCreateProfile: () -> Unit
) {
    BackHandler { onBack() }

    val rentOwners by viewModel.rentOwners.collectAsState()

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Column {
                        Text("Rent A Car Category", fontWeight = FontWeight.Bold, fontSize = 18.sp)
                        Text("গাড়ি ভাড়া ও ট্রাভেলস মালিকদের জন্য", fontSize = 11.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
                    }
                },
                navigationIcon = {
                    IconButton(onClick = onBack, modifier = Modifier.testTag("rent_category_back_btn")) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = MaterialTheme.colorScheme.surface)
            )
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = onCreateProfile,
                containerColor = Color(0xFF1DB954),
                contentColor = Color.White,
                shape = RoundedCornerShape(16.dp),
                modifier = Modifier.testTag("rent_create_profile_fab")
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
            // Rent a Car Business Info Card
            item {
                Spacer(modifier = Modifier.height(4.dp))
                Card(
                    modifier = Modifier.fillMaxWidth().testTag("rent_business_info_card"),
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.secondaryContainer.copy(alpha = 0.65f))
                ) {
                    Column(modifier = Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Box(
                                modifier = Modifier
                                    .size(40.dp)
                                    .clip(CircleShape)
                                    .background(AshokaBlue),
                                contentAlignment = Alignment.Center
                            ) {
                                Icon(Icons.Default.VpnKey, contentDescription = null, tint = Color.White, modifier = Modifier.size(22.dp))
                            }
                            Spacer(modifier = Modifier.width(10.dp))
                            Column {
                                Text("Rent A Car Business Hub", fontWeight = FontWeight.Bold, fontSize = 16.sp)
                                Text("Direct customer booking • 0% Commission on Self-drive", fontSize = 11.sp, color = MaterialTheme.colorScheme.onSecondaryContainer)
                            }
                        }

                        HorizontalDivider(modifier = Modifier.padding(vertical = 4.dp))

                        Text(
                            text = "✓ List multi-car fleets (Innova, Scorpio, Dzire, Hatchbacks)\n" +
                                    "✓ Daily / Hourly rental options with verified chauffeurs\n" +
                                    "✓ Direct company / owner billing with verified GST / Trade Licence\n" +
                                    "✓ Guaranteed mutual protection on West Bengal state routes",
                            fontSize = 12.sp,
                            lineHeight = 18.sp,
                            color = MaterialTheme.colorScheme.onSecondaryContainer
                        )
                    }
                }
            }

            // Existing Rent Owners List Header
            item {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "Registered Fleet Owners (${rentOwners.size})",
                        style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold)
                    )
                    Text("Verified Fleet Agency", fontSize = 11.sp, color = IndianGreen, fontWeight = FontWeight.Bold)
                }
            }

            // List of Existing Rent Owners
            items(rentOwners) { owner ->
                RentOwnerCardItem(owner = owner)
            }

            item {
                Spacer(modifier = Modifier.height(80.dp))
            }
        }
    }
}

@Composable
private fun RentOwnerCardItem(owner: RentACarOwnerProfile) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .testTag("rent_owner_card_${owner.id}"),
        shape = RoundedCornerShape(14.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(modifier = Modifier.padding(14.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Box(
                    modifier = Modifier
                        .size(46.dp)
                        .clip(CircleShape)
                        .background(NavySecondary),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(Icons.Default.DirectionsCar, contentDescription = null, tint = Color.White, modifier = Modifier.size(24.dp))
                }

                Spacer(modifier = Modifier.width(12.dp))

                Column(modifier = Modifier.weight(1f)) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text(owner.ownerName, fontWeight = FontWeight.Bold, fontSize = 14.sp)
                        Spacer(modifier = Modifier.width(6.dp))
                        if (owner.isVerified) {
                            Icon(Icons.Default.Verified, contentDescription = "Verified Agency", tint = IndianGreen, modifier = Modifier.size(15.dp))
                        }
                    }
                    if (owner.companyName.isNotBlank()) {
                        Text(
                            text = owner.companyName,
                            fontSize = 12.sp,
                            fontWeight = FontWeight.SemiBold,
                            color = AshokaBlue
                        )
                    }
                    Text(
                        text = owner.address,
                        fontSize = 11.sp,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }

                Surface(
                    color = IndianGreen.copy(alpha = 0.15f),
                    shape = RoundedCornerShape(6.dp)
                ) {
                    Text(
                        text = "${owner.cars.size} Cars",
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold,
                        color = IndianGreen,
                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 3.dp)
                    )
                }
            }

            HorizontalDivider()

            // Cars summary
            Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
                owner.cars.forEach { car ->
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(Icons.Default.Check, contentDescription = null, tint = IndianGreen, modifier = Modifier.size(14.dp))
                            Spacer(modifier = Modifier.width(4.dp))
                            Text(car.model, fontSize = 12.sp, fontWeight = FontWeight.Medium)
                        }
                        Text(car.vehicleNumber, fontSize = 11.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
                    }
                }
            }

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Licence: ${owner.businessLicence}",
                    fontSize = 10.sp,
                    color = Color.Gray
                )
                Text(
                    text = owner.phone,
                    fontSize = 11.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = SaffronPrimary
                )
            }
        }
    }
}
