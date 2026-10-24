package com.example.ui.screens.editor

import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import androidx.activity.compose.BackHandler
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.expandVertically
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.gestures.detectTransformGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.Redo
import androidx.compose.material.icons.automirrored.filled.Undo
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.BlurOn
import androidx.compose.material.icons.filled.Brush
import androidx.compose.material.icons.filled.CenterFocusStrong
import androidx.compose.material.icons.filled.ColorLens
import androidx.compose.material.icons.filled.Compare
import androidx.compose.material.icons.filled.Crop
import androidx.compose.material.icons.filled.CropPortrait
import androidx.compose.material.icons.filled.Details
import androidx.compose.material.icons.filled.Download
import androidx.compose.material.icons.filled.Face
import androidx.compose.material.icons.filled.Layers
import androidx.compose.material.icons.filled.Palette
import androidx.compose.material.icons.filled.Save
import androidx.compose.material.icons.filled.ShapeLine
import androidx.compose.material.icons.filled.Share
import androidx.compose.material.icons.filled.TextFields
import androidx.compose.material.icons.filled.Tune
import androidx.compose.material.icons.filled.Wallpaper
import androidx.compose.material.icons.filled.WbSunny
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.draw.scale
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.content.FileProvider
import com.example.data.model.ProjectEntity
import com.example.ui.components.UnsavedChangesDialog
import com.example.ui.theme.AvaBlack
import com.example.ui.theme.AvaCard
import com.example.ui.theme.AvaCardBorder
import com.example.ui.theme.AvaDivider
import com.example.ui.theme.AvaPurpleContainer
import com.example.ui.theme.AvaPurpleLight
import com.example.ui.theme.AvaPurplePrimary
import com.example.ui.theme.AvaSurface
import com.example.ui.theme.AvaTextPrimary
import com.example.ui.theme.AvaTextSecondary
import com.example.ui.viewmodel.MainViewModel
import com.example.util.BlurBrushPoint
import com.example.util.BlurType
import com.example.util.CreativeEffect
import com.example.util.DemoArtworkProvider
import com.example.util.DrawPoint
import com.example.util.DrawStroke
import com.example.util.EditorEngine
import com.example.util.EditorSnapshot
import com.example.util.FilterPreset
import com.example.util.FrameStyle
import com.example.util.ImageAdjustments
import com.example.util.LightingStyle
import com.example.util.StudioLayer
import java.io.File
import java.util.UUID
import kotlin.math.roundToInt

enum class EditorCategory(val label: String, val icon: ImageVector) {
    ADJUST("Adjust", Icons.Default.Tune),
    FILTERS("Filters", Icons.Default.Palette),
    EFFECTS("Effects", Icons.Default.AutoAwesome),
    BLUR("Blur", Icons.Default.BlurOn),
    RETOUCH("Retouch", Icons.Default.Face),
    CROP("Crop", Icons.Default.Crop),
    TEXT("Text", Icons.Default.TextFields),
    DRAW("Draw", Icons.Default.Brush),
    ELEMENTS("Elements", Icons.Default.ShapeLine),
    BACKGROUND("Background", Icons.Default.Wallpaper),
    LAYERS("Layers", Icons.Default.Layers),
    FRAMES("Frames", Icons.Default.CropPortrait),
    LIGHTING("Lighting", Icons.Default.WbSunny),
    COLOR("Color", Icons.Default.ColorLens),
    DETAILS("Details", Icons.Default.Details)
}

