package com.example.ui.screens.profile

import android.content.Intent
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
import androidx.compose.material.icons.automirrored.filled.Chat
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.AdminPanelSettings
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Face
import androidx.compose.material.icons.filled.MonetizationOn
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled.Share
import androidx.compose.material.icons.filled.Shield
import androidx.compose.material.icons.filled.Verified
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.TabRowDefaults
import androidx.compose.material3.TabRowDefaults.tabIndicatorOffset
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.CharacterEntity
import com.example.data.model.ProjectEntity
import com.example.ui.components.BrandSignatureBadge
import androidx.compose.ui.graphics.SolidColor
import com.example.ui.theme.AvaBlack
import com.example.ui.theme.AvaCard
import com.example.ui.theme.AvaCardBorder
import com.example.ui.theme.AvaDivider
import com.example.ui.theme.AvaGold
import com.example.ui.theme.AvaPurpleContainer
import com.example.ui.theme.AvaPurpleLight
import com.example.ui.theme.AvaPurplePrimary
import com.example.ui.theme.AvaSuccess
import com.example.ui.theme.AvaSurface
import com.example.ui.theme.AvaTextPrimary
import com.example.ui.theme.AvaTextSecondary
import com.example.ui.viewmodel.AppScreen
import com.example.ui.viewmodel.MainViewModel
import com.example.util.CharacterRenderer
import com.example.util.stringRes
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@Composable
fun ProfileScreen(
    viewModel: MainViewModel,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    val user by viewModel.currentUser.collectAsState()
    val projects by viewModel.projects.collectAsState()
    val characters by viewModel.characters.collectAsState()

    var selectedTabIndex by remember { mutableIntStateOf(0) }
    val tabs = listOf(
        stringRes("tab_designs"),
        stringRes("tab_photos"),
        stringRes("tab_characters"),
        stringRes("tab_saved")
    )

    val appLanguage by viewModel.appLanguage.collectAsState()
    val isAr = appLanguage == com.example.util.AppLanguage.ARABIC

    val currentAvatarCharacter = remember(characters, user?.avatarUri) {
        characters.firstOrNull { user?.avatarUri == "character://${it.id}" }
    }

    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .background(AvaBlack)
    ) {
        // Banner & Profile Header
        item {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(130.dp)
                    .background(
                        Brush.verticalGradient(
                            listOf(Color(0xFF2E1C4E), AvaBlack)
                        )
                    )
            ) {
                // Settings button in top right
                IconButton(
                    onClick = { viewModel.navigateTo(AppScreen.ACCOUNT_SETTINGS) },
                    modifier = Modifier
                        .align(Alignment.TopEnd)
                        .padding(12.dp)
                        .testTag("btn_profile_settings")
                ) {
                    Icon(
                        imageVector = Icons.Default.Settings,
                        contentDescription = "Settings",
                        tint = AvaTextPrimary
                    )
                }
            }

            // User Info Section
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.Bottom
                ) {
                    // Profile Avatar (Character avatar or placeholder)
                    Box(
                        modifier = Modifier
                            .size(76.dp)
                            .clip(CircleShape)
                            .background(AvaPurpleContainer)
                            .border(2.dp, AvaPurplePrimary, CircleShape),
                        contentAlignment = Alignment.Center
                    ) {
                        if (currentAvatarCharacter != null) {
                            CharacterRenderer.CharacterCanvas(
                                character = currentAvatarCharacter,
                                showBackdrop = false
                            )
                        } else {
                            Icon(
                                imageVector = Icons.Default.Person,
                                contentDescription = null,
                                tint = AvaPurpleLight,
                                modifier = Modifier.size(44.dp)
                            )
                        }
                    }

                    // Action buttons: Chat & Edit Profile
                    Row(horizontalArrangement = Arrangement.spacedBy(8.dp), verticalAlignment = Alignment.CenterVertically) {
                        IconButton(
                            onClick = { viewModel.navigateTo(AppScreen.CHAT_LIST) },
                            modifier = Modifier
                                .size(38.dp)
                                .clip(RoundedCornerShape(10.dp))
                                .background(AvaCard)
                                .border(1.dp, AvaCardBorder, RoundedCornerShape(10.dp))
                                .testTag("btn_profile_messages")
                        ) {
                            Icon(
                                imageVector = Icons.AutoMirrored.Filled.Chat,
                                contentDescription = "Messages",
                                tint = AvaPurpleLight,
                                modifier = Modifier.size(18.dp)
                            )
                        }

                        OutlinedButton(
                            onClick = { viewModel.navigateTo(AppScreen.EDIT_PROFILE) },
                            shape = RoundedCornerShape(10.dp),
                            colors = ButtonDefaults.outlinedButtonColors(contentColor = AvaTextPrimary),
                            modifier = Modifier.testTag("btn_edit_profile")
                        ) {
                            Icon(imageVector = Icons.Default.Edit, contentDescription = null, modifier = Modifier.size(14.dp))
                            Spacer(modifier = Modifier.width(6.dp))
                            Text(text = stringRes("edit_profile"), fontSize = 12.sp)
                        }
                    }
                }

                Spacer(modifier = Modifier.height(12.dp))

                // Display Name with Verified, Founder, and Email Badges
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(
                        text = user?.displayName ?: if (isAr) "زائر (غير مسجل)" else "Guest User",
                        color = AvaTextPrimary,
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold
                    )
                    if (user?.isVerified == true) {
                        Spacer(modifier = Modifier.width(6.dp))
                        Icon(
                            imageVector = Icons.Default.Verified,
                            contentDescription = "Verified",
                            tint = AvaPurpleLight,
                            modifier = Modifier.size(18.dp)
                        )
                    }
                    if (user?.isFounder == true) {
                        Spacer(modifier = Modifier.width(6.dp))
                        Box(
                            modifier = Modifier
                                .clip(RoundedCornerShape(6.dp))
                                .background(AvaPurpleContainer)
                                .border(1.dp, AvaPurplePrimary.copy(alpha = 0.5f), RoundedCornerShape(6.dp))
                                .padding(horizontal = 6.dp, vertical = 2.dp)
                        ) {
                            Text(
                                text = "المؤسس • AYHAM",
                                color = AvaPurpleLight,
                                fontSize = 9.sp,
                                fontWeight = FontWeight.Bold
                            )
                        }
                    }
                    if (user?.isEmailVerified == true) {
                        Spacer(modifier = Modifier.width(6.dp))
                        Box(
                            modifier = Modifier
                                .clip(RoundedCornerShape(6.dp))
                                .background(AvaSuccess.copy(alpha = 0.2f))
                                .border(1.dp, AvaSuccess.copy(alpha = 0.5f), RoundedCornerShape(6.dp))
                                .padding(horizontal = 6.dp, vertical = 2.dp)
                        ) {
                            Text(
                                text = if (isAr) "بريد مؤكد ✓" else "Verified ✓",
                                color = AvaSuccess,
                                fontSize = 9.sp,
                                fontWeight = FontWeight.Bold
                            )
                        }
                    }
                }

                Text(
                    text = "@${user?.username ?: "creator"}",
                    color = AvaPurpleLight,
                    fontSize = 13.sp,
                    fontWeight = FontWeight.Medium
                )

                Spacer(modifier = Modifier.height(8.dp))

                // Bio
                Text(
                    text = user?.bio ?: "Digital Visual Artist & Creator at AVA Studio",
                    color = AvaTextSecondary,
                    fontSize = 13.sp,
                    lineHeight = 18.sp
                )

                Spacer(modifier = Modifier.height(8.dp))

                // Guest banner or Email verification reminder
                if (user == null) {
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 8.dp),
                        shape = RoundedCornerShape(12.dp),
                        colors = CardDefaults.cardColors(containerColor = AvaCard),
                        border = CardDefaults.outlinedCardBorder().copy(brush = Brush.horizontalGradient(listOf(AvaPurplePrimary, Color(0xFF4C1D95))))
                    ) {
                        Column(
                            modifier = Modifier.padding(14.dp),
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Text(
                                text = if (isAr) "أنت تستخدم التطبيق كزائر" else "Browsing as Guest",
                                color = AvaTextPrimary,
                                fontSize = 14.sp,
                                fontWeight = FontWeight.Bold
                            )
                            Spacer(modifier = Modifier.height(4.dp))
                            Text(
                                text = if (isAr) "سجّل الدخول للوصول إلى مشاريعك، وتأكيد هويتك، والتمتع بمزايا الحساب الآمن."
                                else "Sign in to secure your designs, customize your profile, and access personal features.",
                                color = AvaTextSecondary,
                                fontSize = 11.sp,
                                textAlign = TextAlign.Center
                            )
                            Spacer(modifier = Modifier.height(10.dp))
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.spacedBy(8.dp)
                            ) {
                                Button(
                                    onClick = { viewModel.navigateTo(AppScreen.LOGIN) },
                                    modifier = Modifier.weight(1f).height(38.dp),
                                    colors = ButtonDefaults.buttonColors(containerColor = AvaPurplePrimary)
                                ) {
                                    Text(if (isAr) "تسجيل الدخول" else "Sign In", color = Color.White, fontSize = 12.sp, fontWeight = FontWeight.Bold)
                                }
                                OutlinedButton(
                                    onClick = { viewModel.navigateTo(AppScreen.REGISTER) },
                                    modifier = Modifier.weight(1f).height(38.dp),
                                    colors = ButtonDefaults.outlinedButtonColors(contentColor = AvaTextPrimary)
                                ) {
                                    Text(if (isAr) "حساب جديد" else "Register", color = AvaTextPrimary, fontSize = 12.sp, fontWeight = FontWeight.Bold)
                                }
                            }
                        }
                    }
                } else if (!user!!.isEmailVerified) {
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable { viewModel.navigateTo(AppScreen.EMAIL_VERIFICATION) }
                            .padding(vertical = 8.dp),
                        shape = RoundedCornerShape(12.dp),
                        colors = CardDefaults.cardColors(containerColor = Color(0xFF332008)),
                        border = CardDefaults.outlinedCardBorder().copy(brush = SolidColor(Color(0xFFF59E0B)))
                    ) {
                        Row(
                            modifier = Modifier.padding(12.dp),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Row(modifier = Modifier.weight(1f), verticalAlignment = Alignment.CenterVertically) {
                                Icon(imageVector = Icons.Default.Email, contentDescription = null, tint = Color(0xFFF59E0B), modifier = Modifier.size(22.dp))
                                Spacer(modifier = Modifier.width(10.dp))
                                Column {
                                    Text(
                                        text = if (isAr) "البريد الإلكتروني بانتظار التأكيد" else "Email Pending Verification",
                                        color = Color(0xFFF59E0B),
                                        fontSize = 12.sp,
                                        fontWeight = FontWeight.Bold
                                    )
                                    Text(
                                        text = if (isAr) "اضغط هنا لإدخال رمز التحقق المكون من 6 أرقام" else "Tap here to enter your 6-digit confirmation code",
                                        color = AvaTextSecondary,
                                        fontSize = 11.sp
                                    )
                                }
                            }
                            Icon(imageVector = Icons.AutoMirrored.Filled.ArrowForward, contentDescription = null, tint = Color(0xFFF59E0B), modifier = Modifier.size(16.dp))
                        }
                    }
                }

                Spacer(modifier = Modifier.height(10.dp))

                // Followers & Following Clickable Row
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(20.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(
                        modifier = Modifier
                            .clickable {
                                viewModel.targetProfileUserId.value = user?.id ?: "user_ayham_creator"
                                viewModel.targetProfileUserName.value = user?.displayName ?: "Ayham"
                                viewModel.followersFollowingTab.value = 0
                                viewModel.navigateTo(AppScreen.FOLLOWERS_FOLLOWING)
                            }
                            .padding(vertical = 4.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "${user?.followersCount ?: 0}",
                            color = AvaTextPrimary,
                            fontSize = 15.sp,
                            fontWeight = FontWeight.Bold
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(
                            text = if (isAr) "متابع" else "Followers",
                            color = AvaTextSecondary,
                            fontSize = 12.sp
                        )
                    }

                    Row(
                        modifier = Modifier
                            .clickable {
                                viewModel.targetProfileUserId.value = user?.id ?: "user_ayham_creator"
                                viewModel.targetProfileUserName.value = user?.displayName ?: "Ayham"
                                viewModel.followersFollowingTab.value = 1
                                viewModel.navigateTo(AppScreen.FOLLOWERS_FOLLOWING)
                            }
                            .padding(vertical = 4.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "${user?.followingCount ?: 0}",
                            color = AvaTextPrimary,
                            fontSize = 15.sp,
                            fontWeight = FontWeight.Bold
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(
                            text = if (isAr) "يتابع" else "Following",
                            color = AvaTextSecondary,
                            fontSize = 12.sp
                        )
                    }
                }

                // If Founder: Founder Dashboard Access Card
                if (user?.isFounder == true) {
                    Spacer(modifier = Modifier.height(12.dp))
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable { viewModel.navigateTo(AppScreen.FOUNDER_DASHBOARD) }
                            .testTag("founder_dashboard_banner"),
                        shape = RoundedCornerShape(12.dp),
                        colors = CardDefaults.cardColors(containerColor = AvaCard),
                        border = CardDefaults.outlinedCardBorder().copy(
                            brush = Brush.horizontalGradient(
                                listOf(AvaPurplePrimary, Color(0xFF4C1D95))
                            )
                        )
                    ) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(12.dp),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Box(
                                    modifier = Modifier
                                        .size(34.dp)
                                        .clip(CircleShape)
                                        .background(AvaPurplePrimary),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.Shield,
                                        contentDescription = null,
                                        tint = Color.White,
                                        modifier = Modifier.size(18.dp)
                                    )
                                }
                                Spacer(modifier = Modifier.width(10.dp))
                                Column {
                                    Text(
                                        text = if (isAr) "لوحة تحكم المؤسس والإدارة" else "Founder & Admin Console",
                                        color = AvaTextPrimary,
                                        fontSize = 13.sp,
                                        fontWeight = FontWeight.Bold
                                    )
                                    Text(
                                        text = if (isAr) "إدارة المستخدمين، التوثيق، البلاغات والمراقبة" else "Manage users, badges, reports & moderation",
                                        color = AvaPurpleLight,
                                        fontSize = 11.sp
                                    )
                                }
                            }
                            Icon(
                                imageVector = Icons.Default.AdminPanelSettings,
                                contentDescription = null,
                                tint = AvaPurpleLight,
                                modifier = Modifier.size(20.dp)
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(14.dp))

                // Three Real Statistics Cards: Designs, Characters, Coins
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(12.dp))
                        .background(AvaCard)
                        .border(1.dp, AvaCardBorder, RoundedCornerShape(12.dp))
                        .padding(vertical = 12.dp),
                    horizontalArrangement = Arrangement.SpaceEvenly
                ) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Text(text = "${projects.size}", color = AvaTextPrimary, fontSize = 18.sp, fontWeight = FontWeight.Bold)
                        Text(text = stringRes("designs_count"), color = AvaTextSecondary, fontSize = 11.sp)
                    }
                    Box(modifier = Modifier.width(1.dp).height(30.dp).background(AvaDivider))
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Text(text = "${characters.size}", color = AvaTextPrimary, fontSize = 18.sp, fontWeight = FontWeight.Bold)
                        Text(text = stringRes("characters_count"), color = AvaTextSecondary, fontSize = 11.sp)
                    }
                    Box(modifier = Modifier.width(1.dp).height(30.dp).background(AvaDivider))
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        modifier = Modifier.clickable { viewModel.navigateTo(AppScreen.COINS_WALLET) }
                    ) {
                        Text(text = "${user?.coinsBalance ?: 0}", color = AvaGold, fontSize = 18.sp, fontWeight = FontWeight.Bold)
                        Text(text = "AVA Coins", color = AvaTextSecondary, fontSize = 11.sp)
                    }
                }

                Spacer(modifier = Modifier.height(14.dp))

                // Action buttons: Share Profile, Wallet
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    OutlinedButton(
                        onClick = {
                            val shareIntent = Intent(Intent.ACTION_SEND).apply {
                                type = "text/plain"
                                putExtra(Intent.EXTRA_TEXT, "Check out my visual creations on AVA Studio: @${user?.username}")
                            }
                            context.startActivity(Intent.createChooser(shareIntent, "Share Profile"))
                        },
                        modifier = Modifier.weight(1f),
                        shape = RoundedCornerShape(10.dp),
                        colors = ButtonDefaults.outlinedButtonColors(contentColor = AvaTextPrimary)
                    ) {
                        Icon(imageVector = Icons.Default.Share, contentDescription = null, modifier = Modifier.size(14.dp))
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(text = stringRes("share_profile"), fontSize = 12.sp)
                    }

                    Button(
                        onClick = { viewModel.navigateTo(AppScreen.COINS_WALLET) },
                        modifier = Modifier.weight(1f),
                        shape = RoundedCornerShape(10.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = AvaPurpleContainer)
                    ) {
                        Icon(imageVector = Icons.Default.MonetizationOn, contentDescription = null, tint = AvaGold, modifier = Modifier.size(16.dp))
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(text = "Coins Wallet", color = AvaPurpleLight, fontSize = 12.sp, fontWeight = FontWeight.Bold)
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))
        }

        // TABS: My Designs, My Photos, My Characters, Saved Items
        item {
            TabRow(
                selectedTabIndex = selectedTabIndex,
                containerColor = AvaSurface,
                contentColor = AvaPurpleLight,
                indicator = { tabPositions ->
                    TabRowDefaults.SecondaryIndicator(
                        Modifier.tabIndicatorOffset(tabPositions[selectedTabIndex]),
                        color = AvaPurplePrimary
                    )
                }
            ) {
                tabs.forEachIndexed { index, title ->
                    Tab(
                        selected = selectedTabIndex == index,
                        onClick = { selectedTabIndex = index },
                        text = {
                            Text(
                                text = title,
                                fontSize = 12.sp,
                                fontWeight = if (selectedTabIndex == index) FontWeight.Bold else FontWeight.Normal
                            )
                        }
                    )
                }
            }
            Spacer(modifier = Modifier.height(12.dp))
        }

        // Tab Content
        when (selectedTabIndex) {
            0, 1 -> {
                // Designs & Photos with strict user isolation
                val currentUserId = user?.id ?: "local_creator"
                val filteredProjects = if (selectedTabIndex == 1) {
                    projects.filter { it.userId == currentUserId && it.projectType == "personal_photo" }
                } else {
                    projects.filter { it.userId == currentUserId }
                }

                if (filteredProjects.isEmpty()) {
                    item {
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(36.dp),
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Text(text = "You haven't created a design yet.", color = AvaTextSecondary, fontSize = 14.sp)
                            Spacer(modifier = Modifier.height(12.dp))
                            Button(
                                onClick = { viewModel.navigateTo(AppScreen.AD_STUDIO) },
                                colors = ButtonDefaults.buttonColors(containerColor = AvaPurplePrimary)
                            ) {
                                Text("Start your first project", color = Color.White)
                            }
                        }
                    }
                } else {
                    items(filteredProjects) { project ->
                        ProjectListItem(
                            project = project,
                            onEdit = {
                                viewModel.activeProject.value = project
                                viewModel.navigateTo(AppScreen.IMAGE_EDITOR)
                            },
                            onDelete = {
                                viewModel.deleteProject(project.id)
                            }
                        )
                    }
                }
            }

            2 -> {
                // Characters Tab
                if (characters.isEmpty()) {
                    item {
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(36.dp),
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Text(text = "You haven't created a character yet.", color = AvaTextSecondary, fontSize = 14.sp)
                            Spacer(modifier = Modifier.height(12.dp))
                            Button(
                                onClick = { viewModel.navigateTo(AppScreen.CHARACTER_STUDIO) },
                                colors = ButtonDefaults.buttonColors(containerColor = AvaPurplePrimary)
                            ) {
                                Text("Create Your Avatar", color = Color.White)
                            }
                        }
                    }
                } else {
                    items(characters) { character ->
                        CharacterListItem(
                            character = character,
                            isCurrentAvatar = user?.avatarUri == "character://${character.id}",
                            onEdit = {
                                viewModel.activeCharacter.value = character
                                viewModel.navigateTo(AppScreen.CHARACTER_STUDIO)
                            },
                            onSetAvatar = {
                                viewModel.setAvatar(character)
                            },
                            onDelete = {
                                viewModel.deleteCharacter(character.id)
                            }
                        )
                    }
                }
            }

            3 -> {
                // Saved Items
                item {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(40.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(text = "All your bookmarked templates and assets appear here.", color = AvaTextSecondary, fontSize = 13.sp)
                    }
                }
            }
        }
    }
}

