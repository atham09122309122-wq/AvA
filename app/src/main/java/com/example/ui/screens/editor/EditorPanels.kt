package com.example.ui.screens.editor

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Brush
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.ContentCopy
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Flip
import androidx.compose.material.icons.filled.FormatBold
import androidx.compose.material.icons.filled.FormatItalic
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.LockOpen
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.RotateLeft
import androidx.compose.material.icons.filled.RotateRight
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.TextButton
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Slider
import androidx.compose.material3.SliderDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.theme.AvaCard
import com.example.ui.theme.AvaCardBorder
import com.example.ui.theme.AvaPurpleContainer
import com.example.ui.theme.AvaPurpleLight
import com.example.ui.theme.AvaPurplePrimary
import com.example.ui.theme.AvaTextPrimary
import com.example.ui.theme.AvaTextSecondary
import com.example.util.BlurType
import com.example.util.CreativeEffect
import com.example.util.FilterCategory
import com.example.util.FilterPreset
import com.example.util.FrameStyle
import com.example.util.ImageAdjustments
import com.example.util.LightingStyle
import com.example.util.StudioLayer
import kotlin.math.roundToInt

// -------------------------------------------------------------
// Slider Component with Value and Reset
// -------------------------------------------------------------
@Composable
fun CompactSlider(
    label: String,
    value: Float,
    range: ClosedFloatingPointRange<Float>,
    onValueChange: (Float) -> Unit,
    onReset: () -> Unit = { onValueChange(0f) },
    valueFormat: (Float) -> String = { "%.1f".format(it) },
    modifier: Modifier = Modifier
) {
    Column(modifier = modifier.fillMaxWidth().padding(vertical = 2.dp)) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(text = label, color = AvaTextPrimary, fontSize = 11.sp, fontWeight = FontWeight.Medium)
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text(text = valueFormat(value), color = AvaPurpleLight, fontSize = 11.sp, fontWeight = FontWeight.Bold)
                Spacer(modifier = Modifier.width(6.dp))
                Icon(
                    imageVector = Icons.Default.Refresh,
                    contentDescription = "Reset",
                    tint = AvaTextSecondary.copy(alpha = 0.7f),
                    modifier = Modifier
                        .size(13.dp)
                        .clickable { onReset() }
                )
            }
        }
        Slider(
            value = value,
            onValueChange = onValueChange,
            valueRange = range,
            colors = SliderDefaults.colors(
                thumbColor = AvaPurpleLight,
                activeTrackColor = AvaPurplePrimary,
                inactiveTrackColor = Color(0xFF2C283E)
            ),
            modifier = Modifier.height(24.dp)
        )
    }
}

