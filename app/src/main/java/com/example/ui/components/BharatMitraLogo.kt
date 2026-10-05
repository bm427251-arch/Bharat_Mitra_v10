package com.example.ui.components

import android.graphics.Paint as AndroidPaint
import android.graphics.Typeface
import androidx.compose.animation.core.*
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.geometry.RoundRect
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.*
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.drawscope.drawIntoCanvas
import androidx.compose.ui.graphics.drawscope.rotate
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage

import androidx.compose.ui.platform.LocalContext

// Official Palette matching the uploaded image exactly
// India Map Orange #FF8C00 top, Green #2E8B57 bottom, Handshake Orange with 3 blue dots, Text BHARAT Orange MITRA Green
val LogoNavyBackground = Color(0xFF0D1B68)
val LogoSaffronOrange = Color(0xFFFF8C00)
val LogoIndianGreen = Color(0xFF2E8B57)
val LogoAshokaBlue = Color(0xFF1E70DC)
val LogoStrokeBlue = Color(0xFF0D1B68)

/**
 * EXACT ASSET LOADER:
 * Image.asset('assets/logo.png', width: 120, height: 40, fit: BoxFit.contain)
 */
@Composable
fun BharatMitraHeaderLogo(
    modifier: Modifier = Modifier,
    width: Dp = 120.dp,
    height: Dp = 40.dp
) {
    val context = LocalContext.current
    val logoDrawableId = remember(context) {
        context.resources.getIdentifier("logo", "drawable", context.packageName)
    }

    if (logoDrawableId != 0) {
        androidx.compose.foundation.Image(
            painter = androidx.compose.ui.res.painterResource(id = logoDrawableId),
            contentDescription = "Bharat Mitra Official Logo",
            modifier = modifier.size(width = width, height = height),
            contentScale = ContentScale.Fit
        )
    } else {
        BharatMitraLogo(
            modifier = modifier,
            size = height * 1.5f,
            showText = true,
            animateGlow = false
        )
    }
}

/**
 * Official Bharat Mitra Logo:
 * India Map Orange #FF8C00 top, Green #2E8B57 bottom,
 * Handshake Orange with 3 blue dots, Text BHARAT Orange MITRA Green.
 */
