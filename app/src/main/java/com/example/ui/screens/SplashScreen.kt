package com.example.ui.screens

import androidx.compose.animation.core.*
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.components.BharatMitraLogo
import com.example.ui.theme.BharatDarkBlue
import com.example.ui.theme.BharatOrange
import kotlinx.coroutines.delay

@Composable
fun SplashScreen(
    onSplashComplete: () -> Unit
) {
    val infiniteTransition = rememberInfiniteTransition(label = "splash_logo_anim")
    val scale by infiniteTransition.animateFloat(
        initialValue = 0.85f,
        targetValue = 1.15f,
        animationSpec = infiniteRepeatable(
            animation = tween(1000, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "scale_anim"
    )

    // Animated dots for "Getting you ready..."
    var dotsCount by remember { mutableStateOf(1) }
    LaunchedEffect(Unit) {
        repeat(5) {
            delay(400L)
            dotsCount = (dotsCount % 3) + 1
        }
        onSplashComplete()
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFF08080C))
            .testTag("splash_screen_view"),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center,
            modifier = Modifier.padding(24.dp)
        ) {
            // Logo center: 3D circular emblem with orange backlight glow
            Box(
                modifier = Modifier
                    .scale(scale)
                    .size(240.dp),
                contentAlignment = Alignment.Center
            ) {
                BharatMitraLogo(
                    size = 230.dp,
                    showText = false,
                    animateGlow = true
                )
            }

            Spacer(modifier = Modifier.height(28.dp))

            Text(
                text = "BHARAT MITRA",
                style = MaterialTheme.typography.headlineMedium.copy(
                    color = Color.White,
                    fontWeight = FontWeight.Black,
                    letterSpacing = 2.5.sp,
                    fontSize = 26.sp
                )
            )

            Spacer(modifier = Modifier.height(6.dp))

            Text(
                text = "Your Trusted Ride & Rental Partner",
                style = MaterialTheme.typography.bodyLarge.copy(
                    color = BharatOrange,
                    fontWeight = FontWeight.Bold,
                    fontSize = 15.sp
                )
            )

            Spacer(modifier = Modifier.height(36.dp))

            // Getting you ready... dots animation
            val dotsText = ".".repeat(dotsCount)
            Text(
                text = "Getting you ready$dotsText",
                style = MaterialTheme.typography.bodyMedium.copy(
                    color = Color.White.copy(alpha = 0.8f),
                    fontSize = 13.sp,
                    fontWeight = FontWeight.Medium
                )
            )
        }
    }
}
