package com.example.ui.components

import android.graphics.Paint as AndroidPaint
import android.graphics.Typeface
import androidx.compose.animation.core.*
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectTransformGestures
import androidx.compose.foundation.layout.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.*
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.drawscope.drawIntoCanvas
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.unit.dp
import com.example.ui.theme.BharatDarkBlue
import com.example.ui.theme.BharatGreen
import com.example.ui.theme.BharatOrange
import kotlin.math.cos
import kotlin.math.sin

data class VehicleMapPin(
    val id: String,
    val xRatio: Float,
    val yRatio: Float,
    val isOrange: Boolean, // Orange = Fast, Green = EV / Group
    val label: String
)

@Composable
fun InteractiveMapCanvas(
    modifier: Modifier = Modifier,
    pickupLocation: String = "Central Square, Stand No. 4",
    dropLocation: String = "Airport Terminal 2",
    originName: String = pickupLocation,
    destinationName: String = dropLocation,
    isDropConfirmed: Boolean = true,
    isDeviated: Boolean = false
) {
    var scale by remember { mutableFloatStateOf(1.0f) }
    var offsetX by remember { mutableFloatStateOf(0f) }
    var offsetY by remember { mutableFloatStateOf(0f) }

    // Continuous animation for moving vehicle along route
    val transition = rememberInfiniteTransition(label = "route_car_animation")
    val carProgress by transition.animateFloat(
        initialValue = 0f,
        targetValue = 1f,
        animationSpec = infiniteRepeatable(
            animation = tween(durationMillis = 8000, easing = LinearEasing),
            repeatMode = RepeatMode.Restart
        ),
        label = "car_progress"
    )

    // Pulse radar animation
    val pulseRadius by transition.animateFloat(
        initialValue = 12f,
        targetValue = 42f,
        animationSpec = infiniteRepeatable(
            animation = tween(durationMillis = 1800, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Restart
        ),
        label = "pulse_radius"
    )

    // 12 nearby vehicle pins (Orange & Green pins)
    val vehiclePins = remember {
        listOf(
            VehicleMapPin("v1", 0.35f, 0.45f, true, "Sedan 3m"),
            VehicleMapPin("v2", 0.42f, 0.38f, false, "EV 2m"),
            VehicleMapPin("v3", 0.28f, 0.52f, true, "SUV 5m"),
            VehicleMapPin("v4", 0.60f, 0.42f, false, "EV 3m"),
            VehicleMapPin("v5", 0.52f, 0.58f, true, "Sedan 4m"),
            VehicleMapPin("v6", 0.68f, 0.32f, false, "EV 2m"),
            VehicleMapPin("v7", 0.22f, 0.35f, true, "SUV 6m"),
            VehicleMapPin("v8", 0.75f, 0.55f, true, "Sedan 3m"),
            VehicleMapPin("v9", 0.38f, 0.65f, false, "Group 4m"),
            VehicleMapPin("v10", 0.48f, 0.25f, true, "Sedan 5m"),
            VehicleMapPin("v11", 0.62f, 0.68f, true, "SUV 5m"),
            VehicleMapPin("v12", 0.30f, 0.22f, false, "EV 3m")
        )
    }

    Box(
        modifier = modifier
            .background(Color(0xFFE9EEF5))
            .pointerInput(Unit) {
                detectTransformGestures { _, pan, zoom, _ ->
                    scale = (scale * zoom).coerceIn(0.7f, 2.5f)
                    offsetX = (offsetX + pan.x).coerceIn(-400f, 400f)
                    offsetY = (offsetY + pan.y).coerceIn(-400f, 400f)
                }
            }
    ) {
        Canvas(modifier = Modifier.fillMaxSize()) {
            val width = size.width
            val height = size.height
            val centerX = width * 0.5f + offsetX
            val centerY = height * 0.5f + offsetY

            // 1. Draw street grid background
            val roadPaint = Color.White
            val roadWidth = 14f * scale

            for (i in 0..6) {
                val y = height * (i / 6f)
                drawLine(roadPaint, Offset(0f, y), Offset(width, y), strokeWidth = roadWidth)
            }
            for (i in 0..6) {
                val x = width * (i / 6f)
                drawLine(roadPaint, Offset(x, 0f), Offset(x, height), strokeWidth = roadWidth)
            }

            // Diagonal avenues
            drawLine(Color(0xFFD6DFEC), Offset(0f, height * 0.2f), Offset(width, height * 0.8f), strokeWidth = 20f * scale)
            drawLine(Color(0xFFD6DFEC), Offset(0f, height * 0.8f), Offset(width, height * 0.3f), strokeWidth = 20f * scale)

            // 2. Green Route Line (Pickup to Destination)
            val startP = Offset(centerX - 100f * scale, centerY + 80f * scale)
            val midP = Offset(centerX + 20f * scale, centerY - 20f * scale)
            val endP = Offset(centerX + 120f * scale, centerY - 100f * scale)

            val routePath = Path().apply {
                moveTo(startP.x, startP.y)
                lineTo(midP.x, midP.y)
                lineTo(endP.x, endP.y)
            }

            // 2. Blue Route Line (polylines: blueRouteLine)
            val routeColor = if (isDeviated) Color(0xFFFF6B00) else Color(0xFF1E70DC)
            drawPath(
                path = routePath,
                color = routeColor.copy(alpha = 0.25f),
                style = Stroke(width = 16f * scale, cap = StrokeCap.Round, join = StrokeJoin.Round)
            )
            // Draw route line
            drawPath(
                path = routePath,
                color = routeColor,
                style = Stroke(width = 8f * scale, cap = StrokeCap.Round, join = StrokeJoin.Round)
            )

            // 3. Animated Moving Car along route
            val currentCarPos = if (carProgress < 0.5f) {
                val t = carProgress * 2f
                Offset(startP.x + (midP.x - startP.x) * t, startP.y + (midP.y - startP.y) * t)
            } else {
                val t = (carProgress - 0.5f) * 2f
                Offset(midP.x + (endP.x - midP.x) * t, midP.y + (endP.y - midP.y) * t)
            }

            // Moving Car Marker (Dark Blue circle with white border & heading)
            drawCircle(
                color = BharatDarkBlue,
                radius = 12f * scale,
                center = currentCarPos
            )
            drawCircle(
                color = Color.White,
                radius = 6f * scale,
                center = currentCarPos
            )

            // 4. Current Location Auto-detected (Start Point with Pulsing Radar & Blue Dot)
            drawCircle(
                color = Color(0xFF1E70DC).copy(alpha = 0.30f),
                radius = pulseRadius * scale,
                center = startP
            )
            // Marker for current location: Blue dot
            drawCircle(
                color = Color.White,
                radius = 12f * scale,
                center = startP
            )
            drawCircle(
                color = Color(0xFF1E70DC),
                radius = 9f * scale,
                center = startP
            )
            drawCircle(
                color = Color.White,
                radius = 3.5f * scale,
                center = startP
            )

            // 5. Destination Marker (Red Pin)
            drawCircle(
                color = Color(0xFFDC2626),
                radius = 10f * scale,
                center = endP
            )
            drawCircle(
                color = Color.White,
                radius = 4f * scale,
                center = endP
            )

            // 6. Draw 12 Nearby Vehicle Pins (Orange & Green)
            vehiclePins.forEach { pin ->
                val pinX = (width * pin.xRatio - width * 0.5f) * scale + centerX
                val pinY = (height * pin.yRatio - height * 0.5f) * scale + centerY
                val pinColor = if (pin.isOrange) BharatOrange else BharatGreen

                // Subtle shadow
                drawCircle(
                    color = Color.Black.copy(alpha = 0.15f),
                    radius = 9f * scale,
                    center = Offset(pinX + 1f, pinY + 2f)
                )

                // Vehicle dot
                drawCircle(
                    color = pinColor,
                    radius = 8f * scale,
                    center = Offset(pinX, pinY)
                )

                // White center pip
                drawCircle(
                    color = Color.White,
                    radius = 3.5f * scale,
                    center = Offset(pinX, pinY)
                )
            }

            // 7. Kolkata Street and Landmark Labels (Park St, AJC Bose Rd, Exide Crossing, Kolkata)
            drawIntoCanvas { canvas ->
                val labelPaint = AndroidPaint().apply {
                    color = android.graphics.Color.DKGRAY
                    textSize = 24f * scale
                    typeface = Typeface.DEFAULT_BOLD
                    isAntiAlias = true
                }
                val streetPaint = AndroidPaint().apply {
                    color = android.graphics.Color.GRAY
                    textSize = 20f * scale
                    typeface = Typeface.DEFAULT_BOLD
                    isAntiAlias = true
                }
                canvas.nativeCanvas.drawText("Park St", centerX - 60f * scale, centerY - 55f * scale, labelPaint)
                canvas.nativeCanvas.drawText("AJC Bose Rd", centerX + 30f * scale, centerY + 40f * scale, streetPaint)
                canvas.nativeCanvas.drawText("Exide Crossing", centerX + 70f * scale, centerY - 10f * scale, labelPaint)

                val cityPaint = AndroidPaint().apply {
                    color = android.graphics.Color.argb(160, 13, 27, 104)
                    textSize = 28f * scale
                    typeface = Typeface.create(Typeface.DEFAULT, Typeface.BOLD)
                    isAntiAlias = true
                }
                canvas.nativeCanvas.drawText("Kolkata", centerX - 140f * scale, centerY + 110f * scale, cityPaint)
            }
        }
    }
}