// -------------------------------------------------------------
// 1. Adjust Panel (Grouped into Light, Color, Details, Appearance)
// -------------------------------------------------------------
@Composable
fun AdjustPanel(
    adjustments: ImageAdjustments,
    onAdjustmentsChange: (ImageAdjustments) -> Unit
) {
    var selectedGroup by remember { mutableIntStateOf(0) }
    val groups = listOf("Light", "Color", "Details", "Style")

    Column(modifier = Modifier.fillMaxWidth()) {
        // Group Tabs
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 6.dp),
            horizontalArrangement = Arrangement.spacedBy(6.dp)
        ) {
            groups.forEachIndexed { index, groupName ->
                val isSel = selectedGroup == index
                Box(
                    modifier = Modifier
                        .weight(1f)
                        .clip(RoundedCornerShape(8.dp))
                        .background(if (isSel) AvaPurplePrimary else AvaCard)
                        .clickable { selectedGroup = index }
                        .padding(vertical = 5.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = groupName,
                        color = if (isSel) Color.White else AvaTextSecondary,
                        fontSize = 11.sp,
                        fontWeight = if (isSel) FontWeight.Bold else FontWeight.Normal
                    )
                }
            }
        }

        when (selectedGroup) {
            0 -> {
                // LIGHT
                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                    CompactSlider(
                        label = "Exposure",
                        value = adjustments.exposure,
                        range = -50f..50f,
                        onValueChange = { onAdjustmentsChange(adjustments.copy(exposure = it)) },
                        modifier = Modifier.weight(1f)
                    )
                    CompactSlider(
                        label = "Brightness",
                        value = adjustments.brightness,
                        range = -100f..100f,
                        onValueChange = { onAdjustmentsChange(adjustments.copy(brightness = it)) },
                        modifier = Modifier.weight(1f)
                    )
                }
                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                    CompactSlider(
                        label = "Contrast",
                        value = adjustments.contrast,
                        range = 0.5f..2.5f,
                        onValueChange = { onAdjustmentsChange(adjustments.copy(contrast = it)) },
                        onReset = { onAdjustmentsChange(adjustments.copy(contrast = 1f)) },
                        modifier = Modifier.weight(1f)
                    )
                    CompactSlider(
                        label = "Highlights",
                        value = adjustments.highlights,
                        range = -50f..50f,
                        onValueChange = { onAdjustmentsChange(adjustments.copy(highlights = it)) },
                        modifier = Modifier.weight(1f)
                    )
                }
                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                    CompactSlider(
                        label = "Shadows",
                        value = adjustments.shadows,
                        range = -50f..50f,
                        onValueChange = { onAdjustmentsChange(adjustments.copy(shadows = it)) },
                        modifier = Modifier.weight(1f)
                    )
                    CompactSlider(
                        label = "Whites",
                        value = adjustments.whites,
                        range = -50f..50f,
                        onValueChange = { onAdjustmentsChange(adjustments.copy(whites = it)) },
                        modifier = Modifier.weight(1f)
                    )
                }
            }
            1 -> {
                // COLOR
                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                    CompactSlider(
                        label = "Saturation",
                        value = adjustments.saturation,
                        range = 0f..2.5f,
                        onValueChange = { onAdjustmentsChange(adjustments.copy(saturation = it)) },
                        onReset = { onAdjustmentsChange(adjustments.copy(saturation = 1f)) },
                        modifier = Modifier.weight(1f)
                    )
                    CompactSlider(
                        label = "Vibrance",
                        value = adjustments.vibrance,
                        range = -50f..50f,
                        onValueChange = { onAdjustmentsChange(adjustments.copy(vibrance = it)) },
                        modifier = Modifier.weight(1f)
                    )
                }
                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                    CompactSlider(
                        label = "Temperature",
                        value = adjustments.temperature,
                        range = -50f..50f,
                        onValueChange = { onAdjustmentsChange(adjustments.copy(temperature = it)) },
                        modifier = Modifier.weight(1f)
                    )
                    CompactSlider(
                        label = "Tint",
                        value = adjustments.tint,
                        range = -50f..50f,
                        onValueChange = { onAdjustmentsChange(adjustments.copy(tint = it)) },
                        modifier = Modifier.weight(1f)
                    )
                }
                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                    CompactSlider(
                        label = "Fade",
                        value = adjustments.fade,
                        range = 0f..50f,
                        onValueChange = { onAdjustmentsChange(adjustments.copy(fade = it)) },
                        modifier = Modifier.weight(1f)
                    )
                    CompactSlider(
                        label = "Red Balance",
                        value = adjustments.colorBalanceR,
                        range = -30f..30f,
                        onValueChange = { onAdjustmentsChange(adjustments.copy(colorBalanceR = it)) },
                        modifier = Modifier.weight(1f)
                    )
                }
            }
            2 -> {
                // DETAILS
                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                    CompactSlider(
                        label = "Sharpness",
                        value = adjustments.sharpness,
                        range = 0f..100f,
                        onValueChange = { onAdjustmentsChange(adjustments.copy(sharpness = it)) },
                        modifier = Modifier.weight(1f)
                    )
                    CompactSlider(
                        label = "Clarity",
                        value = adjustments.clarity,
                        range = 0f..100f,
                        onValueChange = { onAdjustmentsChange(adjustments.copy(clarity = it)) },
                        modifier = Modifier.weight(1f)
                    )
                }
                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                    CompactSlider(
                        label = "Grain",
                        value = adjustments.grain,
                        range = 0f..100f,
                        onValueChange = { onAdjustmentsChange(adjustments.copy(grain = it)) },
                        modifier = Modifier.weight(1f)
                    )
                    CompactSlider(
                        label = "Vignette",
                        value = adjustments.vignette,
                        range = 0f..100f,
                        onValueChange = { onAdjustmentsChange(adjustments.copy(vignette = it)) },
                        modifier = Modifier.weight(1f)
                    )
                }
            }
            3 -> {
                // APPEARANCE
                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                    CompactSlider(
                        label = "Opacity",
                        value = adjustments.opacity,
                        range = 0.1f..1.0f,
                        onValueChange = { onAdjustmentsChange(adjustments.copy(opacity = it)) },
                        onReset = { onAdjustmentsChange(adjustments.copy(opacity = 1f)) },
                        modifier = Modifier.weight(1f)
                    )
                    CompactSlider(
                        label = "Corner Radius",
                        value = adjustments.cornerRadius,
                        range = 0f..60f,
                        onValueChange = { onAdjustmentsChange(adjustments.copy(cornerRadius = it)) },
                        modifier = Modifier.weight(1f)
                    )
                }
                CompactSlider(
                    label = "Border Thickness",
                    value = adjustments.borderThickness,
                    range = 0f..30f,
                    onValueChange = { onAdjustmentsChange(adjustments.copy(borderThickness = it)) }
                )
            }
        }
    }
}

