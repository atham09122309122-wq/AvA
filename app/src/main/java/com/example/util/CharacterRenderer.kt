package com.example.util

import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Color as AndroidColor
import android.graphics.Paint
import android.graphics.Path
import android.graphics.RectF
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.drawscope.Fill
import androidx.compose.ui.graphics.drawscope.Stroke
import com.example.data.model.CharacterEntity

object CharacterRenderer {

    // Palettes
    val SkinTones = listOf(
        "#FCE3D2", // Porcelain
        "#EAC096", // Fair Warm
        "#D09B67", // Tan
        "#A86C3D", // Caramel
        "#754523", // Deep Bronze
        "#442411"  // Espresso
    )

    val HairColors = listOf(
        "#111116", // Midnight Black
        "#4A3728", // Deep Chestnut
        "#8B5A2B", // Golden Brown
        "#E5C158", // Blonde
        "#8B5CF6", // AVA Purple
        "#D1D5DB"  // Platinum Silver
    )

    val BackdropGradients = mapOf(
        "studio_dark" to listOf(Color(0xFF13111C), Color(0xFF09080E)),
        "street_urban" to listOf(Color(0xFF2E1C4E), Color(0xFF120E1E)),
        "basketball_neon" to listOf(Color(0xFF3F192E), Color(0xFF0F0E14)),
        "cyber_purple" to listOf(Color(0xFF4C1D95), Color(0xFF1E1035)),
        "minimal_monochrome" to listOf(Color(0xFF262626), Color(0xFF0F0F0F)),
        "transparent" to listOf(Color.Transparent, Color.Transparent)
    )

