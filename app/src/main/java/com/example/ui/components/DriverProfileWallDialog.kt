package com.example.ui.components

import android.content.Intent
import android.net.Uri
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.example.model.DriverProfile
import com.example.ui.theme.AshokaBlue
import com.example.ui.theme.IndianGreen
import com.example.ui.theme.SaffronPrimary

@Composable
fun DriverProfileWallDialog(
    driver: DriverProfile,
    onDismiss: () -> Unit,
    onBookNow: () -> Unit,
    rentalTitle: String? = null,
    rentalPriceInfo: String? = null
) {
    val context = LocalContext.current

    Dialog(
        onDismissRequest = onDismiss,
        properties = DialogProperties(usePlatformDefaultWidth = false)
    ) {
        Card(
            modifier = Modifier
                .fillMaxWidth(0.94f)
                .fillMaxHeight(0.88f)
                .testTag("driver_profile_wall_card"),
            shape = RoundedCornerShape(24.dp),
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
            elevation = CardDefaults.cardElevation(defaultElevation = 8.dp)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(20.dp)
            ) {
                // Top Header Row with Close
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            Icons.Default.VerifiedUser,
                            contentDescription = null,
                            tint = IndianGreen,
                            modifier = Modifier.size(24.dp)
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = "Driver Profile Wall",
                            style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold)
                        )
                    }
                    IconButton(onClick = onDismiss, modifier = Modifier.testTag("close_profile_wall_btn")) {
                        Icon(Icons.Default.Close, contentDescription = "Close")
                    }
                }

                HorizontalDivider(modifier = Modifier.padding(vertical = 12.dp))

                // Scrollable Content
                Column(
                    modifier = Modifier
                        .weight(1f)
                        .verticalScroll(rememberScrollState()),
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    // Driver Card Header
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        // Driver Photo Avatar
                        Box(
                            modifier = Modifier
                                .size(74.dp)
                                .clip(CircleShape)
                                .background(MaterialTheme.colorScheme.primaryContainer)
                                .border(2.5.dp, SaffronPrimary, CircleShape),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = driver.avatarInitials,
                                style = MaterialTheme.typography.headlineMedium.copy(
                                    fontWeight = FontWeight.ExtraBold,
                                    color = MaterialTheme.colorScheme.onPrimaryContainer
                                )
                            )
                        }

                        Spacer(modifier = Modifier.width(16.dp))

                        Column {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Text(
                                    text = driver.name,
                                    style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold)
                                )
                                Spacer(modifier = Modifier.width(6.dp))
                                Icon(
                                    Icons.Default.CheckCircle,
                                    contentDescription = "Verified Driver",
                                    tint = IndianGreen,
                                    modifier = Modifier.size(18.dp)
                                )
                            }
                            Text(
                                text = "${driver.experienceYears}+ Years Safe Driving • ${driver.vehicleModel}",
                                style = MaterialTheme.typography.bodyMedium,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )

                            // Rating & Trips
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                modifier = Modifier.padding(top = 4.dp)
                            ) {
                                Surface(
                                    color = Color(0xFFFEF3C7),
                                    shape = RoundedCornerShape(8.dp)
                                ) {
                                    Row(
                                        modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp),
                                        verticalAlignment = Alignment.CenterVertically
                                    ) {
                                        Icon(Icons.Default.Star, contentDescription = null, tint = Color(0xFFD97706), modifier = Modifier.size(14.dp))
                                        Spacer(modifier = Modifier.width(3.dp))
                                        Text(
                                            text = "${driver.rating}",
                                            fontWeight = FontWeight.Bold,
                                            fontSize = 12.sp,
                                            color = Color(0xFF92400E)
                                        )
                                    }
                                }
                                Spacer(modifier = Modifier.width(8.dp))
                                Text(
                                    text = "${driver.totalTrips} Trips Completed",
                                    fontSize = 12.sp,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant
                                )
                            }
                        }
                    }

                    // Rental vehicle banner if applicable
                    if (rentalTitle != null && rentalPriceInfo != null) {
                        Surface(
                            color = MaterialTheme.colorScheme.secondaryContainer.copy(alpha = 0.6f),
                            shape = RoundedCornerShape(12.dp)
                        ) {
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(12.dp),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Column {
                                    Text(
                                        text = rentalTitle,
                                        style = MaterialTheme.typography.labelMedium.copy(color = MaterialTheme.colorScheme.secondary)
                                    )
                                    Text(
                                        text = driver.vehicleModel,
                                        style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.Bold)
                                    )
                                }
                                Text(
                                    text = rentalPriceInfo,
                                    style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.ExtraBold, color = SaffronPrimary)
                                )
                            }
                        }
                    }

                    // Bio
                    Text(
                        text = driver.bio,
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )

                    // Document Verification Badges (MANDATORY REQUIREMENT: RC, Insurance, Aadhaar, DL, Police)
                    Text(
                        text = "Official Government Documents & Verification",
                        style = MaterialTheme.typography.labelLarge.copy(fontWeight = FontWeight.Bold)
                    )

                    Card(
                        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f)),
                        shape = RoundedCornerShape(14.dp)
                    ) {
                        Column(
                            modifier = Modifier.padding(14.dp),
                            verticalArrangement = Arrangement.spacedBy(10.dp)
                        ) {
                            // RC Number
                            DocumentRow(
                                icon = Icons.Default.DirectionsCar,
                                title = "RC (Registration Certificate)",
                                value = driver.rcNumber,
                                status = "Verified by VAHAN",
                                isVerified = driver.isRcVerified
                            )

                            // Insurance
                            DocumentRow(
                                icon = Icons.Default.Security,
                                title = "Vehicle Insurance",
                                value = driver.insuranceValidity,
                                status = "Comprehensive Active",
                                isVerified = driver.isInsuranceVerified
                            )

                            // Commercial DL
                            DocumentRow(
                                icon = Icons.Default.Badge,
                                title = "Commercial Driving License",
                                value = "Category: Commercial Badge Endorsed",
                                status = "SARATHI Verified",
                                isVerified = driver.isCommercialDlVerified
                            )

                            // Police Verification
                            DocumentRow(
                                icon = Icons.Default.Verified,
                                title = "Police Background Verification",
                                value = "West Bengal Police Record Check",
                                status = "Clear & Approved",
                                isVerified = driver.isPoliceVerified
                            )

                            // Aadhaar Redacted
                            DocumentRow(
                                icon = Icons.Default.Fingerprint,
                                title = "Identity Verification",
                                value = "Aadhaar Card UID: XXXX-XXXX-2190",
                                status = "UIDAI Verified",
                                isVerified = driver.isAadhaarVerified
                            )
                        }
                    }

                    // Fee & Pricing Structure (MANDATORY: 8-hour fixed fee ₹800 + Overtime fee ₹100/hr)
                    Text(
                        text = "Standard Service Tariff",
                        style = MaterialTheme.typography.labelLarge.copy(fontWeight = FontWeight.Bold)
                    )

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        Card(
                            modifier = Modifier.weight(1f),
                            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.primaryContainer)
                        ) {
                            Column(modifier = Modifier.padding(12.dp)) {
                                Text("8-Hour Fixed Duty", fontSize = 12.sp, color = MaterialTheme.colorScheme.onPrimaryContainer)
                                Text("₹${driver.fixed8HrFee}", style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.ExtraBold, color = MaterialTheme.colorScheme.onPrimaryContainer))
                                Text("Standard full shift", fontSize = 11.sp, color = MaterialTheme.colorScheme.onPrimaryContainer.copy(alpha = 0.8f))
                            }
                        }

                        Card(
                            modifier = Modifier.weight(1f),
                            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.secondaryContainer)
                        ) {
                            Column(modifier = Modifier.padding(12.dp)) {
                                Text("Overtime Rate", fontSize = 12.sp, color = MaterialTheme.colorScheme.onSecondaryContainer)
                                Text("₹${driver.overtimePerHourRate}/hr", style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.ExtraBold, color = MaterialTheme.colorScheme.onSecondaryContainer))
                                Text("Beyond 8 hours duty", fontSize = 11.sp, color = MaterialTheme.colorScheme.onSecondaryContainer.copy(alpha = 0.8f))
                            }
                        }
                    }

                    // Spoken languages
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Icon(Icons.Default.Language, contentDescription = null, tint = AshokaBlue, modifier = Modifier.size(18.dp))
                        Text("Spoken: ${driver.languages.joinToString(", ")}", fontSize = 13.sp)
                    }
                }

                Spacer(modifier = Modifier.height(12.dp))

                // Bottom Action Buttons
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    OutlinedButton(
                        onClick = {
                            val intent = Intent(Intent.ACTION_DIAL, Uri.parse("tel:${driver.phone}"))
                            context.startActivity(intent)
                        },
                        modifier = Modifier
                            .weight(1f)
                            .height(50.dp)
                            .testTag("call_driver_button")
                    ) {
                        Icon(Icons.Default.Phone, contentDescription = null, modifier = Modifier.size(18.dp))
                        Spacer(modifier = Modifier.width(6.dp))
                        Text("Call Driver")
                    }

                    Button(
                        onClick = {
                            onDismiss()
                            onBookNow()
                        },
                        modifier = Modifier
                            .weight(1f)
                            .height(50.dp)
                            .testTag("book_driver_button"),
                        colors = ButtonDefaults.buttonColors(containerColor = SaffronPrimary)
                    ) {
                        Icon(Icons.Default.Check, contentDescription = null, modifier = Modifier.size(18.dp))
                        Spacer(modifier = Modifier.width(6.dp))
                        Text("Book Now")
                    }
                }
            }
        }
    }
}

@Composable
private fun DocumentRow(
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    title: String,
    value: String,
    status: String,
    isVerified: Boolean
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            icon,
            contentDescription = null,
            tint = if (isVerified) IndianGreen else Color.Gray,
            modifier = Modifier.size(20.dp)
        )
        Spacer(modifier = Modifier.width(10.dp))
        Column(modifier = Modifier.weight(1f)) {
            Text(title, fontSize = 11.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
            Text(value, fontSize = 13.sp, fontWeight = FontWeight.SemiBold)
        }
        Surface(
            color = if (isVerified) Color(0xFFDCFCE7) else Color(0xFFF3F4F6),
            shape = RoundedCornerShape(6.dp)
        ) {
            Text(
                text = status,
                fontSize = 11.sp,
                color = if (isVerified) Color(0xFF166534) else Color.Gray,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
            )
        }
    }
}