// -------------------------------------------------------------
// 2. Filters Panel (7 Categories + Intensity)
// -------------------------------------------------------------
@Composable
fun FilterPanel(
    activeFilter: FilterPreset,
    filterIntensity: Float,
    onFilterSelect: (FilterPreset) -> Unit,
    onIntensityChange: (Float) -> Unit
) {
    var selectedCategory by remember { mutableStateOf(FilterCategory.NATURAL) }

    Column(modifier = Modifier.fillMaxWidth()) {
        // Filter Intensity Slider
        if (activeFilter != FilterPreset.NONE) {
            CompactSlider(
                label = "Filter Intensity",
                value = filterIntensity * 100f,
                range = 0f..100f,
                onValueChange = { onIntensityChange(it / 100f) },
                valueFormat = { "${it.roundToInt()}%" }
            )
            Spacer(modifier = Modifier.height(4.dp))
        }

        // Category Pills
        LazyRow(
            horizontalArrangement = Arrangement.spacedBy(6.dp),
            contentPadding = PaddingValues(vertical = 4.dp)
        ) {
            items(FilterCategory.values()) { cat ->
                val isSel = selectedCategory == cat
                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(8.dp))
                        .background(if (isSel) AvaPurplePrimary else AvaCard)
                        .clickable { selectedCategory = cat }
                        .padding(horizontal = 10.dp, vertical = 5.dp)
                ) {
                    Text(
                        text = cat.title,
                        color = if (isSel) Color.White else AvaTextSecondary,
                        fontSize = 11.sp,
                        fontWeight = if (isSel) FontWeight.Bold else FontWeight.Normal
                    )
                }
            }
        }

        // Preset Thumbnails in selected Category
        val presets = FilterPreset.values().filter { it.category == selectedCategory || it == FilterPreset.NONE }
        LazyRow(
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            contentPadding = PaddingValues(vertical = 6.dp)
        ) {
            items(presets) { preset ->
                val isSel = activeFilter == preset
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = Modifier.clickable { onFilterSelect(preset) }
                ) {
                    Box(
                        modifier = Modifier
                            .size(54.dp)
                            .clip(RoundedCornerShape(10.dp))
                            .background(
                                when (preset) {
                                    FilterPreset.NONE -> Color(0xFF2C2D35)
                                    FilterPreset.NATURAL, FilterPreset.CLEAN -> Color(0xFF384358)
                                    FilterPreset.WARM_NATURAL, FilterPreset.WARM_STYLE, FilterPreset.GOLDEN -> Color(0xFF634125)
                                    FilterPreset.COOL_NATURAL, FilterPreset.COOL_STYLE, FilterPreset.DEEP_BLUE -> Color(0xFF1E3A5F)
                                    FilterPreset.FASHION_EDITORIAL, FilterPreset.LUXURY_EDITORIAL -> Color(0xFF4C2A48)
                                    FilterPreset.URBAN, FilterPreset.STREETWEAR -> Color(0xFF3F3D56)
                                    FilterPreset.CINEMATIC, FilterPreset.TEAL_AND_ORANGE -> Color(0xFF1F4E5B)
                                    FilterPreset.CLASSIC_BW, FilterPreset.HIGH_CONTRAST_BW, FilterPreset.SOFT_BW -> Color(0xFF222222)
                                    FilterPreset.PURPLE_MOOD -> Color(0xFF581C87)
                                    else -> Color(0xFF272735)
                                }
                            )
                            .border(
                                width = if (isSel) 2.dp else 1.dp,
                                color = if (isSel) AvaPurpleLight else AvaCardBorder,
                                shape = RoundedCornerShape(10.dp)
                            ),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = preset.title.take(3).uppercase(),
                            color = if (isSel) Color.White else AvaTextSecondary,
                            fontSize = 10.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = preset.title,
                        color = if (isSel) AvaPurpleLight else AvaTextSecondary,
                        fontSize = 10.sp,
                        fontWeight = if (isSel) FontWeight.Bold else FontWeight.Normal,
                        maxLines = 1
                    )
                }
            }
        }
    }
}

