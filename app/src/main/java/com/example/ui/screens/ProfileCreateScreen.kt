package com.example.ui.screens

import android.widget.Toast
import androidx.activity.compose.BackHandler
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
import com.example.ui.theme.AshokaBlue
import com.example.ui.theme.IndianGreen
import com.example.ui.theme.NavySecondary
import com.example.ui.theme.SaffronPrimary
import com.example.viewmodel.MainViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProfileCreateScreen(
    viewModel: MainViewModel,
    role: String = "RIDER",
    vehicleType: String = "BIKE",
    onBack: () -> Unit
) {
    BackHandler { onBack() }
    val context = LocalContext.current

    var name by remember { mutableStateOf("") }
    var phone by remember { mutableStateOf("") }
    var aadhaar by remember { mutableStateOf("") }
    var drivingLicence by remember { mutableStateOf("") }
    var vehicleNumber by remember { mutableStateOf("") }
    var address by remember { mutableStateOf("Colony More, Barasat") }
    var isPhotoAttached by remember { mutableStateOf(false) }
    var showSuccessDialog by remember { mutableStateOf(false) }

    val isToto = vehicleType.equals("TOTO", ignoreCase = true)

    if (showSuccessDialog) {
        com.example.ui.components.PremiumSuccessDialog(
            title = "Rider Profile Registered!",
            message = "Your $vehicleType profile has been successfully registered on Bharat Mitra. Verified drivers enjoy zero commission on all trips.",
            onDismiss = {
                showSuccessDialog = false
                onBack()
            }
        )
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Column {
                        Text("Create Rider Profile ($vehicleType)", fontWeight = FontWeight.Bold, fontSize = 18.sp)
                        Text(
                            text = if (isToto) "টোটো চালক রেজিস্টার (DL optional)" else "বাইক রাইডার রেজিস্ট্রেশন",
                            fontSize = 11.sp,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                },
                navigationIcon = {
                    IconButton(onClick = onBack, modifier = Modifier.testTag("profile_create_back_btn")) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
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
            item {
                Spacer(modifier = Modifier.height(4.dp))
                // Vehicle Badge Header
                Surface(
                    color = (if (isToto) IndianGreen else SaffronPrimary).copy(alpha = 0.12f),
                    shape = RoundedCornerShape(12.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Row(
                        modifier = Modifier.padding(14.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            imageVector = if (isToto) Icons.Default.ElectricRickshaw else Icons.Default.TwoWheeler,
                            contentDescription = null,
                            tint = if (isToto) IndianGreen else SaffronPrimary,
                            modifier = Modifier.size(32.dp)
                        )
                        Spacer(modifier = Modifier.width(12.dp))
                        Column {
                            Text(
                                text = "Registered Vehicle: $vehicleType",
                                fontWeight = FontWeight.Bold,
                                fontSize = 15.sp,
                                color = if (isToto) IndianGreen else SaffronPrimary
                            )
                            Text(
                                text = "Role: $role • Verified Partner Fleet",
                                fontSize = 11.sp,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }
                    }
                }
            }

            // Photo Upload
            item {
                Text("Profile Photo", fontWeight = FontWeight.Bold, fontSize = 13.sp)
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable { isPhotoAttached = !isPhotoAttached }
                        .testTag("rider_photo_upload_card"),
                    shape = RoundedCornerShape(12.dp),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.4f))
                ) {
                    Row(
                        modifier = Modifier.padding(14.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Box(
                            modifier = Modifier
                                .size(50.dp)
                                .clip(CircleShape)
                                .background(if (isPhotoAttached) IndianGreen else Color.LightGray),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                if (isPhotoAttached) Icons.Default.Check else Icons.Default.CameraAlt,
                                contentDescription = null,
                                tint = Color.White
                            )
                        }
                        Spacer(modifier = Modifier.width(14.dp))
                        Column {
                            Text(
                                text = if (isPhotoAttached) "Photo Attached (rider_selfie.jpg)" else "Attach Rider Profile Photo",
                                fontWeight = FontWeight.SemiBold,
                                fontSize = 13.sp,
                                color = if (isPhotoAttached) IndianGreen else MaterialTheme.colorScheme.onSurface
                            )
                            Text(
                                text = "Clear frontal selfie photo for passenger security",
                                fontSize = 11.sp,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }
                    }
                }
            }

            // Name
            item {
                OutlinedTextField(
                    value = name,
                    onValueChange = { name = it },
                    label = { Text("Full Name *") },
                    placeholder = { Text("e.g. Subir Karmakar") },
                    leadingIcon = { Icon(Icons.Default.Person, contentDescription = null) },
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth().testTag("rider_name_input"),
                    shape = RoundedCornerShape(12.dp)
                )
            }

            // Phone
            item {
                OutlinedTextField(
                    value = phone,
                    onValueChange = { phone = it },
                    label = { Text("Mobile Phone Number *") },
                    placeholder = { Text("e.g. +91 98300 12345") },
                    leadingIcon = { Icon(Icons.Default.Phone, contentDescription = null) },
                    singleLine = true,
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Phone),
                    modifier = Modifier.fillMaxWidth().testTag("rider_phone_input"),
                    shape = RoundedCornerShape(12.dp)
                )
            }

            // Aadhaar Number
            item {
                OutlinedTextField(
                    value = aadhaar,
                    onValueChange = { if (it.length <= 12 && it.all { c -> c.isDigit() }) aadhaar = it },
                    label = { Text("Aadhaar Number (UIDAI) *") },
                    placeholder = { Text("12 digits (XXXX-XXXX-1234)") },
                    leadingIcon = { Icon(Icons.Default.Lock, contentDescription = null, tint = IndianGreen) },
                    singleLine = true,
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                    modifier = Modifier.fillMaxWidth().testTag("rider_aadhaar_input"),
                    shape = RoundedCornerShape(12.dp)
                )
            }

            // Driving Licence (Optional for Toto)
            item {
                OutlinedTextField(
                    value = drivingLicence,
                    onValueChange = { drivingLicence = it },
                    label = { Text(if (isToto) "Driving Licence (Optional for Toto)" else "Driving Licence Number *") },
                    placeholder = { Text(if (isToto) "Not required for low-speed e-toto" else "e.g. WB-25-2021-00123") },
                    leadingIcon = { Icon(Icons.Default.Badge, contentDescription = null) },
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth().testTag("rider_dl_input"),
                    shape = RoundedCornerShape(12.dp)
                )
            }

            // Vehicle Number
            item {
                OutlinedTextField(
                    value = vehicleNumber,
                    onValueChange = { vehicleNumber = it },
                    label = { Text("Vehicle Registration / Muni Number *") },
                    placeholder = { Text(if (isToto) "e.g. WB-26-E-1842 or Barasat Muni #102" else "e.g. WB-25-BK-9180") },
                    leadingIcon = { Icon(Icons.Default.Pin, contentDescription = null) },
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth().testTag("rider_vehicle_number_input"),
                    shape = RoundedCornerShape(12.dp)
                )
            }

            // Address
            item {
                OutlinedTextField(
                    value = address,
                    onValueChange = { address = it },
                    label = { Text("Residential / Base Stand Address *") },
                    leadingIcon = { Icon(Icons.Default.Home, contentDescription = null) },
                    minLines = 2,
                    modifier = Modifier.fillMaxWidth().testTag("rider_address_input"),
                    shape = RoundedCornerShape(12.dp)
                )
            }

            // Submit Button
            item {
                Spacer(modifier = Modifier.height(10.dp))
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(52.dp)
                        .clip(RoundedCornerShape(14.dp))
                        .background(
                            androidx.compose.ui.graphics.Brush.horizontalGradient(
                                listOf(Color(0xFFFA8520), Color(0xFF138808))
                            )
                        )
                        .clickable {
                            if (name.isNotBlank() && phone.isNotBlank() && vehicleNumber.isNotBlank()) {
                                viewModel.registerRider(
                                    name = name,
                                    phone = phone,
                                    vehicleType = vehicleType,
                                    vehicleNumber = vehicleNumber,
                                    address = address,
                                    dl = drivingLicence
                                )
                                showSuccessDialog = true
                            } else {
                                Toast.makeText(context, "Please fill in Name, Phone, and Vehicle Number", Toast.LENGTH_SHORT).show()
                            }
                        }
                        .testTag("submit_rider_profile_btn"),
                    contentAlignment = Alignment.Center
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(Icons.Default.CheckCircle, contentDescription = null, tint = Color.White)
                        Spacer(modifier = Modifier.width(8.dp))
                        Text("SAVE & SUBMIT RIDER PROFILE", fontWeight = FontWeight.Bold, color = Color.White, fontSize = 14.sp)
                    }
                }
                Spacer(modifier = Modifier.height(24.dp))
            }
        }
    }
}
