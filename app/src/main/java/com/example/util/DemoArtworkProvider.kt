package com.example.util

import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Color as AndroidColor
import android.graphics.LinearGradient
import android.graphics.Paint
import android.graphics.RadialGradient
import android.graphics.RectF
import android.graphics.Shader
import android.graphics.Typeface
import androidx.compose.ui.graphics.Color

data class DemoArtwork(
    val id: String,
    val category: String,
    val title: String,
    val subtitle: String,
    val aspectRatio: String = "1:1",
    val defaultFilter: FilterPreset = FilterPreset.NONE,
    val defaultAdjustments: ImageAdjustments = ImageAdjustments(),
    val defaultLighting: LightingStyle = LightingStyle.NONE,
    val defaultFrame: FrameStyle = FrameStyle.NONE,
    val initialHeadline: String = "AVA CREATION",
    val initialTagline: String = "Made by Ayham",
    val accentColorHex: Long = 0xFF8B5CF6
)

object DemoArtworkProvider {

    val demoArtworks = listOf(
        DemoArtwork(
            id = "demo_fashion_editorial",
            category = "Fashion Editorial",
            title = "PARIS NOIR — Autumn Collection",
            subtitle = "High-fashion monochrome studio lighting with subtle grain",
            aspectRatio = "4:5",
            defaultFilter = FilterPreset.FASHION_EDITORIAL,
            defaultAdjustments = ImageAdjustments(contrast = 1.25f, exposure = 4f, grain = 15f),
            defaultLighting = LightingStyle.STUDIO_FLASH,
            defaultFrame = FrameStyle.NONE,
            initialHeadline = "PARIS NOIR",
            initialTagline = "Haute Couture 2026",
            accentColorHex = 0xFFF3F4F6
        ),
        DemoArtwork(
            id = "demo_streetwear",
            category = "Streetwear",
            title = "TOKYO UNDERGROUND — Neon & Concrete",
            subtitle = "Raw urban aesthetic with cyberpunk purple and cyan tones",
            aspectRatio = "1:1",
            defaultFilter = FilterPreset.STREETWEAR,
            defaultAdjustments = ImageAdjustments(contrast = 1.3f, vibrance = 25f, clarity = 20f),
            defaultLighting = LightingStyle.NEON_BEAM,
            defaultFrame = FrameStyle.NEON_PURPLE,
            initialHeadline = "NEO TOKYO",
            initialTagline = "Underground Visuals",
            accentColorHex = 0xFFA855F7
        ),
        DemoArtwork(
            id = "demo_product_photo",
            category = "Product Photography",
            title = "CHRONO ELITE — Minimalist Watch",
            subtitle = "Clean studio product lighting, ultra-sharp detail and reflections",
            aspectRatio = "1:1",
            defaultFilter = FilterPreset.CLEAN,
            defaultAdjustments = ImageAdjustments(contrast = 1.15f, brightness = 6f, sharpness = 30f),
            defaultLighting = LightingStyle.DIRECTIONAL_SUN,
            defaultFrame = FrameStyle.MINIMAL_BLACK,
            initialHeadline = "CHRONO 01",
            initialTagline = "Precision Redefined",
            accentColorHex = 0xFFE2E8F0
        ),
        DemoArtwork(
            id = "demo_luxury_product",
            category = "Luxury Products",
            title = "AMETHYST PERFUME — Essence of AVA",
            subtitle = "Golden warmth and deep obsidian glass reflections",
            aspectRatio = "4:5",
            defaultFilter = FilterPreset.LUXURY_EDITORIAL,
            defaultAdjustments = ImageAdjustments(contrast = 1.2f, temperature = 15f, vignette = 25f),
            defaultLighting = LightingStyle.WARM_GLOW,
            defaultFrame = FrameStyle.LUXURY_GOLD,
            initialHeadline = "AMETHYST",
            initialTagline = "Eau De Parfum",
            accentColorHex = 0xFFEAB308
        ),
        DemoArtwork(
            id = "demo_night_photo",
            category = "Night Photography",
            title = "CYBER METROPOLIS — Midnight Horizon",
            subtitle = "Deep blues, luminous highlights, and long exposure neon",
            aspectRatio = "16:9",
            defaultFilter = FilterPreset.NIGHT_PHOTOGRAPHY,
            defaultAdjustments = ImageAdjustments(exposure = -8f, contrast = 1.35f, vibrance = 30f),
            defaultLighting = LightingStyle.COOL_MOONLIGHT,
            defaultFrame = FrameStyle.NONE,
            initialHeadline = "MIDNIGHT 02:00",
            initialTagline = "Horizon Lights",
            accentColorHex = 0xFF38BDF8
        ),
        DemoArtwork(
            id = "demo_portrait_photo",
            category = "Portrait Photography",
            title = "GOLDEN HOUR — Studio Identity",
            subtitle = "Authentic natural skin tones, soft backlight and warm aura",
            aspectRatio = "3:4",
            defaultFilter = FilterPreset.STUDIO_PORTRAIT,
            defaultAdjustments = ImageAdjustments(temperature = 18f, brightness = 5f, highlights = -10f),
            defaultLighting = LightingStyle.WARM_GLOW,
            defaultFrame = FrameStyle.PASSEPARTOUT,
            initialHeadline = "GOLDEN HOUR",
            initialTagline = "Authentic Identity",
            accentColorHex = 0xFFF59E0B
        ),
        DemoArtwork(
            id = "demo_typography",
            category = "Typography",
            title = "CREATIVE FREEDOM — Made by Ayham",
            subtitle = "Bold Swiss layout with layered geometric masks and purple accent",
            aspectRatio = "1:1",
            defaultFilter = FilterPreset.HIGH_CONTRAST_BW,
            defaultAdjustments = ImageAdjustments(contrast = 1.4f, clarity = 35f),
            defaultLighting = LightingStyle.NONE,
            defaultFrame = FrameStyle.MINIMAL_WHITE,
            initialHeadline = "CREATIVE\nFREEDOM",
            initialTagline = "Designed by Ayham • AVA",
            accentColorHex = 0xFFC084FC
        ),
        DemoArtwork(
            id = "demo_social_story",
            category = "Social Media Stories",
            title = "NEW DROP — Limited Edition",
            subtitle = "Vertical 9:16 story template with high-converting call to action",
            aspectRatio = "9:16",
            defaultFilter = FilterPreset.TEAL_AND_ORANGE,
            defaultAdjustments = ImageAdjustments(contrast = 1.2f, vibrance = 20f, sharpness = 20f),
            defaultLighting = LightingStyle.LIGHT_LEAK,
            defaultFrame = FrameStyle.FILM_BORDER,
            initialHeadline = "NEW DROP 26",
            initialTagline = "Swipe Up to Explore",
            accentColorHex = 0xFFEC4899
        ),
        DemoArtwork(
            id = "demo_advertisement",
            category = "Advertisement Designs",
            title = "FUTURE VISION — Global Launch",
            subtitle = "Commercial advertising billboard with sleek gradient typography",
            aspectRatio = "16:9",
            defaultFilter = FilterPreset.CINEMATIC,
            defaultAdjustments = ImageAdjustments(contrast = 1.22f, exposure = 6f, clarity = 25f),
            defaultLighting = LightingStyle.STUDIO_FLASH,
            defaultFrame = FrameStyle.NONE,
            initialHeadline = "FUTURE VISION",
            initialTagline = "Next Gen Creative Suite",
            accentColorHex = 0xFF8B5CF6
        )
    )