// -------------------------------------------------------------
// 3. Blur & Selective Blur Brush Panel
// -------------------------------------------------------------
@Composable
fun BlurPanel(
    activeBlurType: BlurType,
    blurIntensity: Float,
    brushSize: Float,
    isBlurBrushMode: Boolean,
    isBlurEraser: Boolean,
    onBlurTypeChange: (BlurType) -> Unit,
    onIntensityChange: (Float) -> Unit,
    onBrushSizeChange: (Float) -> Unit,
    onToggleBrushMode: (Boolean) -> Unit,
    onToggleEraser: (Boolean) -> Unit,
    onClearBlurStrokes: () -> Unit
) {
    Column(modifier = Modifier.fillMaxWidth()) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(6.dp)
        ) {
            BlurType.values().forEach { bType ->
                val isSel = activeBlurType == bType
                Box(
                    modifier = Modifier
                        .weight(1f)
                        .clip(RoundedCornerShape(8.dp))
                        .background(if (isSel) AvaPurplePrimary else AvaCard)
                        .clickable { onBlurTypeChange(bType) }
                        .padding(vertical = 5.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = bType.title.split(" ").first(),
                        color = if (isSel) Color.White else AvaTextSecondary,
                        fontSize = 10.sp,
                        fontWeight = if (isSel) FontWeight.Bold else FontWeight.Normal,
                        maxLines = 1
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(6.dp))

        CompactSlider(
            label = "Blur Intensity",
            value = blurIntensity,
            range = 0f..50f,
            onValueChange = onIntensityChange
        )

        // If Selective Blur Brush is chosen
        if (activeBlurType == BlurType.SELECTIVE) {
            Spacer(modifier = Modifier.height(4.dp))
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    Button(
                        onClick = { onToggleBrushMode(!isBlurBrushMode) },
                        colors = ButtonDefaults.buttonColors(
                            containerColor = if (isBlurBrushMode) AvaPurplePrimary else AvaCard
                        ),
                        shape = RoundedCornerShape(8.dp),
                        contentPadding = PaddingValues(horizontal = 10.dp, vertical = 4.dp),
                        modifier = Modifier.height(32.dp)
                    ) {
                        Text(if (isBlurBrushMode) "Paint Blur: ON" else "Paint Blur", fontSize = 11.sp)
                    }

                    Button(
                        onClick = { onToggleEraser(!isBlurEraser) },
                        colors = ButtonDefaults.buttonColors(
                            containerColor = if (isBlurEraser) Color(0xFFEF4444) else AvaCard
                        ),
                        shape = RoundedCornerShape(8.dp),
                        contentPadding = PaddingValues(horizontal = 10.dp, vertical = 4.dp),
                        modifier = Modifier.height(32.dp)
                    ) {
                        Text(if (isBlurEraser) "Eraser: ON" else "Eraser", fontSize = 11.sp)
                    }
                }

                TextButton(onClick = onClearBlurStrokes) {
                    Text("Clear Mask", color = Color(0xFFF87171), fontSize = 11.sp)
                }
            }

            CompactSlider(
                label = "Brush Radius",
                value = brushSize,
                range = 10f..100f,
                onValueChange = onBrushSizeChange
            )
        }
    }
}

