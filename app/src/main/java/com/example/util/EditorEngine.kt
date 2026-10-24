package com.example.util

import android.content.Context
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Color as AndroidColor
import android.graphics.ColorMatrix
import android.graphics.ColorMatrixColorFilter
import android.graphics.LinearGradient
import android.graphics.Paint
import android.graphics.Path
import android.graphics.PorterDuff
import android.graphics.PorterDuffXfermode
import android.graphics.RadialGradient
import android.graphics.Rect
import android.graphics.RectF
import android.graphics.Shader
import android.graphics.Typeface
import android.net.Uri
import android.os.Environment
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import java.io.File
import java.io.FileOutputStream
import java.util.UUID

// -------------------------------------------------------------
// Core Adjustments Data Class
// -------------------------------------------------------------
data class ImageAdjustments(
    // Light
    val exposure: Float = 0f,          // -50 to +50
    val brightness: Float = 0f,        // -100 to +100
    val contrast: Float = 1f,          // 0.5 to 2.0
    val highlights: Float = 0f,        // -50 to +50
    val shadows: Float = 0f,           // -50 to +50
    val whites: Float = 0f,            // -50 to +50
    val blacks: Float = 0f,            // -50 to +50
    val gamma: Float = 1f,             // 0.5 to 1.5

    // Color
    val saturation: Float = 1f,        // 0.0 to 2.0
    val vibrance: Float = 0f,          // -50 to +50
    val temperature: Float = 0f,       // -50 to +50 (Cool to Warm)
    val tint: Float = 0f,              // -50 to +50 (Green to Magenta)
    val hue: Float = 0f,               // -180 to +180
    val fade: Float = 0f,              // 0 to 50
    val colorBalanceR: Float = 0f,     // -30 to +30
    val colorBalanceG: Float = 0f,     // -30 to +30
    val colorBalanceB: Float = 0f,     // -30 to +30

    // Details & Effects
    val sharpness: Float = 0f,         // 0 to 100
    val clarity: Float = 0f,           // 0 to 100
    val grain: Float = 0f,             // 0 to 100
    val vignette: Float = 0f,          // 0 to 100
    val noise: Float = 0f,             // 0 to 100

    // Appearance
    val opacity: Float = 1f,           // 0.0 to 1.0
    val borderThickness: Float = 0f,   // 0 to 30
    val cornerRadius: Float = 0f       // 0 to 60
)

// -------------------------------------------------------------
// Professional Filter Library Categories & Presets
// -------------------------------------------------------------
enum class FilterCategory(val title: String) {
    NATURAL("Natural"),
    EDITORIAL("Editorial"),
    STREET("Street"),
    CINEMATIC("Cinematic"),
    BLACK_AND_WHITE("B&W"),
    COLOR_STYLES("Color"),
    NIGHT("Night")
}

enum class FilterPreset(
    val title: String,
    val category: FilterCategory
) {
    NONE("Original", FilterCategory.NATURAL),

    // NATURAL
    NATURAL("Natural", FilterCategory.NATURAL),
    CLEAN("Clean", FilterCategory.NATURAL),
    SOFT_LIGHT("Soft Light", FilterCategory.NATURAL),
    WARM_NATURAL("Warm Natural", FilterCategory.NATURAL),
    COOL_NATURAL("Cool Natural", FilterCategory.NATURAL),

    // EDITORIAL
    FASHION_EDITORIAL("Fashion Editorial", FilterCategory.EDITORIAL),
    STUDIO_PORTRAIT("Studio Portrait", FilterCategory.EDITORIAL),
    LUXURY_EDITORIAL("Luxury Editorial", FilterCategory.EDITORIAL),
    MAGAZINE("Magazine", FilterCategory.EDITORIAL),
    MINIMAL("Minimal", FilterCategory.EDITORIAL),

    // STREET
    URBAN("Urban", FilterCategory.STREET),
    STREETWEAR("Streetwear", FilterCategory.STREET),
    DARK_STREET("Dark Street", FilterCategory.STREET),
    NIGHT_STREET("Night Street", FilterCategory.STREET),
    MOODY("Moody", FilterCategory.STREET),

    // CINEMATIC
    CINEMATIC("Cinematic", FilterCategory.CINEMATIC),
    TEAL_AND_ORANGE("Teal & Orange", FilterCategory.CINEMATIC),
    FILM("35mm Film", FilterCategory.CINEMATIC),
    VINTAGE_FILM("Vintage Film", FilterCategory.CINEMATIC),
    MATTE("Matte", FilterCategory.CINEMATIC),
    DRAMATIC("Dramatic", FilterCategory.CINEMATIC),

    // BLACK AND WHITE
    CLASSIC_BW("Classic B&W", FilterCategory.BLACK_AND_WHITE),
    HIGH_CONTRAST_BW("High Contrast B&W", FilterCategory.BLACK_AND_WHITE),
    SOFT_BW("Soft B&W", FilterCategory.BLACK_AND_WHITE),

    // COLOR STYLES
    WARM_STYLE("Warm", FilterCategory.COLOR_STYLES),
    COOL_STYLE("Cool", FilterCategory.COLOR_STYLES),
    GOLDEN("Golden", FilterCategory.COLOR_STYLES),
    PURPLE_MOOD("Purple Mood", FilterCategory.COLOR_STYLES),
    DEEP_BLUE("Deep Blue", FilterCategory.COLOR_STYLES),
    MUTED_COLORS("Muted Colors", FilterCategory.COLOR_STYLES),
    VIBRANT("Vibrant", FilterCategory.COLOR_STYLES),

    // NIGHT
    NIGHT_PHOTOGRAPHY("Night Photo", FilterCategory.NIGHT),
    NIGHT_FLASH("Night Flash", FilterCategory.NIGHT),
    CITY_LIGHTS("City Lights", FilterCategory.NIGHT),
    LOW_KEY("Low-Key", FilterCategory.NIGHT),
    FLASH_PORTRAIT("Flash Portrait", FilterCategory.NIGHT)
}