    /**
     * Generates a high-quality demonstration Bitmap for the artwork.
     */
    fun generateDemoBitmap(artwork: DemoArtwork, width: Int = 800, height: Int = 800): Bitmap {
        val bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888)
        val canvas = Canvas(bitmap)

        // 1. Rich dark background gradient
        val bgPaint = Paint(Paint.ANTI_ALIAS_FLAG)
        when (artwork.id) {
            "demo_streetwear", "demo_advertisement" -> {
                bgPaint.shader = LinearGradient(
                    0f, 0f, width.toFloat(), height.toFloat(),
                    intArrayOf(AndroidColor.rgb(15, 12, 28), AndroidColor.rgb(45, 18, 75), AndroidColor.rgb(10, 8, 16)),
                    null, Shader.TileMode.CLAMP
                )
            }
            "demo_night_photo" -> {
                bgPaint.shader = LinearGradient(
                    0f, 0f, 0f, height.toFloat(),
                    intArrayOf(AndroidColor.rgb(8, 15, 30), AndroidColor.rgb(18, 30, 60), AndroidColor.rgb(5, 8, 15)),
                    null, Shader.TileMode.CLAMP
                )
            }
            "demo_luxury_product", "demo_portrait_photo" -> {
                bgPaint.shader = RadialGradient(
                    width / 2f, height * 0.45f, width * 0.7f,
                    intArrayOf(AndroidColor.rgb(50, 32, 18), AndroidColor.rgb(20, 14, 10), AndroidColor.rgb(8, 6, 8)),
                    null, Shader.TileMode.CLAMP
                )
            }
            "demo_fashion_editorial", "demo_typography" -> {
                bgPaint.shader = RadialGradient(
                    width / 2f, height / 2f, width * 0.65f,
                    intArrayOf(AndroidColor.rgb(35, 35, 40), AndroidColor.rgb(16, 16, 18), AndroidColor.rgb(6, 6, 8)),
                    null, Shader.TileMode.CLAMP
                )
            }
            else -> {
                bgPaint.shader = RadialGradient(
                    width / 2f, height / 2f, width * 0.75f,
                    intArrayOf(AndroidColor.rgb(30, 24, 48), AndroidColor.rgb(14, 12, 22), AndroidColor.rgb(8, 6, 12)),
                    null, Shader.TileMode.CLAMP
                )
            }
        }
        canvas.drawRect(0f, 0f, width.toFloat(), height.toFloat(), bgPaint)

