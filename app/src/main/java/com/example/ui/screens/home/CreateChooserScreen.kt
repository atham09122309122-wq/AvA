package com.example.ui.screens.home

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
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.Brush
import androidx.compose.material.icons.filled.Campaign
import androidx.compose.material.icons.filled.Face
import androidx.compose.material.icons.filled.Layers
import androidx.compose.material.icons.filled.PhotoCamera
import androidx.compose.material.icons.filled.ViewCarousel
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.components.BrandSignatureBadge
import com.example.ui.theme.AvaBlack
import com.example.ui.theme.AvaCard
import com.example.ui.theme.AvaCardBorder
import com.example.ui.theme.AvaGold
import com.example.ui.theme.AvaPurpleContainer
import com.example.ui.theme.AvaPurpleLight
import com.example.ui.theme.AvaPurplePrimary
import com.example.ui.theme.AvaSuccess
import com.example.ui.theme.AvaTextPrimary
import com.example.ui.theme.AvaTextSecondary
import com.example.ui.viewmodel.AppScreen
import com.example.ui.viewmodel.MainViewModel
import com.example.util.stringRes

data class CreationOption(
    val title: String,
    val subtitle: String,
    val icon: ImageVector,
    val accentColor: Color,
    val screen: AppScreen
)

@Composable
fun CreateChooserScreen(
    viewModel: MainViewModel,
    modifier: Modifier = Modifier
) {
    val options = listOf(
        CreationOption(
            title = stringRes("personal_photo_title"),
            subtitle = stringRes("personal_photo_desc"),
            icon = Icons.Default.PhotoCamera,
            accentColor = AvaSuccess,
            screen = AppScreen.PERSONAL_PHOTO
        ),
        CreationOption(
            title = stringRes("ad_studio_title"),
            subtitle = stringRes("ad_studio_desc"),
            icon = Icons.Default.Campaign,
            accentColor = AvaPurplePrimary,
            screen = AppScreen.AD_STUDIO
        ),
        CreationOption(
            title = stringRes("character_studio_title"),
            subtitle = stringRes("character_studio_sub"),
            icon = Icons.Default.Face,
            accentColor = AvaGold,
            screen = AppScreen.CHARACTER_STUDIO
        ),
        CreationOption(
            title = "Story Designer (9:16)",
            subtitle = "Vertical viral formats for Instagram, TikTok, and Snapchat",
            icon = Icons.Default.ViewCarousel,
            accentColor = Color(0xFFEC4899),
            screen = AppScreen.STORY_DESIGNER
        ),
        CreationOption(
            title = "Trending Templates Library",
            subtitle = "Pre-designed luxury, streetwear and commercial compositions",
            icon = Icons.Default.AutoAwesome,
            accentColor = Color(0xFF3B82F6),
            screen = AppScreen.TEMPLATES
        ),
        CreationOption(
            title = "Blank Canvas Editor",
            subtitle = "Full creative control with layers, text, shapes and filters",
            icon = Icons.Default.Brush,
            accentColor = AvaPurpleLight,
            screen = AppScreen.IMAGE_EDITOR
        )
    )

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(AvaBlack)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 20.dp, vertical = 14.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Studio Creation Hub",
                    color = AvaTextPrimary,
                    fontSize = 22.sp,
                    fontWeight = FontWeight.Bold
                )
                BrandSignatureBadge()
            }
            Text(
                text = "Select a dedicated studio workflow to start creating.",
                color = AvaTextSecondary,
                fontSize = 13.sp
            )
        }

        LazyColumn(
            contentPadding = PaddingValues(horizontal = 16.dp, vertical = 6.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            items(options) { opt ->
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable { viewModel.navigateTo(opt.screen) },
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(containerColor = AvaCard),
                    border = CardDefaults.outlinedCardBorder().copy(brush = androidx.compose.ui.graphics.SolidColor(AvaCardBorder))
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier.weight(1f)
                        ) {
                            Box(
                                modifier = Modifier
                                    .size(46.dp)
                                    .clip(RoundedCornerShape(12.dp))
                                    .background(opt.accentColor.copy(alpha = 0.18f)),
                                contentAlignment = Alignment.Center
                            ) {
                                Icon(
                                    imageVector = opt.icon,
                                    contentDescription = null,
                                    tint = opt.accentColor,
                                    modifier = Modifier.size(24.dp)
                                )
                            }
                            Spacer(modifier = Modifier.width(14.dp))
                            Column {
                                Text(
                                    text = opt.title,
                                    color = AvaTextPrimary,
                                    fontSize = 15.sp,
                                    fontWeight = FontWeight.Bold
                                )
                                Spacer(modifier = Modifier.height(2.dp))
                                Text(
                                    text = opt.subtitle,
                                    color = AvaTextSecondary,
                                    fontSize = 11.sp,
                                    lineHeight = 16.sp
                                )
                            }
                        }
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowForward,
                            contentDescription = null,
                            tint = AvaTextSecondary,
                            modifier = Modifier.size(18.dp)
                        )
                    }
                }
            }
        }
    }
}
