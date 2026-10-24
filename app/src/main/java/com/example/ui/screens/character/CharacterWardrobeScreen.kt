package com.example.ui.screens.character

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
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Checkroom
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.MonetizationOn
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
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
import com.example.data.model.WardrobeItemEntity
import com.example.ui.components.AvaSubTopBar
import com.example.ui.components.CoinsBadge
import com.example.ui.theme.AvaBlack
import com.example.ui.theme.AvaCard
import com.example.ui.theme.AvaCardBorder
import com.example.ui.theme.AvaCardSelected
import com.example.ui.theme.AvaGold
import com.example.ui.theme.AvaPurpleContainer
import com.example.ui.theme.AvaPurpleLight
import com.example.ui.theme.AvaPurplePrimary
import com.example.ui.theme.AvaSuccess
import com.example.ui.theme.AvaTextPrimary
import com.example.ui.theme.AvaTextSecondary
import com.example.ui.viewmodel.AppScreen
import com.example.ui.viewmodel.MainViewModel
import com.example.util.AppLanguage
import com.example.util.stringRes

@Composable
fun CharacterWardrobeScreen(
    viewModel: MainViewModel,
    modifier: Modifier = Modifier
) {
    val user by viewModel.currentUser.collectAsState()
    val wardrobeItems by viewModel.wardrobeItems.collectAsState()
    val currentLang by viewModel.appLanguage.collectAsState()

    var selectedCategory by remember { mutableStateOf("all") }

    val categories = listOf(
        Pair("all", "All"),
        Pair("tops", "Tops"),
        Pair("bottoms", "Bottoms"),
        Pair("shoes", "Shoes"),
        Pair("accessories", "Accessories"),
        Pair("streetwear", "Streetwear"),
        Pair("basketball", "Basketball"),
        Pair("premium", "Premium")
    )

    val filteredItems = remember(wardrobeItems, selectedCategory) {
        if (selectedCategory == "all") {
            wardrobeItems
        } else if (selectedCategory in listOf("streetwear", "basketball", "premium")) {
            wardrobeItems.filter { it.styleTag == selectedCategory }
        } else {
            wardrobeItems.filter { it.category == selectedCategory }
        }
    }

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(AvaBlack)
    ) {
        AvaSubTopBar(
            title = stringRes("wardrobe_store"),
            onBack = { viewModel.navigateBack() },
            actions = {
                CoinsBadge(
                    balance = user?.coinsBalance ?: 0,
                    onClick = { viewModel.navigateTo(AppScreen.COINS_WALLET) }
                )
            }
        )

        // Filter chips
        LazyRow(
            contentPadding = PaddingValues(horizontal = 16.dp, vertical = 6.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            items(categories) { (key, label) ->
                val isSelected = selectedCategory == key
                FilterChip(
                    selected = isSelected,
                    onClick = { selectedCategory = key },
                    label = { Text(text = label, fontSize = 12.sp, color = if (isSelected) Color.White else AvaTextSecondary) },
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

        // Grid of wardrobe items
        LazyVerticalGrid(
            columns = GridCells.Fixed(2),
            contentPadding = PaddingValues(16.dp),
            horizontalArrangement = Arrangement.spacedBy(12.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp),
            modifier = Modifier.fillMaxSize()
        ) {
            items(filteredItems) { item ->
                WardrobeCard(
                    item = item,
                    userBalance = user?.coinsBalance ?: 0,
                    isArabic = currentLang == AppLanguage.ARABIC,
                    onTryOn = {
                        viewModel.activeCharacter.value = null
                        viewModel.navigateTo(AppScreen.CHARACTER_STUDIO)
                    },
                    onPurchase = {
                        viewModel.purchaseWardrobe(item)
                    }
                )
            }
        }
    }
}

@Composable
fun WardrobeCard(
    item: WardrobeItemEntity,
    userBalance: Int,
    isArabic: Boolean,
    onTryOn: () -> Unit,
    onPurchase: () -> Unit
) {
    Card(
        shape = RoundedCornerShape(14.dp),
        colors = CardDefaults.cardColors(containerColor = AvaCard),
        border = CardDefaults.outlinedCardBorder().copy(brush = androidx.compose.ui.graphics.SolidColor(AvaCardBorder)),
        modifier = Modifier.fillMaxWidth().testTag("wardrobe_item_${item.id}")
    ) {
        Column(modifier = Modifier.padding(12.dp)) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(100.dp)
                    .clip(RoundedCornerShape(10.dp))
                    .background(AvaPurpleContainer.copy(alpha = 0.4f)),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Default.Checkroom,
                    contentDescription = null,
                    tint = AvaPurpleLight,
                    modifier = Modifier.size(36.dp)
                )

                if (item.isUnlocked) {
                    Box(
                        modifier = Modifier
                            .align(Alignment.TopEnd)
                            .padding(6.dp)
                            .clip(RoundedCornerShape(6.dp))
                            .background(AvaSuccess.copy(alpha = 0.2f))
                            .padding(horizontal = 6.dp, vertical = 2.dp)
                    ) {
                        Text(text = "OWNED", color = AvaSuccess, fontSize = 9.sp, fontWeight = FontWeight.Bold)
                    }
                } else {
                    Box(
                        modifier = Modifier
                            .align(Alignment.TopEnd)
                            .padding(6.dp)
                            .clip(RoundedCornerShape(6.dp))
                            .background(AvaGold.copy(alpha = 0.2f))
                            .padding(horizontal = 6.dp, vertical = 2.dp)
                    ) {
                        Text(text = "${item.priceCoins} Coins", color = AvaGold, fontSize = 9.sp, fontWeight = FontWeight.Bold)
                    }
                }
            }

            Spacer(modifier = Modifier.height(10.dp))

            Text(
                text = if (isArabic) item.nameAr else item.nameEn,
                color = AvaTextPrimary,
                fontSize = 13.sp,
                fontWeight = FontWeight.SemiBold,
                maxLines = 1
            )
            Text(
                text = item.styleTag.uppercase(),
                color = AvaPurpleLight,
                fontSize = 10.sp,
                fontWeight = FontWeight.Medium
            )

            Spacer(modifier = Modifier.height(10.dp))

            if (item.isUnlocked) {
                Button(
                    onClick = onTryOn,
                    shape = RoundedCornerShape(8.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = AvaPurplePrimary),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(text = stringRes("try_on"), fontSize = 11.sp, fontWeight = FontWeight.Bold)
                }
            } else {
                Button(
                    onClick = onPurchase,
                    enabled = userBalance >= item.priceCoins,
                    shape = RoundedCornerShape(8.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = AvaGold),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Icon(imageVector = Icons.Default.Lock, contentDescription = null, modifier = Modifier.size(12.dp), tint = Color.Black)
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(
                        text = "${item.priceCoins} Coins",
                        color = Color.Black,
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
            }
        }
    }
}
