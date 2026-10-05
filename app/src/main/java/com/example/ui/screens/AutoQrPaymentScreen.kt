package com.example.ui.screens

import android.widget.Toast
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.theme.*
import com.example.viewmodel.MainViewModel
import kotlinx.coroutines.delay

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AutoQrPaymentScreen(
    viewModel: MainViewModel,
    onBack: () -> Unit,
    onPaymentSuccess: () -> Unit
) {
    BackHandler { onBack() }
    val context = LocalContext.current
    var remainingSeconds by remember { mutableStateOf(300) } // 05:00
    var qrSeed by remember { mutableStateOf(101) }
    var isProcessing by remember { mutableStateOf(false) }

    // Timer countdown 05:00
    LaunchedEffect(Unit) {
        while (remainingSeconds > 0) {
            delay(1000L)
            remainingSeconds -= 1
        }
    }

    val minutes = remainingSeconds / 60
    val seconds = remainingSeconds % 60
    val timerString = String.format("%02d:%02d mins remaining", minutes, seconds)

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Column {
                        Text("Auto QR Payment", fontWeight = FontWeight.Bold, fontSize = 18.sp, color = Color.White)
                        Text("Auto QR Generated Secure Payment", fontSize = 11.sp, color = BharatOrangeLight)
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
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .background(LightBackground)
                .verticalScroll(rememberScrollState())
                .padding(18.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // Amount Card
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White),
                elevation = CardDefaults.cardElevation(defaultElevation = 6.dp),
                border = BorderStroke(0.5.dp, CardBorderColor)
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column {
                        Text("Payable Amount", fontSize = 12.sp, color = Color.Gray, fontWeight = FontWeight.Medium)
                        Text("₹100", fontSize = 28.sp, fontWeight = FontWeight.Black, color = BharatDarkBlue)
                    }

                    // Live countdown timer badge: "05:00 mins remaining"
                    Surface(
                        color = BharatOrange.copy(alpha = 0.12f),
                        shape = RoundedCornerShape(8.dp),
                        border = BorderStroke(1.dp, BharatOrange)
                    ) {
                        Row(
                            modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Icon(Icons.Default.Timer, contentDescription = null, tint = BharatOrange, modifier = Modifier.size(16.dp))
                            Spacer(modifier = Modifier.width(4.dp))
                            Text(
                                text = timerString,
                                fontWeight = FontWeight.Bold,
                                fontSize = 12.sp,
                                color = BharatOrange
                            )
                        }
                    }
                }
            }

            // Center Big QR Code Card
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(20.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White),
                elevation = CardDefaults.cardElevation(defaultElevation = 8.dp),
                border = BorderStroke(0.5.dp, CardBorderColor)
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(20.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    Text(
                        text = "Scan with Any UPI App",
                        fontWeight = FontWeight.Bold,
                        fontSize = 15.sp,
                        color = BharatDarkBlue
                    )

                    // Big Custom-drawn QR Code Canvas
                    Box(
                        modifier = Modifier
                            .size(200.dp)
                            .clip(RoundedCornerShape(12.dp))
                            .background(Color.White)
                            .padding(10.dp)
                            .testTag("center_big_qr_code"),
                        contentAlignment = Alignment.Center
                    ) {
                        Canvas(modifier = Modifier.fillMaxSize()) {
                            val cellSize = size.width / 21f
                            // Draw corner position detection patterns (3 corners)
                            drawCornerFinder(0f, 0f, cellSize)
                            drawCornerFinder(cellSize * 14f, 0f, cellSize)
                            drawCornerFinder(0f, cellSize * 14f, cellSize)

                            // Pseudorandom realistic QR module matrix based on qrSeed
                            for (row in 0..20) {
                                for (col in 0..20) {
                                    val inFinder = (row < 7 && col < 7) || (row < 7 && col > 13) || (row > 13 && col < 7)
                                    if (!inFinder) {
                                        val isFilled = ((row * 7 + col * 13 + qrSeed) % 3 != 0)
                                        if (isFilled) {
                                            drawRect(
                                                color = Color(0xFF0D1B68),
                                                topLeft = Offset(col * cellSize, row * cellSize),
                                                size = Size(cellSize * 0.9f, cellSize * 0.9f)
                                            )
                                        }
                                    }
                                }
                            }
                        }
                    }

                    // Auto Generated Dynamic Tag
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.Center
                    ) {
                        Icon(Icons.Default.Autorenew, contentDescription = null, tint = BharatGreen, modifier = Modifier.size(16.dp))
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(
                            text = "Auto-generated UPI QR ID: BM${qrSeed}X99",
                            fontSize = 11.sp,
                            color = BharatGreen,
                            fontWeight = FontWeight.SemiBold
                        )
                    }

                    Text(
                        text = "Point camera to scan or click UPI apps below",
                        fontSize = 11.sp,
                        color = Color.Gray,
                        textAlign = TextAlign.Center
                    )
                }
            }

            // Pay via UPI Apps: GPay, PhonePe, Paytm
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White),
                elevation = CardDefaults.cardElevation(defaultElevation = 6.dp),
                border = BorderStroke(0.5.dp, CardBorderColor)
            ) {
                Column(
                    modifier = Modifier.padding(16.dp),
                    verticalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    Text(
                        text = "Pay via Installed UPI Apps",
                        fontWeight = FontWeight.Bold,
                        fontSize = 14.sp,
                        color = BharatDarkBlue
                    )

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        UpiAppItem(
                            modifier = Modifier.weight(1f),
                            name = "Google Pay",
                            color = Color(0xFF1A73E8),
                            onClick = {
                                Toast.makeText(context, "Opening Google Pay UPI intent...", Toast.LENGTH_SHORT).show()
                            }
                        )
                        UpiAppItem(
                            modifier = Modifier.weight(1f),
                            name = "PhonePe",
                            color = Color(0xFF5F259F),
                            onClick = {
                                Toast.makeText(context, "Opening PhonePe UPI intent...", Toast.LENGTH_SHORT).show()
                            }
                        )
                        UpiAppItem(
                            modifier = Modifier.weight(1f),
                            name = "Paytm",
                            color = Color(0xFF00B9F5),
                            onClick = {
                                Toast.makeText(context, "Opening Paytm UPI intent...", Toast.LENGTH_SHORT).show()
                            }
                        )
                    }
                }
            }

            // Wallet Balance: ₹450
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(14.dp),
                colors = CardDefaults.cardColors(containerColor = BharatGreen.copy(alpha = 0.08f)),
                border = BorderStroke(1.dp, BharatGreen.copy(alpha = 0.3f))
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(14.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(Icons.Default.AccountBalanceWallet, contentDescription = null, tint = BharatGreen)
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = "Bharat Mitra Wallet Balance",
                            fontSize = 13.sp,
                            fontWeight = FontWeight.Bold,
                            color = BharatDarkBlue
                        )
                    }
                    Text(
                        text = "₹450",
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Black,
                        color = BharatGreen
                    )
                }
            }

            // Pay Now ₹100 Button
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(50.dp)
                    .clip(RoundedCornerShape(12.dp))
                    .background(
                        brush = Brush.horizontalGradient(
                            listOf(BharatOrange, BharatGreen)
                        )
                    )
                    .clickable {
                        isProcessing = true
                        viewModel.processRazorpayPayment(100, "Fair Price Ride Payment") {
                            isProcessing = false
                            Toast.makeText(context, "Payment of ₹100 successful via UPI/Razorpay!", Toast.LENGTH_LONG).show()
                            onPaymentSuccess()
                        }
                    }
                    .testTag("pay_now_100_btn"),
                contentAlignment = Alignment.Center
            ) {
                if (isProcessing) {
                    CircularProgressIndicator(modifier = Modifier.size(24.dp), color = Color.White, strokeWidth = 2.dp)
                } else {
                    Text(
                        text = "Pay Now ₹100",
                        color = Color.White,
                        fontWeight = FontWeight.Bold,
                        fontSize = 16.sp
                    )
                }
            }

            // Secure Encrypted Transaction Note
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center,
                modifier = Modifier.padding(bottom = 16.dp)
            ) {
                Icon(Icons.Default.Lock, contentDescription = null, tint = Color.Gray, modifier = Modifier.size(14.dp))
                Spacer(modifier = Modifier.width(4.dp))
                Text(
                    text = "100% Secure Encrypted UPI & Razorpay Payment",
                    fontSize = 11.sp,
                    color = Color.Gray,
                    fontWeight = FontWeight.Medium
                )
            }
        }
    }
}

