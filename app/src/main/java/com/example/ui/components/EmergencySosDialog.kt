package com.example.ui.components

import androidx.compose.animation.*
import androidx.compose.animation.core.*
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.example.model.EmergencyAlert
import com.example.model.EmergencyType
import com.example.ui.theme.EmergencyRed
import com.example.ui.theme.IndianGreen
import com.example.ui.theme.SaffronPrimary
import kotlinx.coroutines.delay

@Composable
fun EmergencySosDialog(
    alert: EmergencyAlert,
    onDismiss: () -> Unit,
    onAcceptVideoByReceiver: () -> Unit,
    onCancelEmergency: () -> Unit
) {
    var callSeconds by remember { mutableIntStateOf(0) }
    LaunchedEffect(Unit) {
        while (true) {
            delay(1000)
            callSeconds++
        }
    }

    val infiniteTransition = rememberInfiniteTransition(label = "sos_pulse")
    val pulseScale by infiniteTransition.animateFloat(
        initialValue = 0.95f,
        targetValue = 1.08f,
        animationSpec = infiniteRepeatable(
            animation = tween(700, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "sos_pulse_scale"
    )

    val isOrangePublic = alert.type == EmergencyType.ORANGE_PUBLIC
    val alertTitle = if (isOrangePublic) "ORANGE CALL: PUBLIC EMERGENCY" else "GREEN CALL: GROUP EMERGENCY"
    val broadcastTarget = if (isOrangePublic) {
        "Broadcasting to BOTH Orange (Nearby Elite) & Green (My Group) Dots"
    } else {
        "Broadcasting ONLY to Green Group Dots"
    }
    val themeColor = if (isOrangePublic) SaffronPrimary else IndianGreen

    Dialog(
        onDismissRequest = { /* User must explicitly end SOS */ },
        properties = DialogProperties(dismissOnBackPress = false, dismissOnClickOutside = false, usePlatformDefaultWidth = false)
    ) {
        Card(
            modifier = Modifier
                .fillMaxWidth(0.95f)
                .fillMaxHeight(0.92f)
                .testTag("emergency_sos_dialog"),
            shape = RoundedCornerShape(24.dp),
            colors = CardDefaults.cardColors(containerColor = Color(0xFF0F172A))
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(20.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.SpaceBetween
            ) {
                // Top Header Badge
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Surface(
                        color = themeColor.copy(alpha = 0.2f),
                        shape = RoundedCornerShape(20.dp),
                        border = androidx.compose.foundation.BorderStroke(1.5.dp, themeColor)
                    ) {
                        Row(
                            modifier = Modifier.padding(horizontal = 14.dp, vertical = 6.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Box(
                                modifier = Modifier
                                    .size(10.dp)
                                    .clip(CircleShape)
                                    .background(themeColor)
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                text = "EMERGENCY BROADCAST ACTIVE",
                                fontSize = 12.sp,
                                fontWeight = FontWeight.ExtraBold,
                                color = themeColor,
                                letterSpacing = 1.sp
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(10.dp))
                    Text(
                        text = alertTitle,
                        style = MaterialTheme.typography.titleLarge.copy(
                            fontWeight = FontWeight.ExtraBold,
                            color = Color.White
                        ),
                        textAlign = TextAlign.Center
                    )
                    Text(
                        text = broadcastTarget,
                        style = MaterialTheme.typography.bodySmall.copy(color = Color(0xFF94A3B8)),
                        textAlign = TextAlign.Center,
                        modifier = Modifier.padding(top = 4.dp)
                    )
                }

                // Middle: Telemetry & Stream Privacy Box
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(16.dp))
                        .background(Color(0xFF1E293B))
                        .padding(16.dp),
                    verticalArrangement = Arrangement.spacedBy(14.dp)
                ) {
                    // Location Telemetry
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(Icons.Default.LocationOn, contentDescription = null, tint = SaffronPrimary, modifier = Modifier.size(24.dp))
                        Spacer(modifier = Modifier.width(10.dp))
                        Column {
                            Text("LIVE GPS LOCATION SHARED", fontSize = 11.sp, fontWeight = FontWeight.Bold, color = SaffronPrimary)
                            Text(alert.locationDescription, fontSize = 13.sp, fontWeight = FontWeight.SemiBold, color = Color.White)
                            Text(
                                "Coordinates: ${String.format("%.4f", alert.latitude)}° N, ${String.format("%.4f", alert.longitude)}° E (Accuracy: ±3m)",
                                fontSize = 10.sp,
                                color = Color(0xFF94A3B8)
                            )
                        }
                    }

                    HorizontalDivider(color = Color(0xFF334155))

                    // Audio Stream Status
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(Icons.Default.Mic, contentDescription = null, tint = IndianGreen, modifier = Modifier.size(24.dp))
                        Spacer(modifier = Modifier.width(10.dp))
                        Column {
                            Text("AUDIO STREAM", fontSize = 11.sp, fontWeight = FontWeight.Bold, color = IndianGreen)
                            Text("Microphone Active • Real-Time High-Fidelity Audio Transmitting", fontSize = 12.sp, color = Color.White)
                        }
                    }

                    HorizontalDivider(color = Color(0xFF334155))

                    // MANDATORY PRIVACY FEATURE: Camera stays OFF unless receiver explicitly accepts!
                    Column {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Icon(
                                    if (alert.isVideoAcceptedByReceiver) Icons.Default.Videocam else Icons.Default.VideocamOff,
                                    contentDescription = null,
                                    tint = if (alert.isVideoAcceptedByReceiver) IndianGreen else EmergencyRed,
                                    modifier = Modifier.size(24.dp)
                                )
                                Spacer(modifier = Modifier.width(10.dp))
                                Column {
                                    Text("VIDEO FEED & PRIVACY", fontSize = 11.sp, fontWeight = FontWeight.Bold, color = if (alert.isVideoAcceptedByReceiver) IndianGreen else Color(0xFFFCA5A5))
                                    Text(
                                        if (alert.isVideoAcceptedByReceiver) "Camera Streaming Live (Accepted)" else "Camera Off (Privacy Protected)",
                                        fontSize = 12.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = Color.White
                                    )
                                }
                            }
                        }

                        // Privacy explanation note
                        Surface(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(top = 8.dp),
                            color = if (alert.isVideoAcceptedByReceiver) Color(0xFF14532D).copy(alpha = 0.4f) else Color(0xFF450A0A).copy(alpha = 0.5f),
                            shape = RoundedCornerShape(8.dp),
                            border = androidx.compose.foundation.BorderStroke(
                                1.dp,
                                if (alert.isVideoAcceptedByReceiver) IndianGreen.copy(alpha = 0.5f) else EmergencyRed.copy(alpha = 0.4f)
                            )
                        ) {
                            Text(
                                text = if (alert.isVideoAcceptedByReceiver) {
                                    "✓ Receiver accepted the video call. Live 2-way video is active."
                                } else {
                                    "🔒 PRIVACY RULE ENFORCED: Your camera and screen remain completely OFF until an Elite or Group receiver explicitly accepts your incoming video call."
                                },
                                fontSize = 11.sp,
                                color = if (alert.isVideoAcceptedByReceiver) Color(0xFF86EFAC) else Color(0xFFFECACA),
                                modifier = Modifier.padding(8.dp)
                            )
                        }

                        // Simulation button to test receiver acceptance
                        if (!alert.isVideoAcceptedByReceiver) {
                            OutlinedButton(
                                onClick = onAcceptVideoByReceiver,
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(top = 8.dp)
                                    .testTag("simulate_accept_video_btn"),
                                colors = ButtonDefaults.outlinedButtonColors(contentColor = Color(0xFF38BDF8)),
                                border = androidx.compose.foundation.BorderStroke(1.dp, Color(0xFF38BDF8))
                            ) {
                                Icon(Icons.Default.Visibility, contentDescription = null, modifier = Modifier.size(16.dp))
                                Spacer(modifier = Modifier.width(6.dp))
                                Text("Simulate: Receiver Accepts Video Call", fontSize = 12.sp)
                            }
                        }
                    }
                }

                // Call Duration Counter
                Text(
                    text = String.format("Broadcast Elapsed: %02d:%02d", callSeconds / 60, callSeconds % 60),
                    fontSize = 13.sp,
                    color = Color(0xFF94A3B8),
                    fontWeight = FontWeight.Medium
                )

                // Big Cancel / End Emergency SOS Button
                Button(
                    onClick = onCancelEmergency,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(54.dp)
                        .testTag("end_sos_button"),
                    colors = ButtonDefaults.buttonColors(containerColor = EmergencyRed),
                    shape = RoundedCornerShape(16.dp)
                ) {
                    Icon(Icons.Default.CallEnd, contentDescription = null, tint = Color.White)
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = "CANCEL EMERGENCY SOS",
                        fontSize = 15.sp,
                        fontWeight = FontWeight.ExtraBold,
                        color = Color.White,
                        letterSpacing = 1.sp
                    )
                }
            }
        }
    }
}
