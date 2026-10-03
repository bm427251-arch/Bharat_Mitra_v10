package com.example.ui.screens

import androidx.compose.animation.*
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.awaitEachGesture
import androidx.compose.foundation.gestures.awaitFirstDown
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.filled.Security
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.model.ScreenState
import com.example.ui.components.BharatMitraLogo
import com.example.ui.theme.DarkBackground
import com.example.ui.theme.DarkSurface
import com.example.ui.theme.SaffronPrimary
import com.example.viewmodel.MainViewModel

/**
 * Splash Screen Requirement 1:
 * - The app must display a custom-coded logo (Indian map with Orange and Green colors,
 *   a handshake in the middle with 3 blue dots, and "BHARAT MITRA" text below).
 *   No static image files; rendered via code (Canvas).
 * - Hidden Admin Entry: Long-pressing the logo continuously for 7 seconds without any
 *   counter should directly navigate to the Admin Panel.
 */
@Composable
fun SplashScreen(
    viewModel: MainViewModel,
    onNavigateToHome: () -> Unit
) {
    val customLogoUri by viewModel.customLogoUri.collectAsState()

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                Brush.verticalGradient(
                    listOf(
                        Color(0xFF0D1B68),
                        Color(0xFF091244),
                        Color(0xFF050B28)
                    )
                )
            )
            .testTag("splash_screen_root"),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center,
            modifier = Modifier
                .fillMaxWidth()
                .padding(24.dp)
        ) {
            // Interactive Custom-Coded Logo with Hidden 7-second Long Press (NO VISIBLE COUNTER)
            Box(
                modifier = Modifier
                    .clip(RoundedCornerShape(32.dp))
                    .pointerInput(Unit) {
                        awaitEachGesture {
                            awaitFirstDown(requireUnconsumed = false)
                            // Start 7-second continuous press timer
                            viewModel.startAdminLongPress()

                            // Wait for pointer release or cancel
                            do {
                                val event = awaitPointerEvent()
                                val isPressed = event.changes.any { it.pressed }
                                if (!isPressed) {
                                    viewModel.cancelAdminLongPress()
                                }
                            } while (event.changes.any { it.pressed })
                            viewModel.cancelAdminLongPress()
                        }
                    }
                    .testTag("splash_interactive_logo_box"),
                contentAlignment = Alignment.Center
            ) {
                BharatMitraLogo(
                    size = 230.dp,
                    showText = true,
                    animateGlow = true,
                    customLogoUri = customLogoUri
                )
            }

            Spacer(modifier = Modifier.height(28.dp))

            Text(
                text = "Together We Protect, Serve & Travel",
                style = MaterialTheme.typography.bodyMedium.copy(
                    fontWeight = FontWeight.Medium,
                    color = Color(0xFF94A3B8),
                    letterSpacing = 0.5.sp
                ),
                textAlign = TextAlign.Center
            )

            Spacer(modifier = Modifier.height(48.dp))

            // Enter App Button
            Button(
                onClick = onNavigateToHome,
                modifier = Modifier
                    .fillMaxWidth(0.75f)
                    .height(52.dp)
                    .testTag("splash_enter_button"),
                colors = ButtonDefaults.buttonColors(containerColor = SaffronPrimary),
                shape = RoundedCornerShape(16.dp),
                elevation = ButtonDefaults.buttonElevation(defaultElevation = 6.dp)
            ) {
                Text(
                    text = "ENTER APPLICATION",
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Bold,
                    letterSpacing = 1.sp
                )
                Spacer(modifier = Modifier.width(8.dp))
                Icon(
                    Icons.AutoMirrored.Filled.ArrowForward,
                    contentDescription = null,
                    modifier = Modifier.size(18.dp)
                )
            }

            Spacer(modifier = Modifier.height(20.dp))

            // Discreet security notice
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center
            ) {
                Icon(
                    Icons.Default.Security,
                    contentDescription = null,
                    tint = Color(0xFF64748B),
                    modifier = Modifier.size(14.dp)
                )
                Spacer(modifier = Modifier.width(6.dp))
                Text(
                    text = "256-Bit Encrypted Community Network",
                    fontSize = 11.sp,
                    color = Color(0xFF64748B)
                )
            }
        }
    }
}