// Canvas helper to draw QR finder corners
private fun androidx.compose.ui.graphics.drawscope.DrawScope.drawCornerFinder(
    startX: Float,
    startY: Float,
    cellSize: Float
) {
    // Outer box (7x7)
    drawRect(
        color = Color(0xFF0D1B68),
        topLeft = Offset(startX, startY),
        size = Size(cellSize * 7f, cellSize * 7f)
    )
    // White inner ring (5x5)
    drawRect(
        color = Color.White,
        topLeft = Offset(startX + cellSize, startY + cellSize),
        size = Size(cellSize * 5f, cellSize * 5f)
    )
    // Inner black center (3x3)
    drawRect(
        color = Color(0xFF0D1B68),
        topLeft = Offset(startX + cellSize * 2f, startY + cellSize * 2f),
        size = Size(cellSize * 3f, cellSize * 3f)
    )
}

@Composable
private fun UpiAppItem(
    modifier: Modifier = Modifier,
    name: String,
    color: Color,
    onClick: () -> Unit
) {
    Surface(
        modifier = modifier.clickable { onClick() },
        color = LightSurfaceVariant,
        shape = RoundedCornerShape(12.dp),
        border = BorderStroke(0.5.dp, CardBorderColor)
    ) {
        Column(
            modifier = Modifier.padding(vertical = 12.dp, horizontal = 4.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Box(
                modifier = Modifier
                    .size(34.dp)
                    .clip(CircleShape)
                    .background(color),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = name.take(1),
                    color = Color.White,
                    fontWeight = FontWeight.Black,
                    fontSize = 14.sp
                )
            }
            Spacer(modifier = Modifier.height(6.dp))
            Text(name, fontSize = 11.sp, fontWeight = FontWeight.Bold, color = BharatDarkBlue, maxLines = 1)
        }
    }
}
