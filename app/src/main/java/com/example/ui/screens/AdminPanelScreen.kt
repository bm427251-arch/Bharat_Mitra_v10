package com.example.ui.screens

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
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
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.example.model.ComplaintStatus
import com.example.model.EliteApprovalStatus
import com.example.ui.components.BharatMitraLogo
import com.example.ui.theme.AshokaBlue
import com.example.ui.theme.EmergencyRed
import com.example.ui.theme.IndianGreen
import com.example.ui.theme.NavySecondary
import com.example.ui.theme.SaffronPrimary
import com.example.viewmodel.MainViewModel

/**
 * Requirement 1: Dynamic Logo & Hidden Admin Panel
 * - Logo Management: Dedicated upload feature allowing admin to upload a new PNG/JPG logo at any time,
 *   instantly updating it across the app.
 * - Secret Admin Entry: Long-pressing app title or logo for 7 seconds without any visual counter
 *   silently navigates directly here.
 * - Admin Capabilities: Customize app design, manually approve Elite member requests, view user ratings,
 *   check complaints, and send direct messages to users.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AdminPanelScreen(
    viewModel: MainViewModel,
    onBack: () -> Unit
) {
    val context = LocalContext.current
    val customLogoUri by viewModel.customLogoUri.collectAsState()
    val appAnnouncement by viewModel.appAnnouncement.collectAsState()
    val eliteRegistrations by viewModel.eliteRegistrations.collectAsState()
    val complaints by viewModel.complaints.collectAsState()
    val userRatings by viewModel.userRatings.collectAsState()
    val adminMessages by viewModel.adminMessages.collectAsState()
    val verifiedDrivers by viewModel.verifiedDrivers.collectAsState()

    var selectedTab by remember { mutableIntStateOf(0) }
    val tabs = listOf("Logo & Design", "Elite Approvals", "Complaints Desk", "User Ratings", "Direct Messages", "Drivers & Tariffs")

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Box(
                            modifier = Modifier
                                .size(32.dp)
                                .clip(CircleShape)
                                .background(SaffronPrimary),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(Icons.Default.AdminPanelSettings, contentDescription = null, tint = Color.White, modifier = Modifier.size(18.dp))
                        }
                        Spacer(modifier = Modifier.width(10.dp))
                        Column {
                            Text("Admin Console", fontWeight = FontWeight.Bold, fontSize = 17.sp)
                            Text("Full Administrative Authority • Secret Bypass", fontSize = 11.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
                        }
                    }
                },
                navigationIcon = {
                    IconButton(onClick = onBack, modifier = Modifier.testTag("admin_back_btn")) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = MaterialTheme.colorScheme.surface)
            )
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            // Admin Navigation Tabs
            ScrollableTabRow(
                selectedTabIndex = selectedTab,
                edgePadding = 16.dp,
                containerColor = MaterialTheme.colorScheme.surface,
                modifier = Modifier.fillMaxWidth().testTag("admin_tabs_row")
            ) {
                tabs.forEachIndexed { index, title ->
                    Tab(
                        selected = selectedTab == index,
                        onClick = { selectedTab = index },
                        text = {
                            Text(
                                title,
                                fontWeight = if (selectedTab == index) FontWeight.Bold else FontWeight.Normal,
                                color = if (selectedTab == index) SaffronPrimary else MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }
                    )
                }
            }

            // Tab Content
            when (selectedTab) {
                0 -> {
                    // TAB 0: Logo Management & App Design Customization
                    var logoInputUrl by remember { mutableStateOf("") }
                    var newAnnouncementInput by remember { mutableStateOf(appAnnouncement) }

                    LazyColumn(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(16.dp),
                        verticalArrangement = Arrangement.spacedBy(14.dp)
                    ) {
                        item {
                            Text(
                                text = "Dynamic Logo Management",
                                style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold)
                            )
                            Text(
                                text = "Upload or apply a new PNG / JPG logo at any time. The updated logo is instantly rendered across all splash and headers in the application.",
                                fontSize = 12.sp,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }

                        // Current Active Logo Preview Card
                        item {
                            Card(
                                modifier = Modifier.fillMaxWidth(),
                                shape = RoundedCornerShape(16.dp),
                                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f))
                            ) {
                                Column(
                                    modifier = Modifier.padding(16.dp),
                                    horizontalAlignment = Alignment.CenterHorizontally,
                                    verticalArrangement = Arrangement.spacedBy(10.dp)
                                ) {
                                    Text(
                                        text = if (customLogoUri != null) "Active: Custom Uploaded Logo (PNG/JPG)" else "Active: Default High-Precision Vector Logo",
                                        fontWeight = FontWeight.Bold,
                                        fontSize = 12.sp,
                                        color = if (customLogoUri != null) SaffronPrimary else IndianGreen
                                    )

                                    // Display Logo
                                    Box(
                                        modifier = Modifier
                                            .size(160.dp)
                                            .clip(RoundedCornerShape(16.dp))
                                            .background(Color(0xFF0D1B68)),
                                        contentAlignment = Alignment.Center
                                    ) {
                                        BharatMitraLogo(
                                            size = 150.dp,
                                            showText = true,
                                            animateGlow = false,
                                            customLogoUri = customLogoUri
                                        )
                                    }

                                    if (customLogoUri != null) {
                                        OutlinedButton(
                                            onClick = { viewModel.resetLogoToDefault() },
                                            colors = ButtonDefaults.outlinedButtonColors(contentColor = EmergencyRed),
                                            border = androidx.compose.foundation.BorderStroke(1.dp, EmergencyRed),
                                            shape = RoundedCornerShape(8.dp)
                                        ) {
                                            Icon(Icons.Default.Refresh, contentDescription = null, modifier = Modifier.size(16.dp))
                                            Spacer(modifier = Modifier.width(6.dp))
                                            Text("Reset to Default Vector-Coded Logo", fontSize = 12.sp)
                                        }
                                    }
                                }
                            }
                        }

                        // Upload New Logo Form
                        item {
                            Card(
                                modifier = Modifier.fillMaxWidth().testTag("logo_upload_card"),
                                shape = RoundedCornerShape(16.dp),
                                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                                elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
                            ) {
                                Column(
                                    modifier = Modifier.padding(16.dp),
                                    verticalArrangement = Arrangement.spacedBy(12.dp)
                                ) {
                                    Text(
                                        text = "Upload / Apply New Logo",
                                        fontWeight = FontWeight.Bold,
                                        fontSize = 14.sp
                                    )

                                    OutlinedTextField(
                                        value = logoInputUrl,
                                        onValueChange = { logoInputUrl = it },
                                        label = { Text("Logo Image URL or Local File URI") },
                                        placeholder = { Text("https://example.com/logo.png or content://...") },
                                        leadingIcon = { Icon(Icons.Default.Image, contentDescription = null) },
                                        singleLine = true,
                                        modifier = Modifier.fillMaxWidth().testTag("logo_url_input"),
                                        shape = RoundedCornerShape(10.dp)
                                    )

                                    // Quick Presets from Admin Studio
                                    Text("Or select high-res admin preset:", fontSize = 11.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)

                                    Row(
                                        modifier = Modifier.fillMaxWidth(),
                                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                                    ) {
                                        OutlinedButton(
                                            onClick = {
                                                logoInputUrl = "https://images.unsplash.com/photo-1532375810709-75b1da00537c?w=600&auto=format&fit=crop&q=80"
                                            },
                                            modifier = Modifier.weight(1f).height(38.dp),
                                            shape = RoundedCornerShape(8.dp)
                                        ) {
                                            Text("Tricolor Badge", fontSize = 11.sp)
                                        }

                                        OutlinedButton(
                                            onClick = {
                                                logoInputUrl = "https://images.unsplash.com/photo-1618005182384-a83a8bd57fbe?w=600&auto=format&fit=crop&q=80"
                                            },
                                            modifier = Modifier.weight(1f).height(38.dp),
                                            shape = RoundedCornerShape(8.dp)
                                        ) {
                                            Text("Abstract Navy", fontSize = 11.sp)
                                        }
                                    }

                                    Button(
                                        onClick = {
                                            if (logoInputUrl.isNotBlank()) {
                                                viewModel.uploadNewLogo(logoInputUrl)
                                            } else {
                                                Toast.makeText(context, "Please enter an image URL or URI first", Toast.LENGTH_SHORT).show()
                                            }
                                        },
                                        modifier = Modifier.fillMaxWidth().height(46.dp).testTag("save_logo_btn"),
                                        colors = ButtonDefaults.buttonColors(containerColor = SaffronPrimary),
                                        shape = RoundedCornerShape(10.dp)
                                    ) {
                                        Icon(Icons.Default.CloudUpload, contentDescription = null)
                                        Spacer(modifier = Modifier.width(8.dp))
                                        Text("INSTANTLY APPLY LOGO ACROSS APP", fontWeight = FontWeight.Bold)
                                    }
                                }
                            }
                        }

                        // App Announcement Banner Customization
                        item {
                            Card(
                                modifier = Modifier.fillMaxWidth(),
                                shape = RoundedCornerShape(16.dp),
                                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                                elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
                            ) {
                                Column(
                                    modifier = Modifier.padding(16.dp),
                                    verticalArrangement = Arrangement.spacedBy(10.dp)
                                ) {
                                    Text(
                                        text = "App Announcement Banner",
                                        fontWeight = FontWeight.Bold,
                                        fontSize = 14.sp
                                    )

                                    OutlinedTextField(
                                        value = newAnnouncementInput,
                                        onValueChange = { newAnnouncementInput = it },
                                        label = { Text("Global Announcement Text") },
                                        modifier = Modifier.fillMaxWidth(),
                                        shape = RoundedCornerShape(10.dp)
                                    )

                                    Button(
                                        onClick = { viewModel.updateAppAnnouncement(newAnnouncementInput) },
                                        modifier = Modifier.fillMaxWidth().height(40.dp),
                                        colors = ButtonDefaults.buttonColors(containerColor = AshokaBlue),
                                        shape = RoundedCornerShape(8.dp)
                                    ) {
                                        Text("Save & Publish Announcement", fontSize = 12.sp, fontWeight = FontWeight.Bold)
                                    }
                                }
                            }
                        }
                    }
                }

                1 -> {
                    // TAB 1: Elite Membership Manual Approval Queue
                    LazyColumn(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(16.dp),
                        verticalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        item {
                            Text(
                                text = "Pending & Approved Elite Submissions (${eliteRegistrations.size})",
                                style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold)
                            )
                            Text(
                                text = "Verify applicant credentials, redacted Aadhaar, and political/organizational badge.",
                                fontSize = 12.sp,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }

                        items(eliteRegistrations) { reg ->
                            Card(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .testTag("admin_reg_card_${reg.id}"),
                                shape = RoundedCornerShape(14.dp),
                                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                                elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
                            ) {
                                Column(
                                    modifier = Modifier.padding(14.dp),
                                    verticalArrangement = Arrangement.spacedBy(8.dp)
                                ) {
                                    Row(
                                        modifier = Modifier.fillMaxWidth(),
                                        horizontalArrangement = Arrangement.SpaceBetween,
                                        verticalAlignment = Alignment.CenterVertically
                                    ) {
                                        Text(
                                            text = reg.fullName,
                                            style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold)
                                        )
                                        Surface(
                                            color = when (reg.status) {
                                                EliteApprovalStatus.APPROVED -> IndianGreen.copy(alpha = 0.15f)
                                                EliteApprovalStatus.PENDING -> Color(0xFFFEF3C7)
                                                EliteApprovalStatus.REJECTED -> EmergencyRed.copy(alpha = 0.15f)
                                            },
                                            shape = RoundedCornerShape(6.dp)
                                        ) {
                                            Text(
                                                text = reg.status.name,
                                                fontSize = 10.sp,
                                                fontWeight = FontWeight.ExtraBold,
                                                color = when (reg.status) {
                                                    EliteApprovalStatus.APPROVED -> IndianGreen
                                                    EliteApprovalStatus.PENDING -> Color(0xFFD97706)
                                                    EliteApprovalStatus.REJECTED -> EmergencyRed
                                                },
                                                modifier = Modifier.padding(horizontal = 8.dp, vertical = 3.dp)
                                            )
                                        }
                                    }

                                    Text("Phone: ${reg.phone} • Email: ${reg.email}", fontSize = 12.sp)
                                    Text("Redacted Aadhaar: ${reg.maskedAadhaar}", fontSize = 12.sp, fontWeight = FontWeight.SemiBold, color = AshokaBlue)
                                    Text("Organization / Party: ${reg.organizationName}", fontSize = 12.sp)

                                    // Uploaded ID preview chip
                                    Surface(
                                        color = MaterialTheme.colorScheme.surfaceVariant,
                                        shape = RoundedCornerShape(8.dp),
                                        modifier = Modifier.fillMaxWidth()
                                    ) {
                                        Row(
                                            modifier = Modifier.padding(8.dp),
                                            verticalAlignment = Alignment.CenterVertically
                                        ) {
                                            Icon(Icons.Default.Badge, contentDescription = null, tint = SaffronPrimary, modifier = Modifier.size(18.dp))
                                            Spacer(modifier = Modifier.width(8.dp))
                                            Text("Attached Document: ${reg.idCardFileName} (Verified Valid)", fontSize = 11.sp)
                                        }
                                    }

                                    // Approve / Reject Actions for Pending
                                    if (reg.status == EliteApprovalStatus.PENDING) {
                                        Row(
                                            modifier = Modifier.fillMaxWidth(),
                                            horizontalArrangement = Arrangement.spacedBy(10.dp)
                                        ) {
                                            OutlinedButton(
                                                onClick = { viewModel.rejectEliteRegistration(reg.id) },
                                                modifier = Modifier.weight(1f).height(40.dp).testTag("reject_reg_${reg.id}"),
                                                colors = ButtonDefaults.outlinedButtonColors(contentColor = EmergencyRed),
                                                border = androidx.compose.foundation.BorderStroke(1.dp, EmergencyRed)
                                            ) {
                                                Icon(Icons.Default.Close, contentDescription = null, modifier = Modifier.size(16.dp))
                                                Spacer(modifier = Modifier.width(4.dp))
                                                Text("Reject", fontSize = 12.sp)
                                            }

                                            Button(
                                                onClick = { viewModel.approveEliteRegistration(reg.id) },
                                                modifier = Modifier.weight(1f).height(40.dp).testTag("approve_reg_${reg.id}"),
                                                colors = ButtonDefaults.buttonColors(containerColor = IndianGreen)
                                            ) {
                                                Icon(Icons.Default.Check, contentDescription = null, modifier = Modifier.size(16.dp))
                                                Spacer(modifier = Modifier.width(4.dp))
                                                Text("Approve Member", fontSize = 12.sp)
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }
                }

                2 -> {
                    // TAB 2: Complaints Desk
                    LazyColumn(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(16.dp),
                        verticalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        item {
                            Text(
                                text = "User Complaints & Dispute Resolution (${complaints.size})",
                                style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold)
                            )
                        }

                        items(complaints) { complaint ->
                            var replyText by remember { mutableStateOf(complaint.adminReply) }

                            Card(
                                modifier = Modifier.fillMaxWidth().testTag("complaint_card_${complaint.id}"),
                                shape = RoundedCornerShape(14.dp),
                                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                                elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
                            ) {
                                Column(
                                    modifier = Modifier.padding(14.dp),
                                    verticalArrangement = Arrangement.spacedBy(8.dp)
                                ) {
                                    Row(
                                        modifier = Modifier.fillMaxWidth(),
                                        horizontalArrangement = Arrangement.SpaceBetween,
                                        verticalAlignment = Alignment.CenterVertically
                                    ) {
                                        Text(complaint.issueType, fontWeight = FontWeight.Bold, fontSize = 14.sp, color = EmergencyRed)
                                        Surface(
                                            color = if (complaint.status == ComplaintStatus.RESOLVED) IndianGreen.copy(alpha = 0.15f) else Color(0xFFFEF3C7),
                                            shape = RoundedCornerShape(6.dp)
                                        ) {
                                            Text(
                                                text = complaint.status.name,
                                                fontSize = 10.sp,
                                                fontWeight = FontWeight.Bold,
                                                color = if (complaint.status == ComplaintStatus.RESOLVED) IndianGreen else Color(0xFFD97706),
                                                modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                                            )
                                        }
                                    }

                                    Text("From: ${complaint.userName} (${complaint.userPhone}) • Filed: ${complaint.filedAt}", fontSize = 11.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
                                    Text(complaint.details, fontSize = 13.sp)

                                    if (complaint.status != ComplaintStatus.RESOLVED) {
                                        OutlinedTextField(
                                            value = replyText,
                                            onValueChange = { replyText = it },
                                            label = { Text("Admin Official Reply & Resolution") },
                                            modifier = Modifier.fillMaxWidth(),
                                            shape = RoundedCornerShape(8.dp)
                                        )

                                        Button(
                                            onClick = {
                                                viewModel.resolveComplaint(complaint.id, replyText.ifBlank { "Issue verified and resolved by Bharat Mitra Admin." })
                                            },
                                            modifier = Modifier.align(Alignment.End).height(36.dp),
                                            colors = ButtonDefaults.buttonColors(containerColor = IndianGreen)
                                        ) {
                                            Text("Mark Resolved & Reply", fontSize = 11.sp, fontWeight = FontWeight.Bold)
                                        }
                                    } else {
                                        Surface(
                                            color = IndianGreen.copy(alpha = 0.1f),
                                            shape = RoundedCornerShape(8.dp),
                                            modifier = Modifier.fillMaxWidth()
                                        ) {
                                            Text(
                                                text = "Admin Reply: ${complaint.adminReply}",
                                                fontSize = 11.sp,
                                                color = IndianGreen,
                                                fontWeight = FontWeight.Medium,
                                                modifier = Modifier.padding(8.dp)
                                            )
                                        }
                                    }
                                }
                            }
                        }
                    }
                }

                3 -> {
                    // TAB 3: User Ratings & Reviews
                    LazyColumn(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(16.dp),
                        verticalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        item {
                            // Ratings Overview Card
                            Card(
                                modifier = Modifier.fillMaxWidth(),
                                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.primaryContainer),
                                shape = RoundedCornerShape(14.dp)
                            ) {
                                Row(
                                    modifier = Modifier.padding(16.dp),
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.SpaceBetween
                                ) {
                                    Column {
                                        Text("Overall Platform Rating", fontSize = 12.sp, color = MaterialTheme.colorScheme.onPrimaryContainer)
                                        Text("4.92 ★", style = MaterialTheme.typography.headlineMedium.copy(fontWeight = FontWeight.ExtraBold, color = MaterialTheme.colorScheme.onPrimaryContainer))
                                        Text("Based on 3,420 user ratings", fontSize = 11.sp, color = MaterialTheme.colorScheme.onPrimaryContainer.copy(alpha = 0.8f))
                                    }
                                    Icon(Icons.Default.Stars, contentDescription = null, tint = SaffronPrimary, modifier = Modifier.size(48.dp))
                                }
                            }
                        }

                        items(userRatings) { rating ->
                            Card(
                                modifier = Modifier.fillMaxWidth(),
                                shape = RoundedCornerShape(12.dp),
                                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
                            ) {
                                Column(modifier = Modifier.padding(12.dp), verticalArrangement = Arrangement.spacedBy(4.dp)) {
                                    Row(
                                        modifier = Modifier.fillMaxWidth(),
                                        horizontalArrangement = Arrangement.SpaceBetween,
                                        verticalAlignment = Alignment.CenterVertically
                                    ) {
                                        Text(rating.userName, fontWeight = FontWeight.Bold, fontSize = 13.sp)
                                        Row {
                                            repeat(rating.rating) {
                                                Icon(Icons.Default.Star, contentDescription = null, tint = Color(0xFFD97706), modifier = Modifier.size(14.dp))
                                            }
                                        }
                                    }
                                    Text("Category: ${rating.category} • ${rating.date}", fontSize = 11.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
                                    Text("\"${rating.feedback}\"", fontSize = 12.sp)
                                }
                            }
                        }
                    }
                }

                4 -> {
                    // TAB 4: Direct Messages to Users
                    var msgTitle by remember { mutableStateOf("") }
                    var msgBody by remember { mutableStateOf("") }
                    var isUrgent by remember { mutableStateOf(false) }

                    LazyColumn(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(16.dp),
                        verticalArrangement = Arrangement.spacedBy(14.dp)
                    ) {
                        item {
                            Text(
                                text = "Send Direct Message & Announcements",
                                style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold)
                            )
                            Text(
                                text = "Compose notifications and direct messages delivered straight to user inboxes.",
                                fontSize = 12.sp,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }

                        item {
                            Card(
                                modifier = Modifier.fillMaxWidth(),
                                shape = RoundedCornerShape(14.dp),
                                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                                elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
                            ) {
                                Column(
                                    modifier = Modifier.padding(14.dp),
                                    verticalArrangement = Arrangement.spacedBy(10.dp)
                                ) {
                                    OutlinedTextField(
                                        value = msgTitle,
                                        onValueChange = { msgTitle = it },
                                        label = { Text("Message Title / Subject") },
                                        singleLine = true,
                                        modifier = Modifier.fillMaxWidth(),
                                        shape = RoundedCornerShape(10.dp)
                                    )

                                    OutlinedTextField(
                                        value = msgBody,
                                        onValueChange = { msgBody = it },
                                        label = { Text("Message Body Content") },
                                        modifier = Modifier.fillMaxWidth(),
                                        shape = RoundedCornerShape(10.dp),
                                        minLines = 3
                                    )

                                    Row(
                                        modifier = Modifier.fillMaxWidth(),
                                        horizontalArrangement = Arrangement.SpaceBetween,
                                        verticalAlignment = Alignment.CenterVertically
                                    ) {
                                        Text("Mark as High-Priority SOS / Alert", fontSize = 12.sp)
                                        Switch(checked = isUrgent, onCheckedChange = { isUrgent = it })
                                    }

                                    Button(
                                        onClick = {
                                            if (msgTitle.isNotBlank() && msgBody.isNotBlank()) {
                                                viewModel.sendAdminDirectMessage(msgTitle, msgBody, isUrgent)
                                                msgTitle = ""
                                                msgBody = ""
                                                isUrgent = false
                                            } else {
                                                Toast.makeText(context, "Please enter both Title and Body", Toast.LENGTH_SHORT).show()
                                            }
                                        },
                                        modifier = Modifier.fillMaxWidth().height(46.dp),
                                        colors = ButtonDefaults.buttonColors(containerColor = SaffronPrimary),
                                        shape = RoundedCornerShape(10.dp)
                                    ) {
                                        Icon(Icons.Default.Send, contentDescription = null)
                                        Spacer(modifier = Modifier.width(8.dp))
                                        Text("TRANSMIT MESSAGE TO ALL USERS", fontWeight = FontWeight.Bold)
                                    }
                                }
                            }
                        }

                        item {
                            Text("Sent Messages Archive (${adminMessages.size})", fontWeight = FontWeight.Bold, fontSize = 14.sp)
                        }

                        items(adminMessages) { msg ->
                            Card(
                                modifier = Modifier.fillMaxWidth(),
                                shape = RoundedCornerShape(12.dp),
                                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f))
                            ) {
                                Column(modifier = Modifier.padding(12.dp), verticalArrangement = Arrangement.spacedBy(4.dp)) {
                                    Row(
                                        modifier = Modifier.fillMaxWidth(),
                                        horizontalArrangement = Arrangement.SpaceBetween
                                    ) {
                                        Text(msg.title, fontWeight = FontWeight.Bold, fontSize = 13.sp)
                                        Text(msg.sentAt, fontSize = 10.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
                                    }
                                    Text(msg.body, fontSize = 12.sp)
                                }
                            }
                        }
                    }
                }

                else -> {
                    // TAB 5: Drivers & Tariffs
                    LazyColumn(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(16.dp),
                        verticalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        item {
                            Text(
                                text = "Verified Drivers Database (${verifiedDrivers.size})",
                                style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold)
                            )
                        }

                        items(verifiedDrivers) { driver ->
                            var fixedFeeInput by remember { mutableStateOf(driver.fixed8HrFee.toString()) }
                            var overtimeRateInput by remember { mutableStateOf(driver.overtimePerHourRate.toString()) }

                            Card(
                                modifier = Modifier.fillMaxWidth(),
                                shape = RoundedCornerShape(14.dp),
                                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
                            ) {
                                Column(
                                    modifier = Modifier.padding(14.dp),
                                    verticalArrangement = Arrangement.spacedBy(8.dp)
                                ) {
                                    Row(
                                        modifier = Modifier.fillMaxWidth(),
                                        horizontalArrangement = Arrangement.SpaceBetween,
                                        verticalAlignment = Alignment.CenterVertically
                                    ) {
                                        Text(driver.name, fontWeight = FontWeight.Bold, fontSize = 14.sp)
                                        Text(driver.rcNumber, fontSize = 12.sp, color = IndianGreen, fontWeight = FontWeight.Bold)
                                    }
                                    Text("${driver.vehicleModel} • Rating: ${driver.rating} ★ (${driver.totalTrips} trips)", fontSize = 12.sp)

                                    Row(
                                        modifier = Modifier.fillMaxWidth(),
                                        horizontalArrangement = Arrangement.spacedBy(10.dp)
                                    ) {
                                        OutlinedTextField(
                                            value = fixedFeeInput,
                                            onValueChange = { fixedFeeInput = it },
                                            label = { Text("8-Hr Fee (₹)") },
                                            modifier = Modifier.weight(1f),
                                            singleLine = true
                                        )

                                        OutlinedTextField(
                                            value = overtimeRateInput,
                                            onValueChange = { overtimeRateInput = it },
                                            label = { Text("Overtime/hr (₹)") },
                                            modifier = Modifier.weight(1f),
                                            singleLine = true
                                        )
                                    }

                                    Button(
                                        onClick = {
                                            val ff = fixedFeeInput.toIntOrNull() ?: 800
                                            val ot = overtimeRateInput.toIntOrNull() ?: 100
                                            viewModel.updateDriverTariff(driver.id, ff, ot)
                                        },
                                        modifier = Modifier.fillMaxWidth().height(38.dp),
                                        colors = ButtonDefaults.buttonColors(containerColor = SaffronPrimary)
                                    ) {
                                        Text("Update Tariff Rate", fontSize = 12.sp)
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}