    @Composable
    fun CharacterCanvas(
        character: CharacterEntity,
        modifier: Modifier = Modifier,
        showBackdrop: Boolean = true
    ) {
        val skinColor = Color(android.graphics.Color.parseColor(character.skinToneHex))
        val hairColor = Color(android.graphics.Color.parseColor(character.hairColorHex))
        val eyeColor = Color(android.graphics.Color.parseColor(character.eyeColorHex))
        val topColor = Color(android.graphics.Color.parseColor(character.topColorHex))
        val bottomColor = Color(android.graphics.Color.parseColor(character.bottomColorHex))

        Canvas(modifier = modifier.fillMaxSize()) {
            val w = size.width
            val h = size.height

            // 1. Backdrop
            if (showBackdrop && character.backdrop != "transparent") {
                val colors = BackdropGradients[character.backdrop] ?: listOf(Color(0xFF13111C), Color(0xFF09080E))
                drawRect(
                    brush = androidx.compose.ui.graphics.Brush.verticalGradient(colors)
                )
            }

            // 2. Ground Shadow
            drawOval(
                color = Color(0x66000000),
                topLeft = Offset(w * 0.25f, h * 0.88f),
                size = Size(w * 0.5f, h * 0.05f)
            )

            // Scaled measurements centered on canvas
            val cx = w * 0.5f
            val isFemale = character.gender == "female"
            val bodyWidthFactor = if (isFemale) 0.85f else 1.0f

            // 3. Legs
            val legWidth = w * 0.09f * bodyWidthFactor
            val legTop = h * 0.52f
            val legBottom = h * 0.78f

            // Left & Right Leg Skin
            drawRoundRect(
                color = skinColor,
                topLeft = Offset(cx - w * 0.12f, legTop),
                size = Size(legWidth, legBottom - legTop),
                cornerRadius = androidx.compose.ui.geometry.CornerRadius(10f, 10f)
            )
            drawRoundRect(
                color = skinColor,
                topLeft = Offset(cx + w * 0.12f - legWidth, legTop),
                size = Size(legWidth, legBottom - legTop),
                cornerRadius = androidx.compose.ui.geometry.CornerRadius(10f, 10f)
            )

            // 4. Pants / Bottoms
            val pantsHeight = if (character.bottomWear == "basketball_shorts") h * 0.18f else h * 0.32f
            drawRoundRect(
                color = bottomColor,
                topLeft = Offset(cx - w * 0.14f * bodyWidthFactor, legTop),
                size = Size(w * 0.28f * bodyWidthFactor, pantsHeight),
                cornerRadius = androidx.compose.ui.geometry.CornerRadius(16f, 16f)
            )
            // Seam detail
            drawLine(
                color = Color(0x44000000),
                start = Offset(cx, legTop + pantsHeight * 0.3f),
                end = Offset(cx, legTop + pantsHeight),
                strokeWidth = 4f
            )

            // 5. Shoes / Sneakers
            val shoeY = h * 0.80f
            val shoeColor = when (character.shoes) {
                "high_top_kicks" -> Color(0xFFF3F4F6)
                "bball_shoes" -> Color(0xFF8B5CF6)
                "boots" -> Color(0xFF1F1F24)
                else -> Color(0xFFE5E7EB)
            }
            // Left shoe
            drawRoundRect(
                color = shoeColor,
                topLeft = Offset(cx - w * 0.14f, shoeY),
                size = Size(w * 0.11f, h * 0.08f),
                cornerRadius = androidx.compose.ui.geometry.CornerRadius(12f, 12f)
            )
            drawRect(
                color = Color(0xFF09080E),
                topLeft = Offset(cx - w * 0.14f, shoeY + h * 0.06f),
                size = Size(w * 0.11f, h * 0.02f)
            )
            // Right shoe
            drawRoundRect(
                color = shoeColor,
                topLeft = Offset(cx + w * 0.03f, shoeY),
                size = Size(w * 0.11f, h * 0.08f),
                cornerRadius = androidx.compose.ui.geometry.CornerRadius(12f, 12f)
            )
            drawRect(
                color = Color(0xFF09080E),
                topLeft = Offset(cx + w * 0.03f, shoeY + h * 0.06f),
                size = Size(w * 0.11f, h * 0.02f)
            )

            // 6. Torso & Arms
            val torsoTop = h * 0.28f
            val torsoHeight = h * 0.28f
            val torsoWidth = w * 0.32f * bodyWidthFactor

            // Arms
            val armWidth = w * 0.08f
            val armHeight = h * 0.24f
            // Left Arm
            drawRoundRect(
                color = topColor,
                topLeft = Offset(cx - torsoWidth * 0.5f - armWidth * 0.8f, torsoTop + 10f),
                size = Size(armWidth, armHeight),
                cornerRadius = androidx.compose.ui.geometry.CornerRadius(16f, 16f)
            )
            // Left Hand
            drawCircle(
                color = skinColor,
                radius = armWidth * 0.5f,
                center = Offset(cx - torsoWidth * 0.5f - armWidth * 0.3f, torsoTop + armHeight + 15f)
            )
            // Right Arm
            drawRoundRect(
                color = topColor,
                topLeft = Offset(cx + torsoWidth * 0.5f - armWidth * 0.2f, torsoTop + 10f),
                size = Size(armWidth, armHeight),
                cornerRadius = androidx.compose.ui.geometry.CornerRadius(16f, 16f)
            )
            // Right Hand (Wristwatch if accessory selected)
            drawCircle(
                color = skinColor,
                radius = armWidth * 0.5f,
                center = Offset(cx + torsoWidth * 0.5f + armWidth * 0.3f, torsoTop + armHeight + 15f)
            )
            if (character.accessory == "watch") {
                drawRect(
                    color = Color(0xFFF59E0B),
                    topLeft = Offset(cx + torsoWidth * 0.5f + armWidth * 0.05f, torsoTop + armHeight),
                    size = Size(armWidth * 0.6f, 12f)
                )
            }

            // Torso Clothing
            drawRoundRect(
                color = topColor,
                topLeft = Offset(cx - torsoWidth * 0.5f, torsoTop),
                size = Size(torsoWidth, torsoHeight),
                cornerRadius = androidx.compose.ui.geometry.CornerRadius(20f, 20f)
            )

            // Torso graphics/accents based on clothing style
            if (character.topWear == "streetwear_hoodie") {
                // Draw hoodie pocket
                drawRoundRect(
                    color = Color(0x22FFFFFF),
                    topLeft = Offset(cx - torsoWidth * 0.35f, torsoTop + torsoHeight * 0.55f),
                    size = Size(torsoWidth * 0.7f, torsoHeight * 0.35f),
                    cornerRadius = androidx.compose.ui.geometry.CornerRadius(12f, 12f)
                )
                // Draw strings
                drawLine(
                    color = Color.White,
                    start = Offset(cx - 15f, torsoTop + 25f),
                    end = Offset(cx - 15f, torsoTop + 90f),
                    strokeWidth = 4f
                )
                drawLine(
                    color = Color.White,
                    start = Offset(cx + 15f, torsoTop + 25f),
                    end = Offset(cx + 15f, torsoTop + 90f),
                    strokeWidth = 4f
                )
            } else if (character.topWear == "jersey") {
                // Jersey number "23" accent
                drawRoundRect(
                    color = Color(0xFFF59E0B),
                    topLeft = Offset(cx - 24f, torsoTop + 50f),
                    size = Size(48f, 40f),
                    cornerRadius = androidx.compose.ui.geometry.CornerRadius(8f, 8f)
                )
            }

            // 7. Neck & Gold Chain
            val neckWidth = w * 0.08f
            val neckHeight = h * 0.06f
            val neckTop = torsoTop - neckHeight + 10f
            drawRect(
                color = skinColor,
                topLeft = Offset(cx - neckWidth * 0.5f, neckTop),
                size = Size(neckWidth, neckHeight)
            )

            if (character.accessory == "chain" || character.accessory == "gold_chain") {
                drawArc(
                    color = Color(0xFFF59E0B),
                    startAngle = 0f,
                    sweepAngle = 180f,
                    useCenter = false,
                    topLeft = Offset(cx - 30f, torsoTop - 5f),
                    size = Size(60f, 50f),
                    style = Stroke(width = 6f)
                )
            }

            // 8. Head & Face
            val headRadius = w * 0.13f
            val headCenter = Offset(cx, neckTop - headRadius * 0.65f)

            // Head shape
            drawCircle(
                color = skinColor,
                radius = headRadius,
                center = headCenter
            )

            // 9. Facial Features
            val eyeSpacing = headRadius * 0.45f
            val eyeY = headCenter.y - headRadius * 0.05f
            val eyeRadius = 9f

            // Left eye
            drawCircle(color = Color.White, radius = eyeRadius, center = Offset(headCenter.x - eyeSpacing, eyeY))
            drawCircle(color = eyeColor, radius = eyeRadius * 0.65f, center = Offset(headCenter.x - eyeSpacing, eyeY))
            drawCircle(color = Color.Black, radius = eyeRadius * 0.35f, center = Offset(headCenter.x - eyeSpacing, eyeY))

            // Right eye
            drawCircle(color = Color.White, radius = eyeRadius, center = Offset(headCenter.x + eyeSpacing, eyeY))
            drawCircle(color = eyeColor, radius = eyeRadius * 0.65f, center = Offset(headCenter.x + eyeSpacing, eyeY))
            drawCircle(color = Color.Black, radius = eyeRadius * 0.35f, center = Offset(headCenter.x + eyeSpacing, eyeY))

            // Eyebrows
            drawLine(
                color = hairColor,
                start = Offset(headCenter.x - eyeSpacing - 18f, eyeY - 18f),
                end = Offset(headCenter.x - eyeSpacing + 18f, eyeY - 16f),
                strokeWidth = 5f
            )
            drawLine(
                color = hairColor,
                start = Offset(headCenter.x + eyeSpacing - 18f, eyeY - 16f),
                end = Offset(headCenter.x + eyeSpacing + 18f, eyeY - 18f),
                strokeWidth = 5f
            )

            // Nose
            drawLine(
                color = Color(0x33000000),
                start = Offset(headCenter.x, eyeY + 5f),
                end = Offset(headCenter.x + 4f, eyeY + 22f),
                strokeWidth = 3f
            )

            // Mouth
            when (character.pose) {
                "confident", "happy" -> {
                    // Confident smile
                    drawArc(
                        color = Color(0xFF8B2535),
                        startAngle = 10f,
                        sweepAngle = 160f,
                        useCenter = false,
                        topLeft = Offset(headCenter.x - 18f, eyeY + 25f),
                        size = Size(36f, 22f),
                        style = Stroke(width = 4f)
                    )
                }
                else -> {
                    // Calm neutral
                    drawLine(
                        color = Color(0xFF6B1D2F),
                        start = Offset(headCenter.x - 14f, eyeY + 36f),
                        end = Offset(headCenter.x + 14f, eyeY + 36f),
                        strokeWidth = 4f
                    )
                }
            }

            // Sunglasses accessory
            if (character.accessory == "shades" || character.accessory == "glasses") {
                drawRoundRect(
                    color = Color(0xEE111827),
                    topLeft = Offset(headCenter.x - eyeSpacing - 22f, eyeY - 14f),
                    size = Size(eyeSpacing * 2f + 44f, 28f),
                    cornerRadius = androidx.compose.ui.geometry.CornerRadius(8f, 8f)
                )
                drawRect(
                    color = Color(0xFFF59E0B),
                    topLeft = Offset(headCenter.x - 10f, eyeY - 4f),
                    size = Size(20f, 6f)
                )
            }

            // 10. Hair / Headwear
            if (character.accessory == "beanie") {
                // Beanie cap
                drawArc(
                    color = Color(0xFF374151),
                    startAngle = 180f,
                    sweepAngle = 180f,
                    useCenter = true,
                    topLeft = Offset(headCenter.x - headRadius * 1.05f, headCenter.y - headRadius * 1.25f),
                    size = Size(headRadius * 2.1f, headRadius * 1.5f)
                )
            } else if (character.hairStyle == "textured_crop" || character.hairStyle == "fade") {
                // Modern textured crop
                drawArc(
                    color = hairColor,
                    startAngle = 180f,
                    sweepAngle = 180f,
                    useCenter = true,
                    topLeft = Offset(headCenter.x - headRadius * 1.05f, headCenter.y - headRadius * 1.15f),
                    size = Size(headRadius * 2.1f, headRadius * 1.3f)
                )
            } else if (character.hairStyle == "afro") {
                // Afro puff
                drawCircle(
                    color = hairColor,
                    radius = headRadius * 1.25f,
                    center = Offset(headCenter.x, headCenter.y - headRadius * 0.35f)
                )
            } else {
                // Long / wavy locks
                drawArc(
                    color = hairColor,
                    startAngle = 170f,
                    sweepAngle = 200f,
                    useCenter = true,
                    topLeft = Offset(headCenter.x - headRadius * 1.1f, headCenter.y - headRadius * 1.2f),
                    size = Size(headRadius * 2.2f, headRadius * 1.8f)
                )
            }
        }
    }