        // 2. Artistic geometric focal elements
        val elementPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
            style = Paint.Style.STROKE
            strokeWidth = 3f
            color = AndroidColor.argb(70, 255, 255, 255)
        }
        canvas.drawCircle(width / 2f, height * 0.42f, width * 0.28f, elementPaint)

        val fillPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
            color = AndroidColor.argb(35, 139, 92, 246)
        }
        canvas.drawRoundRect(
            RectF(width * 0.25f, height * 0.22f, width * 0.75f, height * 0.62f),
            24f, 24f, fillPaint
        )

        // Inner glowing core
        val glowPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
            shader = RadialGradient(
                width / 2f, height * 0.42f, width * 0.35f,
                intArrayOf(
                    artwork.accentColorHex.toInt() and 0x00FFFFFF or 0x66000000,
                    AndroidColor.TRANSPARENT
                ),
                null, Shader.TileMode.CLAMP
            )
        }
        canvas.drawCircle(width / 2f, height * 0.42f, width * 0.35f, glowPaint)

        // 3. Category Chip & Brand Badge
        val badgePaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
            color = AndroidColor.argb(180, 255, 255, 255)
            textSize = 22f
            typeface = Typeface.DEFAULT_BOLD
            letterSpacing = 0.2f
        }
        canvas.drawText("AVA STUDIO • ${artwork.category.uppercase()}", 48f, 72f, badgePaint)

        // 4. Headline
        val titlePaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
            color = AndroidColor.WHITE
            textSize = if (width < 600) 42f else 54f
            typeface = Typeface.DEFAULT_BOLD
            letterSpacing = 0.05f
            setShadowLayer(16f, 0f, 4f, AndroidColor.argb(160, 0, 0, 0))
        }

        val lines = artwork.initialHeadline.split("\n")
        var yPos = height * 0.76f
        for (line in lines) {
            canvas.drawText(line, 48f, yPos, titlePaint)
            yPos += 58f
        }

        // 5. Subtitle & Signature
        val subPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
            color = AndroidColor.rgb(192, 132, 252)
            textSize = 24f
            typeface = Typeface.DEFAULT
            letterSpacing = 0.08f
        }
        canvas.drawText("${artwork.initialTagline}  |  Made by Ayham", 48f, yPos + 8f, subPaint)

        return bitmap
    }
}
