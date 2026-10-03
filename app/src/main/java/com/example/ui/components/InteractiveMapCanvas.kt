package com.example.ui.components

import androidx.compose.animation.core.*
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.gestures.detectTransformGestures
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
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.*
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.*
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.model.DotType
import com.example.model.MapDot
import com.example.ui.theme.AshokaBlue
import com.example.ui.theme.IndianGreen
import com.example.ui.theme.SaffronPrimary
import kotlin.math.cos
import kotlin.math.sin

@OptIn(ExperimentalTextApi::class)
@Composable
fun InteractiveMapCanvas(
    modifier: Modifier = Modifier,
    userGpsTitle: String = "Barasat, WB",
    mapDots: List<MapDot> = emptyList(),
    selectedRadiusKm: Int = 3,
    onSelectLandmark: (String) -> Unit = {},
    onDotClick: (MapDot) -> Unit = {},
    showEliteDots: Boolean = true
) {
    // Interactive Pan & Zoom state
    var scale by remember { mutableFloatStateOf(1.0f) }
    var offsetX by remember { mutableFloatStateOf(0f) }
    var offsetY by remember { mutableFloatStateOf(0f) }

    // Continuous Animation for moving traffic and GPS radar pulse
    val infiniteTransition = rememberInfiniteTransition(label = "map_live_traffic")
    val vehicleProgress by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 1f,
        animationSpec = infiniteRepeatable(
            animation = tween(12000, easing = LinearEasing),
            repeatMode = RepeatMode.Restart
        ),
        label = "vehicle_progress"
    )
    val pulseProgress by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 1f,
        animationSpec = infiniteRepeatable(
            animation = tween(2200, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Restart
        ),
        label = "pulse_progress"
    )

    val textMeasurer = rememberTextMeasurer()

    Box(
        modifier = modifier
            .fillMaxSize()
            .clip(RoundedCornerShape(16.dp))
            .background(Color(0xFF0F172A))
            .pointerInput(Unit) {
                detectTransformGestures { _, pan, zoom, _ ->
                    scale = (scale * zoom).coerceIn(0.6f, 3.0f)
                    offsetX = (offsetX + pan.x).coerceIn(-400f * scale, 400f * scale)
                    offsetY = (offsetY + pan.y).coerceIn(-400f * scale, 400f * scale)
                }
            }
            .pointerInput(Unit) {
                detectTapGestures { tapOffset ->
                    // Check if clicked near landmarks or dots
                    // Default to recenter or tap trigger
                }
            }
            .testTag("interactive_map_canvas")
    ) {
        Canvas(modifier = Modifier.fillMaxSize()) {
            val width = size.width
            val height = size.height
            val centerX = width / 2f + offsetX
            val centerY = height / 2f + offsetY

            // 1. Draw Map Base (Modern Dark Cartography / Streets)
            drawRect(
                brush = Brush.radialGradient(
                    colors = listOf(Color(0xFF1E293B), Color(0xFF090D16)),
                    center = Offset(centerX, centerY),
                    radius = width * 1.2f
                )
            )

            // Grid lines (subtle town blocks)
            val gridSize = 60f * scale
            val gridColor = Color(0xFF334155).copy(alpha = 0.35f)
            var x = (centerX % gridSize) - gridSize
            while (x < width + gridSize) {
                drawLine(gridColor, Offset(x, 0f), Offset(x, height), strokeWidth = 1f)
                x += gridSize
            }
            var y = (centerY % gridSize) - gridSize
            while (y < height + gridSize) {
                drawLine(gridColor, Offset(0f, y), Offset(width, y), strokeWidth = 1f)
                y += gridSize
            }

            // 2. Draw Major Arteries & Highways
            // Jessore Road (NH12) - Diagonal artery
            val nh12Color = Color(0xFF475569)
            val nh12Glow = SaffronPrimary.copy(alpha = 0.15f)
            drawLine(
                color = nh12Glow,
                start = Offset(centerX - 350f * scale, centerY - 250f * scale),
                end = Offset(centerX + 350f * scale, centerY + 280f * scale),
                strokeWidth = 14f * scale,
                cap = StrokeCap.Round
            )
            drawLine(
                color = nh12Color,
                start = Offset(centerX - 350f * scale, centerY - 250f * scale),
                end = Offset(centerX + 350f * scale, centerY + 280f * scale),
                strokeWidth = 8f * scale,
                cap = StrokeCap.Round
            )

            // Barasat - Barrackpore Road
            drawLine(
                color = Color(0xFF334155),
                start = Offset(centerX - 300f * scale, centerY + 80f * scale),
                end = Offset(centerX + 260f * scale, centerY - 140f * scale),
                strokeWidth = 6f * scale,
                cap = StrokeCap.Round
            )

            // Taki Road Crossing
            drawLine(
                color = Color(0xFF334155),
                start = Offset(centerX, centerY - 280f * scale),
                end = Offset(centerX + 120f * scale, centerY + 280f * scale),
                strokeWidth = 5f * scale,
                cap = StrokeCap.Round
            )

            // 3. Draw Coverage Radius Ring (1km / 3km / 5km)
            val coverageRadius = (80f + selectedRadiusKm * 35f) * scale
            drawCircle(
                color = SaffronPrimary.copy(alpha = 0.08f),
                radius = coverageRadius,
                center = Offset(centerX, centerY)
            )
            drawCircle(
                color = SaffronPrimary.copy(alpha = 0.35f),
                radius = coverageRadius,
                center = Offset(centerX, centerY),
                style = Stroke(width = 1.5f * scale, pathEffect = PathEffect.dashPathEffect(floatArrayOf(12f, 8f)))
            )

            // 4. Moving Simulated Vehicles along Jessore Road / Roads
            drawMovingVehicles(centerX, centerY, scale, vehicleProgress)

            // 5. Landmarks Pins
            drawLandmark(
                centerX - 130f * scale, centerY - 80f * scale,
                "Barasat Court", scale, textMeasurer, Color(0xFF38BDF8)
            )
            drawLandmark(
                centerX + 90f * scale, centerY + 60f * scale,
                "Colony More", scale, textMeasurer, Color(0xFFFBBF24)
            )
            drawLandmark(
                centerX - 180f * scale, centerY + 180f * scale,
                "Madhyamgram", scale, textMeasurer, Color(0xFF34D399)
            )
            drawLandmark(
                centerX + 160f * scale, centerY - 160f * scale,
                "Railway Station", scale, textMeasurer, Color(0xFFA78BFA)
            )

            // 6. Draw Elite Map Dots (Orange = Nearby Elite, Green = User's Group)
            if (showEliteDots) {
                mapDots.forEach { dot ->
                    val dotX = centerX + dot.lngOffset * 220f * scale
                    val dotY = centerY + dot.latOffset * 220f * scale

                    val isGreen = dot.dotType == DotType.GREEN_USER_GROUP
                    val dotColor = if (isGreen) IndianGreen else SaffronPrimary
                    val haloColor = if (isGreen) Color(0xFF22C55E).copy(alpha = 0.25f) else SaffronPrimary.copy(alpha = 0.25f)

                    // Halo
                    drawCircle(haloColor, radius = 10f * scale, center = Offset(dotX, dotY))
                    // Dot
                    drawCircle(dotColor, radius = 5f * scale, center = Offset(dotX, dotY))
                    drawCircle(Color.White, radius = 2f * scale, center = Offset(dotX, dotY))

                    // Dot label
                    val labelResult = textMeasurer.measure(
                        text = AnnotatedString(if (isGreen) "★ ${dot.name.take(10)}" else dot.name.take(8)),
                        style = TextStyle(
                            fontSize = (9f * scale).coerceIn(8f, 13f).sp,
                            fontWeight = FontWeight.SemiBold,
                            color = Color(0xFFE2E8F0)
                        )
                    )
                    drawText(
                        textLayoutResult = labelResult,
                        topLeft = Offset(dotX - labelResult.size.width / 2f, dotY + 8f * scale)
                    )
                }
            }

            // 7. Center User Location GPS Marker (Pulsating Blue/Tricolor Pin)
            // Pulsing rings
            drawCircle(
                color = AshokaBlue.copy(alpha = (1f - pulseProgress) * 0.45f),
                radius = (16f + pulseProgress * 28f) * scale,
                center = Offset(centerX, centerY)
            )
            drawCircle(
                color = Color.White,
                radius = 8f * scale,
                center = Offset(centerX, centerY)
            )
            drawCircle(
                color = Color(0xFF2563EB),
                radius = 6f * scale,
                center = Offset(centerX, centerY)
            )
            // Center Dot
            drawCircle(
                color = SaffronPrimary,
                radius = 2.5f * scale,
                center = Offset(centerX, centerY)
            )

            // Current Location Tag
            val userTag = textMeasurer.measure(
                text = AnnotatedString("YOU ($userGpsTitle)"),
                style = TextStyle(
                    fontSize = (10f * scale).coerceIn(9f, 14f).sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.White
                )
            )
            drawRoundRect(
                color = Color(0xFF0F172A).copy(alpha = 0.85f),
                topLeft = Offset(centerX - userTag.size.width / 2f - 8f, centerY - 32f * scale - userTag.size.height),
                size = Size(userTag.size.width + 16f, userTag.size.height + 6f),
                cornerRadius = androidx.compose.ui.geometry.CornerRadius(6f, 6f)
            )
            drawText(
                textLayoutResult = userTag,
                topLeft = Offset(centerX - userTag.size.width / 2f, centerY - 29f * scale - userTag.size.height)
            )
        }

        // Overlay Controls: Recenter GPS, Zoom +/-
        Column(
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .padding(12.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            FloatingActionButton(
                onClick = {
                    scale = 1.0f
                    offsetX = 0f
                    offsetY = 0f
                },
                modifier = Modifier
                    .size(40.dp)
                    .testTag("map_recenter_button"),
                containerColor = MaterialTheme.colorScheme.surfaceVariant,
                contentColor = MaterialTheme.colorScheme.primary,
                shape = CircleShape
            ) {
                Icon(Icons.Default.MyLocation, contentDescription = "Recenter GPS", modifier = Modifier.size(20.dp))
            }

            FloatingActionButton(
                onClick = { scale = (scale * 1.25f).coerceAtMost(3.0f) },
                modifier = Modifier
                    .size(36.dp)
                    .testTag("map_zoom_in_button"),
                containerColor = MaterialTheme.colorScheme.surfaceVariant,
                contentColor = MaterialTheme.colorScheme.onSurfaceVariant,
                shape = CircleShape
            ) {
                Icon(Icons.Default.Add, contentDescription = "Zoom In", modifier = Modifier.size(18.dp))
            }

            FloatingActionButton(
                onClick = { scale = (scale / 1.25f).coerceAtLeast(0.6f) },
                modifier = Modifier
                    .size(36.dp)
                    .testTag("map_zoom_out_button"),
                containerColor = MaterialTheme.colorScheme.surfaceVariant,
                contentColor = MaterialTheme.colorScheme.onSurfaceVariant,
                shape = CircleShape
            ) {
                Icon(Icons.Default.Remove, contentDescription = "Zoom Out", modifier = Modifier.size(18.dp))
            }
        }

        // Map Legend Header
        Row(
            modifier = Modifier
                .align(Alignment.TopStart)
                .padding(10.dp)
                .clip(RoundedCornerShape(20.dp))
                .background(Color(0xFF0F172A).copy(alpha = 0.85f))
                .padding(horizontal = 10.dp, vertical = 5.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Box(Modifier.size(8.dp).clip(CircleShape).background(SaffronPrimary))
            Text("Elite (${selectedRadiusKm}km)", fontSize = 11.sp, color = Color.LightGray, fontWeight = FontWeight.Medium)

            Spacer(Modifier.width(2.dp))
            Box(Modifier.size(8.dp).clip(CircleShape).background(IndianGreen))
            Text("Group (Global)", fontSize = 11.sp, color = Color.LightGray, fontWeight = FontWeight.Medium)
        }
    }
}

/**
 * Draws animated simulated moving vehicles on routes.
 */
private fun DrawScope.drawMovingVehicles(cx: Float, cy: Float, s: Float, progress: Float) {
    // Vehicle 1: Bike on Jessore Road (moving northeast)
    val bikeT = (progress * 1.5f) % 1f
    val bikeX = (cx - 280f * s) + (560f * s) * bikeT
    val bikeY = (cy - 200f * s) + (420f * s) * bikeT
    drawCircle(SaffronPrimary, radius = 4f * s, center = Offset(bikeX, bikeY))

    // Vehicle 2: Toto on local feeder road
    val totoT = (progress + 0.3f) % 1f
    val totoX = (cx - 150f * s) + (300f * s) * totoT
    val totoY = (cy + 70f * s) + (40f * s) * sin(totoT * 6.28f)
    drawCircle(Color(0xFF38BDF8), radius = 5f * s, center = Offset(totoX, totoY))

    // Vehicle 3: Cab moving toward Airport
    val cabT = (progress + 0.7f) % 1f
    val cabX = (cx + 220f * s) - (440f * s) * cabT
    val cabY = (cy - 120f * s) + (260f * s) * cabT
    drawCircle(Color(0xFFF59E0B), radius = 5.5f * s, center = Offset(cabX, cabY))
}

@OptIn(ExperimentalTextApi::class)
private fun DrawScope.drawLandmark(
    x: Float,
    y: Float,
    label: String,
    scale: Float,
    textMeasurer: TextMeasurer,
    pinColor: Color
) {
    // Pin head
    drawCircle(pinColor, radius = 5f * scale, center = Offset(x, y))
    drawCircle(Color.White, radius = 2f * scale, center = Offset(x, y))

    // Label
    val result = textMeasurer.measure(
        text = AnnotatedString(label),
        style = TextStyle(
            fontSize = (10f * scale).coerceIn(9f, 13f).sp,
            fontWeight = FontWeight.Medium,
            color = Color(0xFFCBD5E1)
        )
    )
    drawText(
        textLayoutResult = result,
        topLeft = Offset(x - result.size.width / 2f, y + 6f * scale)
    )
}