// -------------------------------------------------------------
// 4. Drawing Tools Panel
// -------------------------------------------------------------
@Composable
fun DrawPanel(
    brushColor: Color,
    brushSize: Float,
    brushOpacity: Float,
    isEraser: Boolean,
    onColorChange: (Color) -> Unit,
    onSizeChange: (Float) -> Unit,
    onOpacityChange: (Float) -> Unit,
    onToggleEraser: (Boolean) -> Unit,
    onClearDrawing: () -> Unit
) {
    val palette = listOf(
        Color.White,
        Color(0xFF8B5CF6),
        Color(0xFFEC4899),
        Color(0xFF38BDF8),
        Color(0xFFEAB308),
        Color(0xFF10B981),
        Color(0xFFEF4444),
        Color.Black
    )

    Column(modifier = Modifier.fillMaxWidth()) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Palette
            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                palette.forEach { c ->
                    val isSel = brushColor == c && !isEraser
                    Box(
                        modifier = Modifier
                            .size(26.dp)
                            .clip(CircleShape)
                            .background(c)
                            .border(
                                width = if (isSel) 2.5.dp else 1.dp,
                                color = if (isSel) AvaPurpleLight else Color(0xFF4C4863),
                                shape = CircleShape
                            )
                            .clickable {
                                onColorChange(c)
                                onToggleEraser(false)
                            }
                    )
                }
            }

            Row(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                Button(
                    onClick = { onToggleEraser(!isEraser) },
                    colors = ButtonDefaults.buttonColors(
                        containerColor = if (isEraser) Color(0xFFEF4444) else AvaCard
                    ),
                    shape = RoundedCornerShape(8.dp),
                    contentPadding = PaddingValues(horizontal = 8.dp, vertical = 2.dp),
                    modifier = Modifier.height(28.dp)
                ) {
                    Text(if (isEraser) "Eraser" else "Brush", fontSize = 11.sp)
                }

                IconButton(onClick = onClearDrawing, modifier = Modifier.size(28.dp)) {
                    Icon(Icons.Default.Delete, contentDescription = "Clear", tint = Color(0xFFF87171), modifier = Modifier.size(16.dp))
                }
            }
        }

        Spacer(modifier = Modifier.height(6.dp))

        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(10.dp)) {
            CompactSlider(
                label = "Brush Size",
                value = brushSize,
                range = 2f..50f,
                onValueChange = onSizeChange,
                modifier = Modifier.weight(1f)
            )
            CompactSlider(
                label = "Opacity",
                value = brushOpacity,
                range = 0.1f..1.0f,
                onValueChange = onOpacityChange,
                modifier = Modifier.weight(1f)
            )
        }
    }
}

