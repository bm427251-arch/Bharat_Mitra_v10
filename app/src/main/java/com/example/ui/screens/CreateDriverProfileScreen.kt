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
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.components.PremiumSuccessDialog
import com.example.ui.theme.*
import com.example.viewmodel.MainViewModel

data class DriverVehicleType(
    val name: String,
    val icon: ImageVector,
    val id: String
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CreateDriverProfileScreen(
    viewModel: MainViewModel,
    onBack: () -> Unit
) {
    BackHandler { onBack() }
    val context = LocalContext.current

    val vehicleTypes = listOf(
        DriverVehicleType("Toto E-Rickshaw", Icons.Default.ElectricRickshaw, "toto"),
        DriverVehicleType("Auto Rickshaw", Icons.Default.DirectionsTransit, "auto"),
        DriverVehicleType("Bike Taxi", Icons.Default.TwoWheeler, "bike"),
        DriverVehicleType("Mini Cab", Icons.Default.LocalTaxi, "minicab"),
        DriverVehicleType("Sedan", Icons.Default.DirectionsCar, "sedan"),
        DriverVehicleType("SUV", Icons.Default.AirportShuttle, "suv")
    )

    var selectedVehicleType by remember { mutableStateOf("Sedan") }
    var fullName by remember { mutableStateOf("Suresh Mondal") }
    var phoneNumber by remember { mutableStateOf("9876543210") }
    var isPhoneOtpVerified by remember { mutableStateOf(true) }
    var address by remember { mutableStateOf("Barasat Main Road, North 24 Parganas") }
    var dlNumber by remember { mutableStateOf("DL-WB-2021-99214") }
    var dlExpiry by remember { mutableStateOf("2031-10-14") }
    var rcNumber by remember { mutableStateOf("WB-25-AB-1290") }
    var insuranceNumber by remember { mutableStateOf("INS-99214081") }
    var pucNumber by remember { mutableStateOf("PUC-771249") }

    // Upload toggles
    var isProfilePhotoUploaded by remember { mutableStateOf(true) }
    var isDlPhotoUploaded by remember { mutableStateOf(true) }
    var isRcPhotoUploaded by remember { mutableStateOf(true) }
    var isVehicleFrontUploaded by remember { mutableStateOf(true) }
    var isVehicleBackUploaded by remember { mutableStateOf(true) }

    var showSuccessDialog by remember { mutableStateOf(false) }

    if (showSuccessDialog) {
        PremiumSuccessDialog(
            title = "Driver Profile Submitted!",
            message = "Your commercial driver profile for $selectedVehicleType has been recorded and submitted for verification.",
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
                        Text("Create Driver Profile", fontWeight = FontWeight.Bold, fontSize = 18.sp, color = Color.White)
                        Text("Commercial Partner Onboarding", fontSize = 11.sp, color = BharatOrangeLight)
                    }
                },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back", tint = Color.White)
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = BharatDarkBlue)
            )
        }
    ) { innerPadding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .background(LightBackground)
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // Vehicle Type Selection Header
            item {
                Text(
                    text = "Select Vehicle Type *",
                    style = MaterialTheme.typography.titleMedium.copy(
                        fontWeight = FontWeight.Bold,
                        color = BharatDarkBlue,
                        fontSize = 16.sp
                    )
                )
            }

            // Grid of 6 Vehicle Types: Toto E-Rickshaw, Auto Rickshaw, Bike Taxi, Mini Cab, Sedan, SUV
            item {
                Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                    vehicleTypes.chunked(2).forEach { rowItems ->
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(10.dp)
                        ) {
                            rowItems.forEach { vType ->
                                val isSelected = selectedVehicleType == vType.name
                                Card(
                                    modifier = Modifier
                                        .weight(1f)
                                        .clickable { selectedVehicleType = vType.name }
                                        .testTag("driver_vehicle_${vType.id}"),
                                    shape = RoundedCornerShape(16.dp),
                                    colors = CardDefaults.cardColors(
                                        containerColor = if (isSelected) BharatGreen.copy(alpha = 0.12f) else Color.White
                                    ),
                                    elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
                                    border = BorderStroke(
                                        if (isSelected) 2.dp else 0.5.dp,
                                        if (isSelected) BharatGreen else CardBorderColor
                                    )
                                ) {
                                    Row(
                                        modifier = Modifier.padding(12.dp),
                                        verticalAlignment = Alignment.CenterVertically
                                    ) {
                                        Box(
                                            modifier = Modifier
                                                .size(38.dp)
                                                .clip(CircleShape)
                                                .background(if (isSelected) BharatGreen else LightSurfaceVariant),
                                            contentAlignment = Alignment.Center
                                        ) {
                                            Icon(
                                                imageVector = vType.icon,
                                                contentDescription = vType.name,
                                                tint = if (isSelected) Color.White else BharatDarkBlue,
                                                modifier = Modifier.size(20.dp)
                                            )
                                        }
                                        Spacer(modifier = Modifier.width(8.dp))
                                        Column(modifier = Modifier.weight(1f)) {
                                            Text(
                                                text = vType.name,
                                                fontSize = 12.sp,
                                                fontWeight = FontWeight.Bold,
                                                color = BharatDarkBlue,
                                                maxLines = 1
                                            )
                                        }
                                        if (isSelected) {
                                            Icon(
                                                imageVector = Icons.Default.CheckCircle,
                                                contentDescription = null,
                                                tint = BharatGreen,
                                                modifier = Modifier.size(18.dp)
                                            )
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }

            // Driver Form Fields
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
                        Text(
                            text = "Driver & Vehicle Documents",
                            fontWeight = FontWeight.Bold,
                            color = BharatDarkBlue,
                            fontSize = 16.sp
                        )

                        // Full Name
                        OutlinedTextField(
                            value = fullName,
                            onValueChange = { fullName = it },
                            label = { Text("Full Name *") },
                            leadingIcon = { Icon(Icons.Default.Person, contentDescription = null, tint = BharatDarkBlue) },
                            singleLine = true,
                            modifier = Modifier.fillMaxWidth().testTag("driver_name_input"),
                            shape = RoundedCornerShape(12.dp)
                        )

                        // Phone with OTP Verified
                        OutlinedTextField(
                            value = phoneNumber,
                            onValueChange = { phoneNumber = it },
                            label = { Text("Mobile Number (OTP Verified) *") },
                            leadingIcon = { Icon(Icons.Default.Phone, contentDescription = null, tint = BharatDarkBlue) },
                            trailingIcon = {
                                Surface(
                                    color = BharatGreen.copy(alpha = 0.15f),
                                    shape = RoundedCornerShape(6.dp)
                                ) {
                                    Row(
                                        modifier = Modifier.padding(horizontal = 6.dp, vertical = 4.dp),
                                        verticalAlignment = Alignment.CenterVertically
                                    ) {
                                        Icon(Icons.Default.Verified, contentDescription = null, tint = BharatGreen, modifier = Modifier.size(14.dp))
                                        Spacer(modifier = Modifier.width(4.dp))
                                        Text("OTP Verified", color = BharatGreen, fontSize = 11.sp, fontWeight = FontWeight.Bold)
                                    }
                                }
                            },
                            singleLine = true,
                            modifier = Modifier.fillMaxWidth().testTag("driver_phone_input"),
                            shape = RoundedCornerShape(12.dp)
                        )

                        // Address
                        OutlinedTextField(
                            value = address,
                            onValueChange = { address = it },
                            label = { Text("Base Residential Address *") },
                            leadingIcon = { Icon(Icons.Default.Home, contentDescription = null, tint = BharatDarkBlue) },
                            modifier = Modifier.fillMaxWidth().testTag("driver_address_input"),
                            shape = RoundedCornerShape(12.dp)
                        )

                        // DL Number Photo Expiry Mandatory * (Red Star)
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Text("Driving Licence Number & Expiry ", fontWeight = FontWeight.Bold, fontSize = 13.sp)
                            Text("*", color = Color.Red, fontWeight = FontWeight.Bold)
                        }
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(10.dp)
                        ) {
                            OutlinedTextField(
                                value = dlNumber,
                                onValueChange = { dlNumber = it },
                                label = { Text("DL Number") },
                                modifier = Modifier.weight(1.3f).testTag("driver_dl_input"),
                                shape = RoundedCornerShape(12.dp)
                            )
                            OutlinedTextField(
                                value = dlExpiry,
                                onValueChange = { dlExpiry = it },
                                label = { Text("Expiry Date") },
                                modifier = Modifier.weight(1f).testTag("driver_dl_expiry_input"),
                                shape = RoundedCornerShape(12.dp)
                            )
                        }

                        // DL Photo Upload Badge
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clip(RoundedCornerShape(10.dp))
                                .background(LightSurfaceVariant)
                                .clickable { isDlPhotoUploaded = !isDlPhotoUploaded }
                                .padding(12.dp),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Icon(Icons.Default.UploadFile, contentDescription = null, tint = BharatDarkBlue)
                                Spacer(modifier = Modifier.width(8.dp))
                                Text("DL Photo Document Uploaded", fontSize = 12.sp, fontWeight = FontWeight.Medium)
                            }
                            Icon(
                                imageVector = if (isDlPhotoUploaded) Icons.Default.CheckCircle else Icons.Default.RadioButtonUnchecked,
                                contentDescription = null,
                                tint = if (isDlPhotoUploaded) BharatGreen else Color.Gray
                            )
                        }

                        // RC Book Number Photo Mandatory * (Red Star)
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Text("RC Book Registration Number ", fontWeight = FontWeight.Bold, fontSize = 13.sp)
                            Text("*", color = Color.Red, fontWeight = FontWeight.Bold)
                        }
                        OutlinedTextField(
                            value = rcNumber,
                            onValueChange = { rcNumber = it },
                            label = { Text("Vehicle Plate / RC Number *") },
                            leadingIcon = { Icon(Icons.Default.Pin, contentDescription = null, tint = BharatDarkBlue) },
                            modifier = Modifier.fillMaxWidth().testTag("driver_rc_input"),
                            shape = RoundedCornerShape(12.dp)
                        )

                        // Insurance & PUC
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(10.dp)
                        ) {
                            OutlinedTextField(
                                value = insuranceNumber,
                                onValueChange = { insuranceNumber = it },
                                label = { Text("Insurance Policy") },
                                modifier = Modifier.weight(1f),
                                shape = RoundedCornerShape(12.dp)
                            )
                            OutlinedTextField(
                                value = pucNumber,
                                onValueChange = { pucNumber = it },
                                label = { Text("PUC Cert") },
                                modifier = Modifier.weight(1f),
                                shape = RoundedCornerShape(12.dp)
                            )
                        }

                        // Vehicle Photos Front & Back Upload toggles
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(10.dp)
                        ) {
                            Surface(
                                modifier = Modifier
                                    .weight(1f)
                                    .clip(RoundedCornerShape(10.dp))
                                    .background(LightSurfaceVariant)
                                    .clickable { isVehicleFrontUploaded = !isVehicleFrontUploaded }
                                    .padding(10.dp),
                                color = Color.Transparent
                            ) {
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Icon(Icons.Default.CameraAlt, contentDescription = null, tint = BharatDarkBlue, modifier = Modifier.size(16.dp))
                                    Spacer(modifier = Modifier.width(6.dp))
                                    Text("Front Photo ✓", fontSize = 11.sp, fontWeight = FontWeight.Bold, color = BharatGreen)
                                }
                            }
                            Surface(
                                modifier = Modifier
                                    .weight(1f)
                                    .clip(RoundedCornerShape(10.dp))
                                    .background(LightSurfaceVariant)
                                    .clickable { isVehicleBackUploaded = !isVehicleBackUploaded }
                                    .padding(10.dp),
                                color = Color.Transparent
                            ) {
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Icon(Icons.Default.CameraAlt, contentDescription = null, tint = BharatDarkBlue, modifier = Modifier.size(16.dp))
                                    Spacer(modifier = Modifier.width(6.dp))
                                    Text("Back Photo ✓", fontSize = 11.sp, fontWeight = FontWeight.Bold, color = BharatGreen)
                                }
                            }
                        }
                    }
                }
            }

            // Submit Button (NO AUTO APPROVAL TEXT!)
            item {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(52.dp)
                        .clip(RoundedCornerShape(14.dp))
                        .background(
                            brush = Brush.horizontalGradient(
                                listOf(BharatOrange, BharatGreen)
                            )
                        )
                        .clickable {
                            if (fullName.isNotBlank() && phoneNumber.isNotBlank() && dlNumber.isNotBlank() && rcNumber.isNotBlank()) {
                                viewModel.submitDriverProfile(
                                    name = fullName,
                                    phone = phoneNumber,
                                    vehicleType = selectedVehicleType,
                                    dlNumber = dlNumber,
                                    rcNumber = rcNumber
                                )
                                showSuccessDialog = true
                            } else {
                                Toast.makeText(context, "Please complete all mandatory fields with red *", Toast.LENGTH_SHORT).show()
                            }
                        }
                        .testTag("submit_driver_profile_btn"),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "SUBMIT DRIVER PROFILE",
                        color = Color.White,
                        fontWeight = FontWeight.Bold,
                        fontSize = 15.sp
                    )
                }
            }

            item {
                Spacer(modifier = Modifier.height(20.dp))
            }
        }
    }
}
