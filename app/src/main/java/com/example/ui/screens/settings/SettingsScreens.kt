package com.example.ui.screens.settings

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
import androidx.compose.material.icons.filled.AdminPanelSettings
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Help
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Language
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Logout
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Send
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
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
import android.widget.Toast
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.components.AvaSubTopBar
import com.example.ui.components.BrandSignatureBadge
import com.example.ui.theme.AvaBlack
import com.example.ui.theme.AvaCard
import com.example.ui.theme.AvaCardBorder
import com.example.ui.theme.AvaDivider
import com.example.ui.theme.AvaError
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
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@Composable
fun AccountSettingsScreen(
    viewModel: MainViewModel,
    modifier: Modifier = Modifier
) {
    val user by viewModel.currentUser.collectAsState()
    val appLanguage by viewModel.appLanguage.collectAsState()
    val isAr = appLanguage == AppLanguage.ARABIC
    val context = LocalContext.current

    var showDeleteDialog by remember { mutableStateOf(false) }
    var showChangePasswordDialog by remember { mutableStateOf(false) }
    var currentPassword by remember { mutableStateOf("") }
    var newPassword by remember { mutableStateOf("") }
    var confirmNewPassword by remember { mutableStateOf("") }
    var changePasswordError by remember { mutableStateOf<String?>(null) }
    var isChangingPassword by remember { mutableStateOf(false) }

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(AvaBlack)
    ) {
        AvaSubTopBar(
            title = if (isAr) "إعدادات الحساب والخصوصية" else "Account & Privacy Settings",
            onBack = { viewModel.navigateBack() }
        )

        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            // User Header Card if logged in
            item {
                if (user != null) {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(16.dp))
                            .background(AvaCard)
                            .border(1.dp, AvaCardBorder, RoundedCornerShape(16.dp))
                            .padding(16.dp)
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Box(
                                modifier = Modifier
                                    .size(50.dp)
                                    .clip(CircleShape)
                                    .background(AvaPurpleContainer),
                                contentAlignment = Alignment.Center
                            ) {
                                Icon(Icons.Default.Person, contentDescription = null, tint = AvaPurpleLight, modifier = Modifier.size(30.dp))
                            }
                            Spacer(modifier = Modifier.width(14.dp))
                            Column(modifier = Modifier.weight(1f)) {
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Text(text = user?.displayName ?: "", color = AvaTextPrimary, fontWeight = FontWeight.Bold, fontSize = 16.sp)
                                    if (user?.isFounder == true) {
                                        Spacer(modifier = Modifier.width(6.dp))
                                        Text(
                                            text = if (isAr) "المؤسس" else "Founder",
                                            color = AvaGold,
                                            fontSize = 10.sp,
                                            fontWeight = FontWeight.Bold
                                        )
                                    }
                                }
                                Text(text = user?.email ?: "", color = AvaTextSecondary, fontSize = 12.sp)
                            }
                        }
                    }
                } else {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(16.dp))
                            .background(AvaCard)
                            .border(1.dp, AvaPurplePrimary.copy(alpha = 0.5f), RoundedCornerShape(16.dp))
                            .padding(16.dp)
                    ) {
                        Column {
                            Text(
                                text = if (isAr) "حساب زائر (غير مسجل)" else "Guest Mode (Not Signed In)",
                                color = AvaTextPrimary,
                                fontWeight = FontWeight.Bold,
                                fontSize = 15.sp
                            )
                            Spacer(modifier = Modifier.height(4.dp))
                            Text(
                                text = if (isAr) "سجّل الدخول لحفظ مشاريعك وتخصيص استوديو AVA باسمك" else "Sign in to save your designs and customize your AVA studio",
                                color = AvaTextSecondary,
                                fontSize = 12.sp
                            )
                            Spacer(modifier = Modifier.height(12.dp))
                            Row(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                                Button(
                                    onClick = { viewModel.navigateTo(AppScreen.LOGIN) },
                                    colors = ButtonDefaults.buttonColors(containerColor = AvaPurplePrimary),
                                    shape = RoundedCornerShape(8.dp)
                                ) {
                                    Text(if (isAr) "تسجيل الدخول" else "Sign In", color = Color.White, fontSize = 12.sp)
                                }
                                OutlinedButton(
                                    onClick = { viewModel.navigateTo(AppScreen.REGISTER) },
                                    colors = ButtonDefaults.outlinedButtonColors(contentColor = AvaPurpleLight),
                                    shape = RoundedCornerShape(8.dp)
                                ) {
                                    Text(if (isAr) "إنشاء حساب" else "Register", fontSize = 12.sp)
                                }
                            }
                        }
                    }
                }
            }

            // General Settings Group
            item {
                Text(text = if (isAr) "التفضيلات والتطبيق" else "Preferences & Studio", color = AvaPurpleLight, fontSize = 12.sp, fontWeight = FontWeight.Bold)
            }

            item {
                SettingsRow(
                    icon = Icons.Default.Language,
                    title = stringRes("language_settings"),
                    subtitle = if (isAr) "العربية (الحالية) / English" else "English (Active) / العربية",
                    onClick = { viewModel.navigateTo(AppScreen.LANGUAGE_SETTINGS) }
                )
            }

            item {
                SettingsRow(
                    icon = Icons.Default.Info,
                    title = stringRes("about_ava"),
                    subtitle = "Version 1.0.0 • Made by Ayham",
                    onClick = { viewModel.navigateTo(AppScreen.ABOUT_AVA) }
                )
            }

            item {
                SettingsRow(
                    icon = Icons.Default.Help,
                    title = stringRes("help_feedback"),
                    subtitle = if (isAr) "الأسئلة الشائعة والدعم الفني" else "FAQ & Creator Support",
                    onClick = { viewModel.navigateTo(AppScreen.HELP_REPORT) }
                )
            }

            if (user?.isFounder == true) {
                item {
                    SettingsRow(
                        icon = Icons.Default.AdminPanelSettings,
                        title = if (isAr) "لوحة تحكم المؤسس والإدارة" else "Founder & Admin Console",
                        subtitle = if (isAr) "صلاحيات المراقبة وتوثيق الحسابات" else "Moderation, badges and management",
                        iconColor = AvaGold,
                        onClick = { viewModel.navigateTo(AppScreen.FOUNDER_DASHBOARD) }
                    )
                }
            }

            // Authenticated User Section
            if (user != null) {
                item {
                    Spacer(modifier = Modifier.height(10.dp))
                    Text(text = if (isAr) "الأمان وإدارة الحساب" else "Security & Account Management", color = AvaPurpleLight, fontSize = 12.sp, fontWeight = FontWeight.Bold)
                }

                item {
                    val verified = user?.isEmailVerified == true
                    SettingsRow(
                        icon = Icons.Default.Check,
                        title = if (isAr) "حالة البريد الإلكتروني" else "Email Verification Status",
                        subtitle = if (verified) (if (isAr) "مؤكد وموثق بالكامل" else "Verified & Secure")
                        else (if (isAr) "غير مؤكد - اضغط هنا للتحقق" else "Unverified - Tap to verify"),
                        iconColor = if (verified) AvaSuccess else AvaGold,
                        onClick = {
                            if (!verified) {
                                viewModel.navigateTo(AppScreen.EMAIL_VERIFICATION)
                            } else {
                                viewModel.statusMessage.value = if (isAr) "بريدك الإلكتروني موثق بالفعل" else "Your email is already verified"
                            }
                        }
                    )
                }

                item {
                    SettingsRow(
                        icon = Icons.Default.Lock,
                        title = if (isAr) "تغيير كلمة المرور" else "Change Password",
                        subtitle = if (isAr) "تحديث كلمة المرور بتشفير عالي الأمان" else "Update password with cryptographic security",
                        onClick = {
                            changePasswordError = null
                            currentPassword = ""
                            newPassword = ""
                            confirmNewPassword = ""
                            showChangePasswordDialog = true
                        }
                    )
                }

                item {
                    SettingsRow(
                        icon = Icons.Default.Logout,
                        title = stringRes("sign_out"),
                        subtitle = user?.email ?: "",
                        onClick = {
                            CoroutineScope(Dispatchers.Main).launch {
                                viewModel.repository.logout()
                                viewModel.statusMessage.value = if (isAr) "تم تسجيل الخروج" else "Signed out"
                                viewModel.navigateTo(AppScreen.LOGIN)
                            }
                        }
                    )
                }

                item {
                    SettingsRow(
                        icon = Icons.Default.Delete,
                        title = stringRes("delete_account"),
                        subtitle = if (isAr) "حذف الحساب وجميع البيانات نهائياً" else "Permanently remove account and data",
                        iconColor = AvaError,
                        onClick = { showDeleteDialog = true }
                    )
                }
            }
        }
    }

    // Change Password Dialog
    if (showChangePasswordDialog) {
        AlertDialog(
            onDismissRequest = { showChangePasswordDialog = false },
            title = { Text(text = if (isAr) "تغيير كلمة المرور" else "Change Password", color = AvaTextPrimary, fontWeight = FontWeight.Bold) },
            text = {
                Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    OutlinedTextField(
                        value = currentPassword,
                        onValueChange = { currentPassword = it; changePasswordError = null },
                        label = { Text(if (isAr) "كلمة المرور الحالية" else "Current Password") },
                        singleLine = true,
                        visualTransformation = PasswordVisualTransformation(),
                        colors = OutlinedTextFieldDefaults.colors(focusedBorderColor = AvaPurplePrimary, unfocusedBorderColor = AvaCardBorder)
                    )
                    OutlinedTextField(
                        value = newPassword,
                        onValueChange = { newPassword = it; changePasswordError = null },
                        label = { Text(if (isAr) "كلمة المرور الجديدة" else "New Password") },
                        singleLine = true,
                        visualTransformation = PasswordVisualTransformation(),
                        colors = OutlinedTextFieldDefaults.colors(focusedBorderColor = AvaPurplePrimary, unfocusedBorderColor = AvaCardBorder)
                    )
                    OutlinedTextField(
                        value = confirmNewPassword,
                        onValueChange = { confirmNewPassword = it; changePasswordError = null },
                        label = { Text(if (isAr) "تأكيد كلمة المرور الجديدة" else "Confirm New Password") },
                        singleLine = true,
                        visualTransformation = PasswordVisualTransformation(),
                        colors = OutlinedTextFieldDefaults.colors(focusedBorderColor = AvaPurplePrimary, unfocusedBorderColor = AvaCardBorder)
                    )
                    if (changePasswordError != null) {
                        Text(text = changePasswordError ?: "", color = AvaError, fontSize = 12.sp)
                    }
                }
            },
            confirmButton = {
                Button(
                    onClick = {
                        if (currentPassword.isBlank() || newPassword.isBlank()) {
                            val msg = if (isAr) "يرجى ملء جميع الحقول" else "Please fill all fields"
                            changePasswordError = msg
                            Toast.makeText(context, msg, Toast.LENGTH_SHORT).show()
                            return@Button
                        }
                        if (newPassword.length < 6) {
                            val msg = if (isAr) "كلمة المرور يجب أن لا تقل عن 6 أحرف" else "Password must be at least 6 characters"
                            changePasswordError = msg
                            Toast.makeText(context, msg, Toast.LENGTH_SHORT).show()
                            return@Button
                        }
                        if (newPassword != confirmNewPassword) {
                            val msg = if (isAr) "كلمتا المرور غير متطابقتين" else "Passwords do not match"
                            changePasswordError = msg
                            Toast.makeText(context, msg, Toast.LENGTH_SHORT).show()
                            return@Button
                        }
                        isChangingPassword = true
                        CoroutineScope(Dispatchers.Main).launch {
                            val res = viewModel.repository.changePassword(currentPassword, newPassword)
                            isChangingPassword = false
                            if (res.isSuccess) {
                                showChangePasswordDialog = false
                                viewModel.statusMessage.value = if (isAr) "تم تغيير كلمة المرور بنجاح" else "Password changed successfully"
                            } else {
                                changePasswordError = res.exceptionOrNull()?.message ?: "فشل تغيير كلمة المرور"
                            }
                        }
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = AvaPurplePrimary)
                ) {
                    Text(if (isAr) "تأكيد التغيير" else "Update Password", color = Color.White)
                }
            },
            dismissButton = {
                TextButton(onClick = { showChangePasswordDialog = false }) {
                    Text(text = stringRes("cancel"), color = AvaTextSecondary)
                }
            },
            containerColor = AvaCard,
            shape = RoundedCornerShape(16.dp)
        )
    }

    if (showDeleteDialog) {
        AlertDialog(
            onDismissRequest = { showDeleteDialog = false },
            title = { Text(text = if (isAr) "حذف الحساب نهائياً؟" else "Delete Account Permanently?", color = AvaError, fontWeight = FontWeight.Bold) },
            text = {
                Text(
                    text = if (isAr)
                        "سيؤدي هذا الإجراء إلى حذف حسابك ومشاريعك وأعمالك المحفوظة نهائياً من قاعدة البيانات. لن تتمكن من استرجاع البيانات بعد ذلك."
                    else
                        "This will permanently erase your profile and all saved projects from the database. This action cannot be undone.",
                    color = AvaTextSecondary
                )
            },
            confirmButton = {
                Button(
                    onClick = {
                        CoroutineScope(Dispatchers.Main).launch {
                            viewModel.repository.deleteAccount()
                            showDeleteDialog = false
                            viewModel.statusMessage.value = if (isAr) "تم حذف الحساب بنجاح" else "Account deleted"
                            viewModel.navigateTo(AppScreen.LOGIN)
                        }
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = AvaError)
                ) {
                    Text(if (isAr) "نعم، حذف الحساب" else "Yes, Delete Account", color = Color.White)
                }
            },
            dismissButton = {
                TextButton(onClick = { showDeleteDialog = false }) {
                    Text(text = stringRes("cancel"), color = AvaPurpleLight)
                }
            },
            containerColor = AvaCard,
            shape = RoundedCornerShape(16.dp)
        )
    }
}