// -------------------------------------------------------------
// Creative Effects & Lighting & Frames
// -------------------------------------------------------------
enum class CreativeEffect(val title: String) {
    NONE("None"),
    VIGNETTE("Vignette"),
    FILM_GRAIN("Film Grain"),
    LIGHT_LEAK("Light Leak"),
    SOFT_GLOW("Soft Glow"),
    COLOR_FADE("Color Fade"),
    MOTION_BLUR("Motion Blur"),
    FILM_TEXTURE("Film Texture"),
    DUST_SCRATCHES("Dust & Scratches"),
    DUOTONE("Duotone"),
    PIXELATE("Pixelate"),
    POSTERIZE("Posterize")
}

enum class LightingStyle(val title: String) {
    NONE("None"),
    DIRECTIONAL_SUN("Directional Sun"),
    WARM_GLOW("Warm Glow"),
    COOL_MOONLIGHT("Cool Moonlight"),
    STUDIO_FLASH("Studio Flash"),
    NEON_BEAM("Neon Beam"),
    LIGHT_LEAK("Light Leak"),
    PRISM_RAINBOW("Prism Rainbow")
}

enum class FrameStyle(val title: String) {
    NONE("None"),
    MINIMAL_WHITE("Minimal White"),
    MINIMAL_BLACK("Minimal Black"),
    FILM_BORDER("Film 35mm"),
    POLAROID("Polaroid"),
    LUXURY_GOLD("Luxury Gold"),
    NEON_PURPLE("Neon Purple"),
    PASSEPARTOUT("Passepartout")
}

// -------------------------------------------------------------
// Blur Types & Drawing Strokes
// -------------------------------------------------------------
enum class BlurType(val title: String) {
    NONE("None"),
    GAUSSIAN("Gaussian Blur"),
    MOTION("Motion Blur"),
    RADIAL("Zoom/Radial"),
    BACKGROUND("Background Blur"),
    SELECTIVE("Selective Brush")
}

data class BlurBrushPoint(
    val x: Float,
    val y: Float,
    val radius: Float = 36f,
    val isEraser: Boolean = false
)

data class DrawPoint(val x: Float, val y: Float)

data class DrawStroke(
    val points: List<DrawPoint>,
    val color: Color,
    val strokeWidth: Float,
    val opacity: Float = 1f,
    val isEraser: Boolean = false
)

// -------------------------------------------------------------
// Studio Layer System
// -------------------------------------------------------------
enum class LayerType {
    IMAGE, TEXT, SHAPE, ELEMENT, OVERLAY
}

sealed class StudioLayer(
    open val id: String = UUID.randomUUID().toString(),
    open val name: String,
    open val type: LayerType,
    open var isVisible: Boolean = true,
    open var isLocked: Boolean = false,
    open var opacity: Float = 1f
) {
    data class Text(
        override val id: String = UUID.randomUUID().toString(),
        override val name: String = "Text",
        var content: String,
        var x: Float,
        var y: Float,
        var fontSize: Float = 34f,
        var color: Color = Color.White,
        var fontFamily: String = "Modern", // Modern, Minimal, Bold, Editorial, Luxury, Streetwear, Serif, Sans, Arabic
        var isBold: Boolean = true,
        var isItalic: Boolean = false,
        var letterSpacing: Float = 0f,
        var bgHighlight: Color? = null,
        var outlineColor: Color? = null,
        var outlineWidth: Float = 0f,
        var shadowColor: Color? = null,
        var shadowRadius: Float = 0f,
        var rotation: Float = 0f,
        override var isVisible: Boolean = true,
        override var isLocked: Boolean = false,
        override var opacity: Float = 1f
    ) : StudioLayer(id, name, LayerType.TEXT, isVisible, isLocked, opacity)

    data class Shape(
        override val id: String = UUID.randomUUID().toString(),
        override val name: String = "Shape",
        var shapeKind: String = "RECTANGLE", // RECTANGLE, CIRCLE, LINE, ARROW, STAR, BADGE
        var x: Float,
        var y: Float,
        var width: Float,
        var height: Float,
        var fillColor: Color = Color.Transparent,
        var strokeColor: Color = Color(0xFF8B5CF6),
        var strokeWidth: Float = 4f,
        var rotation: Float = 0f,
        override var isVisible: Boolean = true,
        override var isLocked: Boolean = false,
        override var opacity: Float = 1f
    ) : StudioLayer(id, name, LayerType.SHAPE, isVisible, isLocked, opacity)

    data class Element(
        override val id: String = UUID.randomUUID().toString(),
        override val name: String = "Graphic",
        var elementKind: String = "STAR", // STAR, BADGE, ARROW, LINE, FRAME, STAMP
        var x: Float,
        var y: Float,
        var size: Float = 100f,
        var tintColor: Color = Color(0xFF8B5CF6),
        var rotation: Float = 0f,
        override var isVisible: Boolean = true,
        override var isLocked: Boolean = false,
        override var opacity: Float = 1f
    ) : StudioLayer(id, name, LayerType.ELEMENT, isVisible, isLocked, opacity)
}

