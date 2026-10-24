package com.example.ui.screens.photostudio

import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.expandVertically
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
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
import androidx.compose.material.icons.filled.AddPhotoAlternate
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.BlurOn
import androidx.compose.material.icons.filled.CenterFocusStrong
import androidx.compose.material.icons.filled.ColorLens
import androidx.compose.material.icons.filled.Compare
import androidx.compose.material.icons.filled.Crop
import androidx.compose.material.icons.filled.Details
import androidx.compose.material.icons.filled.Download
import androidx.compose.material.icons.filled.Face
import androidx.compose.material.icons.filled.Flip
import androidx.compose.material.icons.filled.OpenInNew
import androidx.compose.material.icons.filled.Palette
import androidx.compose.material.icons.filled.PhotoCamera
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.RotateRight
import androidx.compose.material.icons.filled.Save
import androidx.compose.material.icons.filled.Security
import androidx.compose.material.icons.filled.Share
import androidx.compose.material.icons.filled.Tune
import androidx.compose.material.icons.filled.WbSunny
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
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
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.content.FileProvider
import com.example.data.model.ProjectEntity
import com.example.ui.screens.editor.CompactSlider
import com.example.ui.theme.AvaBlack
import com.example.ui.theme.AvaCard
import com.example.ui.theme.AvaCardBorder
import com.example.ui.theme.AvaCardSelected
import com.example.ui.theme.AvaDivider
import com.example.ui.theme.AvaPurpleContainer
import com.example.ui.theme.AvaPurpleLight
import com.example.ui.theme.AvaPurplePrimary
import com.example.ui.theme.AvaSuccess
import com.example.ui.theme.AvaTextPrimary
import com.example.ui.theme.AvaTextSecondary
import com.example.ui.viewmodel.AppScreen
import com.example.ui.viewmodel.MainViewModel
import com.example.util.AppLanguage
import com.example.util.BlurType
import com.example.util.CreativeEffect
import com.example.util.DemoArtworkProvider
import com.example.util.EditorEngine
import com.example.util.EditorSnapshot
import com.example.util.FilterCategory
import com.example.util.FilterPreset
import com.example.util.FrameStyle
import com.example.util.ImageAdjustments
import com.example.util.LightingStyle
import com.example.util.LocalAppLanguage
import java.io.File
import java.util.UUID

enum class PortraitToolCategory(val labelEn: String, val labelAr: String, val icon: ImageVector) {
    ADJUST("Adjust", "إضاءة", Icons.Default.Tune),
    RETOUCH("Retouch", "تحسين الوجه", Icons.Default.Face),
    FILTERS("Filters", "فلاتر", Icons.Default.Palette),
    COLOR("Color", "ألوان", Icons.Default.ColorLens),
    BOKEH("Bokeh Blur", "عزل الخلفية", Icons.Default.BlurOn),
    LIGHTING("Lighting", "إضاءة استوديو", Icons.Default.WbSunny),
    CROP("Crop & Fit", "قص وتدوير", Icons.Default.Crop),
    DETAILS("Details", "تفاصيل وتأطير", Icons.Default.Details)
}