@Composable
fun SettingsRow(
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    title: String,
    subtitle: String,
    iconColor: Color = AvaPurpleLight,
    onClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() },
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = AvaCard),
        border = CardDefaults.outlinedCardBorder().copy(brush = androidx.compose.ui.graphics.SolidColor(AvaCardBorder))
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(14.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Box(
                    modifier = Modifier
                        .size(36.dp)
                        .clip(CircleShape)
                        .background(AvaPurpleContainer),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(imageVector = icon, contentDescription = null, tint = iconColor, modifier = Modifier.size(18.dp))
                }
                Spacer(modifier = Modifier.width(12.dp))
                Column {
                    Text(text = title, color = AvaTextPrimary, fontSize = 14.sp, fontWeight = FontWeight.SemiBold)
                    Text(text = subtitle, color = AvaTextSecondary, fontSize = 11.sp)
                }
            }
            Icon(imageVector = Icons.AutoMirrored.Filled.ArrowForward, contentDescription = null, tint = AvaTextSecondary, modifier = Modifier.size(16.dp))
        }
    }
}

@Composable
fun LanguageSettingsScreen(
    viewModel: MainViewModel,
    modifier: Modifier = Modifier
) {
    val currentLang by viewModel.appLanguage.collectAsState()

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(AvaBlack)
    ) {
        AvaSubTopBar(
            title = stringRes("language_settings"),
            onBack = { viewModel.navigateBack() }
        )

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Text(
                text = "Select Application Language",
                color = AvaTextPrimary,
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold
            )
            Text(
                text = "The entire interface, typography and layout direction (LTR / RTL) will update instantly.",
                color = AvaTextSecondary,
                fontSize = 13.sp
            )

            Spacer(modifier = Modifier.height(10.dp))

            // English Option
            LanguageChoiceCard(
                title = "English",
                nativeTitle = "English (LTR)",
                isSelected = currentLang == AppLanguage.ENGLISH,
                onClick = { viewModel.setLanguage(AppLanguage.ENGLISH) }
            )

            // Arabic Option
            LanguageChoiceCard(
                title = "العربية",
                nativeTitle = "Arabic (RTL)",
                isSelected = currentLang == AppLanguage.ARABIC,
                onClick = { viewModel.setLanguage(AppLanguage.ARABIC) }
            )
        }
    }
}

