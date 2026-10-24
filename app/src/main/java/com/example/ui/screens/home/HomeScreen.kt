package com.example.ui.screens.home

import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.BlurOn
import androidx.compose.material.icons.filled.Campaign
import androidx.compose.material.icons.filled.Crop
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Folder
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Palette
import androidx.compose.material.icons.filled.PhotoCamera
import androidx.compose.material.icons.filled.PhotoLibrary
import androidx.compose.material.icons.filled.TextFields
import androidx.compose.material.icons.filled.Tune
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.ProjectEntity
import com.example.ui.components.BrandSignatureBadge
import com.example.ui.theme.AvaBlack
import com.example.ui.theme.AvaCard
import com.example.ui.theme.AvaCardBorder
import com.example.ui.theme.AvaDivider
import com.example.ui.theme.AvaPurpleContainer
import com.example.ui.theme.AvaPurpleLight
import com.example.ui.theme.AvaPurplePrimary
import com.example.ui.theme.AvaTextPrimary
import com.example.ui.theme.AvaTextSecondary
import com.example.ui.viewmodel.AppScreen
import com.example.ui.viewmodel.MainViewModel
import com.example.util.AppLanguage
import com.example.util.DemoArtwork
import com.example.util.DemoArtworkProvider
import com.example.util.stringRes
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@Composable
fun HomeScreen(
    viewModel: MainViewModel,
    modifier: Modifier = Modifier
) {
    val recentProjects by viewModel.recentProjects.collectAsState()
    val appLanguage by viewModel.appLanguage.collectAsState()
    val isAr = appLanguage == AppLanguage.ARABIC

    var selectedDemoForPreview by remember { mutableStateOf<DemoArtwork?>(null) }
    var projectToDelete by remember { mutableStateOf<ProjectEntity?>(null) }

    // Real system photo picker launcher
    val photoPickerLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri: Uri? ->
        uri?.let {
            viewModel.createNewProject(
                title = "New Photo Edit",
                aspectRatio = "1:1",
                imageUri = it.toString()
            )
        }
    }

    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .background(AvaBlack),
        contentPadding = PaddingValues(bottom = 36.dp)
    ) {
        // -------------------------------------------------------------
        // 1. Premium Welcome & Action Header
        // -------------------------------------------------------------
        item {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 8.dp)
                    .clip(RoundedCornerShape(20.dp))
                    .background(
                        Brush.verticalGradient(
                            listOf(Color(0xFF23163A), Color(0xFF13101E))
                        )
                    )
                    .border(1.dp, AvaPurplePrimary.copy(alpha = 0.35f), RoundedCornerShape(20.dp))
                    .padding(20.dp)
            ) {
                Column {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        BrandSignatureBadge()

                        Box(
                            modifier = Modifier
                                .clip(RoundedCornerShape(8.dp))
                                .background(Color(0xFF1F1D2C))
                                .padding(horizontal = 8.dp, vertical = 4.dp)
                        ) {
                            Text(
                                text = if (isAr) "محرر محلي غير متصل" else "Local Offline Studio",
                                color = AvaPurpleLight,
                                fontSize = 11.sp,
                                fontWeight = FontWeight.Medium
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(14.dp))

                    Text(
                        text = if (isAr) "استوديو AVA الإبداعي" else "AVA Creative Studio",
                        color = AvaTextPrimary,
                        fontSize = 24.sp,
                        fontWeight = FontWeight.Bold
                    )

                    Spacer(modifier = Modifier.height(6.dp))

                    Text(
                        text = if (isAr)
                            "محرر صور متطور، استوديوهات إعلانية احترافية، ومصمم قصص بصري لحفظ وتعديل تصاميمك محلياً."
                        else
                            "Advanced photo editing suite, professional advertisement studio, and story designer with secure on-device project storage.",
                        color = AvaTextSecondary,
                        fontSize = 13.sp,
                        lineHeight = 19.sp
                    )

                    Spacer(modifier = Modifier.height(18.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        Button(
                            onClick = { photoPickerLauncher.launch("image/*") },
                            modifier = Modifier
                                .weight(1f)
                                .height(46.dp)
                                .testTag("btn_upload_photo"),
                            shape = RoundedCornerShape(12.dp),
                            colors = ButtonDefaults.buttonColors(containerColor = AvaPurplePrimary)
                        ) {
                            Icon(Icons.Default.PhotoLibrary, contentDescription = null, modifier = Modifier.size(18.dp))
                            Spacer(modifier = Modifier.width(6.dp))
                            Text(
                                text = if (isAr) "فتح صورة من جهازي" else "Open Photo",
                                color = Color.White,
                                fontWeight = FontWeight.Bold,
                                fontSize = 13.sp
                            )
                        }

                        OutlinedButton(
                            onClick = { viewModel.navigateTo(AppScreen.CREATE_CHOOSER) },
                            modifier = Modifier
                                .weight(1f)
                                .height(46.dp)
                                .testTag("btn_create_design"),
                            shape = RoundedCornerShape(12.dp),
                            colors = ButtonDefaults.outlinedButtonColors(contentColor = AvaTextPrimary),
                            border = ButtonDefaults.outlinedButtonBorder().copy(brush = SolidColor(AvaPurpleLight.copy(alpha = 0.5f)))
                        ) {
                            Icon(Icons.Default.Add, contentDescription = null, modifier = Modifier.size(18.dp))
                            Spacer(modifier = Modifier.width(6.dp))
                            Text(
                                text = if (isAr) "إنشاء تصميم جديد" else "New Canvas",
                                color = AvaTextPrimary,
                                fontWeight = FontWeight.Bold,
                                fontSize = 13.sp
                            )
                        }
                    }
                }
            }
        }

        // -------------------------------------------------------------
        // 2. Creative Studios Section
        // -------------------------------------------------------------
        item {
            Spacer(modifier = Modifier.height(14.dp))
            SectionHeader(
                title = if (isAr) "استوديوهات التصميم المتخصصة" else "Specialized Creative Studios",
                subtitle = if (isAr) "أدوات مخصصة للصور الشخصية، الحملات الإعلانية ومحتوى السوشيال ميديا" else "Purpose-built workspaces for portraits, branding, and stories"
            )
            Spacer(modifier = Modifier.height(10.dp))
        }

        item {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                // Personal Photo Studio Card
                StudioHeroCard(
                    title = if (isAr) "استوديو الصور الشخصية" else "Personal Photo Studio",
                    description = if (isAr) "تحسين الإضاءة وملامح الوجه مع الحفاظ الكامل على الهوية الطبيعية دون تشويه اصطناعي." else "Portrait enhancement and cinematic lighting while preserving natural human identity.",
                    icon = Icons.Default.PhotoCamera,
                    badgeText = if (isAr) "أصيل 100%" else "Authentic Identity",
                    accentGradient = listOf(Color(0xFF8B5CF6), Color(0xFF6D28D9)),
                    onClick = { viewModel.navigateTo(AppScreen.PERSONAL_PHOTO) },
                    testTag = "card_personal_photo_studio"
                )

                // Advertisement Studio Card
                StudioHeroCard(
                    title = if (isAr) "استوديو الإعلانات والتسويق" else "Advertisement Studio",
                    description = if (isAr) "صانع بوسترات وبانرات ترويجية احترافية للمنتجات والخدمات والمتاجر الإلكترونية." else "Build high-converting product ads, marketing flyers, and promotional banners.",
                    icon = Icons.Default.Campaign,
                    badgeText = if (isAr) "تسويق احترافي" else "Marketing Ready",
                    accentGradient = listOf(Color(0xFF6366F1), Color(0xFF4338CA)),
                    onClick = { viewModel.navigateTo(AppScreen.AD_STUDIO) },
                    testTag = "card_ad_studio"
                )

                // Story Designer Card
                StudioHeroCard(
                    title = if (isAr) "مصمم القصص الرأسية (9:16)" else "Story Designer (9:16)",
                    description = if (isAr) "قوالب رأسية عصرية لمواقع التواصل مع طبقات كتابة وعناصر بصرية ديناميكية." else "Vertical canvas layouts with typographic layers and modern visual composition.",
                    icon = Icons.Default.Palette,
                    badgeText = if (isAr) "إنستغرام وتيك توك" else "Stories & Reels",
                    accentGradient = listOf(Color(0xFF9333EA), Color(0xFF7E22CE)),
                    onClick = { viewModel.navigateTo(AppScreen.STORY_DESIGNER) },
                    testTag = "card_story_designer"
                )
            }
        }

        // -------------------------------------------------------------
        // 3. Featured Visual Inspiration & Demo Gallery
        // -------------------------------------------------------------
        item {
            Spacer(modifier = Modifier.height(20.dp))
            SectionHeader(
                title = if (isAr) "معرض الإلهام والقوالب التجريبية" else "Curated Demo Artwork & Inspiration",
                subtitle = if (isAr) "نماذج فنية حقيقية لاستعراض إمكانيات المحرر — اضغط على أي نموذج للبدء بتعديله فوراً" else "Actual design previews showing AVA capabilities — tap any to open in Photo Editor"
            )
            Spacer(modifier = Modifier.height(10.dp))

            LazyRow(
                contentPadding = PaddingValues(horizontal = 16.dp),
                horizontalArrangement = Arrangement.spacedBy(14.dp)
            ) {
                items(DemoArtworkProvider.demoArtworks) { artwork ->
                    DemoArtworkCard(
                        artwork = artwork,
                        isAr = isAr,
                        onClick = { selectedDemoForPreview = artwork }
                    )
                }
            }
        }

        // -------------------------------------------------------------
        // 4. Quick Creative Tools
        // -------------------------------------------------------------
        item {
            Spacer(modifier = Modifier.height(22.dp))
            SectionHeader(
                title = if (isAr) "أدوات التعديل السريع" else "Useful Quick Tools",
                subtitle = if (isAr) "اختصارات سريعة لفتح محرر الصور بأداة معينة" else "Direct shortcuts to open specific photo tools"
            )
            Spacer(modifier = Modifier.height(10.dp))

            val quickTools = listOf(
                Pair("قص وضبط الأبعاد", Icons.Default.Crop),
                Pair("إضاءة وفلاتر", Icons.Default.Tune),
                Pair("عزل وتغبيش", Icons.Default.BlurOn),
                Pair("إضافة نصوص", Icons.Default.TextFields),
                Pair("أبيض وأسود فاخر", Icons.Default.AutoAwesome)
            )

            LazyRow(
                contentPadding = PaddingValues(horizontal = 16.dp),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                items(quickTools) { (label, icon) ->
                    Card(
                        modifier = Modifier
                            .width(135.dp)
                            .clickable {
                                viewModel.createNewProject(
                                    title = label,
                                    aspectRatio = "1:1"
                                )
                            },
                        shape = RoundedCornerShape(14.dp),
                        colors = CardDefaults.cardColors(containerColor = AvaCard),
                        border = CardDefaults.outlinedCardBorder().copy(brush = SolidColor(AvaCardBorder))
                    ) {
                        Column(
                            modifier = Modifier.padding(14.dp),
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Box(
                                modifier = Modifier
                                    .size(44.dp)
                                    .clip(CircleShape)
                                    .background(AvaPurpleContainer),
                                contentAlignment = Alignment.Center
                            ) {
                                Icon(
                                    imageVector = icon,
                                    contentDescription = label,
                                    tint = AvaPurpleLight,
                                    modifier = Modifier.size(22.dp)
                                )
                            }
                            Spacer(modifier = Modifier.height(10.dp))
                            Text(
                                text = label,
                                color = AvaTextPrimary,
                                fontSize = 12.sp,
                                fontWeight = FontWeight.Medium,
                                maxLines = 1
                            )
                        }
                    }
                }
            }
        }

        // -------------------------------------------------------------
        // 5. Recent Local Projects
        // -------------------------------------------------------------
        item {
            Spacer(modifier = Modifier.height(22.dp))
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column {
                    Text(
                        text = if (isAr) "مشاريعي المحفوظة محلياً" else "Recent Local Projects",
                        color = AvaTextPrimary,
                        fontSize = 17.sp,
                        fontWeight = FontWeight.Bold
                    )
                    Text(
                        text = if (isAr) "المشاريع المحفوظة في ذاكرة هذا الجهاز" else "Designs saved on this device",
                        color = AvaTextSecondary,
                        fontSize = 12.sp
                    )
                }

                if (recentProjects.isNotEmpty()) {
                    TextButton(onClick = { viewModel.navigateTo(AppScreen.MY_PROJECTS) }) {
                        Text(
                            text = if (isAr) "عرض الكل" else "View All",
                            color = AvaPurpleLight,
                            fontSize = 12.sp
                        )
                    }
                }
            }
            Spacer(modifier = Modifier.height(10.dp))
        }

        if (recentProjects.isEmpty()) {
            item {
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp),
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(containerColor = AvaCard),
                    border = CardDefaults.outlinedCardBorder().copy(brush = SolidColor(AvaCardBorder))
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(24.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Box(
                            modifier = Modifier
                                .size(50.dp)
                                .clip(CircleShape)
                                .background(AvaPurpleContainer),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(Icons.Default.Folder, contentDescription = null, tint = AvaPurpleLight)
                        }
                        Spacer(modifier = Modifier.height(12.dp))
                        Text(
                            text = if (isAr) "لا توجد مشاريع محلية بعد" else "No local projects yet",
                            color = AvaTextPrimary,
                            fontSize = 14.sp,
                            fontWeight = FontWeight.Bold
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = if (isAr) "ابدأ تصميم جديد أو افتح قالباً تجريبياً لحفظه محلياً في جهازك."
                            else "Start a new design or customize any demo template to save it here.",
                            color = AvaTextSecondary,
                            fontSize = 12.sp,
                            lineHeight = 16.sp
                        )
                        Spacer(modifier = Modifier.height(14.dp))
                        Button(
                            onClick = { viewModel.navigateTo(AppScreen.CREATE_CHOOSER) },
                            shape = RoundedCornerShape(10.dp),
                            colors = ButtonDefaults.buttonColors(containerColor = AvaPurplePrimary)
                        ) {
                            Text(if (isAr) "بدء أول تصميم" else "Start First Design", color = Color.White, fontSize = 12.sp)
                        }
                    }
                }
            }
        } else {
            items(recentProjects.take(3)) { project ->
                val dateStr = SimpleDateFormat("MMM dd, yyyy", Locale.getDefault()).format(Date(project.updatedAt))
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp, vertical = 5.dp)
                        .clickable {
                            viewModel.activeProject.value = project
                            viewModel.navigateTo(AppScreen.IMAGE_EDITOR)
                        },
                    shape = RoundedCornerShape(14.dp),
                    colors = CardDefaults.cardColors(containerColor = AvaCard),
                    border = CardDefaults.outlinedCardBorder().copy(brush = SolidColor(AvaCardBorder))
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(14.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Box(
                                modifier = Modifier
                                    .size(46.dp)
                                    .clip(RoundedCornerShape(10.dp))
                                    .background(AvaPurpleContainer)
                                    .border(1.dp, AvaPurplePrimary.copy(alpha = 0.4f), RoundedCornerShape(10.dp)),
                                contentAlignment = Alignment.Center
                            ) {
                                Text(
                                    text = project.aspectRatio,
                                    color = AvaPurpleLight,
                                    fontSize = 12.sp,
                                    fontWeight = FontWeight.Bold
                                )
                            }
                            Spacer(modifier = Modifier.width(12.dp))
                            Column {
                                Text(
                                    text = project.title,
                                    color = AvaTextPrimary,
                                    fontSize = 14.sp,
                                    fontWeight = FontWeight.Bold
                                )
                                Text(
                                    text = "$dateStr • ${project.projectType.replace('_', ' ')}",
                                    color = AvaTextSecondary,
                                    fontSize = 11.sp
                                )
                            }
                        }

                        Row(verticalAlignment = Alignment.CenterVertically) {
                            IconButton(
                                onClick = {
                                    viewModel.activeProject.value = project
                                    viewModel.navigateTo(AppScreen.IMAGE_EDITOR)
                                }
                            ) {
                                Icon(Icons.Default.Edit, contentDescription = "Edit", tint = AvaPurpleLight, modifier = Modifier.size(18.dp))
                            }
                            IconButton(
                                onClick = { projectToDelete = project }
                            ) {
                                Icon(Icons.Default.Delete, contentDescription = "Delete", tint = AvaTextSecondary, modifier = Modifier.size(18.dp))
                            }
                        }
                    }
                }
            }
        }
    }

    // -------------------------------------------------------------
    // Demo Artwork Preview Dialog
    // -------------------------------------------------------------
    selectedDemoForPreview?.let { demo ->
        AlertDialog(
            onDismissRequest = { selectedDemoForPreview = null },
            title = {
                Text(
                    text = demo.title,
                    color = AvaTextPrimary,
                    fontSize = 17.sp,
                    fontWeight = FontWeight.Bold
                )
            },
            text = {
                Column {
                    Text(
                        text = "AVA STUDIO • ${demo.category.uppercase()}",
                        color = AvaPurpleLight,
                        fontSize = 11.sp,
                        fontWeight = FontWeight.SemiBold
                    )
                    Spacer(modifier = Modifier.height(6.dp))
                    Text(
                        text = demo.subtitle,
                        color = AvaTextSecondary,
                        fontSize = 13.sp,
                        lineHeight = 18.sp
                    )
                    Spacer(modifier = Modifier.height(10.dp))
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(8.dp))
                            .background(Color(0xFF1B1828))
                            .padding(10.dp)
                    ) {
                        Text(
                            text = if (isAr)
                                "يتضمن هذا النموذج إعدادات إضاءة ${demo.defaultLighting.title} وفلتر ${demo.defaultFilter.title} ونسب أبعاد ${demo.aspectRatio} جاهزة للتعديل المباشر."
                            else
                                "Includes configured ${demo.defaultLighting.title} lighting, ${demo.defaultFilter.title} color matrix, and ${demo.aspectRatio} canvas ratio.",
                            color = AvaTextSecondary,
                            fontSize = 11.sp
                        )
                    }
                }
            },
            confirmButton = {
                Button(
                    onClick = {
                        val artwork = demo
                        selectedDemoForPreview = null
                        viewModel.loadDemoArtworkIntoEditor(artwork)
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = AvaPurplePrimary)
                ) {
                    Text(if (isAr) "فتح في محرر الصور" else "Open in Photo Editor", color = Color.White, fontWeight = FontWeight.Bold)
                }
            },
            dismissButton = {
                TextButton(onClick = { selectedDemoForPreview = null }) {
                    Text(if (isAr) "إلغاء" else "Cancel", color = AvaTextSecondary)
                }
            },
            containerColor = AvaCard,
            shape = RoundedCornerShape(16.dp)
        )
    }

    // Delete Confirmation Dialog
    projectToDelete?.let { proj ->
        AlertDialog(
            onDismissRequest = { projectToDelete = null },
            title = { Text(if (isAr) "حذف المشروع" else "Delete Project", color = AvaTextPrimary, fontWeight = FontWeight.Bold) },
            text = { Text(if (isAr) "هل أنت متأكد من حذف \"${proj.title}\"؟ لا يمكن التراجع عن هذا الإجراء." else "Are you sure you want to delete \"${proj.title}\"?", color = AvaTextSecondary) },
            confirmButton = {
                Button(
                    onClick = {
                        viewModel.deleteProject(proj.id)
                        projectToDelete = null
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFEF4444))
                ) {
                    Text(if (isAr) "تأكيد الحذف" else "Delete", color = Color.White)
                }
            },
            dismissButton = {
                TextButton(onClick = { projectToDelete = null }) {
                    Text(if (isAr) "إلغاء" else "Cancel", color = AvaTextSecondary)
                }
            },
            containerColor = AvaCard
        )
    }
}

