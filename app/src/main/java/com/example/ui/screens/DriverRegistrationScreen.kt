package com.example.ui.screens

import android.widget.Toast
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.background
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
import com.example.model.VehicleCategory
import com.example.ui.theme.IndianGreen
import com.example.ui.theme.SaffronPrimary
import com.example.viewmodel.MainViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DriverRegistrationScreen(
    viewModel: MainViewModel,
    vehicleType: String = "Sedan",
    onBack: () -> Unit
) {
    BackHandler { onBack() }
    val context = LocalContext.current

    var name by remember { mutableStateOf("") }
    var phone by remember { mutableStateOf("") }
    var aadhaar by remember { mutableStateOf("") }
    var drivingLicence by remember { mutableStateOf("") }
    var vehicleNumber by remember { mutableStateOf("") }
    var vehicleModel by remember { mutableStateOf(if (vehicleType == "Innova") "Toyota Innova Crysta" else "Maruti Dzire VXi") }
    var rcBookNumber by remember { mutableStateOf("") }
    var insuranceDetails by remember { mutableStateOf("Valid till 2027") }
    var pucNumber by remember { mutableStateOf("WB-PUC-90124") }

    // Upload attachment toggles
    var isDriverPhotoUploaded by remember { mutableStateOf(false) }
    var isVehiclePhotoUploaded by remember { mutableStateOf(false) }
    var isRcBookUploaded by remember { mutableStateOf(false) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Column {
                        Text("Driver Registration ($vehicleType)", fontWeight = FontWeight.Bold, fontSize = 18.sp)
                        Text("বাণিজ্যিক গাড়ি চালক রেজিস্ট্রেশন", fontSize = 11.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
                    }
                },
                navigationIcon = {
                    IconButton(onClick = onBack, modifier = Modifier.testTag("driver_reg_back_btn")) {
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
                // Vehicle category banner
                Surface(
                    color = SaffronPrimary.copy(alpha = 0.12f),
                    shape = RoundedCornerShape(12.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Row(
                        modifier = Modifier.padding(14.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(Icons.Default.DirectionsCar, contentDescription = null, tint = SaffronPrimary, modifier = Modifier.size(32.dp))
                        Spacer(modifier = Modifier.width(12.dp))
                        Column {
                            Text("Category: $vehicleType", fontWeight = FontWeight.Bold, fontSize = 15.sp, color = SaffronPrimary)
                            Text("Commercial Driver Fleet • 8-Hour / Daily Rates", fontSize = 11.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
                        }
                    }
                }
            }

            // Photos & Document Upload Grid
            item {
                Text("Photos & Document Attachments", fontWeight = FontWeight.Bold, fontSize = 13.sp)
                Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    // Driver Photo
                    Card(
                        modifier = Modifier.fillMaxWidth().clickable { isDriverPhotoUploaded = !isDriverPhotoUploaded }.testTag("driver_photo_card"),
                        shape = RoundedCornerShape(10.dp),
                        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.4f))
                    ) {
                        Row(modifier = Modifier.padding(12.dp), verticalAlignment = Alignment.CenterVertically) {
                            Icon(if (isDriverPhotoUploaded) Icons.Default.CheckCircle else Icons.Default.AccountBox, contentDescription = null, tint = if (isDriverPhotoUploaded) IndianGreen else SaffronPrimary)
                            Spacer(modifier = Modifier.width(10.dp))
                            Text(if (isDriverPhotoUploaded) "Driver Selfie Photo Attached" else "Upload Driver Photo *", fontWeight = FontWeight.SemiBold, fontSize = 12.sp)
                        }
                    }

                    // Vehicle Photo
                    Card(
                        modifier = Modifier.fillMaxWidth().clickable { isVehiclePhotoUploaded = !isVehiclePhotoUploaded }.testTag("vehicle_photo_card"),
                        shape = RoundedCornerShape(10.dp),
                        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.4f))
                    ) {
                        Row(modifier = Modifier.padding(12.dp), verticalAlignment = Alignment.CenterVertically) {
                            Icon(if (isVehiclePhotoUploaded) Icons.Default.CheckCircle else Icons.Default.CameraAlt, contentDescription = null, tint = if (isVehiclePhotoUploaded) IndianGreen else SaffronPrimary)
                            Spacer(modifier = Modifier.width(10.dp))
                            Text(if (isVehiclePhotoUploaded) "Vehicle Exterior Photo Attached" else "Upload Vehicle Photo (Front & Side) *", fontWeight = FontWeight.SemiBold, fontSize = 12.sp)
                        }
                    }

                    // RC Book
                    Card(
                        modifier = Modifier.fillMaxWidth().clickable { isRcBookUploaded = !isRcBookUploaded }.testTag("rc_book_card"),
                        shape = RoundedCornerShape(10.dp),
                        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.4f))
                    ) {
                        Row(modifier = Modifier.padding(12.dp), verticalAlignment = Alignment.CenterVertically) {
                            Icon(if (isRcBookUploaded) Icons.Default.CheckCircle else Icons.Default.UploadFile, contentDescription = null, tint = if (isRcBookUploaded) IndianGreen else SaffronPrimary)
                            Spacer(modifier = Modifier.width(10.dp))
                            Text(if (isRcBookUploaded) "RC Book PDF / Photo Attached" else "Upload RC Book Scan / Photo *", fontWeight = FontWeight.SemiBold, fontSize = 12.sp)
                        }
                    }
                }
            }

            // Driver Personal Fields
            item {
                Text("Driver Identity Details", fontWeight = FontWeight.Bold, fontSize = 13.sp)
            }

            item {
                OutlinedTextField(
                    value = name,
                    onValueChange = { name = it },
                    label = { Text("Driver Full Name *") },
                    placeholder = { Text("e.g. Subhashish Banerjee") },
                    leadingIcon = { Icon(Icons.Default.Person, contentDescription = null) },
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth().testTag("driver_name_input"),
                    shape = RoundedCornerShape(12.dp)
                )
            }

            item {
                OutlinedTextField(
                    value = phone,
                    onValueChange = { phone = it },
                    label = { Text("Mobile Phone Number *") },
                    placeholder = { Text("e.g. +91 98301 24510") },
                    leadingIcon = { Icon(Icons.Default.Phone, contentDescription = null) },
                    singleLine = true,
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Phone),
                    modifier = Modifier.fillMaxWidth().testTag("driver_phone_input"),
                    shape = RoundedCornerShape(12.dp)
                )
            }

            item {
                OutlinedTextField(
                    value = aadhaar,
                    onValueChange = { if (it.length <= 12 && it.all { c -> c.isDigit() }) aadhaar = it },
                    label = { Text("Aadhaar UID (12 Digits) *") },
                    leadingIcon = { Icon(Icons.Default.Lock, contentDescription = null, tint = IndianGreen) },
                    singleLine = true,
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                    modifier = Modifier.fillMaxWidth().testTag("driver_aadhaar_input"),
                    shape = RoundedCornerShape(12.dp)
                )
            }

            item {
                OutlinedTextField(
                    value = drivingLicence,
                    onValueChange = { drivingLicence = it },
                    label = { Text("Commercial Driving Licence Number *") },
                    placeholder = { Text("e.g. WB-25-2018-008129") },
                    leadingIcon = { Icon(Icons.Default.Badge, contentDescription = null) },
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth().testTag("driver_dl_input"),
                    shape = RoundedCornerShape(12.dp)
                )
            }

            // Vehicle Specification Fields
            item {
                Text("Vehicle Specifications", fontWeight = FontWeight.Bold, fontSize = 13.sp)
            }

            item {
                OutlinedTextField(
                    value = vehicleModel,
                    onValueChange = { vehicleModel = it },
                    label = { Text("Vehicle Make & Model *") },
                    placeholder = { Text("e.g. Maruti Dzire VXi / Toyota Innova") },
                    leadingIcon = { Icon(Icons.Default.DirectionsCar, contentDescription = null) },
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth().testTag("driver_model_input"),
                    shape = RoundedCornerShape(12.dp)
                )
            }

            item {
                OutlinedTextField(
                    value = vehicleNumber,
                    onValueChange = { vehicleNumber = it },
                    label = { Text("Vehicle Registration Number *") },
                    placeholder = { Text("e.g. WB-25-AB-4921") },
                    leadingIcon = { Icon(Icons.Default.Pin, contentDescription = null) },
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth().testTag("driver_plate_input"),
                    shape = RoundedCornerShape(12.dp)
                )
            }

            item {
                OutlinedTextField(
                    value = rcBookNumber,
                    onValueChange = { rcBookNumber = it },
                    label = { Text("RC Book Number *") },
                    placeholder = { Text("e.g. RC-WB-2024-9912") },
                    leadingIcon = { Icon(Icons.Default.Description, contentDescription = null) },
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth().testTag("driver_rc_input"),
                    shape = RoundedCornerShape(12.dp)
                )
            }

            item {
                OutlinedTextField(
                    value = insuranceDetails,
                    onValueChange = { insuranceDetails = it },
                    label = { Text("Insurance Policy Details *") },
                    placeholder = { Text("e.g. Digit Comprehensive till 2027") },
                    leadingIcon = { Icon(Icons.Default.VerifiedUser, contentDescription = null) },
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth().testTag("driver_insurance_input"),
                    shape = RoundedCornerShape(12.dp)
                )
            }

            item {
                OutlinedTextField(
                    value = pucNumber,
                    onValueChange = { pucNumber = it },
                    label = { Text("PUC Certificate Number *") },
                    placeholder = { Text("e.g. WB-PUC-90124") },
                    leadingIcon = { Icon(Icons.Default.Eco, contentDescription = null, tint = IndianGreen) },
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth().testTag("driver_puc_input"),
                    shape = RoundedCornerShape(12.dp)
                )
            }

            // Submit Button
            item {
                Spacer(modifier = Modifier.height(10.dp))
                Button(
                    onClick = {
                        if (name.isNotBlank() && phone.isNotBlank() && vehicleNumber.isNotBlank()) {
                            val cat = when (vehicleType.lowercase()) {
                                "suv", "innova" -> VehicleCategory.SUV
                                "hatchback" -> VehicleCategory.HATCHBACK
                                else -> VehicleCategory.SEDAN
                            }
                            viewModel.registerDriver(
                                name = name,
                                phone = phone,
                                vehicleModel = vehicleModel,
                                vehicleNumber = vehicleNumber,
                                vehicleCategory = cat,
                                dl = drivingLicence,
                                rc = rcBookNumber.ifBlank { "RC-${vehicleNumber}" },
                                insurance = insuranceDetails
                            )
                            onBack()
                        } else {
                            Toast.makeText(context, "Please enter Driver Name, Phone, and Vehicle Number", Toast.LENGTH_SHORT).show()
                        }
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(52.dp)
                        .testTag("submit_driver_reg_btn"),
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF1DB954)),
                    shape = RoundedCornerShape(14.dp)
                ) {
                    Icon(Icons.Default.CheckCircle, contentDescription = null)
                    Spacer(modifier = Modifier.width(8.dp))
                    Text("SUBMIT DRIVER PROFILE", fontWeight = FontWeight.Bold)
                }
                Spacer(modifier = Modifier.height(24.dp))
            }
        }
    }
}