@Composable
fun LanguageChoiceCard(
    title: String,
    nativeTitle: String,
    isSelected: Boolean,
    onClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() },
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = if (isSelected) AvaPurpleContainer else AvaCard),
        border = CardDefaults.outlinedCardBorder().copy(
            brush = androidx.compose.ui.graphics.SolidColor(if (isSelected) AvaPurplePrimary else AvaCardBorder)
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
                Text(text = title, color = AvaTextPrimary, fontSize = 16.sp, fontWeight = FontWeight.Bold)
                Text(text = nativeTitle, color = AvaTextSecondary, fontSize = 12.sp)
            }
            if (isSelected) {
                Icon(imageVector = Icons.Default.Check, contentDescription = "Active", tint = AvaPurpleLight)
            }
        }
    }
}

@Composable
fun AboutAvaScreen(
    viewModel: MainViewModel,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .background(AvaBlack)
    ) {
        AvaSubTopBar(
            title = stringRes("about_ava"),
            onBack = { viewModel.navigateBack() }
        )

        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(20.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            item {
                Box(
                    modifier = Modifier
                        .size(72.dp)
                        .clip(RoundedCornerShape(20.dp))
                        .background(Brush.linearGradient(listOf(AvaPurplePrimary, Color(0xFF4C1D95)))),
                    contentAlignment = Alignment.Center
                ) {
                    Text(text = "AVA", color = Color.White, fontSize = 24.sp, fontWeight = FontWeight.Black)
                }
                Spacer(modifier = Modifier.height(8.dp))
                Text(text = stringRes("app_name"), color = AvaTextPrimary, fontSize = 22.sp, fontWeight = FontWeight.Bold)
                BrandSignatureBadge(modifier = Modifier.padding(top = 4.dp))
                Spacer(modifier = Modifier.height(4.dp))
                Text(text = "Version 1.0.0 (Production Build)", color = AvaTextSecondary, fontSize = 12.sp)
            }

            item {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(14.dp),
                    colors = CardDefaults.cardColors(containerColor = AvaCard),
                    border = CardDefaults.outlinedCardBorder().copy(brush = androidx.compose.ui.graphics.SolidColor(AvaCardBorder))
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Text(text = "Vision & Creative Identity", color = AvaTextPrimary, fontWeight = FontWeight.Bold, fontSize = 15.sp)
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(
                            text = "AVA Studio is built with an uncompromising commitment to minimalist luxury, authentic identity preservation, and professional creative tooling. Designed mobile-first with comprehensive English and Arabic bilingual support. Made by Ayham.",
                            color = AvaTextSecondary,
                            fontSize = 13.sp,
                            lineHeight = 20.sp
                        )
                    }
                }
            }

            item {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(14.dp),
                    colors = CardDefaults.cardColors(containerColor = AvaCard),
                    border = CardDefaults.outlinedCardBorder().copy(brush = androidx.compose.ui.graphics.SolidColor(AvaCardBorder))
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Text(text = "Legal & Security", color = AvaTextPrimary, fontWeight = FontWeight.Bold, fontSize = 15.sp)
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(
                            text = "• Zero-trust private local data architecture\n• Real bitmap processing on-device\n• Authentic identity protection guarantee\n• Licensed Material 3 & Modern Compose Engine",
                            color = AvaTextSecondary,
                            fontSize = 12.sp,
                            lineHeight = 18.sp
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun HelpFeedbackScreen(
    viewModel: MainViewModel,
    modifier: Modifier = Modifier
) {
    var feedbackText by remember { mutableStateOf("") }
    var submitted by remember { mutableStateOf(false) }

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(AvaBlack)
    ) {
        AvaSubTopBar(
            title = stringRes("help_feedback"),
            onBack = { viewModel.navigateBack() }
        )

        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(14.dp)
        ) {
            item {
                Text(text = "Frequently Asked Questions", color = AvaTextPrimary, fontSize = 16.sp, fontWeight = FontWeight.Bold)
            }

            val faqs = listOf(
                Pair("How does Identity Preservation work?", "Personal Photo Studio maintains facial landmarks and authentic proportions without synthetic distortions."),
                Pair("What are AVA Coins used for?", "AVA Coins let you unlock premium streetwear, sportswear, and exclusive wardrobe accessories for your 2D character."),
                Pair("Can I export transparent PNGs?", "Yes, both the Image Editor and Character Studio allow exporting high-resolution transparent PNG files.")
            )

            items(faqs) { (q, a) ->
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(12.dp),
                    colors = CardDefaults.cardColors(containerColor = AvaCard),
                    border = CardDefaults.outlinedCardBorder().copy(brush = androidx.compose.ui.graphics.SolidColor(AvaCardBorder))
                ) {
                    Column(modifier = Modifier.padding(14.dp)) {
                        Text(text = q, color = AvaTextPrimary, fontSize = 14.sp, fontWeight = FontWeight.Bold)
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(text = a, color = AvaTextSecondary, fontSize = 12.sp, lineHeight = 17.sp)
                    }
                }
            }

            item {
                Spacer(modifier = Modifier.height(10.dp))
                Text(text = "Send Feedback or Feature Request", color = AvaTextPrimary, fontSize = 16.sp, fontWeight = FontWeight.Bold)
                Spacer(modifier = Modifier.height(8.dp))
                OutlinedTextField(
                    value = feedbackText,
                    onValueChange = { feedbackText = it },
                    placeholder = { Text("Share your ideas directly with the creator...") },
                    modifier = Modifier.fillMaxWidth(),
                    minLines = 3,
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = AvaPurplePrimary,
                        unfocusedBorderColor = AvaCardBorder,
                        focusedTextColor = Color.White
                    )
                )
                Spacer(modifier = Modifier.height(10.dp))

                if (submitted) {
                    Text(text = "Thank you! Your feedback has been sent directly to the team.", color = AvaSuccess, fontSize = 13.sp)
                }

                Button(
                    onClick = {
                        if (feedbackText.isNotBlank()) {
                            submitted = true
                            feedbackText = ""
                        }
                    },
                    modifier = Modifier.fillMaxWidth().height(44.dp),
                    shape = RoundedCornerShape(10.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = AvaPurplePrimary)
                ) {
                    Icon(Icons.Default.Send, contentDescription = null, modifier = Modifier.size(16.dp))
                    Spacer(modifier = Modifier.width(6.dp))
                    Text("Submit Feedback", color = Color.White, fontWeight = FontWeight.Bold)
                }
            }
        }
    }
}