@Composable
fun ImageEditorScreen(
    viewModel: MainViewModel,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    val project = viewModel.activeProject.value

    var projectName by remember { mutableStateOf(project?.title ?: "AVA Creation") }
    var canvasRatio by remember { mutableStateOf(project?.aspectRatio ?: "1:1") }
    var canvasColor by remember { mutableStateOf(Color(0xFF09080E)) }

    // -------------------------------------------------------------
    // Editing State
    // -------------------------------------------------------------
    var adjustments by remember { mutableStateOf(ImageAdjustments()) }
    var activeFilter by remember { mutableStateOf(FilterPreset.NONE) }
    var filterIntensity by remember { mutableFloatStateOf(1f) }
    var activeEffect by remember { mutableStateOf(CreativeEffect.NONE) }
    var activeLighting by remember { mutableStateOf(LightingStyle.NONE) }
    var lightingOpacity by remember { mutableFloatStateOf(0.5f) }
    var activeFrame by remember { mutableStateOf(FrameStyle.NONE) }
    var activeBlurType by remember { mutableStateOf(BlurType.NONE) }
    var blurIntensity by remember { mutableFloatStateOf(0f) }

    // Transform states
    var rotationAngle by remember { mutableFloatStateOf(0f) }
    var flipH by remember { mutableStateOf(false) }
    var flipV by remember { mutableStateOf(false) }

    // Zoom & Pan for Workspace
    var zoomScale by remember { mutableFloatStateOf(1f) }
    var panOffset by remember { mutableStateOf(Offset.Zero) }

    // Before / After Comparison state
    var isComparingBefore by remember { mutableStateOf(false) }

    // Layers (Text, Shapes, Elements)
    val layers = remember { mutableStateListOf<StudioLayer>() }
    var selectedLayerId by remember { mutableStateOf<String?>(null) }

    // Drawing & Blur Brush states
    val drawStrokes = remember { mutableStateListOf<DrawStroke>() }
    var currentDrawColor by remember { mutableStateOf(Color.White) }
    var currentDrawSize by remember { mutableFloatStateOf(8f) }
    var currentDrawOpacity by remember { mutableFloatStateOf(1f) }
    var isDrawEraser by remember { mutableStateOf(false) }

    val blurStrokes = remember { mutableStateListOf<BlurBrushPoint>() }
    var blurBrushRadius by remember { mutableFloatStateOf(36f) }
    var isBlurBrushMode by remember { mutableStateOf(false) }
    var isBlurEraser by remember { mutableStateOf(false) }

    // Active Category Tool Panel
    var activeCategory by remember { mutableStateOf<EditorCategory?>(EditorCategory.ADJUST) }

    // Undo / Redo history stacks
    val undoStack = remember { mutableStateListOf<EditorSnapshot>() }
    val redoStack = remember { mutableStateListOf<EditorSnapshot>() }

    fun captureSnapshot(): EditorSnapshot {
        return EditorSnapshot(
            adjustments = adjustments,
            filter = activeFilter,
            filterIntensity = filterIntensity,
            blurType = activeBlurType,
            blurIntensity = blurIntensity,
            blurStrokes = blurStrokes.toList(),
            drawStrokes = drawStrokes.toList(),
            layers = layers.toList(),
            lightingStyle = activeLighting,
            lightingOpacity = lightingOpacity,
            frameStyle = activeFrame,
            creativeEffect = activeEffect,
            rotationAngle = rotationAngle,
            flipH = flipH,
            flipV = flipV,
            cropRatio = canvasRatio
        )
    }

    fun pushHistory() {
        undoStack.add(captureSnapshot())
        if (undoStack.size > 25) undoStack.removeAt(0)
        redoStack.clear()
        viewModel.hasUnsavedChanges.value = true
    }

    fun applySnapshot(snapshot: EditorSnapshot) {
        adjustments = snapshot.adjustments
        activeFilter = snapshot.filter
        filterIntensity = snapshot.filterIntensity
        activeBlurType = snapshot.blurType
        blurIntensity = snapshot.blurIntensity
        blurStrokes.clear()
        blurStrokes.addAll(snapshot.blurStrokes)
        drawStrokes.clear()
        drawStrokes.addAll(snapshot.drawStrokes)
        layers.clear()
        layers.addAll(snapshot.layers)
        activeLighting = snapshot.lightingStyle
        lightingOpacity = snapshot.lightingOpacity
        activeFrame = snapshot.frameStyle
        activeEffect = snapshot.creativeEffect
        rotationAngle = snapshot.rotationAngle
        flipH = snapshot.flipH
        flipV = snapshot.flipV
        canvasRatio = snapshot.cropRatio
    }

    // -------------------------------------------------------------
    // Base Bitmap Loading
    // -------------------------------------------------------------
    var baseBitmap by remember { mutableStateOf<Bitmap?>(null) }

    LaunchedEffect(project?.thumbnailUri) {
        val uriStr = project?.thumbnailUri
        if (uriStr != null) {
            if (uriStr.startsWith("demo://")) {
                val demoId = uriStr.removePrefix("demo://")
                val found = DemoArtworkProvider.demoArtworks.firstOrNull { it.id == demoId }
                if (found != null) {
                    baseBitmap = DemoArtworkProvider.generateDemoBitmap(found)
                    activeFilter = found.defaultFilter
                    adjustments = found.defaultAdjustments
                    activeLighting = found.defaultLighting
                    activeFrame = found.defaultFrame
                }
            } else {
                try {
                    val uri = Uri.parse(uriStr)
                    context.contentResolver.openInputStream(uri)?.use { stream ->
                        baseBitmap = BitmapFactory.decodeStream(stream)
                    }
                } catch (e: Exception) {
                    // Fallback to demo
                    val firstDemo = DemoArtworkProvider.demoArtworks.first()
                    baseBitmap = DemoArtworkProvider.generateDemoBitmap(firstDemo)
                }
            }
        } else {
            // Default first launch sample artwork
            val firstDemo = DemoArtworkProvider.demoArtworks.first()
            baseBitmap = DemoArtworkProvider.generateDemoBitmap(firstDemo)
        }
    }

    // Back handling
    var showExitDialog by remember { mutableStateOf(false) }
    BackHandler {
        if (viewModel.hasUnsavedChanges.value) {
            showExitDialog = true
        } else {
            viewModel.navigateBack()
        }
    }

    UnsavedChangesDialog(
        show = showExitDialog,
        onKeepEditing = { showExitDialog = false },
        onDiscard = {
            showExitDialog = false
            viewModel.confirmDiscardAndExit()
        }
    )

    // Export dropdown state
    var showExportMenu by remember { mutableStateOf(false) }

    fun doExport(format: Bitmap.CompressFormat, shareAfter: Boolean = false) {
        val bmp = baseBitmap ?: return
        val w = bmp.width
        val h = bmp.height
        val exported = EditorEngine.exportCanvasToBitmap(
            width = w,
            height = h,
            backgroundColor = canvasColor,
            baseBitmap = bmp,
            adjustments = adjustments,
            filter = activeFilter,
            filterIntensity = filterIntensity,
            creativeEffect = activeEffect,
            lightingStyle = activeLighting,
            lightingOpacity = lightingOpacity,
            frameStyle = activeFrame,
            blurType = activeBlurType,
            blurIntensity = blurIntensity,
            blurStrokes = blurStrokes,
            drawStrokes = drawStrokes,
            layers = layers,
            rotationAngle = rotationAngle,
            flipH = flipH,
            flipV = flipV
        )

        val ext = if (format == Bitmap.CompressFormat.PNG) "png" else "jpg"
        val filename = "AVA_${System.currentTimeMillis()}.$ext"
        val savedFile = EditorEngine.saveBitmapToFile(context, exported, format, filename)

        if (shareAfter) {
            val fileUri = FileProvider.getUriForFile(context, "${context.packageName}.fileprovider", savedFile)
            val shareIntent = Intent(Intent.ACTION_SEND).apply {
                type = if (format == Bitmap.CompressFormat.PNG) "image/png" else "image/jpeg"
                putExtra(Intent.EXTRA_STREAM, fileUri)
                addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
            }
            context.startActivity(Intent.createChooser(shareIntent, "Share Design"))
        } else {
            viewModel.statusMessage.value = "Exported successfully: $filename"
        }
    }

    // -------------------------------------------------------------
    // Main UI Layout: TopBar -> Canvas Viewport -> Compact Panel -> Bottom Toolbar
    // -------------------------------------------------------------
    Column(
        modifier = modifier
            .fillMaxSize()
            .background(AvaBlack)
    ) {
        // -------------------------------------------------------------
        // 1. TOP BAR
        // -------------------------------------------------------------
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 10.dp, vertical = 6.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                IconButton(
                    onClick = {
                        if (viewModel.hasUnsavedChanges.value) showExitDialog = true else viewModel.navigateBack()
                    },
                    modifier = Modifier.size(36.dp).testTag("editor_back_btn")
                ) {
                    Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back", tint = AvaTextPrimary)
                }

                Spacer(modifier = Modifier.width(4.dp))

                Column {
                    Text(
                        text = projectName,
                        color = AvaTextPrimary,
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Bold,
                        maxLines = 1
                    )
                    Text(
                        text = "AVA Studio • $canvasRatio",
                        color = AvaPurpleLight,
                        fontSize = 10.sp
                    )
                }
            }

            // Controls: Undo, Redo, Fit, Compare, Save, Export
            Row(verticalAlignment = Alignment.CenterVertically) {
                IconButton(
                    onClick = {
                        if (undoStack.isNotEmpty()) {
                            redoStack.add(captureSnapshot())
                            val prev = undoStack.removeAt(undoStack.size - 1)
                            applySnapshot(prev)
                        }
                    },
                    enabled = undoStack.isNotEmpty(),
                    modifier = Modifier.size(32.dp).testTag("btn_undo")
                ) {
                    Icon(
                        Icons.AutoMirrored.Filled.Undo,
                        contentDescription = "Undo",
                        tint = if (undoStack.isNotEmpty()) AvaTextPrimary else AvaTextSecondary.copy(alpha = 0.3f),
                        modifier = Modifier.size(18.dp)
                    )
                }

                IconButton(
                    onClick = {
                        if (redoStack.isNotEmpty()) {
                            undoStack.add(captureSnapshot())
                            val next = redoStack.removeAt(redoStack.size - 1)
                            applySnapshot(next)
                        }
                    },
                    enabled = redoStack.isNotEmpty(),
                    modifier = Modifier.size(32.dp).testTag("btn_redo")
                ) {
                    Icon(
                        Icons.AutoMirrored.Filled.Redo,
                        contentDescription = "Redo",
                        tint = if (redoStack.isNotEmpty()) AvaTextPrimary else AvaTextSecondary.copy(alpha = 0.3f),
                        modifier = Modifier.size(18.dp)
                    )
                }

                // Reset zoom & pan to fit
                IconButton(
                    onClick = {
                        zoomScale = 1f
                        panOffset = Offset.Zero
                    },
                    modifier = Modifier.size(32.dp).testTag("btn_fit_image")
                ) {
                    Icon(Icons.Default.CenterFocusStrong, contentDescription = "Fit Image", tint = AvaTextSecondary, modifier = Modifier.size(18.dp))
                }

                // Hold to compare
                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(6.dp))
                        .background(if (isComparingBefore) AvaPurplePrimary else AvaCard)
                        .clickable { isComparingBefore = !isComparingBefore }
                        .padding(horizontal = 8.dp, vertical = 4.dp)
                        .testTag("btn_compare_before_after")
                ) {
                    Text(
                        text = if (isComparingBefore) "Original" else "Compare",
                        color = if (isComparingBefore) Color.White else AvaTextSecondary,
                        fontSize = 10.sp,
                        fontWeight = FontWeight.Bold
                    )
                }

                Spacer(modifier = Modifier.width(4.dp))

                // Save
                IconButton(
                    onClick = {
                        val currentProj = project ?: ProjectEntity(
                            id = "proj_" + UUID.randomUUID().toString().take(8),
                            userId = "local_creator",
                            title = projectName,
                            projectType = "IMAGE_EDITOR",
                            aspectRatio = canvasRatio,
                            thumbnailUri = null,
                            createdAt = System.currentTimeMillis(),
                            updatedAt = System.currentTimeMillis()
                        )
                        viewModel.saveProject(currentProj)
                        viewModel.hasUnsavedChanges.value = false
                    },
                    modifier = Modifier.size(32.dp).testTag("btn_save_project")
                ) {
                    Icon(Icons.Default.Save, contentDescription = "Save", tint = AvaPurpleLight, modifier = Modifier.size(18.dp))
                }

                // Export Menu
                Box {
                    IconButton(
                        onClick = { showExportMenu = true },
                        modifier = Modifier.size(32.dp).testTag("btn_export_menu")
                    ) {
                        Icon(Icons.Default.Download, contentDescription = "Export", tint = Color.White, modifier = Modifier.size(18.dp))
                    }

                    DropdownMenu(
                        expanded = showExportMenu,
                        onDismissRequest = { showExportMenu = false },
                        modifier = Modifier.background(AvaCard)
                    ) {
                        DropdownMenuItem(
                            text = { Text("Export PNG (Lossless)", color = AvaTextPrimary, fontSize = 12.sp) },
                            onClick = {
                                showExportMenu = false
                                doExport(Bitmap.CompressFormat.PNG, false)
                            }
                        )
                        DropdownMenuItem(
                            text = { Text("Export JPEG (High Quality)", color = AvaTextPrimary, fontSize = 12.sp) },
                            onClick = {
                                showExportMenu = false
                                doExport(Bitmap.CompressFormat.JPEG, false)
                            }
                        )
                        DropdownMenuItem(
                            text = { Text("Share Design...", color = AvaPurpleLight, fontSize = 12.sp) },
                            onClick = {
                                showExportMenu = false
                                doExport(Bitmap.CompressFormat.PNG, true)
                            }
                        )
                    }
                }
            }
        }

        // -------------------------------------------------------------
        // 2. IMAGE WORKSPACE (Canvas dynamically scales to fit completely)
        // -------------------------------------------------------------
        Box(
            modifier = Modifier
                .weight(1f)
                .fillMaxWidth()
                .background(Color(0xFF09080E))
                .pointerInput(Unit) {
                    detectTransformGestures { _, pan, zoom, _ ->
                        zoomScale = (zoomScale * zoom).coerceIn(0.5f, 5.0f)
                        panOffset += pan
                    }
                },
            contentAlignment = Alignment.Center
        ) {
            val aspectFloat = when (canvasRatio) {
                "4:5" -> 4f / 5f
                "3:4" -> 3f / 4f
                "9:16" -> 9f / 16f
                "16:9" -> 16f / 9f
                else -> 1f
            }

            Box(
                modifier = Modifier
                    .fillMaxSize(0.92f)
                    .aspectRatio(aspectFloat, matchHeightConstraintsFirst = true)
                    .scale(zoomScale)
                    .offset { IntOffset(panOffset.x.roundToInt(), panOffset.y.roundToInt()) }
                    .clip(RoundedCornerShape(adjustments.cornerRadius.dp))
                    .background(canvasColor)
                    .border(adjustments.borderThickness.dp, AvaCardBorder, RoundedCornerShape(adjustments.cornerRadius.dp))
                    .testTag("image_canvas_container"),
                contentAlignment = Alignment.Center
            ) {
                // Base Image Layer with Live Filter / Adjustments
                baseBitmap?.let { bmp ->
                    val colorFilter = if (isComparingBefore) {
                        null
                    } else {
                        EditorEngine.createComposeColorFilter(adjustments, activeFilter, filterIntensity, activeEffect)
                    }

                    androidx.compose.foundation.Image(
                        bitmap = bmp.asImageBitmap(),
                        contentDescription = "Canvas",
                        colorFilter = colorFilter,
                        modifier = Modifier
                            .fillMaxSize()
                            .rotate(rotationAngle)
                            .scale(
                                scaleX = if (flipH) -1f else 1f,
                                scaleY = if (flipV) -1f else 1f
                            )
                    )
                }

                // Interactive Drawing Canvas Overlay
                Canvas(
                    modifier = Modifier
                        .fillMaxSize()
                        .pointerInput(activeCategory, isBlurBrushMode) {
                            if (activeCategory == EditorCategory.DRAW) {
                                detectDragGestures(
                                    onDragStart = { offset ->
                                        pushHistory()
                                        drawStrokes.add(
                                            DrawStroke(
                                                points = listOf(DrawPoint(offset.x, offset.y)),
                                                color = currentDrawColor,
                                                strokeWidth = currentDrawSize,
                                                opacity = currentDrawOpacity,
                                                isEraser = isDrawEraser
                                            )
                                        )
                                    },
                                    onDrag = { change, _ ->
                                        change.consume()
                                        val last = drawStrokes.lastOrNull()
                                        if (last != null) {
                                            val updated = last.copy(points = last.points + DrawPoint(change.position.x, change.position.y))
                                            drawStrokes[drawStrokes.size - 1] = updated
                                        }
                                    }
                                )
                            } else if (activeCategory == EditorCategory.BLUR && isBlurBrushMode) {
                                detectDragGestures(
                                    onDragStart = { offset ->
                                        pushHistory()
                                        blurStrokes.add(
                                            BlurBrushPoint(
                                                x = offset.x,
                                                y = offset.y,
                                                radius = blurBrushRadius,
                                                isEraser = isBlurEraser
                                            )
                                        )
                                    },
                                    onDrag = { change, _ ->
                                        change.consume()
                                        blurStrokes.add(
                                            BlurBrushPoint(
                                                x = change.position.x,
                                                y = change.position.y,
                                                radius = blurBrushRadius,
                                                isEraser = isBlurEraser
                                            )
                                        )
                                    }
                                )
                            }
                        }
                ) {
                    // Draw user strokes
                    for (stroke in drawStrokes) {
                        if (stroke.points.size < 2) continue
                        val path = androidx.compose.ui.graphics.Path()
                        path.moveTo(stroke.points[0].x, stroke.points[0].y)
                        for (i in 1 until stroke.points.size) {
                            path.lineTo(stroke.points[i].x, stroke.points[i].y)
                        }
                        drawPath(
                            path = path,
                            color = stroke.color.copy(alpha = stroke.opacity),
                            style = Stroke(
                                width = stroke.strokeWidth,
                                cap = androidx.compose.ui.graphics.StrokeCap.Round,
                                join = androidx.compose.ui.graphics.StrokeJoin.Round
                            )
                        )
                    }

                    // Draw blur mask preview
                    for (pt in blurStrokes) {
                        if (!pt.isEraser) {
                            drawCircle(
                                color = Color.White.copy(alpha = 0.35f),
                                radius = pt.radius,
                                center = Offset(pt.x, pt.y)
                            )
                        }
                    }
                }

                // Interactive Text & Shape Layers on Canvas
                layers.forEach { layer ->
                    if (!layer.isVisible) return@forEach

                    when (layer) {
                        is StudioLayer.Text -> {
                            val isSel = layer.id == selectedLayerId
                            Box(
                                modifier = Modifier
                                    .offset { IntOffset(layer.x.roundToInt(), layer.y.roundToInt()) }
                                    .rotate(layer.rotation)
                                    .pointerInput(layer.id) {
                                        detectDragGestures { change, dragAmount ->
                                            change.consume()
                                            selectedLayerId = layer.id
                                            val idx = layers.indexOfFirst { it.id == layer.id }
                                            if (idx != -1) {
                                                layers[idx] = layer.copy(x = layer.x + dragAmount.x, y = layer.y + dragAmount.y)
                                            }
                                        }
                                    }
                                    .clickable { selectedLayerId = layer.id }
                                    .border(
                                        width = if (isSel) 1.5.dp else 0.dp,
                                        color = if (isSel) AvaPurpleLight else Color.Transparent,
                                        shape = RoundedCornerShape(4.dp)
                                    )
                                    .padding(4.dp)
                            ) {
                                Text(
                                    text = layer.content,
                                    color = layer.color.copy(alpha = layer.opacity),
                                    fontSize = layer.fontSize.sp,
                                    fontWeight = if (layer.isBold) FontWeight.Bold else FontWeight.Normal,
                                    fontFamily = when (layer.fontFamily) {
                                        "Serif", "Luxury", "Editorial" -> androidx.compose.ui.text.font.FontFamily.Serif
                                        else -> androidx.compose.ui.text.font.FontFamily.SansSerif
                                    }
                                )
                            }
                        }

                        is StudioLayer.Shape -> {
                            Box(
                                modifier = Modifier
                                    .offset { IntOffset(layer.x.roundToInt(), layer.y.roundToInt()) }
                                    .size(layer.width.dp, layer.height.dp)
                                    .pointerInput(layer.id) {
                                        detectDragGestures { change, dragAmount ->
                                            change.consume()
                                            selectedLayerId = layer.id
                                            val idx = layers.indexOfFirst { it.id == layer.id }
                                            if (idx != -1) {
                                                layers[idx] = layer.copy(x = layer.x + dragAmount.x, y = layer.y + dragAmount.y)
                                            }
                                        }
                                    }
                                    .border(layer.strokeWidth.dp, layer.strokeColor, RoundedCornerShape(8.dp))
                            )
                        }

                        is StudioLayer.Element -> {
                            Box(
                                modifier = Modifier
                                    .offset { IntOffset(layer.x.roundToInt(), layer.y.roundToInt()) }
                                    .size(layer.size.dp)
                                    .pointerInput(layer.id) {
                                        detectDragGestures { change, dragAmount ->
                                            change.consume()
                                            selectedLayerId = layer.id
                                            val idx = layers.indexOfFirst { it.id == layer.id }
                                            if (idx != -1) {
                                                layers[idx] = layer.copy(x = layer.x + dragAmount.x, y = layer.y + dragAmount.y)
                                            }
                                        }
                                    }
                                    .border(2.dp, layer.tintColor, RoundedCornerShape(12.dp)),
                                contentAlignment = Alignment.Center
                            ) {
                                Text(layer.elementKind, color = layer.tintColor, fontSize = 11.sp, fontWeight = FontWeight.Bold)
                            }
                        }
                    }
                }
            }
        }

        // -------------------------------------------------------------
        // 3. COMPACT TOOL SETTINGS PANEL (Above Bottom Toolbar)
        // -------------------------------------------------------------
        AnimatedVisibility(
            visible = activeCategory != null,
            enter = expandVertically() + fadeIn(),
            exit = shrinkVertically() + fadeOut()
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(AvaSurface)
                    .border(1.dp, AvaDivider, RoundedCornerShape(topStart = 16.dp, topEnd = 16.dp))
                    .padding(horizontal = 14.dp, vertical = 10.dp)
                    .testTag("compact_tool_panel")
            ) {
                when (activeCategory) {
                    EditorCategory.ADJUST -> {
                        AdjustPanel(
                            adjustments = adjustments,
                            onAdjustmentsChange = {
                                adjustments = it
                                pushHistory()
                            }
                        )
                    }
                    EditorCategory.FILTERS -> {
                        FilterPanel(
                            activeFilter = activeFilter,
                            filterIntensity = filterIntensity,
                            onFilterSelect = {
                                activeFilter = it
                                pushHistory()
                            },
                            onIntensityChange = {
                                filterIntensity = it
                            }
                        )
                    }
                    EditorCategory.EFFECTS -> {
                        EffectsPanel(
                            activeEffect = activeEffect,
                            onEffectSelect = {
                                activeEffect = it
                                pushHistory()
                            }
                        )
                    }
                    EditorCategory.BLUR -> {
                        BlurPanel(
                            activeBlurType = activeBlurType,
                            blurIntensity = blurIntensity,
                            brushSize = blurBrushRadius,
                            isBlurBrushMode = isBlurBrushMode,
                            isBlurEraser = isBlurEraser,
                            onBlurTypeChange = {
                                activeBlurType = it
                                pushHistory()
                            },
                            onIntensityChange = { blurIntensity = it },
                            onBrushSizeChange = { blurBrushRadius = it },
                            onToggleBrushMode = { isBlurBrushMode = it },
                            onToggleEraser = { isBlurEraser = it },
                            onClearBlurStrokes = {
                                blurStrokes.clear()
                                pushHistory()
                            }
                        )
                    }
                    EditorCategory.RETOUCH -> {
                        Column(modifier = Modifier.fillMaxWidth()) {
                            CompactSlider(
                                label = "Skin Smoothness",
                                value = adjustments.clarity,
                                range = -50f..50f,
                                onValueChange = { adjustments = adjustments.copy(clarity = it) }
                            )
                            CompactSlider(
                                label = "Facial Glow",
                                value = adjustments.highlights,
                                range = -50f..50f,
                                onValueChange = { adjustments = adjustments.copy(highlights = it) }
                            )
                        }
                    }
                    EditorCategory.CROP -> {
                        CropPanel(
                            activeRatio = canvasRatio,
                            onRatioChange = {
                                canvasRatio = it
                                pushHistory()
                            },
                            onRotateLeft = {
                                rotationAngle -= 90f
                                pushHistory()
                            },
                            onRotateRight = {
                                rotationAngle += 90f
                                pushHistory()
                            },
                            onFlipH = {
                                flipH = !flipH
                                pushHistory()
                            },
                            onFlipV = {
                                flipV = !flipV
                                pushHistory()
                            }
                        )
                    }
                    EditorCategory.TEXT -> {
                        val selText = layers.filterIsInstance<StudioLayer.Text>().firstOrNull { it.id == selectedLayerId }
                        TextPanel(
                            selectedTextLayer = selText,
                            onAddText = {
                                pushHistory()
                                val newText = StudioLayer.Text(
                                    content = "AVA STUDIO",
                                    x = 80f,
                                    y = 120f
                                )
                                layers.add(newText)
                                selectedLayerId = newText.id
                            },
                            onUpdateText = { updated ->
                                val idx = layers.indexOfFirst { it.id == updated.id }
                                if (idx != -1) layers[idx] = updated
                            },
                            onDeleteText = { id ->
                                pushHistory()
                                layers.removeAll { it.id == id }
                                if (selectedLayerId == id) selectedLayerId = null
                            }
                        )
                    }
                    EditorCategory.DRAW -> {
                        DrawPanel(
                            brushColor = currentDrawColor,
                            brushSize = currentDrawSize,
                            brushOpacity = currentDrawOpacity,
                            isEraser = isDrawEraser,
                            onColorChange = { currentDrawColor = it },
                            onSizeChange = { currentDrawSize = it },
                            onOpacityChange = { currentDrawOpacity = it },
                            onToggleEraser = { isDrawEraser = it },
                            onClearDrawing = {
                                pushHistory()
                                drawStrokes.clear()
                            }
                        )
                    }
                    EditorCategory.ELEMENTS -> {
                        ElementsPanel(
                            onAddShape = { shapeKind ->
                                pushHistory()
                                val s = StudioLayer.Shape(
                                    shapeKind = shapeKind,
                                    x = 60f,
                                    y = 60f,
                                    width = 120f,
                                    height = 120f
                                )
                                layers.add(s)
                                selectedLayerId = s.id
                            },
                            onAddElement = { elemKind ->
                                pushHistory()
                                val e = StudioLayer.Element(
                                    elementKind = elemKind,
                                    x = 80f,
                                    y = 80f,
                                    size = 100f
                                )
                                layers.add(e)
                                selectedLayerId = e.id
                            }
                        )
                    }
                    EditorCategory.BACKGROUND -> {
                        Column(modifier = Modifier.fillMaxWidth()) {
                            Text("Canvas Background Color", color = AvaTextPrimary, fontSize = 11.sp, fontWeight = FontWeight.Medium)
                            Spacer(modifier = Modifier.height(6.dp))
                            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                                val bgColors = listOf(
                                    Color(0xFF09080E),
                                    Color(0xFF1E1B2E),
                                    Color(0xFF2E1C4E),
                                    Color(0xFF1C2536),
                                    Color(0xFF262626),
                                    Color.White
                                )
                                bgColors.forEach { c ->
                                    val isSel = canvasColor == c
                                    Box(
                                        modifier = Modifier
                                            .size(28.dp)
                                            .clip(CircleShape)
                                            .background(c)
                                            .border(if (isSel) 2.dp else 1.dp, if (isSel) AvaPurpleLight else AvaCardBorder, CircleShape)
                                            .clickable {
                                                pushHistory()
                                                canvasColor = c
                                            }
                                    )
                                }
                            }
                        }
                    }
                    EditorCategory.LAYERS -> {
                        LayersPanel(
                            layers = layers,
                            selectedLayerId = selectedLayerId,
                            onSelectLayer = { selectedLayerId = it },
                            onToggleVisibility = { id ->
                                val idx = layers.indexOfFirst { it.id == id }
                                if (idx != -1) {
                                    val item = layers[idx]
                                    item.isVisible = !item.isVisible
                                    layers[idx] = item
                                }
                            },
                            onToggleLock = { id ->
                                val idx = layers.indexOfFirst { it.id == id }
                                if (idx != -1) {
                                    val item = layers[idx]
                                    item.isLocked = !item.isLocked
                                    layers[idx] = item
                                }
                            },
                            onDeleteLayer = { id ->
                                pushHistory()
                                layers.removeAll { it.id == id }
                                if (selectedLayerId == id) selectedLayerId = null
                            },
                            onMoveLayerUp = { idx ->
                                if (idx < layers.size - 1) {
                                    pushHistory()
                                    val temp = layers[idx]
                                    layers[idx] = layers[idx + 1]
                                    layers[idx + 1] = temp
                                }
                            },
                            onMoveLayerDown = { idx ->
                                if (idx > 0) {
                                    pushHistory()
                                    val temp = layers[idx]
                                    layers[idx] = layers[idx - 1]
                                    layers[idx - 1] = temp
                                }
                            }
                        )
                    }
                    EditorCategory.FRAMES -> {
                        FramesPanel(
                            activeFrame = activeFrame,
                            onFrameSelect = {
                                activeFrame = it
                                pushHistory()
                            }
                        )
                    }
                    EditorCategory.LIGHTING -> {
                        LightingPanel(
                            activeLighting = activeLighting,
                            lightingOpacity = lightingOpacity,
                            onLightingSelect = {
                                activeLighting = it
                                pushHistory()
                            },
                            onOpacityChange = { lightingOpacity = it }
                        )
                    }
                    EditorCategory.COLOR -> {
                        Column(modifier = Modifier.fillMaxWidth()) {
                            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                                CompactSlider(
                                    label = "Red Balance",
                                    value = adjustments.colorBalanceR,
                                    range = -30f..30f,
                                    onValueChange = { adjustments = adjustments.copy(colorBalanceR = it) },
                                    modifier = Modifier.weight(1f)
                                )
                                CompactSlider(
                                    label = "Blue Balance",
                                    value = adjustments.colorBalanceB,
                                    range = -30f..30f,
                                    onValueChange = { adjustments = adjustments.copy(colorBalanceB = it) },
                                    modifier = Modifier.weight(1f)
                                )
                            }
                        }
                    }
                    EditorCategory.DETAILS -> {
                        Column(modifier = Modifier.fillMaxWidth()) {
                            CompactSlider(
                                label = "Sharpness Detail",
                                value = adjustments.sharpness,
                                range = 0f..100f,
                                onValueChange = { adjustments = adjustments.copy(sharpness = it) }
                            )
                            CompactSlider(
                                label = "Grain Texture",
                                value = adjustments.grain,
                                range = 0f..100f,
                                onValueChange = { adjustments = adjustments.copy(grain = it) }
                            )
                        }
                    }
                    null -> {}
                }
            }
        }

        // -------------------------------------------------------------
        // 4. BOTTOM 15-CATEGORY TOOLBAR (Horizontally Scrollable)
        // -------------------------------------------------------------
        LazyRow(
            modifier = Modifier
                .fillMaxWidth()
                .background(AvaBlack)
                .border(1.dp, AvaDivider, RoundedCornerShape(0.dp))
                .padding(vertical = 6.dp),
            contentPadding = PaddingValues(horizontal = 10.dp),
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            items(EditorCategory.values()) { category ->
                val isSel = activeCategory == category
                Column(
                    modifier = Modifier
                        .clickable {
                            activeCategory = if (isSel) null else category
                            // Reset brush modes if moving out of draw/blur
                            if (category != EditorCategory.BLUR) isBlurBrushMode = false
                        }
                        .padding(horizontal = 4.dp, vertical = 2.dp)
                        .testTag("tool_tab_${category.name.lowercase()}"),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Box(
                        modifier = Modifier
                            .size(36.dp)
                            .clip(CircleShape)
                            .background(if (isSel) AvaPurplePrimary else AvaCard)
                            .border(1.dp, if (isSel) AvaPurpleLight else AvaCardBorder, CircleShape),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = category.icon,
                            contentDescription = category.label,
                            tint = if (isSel) Color.White else AvaTextSecondary,
                            modifier = Modifier.size(18.dp)
                        )
                    }

                    Spacer(modifier = Modifier.height(3.dp))

                    Text(
                        text = category.label,
                        color = if (isSel) AvaPurpleLight else AvaTextSecondary,
                        fontSize = 10.sp,
                        fontWeight = if (isSel) FontWeight.Bold else FontWeight.Normal
                    )
                }
            }
        }
    }
}