// -------------------------------------------------------------
// Undo / Redo Snapshot State
// -------------------------------------------------------------
data class EditorSnapshot(
    val adjustments: ImageAdjustments,
    val filter: FilterPreset,
    val filterIntensity: Float,
    val blurType: BlurType,
    val blurIntensity: Float,
    val blurStrokes: List<BlurBrushPoint>,
    val drawStrokes: List<DrawStroke>,
    val layers: List<StudioLayer>,
    val lightingStyle: LightingStyle,
    val lightingOpacity: Float,
    val frameStyle: FrameStyle,
    val creativeEffect: CreativeEffect,
    val rotationAngle: Float,
    val flipH: Boolean,
    val flipV: Boolean,
    val cropRatio: String
)

// -------------------------------------------------------------
// Main Editor Engine
// -------------------------------------------------------------
object EditorEngine {

    /**
     * Builds a single unified ColorMatrixColorFilter applying all Light, Color,
     * Details, Filter Preset (with intensity interpolation), and Creative Effects.
     */
    fun createMasterColorMatrix(
        adjustments: ImageAdjustments,
        filter: FilterPreset,
        filterIntensity: Float = 1f,
        creativeEffect: CreativeEffect = CreativeEffect.NONE
    ): ColorMatrix {
        val masterCm = ColorMatrix()

        // 1. Contrast, Brightness & Exposure & Highlights/Shadows
        val c = adjustments.contrast.coerceIn(0.2f, 3.0f)
        val b = adjustments.brightness + (adjustments.exposure * 0.6f) + (adjustments.highlights * 0.3f) + (adjustments.shadows * 0.2f)
        val t = (1f - c) * 128f / 2f + b

        val contrastMatrix = ColorMatrix(
            floatArrayOf(
                c, 0f, 0f, 0f, t,
                0f, c, 0f, 0f, t,
                0f, 0f, c, 0f, t,
                0f, 0f, 0f, 1f, 0f
            )
        )
        masterCm.postConcat(contrastMatrix)

        // 2. Saturation & Vibrance
        val effectiveSat = (adjustments.saturation + (adjustments.vibrance * 0.015f)).coerceAtLeast(0f)
        val satMatrix = ColorMatrix()
        satMatrix.setSaturation(effectiveSat)
        masterCm.postConcat(satMatrix)

        // 3. Temperature & Tint & Color Balance
        val temp = adjustments.temperature
        val tint = adjustments.tint
        val rBal = adjustments.colorBalanceR * 1.5f + (temp * 1.3f)
        val gBal = adjustments.colorBalanceG * 1.5f - (tint * 1.2f)
        val bBal = adjustments.colorBalanceB * 1.5f - (temp * 1.3f) + (tint * 0.8f)

        if (rBal != 0f || gBal != 0f || bBal != 0f) {
            val colorShiftMatrix = ColorMatrix(
                floatArrayOf(
                    1f, 0f, 0f, 0f, rBal,
                    0f, 1f, 0f, 0f, gBal,
                    0f, 0f, 1f, 0f, bBal,
                    0f, 0f, 0f, 1f, 0f
                )
            )
            masterCm.postConcat(colorShiftMatrix)
        }

        // 4. Fade (lifts blacks)
        if (adjustments.fade > 0f) {
            val fadeVal = adjustments.fade * 0.9f
            val fadeMatrix = ColorMatrix(
                floatArrayOf(
                    1f - (adjustments.fade * 0.005f), 0f, 0f, 0f, fadeVal,
                    0f, 1f - (adjustments.fade * 0.005f), 0f, 0f, fadeVal,
                    0f, 0f, 1f - (adjustments.fade * 0.005f), 0f, fadeVal,
                    0f, 0f, 0f, 1f, 0f
                )
            )
            masterCm.postConcat(fadeMatrix)
        }

        // 5. Filter Preset Matrix with Intensity Weighting
        val rawFilterMatrix = getPresetColorMatrix(filter)
        if (rawFilterMatrix != null && filterIntensity > 0.01f) {
            if (filterIntensity >= 0.99f) {
                masterCm.postConcat(rawFilterMatrix)
            } else {
                // Interpolate between Identity and rawFilterMatrix based on filterIntensity
                val interpolated = interpolateColorMatrix(rawFilterMatrix, filterIntensity)
                masterCm.postConcat(interpolated)
            }
        }

        // 6. Creative Effect Adjustments
        when (creativeEffect) {
            CreativeEffect.DUOTONE -> {
                val duotone = ColorMatrix(
                    floatArrayOf(
                        0.7f, 0.3f, 0f, 0f, 25f,
                        0f, 0.4f, 0.6f, 0f, -10f,
                        0.3f, 0f, 0.8f, 0f, 40f,
                        0f, 0f, 0f, 1f, 0f
                    )
                )
                masterCm.postConcat(duotone)
            }
            CreativeEffect.POSTERIZE -> {
                val postMatrix = ColorMatrix(
                    floatArrayOf(
                        1.4f, 0f, 0f, 0f, -20f,
                        0f, 1.4f, 0f, 0f, -20f,
                        0f, 0f, 1.4f, 0f, -20f,
                        0f, 0f, 0f, 1f, 0f
                    )
                )
                masterCm.postConcat(postMatrix)
            }
            CreativeEffect.COLOR_FADE -> {
                val fadeFx = ColorMatrix(
                    floatArrayOf(
                        0.9f, 0f, 0f, 0f, 35f,
                        0f, 0.9f, 0f, 0f, 30f,
                        0f, 0f, 0.85f, 0f, 45f,
                        0f, 0f, 0f, 1f, 0f
                    )
                )
                masterCm.postConcat(fadeFx)
            }
            else -> {}
        }

        return masterCm
    }