// -------------------------------------------------------------
// 5. Crop & Transform Panel
// -------------------------------------------------------------
@Composable
fun CropPanel(
    activeRatio: String,
    onRatioChange: (String) -> Unit,
    onRotateLeft: () -> Unit,
    onRotateRight: () -> Unit,
    onFlipH: () -> Unit,
    onFlipV: () -> Unit
) {
    val ratios = listOf("1:1", "4:5", "3:4", "9:16", "16:9", "Free")

    Column(modifier = Modifier.fillMaxWidth()) {
        Text("Aspect Ratio", color = AvaTextPrimary, fontSize = 11.sp, fontWeight = FontWeight.Medium)
        Spacer(modifier = Modifier.height(4.dp))
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(6.dp)
        ) {
            ratios.forEach { ratio ->
                val isSel = activeRatio == ratio
                Box(
                    modifier = Modifier
                        .weight(1f)
                        .clip(RoundedCornerShape(8.dp))
                        .background(if (isSel) AvaPurplePrimary else AvaCard)
                        .clickable { onRatioChange(ratio) }
                        .padding(vertical = 5.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = ratio,
                        color = if (isSel) Color.White else AvaTextSecondary,
                        fontSize = 11.sp,
                        fontWeight = if (isSel) FontWeight.Bold else FontWeight.Normal
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(8.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text("Rotate & Flip", color = AvaTextPrimary, fontSize = 11.sp, fontWeight = FontWeight.Medium)

            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                IconButton(onClick = onRotateLeft, modifier = Modifier.size(32.dp)) {
                    Icon(Icons.Default.RotateLeft, contentDescription = "Rotate Left", tint = AvaPurpleLight)
                }
                IconButton(onClick = onRotateRight, modifier = Modifier.size(32.dp)) {
                    Icon(Icons.Default.RotateRight, contentDescription = "Rotate Right", tint = AvaPurpleLight)
                }
                IconButton(onClick = onFlipH, modifier = Modifier.size(32.dp)) {
                    Icon(Icons.Default.Flip, contentDescription = "Flip H", tint = AvaPurpleLight)
                }
                IconButton(onClick = onFlipV, modifier = Modifier.size(32.dp)) {
                    Icon(Icons.Default.Flip, contentDescription = "Flip V", tint = AvaTextSecondary)
                }
            }
        }
    }
}

// -------------------------------------------------------------
// 6. Professional Text Editor Panel
// -------------------------------------------------------------
@Composable
fun TextPanel(
    selectedTextLayer: StudioLayer.Text?,
    onAddText: () -> Unit,
    onUpdateText: (StudioLayer.Text) -> Unit,
    onDeleteText: (String) -> Unit
) {
    if (selectedTextLayer == null) {
        Column(
            modifier = Modifier.fillMaxWidth().padding(vertical = 8.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text("Select an existing text on canvas or add a new headline", color = AvaTextSecondary, fontSize = 12.sp)
            Spacer(modifier = Modifier.height(8.dp))
            Button(
                onClick = onAddText,
                shape = RoundedCornerShape(10.dp),
                colors = ButtonDefaults.buttonColors(containerColor = AvaPurplePrimary)
            ) {
                Icon(Icons.Default.Add, contentDescription = null, modifier = Modifier.size(16.dp))
                Spacer(modifier = Modifier.width(6.dp))
                Text("Add Text Layer", color = Color.White, fontSize = 12.sp, fontWeight = FontWeight.Bold)
            }
        }
    } else {
        Column(modifier = Modifier.fillMaxWidth()) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                OutlinedTextField(
                    value = selectedTextLayer.content,
                    onValueChange = { onUpdateText(selectedTextLayer.copy(content = it)) },
                    modifier = Modifier.weight(1f).height(46.dp),
                    shape = RoundedCornerShape(8.dp),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = AvaPurplePrimary,
                        unfocusedBorderColor = AvaCardBorder,
                        focusedTextColor = Color.White,
                        unfocusedTextColor = Color.White
                    ),
                    singleLine = true
                )

                Spacer(modifier = Modifier.width(8.dp))

                IconButton(onClick = { onDeleteText(selectedTextLayer.id) }) {
                    Icon(Icons.Default.Delete, contentDescription = "Delete", tint = Color(0xFFF87171))
                }
            }

            Spacer(modifier = Modifier.height(6.dp))

            // Font Families
            val fonts = listOf("Modern", "Editorial", "Bold", "Luxury", "Streetwear", "Serif", "Arabic")
            LazyRow(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                items(fonts) { fontName ->
                    val isSel = selectedTextLayer.fontFamily == fontName
                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(6.dp))
                            .background(if (isSel) AvaPurplePrimary else AvaCard)
                            .clickable { onUpdateText(selectedTextLayer.copy(fontFamily = fontName)) }
                            .padding(horizontal = 8.dp, vertical = 4.dp)
                    ) {
                        Text(text = fontName, color = if (isSel) Color.White else AvaTextSecondary, fontSize = 10.sp)
                    }
                }
            }

            Spacer(modifier = Modifier.height(6.dp))

            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                CompactSlider(
                    label = "Font Size",
                    value = selectedTextLayer.fontSize,
                    range = 14f..100f,
                    onValueChange = { onUpdateText(selectedTextLayer.copy(fontSize = it)) },
                    modifier = Modifier.weight(1f)
                )
                CompactSlider(
                    label = "Opacity",
                    value = selectedTextLayer.opacity,
                    range = 0.1f..1.0f,
                    onValueChange = { onUpdateText(selectedTextLayer.copy(opacity = it)) },
                    modifier = Modifier.weight(1f)
                )
            }
        }
    }
}

