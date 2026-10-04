package com.example.ui.screens

import android.widget.Toast
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
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
import com.example.model.RentCarItem
import com.example.ui.theme.AshokaBlue
import com.example.ui.theme.IndianGreen
import com.example.ui.theme.SaffronPrimary
import com.example.viewmodel.MainViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RentACarRegistrationScreen(
    viewModel: MainViewModel,
    onBack: () -> Unit
) {
    BackHandler { onBack() }
    val context = LocalContext.current

    var ownerName by remember { mutableStateOf("") }
    var companyName by remember { mutableStateOf("") }
    var phone by remember { mutableStateOf("") }
    var aadhaar by remember { mutableStateOf("") }
    var panNumber by remember { mutableStateOf("") }
    var businessLicence by remember { mutableStateOf("") }
    var address by remember { mutableStateOf("Barasat, North 24 Parganas") }
    var bankDetails by remember { mutableStateOf("") }
    var isPhotoUploaded by remember { mutableStateOf(false) }

    // Multi-car fleet list
    var carList by remember {
        mutableStateOf(
            listOf(
                RentCarItem("car_1", "Toyota Innova Crysta", "WB-02-AK-7719", "", "RC-9912", "National Ins"),
                RentCarItem("car_2", "Maruti Dzire Tour", "WB-25-AB-4921", "", "RC-4412", "Digit Ins")
            )
        )
    }

    var showAddCarDialog by remember { mutableStateOf(false) }
    var newCarModel by remember { mutableStateOf("") }
    var newCarPlate by remember { mutableStateOf("") }
    var newCarRc by remember { mutableStateOf("") }
    var newCarIns by remember { mutableStateOf("") }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Column {
                        Text("Rent A Car Registration", fontWeight = FontWeight.Bold, fontSize = 18.sp)
                        Text("গাড়ি ভাড়া ব্যবসা রেজিস্টার", fontSize = 11.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
                    }
                },
                navigationIcon = {
                    IconButton(onClick = onBack, modifier = Modifier.testTag("rent_reg_back_btn")) {
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
                // Business info card
                Surface(
                    color = AshokaBlue.copy(alpha = 0.1f),
                    shape = RoundedCornerShape(12.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Row(
                        modifier = Modifier.padding(14.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(Icons.Default.VpnKey, contentDescription = null, tint = AshokaBlue, modifier = Modifier.size(32.dp))
                        Spacer(modifier = Modifier.width(12.dp))
                        Column {
                            Text("Rental Agency & Fleet Owner Profile", fontWeight = FontWeight.Bold, fontSize = 15.sp, color = AshokaBlue)
                            Text("List multiple cars for daily / weekly self-drive or with driver", fontSize = 11.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
                        }
                    }
                }
            }

            // Photo upload
            item {
                Text("Agency / Owner Photo", fontWeight = FontWeight.Bold, fontSize = 13.sp)
                Card(
                    modifier = Modifier.fillMaxWidth().clickable { isPhotoUploaded = !isPhotoUploaded }.testTag("rent_owner_photo_card"),
                    shape = RoundedCornerShape(10.dp),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.4f))
                ) {
                    Row(modifier = Modifier.padding(12.dp), verticalAlignment = Alignment.CenterVertically) {
                        Icon(if (isPhotoUploaded) Icons.Default.CheckCircle else Icons.Default.CameraAlt, contentDescription = null, tint = if (isPhotoUploaded) IndianGreen else AshokaBlue)
                        Spacer(modifier = Modifier.width(10.dp))
                        Text(if (isPhotoUploaded) "Owner ID / Office Photo Attached" else "Upload Agency Owner / Office Photo *", fontWeight = FontWeight.SemiBold, fontSize = 12.sp)
                    }
                }
            }

            // Owner Details
            item {
                Text("Owner & Business Identity", fontWeight = FontWeight.Bold, fontSize = 13.sp)
            }

            item {
                OutlinedTextField(
                    value = ownerName,
                    onValueChange = { ownerName = it },
                    label = { Text("Owner Full Name *") },
                    placeholder = { Text("e.g. Bikash Mukherjee") },
                    leadingIcon = { Icon(Icons.Default.Person, contentDescription = null) },
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth().testTag("rent_owner_name_input"),
                    shape = RoundedCornerShape(12.dp)
                )
            }

            item {
                OutlinedTextField(
                    value = companyName,
                    onValueChange = { companyName = it },
                    label = { Text("Company / Agency Name (Optional)") },
                    placeholder = { Text("e.g. Maa Tara Car Travels") },
                    leadingIcon = { Icon(Icons.Default.Business, contentDescription = null) },
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth().testTag("rent_company_name_input"),
                    shape = RoundedCornerShape(12.dp)
                )
            }

            item {
                OutlinedTextField(
                    value = phone,
                    onValueChange = { phone = it },
                    label = { Text("Mobile Phone Number *") },
                    placeholder = { Text("e.g. +91 98310 99421") },
                    leadingIcon = { Icon(Icons.Default.Phone, contentDescription = null) },
                    singleLine = true,
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Phone),
                    modifier = Modifier.fillMaxWidth().testTag("rent_phone_input"),
                    shape = RoundedCornerShape(12.dp)
                )
            }

            item {
                OutlinedTextField(
                    value = aadhaar,
                    onValueChange = { if (it.length <= 12 && it.all { c -> c.isDigit() }) aadhaar = it },
                    label = { Text("Aadhaar Number (UIDAI) *") },
                    leadingIcon = { Icon(Icons.Default.Lock, contentDescription = null, tint = IndianGreen) },
                    singleLine = true,
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                    modifier = Modifier.fillMaxWidth().testTag("rent_aadhaar_input"),
                    shape = RoundedCornerShape(12.dp)
                )
            }

            item {
                OutlinedTextField(
                    value = panNumber,
                    onValueChange = { panNumber = it.uppercase() },
                    label = { Text("PAN Number *") },
                    placeholder = { Text("e.g. ABCDE1234F") },
                    leadingIcon = { Icon(Icons.Default.CreditCard, contentDescription = null) },
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth().testTag("rent_pan_input"),
                    shape = RoundedCornerShape(12.dp)
                )
            }

            item {
                OutlinedTextField(
                    value = businessLicence,
                    onValueChange = { businessLicence = it },
                    label = { Text("Trade / Business Licence Number *") },
                    placeholder = { Text("e.g. TRD/2026/BAR/9012") },
                    leadingIcon = { Icon(Icons.Default.Verified, contentDescription = null) },
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth().testTag("rent_licence_input"),
                    shape = RoundedCornerShape(12.dp)
                )
            }

            item {
                OutlinedTextField(
                    value = address,
                    onValueChange = { address = it },
                    label = { Text("Garage / Office Address *") },
                    leadingIcon = { Icon(Icons.Default.LocationOn, contentDescription = null) },
                    minLines = 2,
                    modifier = Modifier.fillMaxWidth().testTag("rent_address_input"),
                    shape = RoundedCornerShape(12.dp)
                )
            }

            item {
                OutlinedTextField(
                    value = bankDetails,
                    onValueChange = { bankDetails = it },
                    label = { Text("Bank Account Details (Payouts) *") },
                    placeholder = { Text("e.g. SBI • A/C: 38210944120 • IFSC: SBIN0000024") },
                    leadingIcon = { Icon(Icons.Default.AccountBalance, contentDescription = null) },
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth().testTag("rent_bank_input"),
                    shape = RoundedCornerShape(12.dp)
                )
            }

            // Multiple Car Fleet Section
            item {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text("Rental Fleet Cars (${carList.size})", fontWeight = FontWeight.Bold, fontSize = 14.sp)
                    TextButton(onClick = { showAddCarDialog = true }, modifier = Modifier.testTag("add_car_to_fleet_btn")) {
                        Icon(Icons.Default.Add, contentDescription = null, modifier = Modifier.size(16.dp))
                        Spacer(modifier = Modifier.width(4.dp))
                        Text("Add Car to Fleet", fontWeight = FontWeight.Bold, color = SaffronPrimary)
                    }
                }
            }

            itemsIndexed(carList) { index, car ->
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(12.dp),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f))
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth().padding(12.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Column {
                            Text(car.model, fontWeight = FontWeight.Bold, fontSize = 13.sp)
                            Text("Plate: ${car.vehicleNumber} • RC: ${car.rcNumber}", fontSize = 11.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
                            Text("Insurance: ${car.insuranceNumber}", fontSize = 10.sp, color = IndianGreen)
                        }
                        IconButton(onClick = {
                            carList = carList.toMutableList().also { it.removeAt(index) }
                        }) {
                            Icon(Icons.Default.Delete, contentDescription = "Remove", tint = Color.Red, modifier = Modifier.size(18.dp))
                        }
                    }
                }
            }

            // Submit Button
            item {
                Spacer(modifier = Modifier.height(10.dp))
                Button(
                    onClick = {
                        if (ownerName.isNotBlank() && phone.isNotBlank()) {
                            viewModel.registerRentOwner(
                                ownerName = ownerName,
                                companyName = companyName,
                                phone = phone,
                                address = address,
                                pan = panNumber.ifBlank { "ABCDE1234F" },
                                licence = businessLicence.ifBlank { "TRD/2026/BAR/7710" },
                                bank = bankDetails.ifBlank { "SBI • A/C: XXXX-XXXX-5541 • IFSC: SBIN0000024" },
                                cars = carList
                            )
                            onBack()
                        } else {
                            Toast.makeText(context, "Please enter Owner Name and Phone Number", Toast.LENGTH_SHORT).show()
                        }
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(52.dp)
                        .testTag("submit_rent_owner_btn"),
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF1DB954)),
                    shape = RoundedCornerShape(14.dp)
                ) {
                    Icon(Icons.Default.CheckCircle, contentDescription = null)
                    Spacer(modifier = Modifier.width(8.dp))
                    Text("SAVE RENT A CAR BUSINESS PROFILE", fontWeight = FontWeight.Bold)
                }
                Spacer(modifier = Modifier.height(24.dp))
            }
        }
    }

    // Add Car Dialog
    if (showAddCarDialog) {
        AlertDialog(
            onDismissRequest = { showAddCarDialog = false },
            title = { Text("Add Car to Rental Fleet", fontWeight = FontWeight.Bold) },
            text = {
                Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    OutlinedTextField(
                        value = newCarModel,
                        onValueChange = { newCarModel = it },
                        label = { Text("Vehicle Make & Model") },
                        placeholder = { Text("e.g. Mahindra Scorpio-N") },
                        singleLine = true,
                        modifier = Modifier.fillMaxWidth()
                    )
                    OutlinedTextField(
                        value = newCarPlate,
                        onValueChange = { newCarPlate = it },
                        label = { Text("Registration Number") },
                        placeholder = { Text("e.g. WB-25-SC-0811") },
                        singleLine = true,
                        modifier = Modifier.fillMaxWidth()
                    )
                    OutlinedTextField(
                        value = newCarRc,
                        onValueChange = { newCarRc = it },
                        label = { Text("RC Book Number") },
                        placeholder = { Text("e.g. RC-778219") },
                        singleLine = true,
                        modifier = Modifier.fillMaxWidth()
                    )
                    OutlinedTextField(
                        value = newCarIns,
                        onValueChange = { newCarIns = it },
                        label = { Text("Insurance Details") },
                        placeholder = { Text("e.g. Oriental Insurance") },
                        singleLine = true,
                        modifier = Modifier.fillMaxWidth()
                    )
                }
            },
            confirmButton = {
                Button(
                    onClick = {
                        if (newCarModel.isNotBlank() && newCarPlate.isNotBlank()) {
                            carList = carList + RentCarItem(
                                id = "car_${System.currentTimeMillis()}",
                                model = newCarModel,
                                vehicleNumber = newCarPlate,
                                photoUri = "",
                                rcNumber = newCarRc.ifBlank { "RC-${newCarPlate}" },
                                insuranceNumber = newCarIns.ifBlank { "Comprehensive Ins" }
                            )
                            newCarModel = ""
                            newCarPlate = ""
                            newCarRc = ""
                            newCarIns = ""
                            showAddCarDialog = false
                        } else {
                            Toast.makeText(context, "Please enter Model and Plate number", Toast.LENGTH_SHORT).show()
                        }
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF1DB954))
                ) {
                    Text("Add Car")
                }
            },
            dismissButton = {
                TextButton(onClick = { showAddCarDialog = false }) {
                    Text("Cancel")
                }
            }
        )
    }
}
