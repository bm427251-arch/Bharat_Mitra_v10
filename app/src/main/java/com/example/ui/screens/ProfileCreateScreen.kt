package com.example.ui.screens

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
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
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.model.ScreenState
import com.example.ui.theme.AshokaBlue
import com.example.ui.theme.IndianGreen
import com.example.ui.theme.NavySecondary
import com.example.ui.theme.SaffronPrimary
import com.example.viewmodel.MainViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProfileCreateScreen(
    viewModel: MainViewModel,
    onBack: () -> Unit,
    onNavigateToAdmin: () -> Unit
) {
    val context = LocalContext.current
    var userName by remember { mutableStateOf("Rajdeep Banerjee") }
    var userPhone by remember { mutableStateOf("+91 98301 55678") }
    var emergencyContact1 by remember { mutableStateOf("+91 98310 99421 (Brother)") }
    var emergencyContact2 by remember { mutableStateOf("112 (National Emergency Helpline)") }
    var homeAddress by remember { mutableStateOf("Colony More, Jessore Road, Barasat") }
    var workAddress by remember { mutableStateOf("Barasat Court Complex, Kachhari Road") }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Column {
                        Text("Profile & Identity", fontWeight = FontWeight.Bold, fontSize = 18.sp)
                        Text("Personal KYC, emergency contacts & preferences", fontSize = 11.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
                    }
                },
                navigationIcon = {
                    IconButton(onClick = onBack, modifier = Modifier.testTag("profile_back_btn")) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
                    }
                },
                actions = {
                    IconButton(
                        onClick = onNavigateToAdmin,
                        modifier = Modifier.testTag("profile_admin_btn")
                    ) {
                        Icon(Icons.Default.AdminPanelSettings, contentDescription = "Admin Entry", tint = AshokaBlue)
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = MaterialTheme.colorScheme.surface)
            )
        }
    ) { innerPadding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(horizontal = 16.dp),
            verticalArrangement = Arrangement.spacedBy(14.dp)
        ) {
            // Profile Card Header
            item {
                Card(
                    modifier = Modifier.fillMaxWidth().testTag("profile_header_card"),
                    shape = RoundedCornerShape(18.dp),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                    elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Box(
                            modifier = Modifier
                                .size(64.dp)
                                .clip(CircleShape)
                                .background(NavySecondary)
                                .border(2.dp, SaffronPrimary, CircleShape),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = "RB",
                                fontWeight = FontWeight.ExtraBold,
                                fontSize = 20.sp,
                                color = Color.White
                            )
                        }

                        Spacer(modifier = Modifier.width(14.dp))

                        Column {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Text(
                                    text = userName,
                                    style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold)
                                )
                                Spacer(modifier = Modifier.width(6.dp))
                                Icon(Icons.Default.CheckCircle, contentDescription = "KYC Verified", tint = IndianGreen, modifier = Modifier.size(16.dp))
                            }
                            Text(
                                text = userPhone,
                                fontSize = 12.sp,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                            Surface(
                                color = IndianGreen.copy(alpha = 0.15f),
                                shape = RoundedCornerShape(6.dp),
                                modifier = Modifier.padding(top = 4.dp)
                            ) {
                                Text(
                                    text = "Aadhaar UID Masked: XXXX-XXXX-4819",
                                    fontSize = 10.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = IndianGreen,
                                    modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                                )
                            }
                        }
                    }
                }
            }

            // Edit Profile Fields
            item {
                Text(
                    text = "Personal Information",
                    style = MaterialTheme.typography.labelLarge.copy(fontWeight = FontWeight.Bold)
                )
            }

            item {
                OutlinedTextField(
                    value = userName,
                    onValueChange = { userName = it },
                    label = { Text("Your Full Name") },
                    leadingIcon = { Icon(Icons.Default.Person, contentDescription = null) },
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth().testTag("profile_name_input"),
                    shape = RoundedCornerShape(12.dp)
                )
            }

            item {
                OutlinedTextField(
                    value = userPhone,
                    onValueChange = { userPhone = it },
                    label = { Text("Primary Phone Number") },
                    leadingIcon = { Icon(Icons.Default.Phone, contentDescription = null) },
                    singleLine = true,
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Phone),
                    modifier = Modifier.fillMaxWidth().testTag("profile_phone_input"),
                    shape = RoundedCornerShape(12.dp)
                )
            }

            // Emergency Contacts
            item {
                Text(
                    text = "Emergency SOS Contacts",
                    style = MaterialTheme.typography.labelLarge.copy(fontWeight = FontWeight.Bold)
                )
            }

            item {
                OutlinedTextField(
                    value = emergencyContact1,
                    onValueChange = { emergencyContact1 = it },
                    label = { Text("Emergency Contact 1 (Group Member)") },
                    leadingIcon = { Icon(Icons.Default.ContactPhone, contentDescription = null, tint = IndianGreen) },
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth().testTag("profile_emergency_1_input"),
                    shape = RoundedCornerShape(12.dp)
                )
            }

            item {
                OutlinedTextField(
                    value = emergencyContact2,
                    onValueChange = { emergencyContact2 = it },
                    label = { Text("Emergency Contact 2 (Helpline)") },
                    leadingIcon = { Icon(Icons.Default.Emergency, contentDescription = null, tint = SaffronPrimary) },
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth().testTag("profile_emergency_2_input"),
                    shape = RoundedCornerShape(12.dp)
                )
            }

            // Saved Locations
            item {
                Text(
                    text = "Saved Favorite Locations",
                    style = MaterialTheme.typography.labelLarge.copy(fontWeight = FontWeight.Bold)
                )
            }

            item {
                OutlinedTextField(
                    value = homeAddress,
                    onValueChange = { homeAddress = it },
                    label = { Text("Home Location") },
                    leadingIcon = { Icon(Icons.Default.Home, contentDescription = null) },
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth().testTag("profile_home_input"),
                    shape = RoundedCornerShape(12.dp)
                )
            }

            item {
                OutlinedTextField(
                    value = workAddress,
                    onValueChange = { workAddress = it },
                    label = { Text("Work / Court Location") },
                    leadingIcon = { Icon(Icons.Default.Work, contentDescription = null) },
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth().testTag("profile_work_input"),
                    shape = RoundedCornerShape(12.dp)
                )
            }

            // Save Profile Button
            item {
                Button(
                    onClick = {
                        Toast.makeText(context, "Profile details updated successfully!", Toast.LENGTH_SHORT).show()
                    },
                    modifier = Modifier.fillMaxWidth().height(48.dp).testTag("save_profile_button"),
                    colors = ButtonDefaults.buttonColors(containerColor = SaffronPrimary),
                    shape = RoundedCornerShape(12.dp)
                ) {
                    Text("SAVE PROFILE CHANGES", fontWeight = FontWeight.Bold)
                }
            }

            item {
                Spacer(modifier = Modifier.height(24.dp))
            }
        }
    }
}
