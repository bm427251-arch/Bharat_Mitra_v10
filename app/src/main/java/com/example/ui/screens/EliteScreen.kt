package com.example.ui.screens

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.widget.Toast
import androidx.compose.animation.*
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
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
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.SampleData
import com.example.model.DotType
import com.example.model.EliteApprovalStatus
import com.example.ui.components.EmergencySosDialog
import com.example.ui.components.InteractiveMapCanvas
import com.example.ui.theme.AshokaBlue
import com.example.ui.theme.EmergencyRed
import com.example.ui.theme.IndianGreen
import com.example.ui.theme.SaffronPrimary
import com.example.viewmodel.MainViewModel

/**
 * Requirement 5: Elite Service Section (₹29/month subscription)
 * - Registration Form: Collects Name, Phone, Email, [Aadhaar Redacted], and Political/Org ID photo.
 * - Map Dot System: Orange Dot (Nearby 1/3/5km radius), Green Dot (Group members globally).
 * - Call Logic & Privacy: Orange Call (Public Emergency), Green Call (Group Only), Privacy Feature.
 * - Payments & GPS Sharing: Razorpay link integration (razorpay.me/@bharatmitrainfotech),
 *   Extra ₹29/24hr fee for continuous group GPS sharing.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EliteScreen(
    viewModel: MainViewModel,
    onBack: () -> Unit
) {
    val context = LocalContext.current
    val eliteRadiusKm by viewModel.eliteRadiusKm.collectAsState()
    val mapDots by viewModel.mapDots.collectAsState()
    val eliteRegistrations by viewModel.eliteRegistrations.collectAsState()
    val is24HrGpsActive by viewModel.is24HrGpsSharingActive.collectAsState()
    val activeEmergencyAlert by viewModel.activeEmergencyAlert.collectAsState()

    // Registration Form Inputs
    var regName by remember { mutableStateOf("") }
    var regPhone by remember { mutableStateOf("") }
    var regEmail by remember { mutableStateOf("") }
    var rawAadhaarDigits by remember { mutableStateOf("") }
    var orgName by remember { mutableStateOf("") }
    var isOrgIdUploaded by remember { mutableStateOf(false) }
    var showRegistrationSheet by remember { mutableStateOf(true) }

    // User's own registration status
    val myRegistration = eliteRegistrations.firstOrNull { it.phone == regPhone.ifBlank { "+91 98310 99421" } }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Column {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Text("Elite Community Network", fontWeight = FontWeight.Bold, fontSize = 17.sp)
                            Spacer(modifier = Modifier.width(6.dp))
                            Surface(
                                color = SaffronPrimary.copy(alpha = 0.2f),
                                shape = RoundedCornerShape(6.dp)
                            ) {
                                Text(
                                    "₹29/mo",
                                    fontSize = 11.sp,
                                    fontWeight = FontWeight.ExtraBold,
                                    color = SaffronPrimary,
                                    modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                                )
                            }
                        }
                        Text("Mutual emergency protection & worldwide tracking", fontSize = 11.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
                    }
                },
                navigationIcon = {
                    IconButton(onClick = onBack, modifier = Modifier.testTag("elite_back_btn")) {
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
            // 1. Subscription & Razorpay Quick Action Banner
            item {
                Card(
                    modifier = Modifier.fillMaxWidth().testTag("elite_subscription_card"),
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.secondaryContainer.copy(alpha = 0.65f))
                ) {
                    Column(
                        modifier = Modifier.padding(16.dp),
                        verticalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Column {
                                Text(
                                    text = "Elite Safety Membership",
                                    style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold)
                                )
                                Text(
                                    text = "₹29 / Month • Self or Third-Party Payment",
                                    fontSize = 12.sp,
                                    color = MaterialTheme.colorScheme.onSecondaryContainer
                                )
                            }

                            Surface(
                                color = if (myRegistration?.status == EliteApprovalStatus.APPROVED) IndianGreen else Color(0xFFD97706),
                                shape = RoundedCornerShape(8.dp)
                            ) {
                                Text(
                                    text = when (myRegistration?.status) {
                                        EliteApprovalStatus.APPROVED -> "ACTIVE ELITE"
                                        EliteApprovalStatus.PENDING -> "PENDING APPROVAL"
                                        else -> "NOT REGISTERED"
                                    },
                                    fontSize = 10.sp,
                                    fontWeight = FontWeight.ExtraBold,
                                    color = Color.White,
                                    modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                                )
                            }
                        }

                        // Razorpay link integration button
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(10.dp)
                        ) {
                            Button(
                                onClick = { viewModel.openRazorpayPayment(context) },
                                modifier = Modifier.weight(1f).height(44.dp).testTag("pay_razorpay_btn"),
                                colors = ButtonDefaults.buttonColors(containerColor = SaffronPrimary),
                                shape = RoundedCornerShape(10.dp)
                            ) {
                                Icon(Icons.Default.Payment, contentDescription = null, modifier = Modifier.size(16.dp))
                                Spacer(modifier = Modifier.width(6.dp))
                                Text("Pay via Razorpay", fontSize = 12.sp, fontWeight = FontWeight.Bold)
                            }

                            OutlinedButton(
                                onClick = {
                                    val cm = context.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
                                    cm.setPrimaryClip(ClipData.newPlainText("Razorpay Link", SampleData.RAZORPAY_PAYMENT_URL))
                                    Toast.makeText(context, "Razorpay link copied to clipboard!", Toast.LENGTH_SHORT).show()
                                },
                                modifier = Modifier.height(44.dp).testTag("copy_razorpay_link_btn"),
                                shape = RoundedCornerShape(10.dp)
                            ) {
                                Icon(Icons.Default.ContentCopy, contentDescription = "Copy Link", modifier = Modifier.size(16.dp))
                            }
                        }
                    }
                }
            }

            // 2. Call Logic: ORANGE CALL (Public) & GREEN CALL (Group Only)
            item {
                Text(
                    text = "Emergency SOS Broadcast Actions",
                    style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold)
                )
            }

            item {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    // ORANGE CALL (Public Emergency)
                    Card(
                        modifier = Modifier
                            .weight(1f)
                            .clickable { viewModel.triggerOrangeCall() }
                            .testTag("trigger_orange_call_card"),
                        colors = CardDefaults.cardColors(containerColor = Color(0xFFFFF7ED)),
                        border = androidx.compose.foundation.BorderStroke(2.dp, SaffronPrimary),
                        shape = RoundedCornerShape(14.dp)
                    ) {
                        Column(
                            modifier = Modifier.padding(14.dp),
                            horizontalAlignment = Alignment.CenterHorizontally,
                            verticalArrangement = Arrangement.spacedBy(6.dp)
                        ) {
                            Box(
                                modifier = Modifier
                                    .size(44.dp)
                                    .clip(CircleShape)
                                    .background(SaffronPrimary),
                                contentAlignment = Alignment.Center
                            ) {
                                Icon(Icons.Default.Emergency, contentDescription = null, tint = Color.White)
                            }
                            Text(
                                text = "ORANGE CALL",
                                fontWeight = FontWeight.ExtraBold,
                                fontSize = 13.sp,
                                color = SaffronPrimary
                            )
                            Text(
                                text = "PUBLIC EMERGENCY",
                                fontSize = 10.sp,
                                fontWeight = FontWeight.Bold,
                                color = Color(0xFF9A3412)
                            )
                            Text(
                                text = "Alerts both Nearby Elite & Group Dots",
                                fontSize = 10.sp,
                                textAlign = TextAlign.Center,
                                color = Color(0xFF7C2D12)
                            )
                        }
                    }

                    // GREEN CALL (Group Only)
                    Card(
                        modifier = Modifier
                            .weight(1f)
                            .clickable { viewModel.triggerGreenCall() }
                            .testTag("trigger_green_call_card"),
                        colors = CardDefaults.cardColors(containerColor = Color(0xFFF0FDF4)),
                        border = androidx.compose.foundation.BorderStroke(2.dp, IndianGreen),
                        shape = RoundedCornerShape(14.dp)
                    ) {
                        Column(
                            modifier = Modifier.padding(14.dp),
                            horizontalAlignment = Alignment.CenterHorizontally,
                            verticalArrangement = Arrangement.spacedBy(6.dp)
                        ) {
                            Box(
                                modifier = Modifier
                                    .size(44.dp)
                                    .clip(CircleShape)
                                    .background(IndianGreen),
                                contentAlignment = Alignment.Center
                            ) {
                                Icon(Icons.Default.Groups, contentDescription = null, tint = Color.White)
                            }
                            Text(
                                text = "GREEN CALL",
                                fontWeight = FontWeight.ExtraBold,
                                fontSize = 13.sp,
                                color = IndianGreen
                            )
                            Text(
                                text = "GROUP ONLY",
                                fontSize = 10.sp,
                                fontWeight = FontWeight.Bold,
                                color = Color(0xFF166534)
                            )
                            Text(
                                text = "Alerts user's created group members only",
                                fontSize = 10.sp,
                                textAlign = TextAlign.Center,
                                color = Color(0xFF14532D)
                            )
                        }
                    }
                }
            }

            // 3. Map Dot System with Radius Filter (1/3/5 km)
            item {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column {
                        Text(
                            text = "Elite & Group Radar Network",
                            style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold)
                        )
                        Text(
                            text = "Orange: Nearby Elite • Green: Your Group (Worldwide)",
                            fontSize = 11.sp,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }

                    // Radius selector: 1 / 3 / 5 km
                    Row(
                        horizontalArrangement = Arrangement.spacedBy(4.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        listOf(1, 3, 5).forEach { radius ->
                            val isSelected = eliteRadiusKm == radius
                            Surface(
                                modifier = Modifier
                                    .clip(RoundedCornerShape(8.dp))
                                    .clickable { viewModel.setEliteRadius(radius) }
                                    .testTag("radius_filter_${radius}km"),
                                color = if (isSelected) SaffronPrimary else MaterialTheme.colorScheme.surfaceVariant
                            ) {
                                Text(
                                    text = "${radius}km",
                                    fontSize = 11.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = if (isSelected) Color.White else MaterialTheme.colorScheme.onSurfaceVariant,
                                    modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                                )
                            }
                        }
                    }
                }
            }

            // Interactive Radar Map
            item {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(230.dp)
                ) {
                    InteractiveMapCanvas(
                        userGpsTitle = "Barasat",
                        mapDots = mapDots,
                        selectedRadiusKm = eliteRadiusKm,
                        showEliteDots = true
                    )
                }
            }

            // 4. 24-Hour Continuous Group GPS Sharing (Extra ₹29/24hr)
            item {
                Card(
                    shape = RoundedCornerShape(14.dp),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f))
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(14.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Row(modifier = Modifier.weight(1f), verticalAlignment = Alignment.CenterVertically) {
                            Icon(Icons.Default.ShareLocation, contentDescription = null, tint = AshokaBlue, modifier = Modifier.size(24.dp))
                            Spacer(modifier = Modifier.width(10.dp))
                            Column {
                                Text(
                                    text = "24-Hour Group GPS Sharing",
                                    style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold)
                                )
                                Text(
                                    text = "Extra ₹29/24hr fee managed by Group Admin",
                                    fontSize = 11.sp,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant
                                )
                            }
                        }

                        Switch(
                            checked = is24HrGpsActive,
                            onCheckedChange = { viewModel.toggle24HrGpsSharing(it, context) },
                            modifier = Modifier.testTag("toggle_24hr_gps_switch")
                        )
                    }
                }
            }

            // 5. Elite Registration Form Header / Button
            item {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "Official KYC Registration",
                        style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold)
                    )
                    TextButton(
                        onClick = { showRegistrationSheet = !showRegistrationSheet },
                        modifier = Modifier.testTag("toggle_reg_form_btn")
                    ) {
                        Text(if (showRegistrationSheet) "Hide Form" else "Open Form", color = SaffronPrimary, fontWeight = FontWeight.Bold)
                    }
                }
            }

            if (showRegistrationSheet) {
                item {
                    Card(
                        modifier = Modifier.fillMaxWidth().testTag("elite_registration_form_card"),
                        shape = RoundedCornerShape(16.dp),
                        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                        elevation = CardDefaults.cardElevation(defaultElevation = 3.dp)
                    ) {
                        Column(
                            modifier = Modifier.padding(16.dp),
                            verticalArrangement = Arrangement.spacedBy(10.dp)
                        ) {
                            Text(
                                text = "Submit for Manual Admin Approval",
                                fontSize = 12.sp,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )

                            OutlinedTextField(
                                value = regName,
                                onValueChange = { regName = it },
                                label = { Text("Full Name") },
                                singleLine = true,
                                modifier = Modifier.fillMaxWidth().testTag("reg_name_input"),
                                shape = RoundedCornerShape(10.dp)
                            )

                            OutlinedTextField(
                                value = regPhone,
                                onValueChange = { regPhone = it },
                                label = { Text("Mobile Phone Number") },
                                singleLine = true,
                                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Phone),
                                modifier = Modifier.fillMaxWidth().testTag("reg_phone_input"),
                                shape = RoundedCornerShape(10.dp)
                            )

                            OutlinedTextField(
                                value = regEmail,
                                onValueChange = { regEmail = it },
                                label = { Text("Email Address") },
                                singleLine = true,
                                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
                                modifier = Modifier.fillMaxWidth().testTag("reg_email_input"),
                                shape = RoundedCornerShape(10.dp)
                            )

                            // Redacted Aadhaar Input
                            OutlinedTextField(
                                value = rawAadhaarDigits,
                                onValueChange = { if (it.length <= 12 && it.all { c -> c.isDigit() }) rawAadhaarDigits = it },
                                label = { Text("Aadhaar Number (Auto-Redacted for Privacy)") },
                                placeholder = { Text("12-Digit UID (XXXX-XXXX-1234)") },
                                singleLine = true,
                                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                                trailingIcon = {
                                    Icon(Icons.Default.Lock, contentDescription = "Encrypted UIDAI", tint = IndianGreen)
                                },
                                supportingText = {
                                    Text(
                                        text = if (rawAadhaarDigits.length >= 4) {
                                            "Redacted storage format: XXXX-XXXX-${rawAadhaarDigits.takeLast(4)}"
                                        } else "Enter 12 digits for encrypted masking",
                                        fontSize = 11.sp,
                                        color = IndianGreen
                                    )
                                },
                                modifier = Modifier.fillMaxWidth().testTag("reg_aadhaar_input"),
                                shape = RoundedCornerShape(10.dp)
                            )

                            OutlinedTextField(
                                value = orgName,
                                onValueChange = { orgName = it },
                                label = { Text("Political / Organizational Association Name") },
                                singleLine = true,
                                modifier = Modifier.fillMaxWidth().testTag("reg_org_name_input"),
                                shape = RoundedCornerShape(10.dp)
                            )

                            // ID Card Upload Button
                            Surface(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .clickable {
                                        isOrgIdUploaded = true
                                        Toast.makeText(context, "ID Card captured & attached successfully!", Toast.LENGTH_SHORT).show()
                                    }
                                    .testTag("upload_id_card_btn"),
                                color = if (isOrgIdUploaded) IndianGreen.copy(alpha = 0.15f) else MaterialTheme.colorScheme.surfaceVariant,
                                shape = RoundedCornerShape(10.dp),
                                border = androidx.compose.foundation.BorderStroke(1.dp, if (isOrgIdUploaded) IndianGreen else Color.Gray.copy(alpha = 0.5f))
                            ) {
                                Row(
                                    modifier = Modifier.padding(12.dp),
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.Center
                                ) {
                                    Icon(
                                        if (isOrgIdUploaded) Icons.Default.CheckCircle else Icons.Default.AddAPhoto,
                                        contentDescription = null,
                                        tint = if (isOrgIdUploaded) IndianGreen else MaterialTheme.colorScheme.primary,
                                        modifier = Modifier.size(20.dp)
                                    )
                                    Spacer(modifier = Modifier.width(8.dp))
                                    Text(
                                        text = if (isOrgIdUploaded) "Political / Org ID Card Attached (id_card_01.jpg)" else "Capture / Upload Political / Org ID Card Photo",
                                        fontSize = 12.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = if (isOrgIdUploaded) IndianGreen else MaterialTheme.colorScheme.onSurface
                                    )
                                }
                            }

                            Button(
                                onClick = {
                                    if (regName.isNotBlank() && regPhone.isNotBlank()) {
                                        viewModel.submitEliteRegistration(
                                            name = regName,
                                            phone = regPhone,
                                            email = regEmail,
                                            aadhaar = rawAadhaarDigits,
                                            orgName = orgName
                                        )
                                        showRegistrationSheet = false
                                    } else {
                                        Toast.makeText(context, "Please enter at least Name and Phone", Toast.LENGTH_SHORT).show()
                                    }
                                },
                                modifier = Modifier.fillMaxWidth().height(48.dp).testTag("submit_elite_registration_btn"),
                                colors = ButtonDefaults.buttonColors(containerColor = SaffronPrimary),
                                shape = RoundedCornerShape(10.dp)
                            ) {
                                Text("SUBMIT FOR MANUAL ADMIN SCRUTINY", fontWeight = FontWeight.Bold)
                            }
                        }
                    }
                }
            }

            item {
                Spacer(modifier = Modifier.height(20.dp))
            }
        }
    }

    // Emergency SOS Active Alert Dialog
    if (activeEmergencyAlert != null) {
        EmergencySosDialog(
            alert = activeEmergencyAlert!!,
            onDismiss = { viewModel.endEmergencyCall() },
            onAcceptVideoByReceiver = { viewModel.acceptVideoByReceiver() },
            onCancelEmergency = { viewModel.endEmergencyCall() }
        )
    }
}
