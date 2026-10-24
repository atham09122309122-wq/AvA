package com.example.ui.screens.explore

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
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.Campaign
import androidx.compose.material.icons.filled.Face
import androidx.compose.material.icons.filled.Palette
import androidx.compose.material.icons.filled.PhotoCamera
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.TrendingUp
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
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

@Composable
fun ExploreScreen(
    viewModel: MainViewModel,
    modifier: Modifier = Modifier
) {
    var searchQuery by remember { mutableStateOf("") }

    val trendingCategories = listOf(
        Triple("Streetwear Ads", Icons.Default.Campaign, Color(0xFF6D28D9)),
        Triple("Cinematic Portraits", Icons.Default.PhotoCamera, Color(0xFF10B981)),
        Triple("Virtual Avatars", Icons.Default.Face, Color(0xFFF59E0B)),
        Triple("Typography Art", Icons.Default.Palette, Color(0xFF3B82F6))
    )

    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .background(AvaBlack),
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // Header
        item {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column {
                    Text(text = stringRes("nav_explore"), color = AvaTextPrimary, fontSize = 22.sp, fontWeight = FontWeight.Bold)
                    Text(text = "Discover aesthetics and creative inspiration", color = AvaTextSecondary, fontSize = 12.sp)
                }
                BrandSignatureBadge()
            }
        }

        // Search Bar
        item {
            OutlinedTextField(
                value = searchQuery,
                onValueChange = { searchQuery = it },
                placeholder = { Text(stringRes("search_hint"), color = AvaTextSecondary, fontSize = 13.sp) },
                leadingIcon = { Icon(Icons.Default.Search, contentDescription = null, tint = AvaTextSecondary) },
                modifier = Modifier.fillMaxWidth().testTag("explore_search_bar"),
                shape = RoundedCornerShape(12.dp),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = AvaPurplePrimary,
                    unfocusedBorderColor = AvaCardBorder,
                    focusedTextColor = Color.White
                )
            )
        }

        // Category Cards Strip
        item {
            Text(text = "Creative Hubs", color = AvaTextPrimary, fontSize = 15.sp, fontWeight = FontWeight.Bold)
            Spacer(modifier = Modifier.height(10.dp))
            LazyRow(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                items(trendingCategories) { (title, icon, accent) ->
                    Card(
                        modifier = Modifier
                            .width(135.dp)
                            .height(95.dp)
                            .clickable {
                                when (title) {
                                    "Streetwear Ads" -> viewModel.navigateTo(AppScreen.AD_STUDIO)
                                    "Cinematic Portraits" -> viewModel.navigateTo(AppScreen.PERSONAL_PHOTO)
                                    "Virtual Avatars" -> viewModel.navigateTo(AppScreen.CHARACTERS)
                                    else -> viewModel.navigateTo(AppScreen.TEMPLATES)
                                }
                            },
                        shape = RoundedCornerShape(12.dp),
                        colors = CardDefaults.cardColors(containerColor = AvaCard),
                        border = CardDefaults.outlinedCardBorder().copy(brush = androidx.compose.ui.graphics.SolidColor(AvaCardBorder))
                    ) {
                        Column(
                            modifier = Modifier.padding(12.dp),
                            verticalArrangement = Arrangement.SpaceBetween
                        ) {
                            Box(
                                modifier = Modifier
                                    .size(32.dp)
                                    .clip(CircleShape)
                                    .background(accent.copy(alpha = 0.2f)),
                                contentAlignment = Alignment.Center
                            ) {
                                Icon(imageVector = icon, contentDescription = null, tint = accent, modifier = Modifier.size(18.dp))
                            }
                            Text(text = title, color = AvaTextPrimary, fontSize = 12.sp, fontWeight = FontWeight.SemiBold)
                        }
                    }
                }
            }
        }

        // Featured Creator Spotlight (Ayham)
        item {
            Text(text = "Featured Master Creator", color = AvaTextPrimary, fontSize = 15.sp, fontWeight = FontWeight.Bold)
            Spacer(modifier = Modifier.height(8.dp))
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = AvaCard),
                border = CardDefaults.outlinedCardBorder().copy(
                    brush = Brush.horizontalGradient(listOf(AvaPurplePrimary.copy(alpha = 0.5f), AvaCardBorder))
                )
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Box(
                        modifier = Modifier
                            .size(50.dp)
                            .clip(CircleShape)
                            .background(Brush.linearGradient(listOf(AvaPurplePrimary, Color(0xFF6D28D9)))),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(text = "AY", color = Color.White, fontSize = 18.sp, fontWeight = FontWeight.Black)
                    }
                    Spacer(modifier = Modifier.width(14.dp))
                    Column {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Text(text = "Ayham", color = AvaTextPrimary, fontSize = 16.sp, fontWeight = FontWeight.Bold)
                            Spacer(modifier = Modifier.width(6.dp))
                            Icon(imageVector = Icons.Default.AutoAwesome, contentDescription = null, tint = AvaGold, modifier = Modifier.size(14.dp))
                        }
                        Text(text = "@ayham_creative • Founder & Master Designer", color = AvaPurpleLight, fontSize = 11.sp)
                        Text(text = "Pioneering minimalist luxury digital art & 2D character studio.", color = AvaTextSecondary, fontSize = 11.sp)
                    }
                }
            }
        }

        // Trending Collections
        item {
            Text(text = "Trending Aesthetic Drops", color = AvaTextPrimary, fontSize = 15.sp, fontWeight = FontWeight.Bold)
            Spacer(modifier = Modifier.height(8.dp))

            val drops = listOf(
                Pair("MIDNIGHT AURA COLLECTION", "Streetwear garments, heavy typography, and titanium jewelry."),
                Pair("NEON RETRO COURT 2026", "Basketball court performance visual identities and high-top kicks."),
                Pair("PARISIAN MINIMAL NOIR", "Quiet luxury editorial aesthetics and studio portrait enhancements.")
            )

            drops.forEach { (title, desc) ->
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 4.dp)
                        .clickable { viewModel.navigateTo(AppScreen.TEMPLATES) },
                    shape = RoundedCornerShape(12.dp),
                    colors = CardDefaults.cardColors(containerColor = AvaCard),
                    border = CardDefaults.outlinedCardBorder().copy(brush = androidx.compose.ui.graphics.SolidColor(AvaCardBorder))
                ) {
                    Column(modifier = Modifier.padding(14.dp)) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(text = title, color = AvaTextPrimary, fontSize = 13.sp, fontWeight = FontWeight.Bold)
                            Icon(imageVector = Icons.Default.TrendingUp, contentDescription = null, tint = AvaSuccess, modifier = Modifier.size(16.dp))
                        }
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(text = desc, color = AvaTextSecondary, fontSize = 11.sp, lineHeight = 16.sp)
                    }
                }
            }
        }
    }
}
