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
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Chat
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Verified
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
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
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.UserEntity
import com.example.ui.components.AvaSubTopBar
import com.example.ui.components.BrandSignatureBadge
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

@Composable
fun ExploreUsersPostsScreen(
    viewModel: MainViewModel,
    modifier: Modifier = Modifier
) {
    val users by viewModel.users.collectAsState()
    val currentUser by viewModel.currentUser.collectAsState()
    var searchQuery by remember { mutableStateOf("") }
    var selectedFilter by remember { mutableStateOf("الكل") }

    val filters = listOf("الكل", "الموثقون", "المصممون", "المصورون")

    val filteredUsers = remember(users, searchQuery, selectedFilter) {
        users.filter { u ->
            val matchesQuery = searchQuery.isEmpty() ||
                    u.displayName.contains(searchQuery, ignoreCase = true) ||
                    u.username.contains(searchQuery, ignoreCase = true) ||
                    u.bio.contains(searchQuery, ignoreCase = true)
            val matchesFilter = when (selectedFilter) {
                "الموثقون" -> u.isVerified || u.isFounder
                else -> true
            }
            matchesQuery && matchesFilter
        }
    }

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(AvaBlack)
    ) {
        // Top Bar
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 10.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column {
                Text(text = "استكشاف المبدعين والمنشورات", color = AvaTextPrimary, fontSize = 20.sp, fontWeight = FontWeight.Bold)
                Text(text = "تواصل مع مجتمع AVA واكتشف أعمالهم", color = AvaTextSecondary, fontSize = 11.sp)
            }
            BrandSignatureBadge()
        }

        // Search Bar
        OutlinedTextField(
            value = searchQuery,
            onValueChange = { searchQuery = it },
            placeholder = { Text("ابحث بالاسم، المعرف أو التخصص...", color = AvaTextSecondary, fontSize = 12.sp) },
            leadingIcon = { Icon(Icons.Default.Search, contentDescription = null, tint = AvaTextSecondary) },
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 4.dp)
                .testTag("explore_user_search"),
            shape = RoundedCornerShape(12.dp),
            colors = OutlinedTextFieldDefaults.colors(
                focusedBorderColor = AvaPurplePrimary,
                unfocusedBorderColor = AvaCardBorder,
                focusedTextColor = Color.White
            )
        )

        // Filter chips
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 6.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            filters.forEach { f ->
                val isSelected = selectedFilter == f
                FilterChip(
                    selected = isSelected,
                    onClick = { selectedFilter = f },
                    label = { Text(text = f, fontSize = 11.sp, color = if (isSelected) Color.White else AvaTextSecondary) },
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

        // Users List
        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            contentPadding = PaddingValues(16.dp),
            verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            items(filteredUsers) { userItem ->
                val isSelf = userItem.id == currentUser?.id
                UserExploreCard(
                    user = userItem,
                    isSelf = isSelf,
                    onFollowClick = { viewModel.toggleFollow(userItem.id) },
                    onChatClick = {
                        viewModel.activeChatUserId.value = userItem.id
                        viewModel.activeChatUserName.value = userItem.displayName
                        viewModel.activeChatUserUsername.value = userItem.username
                        viewModel.navigateTo(AppScreen.CHAT_CONVERSATION)
                    }
                )
            }
        }
    }
}

@Composable
fun UserExploreCard(
    user: UserEntity,
    isSelf: Boolean,
    onFollowClick: () -> Unit,
    onChatClick: () -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth().testTag("user_card_${user.id}"),
        shape = RoundedCornerShape(14.dp),
        colors = CardDefaults.cardColors(containerColor = AvaCard),
        border = CardDefaults.outlinedCardBorder().copy(brush = androidx.compose.ui.graphics.SolidColor(AvaCardBorder))
    ) {
        Column(modifier = Modifier.padding(14.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Box(
                        modifier = Modifier
                            .size(46.dp)
                            .clip(CircleShape)
                            .background(
                                Brush.linearGradient(listOf(AvaPurplePrimary, Color(0xFF6D28D9)))
                            )
                            .border(1.5.dp, if (user.isFounder) AvaGold else AvaPurplePrimary, CircleShape),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = user.displayName.take(1).uppercase(),
                            color = Color.White,
                            fontWeight = FontWeight.Bold,
                            fontSize = 18.sp
                        )
                    }
                    Spacer(modifier = Modifier.width(10.dp))
                    Column {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Text(text = user.displayName, color = AvaTextPrimary, fontSize = 14.sp, fontWeight = FontWeight.Bold)
                            if (user.isFounder) {
                                Spacer(modifier = Modifier.width(4.dp))
                                Box(
                                    modifier = Modifier
                                        .clip(RoundedCornerShape(4.dp))
                                        .background(AvaGold.copy(alpha = 0.2f))
                                        .padding(horizontal = 4.dp, vertical = 1.dp)
                                ) {
                                    Text(text = "المؤسس", color = AvaGold, fontSize = 9.sp, fontWeight = FontWeight.Bold)
                                }
                            } else if (user.isVerified) {
                                Spacer(modifier = Modifier.width(4.dp))
                                Icon(imageVector = Icons.Default.Verified, contentDescription = "Verified", tint = AvaPurpleLight, modifier = Modifier.size(14.dp))
                            }
                        }
                        Text(text = "@${user.username} • ${user.followersCount} متابع", color = AvaTextSecondary, fontSize = 11.sp)
                    }
                }

                if (!isSelf) {
                    Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                        IconButton(onClick = onChatClick, modifier = Modifier.size(34.dp)) {
                            Icon(imageVector = Icons.AutoMirrored.Filled.Chat, contentDescription = "Chat", tint = AvaPurpleLight, modifier = Modifier.size(18.dp))
                        }

                        Button(
                            onClick = onFollowClick,
                            shape = RoundedCornerShape(8.dp),
                            colors = ButtonDefaults.buttonColors(containerColor = AvaPurplePrimary),
                            contentPadding = PaddingValues(horizontal = 12.dp, vertical = 4.dp),
                            modifier = Modifier.height(32.dp)
                        ) {
                            Text(text = "متابعة", color = Color.White, fontSize = 11.sp, fontWeight = FontWeight.Bold)
                        }
                    }
                }
            }

            if (user.bio.isNotBlank()) {
                Spacer(modifier = Modifier.height(8.dp))
                Text(text = user.bio, color = AvaTextSecondary, fontSize = 12.sp, lineHeight = 16.sp)
            }
        }
    }
}