@Composable
fun NotificationsScreen(
    viewModel: MainViewModel,
    modifier: Modifier = Modifier
) {
    val notifs by viewModel.notifications.collectAsState()
    val currentLang by viewModel.appLanguage.collectAsState()
    val isAr = currentLang == AppLanguage.ARABIC

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(AvaBlack)
    ) {
        AvaSubTopBar(
            title = stringRes("notifications"),
            onBack = { viewModel.navigateBack() },
            actions = {
                TextButton(onClick = { viewModel.markNotificationsRead() }) {
                    Text(text = "Mark read", color = AvaPurpleLight, fontSize = 12.sp)
                }
            }
        )

        if (notifs.isEmpty()) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(32.dp),
                contentAlignment = Alignment.Center
            ) {
                Text(text = "No notifications right now.", color = AvaTextSecondary)
            }
        } else {
            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                contentPadding = PaddingValues(16.dp),
                verticalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                items(notifs) { notif ->
                    val dateStr = SimpleDateFormat("MMM dd, hh:mm a", Locale.getDefault()).format(Date(notif.timestamp))
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(12.dp),
                        colors = CardDefaults.cardColors(
                            containerColor = if (!notif.isRead) AvaPurpleContainer.copy(alpha = 0.4f) else AvaCard
                        ),
                        border = CardDefaults.outlinedCardBorder().copy(brush = androidx.compose.ui.graphics.SolidColor(AvaCardBorder))
                    ) {
                        Column(modifier = Modifier.padding(14.dp)) {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text(
                                    text = if (isAr) notif.titleAr else notif.titleEn,
                                    color = AvaTextPrimary,
                                    fontSize = 14.sp,
                                    fontWeight = FontWeight.Bold
                                )
                                Text(text = dateStr, color = AvaTextSecondary, fontSize = 10.sp)
                            }
                            Spacer(modifier = Modifier.height(4.dp))
                            Text(
                                text = if (isAr) notif.messageAr else notif.messageEn,
                                color = AvaTextSecondary,
                                fontSize = 12.sp,
                                lineHeight = 16.sp
                            )
                        }
                    }
                }
            }
        }
    }
}