// -------------------------------------------------------------
// 7. Elements & Shapes Panel
// -------------------------------------------------------------
@Composable
fun ElementsPanel(
    onAddShape: (String) -> Unit,
    onAddElement: (String) -> Unit
) {
    val shapes = listOf("RECTANGLE", "CIRCLE", "LINE", "ARROW")
    val graphics = listOf("STAR", "BADGE", "FRAME", "STAMP")

    Column(modifier = Modifier.fillMaxWidth()) {
        Text("Shapes & Overlays", color = AvaTextPrimary, fontSize = 11.sp, fontWeight = FontWeight.Medium)
        Spacer(modifier = Modifier.height(4.dp))
        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            shapes.forEach { shapeKind ->
                Box(
                    modifier = Modifier
                        .weight(1f)
                        .clip(RoundedCornerShape(8.dp))
                        .background(AvaCard)
                        .border(1.dp, AvaCardBorder, RoundedCornerShape(8.dp))
                        .clickable { onAddShape(shapeKind) }
                        .padding(vertical = 8.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Text(text = shapeKind, color = AvaTextPrimary, fontSize = 10.sp, fontWeight = FontWeight.Bold)
                }
            }
        }

        Spacer(modifier = Modifier.height(8.dp))
        Text("Creative Graphics", color = AvaTextPrimary, fontSize = 11.sp, fontWeight = FontWeight.Medium)
        Spacer(modifier = Modifier.height(4.dp))
        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            graphics.forEach { gKind ->
                Box(
                    modifier = Modifier
                        .weight(1f)
                        .clip(RoundedCornerShape(8.dp))
                        .background(AvaPurpleContainer)
                        .border(1.dp, AvaPurplePrimary.copy(alpha = 0.4f), RoundedCornerShape(8.dp))
                        .clickable { onAddElement(gKind) }
                        .padding(vertical = 8.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Text(text = gKind, color = AvaPurpleLight, fontSize = 10.sp, fontWeight = FontWeight.Bold)
                }
            }
        }
    }
}

// -------------------------------------------------------------
// 8. Layers Manager Panel
// -------------------------------------------------------------
@Composable
fun LayersPanel(
    layers: List<StudioLayer>,
    selectedLayerId: String?,
    onSelectLayer: (String) -> Unit,
    onToggleVisibility: (String) -> Unit,
    onToggleLock: (String) -> Unit,
    onDeleteLayer: (String) -> Unit,
    onMoveLayerUp: (Int) -> Unit,
    onMoveLayerDown: (Int) -> Unit
) {
    if (layers.isEmpty()) {
        Box(
            modifier = Modifier.fillMaxWidth().padding(16.dp),
            contentAlignment = Alignment.Center
        ) {
            Text("No additional layers added. Base Photo is active.", color = AvaTextSecondary, fontSize = 12.sp)
        }
    } else {
        Column(modifier = Modifier.fillMaxWidth(), verticalArrangement = Arrangement.spacedBy(6.dp)) {
            layers.forEachIndexed { index, layer ->
                val isSel = layer.id == selectedLayerId
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(8.dp))
                        .background(if (isSel) AvaPurpleContainer else AvaCard)
                        .border(1.dp, if (isSel) AvaPurplePrimary else AvaCardBorder, RoundedCornerShape(8.dp))
                        .clickable { onSelectLayer(layer.id) }
                        .padding(horizontal = 8.dp, vertical = 4.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text(
                            text = "#${index + 1} ${layer.name}",
                            color = if (isSel) Color.White else AvaTextPrimary,
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }

                    Row(verticalAlignment = Alignment.CenterVertically) {
                        IconButton(onClick = { onToggleVisibility(layer.id) }, modifier = Modifier.size(24.dp)) {
                            Icon(
                                imageVector = if (layer.isVisible) Icons.Default.Visibility else Icons.Default.VisibilityOff,
                                contentDescription = "Visibility",
                                tint = if (layer.isVisible) AvaPurpleLight else AvaTextSecondary,
                                modifier = Modifier.size(16.dp)
                            )
                        }

                        IconButton(onClick = { onToggleLock(layer.id) }, modifier = Modifier.size(24.dp)) {
                            Icon(
                                imageVector = if (layer.isLocked) Icons.Default.Lock else Icons.Default.LockOpen,
                                contentDescription = "Lock",
                                tint = AvaTextSecondary,
                                modifier = Modifier.size(16.dp)
                            )
                        }

                        IconButton(onClick = { onMoveLayerUp(index) }, modifier = Modifier.size(24.dp)) {
                            Icon(Icons.Default.KeyboardArrowUp, contentDescription = "Up", tint = AvaTextSecondary, modifier = Modifier.size(18.dp))
                        }

                        IconButton(onClick = { onMoveLayerDown(index) }, modifier = Modifier.size(24.dp)) {
                            Icon(Icons.Default.KeyboardArrowDown, contentDescription = "Down", tint = AvaTextSecondary, modifier = Modifier.size(18.dp))
                        }

                        IconButton(onClick = { onDeleteLayer(layer.id) }, modifier = Modifier.size(24.dp)) {
                            Icon(Icons.Default.Delete, contentDescription = "Delete", tint = Color(0xFFF87171), modifier = Modifier.size(16.dp))
                        }
                    }
                }
            }
        }
    }
}

