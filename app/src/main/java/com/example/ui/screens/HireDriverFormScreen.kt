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
import com.example.ui.components.PremiumSuccessDialog
import com.example.ui.theme.*
import com.example.viewmodel.MainViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HireDriverFormScreen(
    viewModel: MainViewModel,
    onBack: () -> Unit
) {
    BackHandler { onBack() }
    val context = LocalContext.current

    var name by remember { mutableStateOf("Subhashish Mondal") }
    var phone by remember { mutableStateOf("9831092812") }
    var address by remember { mutableStateOf("Salt Lake Sector 5, Kolkata") }
    var dlNumber by remember { mutableStateOf("DL-04-2018-009124") }
    var dlExpiry by remember { mutableStateOf("2033-05-18") }

    val experienceOptions = listOf("1-2 Years", "3-5 Years", "6-8 Years", "8-10 Years", "10+ Years")
    var experienceDropdownExpanded by remember { mutableStateOf(false) }
    var selectedExperience by remember { mutableStateOf(experienceOptions[1]) }

    val skillOptions = listOf("Manual Only", "Automatic Only", "Both (Manual & Automatic)")
    var selectedSkill by remember { mutableStateOf(skillOptions[2]) }

    var refName by remember { mutableStateOf("Gourab Roy (Fleet Incharge)") }
    var refPhone by remember { mutableStateOf("9830012984") }

    // Upload toggles (NO RC FIELD!)
    var isAadhaarUploaded by remember { mutableStateOf(true) }
    var isDlPhotoUploaded by remember { mutableStateOf(true) }
    var isSelfieUploaded by remember { mutableStateOf(true) }

    var showSuccessDialog by remember { mutableStateOf(false) }

    if (showSuccessDialog) {
        PremiumSuccessDialog(
            title = "Driver Application Submitted!",
            message = "Your DL Only Hire Driver application has been submitted and sent for background verification.",
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
                        Text("Become Hire Driver", fontWeight = FontWeight.Bold, fontSize = 18.sp, color = Color.White)
                        Text("DL Only • No Vehicle Needed", fontSize = 11.sp, color = BharatOrangeLight)
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
            // Notice: DL Only, No RC Needed
            item {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(containerColor = BharatGreen.copy(alpha = 0.08f)),
                    border = BorderStroke(1.dp, BharatGreen.copy(alpha = 0.4f))
                ) {
                    Row(
                        modifier = Modifier.padding(14.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(Icons.Default.Badge, contentDescription = null, tint = BharatGreen)
                        Spacer(modifier = Modifier.width(10.dp))
                        Text(
                            text = "Drive customers' cars or commercial fleets. No RC or car required. Only valid Commercial Driving Licence required.",
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Medium,
                            color = BharatDarkBlue
                        )
                    }
                }
            }

            // Rate Chart Card (1hr ₹120, 4hr ₹400, 8hr ₹750)
            item {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(containerColor = Color.White),
                    elevation = CardDefaults.cardElevation(defaultElevation = 6.dp),
                    border = BorderStroke(0.5.dp, CardBorderColor)
                ) {
                    Column(
                        modifier = Modifier.padding(16.dp),
                        verticalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Text("Guaranteed Driver Payout Rate Chart", fontWeight = FontWeight.Bold, fontSize = 14.sp, color = BharatDarkBlue)
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            RateChartBadge("1 Hour", "₹120")
                            RateChartBadge("4 Hours", "₹400")
                            RateChartBadge("8 Hours (Full Day)", "₹750")
                        }
                    }
                }
            }

            // Form Fields
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
                        Text("Personal & Licence Details", fontWeight = FontWeight.Bold, fontSize = 16.sp, color = BharatDarkBlue)

                        // Full Name
                        OutlinedTextField(
                            value = name,
                            onValueChange = { name = it },
                            label = { Text("Full Legal Name *") },
                            leadingIcon = { Icon(Icons.Default.Person, contentDescription = null, tint = BharatDarkBlue) },
                            modifier = Modifier.fillMaxWidth().testTag("hire_driver_name_input"),
                            shape = RoundedCornerShape(12.dp)
                        )

                        // Phone with OTP Verified
                        OutlinedTextField(
                            value = phone,
                            onValueChange = { phone = it },
                            label = { Text("Mobile Number (OTP Verified) *") },
                            leadingIcon = { Icon(Icons.Default.Phone, contentDescription = null, tint = BharatDarkBlue) },
                            trailingIcon = {
                                Surface(
                                    color = BharatGreen.copy(alpha = 0.15f),
                                    shape = RoundedCornerShape(6.dp)
                                ) {
                                    Text("OTP Verified", color = BharatGreen, fontWeight = FontWeight.Bold, fontSize = 11.sp, modifier = Modifier.padding(horizontal = 6.dp, vertical = 3.dp))
                                }
                            },
                            modifier = Modifier.fillMaxWidth().testTag("hire_driver_phone_input"),
                            shape = RoundedCornerShape(12.dp)
                        )

                        // Address
                        OutlinedTextField(
                            value = address,
                            onValueChange = { address = it },
                            label = { Text("Residential Address *") },
                            leadingIcon = { Icon(Icons.Default.Home, contentDescription = null, tint = BharatDarkBlue) },
                            modifier = Modifier.fillMaxWidth().testTag("hire_driver_address_input"),
                            shape = RoundedCornerShape(12.dp)
                        )

                        // Driving Licence Number Photo Expiry Mandatory * (Red Star)
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
                                modifier = Modifier.weight(1.3f).testTag("hire_driver_dl_input"),
                                shape = RoundedCornerShape(12.dp)
                            )
                            OutlinedTextField(
                                value = dlExpiry,
                                onValueChange = { dlExpiry = it },
                                label = { Text("Expiry Date") },
                                modifier = Modifier.weight(1f).testTag("hire_driver_dl_expiry_input"),
                                shape = RoundedCornerShape(12.dp)
                            )
                        }

                        // Experience Years Dropdown
                        Box(modifier = Modifier.fillMaxWidth()) {
                            OutlinedTextField(
                                value = selectedExperience,
                                onValueChange = {},
                                readOnly = true,
                                label = { Text("Experience Years (1-10+ years) *") },
                                trailingIcon = {
                                    IconButton(onClick = { experienceDropdownExpanded = !experienceDropdownExpanded }) {
                                        Icon(Icons.Default.ArrowDropDown, contentDescription = null)
                                    }
                                },
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .clickable { experienceDropdownExpanded = true }
                                    .testTag("hire_driver_exp_dropdown"),
                                shape = RoundedCornerShape(12.dp)
                            )

                            DropdownMenu(
                                expanded = experienceDropdownExpanded,
                                onDismissRequest = { experienceDropdownExpanded = false }
                            ) {
                                experienceOptions.forEach { exp ->
                                    DropdownMenuItem(
                                        text = { Text(exp) },
                                        onClick = {
                                            selectedExperience = exp
                                            experienceDropdownExpanded = false
                                        }
                                    )
                                }
                            }
                        }

                        // Driving Skill (Manual, Automatic, Both)
                        Text("Driving Skill / Gearbox Capability *", fontSize = 13.sp, fontWeight = FontWeight.Bold, color = BharatDarkBlue)
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(6.dp)
                        ) {
                            skillOptions.forEach { skill ->
                                val isSelected = selectedSkill == skill
                                FilterChip(
                                    selected = isSelected,
                                    onClick = { selectedSkill = skill },
                                    label = { Text(skill.split(" ").first(), fontSize = 11.sp, fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal) },
                                    colors = FilterChipDefaults.filterChipColors(
                                        selectedContainerColor = BharatDarkBlue,
                                        selectedLabelColor = Color.White
                                    )
                                )
                            }
                        }

                        // Reference Person Name & Phone
                        Text("Reference Person Details", fontWeight = FontWeight.Bold, fontSize = 13.sp, color = BharatDarkBlue)
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(10.dp)
                        ) {
                            OutlinedTextField(
                                value = refName,
                                onValueChange = { refName = it },
                                label = { Text("Reference Name") },
                                modifier = Modifier.weight(1f),
                                shape = RoundedCornerShape(12.dp)
                            )
                            OutlinedTextField(
                                value = refPhone,
                                onValueChange = { refPhone = it },
                                label = { Text("Phone") },
                                modifier = Modifier.weight(1f),
                                shape = RoundedCornerShape(12.dp)
                            )
                        }

                        // Document Upload Toggles (DL Photo, Selfie, Aadhaar - NO RC!)
                        Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                            DocumentUploadItem("Aadhaar Card Upload", isAadhaarUploaded) { isAadhaarUploaded = !isAadhaarUploaded }
                            DocumentUploadItem("DL Photo Document Uploaded *", isDlPhotoUploaded) { isDlPhotoUploaded = !isDlPhotoUploaded }
                            DocumentUploadItem("Driver Selfie Photo", isSelfieUploaded) { isSelfieUploaded = !isSelfieUploaded }
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
                            if (name.isNotBlank() && phone.isNotBlank() && dlNumber.isNotBlank()) {
                                viewModel.submitHireDriver(
                                    name = name,
                                    phone = phone,
                                    dlNumber = dlNumber,
                                    experience = selectedExperience,
                                    drivingSkill = selectedSkill
                                )
                                showSuccessDialog = true
                            } else {
                                Toast.makeText(context, "Please enter Name, Phone, and DL Number", Toast.LENGTH_SHORT).show()
                            }
                        }
                        .testTag("submit_hire_driver_form_btn"),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "SUBMIT HIRE DRIVER APPLICATION",
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

@Composable
private fun RateChartBadge(duration: String, rate: String) {
    Surface(
        color = LightSurfaceVariant,
        shape = RoundedCornerShape(10.dp),
        border = BorderStroke(0.5.dp, CardBorderColor)
    ) {
        Column(
            modifier = Modifier.padding(horizontal = 10.dp, vertical = 8.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(duration, fontSize = 11.sp, color = Color.Gray)
            Text(rate, fontSize = 14.sp, fontWeight = FontWeight.Bold, color = BharatGreen)
        }
    }
}

@Composable
private fun DocumentUploadItem(
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
