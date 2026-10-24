package com.example.ui.screens.adstudio

import androidx.compose.animation.AnimatedContent
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowForward
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Image
import androidx.compose.material.icons.filled.Palette
import androidx.compose.material.icons.filled.Upload
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
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
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.ProjectEntity
import com.example.ui.components.AvaSubTopBar
import com.example.ui.theme.AvaBlack
import com.example.ui.theme.AvaCard
import com.example.ui.theme.AvaCardBorder
import com.example.ui.theme.AvaCardSelected
import com.example.ui.theme.AvaGold
import com.example.ui.theme.AvaPurpleContainer
import com.example.ui.theme.AvaPurpleLight
import com.example.ui.theme.AvaPurplePrimary
import com.example.ui.theme.AvaSurface
import com.example.ui.theme.AvaTextPrimary
import com.example.ui.theme.AvaTextSecondary
import com.example.ui.viewmodel.AppScreen
import com.example.ui.viewmodel.MainViewModel
import com.example.util.StudioLayer
import com.example.util.stringRes
import java.util.UUID

@Composable
fun AdStudioWizardScreen(
    viewModel: MainViewModel,
    modifier: Modifier = Modifier
) {
    var currentStep by remember { mutableIntStateOf(1) }

    // Form State
    var selectedAdType by remember { mutableStateOf("Product Advertisement") }
    var brandName by remember { mutableStateOf("AVA NOIR") }
    var productName by remember { mutableStateOf("Cyber Matte Chrono") }
    var productDesc by remember { mutableStateOf("Precision titanium luxury aesthetic for creators.") }
    var headline by remember { mutableStateOf("DEFINING MODERN LUXURY") }
    var subtitle by remember { mutableStateOf("Exclusively Crafted for Visionaries") }
    var callToAction by remember { mutableStateOf("SHOP DROP 01") }
    var webHandle by remember { mutableStateOf("@avanor.studio") }
    var targetPlatform by remember { mutableStateOf("Instagram & TikTok") }
    var selectedRatio by remember { mutableStateOf("4:5") }
    var selectedStyle by remember { mutableStateOf("Dark Premium") }

    val adTypes = listOf(
        Pair("Product Advertisement", "Showcase physical or digital goods with pro lighting"),
        Pair("Clothing Advertisement", "Fashion lookbooks, apparel drops and street garments"),
        Pair("Brand Promotion", "Elevate brand identity with high-end luxury typography"),
        Pair("Instagram Story", "Full-bleed 9:16 vertical viral engagement format"),
        Pair("Instagram Post", "High-conversion 4:5 or 1:1 portrait aesthetic"),
        Pair("YouTube Thumbnail", "Eye-catching high contrast 16:9 click-worthy layout"),
        Pair("General Social Media", "Adaptive promotional layouts for Twitter, LinkedIn, Web"),
        Pair("Custom Design", "Full creative freedom with custom layer canvas")
    )

    val canvasFormats = listOf(
        Triple("Instagram Story", "9:16", "1080 x 1920 px"),
        Triple("Instagram Portrait", "4:5", "1080 x 1350 px"),
        Triple("Square Post", "1:1", "1080 x 1080 px"),
        Triple("Landscape Banner", "16:9", "1920 x 1080 px")
    )

    val visualStyles = listOf(
        "Dark Premium", "Minimal", "Luxury", "Streetwear",
        "Sport & Energy", "Fashion Editorial", "Clean Commercial",
        "Natural & Earthy", "Bold Typography", "Cyber Neon"
    )

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(AvaBlack)
    ) {
        AvaSubTopBar(
            title = when (currentStep) {
                1 -> stringRes("ad_step_1_title")
                2 -> stringRes("ad_step_2_title")
                3 -> stringRes("ad_step_3_title")
                else -> stringRes("ad_step_4_title")
            },
            onBack = {
                if (currentStep > 1) {
                    currentStep--
                } else {
                    viewModel.navigateBack()
                }
            }
        )

        // Step Progress Indicator
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 6.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            for (step in 1..4) {
                Box(
                    modifier = Modifier
                        .weight(1f)
                        .height(4.dp)
                        .clip(RoundedCornerShape(2.dp))
                        .background(
                            if (step <= currentStep) AvaPurplePrimary else AvaCardBorder
                        )
                )
            }
        }

        // Body Content by Step
        Box(
            modifier = Modifier
                .weight(1f)
                .fillMaxWidth()
        ) {
            when (currentStep) {
                1 -> {
                    // STEP 1: Select Ad Type
                    LazyColumn(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(16.dp),
                        verticalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        items(adTypes.size) { idx ->
                            val (title, desc) = adTypes[idx]
                            val isSelected = selectedAdType == title
                            Card(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .clickable { selectedAdType = title }
                                    .testTag("ad_type_$idx"),
                                shape = RoundedCornerShape(14.dp),
                                colors = CardDefaults.cardColors(
                                    containerColor = if (isSelected) AvaCardSelected else AvaCard
                                ),
                                border = CardDefaults.outlinedCardBorder().copy(
                                    brush = androidx.compose.ui.graphics.SolidColor(
                                        if (isSelected) AvaPurplePrimary else AvaCardBorder
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
                                    Column(modifier = Modifier.weight(1f)) {
                                        Text(
                                            text = title,
                                            color = AvaTextPrimary,
                                            fontSize = 15.sp,
                                            fontWeight = FontWeight.Bold
                                        )
                                        Spacer(modifier = Modifier.height(4.dp))
                                        Text(
                                            text = desc,
                                            color = AvaTextSecondary,
                                            fontSize = 12.sp
                                        )
                                    }
                                    if (isSelected) {
                                        Icon(
                                            imageVector = Icons.Default.CheckCircle,
                                            contentDescription = "Selected",
                                            tint = AvaPurpleLight
                                        )
                                    }
                                }
                            }
                        }
                    }
                }

                2 -> {
                    // STEP 2: Product & Brand Details Form
                    LazyColumn(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(16.dp),
                        verticalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        item {
                            OutlinedTextField(
                                value = brandName,
                                onValueChange = { brandName = it },
                                label = { Text(stringRes("brand_name")) },
                                modifier = Modifier.fillMaxWidth().testTag("input_brand_name"),
                                colors = OutlinedTextFieldDefaults.colors(
                                    focusedBorderColor = AvaPurplePrimary,
                                    unfocusedBorderColor = AvaCardBorder,
                                    focusedTextColor = AvaTextPrimary,
                                    unfocusedTextColor = AvaTextPrimary
                                )
                            )
                        }
                        item {
                            OutlinedTextField(
                                value = productName,
                                onValueChange = { productName = it },
                                label = { Text(stringRes("product_name")) },
                                modifier = Modifier.fillMaxWidth().testTag("input_product_name"),
                                colors = OutlinedTextFieldDefaults.colors(
                                    focusedBorderColor = AvaPurplePrimary,
                                    unfocusedBorderColor = AvaCardBorder,
                                    focusedTextColor = AvaTextPrimary,
                                    unfocusedTextColor = AvaTextPrimary
                                )
                            )
                        }
                        item {
                            OutlinedTextField(
                                value = productDesc,
                                onValueChange = { productDesc = it },
                                label = { Text(stringRes("product_desc")) },
                                modifier = Modifier.fillMaxWidth(),
                                colors = OutlinedTextFieldDefaults.colors(
                                    focusedBorderColor = AvaPurplePrimary,
                                    unfocusedBorderColor = AvaCardBorder,
                                    focusedTextColor = AvaTextPrimary,
                                    unfocusedTextColor = AvaTextPrimary
                                )
                            )
                        }
                        item {
                            OutlinedTextField(
                                value = headline,
                                onValueChange = { headline = it },
                                label = { Text(stringRes("main_headline")) },
                                modifier = Modifier.fillMaxWidth(),
                                colors = OutlinedTextFieldDefaults.colors(
                                    focusedBorderColor = AvaPurplePrimary,
                                    unfocusedBorderColor = AvaCardBorder,
                                    focusedTextColor = AvaTextPrimary,
                                    unfocusedTextColor = AvaTextPrimary
                                )
                            )
                        }
                        item {
                            OutlinedTextField(
                                value = subtitle,
                                onValueChange = { subtitle = it },
                                label = { Text(stringRes("supporting_text")) },
                                modifier = Modifier.fillMaxWidth(),
                                colors = OutlinedTextFieldDefaults.colors(
                                    focusedBorderColor = AvaPurplePrimary,
                                    unfocusedBorderColor = AvaCardBorder,
                                    focusedTextColor = AvaTextPrimary,
                                    unfocusedTextColor = AvaTextPrimary
                                )
                            )
                        }
                        item {
                            OutlinedTextField(
                                value = callToAction,
                                onValueChange = { callToAction = it },
                                label = { Text(stringRes("call_to_action")) },
                                modifier = Modifier.fillMaxWidth(),
                                colors = OutlinedTextFieldDefaults.colors(
                                    focusedBorderColor = AvaPurplePrimary,
                                    unfocusedBorderColor = AvaCardBorder,
                                    focusedTextColor = AvaTextPrimary,
                                    unfocusedTextColor = AvaTextPrimary
                                )
                            )
                        }
                        item {
                            OutlinedTextField(
                                value = webHandle,
                                onValueChange = { webHandle = it },
                                label = { Text(stringRes("website_handle")) },
                                modifier = Modifier.fillMaxWidth(),
                                colors = OutlinedTextFieldDefaults.colors(
                                    focusedBorderColor = AvaPurplePrimary,
                                    unfocusedBorderColor = AvaCardBorder,
                                    focusedTextColor = AvaTextPrimary,
                                    unfocusedTextColor = AvaTextPrimary
                                )
                            )
                        }
                    }
                }

                3 -> {
                    // STEP 3: Canvas Format
                    LazyColumn(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(16.dp),
                        verticalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        items(canvasFormats.size) { idx ->
                            val (name, ratio, dims) = canvasFormats[idx]
                            val isSelected = selectedRatio == ratio
                            Card(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .clickable { selectedRatio = ratio },
                                shape = RoundedCornerShape(14.dp),
                                colors = CardDefaults.cardColors(
                                    containerColor = if (isSelected) AvaCardSelected else AvaCard
                                ),
                                border = CardDefaults.outlinedCardBorder().copy(
                                    brush = androidx.compose.ui.graphics.SolidColor(
                                        if (isSelected) AvaPurplePrimary else AvaCardBorder
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
                                    Column {
                                        Text(
                                            text = name,
                                            color = AvaTextPrimary,
                                            fontSize = 15.sp,
                                            fontWeight = FontWeight.Bold
                                        )
                                        Text(
                                            text = "$ratio • $dims",
                                            color = AvaTextSecondary,
                                            fontSize = 12.sp
                                        )
                                    }
                                    if (isSelected) {
                                        Icon(
                                            imageVector = Icons.Default.CheckCircle,
                                            contentDescription = null,
                                            tint = AvaPurpleLight
                                        )
                                    }
                                }
                            }
                        }
                    }
                }

                4 -> {
                    // STEP 4: Visual Direction
                    LazyColumn(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(16.dp),
                        verticalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        items(visualStyles.size) { idx ->
                            val style = visualStyles[idx]
                            val isSelected = selectedStyle == style
                            Card(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .clickable { selectedStyle = style },
                                shape = RoundedCornerShape(12.dp),
                                colors = CardDefaults.cardColors(
                                    containerColor = if (isSelected) AvaCardSelected else AvaCard
                                ),
                                border = CardDefaults.outlinedCardBorder().copy(
                                    brush = androidx.compose.ui.graphics.SolidColor(
                                        if (isSelected) AvaPurplePrimary else AvaCardBorder
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
                                    Text(
                                        text = style,
                                        color = AvaTextPrimary,
                                        fontSize = 14.sp,
                                        fontWeight = FontWeight.Medium
                                    )
                                    if (isSelected) {
                                        Icon(
                                            imageVector = Icons.Default.CheckCircle,
                                            contentDescription = null,
                                            tint = AvaPurpleLight
                                        )
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }

        // Bottom Navigation Action Bar
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            if (currentStep > 1) {
                OutlinedButton(
                    onClick = { currentStep-- },
                    modifier = Modifier.weight(1f),
                    shape = RoundedCornerShape(10.dp),
                    colors = ButtonDefaults.outlinedButtonColors(contentColor = AvaTextPrimary)
                ) {
                    Text(text = stringRes("previous_step"))
                }
            }

            Button(
                onClick = {
                    if (currentStep < 4) {
                        currentStep++
                    } else {
                        // Open Editor populated with Ad info
                        val user = viewModel.currentUser.value
                        val newAdProject = ProjectEntity(
                            id = UUID.randomUUID().toString(),
                            userId = user?.id ?: "guest",
                            title = "$brandName — $headline",
                            projectType = "advertisement",
                            aspectRatio = selectedRatio,
                            layersJson = "ad_layers"
                        )
                        viewModel.activeProject.value = newAdProject
                        viewModel.navigateTo(AppScreen.IMAGE_EDITOR)
                    }
                },
                modifier = Modifier.weight(if (currentStep > 1) 2f else 1f).testTag("btn_ad_wizard_next"),
                shape = RoundedCornerShape(10.dp),
                colors = ButtonDefaults.buttonColors(containerColor = AvaPurplePrimary)
            ) {
                Text(
                    text = if (currentStep < 4) stringRes("next_step") else stringRes("open_in_editor"),
                    color = Color.White,
                    fontWeight = FontWeight.Bold
                )
            }
        }
    }
}