    fun createColorFilter(
        adjustments: ImageAdjustments,
        filter: FilterPreset,
        filterIntensity: Float = 1f,
        creativeEffect: CreativeEffect = CreativeEffect.NONE
    ): ColorMatrixColorFilter {
        return ColorMatrixColorFilter(createMasterColorMatrix(adjustments, filter, filterIntensity, creativeEffect))
    }

    fun createComposeColorFilter(
        adjustments: ImageAdjustments,
        filter: FilterPreset,
        filterIntensity: Float = 1f,
        creativeEffect: CreativeEffect = CreativeEffect.NONE
    ): androidx.compose.ui.graphics.ColorFilter {
        val cm = createMasterColorMatrix(adjustments, filter, filterIntensity, creativeEffect)
        return androidx.compose.ui.graphics.ColorFilter.colorMatrix(androidx.compose.ui.graphics.ColorMatrix(cm.array))
    }

    private fun interpolateColorMatrix(target: ColorMatrix, weight: Float): ColorMatrix {
        val w = weight.coerceIn(0f, 1f)
        val targetArray = target.array
        val identityArray = floatArrayOf(
            1f, 0f, 0f, 0f, 0f,
            0f, 1f, 0f, 0f, 0f,
            0f, 0f, 1f, 0f, 0f,
            0f, 0f, 0f, 1f, 0f
        )
        val result = FloatArray(20)
        for (i in 0 until 20) {
            result[i] = identityArray[i] * (1f - w) + targetArray[i] * w
        }
        return ColorMatrix(result)
    }