// -------------------------------------------------------------
// 9. Frames Panel
// -------------------------------------------------------------
@Composable
fun FramesPanel(
    activeFrame: FrameStyle,
    onFrameSelect: (FrameStyle) -> Unit
) {
    Column(modifier = Modifier.fillMaxWidth()) {
        Text("Studio Frames & Borders", color = AvaTextPrimary, fontSize = 11.sp, fontWeight = FontWeight.Medium)
        Spacer(modifier = Modifier.height(6.dp))
        LazyRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            items(FrameStyle.values()) { fStyle ->
                val isSel = activeFrame == fStyle
                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(8.dp))
                        .background(if (isSel) AvaPurplePrimary else AvaCard)
                        .border(1.dp, if (isSel) AvaPurpleLight else AvaCardBorder, RoundedCornerShape(8.dp))
                        .clickable { onFrameSelect(fStyle) }
                        .padding(horizontal = 12.dp, vertical = 7.dp)
                ) {
                    Text(
                        text = fStyle.title,
                        color = if (isSel) Color.White else AvaTextSecondary,
                        fontSize = 11.sp,
                        fontWeight = if (isSel) FontWeight.Bold else FontWeight.Normal
                    )
                }
            }
        }
    }
}

// -------------------------------------------------------------
// 10. Lighting Panel
// -------------------------------------------------------------
@Composable
fun LightingPanel(
    activeLighting: LightingStyle,
    lightingOpacity: Float,
    onLightingSelect: (LightingStyle) -> Unit,
    onOpacityChange: (Float) -> Unit
) {
    Column(modifier = Modifier.fillMaxWidth()) {
        CompactSlider(
            label = "Lighting Opacity",
            value = lightingOpacity * 100f,
            range = 0f..100f,
            onValueChange = { onOpacityChange(it / 100f) },
            valueFormat = { "${it.roundToInt()}%" }
        )

        Spacer(modifier = Modifier.height(4.dp))

        LazyRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            items(LightingStyle.values()) { lStyle ->
                val isSel = activeLighting == lStyle
                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(8.dp))
                        .background(if (isSel) AvaPurplePrimary else AvaCard)
                        .border(1.dp, if (isSel) AvaPurpleLight else AvaCardBorder, RoundedCornerShape(8.dp))
                        .clickable { onLightingSelect(lStyle) }
                        .padding(horizontal = 12.dp, vertical = 6.dp)
                ) {
                    Text(
                        text = lStyle.title,
                        color = if (isSel) Color.White else AvaTextSecondary,
                        fontSize = 11.sp,
                        fontWeight = if (isSel) FontWeight.Bold else FontWeight.Normal
                    )
                }
            }
        }
    }
}

// -------------------------------------------------------------
// 11. Creative Effects Panel
// -------------------------------------------------------------
@Composable
fun EffectsPanel(
    activeEffect: CreativeEffect,
    onEffectSelect: (CreativeEffect) -> Unit
) {
    Column(modifier = Modifier.fillMaxWidth()) {
        Text("Creative Effects Library", color = AvaTextPrimary, fontSize = 11.sp, fontWeight = FontWeight.Medium)
        Spacer(modifier = Modifier.height(6.dp))
        LazyRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            items(CreativeEffect.values()) { eff ->
                val isSel = activeEffect == eff
                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(8.dp))
                        .background(if (isSel) AvaPurplePrimary else AvaCard)
                        .border(1.dp, if (isSel) AvaPurpleLight else AvaCardBorder, RoundedCornerShape(8.dp))
                        .clickable { onEffectSelect(eff) }
                        .padding(horizontal = 12.dp, vertical = 7.dp)
                ) {
                    Text(
                        text = eff.title,
                        color = if (isSel) Color.White else AvaTextSecondary,
                        fontSize = 11.sp,
                        fontWeight = if (isSel) FontWeight.Bold else FontWeight.Normal
                    )
                }
            }
        }
    }
}