@Composable
fun BharatMitraLogo(
    modifier: Modifier = Modifier,
    size: Dp = 260.dp,
    showText: Boolean = true,
    animateGlow: Boolean = true,
    customLogoUri: String? = null
) {
    val context = LocalContext.current
    val logoDrawableId = remember(context) {
        context.resources.getIdentifier("logo", "drawable", context.packageName)
    }

    if (logoDrawableId != 0 && customLogoUri == null) {
        Column(
            modifier = modifier,
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            androidx.compose.foundation.Image(
                painter = androidx.compose.ui.res.painterResource(id = logoDrawableId),
                contentDescription = "Bharat Mitra Official Logo",
                modifier = Modifier.size(size),
                contentScale = ContentScale.Fit
            )
        }
        return
    }

    if (!customLogoUri.isNullOrBlank()) {
        Column(
            modifier = modifier,
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            AsyncImage(
                model = customLogoUri,
                contentDescription = "Bharat Mitra Official Logo",
                modifier = Modifier
                    .size(size)
                    .clip(androidx.compose.foundation.shape.RoundedCornerShape(16.dp)),
                contentScale = ContentScale.Fit
            )
        }
        return
    }

    val infiniteTransition = rememberInfiniteTransition(label = "logo_anim")
    val pulseGlow by infiniteTransition.animateFloat(
        initialValue = 0.95f,
        targetValue = 1.05f,
        animationSpec = infiniteRepeatable(
            animation = tween(2200, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "pulse_glow"
    )

    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Canvas(
            modifier = Modifier.size(size)
        ) {
            val canvasW = this.size.width
            val canvasH = this.size.height

            // Scale factor based on standard 300x380 reference canvas
            val scaleX = canvasW / 300f
            val scaleY = canvasH / (if (showText) 380f else 300f)
            val s = minOf(scaleX, scaleY)

            val originX = (canvasW - 300f * s) / 2f
            val originY = (canvasH - (if (showText) 380f else 300f) * s) / 2f

            // Optional subtle ambient glow behind the map
            if (animateGlow) {
                drawCircle(
                    brush = Brush.radialGradient(
                        colors = listOf(
                            LogoSaffronOrange.copy(alpha = 0.15f * pulseGlow),
                            LogoIndianGreen.copy(alpha = 0.10f * pulseGlow),
                            Color.Transparent
                        ),
                        center = Offset(originX + 150f * s, originY + 140f * s),
                        radius = 160f * s
                    )
                )
            }

            // 1. Draw North & West India Map (Saffron Orange)
            drawNorthWestIndiaMap(originX, originY, s)

            // 2. Draw East & South India Map (Vibrant Green)
            drawEastSouthIndiaMap(originX, originY, s)

            // 3. Draw Center Handshake (Orange with Navy Separations & Outline)
            drawCenterHandshake(originX, originY, s)

            // 4. Draw Three Ashoka Blue Dots in Triangle Formation on Handshake
            drawThreeTriangleDots(originX, originY, s)

            // 5. Draw "BHARAT MITRA" text below with White outline stroke
            if (showText) {
                drawBharatMitraOutlinedText(originX, originY, s)
            }
        }
    }
}

/**
 * Draws the Northern and Western portion of India in solid Saffron Orange.
 */
private fun DrawScope.drawNorthWestIndiaMap(ox: Float, oy: Float, s: Float) {
    val path = Path().apply {
        // Northern crown (Jammu & Kashmir)
        moveTo(ox + 125f * s, oy + 42f * s)
        lineTo(ox + 130f * s, oy + 36f * s)
        lineTo(ox + 135f * s, oy + 38f * s)
        lineTo(ox + 143f * s, oy + 44f * s)
        lineTo(ox + 148f * s, oy + 52f * s)
        lineTo(ox + 156f * s, oy + 46f * s)
        lineTo(ox + 155f * s, oy + 58f * s)
        lineTo(ox + 150f * s, oy + 66f * s)
        lineTo(ox + 153f * s, oy + 76f * s)
        lineTo(ox + 148f * s, oy + 84f * s)
        lineTo(ox + 160f * s, oy + 92f * s)

        // Down to Nepal border
        lineTo(ox + 150f * s, oy + 104f * s)

        // Handshake partition line heading southwest
        lineTo(ox + 135f * s, oy + 115f * s)
        lineTo(ox + 124f * s, oy + 128f * s)
        lineTo(ox + 110f * s, oy + 142f * s)
        lineTo(ox + 106f * s, oy + 152f * s)

        // Maharashtra / Gujarat coastline down
        lineTo(ox + 104f * s, oy + 165f * s)
        lineTo(ox + 98f * s, oy + 160f * s)

        // Gujarat bulge / Kathiawar peninsula
        lineTo(ox + 88f * s, oy + 155f * s)
        lineTo(ox + 80f * s, oy + 148f * s)
        lineTo(ox + 84f * s, oy + 140f * s)
        lineTo(ox + 74f * s, oy + 138f * s)
        lineTo(ox + 76f * s, oy + 130f * s)
        lineTo(ox + 86f * s, oy + 128f * s)

        // Rajasthan / Pakistan western border heading up to Punjab/Kashmir
        lineTo(ox + 90f * s, oy + 114f * s)
        lineTo(ox + 96f * s, oy + 110f * s)
        lineTo(ox + 93f * s, oy + 98f * s)
        lineTo(ox + 100f * s, oy + 94f * s)
        lineTo(ox + 116f * s, oy + 90f * s)
        lineTo(ox + 120f * s, oy + 78f * s)
        lineTo(ox + 118f * s, oy + 68f * s)
        lineTo(ox + 122f * s, oy + 54f * s)
        close()
    }

    drawPath(path = path, color = LogoSaffronOrange)
}

/**
 * Draws the Eastern and Southern portion of India in solid Vibrant Green.
 */
private fun DrawScope.drawEastSouthIndiaMap(ox: Float, oy: Float, s: Float) {
    val path = Path().apply {
        // Starting from border with orange section in North-Central
        moveTo(ox + 150f * s, oy + 104f * s)

        // Northeast connector, Sikkim & Bhutan curves
        lineTo(ox + 164f * s, oy + 110f * s)
        lineTo(ox + 180f * s, oy + 116f * s)
        lineTo(ox + 196f * s, oy + 114f * s)
        lineTo(ox + 204f * s, oy + 106f * s)

        // Arunachal Pradesh horn & Northeast states
        lineTo(ox + 218f * s, oy + 95f * s)
        lineTo(ox + 226f * s, oy + 98f * s)
        lineTo(ox + 242f * s, oy + 100f * s)
        lineTo(ox + 252f * s, oy + 112f * s)
        lineTo(ox + 242f * s, oy + 120f * s)
        lineTo(ox + 236f * s, oy + 130f * s)
        lineTo(ox + 238f * s, oy + 144f * s)
        lineTo(ox + 232f * s, oy + 150f * s)
        lineTo(ox + 222f * s, oy + 140f * s)
        lineTo(ox + 214f * s, oy + 145f * s)
        lineTo(ox + 208f * s, oy + 134f * s)

        // Bengal & Odisha Bay of Bengal coastline
        lineTo(ox + 204f * s, oy + 146f * s)
        lineTo(ox + 205f * s, oy + 160f * s)
        lineTo(ox + 198f * s, oy + 172f * s)
        lineTo(ox + 186f * s, oy + 190f * s)
        lineTo(ox + 170f * s, oy + 210f * s)

        // Andhra Pradesh & Tamil Nadu down to Kanyakumari (Southern Cape)
        lineTo(ox + 154f * s, oy + 230f * s)
        lineTo(ox + 148f * s, oy + 245f * s)
        lineTo(ox + 140f * s, oy + 256f * s)
        lineTo(ox + 132f * s, oy + 266f * s) // Kanyakumari Tip

        // Kerala & Western Ghats Arabian Sea coastline heading up
        lineTo(ox + 126f * s, oy + 256f * s)
        lineTo(ox + 120f * s, oy + 240f * s)
        lineTo(ox + 115f * s, oy + 215f * s)
        lineTo(ox + 110f * s, oy + 185f * s)
        lineTo(ox + 104f * s, oy + 165f * s)

        // Internal dividing contour matching handshake boundary back to North-Central
        lineTo(ox + 106f * s, oy + 152f * s)
        lineTo(ox + 110f * s, oy + 142f * s)
        lineTo(ox + 124f * s, oy + 128f * s)
        lineTo(ox + 135f * s, oy + 115f * s)
        close()
    }

    drawPath(path = path, color = LogoIndianGreen)
}

/**
 * Draws the central Handshake in Saffron Orange with distinct dark navy blue
 * separation lines and interlocking grip matching the reference image.
 */
private fun DrawScope.drawCenterHandshake(ox: Float, oy: Float, s: Float) {
    // Left Hand (wrist coming from top-left)
    val leftArmPath = Path().apply {
        moveTo(ox + 110f * s, oy + 135f * s)
        lineTo(ox + 126f * s, oy + 124f * s)
        lineTo(ox + 142f * s, oy + 130f * s)
        lineTo(ox + 134f * s, oy + 148f * s)
        lineTo(ox + 116f * s, oy + 150f * s)
        close()
    }
    drawPath(path = leftArmPath, color = LogoSaffronOrange)
    drawPath(path = leftArmPath, color = LogoStrokeBlue, style = Stroke(width = 2.5f * s))

    // Right Hand (Palm & Wrist extending right/down)
    val rightHandBody = Path().apply {
        moveTo(ox + 132f * s, oy + 132f * s)
        lineTo(ox + 150f * s, oy + 130f * s)
        lineTo(ox + 166f * s, oy + 144f * s)
        lineTo(ox + 184f * s, oy + 165f * s)
        lineTo(ox + 172f * s, oy + 175f * s)
        lineTo(ox + 144f * s, oy + 158f * s)
        lineTo(ox + 130f * s, oy + 146f * s)
        close()
    }
    drawPath(path = rightHandBody, color = LogoSaffronOrange)
    drawPath(path = rightHandBody, color = LogoStrokeBlue, style = Stroke(width = 2.5f * s))

    // Four fingers extending down-right on right hand with gaps
    val fingerOffsets = listOf(
        Pair(Offset(150f * s, 148f * s), Offset(182f * s, 168f * s)),
        Pair(Offset(144f * s, 154f * s), Offset(176f * s, 175f * s)),
        Pair(Offset(138f * s, 160f * s), Offset(168f * s, 181f * s)),
        Pair(Offset(132f * s, 166f * s), Offset(160f * s, 187f * s))
    )

    fingerOffsets.forEach { (start, end) ->
        val fingerPath = Path().apply {
            moveTo(ox + start.x, oy + start.y)
            lineTo(ox + end.x, oy + end.y)
        }
        drawPath(
            path = fingerPath,
            color = LogoStrokeBlue,
            style = Stroke(width = 2.2f * s, cap = StrokeCap.Round)
        )
    }

    // Four curled fingers of the bottom clasping hand (4 rounded orange bulbs with blue outline)
    val claspLoops = listOf(
        Offset(112f * s, 155f * s),
        Offset(120f * s, 162f * s),
        Offset(128f * s, 170f * s),
        Offset(138f * s, 177f * s)
    )

    claspLoops.forEach { pos ->
        drawCircle(
            color = LogoSaffronOrange,
            radius = 6f * s,
            center = Offset(ox + pos.x, oy + pos.y)
        )
        drawCircle(
            color = LogoStrokeBlue,
            radius = 6f * s,
            center = Offset(ox + pos.x, oy + pos.y),
            style = Stroke(width = 2.2f * s)
        )
    }
}

/**
 * Draws the 3 Ashoka Blue dots arranged in a triangle formation on the palm/wrist.
 * (1 dot on top, 2 dots below).
 */
private fun DrawScope.drawThreeTriangleDots(ox: Float, oy: Float, s: Float) {
    val dotRadius = 4.2f * s
    // Center point on the right hand palm
    val px = ox + 152f * s
    val py = oy + 152f * s

    // Top dot
    drawCircle(
        color = LogoAshokaBlue,
        radius = dotRadius,
        center = Offset(px, py - 6f * s)
    )

    // Bottom-Left dot
    drawCircle(
        color = LogoAshokaBlue,
        radius = dotRadius,
        center = Offset(px - 5.5f * s, py + 4f * s)
    )

    // Bottom-Right dot
    drawCircle(
        color = LogoAshokaBlue,
        radius = dotRadius,
        center = Offset(px + 5.5f * s, py + 4f * s)
    )
}

/**
 * Draws "BHARAT MITRA" text:
 * - "BHARAT" in Saffron Orange with White outline stroke.
 * - "MITRA" in Vibrant Green with White outline stroke.
 */
private fun DrawScope.drawBharatMitraOutlinedText(ox: Float, oy: Float, s: Float) {
    drawIntoCanvas { canvas ->
        val textY = oy + 320f * s
        val textSize = 32f * s

        val strokePaint = AndroidPaint().apply {
            isAntiAlias = true
            this.textSize = textSize
            typeface = Typeface.create(Typeface.DEFAULT, Typeface.BOLD)
            style = AndroidPaint.Style.STROKE
            strokeWidth = 5.5f * s
            strokeJoin = AndroidPaint.Join.ROUND
            strokeCap = AndroidPaint.Cap.ROUND
            color = android.graphics.Color.WHITE
        }

        val fillPaintOrange = AndroidPaint().apply {
            isAntiAlias = true
            this.textSize = textSize
            typeface = Typeface.create(Typeface.DEFAULT, Typeface.BOLD)
            style = AndroidPaint.Style.FILL
            color = android.graphics.Color.rgb(0xFA, 0x85, 0x20) // LogoSaffronOrange
        }

        val fillPaintGreen = AndroidPaint().apply {
            isAntiAlias = true
            this.textSize = textSize
            typeface = Typeface.create(Typeface.DEFAULT, Typeface.BOLD)
            style = AndroidPaint.Style.FILL
            color = android.graphics.Color.rgb(0x0F, 0xA3, 0x38) // LogoIndianGreen
        }

        val bharatText = "BHARAT"
        val mitraText = "MITRA"
        val spaceWidth = 14f * s

        val bharatWidth = fillPaintOrange.measureText(bharatText)
        val mitraWidth = fillPaintGreen.measureText(mitraText)
        val totalWidth = bharatWidth + spaceWidth + mitraWidth

        // Centered start X
        val startX = ox + 150f * s - (totalWidth / 2f)
        val mitraStartX = startX + bharatWidth + spaceWidth

        val nativeCanvas = canvas.nativeCanvas

        // 1. Draw White Outlines
        nativeCanvas.drawText(bharatText, startX, textY, strokePaint)
        nativeCanvas.drawText(mitraText, mitraStartX, textY, strokePaint)

        // 2. Draw Solid Fills
        nativeCanvas.drawText(bharatText, startX, textY, fillPaintOrange)
        nativeCanvas.drawText(mitraText, mitraStartX, textY, fillPaintGreen)
    }
}
