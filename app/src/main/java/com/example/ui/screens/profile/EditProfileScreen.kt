package com.example.ui.screens.profile

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
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
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.components.AvaSubTopBar
import com.example.ui.theme.AvaBlack
import com.example.ui.theme.AvaCardBorder
import com.example.ui.theme.AvaPurpleLight
import com.example.ui.theme.AvaPurplePrimary
import com.example.ui.theme.AvaTextPrimary
import com.example.ui.theme.AvaTextSecondary
import com.example.ui.viewmodel.MainViewModel
import com.example.util.AppLanguage
import com.example.util.stringRes
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@Composable
fun EditProfileScreen(
    viewModel: MainViewModel,
    modifier: Modifier = Modifier
) {
    val user by viewModel.currentUser.collectAsState()

    var displayName by remember(user) { mutableStateOf(user?.displayName ?: "") }
    var username by remember(user) { mutableStateOf(user?.username ?: "") }
    var bio by remember(user) { mutableStateOf(user?.bio ?: "") }
    var favoriteStyle by remember(user) { mutableStateOf(user?.favoriteStyle ?: "Dark Premium") }

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(AvaBlack)
    ) {
        AvaSubTopBar(
            title = stringRes("edit_profile"),
            onBack = { viewModel.navigateBack() }
        )

        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(14.dp)
        ) {
            item {
                OutlinedTextField(
                    value = displayName,
                    onValueChange = { displayName = it },
                    label = { Text(stringRes("display_name")) },
                    modifier = Modifier.fillMaxWidth().testTag("edit_display_name"),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = AvaPurplePrimary,
                        unfocusedBorderColor = AvaCardBorder,
                        focusedTextColor = Color.White
                    )
                )
            }

            item {
                OutlinedTextField(
                    value = username,
                    onValueChange = { username = it },
                    label = { Text(stringRes("username")) },
                    modifier = Modifier.fillMaxWidth().testTag("edit_username"),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = AvaPurplePrimary,
                        unfocusedBorderColor = AvaCardBorder,
                        focusedTextColor = Color.White
                    )
                )
            }

            item {
                OutlinedTextField(
                    value = bio,
                    onValueChange = { bio = it },
                    label = { Text(stringRes("bio")) },
                    modifier = Modifier.fillMaxWidth(),
                    minLines = 3,
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = AvaPurplePrimary,
                        unfocusedBorderColor = AvaCardBorder,
                        focusedTextColor = Color.White
                    )
                )
            }

            item {
                OutlinedTextField(
                    value = favoriteStyle,
                    onValueChange = { favoriteStyle = it },
                    label = { Text("Favorite Creative Style") },
                    modifier = Modifier.fillMaxWidth(),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = AvaPurplePrimary,
                        unfocusedBorderColor = AvaCardBorder,
                        focusedTextColor = Color.White
                    )
                )
            }

            item {
                Spacer(modifier = Modifier.height(10.dp))
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    OutlinedButton(
                        onClick = { viewModel.navigateBack() },
                        modifier = Modifier.weight(1f),
                        shape = RoundedCornerShape(10.dp),
                        colors = ButtonDefaults.outlinedButtonColors(contentColor = AvaTextPrimary)
                    ) {
                        Text(text = stringRes("cancel"))
                    }

                    Button(
                        onClick = {
                            CoroutineScope(Dispatchers.Main).launch {
                                viewModel.repository.updateProfile(
                                    displayName = displayName,
                                    username = username,
                                    bio = bio,
                                    avatarUri = user?.avatarUri,
                                    favoriteStyle = favoriteStyle,
                                    preferredLanguage = user?.preferredLanguage ?: "en"
                                )
                                viewModel.statusMessage.value = "Profile updated!"
                                viewModel.navigateBack()
                            }
                        },
                        modifier = Modifier.weight(1.5f).testTag("btn_save_profile_changes"),
                        shape = RoundedCornerShape(10.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = AvaPurplePrimary)
                    ) {
                        Text(text = stringRes("save_changes"), color = Color.White, fontWeight = FontWeight.Bold)
                    }
                }
            }
        }
    }
}