    private fun getPresetColorMatrix(preset: FilterPreset): ColorMatrix? {
        return when (preset) {
            FilterPreset.NONE -> null

            // NATURAL
            FilterPreset.NATURAL -> ColorMatrix(floatArrayOf(
                1.04f, 0f, 0f, 0f, 4f,
                0f, 1.04f, 0f, 0f, 4f,
                0f, 0f, 1.04f, 0f, 4f,
                0f, 0f, 0f, 1f, 0f
            ))
            FilterPreset.CLEAN -> ColorMatrix(floatArrayOf(
                1.08f, 0f, 0f, 0f, 8f,
                0f, 1.08f, 0f, 0f, 8f,
                0f, 0f, 1.10f, 0f, 10f,
                0f, 0f, 0f, 1f, 0f
            ))
            FilterPreset.SOFT_LIGHT -> ColorMatrix(floatArrayOf(
                1.02f, 0f, 0f, 0f, 12f,
                0f, 1.01f, 0f, 0f, 10f,
                0f, 0f, 0.98f, 0f, 6f,
                0f, 0f, 0f, 1f, 0f
            ))
            FilterPreset.WARM_NATURAL -> ColorMatrix(floatArrayOf(
                1.12f, 0f, 0f, 0f, 14f,
                0f, 1.04f, 0f, 0f, 6f,
                0f, 0f, 0.92f, 0f, -8f,
                0f, 0f, 0f, 1f, 0f
            ))
            FilterPreset.COOL_NATURAL -> ColorMatrix(floatArrayOf(
                0.94f, 0f, 0f, 0f, -4f,
                0f, 1.02f, 0f, 0f, 2f,
                0f, 0f, 1.15f, 0f, 16f,
                0f, 0f, 0f, 1f, 0f
            ))

            // EDITORIAL
            FilterPreset.FASHION_EDITORIAL -> ColorMatrix(floatArrayOf(
                1.18f, 0f, 0f, 0f, -5f,
                0f, 1.14f, 0f, 0f, -5f,
                0f, 0f, 1.08f, 0f, 8f,
                0f, 0f, 0f, 1f, 0f
            ))
            FilterPreset.STUDIO_PORTRAIT -> ColorMatrix(floatArrayOf(
                1.10f, 0.05f, 0f, 0f, 10f,
                0.05f, 1.05f, 0f, 0f, 6f,
                0f, 0f, 0.98f, 0f, 2f,
                0f, 0f, 0f, 1f, 0f
            ))
            FilterPreset.LUXURY_EDITORIAL -> ColorMatrix(floatArrayOf(
                1.22f, 0.08f, 0f, 0f, 15f,
                0.08f, 1.12f, 0f, 0f, 10f,
                0f, 0f, 0.85f, 0f, -14f,
                0f, 0f, 0f, 1f, 0f
            ))
            FilterPreset.MAGAZINE -> ColorMatrix(floatArrayOf(
                1.25f, 0f, 0f, 0f, -12f,
                0f, 1.22f, 0f, 0f, -12f,
                0f, 0f, 1.20f, 0f, -6f,
                0f, 0f, 0f, 1f, 0f
            ))
            FilterPreset.MINIMAL -> ColorMatrix(floatArrayOf(
                0.98f, 0f, 0f, 0f, 14f,
                0f, 0.98f, 0f, 0f, 14f,
                0f, 0f, 0.98f, 0f, 14f,
                0f, 0f, 0f, 1f, 0f
            ))

            // STREET
            FilterPreset.URBAN -> ColorMatrix(floatArrayOf(
                1.24f, 0f, 0f, 0f, -18f,
                0f, 1.16f, 0f, 0f, -14f,
                0f, 0f, 1.12f, 0f, -8f,
                0f, 0f, 0f, 1f, 0f
            ))
            FilterPreset.STREETWEAR -> ColorMatrix(floatArrayOf(
                1.28f, 0f, 0f, 0f, -12f,
                0f, 1.25f, 0f, 0f, -12f,
                0f, 0f, 1.30f, 0f, 10f,
                0f, 0f, 0f, 1f, 0f
            ))
            FilterPreset.DARK_STREET -> ColorMatrix(floatArrayOf(
                0.85f, 0f, 0f, 0f, -28f,
                0f, 0.85f, 0f, 0f, -28f,
                0f, 0f, 0.95f, 0f, -12f,
                0f, 0f, 0f, 1f, 0f
            ))
            FilterPreset.NIGHT_STREET -> ColorMatrix(floatArrayOf(
                0.90f, 0f, 0.1f, 0f, -15f,
                0f, 0.92f, 0.1f, 0f, -15f,
                0.1f, 0f, 1.35f, 0f, 25f,
                0f, 0f, 0f, 1f, 0f
            ))
            FilterPreset.MOODY -> ColorMatrix(floatArrayOf(
                0.92f, 0f, 0f, 0f, -8f,
                0f, 0.95f, 0f, 0f, -6f,
                0f, 0f, 1.05f, 0f, 12f,
                0f, 0f, 0f, 1f, 0f
            ))

            // CINEMATIC
            FilterPreset.CINEMATIC -> ColorMatrix(floatArrayOf(
                1.15f, 0f, 0.05f, 0f, 8f,
                0f, 1.05f, 0.05f, 0f, 4f,
                0.05f, 0.05f, 1.28f, 0f, 18f,
                0f, 0f, 0f, 1f, 0f
            ))
            FilterPreset.TEAL_AND_ORANGE -> ColorMatrix(floatArrayOf(
                1.30f, 0f, 0f, 0f, 22f,
                0f, 1.05f, 0.15f, 0f, 5f,
                -0.1f, 0.1f, 1.40f, 0f, 28f,
                0f, 0f, 0f, 1f, 0f
            ))
            FilterPreset.FILM -> ColorMatrix(floatArrayOf(
                1.12f, 0f, 0f, 0f, 10f,
                0f, 1.08f, 0f, 0f, 6f,
                0f, 0f, 0.96f, 0f, 12f,
                0f, 0f, 0f, 1f, 0f
            ))
            FilterPreset.VINTAGE_FILM -> ColorMatrix(floatArrayOf(
                1.15f, 0.08f, 0f, 0f, 16f,
                0.05f, 1.02f, 0f, 0f, 12f,
                0.05f, 0f, 0.82f, 0f, -8f,
                0f, 0f, 0f, 1f, 0f
            ))
            FilterPreset.MATTE -> ColorMatrix(floatArrayOf(
                0.96f, 0f, 0f, 0f, 22f,
                0f, 0.96f, 0f, 0f, 22f,
                0f, 0f, 0.96f, 0f, 22f,
                0f, 0f, 0f, 1f, 0f
            ))
            FilterPreset.DRAMATIC -> ColorMatrix(floatArrayOf(
                1.35f, 0f, 0f, 0f, -22f,
                0f, 1.30f, 0f, 0f, -22f,
                0f, 0f, 1.28f, 0f, -16f,
                0f, 0f, 0f, 1f, 0f
            ))

            // BLACK AND WHITE
            FilterPreset.CLASSIC_BW -> {
                val bw = ColorMatrix()
                bw.setSaturation(0f)
                bw
            }
            FilterPreset.HIGH_CONTRAST_BW -> {
                val bw = ColorMatrix()
                bw.setSaturation(0f)
                val cMatrix = ColorMatrix(floatArrayOf(
                    1.4f, 0f, 0f, 0f, -30f,
                    0f, 1.4f, 0f, 0f, -30f,
                    0f, 0f, 1.4f, 0f, -30f,
                    0f, 0f, 0f, 1f, 0f
                ))
                bw.postConcat(cMatrix)
                bw
            }
            FilterPreset.SOFT_BW -> {
                val bw = ColorMatrix()
                bw.setSaturation(0f)
                val soft = ColorMatrix(floatArrayOf(
                    0.95f, 0f, 0f, 0f, 20f,
                    0f, 0.95f, 0f, 0f, 20f,
                    0f, 0f, 0.95f, 0f, 20f,
                    0f, 0f, 0f, 1f, 0f
                ))
                bw.postConcat(soft)
                bw
            }

            // COLOR STYLES
            FilterPreset.WARM_STYLE -> ColorMatrix(floatArrayOf(
                1.20f, 0f, 0f, 0f, 18f,
                0f, 1.05f, 0f, 0f, 4f,
                0f, 0f, 0.82f, 0f, -16f,
                0f, 0f, 0f, 1f, 0f
            ))
            FilterPreset.COOL_STYLE -> ColorMatrix(floatArrayOf(
                0.86f, 0f, 0f, 0f, -12f,
                0f, 1.0f, 0f, 0f, 2f,
                0f, 0f, 1.30f, 0f, 26f,
                0f, 0f, 0f, 1f, 0f
            ))
            FilterPreset.GOLDEN -> ColorMatrix(floatArrayOf(
                1.26f, 0.1f, 0f, 0f, 24f,
                0.05f, 1.15f, 0f, 0f, 16f,
                0f, 0f, 0.74f, 0f, -22f,
                0f, 0f, 0f, 1f, 0f
            ))
            FilterPreset.PURPLE_MOOD -> ColorMatrix(floatArrayOf(
                1.12f, 0f, 0.15f, 0f, 15f,
                0f, 0.90f, 0f, 0f, -10f,
                0.15f, 0f, 1.35f, 0f, 32f,
                0f, 0f, 0f, 1f, 0f
            ))
            FilterPreset.DEEP_BLUE -> ColorMatrix(floatArrayOf(
                0.78f, 0f, 0f, 0f, -20f,
                0f, 0.92f, 0f, 0f, -8f,
                0f, 0.1f, 1.45f, 0f, 35f,
                0f, 0f, 0f, 1f, 0f
            ))
            FilterPreset.MUTED_COLORS -> ColorMatrix(floatArrayOf(
                0.85f, 0.05f, 0.05f, 0f, 8f,
                0.05f, 0.85f, 0.05f, 0f, 8f,
                0.05f, 0.05f, 0.85f, 0f, 8f,
                0f, 0f, 0f, 1f, 0f
            ))
            FilterPreset.VIBRANT -> ColorMatrix(floatArrayOf(
                1.25f, -0.05f, -0.05f, 0f, 0f,
                -0.05f, 1.25f, -0.05f, 0f, 0f,
                -0.05f, -0.05f, 1.25f, 0f, 0f,
                0f, 0f, 0f, 1f, 0f
            ))

            // NIGHT
            FilterPreset.NIGHT_PHOTOGRAPHY -> ColorMatrix(floatArrayOf(
                0.88f, 0f, 0.15f, 0f, -12f,
                0f, 0.92f, 0.08f, 0f, -8f,
                0.1f, 0f, 1.38f, 0f, 22f,
                0f, 0f, 0f, 1f, 0f
            ))
            FilterPreset.NIGHT_FLASH -> ColorMatrix(floatArrayOf(
                1.35f, 0f, 0f, 0f, 15f,
                0f, 1.30f, 0f, 0f, 12f,
                0f, 0f, 1.25f, 0f, 8f,
                0f, 0f, 0f, 1f, 0f
            ))
            FilterPreset.CITY_LIGHTS -> ColorMatrix(floatArrayOf(
                1.15f, 0.1f, 0f, 0f, 6f,
                0.05f, 1.10f, 0.05f, 0f, 4f,
                0f, 0.1f, 1.40f, 0f, 24f,
                0f, 0f, 0f, 1f, 0f
            ))
            FilterPreset.LOW_KEY -> ColorMatrix(floatArrayOf(
                0.75f, 0f, 0f, 0f, -35f,
                0f, 0.75f, 0f, 0f, -35f,
                0f, 0f, 0.85f, 0f, -25f,
                0f, 0f, 0f, 1f, 0f
            ))
            FilterPreset.FLASH_PORTRAIT -> ColorMatrix(floatArrayOf(
                1.25f, 0.05f, 0f, 0f, 18f,
                0.05f, 1.18f, 0f, 0f, 12f,
                0f, 0f, 1.10f, 0f, 4f,
                0f, 0f, 0f, 1f, 0f
            ))
        }
    }

