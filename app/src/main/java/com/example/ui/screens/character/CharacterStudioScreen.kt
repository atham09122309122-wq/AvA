package com.example.ui.screens.character

import android.graphics.Bitmap
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
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
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Checkroom
import androidx.compose.material.icons.filled.Download
import androidx.compose.material.icons.filled.Face
import androidx.compose.material.icons.filled.Female
import androidx.compose.material.icons.filled.Male
import androidx.compose.material.icons.filled.Palette
import androidx.compose.material.icons.filled.Save
import androidx.compose.material.icons.filled.ShoppingBag
import androidx.compose.material.icons.filled.Style
import androidx.compose.material.icons.filled.Wallpaper
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
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.CharacterEntity
import com.example.ui.components.AvaSubTopBar
import com.example.ui.components.UnsavedChangesDialog
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
import com.example.util.CharacterRenderer
import com.example.util.EditorEngine
import com.example.util.stringRes
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.util.UUID

@Composable
fun CharacterStudioScreen(
    viewModel: MainViewModel,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    val existing = viewModel.activeCharacter.value

    var characterName by remember { mutableStateOf(existing?.name ?: "Aero") }
    var gender by remember { mutableStateOf(existing?.gender ?: "male") }
    var skinToneHex by remember { mutableStateOf(existing?.skinToneHex ?: "#D09B67") }
    var hairStyle by remember { mutableStateOf(existing?.hairStyle ?: "textured_crop") }
    var hairColorHex by remember { mutableStateOf(existing?.hairColorHex ?: "#111116") }
    var eyeColorHex by remember { mutableStateOf(existing?.eyeColorHex ?: "#2E1C4E") }
    var topWear by remember { mutableStateOf(existing?.topWear ?: "streetwear_hoodie") }
    var topColorHex by remember { mutableStateOf(existing?.topColorHex ?: "#7C3AED") }
    var bottomWear by remember { mutableStateOf(existing?.bottomWear ?: "cargo_pants") }
    var bottomColorHex by remember { mutableStateOf(existing?.bottomColorHex ?: "#181524") }
    var shoes by remember { mutableStateOf(existing?.shoes ?: "high_top_kicks") }
    var accessory by remember { mutableStateOf(existing?.accessory ?: "chain") }
    var pose by remember { mutableStateOf(existing?.pose ?: "confident") }
    var backdrop by remember { mutableStateOf(existing?.backdrop ?: "studio_dark") }

    var selectedTab by remember { mutableStateOf("base") } // base, hair, clothing, shoes, accessories, pose, backdrop

    // Character instance for live rendering
    val currentCharacter = remember(
        characterName, gender, skinToneHex, hairStyle, hairColorHex,
        eyeColorHex, topWear, topColorHex, bottomWear, bottomColorHex,
        shoes, accessory, pose, backdrop
    ) {
        CharacterEntity(
            id = existing?.id ?: UUID.randomUUID().toString(),
            userId = viewModel.currentUser.value?.id ?: "guest",
            name = characterName,
            gender = gender,
            skinToneHex = skinToneHex,
            faceShape = "sharp",
            hairStyle = hairStyle,
            hairColorHex = hairColorHex,
            eyeStyle = "focused",
            eyeColorHex = eyeColorHex,
            topWear = topWear,
            topColorHex = topColorHex,
            bottomWear = bottomWear,
            bottomColorHex = bottomColorHex,
            shoes = shoes,
            accessory = accessory,
            pose = pose,
            backdrop = backdrop
        )
    }

    BackHandler {
        viewModel.navigateBack()
    }

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(AvaBlack)
    ) {
        AvaSubTopBar(
            title = stringRes("character_studio_title"),
            onBack = { viewModel.navigateBack() },
            actions = {
                // Set as Profile Avatar button
                IconButton(
                    onClick = {
                        viewModel.setAvatar(currentCharacter)
                    },
                    modifier = Modifier.testTag("btn_set_avatar")
                ) {
                    Icon(
                        imageVector = Icons.Default.AccountCircle,
                        contentDescription = stringRes("set_as_avatar"),
                        tint = AvaPurpleLight
                    )
                }

                // Export PNG button
                IconButton(
                    onClick = {
                        val bmp = CharacterRenderer.renderCharacterToBitmap(currentCharacter, 1080)
                        val file = EditorEngine.saveBitmapToFile(context, bmp, "AVA_Avatar_${characterName}_${System.currentTimeMillis()}.png")
                        viewModel.statusMessage.value = "Avatar exported: ${file.name}"
                    },
                    modifier = Modifier.testTag("btn_export_avatar")
                ) {
                    Icon(
                        imageVector = Icons.Default.Download,
                        contentDescription = "Export PNG",
                        tint = AvaGold
                    )
                }
            }
        )

        // Character Name Field
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 4.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            OutlinedTextField(
                value = characterName,
                onValueChange = { characterName = it },
                label = { Text(stringRes("character_name")) },
                singleLine = true,
                modifier = Modifier.weight(1f).testTag("input_char_name"),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = AvaPurplePrimary,
                    unfocusedBorderColor = AvaCardBorder,
                    focusedTextColor = Color.White
                )
            )
            Spacer(modifier = Modifier.width(10.dp))
            Button(
                onClick = {
                    CoroutineScope(Dispatchers.IO).launch {
                        viewModel.repository.saveCharacter(currentCharacter)
                    }
                    viewModel.statusMessage.value = "Character saved successfully!"
                },
                colors = ButtonDefaults.buttonColors(containerColor = AvaPurplePrimary),
                shape = RoundedCornerShape(10.dp),
                modifier = Modifier.testTag("btn_save_char")
            ) {
                Icon(Icons.Default.Save, contentDescription = null, modifier = Modifier.size(16.dp))
                Spacer(modifier = Modifier.width(4.dp))
                Text(text = stringRes("save_character"))
            }
        }

        // LARGE CHARACTER PREVIEW (Interactive 2D Canvas)
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f)
                .padding(horizontal = 16.dp, vertical = 6.dp)
                .clip(RoundedCornerShape(18.dp))
                .border(1.dp, AvaCardBorder, RoundedCornerShape(18.dp)),
            contentAlignment = Alignment.Center
        ) {
            CharacterRenderer.CharacterCanvas(
                character = currentCharacter,
                showBackdrop = true
            )

            // Gender Switcher Overlay Pill
            Row(
                modifier = Modifier
                    .align(Alignment.TopStart)
                    .padding(12.dp)
                    .clip(RoundedCornerShape(20.dp))
                    .background(AvaCardSelected.copy(alpha = 0.85f))
                    .padding(4.dp)
            ) {
                Box(
                    modifier = Modifier
                        .clip(CircleShape)
                        .background(if (gender == "male") AvaPurplePrimary else Color.Transparent)
                        .clickable { gender = "male" }
                        .padding(6.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.Male,
                        contentDescription = "Male",
                        tint = Color.White,
                        modifier = Modifier.size(18.dp)
                    )
                }
                Box(
                    modifier = Modifier
                        .clip(CircleShape)
                        .background(if (gender == "female") AvaPurplePrimary else Color.Transparent)
                        .clickable { gender = "female" }
                        .padding(6.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.Female,
                        contentDescription = "Female",
                        tint = Color.White,
                        modifier = Modifier.size(18.dp)
                    )
                }
            }
        }

        // CATEGORY TABS STRIP
        val tabs = listOf(
            Pair("base", stringRes("skin_tone")),
            Pair("hair", stringRes("hair_style")),
            Pair("tops", stringRes("tops_clothing")),
            Pair("bottoms", stringRes("bottoms_clothing")),
            Pair("shoes", stringRes("shoes")),
            Pair("accessories", stringRes("accessories")),
            Pair("backdrop", stringRes("character_backdrop"))
        )

        LazyRow(
            modifier = Modifier
                .fillMaxWidth()
                .background(AvaSurface)
                .padding(vertical = 8.dp),
            contentPadding = androidx.compose.foundation.layout.PaddingValues(horizontal = 16.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            items(tabs) { (key, label) ->
                val isSelected = selectedTab == key
                FilterChip(
                    selected = isSelected,
                    onClick = { selectedTab = key },
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

        // CUSTOMIZATION OPTIONS PANEL
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(130.dp)
                .background(AvaCard)
                .padding(14.dp)
        ) {
            when (selectedTab) {
                "base" -> {
                    Column {
                        Text(text = "Select Skin Tone", color = AvaTextSecondary, fontSize = 12.sp)
                        Spacer(modifier = Modifier.height(10.dp))
                        Row(horizontalArrangement = Arrangement.spacedBy(14.dp)) {
                            CharacterRenderer.SkinTones.forEach { toneHex ->
                                val isSelected = skinToneHex.equals(toneHex, ignoreCase = true)
                                Box(
                                    modifier = Modifier
                                        .size(42.dp)
                                        .clip(CircleShape)
                                        .background(Color(android.graphics.Color.parseColor(toneHex)))
                                        .border(
                                            width = if (isSelected) 3.dp else 1.dp,
                                            color = if (isSelected) AvaPurplePrimary else Color.Transparent,
                                            shape = CircleShape
                                        )
                                        .clickable { skinToneHex = toneHex },
                                    contentAlignment = Alignment.Center
                                ) {
                                    if (isSelected) {
                                        Icon(Icons.Default.Check, contentDescription = null, tint = Color.Black, modifier = Modifier.size(16.dp))
                                    }
                                }
                            }
                        }
                    }
                }

                "hair" -> {
                    Column {
                        Text(text = "Hairstyles & Colors", color = AvaTextSecondary, fontSize = 12.sp)
                        Spacer(modifier = Modifier.height(8.dp))
                        val styles = listOf(
                            Pair("Textured Crop", "textured_crop"),
                            Pair("Modern Fade", "fade"),
                            Pair("Afro Puff", "afro"),
                            Pair("Wavy Locks", "wavy")
                        )
                        LazyRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                            items(styles) { (name, sId) ->
                                val isSelected = hairStyle == sId
                                Box(
                                    modifier = Modifier
                                        .clip(RoundedCornerShape(8.dp))
                                        .background(if (isSelected) AvaPurpleContainer else AvaBlack)
                                        .border(1.dp, if (isSelected) AvaPurplePrimary else AvaCardBorder, RoundedCornerShape(8.dp))
                                        .clickable { hairStyle = sId }
                                        .padding(horizontal = 12.dp, vertical = 8.dp)
                                ) {
                                    Text(text = name, color = if (isSelected) Color.White else AvaTextSecondary, fontSize = 12.sp)
                                }
                            }
                        }
                    }
                }

                "tops" -> {
                    Column {
                        Text(text = "Top Clothing & Apparel", color = AvaTextSecondary, fontSize = 12.sp)
                        Spacer(modifier = Modifier.height(8.dp))
                        val tops = listOf(
                            Triple("AVA Signature Hoodie", "streetwear_hoodie", "#7C3AED"),
                            Triple("Minimalist Tee", "tshirt", "#1F2937"),
                            Triple("B-Ball Jersey #23", "jersey", "#DC2626"),
                            Triple("Velvet Blazer", "blazer", "#4C1D95")
                        )
                        LazyRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                            items(tops) { (name, tId, cHex) ->
                                val isSelected = topWear == tId
                                Box(
                                    modifier = Modifier
                                        .clip(RoundedCornerShape(8.dp))
                                        .background(if (isSelected) AvaPurpleContainer else AvaBlack)
                                        .border(1.dp, if (isSelected) AvaPurplePrimary else AvaCardBorder, RoundedCornerShape(8.dp))
                                        .clickable {
                                            topWear = tId
                                            topColorHex = cHex
                                        }
                                        .padding(horizontal = 12.dp, vertical = 8.dp)
                                ) {
                                    Text(text = name, color = if (isSelected) Color.White else AvaTextSecondary, fontSize = 12.sp)
                                }
                            }
                        }
                    }
                }

                "bottoms" -> {
                    Column {
                        Text(text = "Bottom Wear", color = AvaTextSecondary, fontSize = 12.sp)
                        Spacer(modifier = Modifier.height(8.dp))
                        val bottoms = listOf(
                            Triple("Cargo Joggers", "cargo_pants", "#181524"),
                            Triple("Court B-Ball Shorts", "basketball_shorts", "#DC2626"),
                            Triple("Denim Jeans", "jeans", "#1E3A8A")
                        )
                        LazyRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                            items(bottoms) { (name, bId, cHex) ->
                                val isSelected = bottomWear == bId
                                Box(
                                    modifier = Modifier
                                        .clip(RoundedCornerShape(8.dp))
                                        .background(if (isSelected) AvaPurpleContainer else AvaBlack)
                                        .border(1.dp, if (isSelected) AvaPurplePrimary else AvaCardBorder, RoundedCornerShape(8.dp))
                                        .clickable {
                                            bottomWear = bId
                                            bottomColorHex = cHex
                                        }
                                        .padding(horizontal = 12.dp, vertical = 8.dp)
                                ) {
                                    Text(text = name, color = if (isSelected) Color.White else AvaTextSecondary, fontSize = 12.sp)
                                }
                            }
                        }
                    }
                }

                "shoes" -> {
                    Column {
                        Text(text = "Sneakers & Kicks", color = AvaTextSecondary, fontSize = 12.sp)
                        Spacer(modifier = Modifier.height(8.dp))
                        val kicks = listOf(
                            Pair("High-Top Dunks", "high_top_kicks"),
                            Pair("B-Ball Velocity", "bball_shoes"),
                            Pair("Combat Boots", "boots")
                        )
                        LazyRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                            items(kicks) { (name, sId) ->
                                val isSelected = shoes == sId
                                Box(
                                    modifier = Modifier
                                        .clip(RoundedCornerShape(8.dp))
                                        .background(if (isSelected) AvaPurpleContainer else AvaBlack)
                                        .border(1.dp, if (isSelected) AvaPurplePrimary else AvaCardBorder, RoundedCornerShape(8.dp))
                                        .clickable { shoes = sId }
                                        .padding(horizontal = 12.dp, vertical = 8.dp)
                                ) {
                                    Text(text = name, color = if (isSelected) Color.White else AvaTextSecondary, fontSize = 12.sp)
                                }
                            }
                        }
                    }
                }

                "accessories" -> {
                    Column {
                        Text(text = "Chains, Headwear & Shades", color = AvaTextSecondary, fontSize = 12.sp)
                        Spacer(modifier = Modifier.height(8.dp))
                        val accs = listOf(
                            Pair("Cuban Gold Chain", "chain"),
                            Pair("Retro Sunglasses", "shades"),
                            Pair("Studio Beanie", "beanie"),
                            Pair("Pro Smartwatch", "watch"),
                            Pair("None", "none")
                        )
                        LazyRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                            items(accs) { (name, aId) ->
                                val isSelected = accessory == aId
                                Box(
                                    modifier = Modifier
                                        .clip(RoundedCornerShape(8.dp))
                                        .background(if (isSelected) AvaPurpleContainer else AvaBlack)
                                        .border(1.dp, if (isSelected) AvaPurplePrimary else AvaCardBorder, RoundedCornerShape(8.dp))
                                        .clickable { accessory = aId }
                                        .padding(horizontal = 12.dp, vertical = 8.dp)
                                ) {
                                    Text(text = name, color = if (isSelected) Color.White else AvaTextSecondary, fontSize = 12.sp)
                                }
                            }
                        }
                    }
                }

                "backdrop" -> {
                    Column {
                        Text(text = "Studio Backdrop Environment", color = AvaTextSecondary, fontSize = 12.sp)
                        Spacer(modifier = Modifier.height(8.dp))
                        val bgs = listOf(
                            Pair("Studio Dark", "studio_dark"),
                            Pair("Street Urban", "street_urban"),
                            Pair("Cyber Purple", "cyber_purple"),
                            Pair("Basketball Neon", "basketball_neon"),
                            Pair("Transparent", "transparent")
                        )
                        LazyRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                            items(bgs) { (name, bId) ->
                                val isSelected = backdrop == bId
                                Box(
                                    modifier = Modifier
                                        .clip(RoundedCornerShape(8.dp))
                                        .background(if (isSelected) AvaPurpleContainer else AvaBlack)
                                        .border(1.dp, if (isSelected) AvaPurplePrimary else AvaCardBorder, RoundedCornerShape(8.dp))
                                        .clickable { backdrop = bId }
                                        .padding(horizontal = 12.dp, vertical = 8.dp)
                                ) {
                                    Text(text = name, color = if (isSelected) Color.White else AvaTextSecondary, fontSize = 12.sp)
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}