@Composable
private fun SectionHeader(
    title: String,
    subtitle: String,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp)
    ) {
        Text(
            text = title,
            color = AvaTextPrimary,
            fontSize = 17.sp,
            fontWeight = FontWeight.Bold
        )
        Text(
            text = subtitle,
            color = AvaTextSecondary,
            fontSize = 12.sp
        )
    }
}

@Composable
private fun StudioHeroCard(
    title: String,
    description: String,
    icon: ImageVector,
    badgeText: String,
    accentGradient: List<Color>,
    onClick: () -> Unit,
    testTag: String
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() }
            .testTag(testTag),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = AvaCard),
        border = CardDefaults.outlinedCardBorder().copy(
            brush = Brush.horizontalGradient(
                listOf(accentGradient.first().copy(alpha = 0.5f), AvaCardBorder)
            )
        )
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Row(
                modifier = Modifier.weight(1f),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Box(
                    modifier = Modifier
                        .size(48.dp)
                        .clip(RoundedCornerShape(12.dp))
                        .background(Brush.linearGradient(accentGradient)),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = icon,
                        contentDescription = title,
                        tint = Color.White,
                        modifier = Modifier.size(24.dp)
                    )
                }

                Spacer(modifier = Modifier.width(14.dp))

                Column {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text(
                            text = title,
                            color = AvaTextPrimary,
                            fontSize = 15.sp,
                            fontWeight = FontWeight.Bold
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Box(
                            modifier = Modifier
                                .clip(RoundedCornerShape(6.dp))
                                .background(accentGradient.first().copy(alpha = 0.2f))
                                .padding(horizontal = 6.dp, vertical = 2.dp)
                        ) {
                            Text(
                                text = badgeText,
                                color = AvaPurpleLight,
                                fontSize = 10.sp,
                                fontWeight = FontWeight.SemiBold
                            )
                        }
                    }
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = description,
                        color = AvaTextSecondary,
                        fontSize = 12.sp,
                        lineHeight = 17.sp,
                        maxLines = 2
                    )
                }
            }

            Icon(
                imageVector = Icons.AutoMirrored.Filled.ArrowForward,
                contentDescription = "Open",
                tint = AvaPurpleLight,
                modifier = Modifier
                    .size(20.dp)
                    .padding(start = 6.dp)
            )
        }
    }
}

