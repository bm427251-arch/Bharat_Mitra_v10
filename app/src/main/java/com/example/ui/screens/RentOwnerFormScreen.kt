package com.example.ui.screens

import android.widget.Toast
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
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
import com.example.model.VehicleCategory
import com.example.ui.components.PremiumSuccessDialog
import com.example.ui.theme.*
import com.example.viewmodel.MainViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RentOwnerFormScreen(
    viewModel: MainViewModel,
    onBack: () -> Unit,
    onContinueToSubscription: () -> Unit
) {
    BackHandler { onBack() }
    val context = LocalContext.current

    var ownerName by remember { mutableStateOf("Rajib Banerjee") }
    var companyName by remember { mutableStateOf("Maa Tara Fleet & Logistics") }
    var phone by remember { mutableStateOf("9830129841") }
    var isPhoneOtpVerified by remember { mutableStateOf(true) }

    // Special Requirement A: ALL TYPES dropdown: Ambulance, Lorry, Truck, Bus, JCB, Crane, etc.
    val allVehicleTypes = listOf(
        "Ambulance",
        "Lorry",
        "Truck",
        "Pickup Van",
        "Bus",
        "Traveller",
        "Tempo",
        "JCB",
        "Crane",
        "Sedan",
        "SUV",
        "Hatchback",
        "MUV",
        "Luxury Car",
        "Electric Vehicle (EV)",
        "Bike Taxi",
        "Auto Rickshaw",
        "Toto (E-Rickshaw)",
        "Other Commercial Vehicle"
    )

    var expandedDropdown by remember { mutableStateOf(false) }
    var selectedCategory by remember { mutableStateOf(allVehicleTypes[0]) } // Defaults to Ambulance
    var vehicleModelText by remember { mutableStateOf("Bolero Life Support Ambulance") }

    var rcNumber by remember { mutableStateOf("WB-02-AM-8819") }
    var insuranceExpiry by remember { mutableStateOf("2028-12-31") }
    var pucCert by remember { mutableStateOf("PUC-991204") }
    var bankAccountIfsc by remember { mutableStateOf("SBI • A/C: 38810291048 • IFSC: SBIN0000024") }

    // Upload toggles
    var isAadhaarUploaded by remember { mutableStateOf(true) }
    var isPanUploaded by remember { mutableStateOf(true) }
    var isTradeLicenceUploaded by remember { mutableStateOf(true) }
    var isRcPhotoUploaded by remember { mutableStateOf(true) }
    var isFourSidesPhotosUploaded by remember { mutableStateOf(true) }

    var showSuccessDialog by remember { mutableStateOf(false) }

    if (showSuccessDialog) {
        PremiumSuccessDialog(
            title = "Registration Submitted!",
            message = "Your $selectedCategory ($vehicleModelText) registration has been recorded and submitted for fleet review.",
            onDismiss = {
                showSuccessDialog = false
                onContinueToSubscription()
            }
        )
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Column {
                        Text("Become Rent Owner", fontWeight = FontWeight.Bold, fontSize = 18.sp, color = Color.White)
                        Text("Any Vehicle Can Be Added • Fleet Partner", fontSize = 11.sp, color = BharatOrangeLight)
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
            // Notice: ANY vehicle can be added
            item {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(containerColor = BharatDarkBlue.copy(alpha = 0.08f)),
                    border = BorderStroke(1.dp, BharatDarkBlue.copy(alpha = 0.3f))
                ) {
                    Row(
                        modifier = Modifier.padding(14.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(Icons.Default.Verified, contentDescription = null, tint = BharatDarkBlue)
                        Spacer(modifier = Modifier.width(10.dp))
                        Text(
                            text = "Any vehicle can be added: Ambulance, Lorry, Truck, Bus, Car, JCB, Tempo & more. Free text model entry.",
                            fontSize = 12.sp,
                            fontWeight = FontWeight.SemiBold,
                            color = BharatDarkBlue
                        )
                    }
                }
            }

            // Owner & Business Info
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
                        Text("Owner & Business Details", fontWeight = FontWeight.Bold, fontSize = 16.sp, color = BharatDarkBlue)

                        OutlinedTextField(
                            value = ownerName,
                            onValueChange = { ownerName = it },
                            label = { Text("Owner Full Name *") },
                            leadingIcon = { Icon(Icons.Default.Person, contentDescription = null, tint = BharatDarkBlue) },
                            modifier = Modifier.fillMaxWidth().testTag("owner_name_input"),
                            shape = RoundedCornerShape(12.dp)
                        )

                        OutlinedTextField(
                            value = companyName,
                            onValueChange = { companyName = it },
                            label = { Text("Fleet / Company Name") },
                            leadingIcon = { Icon(Icons.Default.Business, contentDescription = null, tint = BharatDarkBlue) },
                            modifier = Modifier.fillMaxWidth().testTag("company_name_input"),
                            shape = RoundedCornerShape(12.dp)
                        )

                        OutlinedTextField(
                            value = phone,
                            onValueChange = { phone = it },
                            label = { Text("Phone Number (OTP Verified) *") },
                            leadingIcon = { Icon(Icons.Default.Phone, contentDescription = null, tint = BharatDarkBlue) },
                            trailingIcon = {
                                Surface(
                                    color = BharatGreen.copy(alpha = 0.15f),
                                    shape = RoundedCornerShape(6.dp)
                                ) {
                                    Text("OTP Verified", color = BharatGreen, fontWeight = FontWeight.Bold, fontSize = 11.sp, modifier = Modifier.padding(horizontal = 6.dp, vertical = 3.dp))
                                }
                            },
                            modifier = Modifier.fillMaxWidth().testTag("owner_phone_input"),
                            shape = RoundedCornerShape(12.dp)
                        )

                        // Bank Account & IFSC
                        OutlinedTextField(
                            value = bankAccountIfsc,
                            onValueChange = { bankAccountIfsc = it },
                            label = { Text("Bank Account & IFSC for Payouts *") },
                            leadingIcon = { Icon(Icons.Default.AccountBalance, contentDescription = null, tint = BharatDarkBlue) },
                            modifier = Modifier.fillMaxWidth().testTag("bank_ifsc_input"),
                            shape = RoundedCornerShape(12.dp)
                        )
                    }
                }
            }

            // Vehicle Category Dropdown (ALL TYPES) & Free Text Model
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
                        Text("Vehicle Classification", fontWeight = FontWeight.Bold, fontSize = 16.sp, color = BharatDarkBlue)

                        // Vehicle Category Dropdown
                        Box(modifier = Modifier.fillMaxWidth()) {
                            OutlinedTextField(
                                value = selectedCategory,
                                onValueChange = {},
                                readOnly = true,
                                label = { Text("Vehicle Category (Select Any Type) *") },
                                trailingIcon = {
                                    IconButton(onClick = { expandedDropdown = !expandedDropdown }) {
                                        Icon(Icons.Default.ArrowDropDown, contentDescription = null)
                                    }
                                },
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .clickable { expandedDropdown = true }
                                    .testTag("vehicle_category_dropdown"),
                                shape = RoundedCornerShape(12.dp)
                            )

                            DropdownMenu(
                                expanded = expandedDropdown,
                                onDismissRequest = { expandedDropdown = false },
                                modifier = Modifier.fillMaxWidth(0.9f)
                            ) {
                                allVehicleTypes.forEach { type ->
                                    DropdownMenuItem(
                                        text = { Text(type, fontWeight = if (type == selectedCategory) FontWeight.Bold else FontWeight.Normal) },
                                        onClick = {
                                            selectedCategory = type
                                            expandedDropdown = false
                                        }
                                    )
                                }
                            }
                        }

                        // Vehicle Model Text Input (Free Text, e.g. Bolero Ambulance, Tata 407 Lorry)
                        OutlinedTextField(
                            value = vehicleModelText,
                            onValueChange = { vehicleModelText = it },
                            label = { Text("Vehicle Make & Model (Free Text) *") },
                            placeholder = { Text("e.g. Bolero Ambulance, Tata 407 Lorry, Ashok Leyland Bus") },
                            leadingIcon = { Icon(Icons.Default.DirectionsCar, contentDescription = null, tint = BharatDarkBlue) },
                            modifier = Modifier.fillMaxWidth().testTag("vehicle_model_freetext_input"),
                            shape = RoundedCornerShape(12.dp)
                        )

                        // RC Number
                        OutlinedTextField(
                            value = rcNumber,
                            onValueChange = { rcNumber = it },
                            label = { Text("RC Registration Plate Number *") },
                            leadingIcon = { Icon(Icons.Default.Pin, contentDescription = null, tint = BharatDarkBlue) },
                            modifier = Modifier.fillMaxWidth().testTag("owner_rc_input"),
                            shape = RoundedCornerShape(12.dp)
                        )

                        // Insurance & PUC
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(10.dp)
                        ) {
                            OutlinedTextField(
                                value = insuranceExpiry,
                                onValueChange = { insuranceExpiry = it },
                                label = { Text("Insurance Expiry") },
                                modifier = Modifier.weight(1f),
                                shape = RoundedCornerShape(12.dp)
                            )
                            OutlinedTextField(
                                value = pucCert,
                                onValueChange = { pucCert = it },
                                label = { Text("PUC Validity") },
                                modifier = Modifier.weight(1f),
                                shape = RoundedCornerShape(12.dp)
                            )
                        }

                        // Upload Documents checklist
                        Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                            DocumentUploadRow("Aadhaar Card (Front & Back)", isAadhaarUploaded) { isAadhaarUploaded = !isAadhaarUploaded }
                            DocumentUploadRow("PAN Card Upload", isPanUploaded) { isPanUploaded = !isPanUploaded }
                            DocumentUploadRow("Trade Licence / GST", isTradeLicenceUploaded) { isTradeLicenceUploaded = !isTradeLicenceUploaded }
                            DocumentUploadRow("RC Book Photo", isRcPhotoUploaded) { isRcPhotoUploaded = !isRcPhotoUploaded }
                            DocumentUploadRow("Vehicle Photos (4 Sides)", isFourSidesPhotosUploaded) { isFourSidesPhotosUploaded = !isFourSidesPhotosUploaded }
                        }
                    }
                }
            }

            // Buttons: Subscribe ₹299/month & Continue to Subscription (NO AUTO APPROVAL TEXT!)
            item {
                Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
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
                                if (ownerName.isNotBlank() && phone.isNotBlank() && rcNumber.isNotBlank()) {
                                    viewModel.submitRentOwner(
                                        ownerName = ownerName,
                                        companyName = companyName,
                                        vehicleCategory = selectedCategory,
                                        vehicleModel = vehicleModelText,
                                        rcNumber = rcNumber
                                    )
                                    showSuccessDialog = true
                                } else {
                                    Toast.makeText(context, "Please enter Owner Name, Phone, and RC Number", Toast.LENGTH_SHORT).show()
                                }
                            }
                            .testTag("subscribe_299_rent_owner_btn"),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = "Subscribe ₹299/month (Fleet Partner)",
                            color = Color.White,
                            fontWeight = FontWeight.Bold,
                            fontSize = 15.sp
                        )
                    }

                    OutlinedButton(
                        onClick = onContinueToSubscription,
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(48.dp)
                            .testTag("continue_to_rent_sub_btn"),
                        shape = RoundedCornerShape(12.dp),
                        border = BorderStroke(1.5.dp, BharatDarkBlue)
                    ) {
                        Text("Continue to Subscription Plans", fontWeight = FontWeight.Bold, color = BharatDarkBlue)
                    }
                }
            }

            item {
                Spacer(modifier = Modifier.height(20.dp))
            }
        }
    }
}

@Composable
private fun DocumentUploadRow(
    title: String,
    isUploaded: Boolean,
    onToggle: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(10.dp))
            .background(LightSurfaceVariant)
            .clickable { onToggle() }
            .padding(horizontal = 12.dp, vertical = 10.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Icon(Icons.Default.UploadFile, contentDescription = null, tint = BharatDarkBlue, modifier = Modifier.size(18.dp))
            Spacer(modifier = Modifier.width(8.dp))
            Text(title, fontSize = 12.sp, fontWeight = FontWeight.Medium, color = BharatDarkBlue)
        }
        Icon(
            imageVector = if (isUploaded) Icons.Default.CheckCircle else Icons.Default.RadioButtonUnchecked,
            contentDescription = null,
            tint = if (isUploaded) BharatGreen else Color.Gray,
            modifier = Modifier.size(18.dp)
        )
    }
}