    /**
     * Renders the complete artwork at full resolution including all layers,
     * drawings, text, overlays, and frames, and exports to Bitmap.
     */
    fun exportCanvasToBitmap(
        width: Int,
        height: Int,
        backgroundColor: Color,
        baseBitmap: Bitmap?,
        adjustments: ImageAdjustments,
        filter: FilterPreset,
        filterIntensity: Float = 1f,
        creativeEffect: CreativeEffect = CreativeEffect.NONE,
        lightingStyle: LightingStyle = LightingStyle.NONE,
        lightingOpacity: Float = 0.5f,
        frameStyle: FrameStyle = FrameStyle.NONE,
        blurType: BlurType = BlurType.NONE,
        blurIntensity: Float = 0f,
        blurStrokes: List<BlurBrushPoint> = emptyList(),
        drawStrokes: List<DrawStroke> = emptyList(),
        layers: List<StudioLayer> = emptyList(),
        rotationAngle: Float = 0f,
        flipH: Boolean = false,
        flipV: Boolean = false
    ): Bitmap {
        val outputBitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888)
        val canvas = Canvas(outputBitmap)

        // 1. Background fill
        canvas.drawColor(backgroundColor.toArgb())

        // 2. Base Image with Filter & Adjustments
        if (baseBitmap != null) {
            val paint = Paint(Paint.ANTI_ALIAS_FLAG or Paint.FILTER_BITMAP_FLAG).apply {
                colorFilter = createColorFilter(adjustments, filter, filterIntensity, creativeEffect)
                alpha = (adjustments.opacity * 255).toInt().coerceIn(0, 255)
            }

            canvas.save()
            // Transformations: rotation & flips
            canvas.translate(width / 2f, height / 2f)
            canvas.rotate(rotationAngle)
            val scaleX = if (flipH) -1f else 1f
            val scaleY = if (flipV) -1f else 1f
            canvas.scale(scaleX, scaleY)
            canvas.translate(-width / 2f, -height / 2f)

            val destRect = Rect(0, 0, width, height)
            canvas.drawBitmap(baseBitmap, null, destRect, paint)
            canvas.restore()

            // 3. Selective Blur Mask
            if (blurType == BlurType.SELECTIVE && blurStrokes.isNotEmpty()) {
                val blurPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
                    color = AndroidColor.WHITE
                    alpha = (blurIntensity.coerceIn(0f, 50f) * 4f).toInt().coerceIn(40, 200)
                    maskFilter = android.graphics.BlurMaskFilter(24f, android.graphics.BlurMaskFilter.Blur.NORMAL)
                }
                for (pt in blurStrokes) {
                    if (!pt.isEraser) {
                        canvas.drawCircle(pt.x, pt.y, pt.radius, blurPaint)
                    }
                }
            } else if (blurType != BlurType.NONE && blurIntensity > 0) {
                // Background / Gaussian overlay simulation
                val fullBlurPaint = Paint().apply {
                    color = AndroidColor.BLACK
                    alpha = (blurIntensity * 2.5f).toInt().coerceIn(10, 160)
                }
                canvas.drawRect(0f, 0f, width.toFloat(), height.toFloat(), fullBlurPaint)
            }
        }

        // 4. Drawing Strokes (Finger drawing)
        for (stroke in drawStrokes) {
            if (stroke.points.size < 2) continue
            val strokePaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
                color = stroke.color.toArgb()
                strokeWidth = stroke.strokeWidth
                style = Paint.Style.STROKE
                strokeCap = Paint.Cap.ROUND
                strokeJoin = Paint.Join.ROUND
                alpha = (stroke.opacity * 255).toInt().coerceIn(0, 255)
                if (stroke.isEraser) {
                    xfermode = PorterDuffXfermode(PorterDuff.Mode.CLEAR)
                }
            }

            val path = Path()
            path.moveTo(stroke.points[0].x, stroke.points[0].y)
            for (i in 1 until stroke.points.size) {
                path.lineTo(stroke.points[i].x, stroke.points[i].y)
            }
            canvas.drawPath(path, strokePaint)
        }

        // 5. Lighting Overlays
        if (lightingStyle != LightingStyle.NONE && lightingOpacity > 0.05f) {
            val lightPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
                alpha = (lightingOpacity * 255).toInt().coerceIn(0, 255)
            }
            when (lightingStyle) {
                LightingStyle.DIRECTIONAL_SUN -> {
                    lightPaint.shader = RadialGradient(
                        0f, 0f, width * 0.75f,
                        intArrayOf(AndroidColor.argb(220, 255, 235, 180), AndroidColor.TRANSPARENT),
                        null, Shader.TileMode.CLAMP
                    )
                    canvas.drawRect(0f, 0f, width.toFloat(), height.toFloat(), lightPaint)
                }
                LightingStyle.WARM_GLOW -> {
                    lightPaint.shader = RadialGradient(
                        width / 2f, height / 2f, width * 0.6f,
                        intArrayOf(AndroidColor.argb(180, 255, 180, 100), AndroidColor.TRANSPARENT),
                        null, Shader.TileMode.CLAMP
                    )
                    canvas.drawRect(0f, 0f, width.toFloat(), height.toFloat(), lightPaint)
                }
                LightingStyle.COOL_MOONLIGHT -> {
                    lightPaint.shader = LinearGradient(
                        0f, 0f, 0f, height.toFloat(),
                        intArrayOf(AndroidColor.argb(160, 160, 210, 255), AndroidColor.TRANSPARENT),
                        null, Shader.TileMode.CLAMP
                    )
                    canvas.drawRect(0f, 0f, width.toFloat(), height.toFloat(), lightPaint)
                }
                LightingStyle.STUDIO_FLASH -> {
                    lightPaint.shader = RadialGradient(
                        width * 0.5f, height * 0.4f, width * 0.5f,
                        intArrayOf(AndroidColor.argb(210, 255, 255, 255), AndroidColor.TRANSPARENT),
                        null, Shader.TileMode.CLAMP
                    )
                    canvas.drawRect(0f, 0f, width.toFloat(), height.toFloat(), lightPaint)
                }
                LightingStyle.NEON_BEAM -> {
                    lightPaint.shader = LinearGradient(
                        0f, 0f, width.toFloat(), height.toFloat(),
                        intArrayOf(AndroidColor.argb(190, 168, 85, 247), AndroidColor.TRANSPARENT),
                        null, Shader.TileMode.CLAMP
                    )
                    canvas.drawRect(0f, 0f, width.toFloat(), height.toFloat(), lightPaint)
                }
                LightingStyle.LIGHT_LEAK -> {
                    lightPaint.shader = RadialGradient(
                        width.toFloat(), 0f, width * 0.7f,
                        intArrayOf(AndroidColor.argb(220, 255, 120, 80), AndroidColor.TRANSPARENT),
                        null, Shader.TileMode.CLAMP
                    )
                    canvas.drawRect(0f, 0f, width.toFloat(), height.toFloat(), lightPaint)
                }
                LightingStyle.PRISM_RAINBOW -> {
                    lightPaint.shader = LinearGradient(
                        0f, 0f, width * 0.6f, height * 0.6f,
                        intArrayOf(
                            AndroidColor.argb(160, 255, 80, 80),
                            AndroidColor.argb(160, 255, 220, 80),
                            AndroidColor.argb(160, 80, 220, 255),
                            AndroidColor.TRANSPARENT
                        ),
                        null, Shader.TileMode.CLAMP
                    )
                    canvas.drawRect(0f, 0f, width.toFloat(), height.toFloat(), lightPaint)
                }
                else -> {}
            }
        }

        // 6. Studio Layers (Text, Shapes, Graphics)
        for (layer in layers) {
            if (!layer.isVisible) continue

            when (layer) {
                is StudioLayer.Text -> {
                    val paint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
                        color = layer.color.toArgb()
                        textSize = layer.fontSize
                        alpha = (layer.opacity * 255).toInt().coerceIn(0, 255)
                        typeface = when (layer.fontFamily) {
                            "Editorial", "Luxury", "Serif" -> Typeface.SERIF
                            "Minimal", "Modern", "Sans" -> Typeface.SANS_SERIF
                            "Bold", "Streetwear" -> Typeface.DEFAULT_BOLD
                            else -> if (layer.isBold) Typeface.DEFAULT_BOLD else Typeface.DEFAULT
                        }
                    }

                    // Background highlight
                    layer.bgHighlight?.let { bg ->
                        val textBounds = Rect()
                        paint.getTextBounds(layer.content, 0, layer.content.length, textBounds)
                        val bgPaint = Paint().apply {
                            color = bg.toArgb()
                            alpha = (layer.opacity * 255).toInt().coerceIn(0, 255)
                        }
                        val pad = 12f
                        canvas.drawRoundRect(
                            RectF(
                                layer.x - pad,
                                layer.y + textBounds.top - pad,
                                layer.x + textBounds.width() + pad,
                                layer.y + textBounds.bottom + pad
                            ),
                            8f, 8f, bgPaint
                        )
                    }

                    // Shadow if configured
                    layer.shadowColor?.let { sColor ->
                        paint.setShadowLayer(layer.shadowRadius, 4f, 4f, sColor.toArgb())
                    }

                    canvas.save()
                    canvas.rotate(layer.rotation, layer.x, layer.y)
                    canvas.drawText(layer.content, layer.x, layer.y, paint)
                    canvas.restore()
                }

                is StudioLayer.Shape -> {
                    val paint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
                        color = layer.strokeColor.toArgb()
                        strokeWidth = layer.strokeWidth
                        style = if (layer.fillColor != Color.Transparent) Paint.Style.FILL_AND_STROKE else Paint.Style.STROKE
                        alpha = (layer.opacity * 255).toInt().coerceIn(0, 255)
                    }

                    canvas.save()
                    canvas.rotate(layer.rotation, layer.x + layer.width / 2f, layer.y + layer.height / 2f)
                    when (layer.shapeKind) {
                        "CIRCLE" -> {
                            val r = minOf(layer.width, layer.height) / 2f
                            canvas.drawCircle(layer.x + r, layer.y + r, r, paint)
                        }
                        "LINE" -> {
                            canvas.drawLine(layer.x, layer.y, layer.x + layer.width, layer.y + layer.height, paint)
                        }
                        else -> {
                            canvas.drawRoundRect(
                                RectF(layer.x, layer.y, layer.x + layer.width, layer.y + layer.height),
                                12f, 12f, paint
                            )
                        }
                    }
                    canvas.restore()
                }

                is StudioLayer.Element -> {
                    val paint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
                        color = layer.tintColor.toArgb()
                        style = Paint.Style.STROKE
                        strokeWidth = 5f
                        alpha = (layer.opacity * 255).toInt().coerceIn(0, 255)
                    }
                    val s = layer.size
                    canvas.save()
                    canvas.rotate(layer.rotation, layer.x + s / 2f, layer.y + s / 2f)
                    canvas.drawRoundRect(
                        RectF(layer.x, layer.y, layer.x + s, layer.y + s),
                        16f, 16f, paint
                    )
                    canvas.restore()
                }
            }
        }

        // 7. Frames
        if (frameStyle != FrameStyle.NONE) {
            val framePaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
                style = Paint.Style.STROKE
            }
            when (frameStyle) {
                FrameStyle.MINIMAL_WHITE -> {
                    framePaint.color = AndroidColor.WHITE
                    framePaint.strokeWidth = 24f
                    canvas.drawRect(12f, 12f, width - 12f, height - 12f, framePaint)
                }
                FrameStyle.MINIMAL_BLACK -> {
                    framePaint.color = AndroidColor.BLACK
                    framePaint.strokeWidth = 24f
                    canvas.drawRect(12f, 12f, width - 12f, height - 12f, framePaint)
                }
                FrameStyle.POLAROID -> {
                    val bgPaint = Paint().apply { color = AndroidColor.WHITE }
                    canvas.drawRect(0f, height - 120f, width.toFloat(), height.toFloat(), bgPaint)
                    canvas.drawRect(0f, 0f, 30f, height.toFloat(), bgPaint)
                    canvas.drawRect(width - 30f, 0f, width.toFloat(), height.toFloat(), bgPaint)
                    canvas.drawRect(0f, 0f, width.toFloat(), 30f, bgPaint)
                }
                FrameStyle.LUXURY_GOLD -> {
                    framePaint.color = AndroidColor.rgb(234, 179, 8)
                    framePaint.strokeWidth = 18f
                    canvas.drawRect(9f, 9f, width - 9f, height - 9f, framePaint)
                    framePaint.strokeWidth = 2f
                    canvas.drawRect(24f, 24f, width - 24f, height - 24f, framePaint)
                }
                FrameStyle.NEON_PURPLE -> {
                    framePaint.color = AndroidColor.rgb(168, 85, 247)
                    framePaint.strokeWidth = 14f
                    canvas.drawRect(7f, 7f, width - 7f, height - 7f, framePaint)
                }
                FrameStyle.FILM_BORDER -> {
                    val borderPaint = Paint().apply { color = AndroidColor.BLACK }
                    canvas.drawRect(0f, 0f, width.toFloat(), 40f, borderPaint)
                    canvas.drawRect(0f, height - 40f, width.toFloat(), height.toFloat(), borderPaint)
                }
                else -> {}
            }
        }

        return outputBitmap
    }

    /**
     * Saves exported Bitmap to local cache and returns the File.
     */
    fun saveBitmapToFile(context: Context, bitmap: Bitmap, format: Bitmap.CompressFormat, filename: String): File {
        val storageDir = context.getExternalFilesDir(Environment.DIRECTORY_PICTURES) ?: context.filesDir
        val file = File(storageDir, filename)
        FileOutputStream(file).use { out ->
            val quality = if (format == Bitmap.CompressFormat.PNG) 100 else 92
            bitmap.compress(format, quality, out)
            out.flush()
        }
        return file
    }

    fun saveBitmapToFile(context: Context, bitmap: Bitmap, filename: String): File {
        val format = if (filename.endsWith(".jpg", ignoreCase = true) || filename.endsWith(".jpeg", ignoreCase = true)) {
            Bitmap.CompressFormat.JPEG
        } else {
            Bitmap.CompressFormat.PNG
        }
        return saveBitmapToFile(context, bitmap, format, filename)
    }
}