@Composable
fun ProjectListItem(
    project: ProjectEntity,
    onEdit: () -> Unit,
    onDelete: () -> Unit
) {
    val dateStr = SimpleDateFormat("MMM dd, yyyy", Locale.getDefault()).format(Date(project.updatedAt))

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 6.dp)
            .clickable { onEdit() },
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = AvaCard),
        border = CardDefaults.outlinedCardBorder().copy(brush = androidx.compose.ui.graphics.SolidColor(AvaCardBorder))
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Box(
                    modifier = Modifier
                        .size(46.dp)
                        .clip(RoundedCornerShape(8.dp))
                        .background(AvaPurpleContainer),
                    contentAlignment = Alignment.Center
                ) {
                    Text(text = project.aspectRatio, color = AvaPurpleLight, fontSize = 11.sp, fontWeight = FontWeight.Bold)
                }
                Spacer(modifier = Modifier.width(12.dp))
                Column {
                    Text(text = project.title, color = AvaTextPrimary, fontSize = 14.sp, fontWeight = FontWeight.SemiBold)
                    Text(text = "${project.projectType.uppercase()} • $dateStr", color = AvaTextSecondary, fontSize = 11.sp)
                }
            }

            Row {
                IconButton(onClick = onEdit) {
                    Icon(imageVector = Icons.Default.Edit, contentDescription = "Edit", tint = AvaPurpleLight, modifier = Modifier.size(18.dp))
                }
                IconButton(onClick = onDelete) {
                    Icon(imageVector = Icons.Default.Delete, contentDescription = "Delete", tint = Color(0xFFEF4444), modifier = Modifier.size(18.dp))
                }
            }
        }
    }
}

