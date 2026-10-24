package com.example.ui.screens.templates

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
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
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
import com.example.ui.theme.AvaGold
import com.example.ui.theme.AvaPurpleContainer
import com.example.ui.theme.AvaPurpleLight
import com.example.ui.theme.AvaPurplePrimary
import com.example.ui.theme.AvaTextPrimary
import com.example.ui.theme.AvaTextSecondary
import com.example.ui.viewmodel.AppScreen
import com.example.ui.viewmodel.MainViewModel
import com.example.util.stringRes
import java.util.UUID

data class StudioTemplate(
    val id: String,
    val title: String,
    val category: String,
    val aspectRatio: String,
    val headline: String,
    val subtitle: String,
    val bgGradient: List<Color>
)

@Composable
fun TemplatesScreen(
    viewModel: MainViewModel,
    modifier: Modifier = Modifier
) {
    var searchQuery by remember { mutableStateOf("") }
    var selectedCategory by remember { mutableStateOf("All") }

    val categories = listOf(
        "All", "Fashion", "Clothing", "Sports", "Basketball",
        "Product Ads", "Business", "Minimal", "Dark Premium",
        "Typography", "Social Stories"
    )

    val templates = remember {
        listOf(
            StudioTemplate(
                id = "tpl_01",
                title = "Noir Luxury Editorial",
                category = "Fashion",
                aspectRatio = "4:5",
                headline = "AVANT GARDE 2026",
                subtitle = "Paris • Tokyo • Milan",
                bgGradient = listOf(Color(0xFF1E1035), Color(0xFF09080E))
            ),
            StudioTemplate(
                id = "tpl_02",
                title = "Apex Court Basketball",
                category = "Basketball",
                aspectRatio = "9:16",
                headline = "GAME 7 FINALS",
                subtitle = "Unstoppable Intensity",
                bgGradient = listOf(Color(0xFF3F192E), Color(0xFF0F0E14))
            ),
            StudioTemplate(
                id = "tpl_03",
                title = "Midnight Minimalist Drop",
                category = "Streetwear",
                aspectRatio = "1:1",
                headline = "RAW SILHOUETTE",
                subtitle = "Heavyweight 450GSM Cotton",
                bgGradient = listOf(Color(0xFF13111C), Color(0xFF09080E))
            ),
            StudioTemplate(
                id = "tpl_04",
                title = "Titanium Product Banner",
                category = "Product Ads",
                aspectRatio = "16:9",
                headline = "CHRONO PRECISION",
                subtitle = "Engineered Without Compromise",
                bgGradient = listOf(Color(0xFF262626), Color(0xFF0F0F0F))
            ),
            StudioTemplate(
                id = "tpl_05",
                title = "Cyber Vibe Story",
                category = "Social Stories",
                aspectRatio = "9:16",
                headline = "NEON REVOLUTION",
                subtitle = "AVA Creative Collective",
                bgGradient = listOf(Color(0xFF4C1D95), Color(0xFF1E1035))
            ),
            StudioTemplate(
                id = "tpl_06",
                title = "Editorial Lifestyle Look",
                category = "Personal Lifestyle",
                aspectRatio = "4:5",
                headline = "GOLDEN HOUR",
                subtitle = "Natural Light Aesthetics",
                bgGradient = listOf(Color(0xFF3D2C0D), Color(0xFF09080E))
            )
        )
    }

    val filteredTemplates = remember(searchQuery, selectedCategory) {
        templates.filter { t ->
            val matchesCat = selectedCategory == "All" || t.category.equals(selectedCategory, ignoreCase = true)
            val matchesQuery = searchQuery.isEmpty() || t.title.contains(searchQuery, ignoreCase = true) || t.headline.contains(searchQuery, ignoreCase = true)
            matchesCat && matchesQuery
        }
    }

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(AvaBlack)
    ) {
        AvaSubTopBar(
            title = "Templates & Story Designs",
            onBack = { viewModel.navigateBack() }
        )

        // Search Bar
        OutlinedTextField(
            value = searchQuery,
            onValueChange = { searchQuery = it },
            placeholder = { Text(stringRes("search_hint"), color = AvaTextSecondary, fontSize = 13.sp) },
            leadingIcon = { Icon(Icons.Default.Search, contentDescription = null, tint = AvaTextSecondary) },
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 6.dp),
            shape = RoundedCornerShape(12.dp),
            colors = OutlinedTextFieldDefaults.colors(
                focusedBorderColor = AvaPurplePrimary,
                unfocusedBorderColor = AvaCardBorder,
                focusedTextColor = Color.White
            )
        )

        // Category Filter
        LazyRow(
            contentPadding = PaddingValues(horizontal = 16.dp, vertical = 6.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            items(categories) { cat ->
                val isSelected = selectedCategory == cat
                FilterChip(
                    selected = isSelected,
                    onClick = { selectedCategory = cat },
                    label = { Text(text = cat, fontSize = 12.sp, color = if (isSelected) Color.White else AvaTextSecondary) },
                    colors = FilterChipDefaults.filterChipColors(
                        selectedContainerColor = AvaPurplePrimary,
                        containerColor = AvaCard
                    ),
                    border = FilterChipDefaults.filterChipBorder(
                        borderColor = if (isSelected) AvaPurplePrimary else AvaCardBorder,
                        enabled = true,
                        selected = isSelected
                    )
                )
            }
        }

        // Templates Grid
        LazyVerticalGrid(
            columns = GridCells.Fixed(2),
            contentPadding = PaddingValues(16.dp),
            horizontalArrangement = Arrangement.spacedBy(12.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp),
            modifier = Modifier.fillMaxSize()
        ) {
            items(filteredTemplates) { tpl ->
                TemplateCard(
                    template = tpl,
                    onUse = {
                        val user = viewModel.currentUser.value
                        val projectFromTpl = ProjectEntity(
                            id = UUID.randomUUID().toString(),
                            userId = user?.id ?: "guest",
                            title = tpl.title,
                            projectType = "template",
                            aspectRatio = tpl.aspectRatio,
                            layersJson = "template_layers"
                        )
                        viewModel.activeProject.value = projectFromTpl
                        viewModel.navigateTo(AppScreen.IMAGE_EDITOR)
                    }
                )
            }
        }
    }
}