    /**
     * Renders character into a high-res bitmap for PNG export.
     */
    fun renderCharacterToBitmap(character: CharacterEntity, size: Int = 1080): Bitmap {
        val bitmap = Bitmap.createBitmap(size, size, Bitmap.Config.ARGB_8888)
        val canvas = Canvas(bitmap)

        if (character.backdrop != "transparent") {
            val bgPaint = Paint().apply {
                color = AndroidColor.parseColor("#13111C")
            }
            canvas.drawRect(0f, 0f, size.toFloat(), size.toFloat(), bgPaint)
        }

        val shadowPaint = Paint().apply {
            color = AndroidColor.argb(100, 0, 0, 0)
        }
        canvas.drawOval(
            RectF(size * 0.25f, size * 0.88f, size * 0.75f, size * 0.94f),
            shadowPaint
        )

        // Draw body elements
        val skinPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
            color = AndroidColor.parseColor(character.skinToneHex)
        }
        val hairPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
            color = AndroidColor.parseColor(character.hairColorHex)
        }
        val topPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
            color = AndroidColor.parseColor(character.topColorHex)
        }
        val bottomPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
            color = AndroidColor.parseColor(character.bottomColorHex)
        }

        val cx = size * 0.5f

        // Legs & Pants
        canvas.drawRoundRect(
            RectF(cx - size * 0.14f, size * 0.52f, cx + size * 0.14f, size * 0.82f),
            32f, 32f, bottomPaint
        )

        // Torso
        canvas.drawRoundRect(
            RectF(cx - size * 0.16f, size * 0.28f, cx + size * 0.16f, size * 0.56f),
            40f, 40f, topPaint
        )

        // Neck & Head
        canvas.drawRect(cx - size * 0.04f, size * 0.22f, cx + size * 0.04f, size * 0.30f, skinPaint)
        canvas.drawCircle(cx, size * 0.18f, size * 0.13f, skinPaint)

        // Hair
        canvas.drawArc(
            RectF(cx - size * 0.14f, size * 0.04f, cx + size * 0.14f, size * 0.24f),
            180f, 180f, true, hairPaint
        )

        return bitmap
    }
}