@Composable
fun CharacterListItem(
    character: CharacterEntity,
    isCurrentAvatar: Boolean,
    onEdit: () -> Unit,
    onSetAvatar: () -> Unit,
    onDelete: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 6.dp),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = AvaCard),
        border = CardDefaults.outlinedCardBorder().copy(brush = androidx.compose.ui.graphics.SolidColor(AvaCardBorder))
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Box(
                    modifier = Modifier
                        .size(54.dp)
                        .clip(RoundedCornerShape(8.dp))
                ) {
                    CharacterRenderer.CharacterCanvas(character = character, showBackdrop = true)
                }
                Spacer(modifier = Modifier.width(12.dp))
                Column {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text(text = character.name, color = AvaTextPrimary, fontSize = 14.sp, fontWeight = FontWeight.Bold)
                        if (isCurrentAvatar) {
                            Spacer(modifier = Modifier.width(6.dp))
                            Box(
                                modifier = Modifier
                                    .clip(RoundedCornerShape(4.dp))
                                    .background(AvaPurplePrimary)
                                    .padding(horizontal = 4.dp, vertical = 1.dp)
                            ) {
                                Text(text = "AVATAR", color = Color.White, fontSize = 8.sp, fontWeight = FontWeight.Bold)
                            }
                        }
                    }
                    Text(text = "${character.gender.uppercase()} • ${character.topWear.replace("_", " ")}", color = AvaTextSecondary, fontSize = 11.sp)
                }
            }

            Row {
                if (!isCurrentAvatar) {
                    TextButton(onClick = onSetAvatar) {
                        Text(text = "Use Avatar", fontSize = 11.sp, color = AvaPurpleLight)
                    }
                }
                IconButton(onClick = onEdit) {
                    Icon(imageVector = Icons.Default.Edit, contentDescription = "Edit", tint = AvaPurpleLight, modifier = Modifier.size(18.dp))
                }
                IconButton(onClick = onDelete) {
                    Icon(imageVector = Icons.Default.Delete, contentDescription = "Delete", tint = Color(0xFFEF4444), modifier = Modifier.size(18.dp))
                }
            }
        }
    }
}