@Composable
fun TemplateCard(
    template: StudioTemplate,
    onUse: () -> Unit
) {
    var isFav by remember { mutableStateOf(false) }

    Card(
        shape = RoundedCornerShape(14.dp),
        colors = CardDefaults.cardColors(containerColor = AvaCard),
        border = CardDefaults.outlinedCardBorder().copy(brush = androidx.compose.ui.graphics.SolidColor(AvaCardBorder)),
        modifier = Modifier.fillMaxWidth().testTag("tpl_${template.id}")
    ) {
        Column {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(130.dp)
                    .background(Brush.verticalGradient(template.bgGradient)),
                contentAlignment = Alignment.Center
            ) {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = Modifier.padding(8.dp)
                ) {
                    Text(
                        text = template.headline,
                        color = Color.White,
                        fontSize = 13.sp,
                        fontWeight = FontWeight.Black,
                        maxLines = 1
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = template.subtitle,
                        color = AvaPurpleLight,
                        fontSize = 10.sp,
                        maxLines = 1
                    )
                }

                // Ratio badge
                Box(
                    modifier = Modifier
                        .align(Alignment.TopStart)
                        .padding(8.dp)
                        .clip(RoundedCornerShape(6.dp))
                        .background(Color.Black.copy(alpha = 0.6f))
                        .padding(horizontal = 6.dp, vertical = 2.dp)
                ) {
                    Text(text = template.aspectRatio, color = Color.White, fontSize = 9.sp, fontWeight = FontWeight.Bold)
                }

                // Favorite button
                IconButton(
                    onClick = { isFav = !isFav },
                    modifier = Modifier
                        .align(Alignment.TopEnd)
                        .size(30.dp)
                ) {
                    Icon(
                        imageVector = if (isFav) Icons.Default.Favorite else Icons.Default.FavoriteBorder,
                        contentDescription = "Favorite",
                        tint = if (isFav) Color(0xFFEF4444) else Color.White,
                        modifier = Modifier.size(16.dp)
                    )
                }
            }

            Column(modifier = Modifier.padding(10.dp)) {
                Text(
                    text = template.title,
                    color = AvaTextPrimary,
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Bold,
                    maxLines = 1
                )
                Text(
                    text = template.category,
                    color = AvaTextSecondary,
                    fontSize = 10.sp
                )
                Spacer(modifier = Modifier.height(8.dp))
                Button(
                    onClick = onUse,
                    shape = RoundedCornerShape(8.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = AvaPurplePrimary),
                    modifier = Modifier.fillMaxWidth().height(32.dp),
                    contentPadding = PaddingValues(0.dp)
                ) {
                    Text(text = stringRes("use_template"), fontSize = 11.sp, fontWeight = FontWeight.Bold)
                }
            }
        }
    }
}
