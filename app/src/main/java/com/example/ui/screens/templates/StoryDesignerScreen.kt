package com.example.ui.screens.templates

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
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.Download
import androidx.compose.material.icons.filled.Palette
import androidx.compose.material.icons.filled.Save
import androidx.compose.material.icons.filled.TextFields
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
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
import com.example.ui.theme.AvaSurface
import com.example.ui.theme.AvaTextPrimary
import com.example.ui.theme.AvaTextSecondary
import com.example.ui.viewmodel.AppScreen
import com.example.ui.viewmodel.MainViewModel
import java.util.UUID

@Composable
fun StoryDesignerScreen(
    viewModel: MainViewModel,
    modifier: Modifier = Modifier
) {
    var storyHeadline by remember { mutableStateOf("EXCLUSIVE DROP") }
    var storySubtitle by remember { mutableStateOf("Tap the link to explore") }
    var selectedGradient by remember {
        mutableStateOf(listOf(Color(0xFF2E1C4E), Color(0xFF0D0B14)))
    }

    val gradients = listOf(
        Pair("Midnight Violet", listOf(Color(0xFF2E1C4E), Color(0xFF0D0B14))),
        Pair("Cyber Purple", listOf(Color(0xFF4C1D95), Color(0xFF1E1035))),
        Pair("Sunset Flame", listOf(Color(0xFF7C2D12), Color(0xFF18181B))),
        Pair("Obsidian Minimal", listOf(Color(0xFF1F2937), Color(0xFF030712))),
        Pair("Emerald Luxe", listOf(Color(0xFF064E3B), Color(0xFF022C22)))
    )

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(AvaBlack)
    ) {
        AvaSubTopBar(
            title = "Story Designer (9:16)",
            onBack = { viewModel.navigateBack() },
            actions = {
                IconButton(
                    onClick = {
                        val user = viewModel.currentUser.value
                        val project = ProjectEntity(
                            id = UUID.randomUUID().toString(),
                            userId = user?.id ?: "guest",
                            title = "Story: $storyHeadline",
                            projectType = "story",
                            aspectRatio = "9:16"
                        )
                        viewModel.activeProject.value = project
                        viewModel.navigateTo(AppScreen.IMAGE_EDITOR)
                    },
                    modifier = Modifier.testTag("btn_open_story_editor")
                ) {
                    Icon(
                        imageVector = Icons.Default.AutoAwesome,
                        contentDescription = "Open Editor",
                        tint = AvaPurpleLight
                    )
                }
            }
        )

        Column(
            modifier = Modifier
                .weight(1f)
                .fillMaxWidth()
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // 9:16 Story Canvas Preview
            Box(
                modifier = Modifier
                    .weight(1f)
                    .aspectRatio(9f / 16f)
                    .clip(RoundedCornerShape(16.dp))
                    .background(Brush.verticalGradient(selectedGradient))
                    .border(1.5.dp, AvaCardBorder, RoundedCornerShape(16.dp))
                    .padding(20.dp),
                contentAlignment = Alignment.Center
            ) {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    Box(
                        modifier = Modifier
                            .size(54.dp)
                            .clip(CircleShape)
                            .background(AvaPurpleContainer),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Default.AutoAwesome,
                            contentDescription = null,
                            tint = AvaPurpleLight,
                            modifier = Modifier.size(28.dp)
                        )
                    }

                    Spacer(modifier = Modifier.height(24.dp))

                    Text(
                        text = storyHeadline,
                        color = Color.White,
                        fontSize = 24.sp,
                        fontWeight = FontWeight.Black,
                        letterSpacing = 1.sp
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    Text(
                        text = storySubtitle,
                        color = AvaPurpleLight,
                        fontSize = 13.sp,
                        fontWeight = FontWeight.Medium
                    )

                    Spacer(modifier = Modifier.height(30.dp))

                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(20.dp))
                            .background(Color.White)
                            .padding(horizontal = 18.dp, vertical = 8.dp)
                    ) {
                        Text(
                            text = "SWIPE UP / TAP",
                            color = Color.Black,
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(14.dp))

            // Text Inputs
            OutlinedTextField(
                value = storyHeadline,
                onValueChange = { storyHeadline = it },
                label = { Text("Story Headline") },
                singleLine = true,
                modifier = Modifier.fillMaxWidth(),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = AvaPurplePrimary,
                    unfocusedBorderColor = AvaCardBorder,
                    focusedTextColor = Color.White
                )
            )

            Spacer(modifier = Modifier.height(8.dp))

            // Gradients Picker
            LazyRow(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                items(gradients) { (name, grad) ->
                    val isSelected = selectedGradient == grad
                    Box(
                        modifier = Modifier
                            .size(36.dp)
                            .clip(CircleShape)
                            .background(Brush.verticalGradient(grad))
                            .border(
                                width = if (isSelected) 3.dp else 1.dp,
                                color = if (isSelected) AvaPurplePrimary else AvaCardBorder,
                                shape = CircleShape
                            )
                            .clickable { selectedGradient = grad }
                    )
                }
            }
        }
    }
}