@Composable
private fun DemoArtworkCard(
    artwork: DemoArtwork,
    isAr: Boolean,
    onClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .width(220.dp)
            .clickable { onClick() }
            .testTag("demo_art_${artwork.id}"),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = AvaCard),
        border = CardDefaults.outlinedCardBorder().copy(brush = SolidColor(AvaCardBorder))
    ) {
        Column {
            // Visual Preview Box
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(130.dp)
                    .background(
                        Brush.verticalGradient(
                            listOf(Color(artwork.accentColorHex).copy(alpha = 0.25f), Color(0xFF13111C))
                        )
                    )
                    .padding(12.dp)
            ) {
                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(6.dp))
                        .background(Color.Black.copy(alpha = 0.6f))
                        .padding(horizontal = 6.dp, vertical = 2.dp)
                        .align(Alignment.TopStart)
                ) {
                    Text(
                        text = artwork.category,
                        color = AvaPurpleLight,
                        fontSize = 10.sp,
                        fontWeight = FontWeight.Bold
                    )
                }

                Text(
                    text = artwork.initialHeadline,
                    color = Color.White,
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.align(Alignment.BottomStart)
                )
            }

            // Card Body
            Column(modifier = Modifier.padding(12.dp)) {
                Text(
                    text = artwork.title,
                    color = AvaTextPrimary,
                    fontSize = 13.sp,
                    fontWeight = FontWeight.Bold,
                    maxLines = 1
                )
                Spacer(modifier = Modifier.height(2.dp))
                Text(
                    text = artwork.subtitle,
                    color = AvaTextSecondary,
                    fontSize = 11.sp,
                    lineHeight = 15.sp,
                    maxLines = 2
                )
                Spacer(modifier = Modifier.height(8.dp))
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "Ratio: ${artwork.aspectRatio}",
                        color = AvaPurpleLight,
                        fontSize = 10.sp,
                        fontWeight = FontWeight.Medium
                    )
                    Text(
                        text = if (isAr) "تعديل القالب" else "Use Template",
                        color = AvaTextPrimary,
                        fontSize = 11.sp,
                        fontWeight = FontWeight.SemiBold
                    )
                }
            }
        }
    }
}