@Composable
fun PersonalPhotoStudioScreen(
    viewModel: MainViewModel,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    val isAr = LocalAppLanguage.current == AppLanguage.ARABIC

    // -------------------------------------------------------------
    // Base Portrait Image & Selection State
    // -------------------------------------------------------------
    var baseBitmap by remember { mutableStateOf<Bitmap?>(null) }
    var isUserPersonalPhoto by remember { mutableStateOf(false) }
    var personalPhotoUriStr by remember { mutableStateOf<String?>(null) }

    // Initialize with portrait sample or existing active project image
    LaunchedEffect(Unit) {
        val activeProj = viewModel.activeProject.value
        if (activeProj?.thumbnailUri != null && activeProj.thumbnailUri.startsWith("demo://")) {
            val demoId = activeProj.thumbnailUri.removePrefix("demo://")
            val foundDemo = DemoArtworkProvider.demoArtworks.find { it.id == demoId }
            if (foundDemo != null) {
                baseBitmap = DemoArtworkProvider.generateDemoBitmap(foundDemo)
            }
        }
        if (baseBitmap == null) {
            val portraitDemo = DemoArtworkProvider.demoArtworks.find { it.category == "Portrait" }
                ?: DemoArtworkProvider.demoArtworks.first()
            baseBitmap = DemoArtworkProvider.generateDemoBitmap(portraitDemo)
        }
    }

    // Modern Zero-Permission Photo Picker
    val photoPickerLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.PickVisualMedia()
    ) { uri: Uri? ->
        if (uri != null) {
            try {
                context.contentResolver.openInputStream(uri)?.use { stream ->
                    val decoded = BitmapFactory.decodeStream(stream)
                    if (decoded != null) {
                        baseBitmap = decoded
                        isUserPersonalPhoto = true
                        personalPhotoUriStr = uri.toString()
                        viewModel.statusMessage.value = if (isAr) "تم تحميل صورتك الشخصية بنجاح!" else "Personal photo loaded successfully!"
                    }
                }
            } catch (e: Exception) {
                viewModel.statusMessage.value = if (isAr) "تعذر فتح الصورة المختارة" else "Failed to open selected photo"
            }
        }
    }

    // Fallback file picker for broader compatibility
    val fallbackFilePicker = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri: Uri? ->
        if (uri != null) {
            try {
                context.contentResolver.openInputStream(uri)?.use { stream ->
                    val decoded = BitmapFactory.decodeStream(stream)
                    if (decoded != null) {
                        baseBitmap = decoded
                        isUserPersonalPhoto = true
                        personalPhotoUriStr = uri.toString()
                        viewModel.statusMessage.value = if (isAr) "تم تحميل صورتك الشخصية بنجاح!" else "Personal photo loaded successfully!"
                    }
                }
            } catch (e: Exception) {
                viewModel.statusMessage.value = if (isAr) "تعذر فتح الصورة" else "Could not open image"
            }
        }
    }

    fun openUserPhotoPicker() {
        try {
            photoPickerLauncher.launch(
                PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly)
            )
        } catch (e: Exception) {
            fallbackFilePicker.launch("image/*")
        }
    }

    // -------------------------------------------------------------
    // Portrait Editing State (Same tools with full efficacy!)
    // -------------------------------------------------------------
    var adjustments by remember { mutableStateOf(ImageAdjustments()) }
    var activeFilter by remember { mutableStateOf(FilterPreset.STUDIO_PORTRAIT) }
    var filterIntensity by remember { mutableFloatStateOf(0.85f) }
    var activeLighting by remember { mutableStateOf(LightingStyle.STUDIO_FLASH) }
    var lightingOpacity by remember { mutableFloatStateOf(0.4f) }
    var activeBokehBlur by remember { mutableStateOf(BlurType.NONE) }
    var bokehIntensity by remember { mutableFloatStateOf(0f) }
    var activeEffect by remember { mutableStateOf(CreativeEffect.NONE) }
    var canvasRatio by remember { mutableStateOf("4:5") } // Default 4:5 optimal portrait aspect ratio

    // Transform states
    var rotationAngle by remember { mutableFloatStateOf(0f) }
    var flipH by remember { mutableStateOf(false) }
    var flipV by remember { mutableStateOf(false) }

    // Zoom & Pan
    var zoomScale by remember { mutableFloatStateOf(1f) }
    var panOffset by remember { mutableStateOf(Offset.Zero) }

    // Before / After Compare
    var isComparingOriginal by remember { mutableStateOf(false) }

    // Active Category Tool Panel
    var activeCategory by remember { mutableStateOf<PortraitToolCategory?>(PortraitToolCategory.ADJUST) }

    // Undo / Redo history
    val undoStack = remember { mutableStateListOf<EditorSnapshot>() }
    val redoStack = remember { mutableStateListOf<EditorSnapshot>() }

    fun captureSnapshot(): EditorSnapshot {
        return EditorSnapshot(
            adjustments = adjustments.copy(),
            filter = activeFilter,
            filterIntensity = filterIntensity,
            blurType = activeBokehBlur,
            blurIntensity = bokehIntensity,
            blurStrokes = emptyList(),
            drawStrokes = emptyList(),
            layers = emptyList(),
            lightingStyle = activeLighting,
            lightingOpacity = lightingOpacity,
            frameStyle = FrameStyle.NONE,
            creativeEffect = activeEffect,
            rotationAngle = rotationAngle,
            flipH = flipH,
            flipV = flipV,
            cropRatio = canvasRatio
        )
    }

    fun pushHistory() {
        undoStack.add(captureSnapshot())
        redoStack.clear()
        viewModel.hasUnsavedChanges.value = true
    }

    fun applySnapshot(snap: EditorSnapshot) {
        adjustments = snap.adjustments
        activeFilter = snap.filter
        filterIntensity = snap.filterIntensity
        activeBokehBlur = snap.blurType
        bokehIntensity = snap.blurIntensity
        activeLighting = snap.lightingStyle
        lightingOpacity = snap.lightingOpacity
        activeEffect = snap.creativeEffect
        rotationAngle = snap.rotationAngle
        flipH = snap.flipH
        flipV = snap.flipV
        canvasRatio = snap.cropRatio
    }

    fun resetAllAdjustments() {
        pushHistory()
        adjustments = ImageAdjustments()
        activeFilter = FilterPreset.NONE
        filterIntensity = 1f
        activeLighting = LightingStyle.NONE
        lightingOpacity = 0.5f
        activeBokehBlur = BlurType.NONE
        bokehIntensity = 0f
        activeEffect = CreativeEffect.NONE
        rotationAngle = 0f
        flipH = false
        flipV = false
        zoomScale = 1f
        panOffset = Offset.Zero
        viewModel.statusMessage.value = if (isAr) "تمت إعادة ضبط التعديلات" else "Adjustments reset"
    }

    // Export Dropdown Menu state
    var showExportMenu by remember { mutableStateOf(false) }

    fun doExport(format: Bitmap.CompressFormat, shareAfter: Boolean = false) {
        val bmp = baseBitmap ?: return
        val w = bmp.width
        val h = bmp.height
        val exported = EditorEngine.exportCanvasToBitmap(
            width = w,
            height = h,
            backgroundColor = Color(0xFF09080E),
            baseBitmap = bmp,
            adjustments = adjustments,
            filter = activeFilter,
            filterIntensity = filterIntensity,
            creativeEffect = activeEffect,
            lightingStyle = activeLighting,
            lightingOpacity = lightingOpacity,
            frameStyle = FrameStyle.NONE,
            blurType = activeBokehBlur,
            blurIntensity = bokehIntensity,
            blurStrokes = emptyList(),
            drawStrokes = emptyList(),
            layers = emptyList(),
            rotationAngle = rotationAngle,
            flipH = flipH,
            flipV = flipV
        )

        val ext = if (format == Bitmap.CompressFormat.PNG) "png" else "jpg"
        val filename = "AVA_Portrait_${System.currentTimeMillis()}.$ext"
        val savedFile = EditorEngine.saveBitmapToFile(context, exported, format, filename)

        if (shareAfter) {
            val fileUri = FileProvider.getUriForFile(context, "${context.packageName}.fileprovider", savedFile)
            val shareIntent = Intent(Intent.ACTION_SEND).apply {
                type = if (format == Bitmap.CompressFormat.PNG) "image/png" else "image/jpeg"
                putExtra(Intent.EXTRA_STREAM, fileUri)
                addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
            }
            context.startActivity(Intent.createChooser(shareIntent, if (isAr) "مشاركة الصورة الشخصية" else "Share Portrait"))
        } else {
            viewModel.statusMessage.value = if (isAr) "تم تصدير وحفظ الصورة: $filename" else "Portrait exported: $filename"
        }
    }

    // -------------------------------------------------------------
    // Main Studio Viewport
    // -------------------------------------------------------------
    Column(
        modifier = modifier
            .fillMaxSize()
            .background(AvaBlack)
    ) {
        // -------------------------------------------------------------
        // 1. TOP BAR (Navigation, Project, History, Compare, Save, Export)
        // -------------------------------------------------------------
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 8.dp, vertical = 6.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                IconButton(
                    onClick = { viewModel.navigateBack() },
                    modifier = Modifier.size(36.dp).testTag("personal_photo_back")
                ) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                        contentDescription = "Back",
                        tint = AvaTextPrimary
                    )
                }

                Spacer(modifier = Modifier.width(4.dp))

                Column {
                    Text(
                        text = if (isAr) "استوديو الصور الشخصية" else "Personal Photo Studio",
                        color = AvaTextPrimary,
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Bold
                    )
                    Text(
                        text = if (isUserPersonalPhoto)
                            (if (isAr) "صورتك الشخصية • محلي 100%" else "My Photo • 100% On-Device")
                        else
                            (if (isAr) "نموذج استوديو تجريبي" else "Studio Demo Portrait"),
                        color = if (isUserPersonalPhoto) AvaSuccess else AvaPurpleLight,
                        fontSize = 10.sp
                    )
                }
            }

            // Controls: Undo, Redo, Compare, Reset, Save, Export, Pro Editor
            Row(verticalAlignment = Alignment.CenterVertically) {
                // Undo
                IconButton(
                    onClick = {
                        if (undoStack.isNotEmpty()) {
                            redoStack.add(captureSnapshot())
                            val prev = undoStack.removeAt(undoStack.size - 1)
                            applySnapshot(prev)
                        }
                    },
                    enabled = undoStack.isNotEmpty(),
                    modifier = Modifier.size(32.dp).testTag("btn_portrait_undo")
                ) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.Undo,
                        contentDescription = "Undo",
                        tint = if (undoStack.isNotEmpty()) AvaTextPrimary else AvaTextSecondary.copy(alpha = 0.3f),
                        modifier = Modifier.size(18.dp)
                    )
                }

                // Redo
                IconButton(
                    onClick = {
                        if (redoStack.isNotEmpty()) {
                            undoStack.add(captureSnapshot())
                            val next = redoStack.removeAt(redoStack.size - 1)
                            applySnapshot(next)
                        }
                    },
                    enabled = redoStack.isNotEmpty(),
                    modifier = Modifier.size(32.dp).testTag("btn_portrait_redo")
                ) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.Redo,
                        contentDescription = "Redo",
                        tint = if (redoStack.isNotEmpty()) AvaTextPrimary else AvaTextSecondary.copy(alpha = 0.3f),
                        modifier = Modifier.size(18.dp)
                    )
                }

                // Before/After Comparison (Press to toggle)
                IconButton(
                    onClick = { isComparingOriginal = !isComparingOriginal },
                    modifier = Modifier.size(32.dp).testTag("btn_portrait_compare")
                ) {
                    Icon(
                        imageVector = Icons.Default.Compare,
                        contentDescription = "Compare",
                        tint = if (isComparingOriginal) Color(0xFFEF4444) else AvaPurpleLight,
                        modifier = Modifier.size(18.dp)
                    )
                }

                // Reset
                IconButton(
                    onClick = { resetAllAdjustments() },
                    modifier = Modifier.size(32.dp).testTag("btn_portrait_reset")
                ) {
                    Icon(
                        imageVector = Icons.Default.Refresh,
                        contentDescription = "Reset",
                        tint = AvaTextSecondary,
                        modifier = Modifier.size(17.dp)
                    )
                }

                // Save Project
                IconButton(
                    onClick = {
                        val proj = ProjectEntity(
                            id = "portrait_" + UUID.randomUUID().toString().take(8),
                            userId = "local_creator",
                            title = if (isAr) "بورتريه شخصي محسّن" else "Enhanced Personal Portrait",
                            projectType = "personal_photo",
                            aspectRatio = canvasRatio,
                            thumbnailUri = personalPhotoUriStr,
                            createdAt = System.currentTimeMillis(),
                            updatedAt = System.currentTimeMillis()
                        )
                        viewModel.saveProject(proj)
                        viewModel.hasUnsavedChanges.value = false
                    },
                    modifier = Modifier.size(32.dp).testTag("btn_portrait_save")
                ) {
                    Icon(
                        imageVector = Icons.Default.Save,
                        contentDescription = "Save",
                        tint = AvaPurpleLight,
                        modifier = Modifier.size(18.dp)
                    )
                }

                // Export Menu
                Box {
                    IconButton(
                        onClick = { showExportMenu = true },
                        modifier = Modifier.size(32.dp).testTag("btn_portrait_export")
                    ) {
                        Icon(
                            imageVector = Icons.Default.Download,
                            contentDescription = "Export",
                            tint = Color.White,
                            modifier = Modifier.size(18.dp)
                        )
                    }

                    DropdownMenu(
                        expanded = showExportMenu,
                        onDismissRequest = { showExportMenu = false },
                        modifier = Modifier.background(AvaCard).border(1.dp, AvaCardBorder, RoundedCornerShape(8.dp))
                    ) {
                        DropdownMenuItem(
                            text = { Text(if (isAr) "حفظ بجودة PNG عالية" else "Export Lossless PNG", color = AvaTextPrimary, fontSize = 12.sp) },
                            onClick = {
                                showExportMenu = false
                                doExport(Bitmap.CompressFormat.PNG)
                            }
                        )
                        DropdownMenuItem(
                            text = { Text(if (isAr) "حفظ بصيغة JPG" else "Export JPG (Optimized)", color = AvaTextPrimary, fontSize = 12.sp) },
                            onClick = {
                                showExportMenu = false
                                doExport(Bitmap.CompressFormat.JPEG)
                            }
                        )
                        DropdownMenuItem(
                            text = {
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Icon(Icons.Default.Share, contentDescription = null, tint = AvaPurpleLight, modifier = Modifier.size(16.dp))
                                    Spacer(modifier = Modifier.width(8.dp))
                                    Text(if (isAr) "مشاركة الصورة..." else "Share Portrait...", color = AvaTextPrimary, fontSize = 12.sp)
                                }
                            },
                            onClick = {
                                showExportMenu = false
                                doExport(Bitmap.CompressFormat.PNG, shareAfter = true)
                            }
                        )
                    }
                }

                // Transfer to Full Pro Editor (for complex text, stickers & drawing)
                IconButton(
                    onClick = {
                        val currentProj = ProjectEntity(
                            id = "portrait_" + UUID.randomUUID().toString().take(8),
                            userId = "local_creator",
                            title = if (isAr) "تعديل البورتريه الشامل" else "Studio Portrait Master",
                            projectType = "IMAGE_EDITOR",
                            aspectRatio = canvasRatio,
                            thumbnailUri = personalPhotoUriStr,
                            createdAt = System.currentTimeMillis(),
                            updatedAt = System.currentTimeMillis()
                        )
                        viewModel.activeProject.value = currentProj
                        viewModel.navigateTo(AppScreen.IMAGE_EDITOR)
                    },
                    modifier = Modifier.size(32.dp).testTag("btn_portrait_to_full_editor")
                ) {
                    Icon(
                        imageVector = Icons.Default.OpenInNew,
                        contentDescription = "Full Editor",
                        tint = AvaPurpleLight,
                        modifier = Modifier.size(18.dp)
                    )
                }
            }
        }

        // -------------------------------------------------------------
        // 2. Personal Photo Upload / Action Header Strip
        // -------------------------------------------------------------
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 12.dp, vertical = 4.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Identity Safe Privacy Badge
            Row(
                modifier = Modifier
                    .clip(RoundedCornerShape(8.dp))
                    .background(AvaSuccess.copy(alpha = 0.12f))
                    .border(1.dp, AvaSuccess.copy(alpha = 0.3f), RoundedCornerShape(8.dp))
                    .padding(horizontal = 8.dp, vertical = 4.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    imageVector = Icons.Default.Security,
                    contentDescription = null,
                    tint = AvaSuccess,
                    modifier = Modifier.size(13.dp)
                )
                Spacer(modifier = Modifier.width(6.dp))
                Text(
                    text = if (isAr) "خصوصية كاملة • معالجة محلية" else "100% On-Device • Identity Safe",
                    color = AvaSuccess,
                    fontSize = 10.sp,
                    fontWeight = FontWeight.Bold
                )
            }

            // Prominent "Add My Personal Photo" / "Change Photo" Button
            Button(
                onClick = { openUserPhotoPicker() },
                colors = ButtonDefaults.buttonColors(
                    containerColor = if (isUserPersonalPhoto) AvaCardSelected else AvaPurplePrimary
                ),
                shape = RoundedCornerShape(12.dp),
                contentPadding = PaddingValues(horizontal = 10.dp, vertical = 4.dp),
                modifier = Modifier
                    .height(30.dp)
                    .testTag("btn_add_my_personal_photo")
            ) {
                Icon(
                    imageVector = if (isUserPersonalPhoto) Icons.Default.PhotoCamera else Icons.Default.AddPhotoAlternate,
                    contentDescription = null,
                    tint = Color.White,
                    modifier = Modifier.size(14.dp)
                )
                Spacer(modifier = Modifier.width(6.dp))
                Text(
                    text = if (isUserPersonalPhoto)
                        (if (isAr) "تغيير صورتي" else "Change Photo")
                    else
                        (if (isAr) "إضافة صورتي الشخصية" else "Add My Personal Photo"),
                    color = Color.White,
                    fontSize = 11.sp,
                    fontWeight = FontWeight.Bold
                )
            }
        }

        // -------------------------------------------------------------
        // 3. CENTERED WORKSPACE VIEWPORT (Pinch Zoom, Pan, Dynamic Fit)
        // -------------------------------------------------------------
        Box(
            modifier = Modifier
                .weight(1f)
                .fillMaxWidth()
                .padding(horizontal = 8.dp, vertical = 4.dp)
                .clip(RoundedCornerShape(12.dp))
                .background(Color(0xFF09080E))
                .border(1.dp, AvaDivider, RoundedCornerShape(12.dp))
                .pointerInput(Unit) {
                    detectTransformGestures { _, pan, zoom, _ ->
                        zoomScale = (zoomScale * zoom).coerceIn(0.7f, 4f)
                        panOffset += pan
                    }
                },
            contentAlignment = Alignment.Center
        ) {
            val aspectFloat = when (canvasRatio) {
                "1:1" -> 1f
                "4:5" -> 0.8f
                "3:4" -> 0.75f
                "9:16" -> 0.5625f
                "16:9" -> 1.777f
                else -> 0.8f
            }

            Box(
                modifier = Modifier
                    .fillMaxSize(0.92f)
                    .aspectRatio(aspectFloat, matchHeightConstraintsFirst = true)
                    .offset { androidx.compose.ui.unit.IntOffset(panOffset.x.toInt(), panOffset.y.toInt()) }
                    .scale(zoomScale)
                    .clip(RoundedCornerShape(8.dp))
                    .background(Color(0xFF13111C))
                    .border(1.dp, AvaCardBorder, RoundedCornerShape(8.dp))
                    .testTag("personal_portrait_canvas"),
                contentAlignment = Alignment.Center
            ) {
                // Base Image with Live ColorMatrix / Adjustments Filter
                baseBitmap?.let { bmp ->
                    val colorFilter = if (isComparingOriginal) {
                        null // Show original unedited photo
                    } else {
                        EditorEngine.createComposeColorFilter(adjustments, activeFilter, filterIntensity, activeEffect)
                    }

                    Image(
                        bitmap = bmp.asImageBitmap(),
                        contentDescription = "Personal Portrait",
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

                // Bokeh / Depth Focus Simulation Shader
                if (bokehIntensity > 0.05f && !isComparingOriginal) {
                    Canvas(modifier = Modifier.fillMaxSize()) {
                        val center = Offset(size.width / 2f, size.height * 0.45f)
                        val radius = size.minDimension * 0.42f
                        drawCircle(
                            brush = Brush.radialGradient(
                                colors = listOf(
                                    Color.Transparent,
                                    Color.Transparent,
                                    Color.Black.copy(alpha = (bokehIntensity * 0.0035f).coerceIn(0f, 0.45f))
                                ),
                                center = center,
                                radius = radius * 1.5f
                            )
                        )
                    }
                }

                // Studio Lighting Overlay Layer
                if (activeLighting != LightingStyle.NONE && !isComparingOriginal) {
                    Canvas(modifier = Modifier.fillMaxSize()) {
                        when (activeLighting) {
                            LightingStyle.STUDIO_FLASH -> {
                                drawCircle(
                                    brush = Brush.radialGradient(
                                        colors = listOf(
                                            Color.White.copy(alpha = lightingOpacity * 0.35f),
                                            Color.Transparent
                                        ),
                                        center = Offset(size.width * 0.5f, size.height * 0.3f),
                                        radius = size.minDimension * 0.65f
                                    )
                                )
                            }
                            LightingStyle.DIRECTIONAL_SUN, LightingStyle.WARM_GLOW -> {
                                drawRect(
                                    brush = Brush.linearGradient(
                                        colors = listOf(
                                            Color(0xFFFFB74D).copy(alpha = lightingOpacity * 0.35f),
                                            Color.Transparent
                                        ),
                                        start = Offset(0f, 0f),
                                        end = Offset(size.width, size.height)
                                    )
                                )
                            }
                            LightingStyle.COOL_MOONLIGHT -> {
                                drawRect(
                                    brush = Brush.linearGradient(
                                        colors = listOf(
                                            Color(0xFF60A5FA).copy(alpha = lightingOpacity * 0.3f),
                                            Color.Transparent
                                        ),
                                        start = Offset(size.width, 0f),
                                        end = Offset(0f, size.height)
                                    )
                                )
                            }
                            LightingStyle.NEON_BEAM -> {
                                drawRect(
                                    brush = Brush.linearGradient(
                                        colors = listOf(
                                            AvaPurpleLight.copy(alpha = lightingOpacity * 0.4f),
                                            Color.Transparent
                                        ),
                                        start = Offset(0f, size.height * 0.5f),
                                        end = Offset(size.width, size.height * 0.5f)
                                    )
                                )
                            }
                            else -> {}
                        }
                    }
                }

                // Before / After Indicator Badge
                Box(
                    modifier = Modifier
                        .align(Alignment.TopEnd)
                        .padding(8.dp)
                        .clip(RoundedCornerShape(6.dp))
                        .background(if (isComparingOriginal) Color(0xFFEF4444) else AvaPurplePrimary)
                        .padding(horizontal = 6.dp, vertical = 3.dp)
                ) {
                    Text(
                        text = if (isComparingOriginal)
                            (if (isAr) "الصورة الأصلية" else "ORIGINAL")
                        else
                            (if (isAr) "المعاينة المعدلة" else "EDITED"),
                        color = Color.White,
                        fontSize = 9.sp,
                        fontWeight = FontWeight.Bold
                    )
                }

                // If not user photo, show subtle prompt to upload personal photo
                if (!isUserPersonalPhoto) {
                    Box(
                        modifier = Modifier
                            .align(Alignment.BottomCenter)
                            .padding(bottom = 10.dp)
                            .clip(RoundedCornerShape(20.dp))
                            .background(Color.Black.copy(alpha = 0.75f))
                            .border(1.dp, AvaPurplePrimary.copy(alpha = 0.5f), RoundedCornerShape(20.dp))
                            .clickable { openUserPhotoPicker() }
                            .padding(horizontal = 12.dp, vertical = 6.dp)
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(
                                imageVector = Icons.Default.AddPhotoAlternate,
                                contentDescription = null,
                                tint = AvaPurpleLight,
                                modifier = Modifier.size(14.dp)
                            )
                            Spacer(modifier = Modifier.width(6.dp))
                            Text(
                                text = if (isAr) "اضغط لإضافة صورتك الشخصية" else "Tap to add your photo",
                                color = Color.White,
                                fontSize = 11.sp,
                                fontWeight = FontWeight.Medium
                            )
                        }
                    }
                }
            }
        }

        // -------------------------------------------------------------
        // 4. COMPACT SETTINGS PANEL (Appears strictly above toolbar)
        // -------------------------------------------------------------
        AnimatedVisibility(
            visible = activeCategory != null,
            enter = expandVertically() + fadeIn(),
            exit = shrinkVertically() + fadeOut()
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(AvaCard)
                    .border(1.dp, AvaCardBorder, RoundedCornerShape(topStart = 14.dp, topEnd = 14.dp))
                    .padding(horizontal = 14.dp, vertical = 10.dp)
            ) {
                // Panel Header: Category Name + Close Button
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        activeCategory?.let { cat ->
                            Icon(cat.icon, contentDescription = null, tint = AvaPurpleLight, modifier = Modifier.size(16.dp))
                            Spacer(modifier = Modifier.width(6.dp))
                            Text(
                                text = if (isAr) cat.labelAr else cat.labelEn,
                                color = AvaTextPrimary,
                                fontSize = 13.sp,
                                fontWeight = FontWeight.Bold
                            )
                        }
                    }
                    TextButton(
                        onClick = { activeCategory = null },
                        contentPadding = PaddingValues(0.dp),
                        modifier = Modifier.height(24.dp)
                    ) {
                        Text(if (isAr) "إخفاء" else "Hide", color = AvaTextSecondary, fontSize = 11.sp)
                    }
                }

                Spacer(modifier = Modifier.height(4.dp))

                // Active Category Tools & Sliders
                when (activeCategory) {
                    PortraitToolCategory.ADJUST -> {
                        Column(modifier = Modifier.fillMaxWidth()) {
                            CompactSlider(
                                label = if (isAr) "التعريض (Exposure)" else "Exposure",
                                value = adjustments.exposure,
                                range = -1.5f..1.5f,
                                onValueChange = {
                                    pushHistory()
                                    adjustments = adjustments.copy(exposure = it)
                                }
                            )
                            CompactSlider(
                                label = if (isAr) "السطوع (Brightness)" else "Brightness",
                                value = adjustments.brightness,
                                range = -60f..60f,
                                onValueChange = {
                                    pushHistory()
                                    adjustments = adjustments.copy(brightness = it)
                                }
                            )
                            CompactSlider(
                                label = if (isAr) "التباين (Contrast)" else "Contrast",
                                value = adjustments.contrast,
                                range = 0.5f..2.0f,
                                onReset = { adjustments = adjustments.copy(contrast = 1.0f) },
                                onValueChange = {
                                    pushHistory()
                                    adjustments = adjustments.copy(contrast = it)
                                }
                            )
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.spacedBy(10.dp)
                            ) {
                                CompactSlider(
                                    label = if (isAr) "المناطق الفاتحة" else "Highlights",
                                    value = adjustments.highlights,
                                    range = -40f..40f,
                                    onValueChange = { adjustments = adjustments.copy(highlights = it) },
                                    modifier = Modifier.weight(1f)
                                )
                                CompactSlider(
                                    label = if (isAr) "الظلال" else "Shadows",
                                    value = adjustments.shadows,
                                    range = -40f..40f,
                                    onValueChange = { adjustments = adjustments.copy(shadows = it) },
                                    modifier = Modifier.weight(1f)
                                )
                            }
                        }
                    }

                    PortraitToolCategory.RETOUCH -> {
                        Column(modifier = Modifier.fillMaxWidth()) {
                            Text(
                                text = if (isAr) "أدوات تجميل ونقاء البورتريه مع الحفاظ على ملامحك الأصلية"
                                else "Enhance skin and facial depth while preserving authenticity",
                                color = AvaTextSecondary,
                                fontSize = 11.sp
                            )
                            Spacer(modifier = Modifier.height(6.dp))

                            CompactSlider(
                                label = if (isAr) "تنعيم البشرة الطبيعي" else "Skin Smoothing",
                                value = adjustments.highlights.coerceAtLeast(0f),
                                range = 0f..50f,
                                onValueChange = {
                                    pushHistory()
                                    adjustments = adjustments.copy(highlights = it, clarity = adjustments.clarity.coerceAtLeast(10f))
                                }
                            )
                            CompactSlider(
                                label = if (isAr) "إشراقة الوجه (Face Glow)" else "Face Glow & Radiance",
                                value = adjustments.brightness.coerceAtLeast(0f),
                                range = 0f..35f,
                                onValueChange = {
                                    pushHistory()
                                    adjustments = adjustments.copy(brightness = it, exposure = it * 0.015f)
                                }
                            )
                            CompactSlider(
                                label = if (isAr) "وضوح ونقاء الملامح" else "Feature Clarity",
                                value = adjustments.clarity,
                                range = 0f..60f,
                                onValueChange = {
                                    pushHistory()
                                    adjustments = adjustments.copy(clarity = it)
                                }
                            )
                            CompactSlider(
                                label = if (isAr) "حدة العيون والتفاصيل" else "Sharpness Detail",
                                value = adjustments.sharpness,
                                range = 0f..80f,
                                onValueChange = {
                                    pushHistory()
                                    adjustments = adjustments.copy(sharpness = it)
                                }
                            )
                        }
                    }

                    PortraitToolCategory.FILTERS -> {
                        val portraitFilters = listOf(
                            Pair("Original", FilterPreset.NONE),
                            Pair("Studio Portrait", FilterPreset.STUDIO_PORTRAIT),
                            Pair("Natural Clean", FilterPreset.NATURAL),
                            Pair("Soft Light", FilterPreset.SOFT_LIGHT),
                            Pair("Fashion Editorial", FilterPreset.FASHION_EDITORIAL),
                            Pair("Golden Hour", FilterPreset.GOLDEN),
                            Pair("Film Noir", FilterPreset.DARK_STREET),
                            Pair("Classic B&W", FilterPreset.CLASSIC_BW),
                            Pair("Teal & Orange", FilterPreset.TEAL_AND_ORANGE),
                            Pair("Night Photo", FilterPreset.NIGHT_PHOTOGRAPHY)
                        )

                        Column(modifier = Modifier.fillMaxWidth()) {
                            // Filter intensity slider
                            CompactSlider(
                                label = if (isAr) "كثافة الفلتر" else "Filter Intensity",
                                value = filterIntensity,
                                range = 0f..1f,
                                valueFormat = { "${(it * 100).toInt()}%" },
                                onValueChange = { filterIntensity = it }
                            )

                            Spacer(modifier = Modifier.height(6.dp))

                            LazyRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                                items(portraitFilters) { (name, preset) ->
                                    val isSel = activeFilter == preset
                                    Box(
                                        modifier = Modifier
                                            .clip(RoundedCornerShape(8.dp))
                                            .background(if (isSel) AvaPurplePrimary else AvaCardBorder)
                                            .border(1.dp, if (isSel) AvaPurpleLight else AvaDivider, RoundedCornerShape(8.dp))
                                            .clickable {
                                                pushHistory()
                                                activeFilter = preset
                                            }
                                            .padding(horizontal = 12.dp, vertical = 7.dp)
                                    ) {
                                        Text(
                                            text = name,
                                            color = if (isSel) Color.White else AvaTextSecondary,
                                            fontSize = 11.sp,
                                            fontWeight = if (isSel) FontWeight.Bold else FontWeight.Normal
                                        )
                                    }
                                }
                            }
                        }
                    }

                    PortraitToolCategory.COLOR -> {
                        Column(modifier = Modifier.fillMaxWidth()) {
                            CompactSlider(
                                label = if (isAr) "تشبع الألوان (Saturation)" else "Saturation",
                                value = adjustments.saturation,
                                range = 0f..2.0f,
                                onReset = { adjustments = adjustments.copy(saturation = 1.0f) },
                                onValueChange = {
                                    pushHistory()
                                    adjustments = adjustments.copy(saturation = it)
                                }
                            )
                            CompactSlider(
                                label = if (isAr) "الحرارة والدفء (Temperature)" else "Warmth & Temperature",
                                value = adjustments.temperature,
                                range = -50f..50f,
                                onValueChange = {
                                    pushHistory()
                                    adjustments = adjustments.copy(temperature = it)
                                }
                            )
                            CompactSlider(
                                label = if (isAr) "حيوية درجات البشرة (Vibrance)" else "Skin Vibrance",
                                value = adjustments.vibrance,
                                range = -50f..50f,
                                onValueChange = {
                                    pushHistory()
                                    adjustments = adjustments.copy(vibrance = it)
                                }
                            )
                            CompactSlider(
                                label = if (isAr) "تدرج التلاشي (Fade)" else "Matte Fade",
                                value = adjustments.fade,
                                range = 0f..50f,
                                onValueChange = {
                                    pushHistory()
                                    adjustments = adjustments.copy(fade = it)
                                }
                            )
                        }
                    }

                    PortraitToolCategory.BOKEH -> {
                        Column(modifier = Modifier.fillMaxWidth()) {
                            CompactSlider(
                                label = if (isAr) "قوة عزل الخلفية (Bokeh Strength)" else "Bokeh Blur Strength",
                                value = bokehIntensity,
                                range = 0f..100f,
                                onValueChange = {
                                    pushHistory()
                                    bokehIntensity = it
                                    activeBokehBlur = if (it > 0f) BlurType.BACKGROUND else BlurType.NONE
                                }
                            )

                            Spacer(modifier = Modifier.height(8.dp))

                            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                                val blurStyles = listOf(
                                    Pair("Off", BlurType.NONE),
                                    Pair("Lens Bokeh", BlurType.BACKGROUND),
                                    Pair("Radial Focus", BlurType.RADIAL),
                                    Pair("Soft Field", BlurType.GAUSSIAN)
                                )
                                blurStyles.forEach { (title, bType) ->
                                    val isSel = activeBokehBlur == bType
                                    Box(
                                        modifier = Modifier
                                            .clip(RoundedCornerShape(8.dp))
                                            .background(if (isSel) AvaPurplePrimary else AvaCardBorder)
                                            .clickable {
                                                pushHistory()
                                                activeBokehBlur = bType
                                                if (bType != BlurType.NONE && bokehIntensity == 0f) {
                                                    bokehIntensity = 40f
                                                }
                                            }
                                            .padding(horizontal = 10.dp, vertical = 6.dp)
                                    ) {
                                        Text(title, color = if (isSel) Color.White else AvaTextSecondary, fontSize = 11.sp)
                                    }
                                }
                            }
                        }
                    }

                    PortraitToolCategory.LIGHTING -> {
                        Column(modifier = Modifier.fillMaxWidth()) {
                            CompactSlider(
                                label = if (isAr) "شدة الإضاءة" else "Lighting Opacity",
                                value = lightingOpacity,
                                range = 0f..1f,
                                valueFormat = { "${(it * 100).toInt()}%" },
                                onValueChange = { lightingOpacity = it }
                            )

                            Spacer(modifier = Modifier.height(6.dp))

                            LazyRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                                items(LightingStyle.values()) { lStyle ->
                                    val isSel = activeLighting == lStyle
                                    Box(
                                        modifier = Modifier
                                            .clip(RoundedCornerShape(8.dp))
                                            .background(if (isSel) AvaPurplePrimary else AvaCardBorder)
                                            .clickable {
                                                pushHistory()
                                                activeLighting = lStyle
                                            }
                                            .padding(horizontal = 10.dp, vertical = 6.dp)
                                    ) {
                                        Text(lStyle.title, color = if (isSel) Color.White else AvaTextSecondary, fontSize = 11.sp)
                                    }
                                }
                            }
                        }
                    }

                    PortraitToolCategory.CROP -> {
                        Column(modifier = Modifier.fillMaxWidth()) {
                            Text(
                                text = if (isAr) "أبعاد البورتريه والتدوير" else "Portrait Aspect Ratio & Orientation",
                                color = AvaTextPrimary,
                                fontSize = 11.sp,
                                fontWeight = FontWeight.Medium
                            )
                            Spacer(modifier = Modifier.height(6.dp))

                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.spacedBy(8.dp)
                            ) {
                                listOf("1:1", "4:5", "3:4", "9:16").forEach { ratio ->
                                    val isSel = canvasRatio == ratio
                                    Box(
                                        modifier = Modifier
                                            .weight(1f)
                                            .clip(RoundedCornerShape(8.dp))
                                            .background(if (isSel) AvaPurplePrimary else AvaCardBorder)
                                            .clickable {
                                                pushHistory()
                                                canvasRatio = ratio
                                            }
                                            .padding(vertical = 8.dp),
                                        contentAlignment = Alignment.Center
                                    ) {
                                        Text(ratio, color = if (isSel) Color.White else AvaTextSecondary, fontSize = 11.sp, fontWeight = FontWeight.Bold)
                                    }
                                }
                            }

                            Spacer(modifier = Modifier.height(10.dp))

                            // Rotate & Flip buttons
                            Row(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                                Button(
                                    onClick = {
                                        pushHistory()
                                        rotationAngle = (rotationAngle + 90f) % 360f
                                    },
                                    colors = ButtonDefaults.buttonColors(containerColor = AvaCardBorder),
                                    shape = RoundedCornerShape(8.dp),
                                    modifier = Modifier.weight(1f).height(34.dp)
                                ) {
                                    Icon(Icons.Default.RotateRight, contentDescription = null, modifier = Modifier.size(16.dp))
                                    Spacer(modifier = Modifier.width(6.dp))
                                    Text(if (isAr) "تدوير 90°" else "Rotate 90°", fontSize = 11.sp)
                                }

                                Button(
                                    onClick = {
                                        pushHistory()
                                        flipH = !flipH
                                    },
                                    colors = ButtonDefaults.buttonColors(containerColor = AvaCardBorder),
                                    shape = RoundedCornerShape(8.dp),
                                    modifier = Modifier.weight(1f).height(34.dp)
                                ) {
                                    Icon(Icons.Default.Flip, contentDescription = null, modifier = Modifier.size(16.dp))
                                    Spacer(modifier = Modifier.width(6.dp))
                                    Text(if (isAr) "قلب أفقي" else "Flip H", fontSize = 11.sp)
                                }
                            }
                        }
                    }

                    PortraitToolCategory.DETAILS -> {
                        Column(modifier = Modifier.fillMaxWidth()) {
                            CompactSlider(
                                label = if (isAr) "تأطير سينمائي (Vignette)" else "Portrait Vignette",
                                value = adjustments.vignette,
                                range = 0f..60f,
                                onValueChange = {
                                    pushHistory()
                                    adjustments = adjustments.copy(vignette = it)
                                }
                            )
                            CompactSlider(
                                label = if (isAr) "حبيبات الفيلم (Film Grain)" else "Analog Film Grain",
                                value = adjustments.grain,
                                range = 0f..80f,
                                onValueChange = {
                                    pushHistory()
                                    adjustments = adjustments.copy(grain = it)
                                }
                            )
                            CompactSlider(
                                label = if (isAr) "الحدة المجهرية (Micro-Sharpness)" else "Micro-Sharpness",
                                value = adjustments.sharpness,
                                range = 0f..100f,
                                onValueChange = {
                                    pushHistory()
                                    adjustments = adjustments.copy(sharpness = it)
                                }
                            )
                        }
                    }

                    null -> {}
                }
            }
        }

        // -------------------------------------------------------------
        // 5. BOTTOM 8-CATEGORY QUICK TOOLBAR (Horizontally Scrollable)
        // -------------------------------------------------------------
        LazyRow(
            modifier = Modifier
                .fillMaxWidth()
                .background(AvaBlack)
                .border(1.dp, AvaDivider, RoundedCornerShape(0.dp))
                .padding(vertical = 6.dp),
            contentPadding = PaddingValues(horizontal = 10.dp),
            horizontalArrangement = Arrangement.spacedBy(14.dp)
        ) {
            items(PortraitToolCategory.values()) { category ->
                val isSel = activeCategory == category
                Column(
                    modifier = Modifier
                        .clickable {
                            activeCategory = if (isSel) null else category
                        }
                        .padding(horizontal = 4.dp, vertical = 2.dp)
                        .testTag("portrait_tab_${category.name.lowercase()}"),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Box(
                        modifier = Modifier
                            .size(38.dp)
                            .clip(CircleShape)
                            .background(if (isSel) AvaPurplePrimary else AvaCard)
                            .border(1.dp, if (isSel) AvaPurpleLight else AvaCardBorder, CircleShape),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = category.icon,
                            contentDescription = if (isAr) category.labelAr else category.labelEn,
                            tint = if (isSel) Color.White else AvaTextSecondary,
                            modifier = Modifier.size(19.dp)
                        )
                    }

                    Spacer(modifier = Modifier.height(3.dp))

                    Text(
                        text = if (isAr) category.labelAr else category.labelEn,
                        color = if (isSel) AvaPurpleLight else AvaTextSecondary,
                        fontSize = 10.sp,
                        fontWeight = if (isSel) FontWeight.Bold else FontWeight.Normal
                    )
                }
            }
        }
    }
}
