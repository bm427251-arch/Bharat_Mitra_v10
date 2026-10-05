package com.example.ui.screens

import androidx.compose.runtime.Composable
import com.example.viewmodel.MainViewModel

/**
 * PAGE 6B: RideInProgressScreen (Replaces and upgrades old Live Tracking with
 * Full Screen Map, Overspeed Banner #FF6B00, Speed Card, Driver Card, SOS WhatsApp/112,
 * Voice Nav, Dynamic Share Link, Route Deviation, and Night Mode).
 */
@Composable
fun LiveTrackingScreen(
    viewModel: MainViewModel,
    onBack: () -> Unit,
    onFinishRideAndRate: () -> Unit
) {
    RideInProgressScreen(
        viewModel = viewModel,
        onBack = onBack,
        onFinishRideAndRate = onFinishRideAndRate
    )
}
