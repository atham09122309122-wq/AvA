package com.example.ui.screens.auth

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Security
import androidx.compose.material.icons.filled.VerifiedUser
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Checkbox
import androidx.compose.material3.CheckboxDefaults
import android.widget.Toast
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
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
import com.example.util.SecurityUtils
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@Composable
fun RegisterScreen(
    viewModel: MainViewModel,
    modifier: Modifier = Modifier
) {
    val scope = rememberCoroutineScope()
    val appLanguage by viewModel.appLanguage.collectAsState()
    val isAr = appLanguage == AppLanguage.ARABIC

    var displayName by remember { mutableStateOf("") }
    var username by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var confirmPassword by remember { mutableStateOf("") }
    var passwordVisible by remember { mutableStateOf(false) }
    var confirmPasswordVisible by remember { mutableStateOf(false) }
    var acceptedTerms by remember { mutableStateOf(false) }

    val context = LocalContext.current
    var emailTouched by remember { mutableStateOf(false) }
    var passwordTouched by remember { mutableStateOf(false) }
    var emailHasError by remember { mutableStateOf(false) }
    var passwordHasError by remember { mutableStateOf(false) }
    var emailErrorMessage by remember { mutableStateOf<String?>(null) }
    var passwordErrorMessage by remember { mutableStateOf<String?>(null) }

    var errorMessage by remember { mutableStateOf<String?>(null) }
    var isSubmitting by remember { mutableStateOf(false) }

    // Real-Time Email Validation Check with Toast Notification
    LaunchedEffect(email) {
        if (!emailTouched) return@LaunchedEffect
        delay(750L)
        if (email.isNotBlank()) {
            val valid = SecurityUtils.isValidEmail(email)
            emailHasError = !valid
            if (!valid) {
                val msg = if (isAr) "صيغة البريد الإلكتروني غير صحيحة (مثال: name@domain.com)" else "Invalid email format (e.g. name@domain.com)"
                emailErrorMessage = msg
                Toast.makeText(context, msg, Toast.LENGTH_SHORT).show()
            } else {
                emailErrorMessage = null
            }
        } else {
            emailHasError = false
            emailErrorMessage = null
        }
    }

    // Real-Time Password Requirements Check with Toast Notification
    LaunchedEffect(password) {
        if (!passwordTouched) return@LaunchedEffect
        delay(750L)
        if (password.isNotEmpty()) {
            val valid = SecurityUtils.isValidPassword(password)
            passwordHasError = !valid
            if (!valid) {
                val msg = if (isAr) "كلمة المرور يجب أن لا تقل عن 6 أحرف" else "Password must be at least 6 characters"
                passwordErrorMessage = msg
                Toast.makeText(context, msg, Toast.LENGTH_SHORT).show()
            } else {
                passwordErrorMessage = null
            }
        } else {
            passwordHasError = false
            passwordErrorMessage = null
        }
    }

    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .background(AvaBlack)
            .padding(horizontal = 24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        item {
            Spacer(modifier = Modifier.height(28.dp))
            Box(
                modifier = Modifier
                    .size(60.dp)
                    .clip(RoundedCornerShape(16.dp))
                    .background(Brush.linearGradient(listOf(AvaPurplePrimary, Color(0xFF4C1D95)))),
                contentAlignment = Alignment.Center
            ) {
                Text(text = "AVA", color = Color.White, fontSize = 22.sp, fontWeight = FontWeight.Black)
            }
            Spacer(modifier = Modifier.height(12.dp))
            Text(
                text = if (isAr) "إنشاء حساب جديد" else "Create New Account",
                color = AvaTextPrimary,
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold
            )
            Text(
                text = if (isAr) "انضم إلى استوديو AVA الإبداعي وحافظ على أعمالك وتصاميمك" else "Join AVA Creative Studio and secure your designs",
                color = AvaTextSecondary,
                fontSize = 12.sp,
                textAlign = TextAlign.Center,
                modifier = Modifier.padding(top = 4.dp, bottom = 12.dp)
            )
            BrandSignatureBadge()
            Spacer(modifier = Modifier.height(20.dp))
        }

        item {
            OutlinedTextField(
                value = displayName,
                onValueChange = { displayName = it; errorMessage = null },
                label = { Text(if (isAr) "اسم العرض (الاسم الكامل)" else "Display Name") },
                singleLine = true,
                leadingIcon = { Icon(Icons.Default.Person, contentDescription = null, tint = AvaTextSecondary) },
                modifier = Modifier.fillMaxWidth().testTag("reg_display_name"),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = AvaPurplePrimary,
                    unfocusedBorderColor = AvaCardBorder,
                    focusedTextColor = Color.White,
                    unfocusedTextColor = Color.White
                )
            )
            Spacer(modifier = Modifier.height(10.dp))

            OutlinedTextField(
                value = username,
                onValueChange = { username = it; errorMessage = null },
                label = { Text(if (isAr) "اسم المستخدم الفريد" else "Username") },
                singleLine = true,
                leadingIcon = { Text("@", color = AvaTextSecondary, modifier = Modifier.padding(start = 12.dp), fontWeight = FontWeight.Bold) },
                modifier = Modifier.fillMaxWidth().testTag("reg_username"),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = AvaPurplePrimary,
                    unfocusedBorderColor = AvaCardBorder,
                    focusedTextColor = Color.White,
                    unfocusedTextColor = Color.White
                )
            )
            Spacer(modifier = Modifier.height(10.dp))

            OutlinedTextField(
                value = email,
                onValueChange = {
                    email = it
                    emailTouched = true
                    errorMessage = null
                },
                label = { Text(if (isAr) "البريد الإلكتروني" else "Email Address") },
                singleLine = true,
                isError = emailHasError,
                supportingText = if (emailHasError && emailErrorMessage != null) {
                    { Text(text = emailErrorMessage ?: "", color = AvaError, fontSize = 11.sp) }
                } else null,
                leadingIcon = { Icon(Icons.Default.Email, contentDescription = null, tint = if (emailHasError) AvaError else AvaTextSecondary) },
                modifier = Modifier.fillMaxWidth().testTag("reg_email"),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = AvaPurplePrimary,
                    unfocusedBorderColor = AvaCardBorder,
                    errorBorderColor = AvaError,
                    focusedTextColor = Color.White,
                    unfocusedTextColor = Color.White
                )
            )
            Spacer(modifier = Modifier.height(10.dp))

            OutlinedTextField(
                value = password,
                onValueChange = {
                    password = it
                    passwordTouched = true
                    errorMessage = null
                },
                label = { Text(if (isAr) "كلمة المرور (6 أحرف فأكثر)" else "Password (min 6 characters)") },
                singleLine = true,
                visualTransformation = if (passwordVisible) VisualTransformation.None else PasswordVisualTransformation(),
                isError = passwordHasError,
                supportingText = if (passwordHasError && passwordErrorMessage != null) {
                    { Text(text = passwordErrorMessage ?: "", color = AvaError, fontSize = 11.sp) }
                } else null,
                leadingIcon = { Icon(Icons.Default.Lock, contentDescription = null, tint = if (passwordHasError) AvaError else AvaTextSecondary) },
                trailingIcon = {
                    IconButton(onClick = { passwordVisible = !passwordVisible }) {
                        Icon(
                            imageVector = if (passwordVisible) Icons.Default.Visibility else Icons.Default.VisibilityOff,
                            contentDescription = if (passwordVisible) "Hide password" else "Show password",
                            tint = AvaTextSecondary
                        )
                    }
                },
                modifier = Modifier.fillMaxWidth().testTag("reg_password"),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = AvaPurplePrimary,
                    unfocusedBorderColor = AvaCardBorder,
                    errorBorderColor = AvaError,
                    focusedTextColor = Color.White,
                    unfocusedTextColor = Color.White
                )
            )
            Spacer(modifier = Modifier.height(10.dp))

            OutlinedTextField(
                value = confirmPassword,
                onValueChange = { confirmPassword = it; errorMessage = null },
                label = { Text(if (isAr) "تأكيد كلمة المرور" else "Confirm Password") },
                singleLine = true,
                visualTransformation = if (confirmPasswordVisible) VisualTransformation.None else PasswordVisualTransformation(),
                leadingIcon = { Icon(Icons.Default.Lock, contentDescription = null, tint = AvaTextSecondary) },
                trailingIcon = {
                    IconButton(onClick = { confirmPasswordVisible = !confirmPasswordVisible }) {
                        Icon(
                            imageVector = if (confirmPasswordVisible) Icons.Default.Visibility else Icons.Default.VisibilityOff,
                            contentDescription = if (confirmPasswordVisible) "Hide password" else "Show password",
                            tint = AvaTextSecondary
                        )
                    }
                },
                modifier = Modifier.fillMaxWidth().testTag("reg_confirm_password"),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = AvaPurplePrimary,
                    unfocusedBorderColor = AvaCardBorder,
                    focusedTextColor = Color.White,
                    unfocusedTextColor = Color.White
                )
            )

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 8.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Checkbox(
                    checked = acceptedTerms,
                    onCheckedChange = { acceptedTerms = it; errorMessage = null },
                    colors = CheckboxDefaults.colors(
                        checkedColor = AvaPurplePrimary,
                        uncheckedColor = AvaCardBorder
                    ),
                    modifier = Modifier.testTag("reg_terms_checkbox")
                )
                Text(
                    text = if (isAr) "أوافق على شروط الاستخدام وسياسة الخصوصية الآمنة لـ AVA" else "I agree to AVA Terms of Service and Privacy Policy",
                    color = AvaTextSecondary,
                    fontSize = 12.sp
                )
            }

            if (errorMessage != null) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 10.dp)
                        .clip(RoundedCornerShape(8.dp))
                        .background(AvaError.copy(alpha = 0.15f))
                        .border(1.dp, AvaError.copy(alpha = 0.4f), RoundedCornerShape(8.dp))
                        .padding(10.dp)
                ) {
                    Text(
                        text = errorMessage ?: "",
                        color = AvaError,
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Medium
                    )
                }
            }

            Button(
                onClick = {
                    if (displayName.isBlank() || username.isBlank() || email.isBlank() || password.isBlank()) {
                        val msg = if (isAr) "يرجى ملء جميع الحقول المطلوبة" else "Please fill in all required fields"
                        errorMessage = msg
                        Toast.makeText(context, msg, Toast.LENGTH_SHORT).show()
                        return@Button
                    }
                    if (!SecurityUtils.isValidEmail(email)) {
                        val msg = if (isAr) "صيغة البريد الإلكتروني غير صحيحة" else "Invalid email address format"
                        errorMessage = msg
                        emailHasError = true
                        emailErrorMessage = msg
                        Toast.makeText(context, msg, Toast.LENGTH_SHORT).show()
                        return@Button
                    }
                    if (password.length < 6) {
                        val msg = if (isAr) "كلمة المرور يجب أن لا تقل عن 6 أحرف" else "Password must be at least 6 characters"
                        errorMessage = msg
                        passwordHasError = true
                        passwordErrorMessage = msg
                        Toast.makeText(context, msg, Toast.LENGTH_SHORT).show()
                        return@Button
                    }
                    if (password != confirmPassword) {
                        val msg = if (isAr) "كلمتا المرور غير متطابقتين" else "Passwords do not match"
                        errorMessage = msg
                        Toast.makeText(context, msg, Toast.LENGTH_SHORT).show()
                        return@Button
                    }
                    if (!acceptedTerms) {
                        val msg = if (isAr) "يرجى الموافقة على الشروط وسياسة الخصوصية للمتابعة" else "Please accept the terms and conditions"
                        errorMessage = msg
                        Toast.makeText(context, msg, Toast.LENGTH_SHORT).show()
                        return@Button
                    }

                    isSubmitting = true
                    scope.launch {
                        val result = viewModel.repository.register(
                            displayName = displayName,
                            username = username,
                            email = email,
                            password = password
                        )
                        isSubmitting = false
                        if (result.isSuccess) {
                            viewModel.statusMessage.value = if (isAr) "تم إنشاء الحساب بنجاح!" else "Account created successfully!"
                            viewModel.navigateTo(AppScreen.EMAIL_VERIFICATION)
                        } else {
                            errorMessage = result.exceptionOrNull()?.message ?: "فشل إنشاء الحساب"
                        }
                    }
                },
                enabled = !isSubmitting,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(48.dp)
                    .testTag("btn_register_submit"),
                shape = RoundedCornerShape(12.dp),
                colors = ButtonDefaults.buttonColors(containerColor = AvaPurplePrimary)
            ) {
                if (isSubmitting) {
                    CircularProgressIndicator(modifier = Modifier.size(20.dp), color = Color.White, strokeWidth = 2.dp)
                } else {
                    Text(text = if (isAr) "إنشاء الحساب الآن" else "Register Account", color = Color.White, fontWeight = FontWeight.Bold, fontSize = 15.sp)
                }
            }

            SocialAuthSection(
                onGoogleClick = {
                    isSubmitting = true
                    scope.launch {
                        val res = viewModel.repository.signInWithSocialProvider("google")
                        isSubmitting = false
                        if (res.isSuccess) {
                            viewModel.statusMessage.value = if (isAr) "تم تسجيل الدخول بنجاح عبر Google!" else "Signed in with Google successfully!"
                            viewModel.navigateToBottomDestination(AppScreen.HOME)
                        } else {
                            errorMessage = res.exceptionOrNull()?.message ?: "فشل تسجيل الدخول عبر Google"
                        }
                    }
                },
                onAppleClick = {
                    isSubmitting = true
                    scope.launch {
                        val res = viewModel.repository.signInWithSocialProvider("apple")
                        isSubmitting = false
                        if (res.isSuccess) {
                            viewModel.statusMessage.value = if (isAr) "تم تسجيل الدخول بنجاح عبر Apple!" else "Signed in with Apple successfully!"
                            viewModel.navigateToBottomDestination(AppScreen.HOME)
                        } else {
                            errorMessage = res.exceptionOrNull()?.message ?: "فشل تسجيل الدخول عبر Apple"
                        }
                    }
                },
                isAr = isAr,
                isLoading = isSubmitting
            )

            Spacer(modifier = Modifier.height(16.dp))

            TextButton(onClick = { viewModel.navigateTo(AppScreen.LOGIN) }) {
                Text(
                    text = if (isAr) "لديك حساب بالفعل؟ تسجيل الدخول" else "Already have an account? Sign in",
                    color = AvaPurpleLight,
                    fontSize = 13.sp,
                    fontWeight = FontWeight.SemiBold
                )
            }

            TextButton(onClick = { viewModel.navigateToBottomDestination(AppScreen.HOME) }) {
                Text(
                    text = if (isAr) "المتابعة كزائر وتصفح الاستوديو" else "Continue as Guest",
                    color = AvaTextSecondary,
                    fontSize = 12.sp
                )
            }

            Spacer(modifier = Modifier.height(28.dp))
        }
    }
}

@Composable
fun LoginScreen(
    viewModel: MainViewModel,
    modifier: Modifier = Modifier
) {
    val scope = rememberCoroutineScope()
    val appLanguage by viewModel.appLanguage.collectAsState()
    val isAr = appLanguage == AppLanguage.ARABIC

    var identifier by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var passwordVisible by remember { mutableStateOf(false) }

    val context = LocalContext.current
    var identifierTouched by remember { mutableStateOf(false) }
    var passwordTouched by remember { mutableStateOf(false) }
    var identifierHasError by remember { mutableStateOf(false) }
    var passwordHasError by remember { mutableStateOf(false) }
    var identifierErrorMessage by remember { mutableStateOf<String?>(null) }
    var passwordErrorMessage by remember { mutableStateOf<String?>(null) }

    var errorMessage by remember { mutableStateOf<String?>(null) }
    var isSubmitting by remember { mutableStateOf(false) }

    // Real-Time Identifier / Email Validation Check with Toast Notification
    LaunchedEffect(identifier) {
        if (!identifierTouched) return@LaunchedEffect
        delay(750L)
        if (identifier.contains("@")) {
            val valid = SecurityUtils.isValidEmail(identifier)
            identifierHasError = !valid
            if (!valid) {
                val msg = if (isAr) "صيغة البريد الإلكتروني غير صحيحة" else "Invalid email format"
                identifierErrorMessage = msg
                Toast.makeText(context, msg, Toast.LENGTH_SHORT).show()
            } else {
                identifierErrorMessage = null
            }
        } else {
            identifierHasError = false
            identifierErrorMessage = null
        }
    }

    // Real-Time Password Requirements Check with Toast Notification
    LaunchedEffect(password) {
        if (!passwordTouched) return@LaunchedEffect
        delay(750L)
        if (password.isNotEmpty() && password.length < 6) {
            passwordHasError = true
            val msg = if (isAr) "كلمة المرور يجب أن لا تقل عن 6 أحرف" else "Password must be at least 6 characters"
            passwordErrorMessage = msg
            Toast.makeText(context, msg, Toast.LENGTH_SHORT).show()
        } else {
            passwordHasError = false
            passwordErrorMessage = null
        }
    }

    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .background(AvaBlack)
            .padding(horizontal = 24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        item {
            Spacer(modifier = Modifier.height(36.dp))
            Box(
                modifier = Modifier
                    .size(64.dp)
                    .clip(RoundedCornerShape(18.dp))
                    .background(Brush.linearGradient(listOf(AvaPurplePrimary, Color(0xFF4C1D95)))),
                contentAlignment = Alignment.Center
            ) {
                Text(text = "AVA", color = Color.White, fontSize = 24.sp, fontWeight = FontWeight.Black)
            }
            Spacer(modifier = Modifier.height(12.dp))
            Text(
                text = if (isAr) "تسجيل الدخول" else "Welcome Back",
                color = AvaTextPrimary,
                fontSize = 26.sp,
                fontWeight = FontWeight.Bold
            )
            Text(
                text = if (isAr) "سجّل الدخول للوصول إلى مشاريعك واستوديوهات التصميم" else "Sign in to access your projects and creative studios",
                color = AvaTextSecondary,
                fontSize = 12.sp,
                textAlign = TextAlign.Center,
                modifier = Modifier.padding(top = 4.dp, bottom = 8.dp)
            )
            BrandSignatureBadge()

            Spacer(modifier = Modifier.height(28.dp))

            OutlinedTextField(
                value = identifier,
                onValueChange = {
                    identifier = it
                    identifierTouched = true
                    errorMessage = null
                },
                label = { Text(if (isAr) "البريد الإلكتروني أو اسم المستخدم" else "Email or @username") },
                singleLine = true,
                isError = identifierHasError,
                supportingText = if (identifierHasError && identifierErrorMessage != null) {
                    { Text(text = identifierErrorMessage ?: "", color = AvaError, fontSize = 11.sp) }
                } else null,
                leadingIcon = { Icon(Icons.Default.Person, contentDescription = null, tint = if (identifierHasError) AvaError else AvaTextSecondary) },
                modifier = Modifier.fillMaxWidth().testTag("login_identifier"),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = AvaPurplePrimary,
                    unfocusedBorderColor = AvaCardBorder,
                    errorBorderColor = AvaError,
                    focusedTextColor = Color.White,
                    unfocusedTextColor = Color.White
                )
            )
            Spacer(modifier = Modifier.height(12.dp))

            OutlinedTextField(
                value = password,
                onValueChange = {
                    password = it
                    passwordTouched = true
                    errorMessage = null
                },
                label = { Text(if (isAr) "كلمة المرور" else "Password") },
                singleLine = true,
                visualTransformation = if (passwordVisible) VisualTransformation.None else PasswordVisualTransformation(),
                isError = passwordHasError,
                supportingText = if (passwordHasError && passwordErrorMessage != null) {
                    { Text(text = passwordErrorMessage ?: "", color = AvaError, fontSize = 11.sp) }
                } else null,
                leadingIcon = { Icon(Icons.Default.Lock, contentDescription = null, tint = if (passwordHasError) AvaError else AvaTextSecondary) },
                trailingIcon = {
                    IconButton(onClick = { passwordVisible = !passwordVisible }) {
                        Icon(
                            imageVector = if (passwordVisible) Icons.Default.Visibility else Icons.Default.VisibilityOff,
                            contentDescription = if (passwordVisible) "Hide password" else "Show password",
                            tint = AvaTextSecondary
                        )
                    }
                },
                modifier = Modifier.fillMaxWidth().testTag("login_password"),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = AvaPurplePrimary,
                    unfocusedBorderColor = AvaCardBorder,
                    errorBorderColor = AvaError,
                    focusedTextColor = Color.White,
                    unfocusedTextColor = Color.White
                )
            )

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.End
            ) {
                TextButton(onClick = { viewModel.navigateTo(AppScreen.FORGOT_PASSWORD) }) {
                    Text(
                        text = if (isAr) "نسيت كلمة المرور؟" else "Forgot password?",
                        color = AvaPurpleLight,
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Medium
                    )
                }
            }

            if (errorMessage != null) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 12.dp)
                        .clip(RoundedCornerShape(8.dp))
                        .background(AvaError.copy(alpha = 0.15f))
                        .border(1.dp, AvaError.copy(alpha = 0.4f), RoundedCornerShape(8.dp))
                        .padding(10.dp)
                ) {
                    Text(
                        text = errorMessage ?: "",
                        color = AvaError,
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Medium
                    )
                }
            }

            Button(
                onClick = {
                    if (identifier.isBlank() || password.isBlank()) {
                        val msg = if (isAr) "يرجى إدخال البريد الإلكتروني أو اسم المستخدم وكلمة المرور" else "Please enter email/username and password"
                        errorMessage = msg
                        Toast.makeText(context, msg, Toast.LENGTH_SHORT).show()
                        return@Button
                    }
                    if (identifier.contains("@") && !SecurityUtils.isValidEmail(identifier)) {
                        val msg = if (isAr) "صيغة البريد الإلكتروني غير صحيحة" else "Invalid email format"
                        errorMessage = msg
                        identifierHasError = true
                        identifierErrorMessage = msg
                        Toast.makeText(context, msg, Toast.LENGTH_SHORT).show()
                        return@Button
                    }
                    if (password.length < 6) {
                        val msg = if (isAr) "كلمة المرور يجب أن لا تقل عن 6 أحرف" else "Password must be at least 6 characters"
                        errorMessage = msg
                        passwordHasError = true
                        passwordErrorMessage = msg
                        Toast.makeText(context, msg, Toast.LENGTH_SHORT).show()
                        return@Button
                    }
                    isSubmitting = true
                    scope.launch {
                        val result = viewModel.repository.login(identifier, password)
                        isSubmitting = false
                        if (result.isSuccess) {
                            viewModel.statusMessage.value = if (isAr) "تم تسجيل الدخول بنجاح!" else "Signed in successfully!"
                            viewModel.navigateToBottomDestination(AppScreen.HOME)
                        } else {
                            errorMessage = result.exceptionOrNull()?.message ?: "فشل تسجيل الدخول"
                        }
                    }
                },
                enabled = !isSubmitting,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(48.dp)
                    .testTag("btn_login_submit"),
                shape = RoundedCornerShape(12.dp),
                colors = ButtonDefaults.buttonColors(containerColor = AvaPurplePrimary)
            ) {
                if (isSubmitting) {
                    CircularProgressIndicator(modifier = Modifier.size(20.dp), color = Color.White, strokeWidth = 2.dp)
                } else {
                    Text(text = if (isAr) "تسجيل الدخول" else "Sign In", color = Color.White, fontWeight = FontWeight.Bold, fontSize = 15.sp)
                }
            }

            SocialAuthSection(
                onGoogleClick = {
                    isSubmitting = true
                    scope.launch {
                        val res = viewModel.repository.signInWithSocialProvider("google")
                        isSubmitting = false
                        if (res.isSuccess) {
                            viewModel.statusMessage.value = if (isAr) "تم تسجيل الدخول بنجاح عبر Google!" else "Signed in with Google successfully!"
                            viewModel.navigateToBottomDestination(AppScreen.HOME)
                        } else {
                            errorMessage = res.exceptionOrNull()?.message ?: "فشل تسجيل الدخول عبر Google"
                        }
                    }
                },
                onAppleClick = {
                    isSubmitting = true
                    scope.launch {
                        val res = viewModel.repository.signInWithSocialProvider("apple")
                        isSubmitting = false
                        if (res.isSuccess) {
                            viewModel.statusMessage.value = if (isAr) "تم تسجيل الدخول بنجاح عبر Apple!" else "Signed in with Apple successfully!"
                            viewModel.navigateToBottomDestination(AppScreen.HOME)
                        } else {
                            errorMessage = res.exceptionOrNull()?.message ?: "فشل تسجيل الدخول عبر Apple"
                        }
                    }
                },
                isAr = isAr,
                isLoading = isSubmitting
            )

            Spacer(modifier = Modifier.height(16.dp))

            TextButton(onClick = { viewModel.navigateTo(AppScreen.REGISTER) }) {
                Text(
                    text = if (isAr) "ليس لديك حساب؟ إنشاء حساب جديد" else "Don't have an account? Sign up",
                    color = AvaPurpleLight,
                    fontSize = 13.sp,
                    fontWeight = FontWeight.SemiBold
                )
            }

            TextButton(onClick = { viewModel.navigateToBottomDestination(AppScreen.HOME) }) {
                Text(
                    text = if (isAr) "المتابعة كزائر دون تسجيل دخول" else "Continue as Guest",
                    color = AvaTextSecondary,
                    fontSize = 12.sp
                )
            }

            Spacer(modifier = Modifier.height(36.dp))
        }
    }
}

@Composable
fun EmailVerificationScreen(
    viewModel: MainViewModel,
    modifier: Modifier = Modifier
) {
    val scope = rememberCoroutineScope()
    val appLanguage by viewModel.appLanguage.collectAsState()
    val user by viewModel.currentUser.collectAsState()
    val isAr = appLanguage == AppLanguage.ARABIC

    var codeInput by remember { mutableStateOf("") }
    var statusText by remember { mutableStateOf<String?>(null) }
    var isError by remember { mutableStateOf(false) }
    var isSubmitting by remember { mutableStateOf(false) }

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(AvaBlack)
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Box(
            modifier = Modifier
                .size(68.dp)
                .clip(CircleShape)
                .background(AvaPurpleContainer),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = Icons.Default.VerifiedUser,
                contentDescription = null,
                tint = AvaPurpleLight,
                modifier = Modifier.size(36.dp)
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        Text(
            text = if (isAr) "تأكيد البريد الإلكتروني" else "Email Verification",
            color = AvaTextPrimary,
            fontSize = 22.sp,
            fontWeight = FontWeight.Bold
        )

        Spacer(modifier = Modifier.height(6.dp))

        Text(
            text = if (isAr)
                "أرسلنا رمز تحقق مكوّن من 6 أرقام إلى بريدك الإلكتروني:\n${user?.email ?: ""}"
            else
                "We sent a 6-digit confirmation code to your email:\n${user?.email ?: ""}",
            color = AvaTextSecondary,
            fontSize = 13.sp,
            textAlign = TextAlign.Center
        )

        Spacer(modifier = Modifier.height(24.dp))

        OutlinedTextField(
            value = codeInput,
            onValueChange = {
                if (it.length <= 6) codeInput = it
                statusText = null
            },
            label = { Text(if (isAr) "أدخل الرمز (6 أرقام)" else "Enter 6-digit code") },
            singleLine = true,
            modifier = Modifier.fillMaxWidth().testTag("verification_code_input"),
            colors = OutlinedTextFieldDefaults.colors(
                focusedBorderColor = AvaPurplePrimary,
                unfocusedBorderColor = AvaCardBorder,
                focusedTextColor = Color.White,
                unfocusedTextColor = Color.White
            )
        )

        Spacer(modifier = Modifier.height(12.dp))

        if (statusText != null) {
            Text(
                text = statusText ?: "",
                color = if (isError) AvaError else AvaSuccess,
                fontSize = 13.sp,
                fontWeight = FontWeight.Medium,
                textAlign = TextAlign.Center,
                modifier = Modifier.padding(bottom = 10.dp)
            )
        }

        Button(
            onClick = {
                if (codeInput.isBlank()) {
                    statusText = if (isAr) "يرجى إدخال رمز التحقق" else "Please enter the code"
                    isError = true
                    return@Button
                }
                isSubmitting = true
                scope.launch {
                    val result = viewModel.repository.verifyEmailCode(codeInput)
                    isSubmitting = false
                    if (result.isSuccess) {
                        statusText = if (isAr) "تم تأكيد بريدك الإلكتروني بنجاح!" else "Email verified successfully!"
                        isError = false
                        viewModel.statusMessage.value = statusText
                        viewModel.navigateToBottomDestination(AppScreen.HOME)
                    } else {
                        statusText = result.exceptionOrNull()?.message ?: "رمز غير صحيح"
                        isError = true
                    }
                }
            },
            enabled = !isSubmitting,
            modifier = Modifier.fillMaxWidth().height(48.dp).testTag("btn_verify_code"),
            shape = RoundedCornerShape(12.dp),
            colors = ButtonDefaults.buttonColors(containerColor = AvaPurplePrimary)
        ) {
            if (isSubmitting) {
                CircularProgressIndicator(modifier = Modifier.size(20.dp), color = Color.White, strokeWidth = 2.dp)
            } else {
                Text(text = if (isAr) "تأكيد الحساب" else "Confirm Account", color = Color.White, fontWeight = FontWeight.Bold)
            }
        }

        Spacer(modifier = Modifier.height(12.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            TextButton(
                onClick = {
                    scope.launch {
                        val res = viewModel.repository.resendVerificationCode()
                        if (res.isSuccess) {
                            statusText = if (isAr) "تم إرسال رمز جديد: ${res.getOrNull()}" else "New code sent: ${res.getOrNull()}"
                            isError = false
                        }
                    }
                }
            ) {
                Text(text = if (isAr) "إعادة إرسال الرمز" else "Resend Code", color = AvaPurpleLight)
            }

            TextButton(onClick = { viewModel.navigateToBottomDestination(AppScreen.HOME) }) {
                Text(text = if (isAr) "التأكيد لاحقاً" else "Verify Later", color = AvaTextSecondary)
            }
        }
    }
}

@Composable
fun ForgotPasswordScreen(
    viewModel: MainViewModel,
    modifier: Modifier = Modifier
) {
    val scope = rememberCoroutineScope()
    val appLanguage by viewModel.appLanguage.collectAsState()
    val isAr = appLanguage == AppLanguage.ARABIC
    val context = LocalContext.current

    var email by remember { mutableStateOf("") }
    var emailTouched by remember { mutableStateOf(false) }
    var emailHasError by remember { mutableStateOf(false) }

    var resetCode by remember { mutableStateOf("") }
    var newPassword by remember { mutableStateOf("") }
    var newPasswordTouched by remember { mutableStateOf(false) }
    var newPasswordHasError by remember { mutableStateOf(false) }

    var confirmNewPassword by remember { mutableStateOf("") }
    var passwordVisible by remember { mutableStateOf(false) }

    var step by remember { mutableStateOf(1) } // 1: Request Code, 2: Reset with code
    var statusMessage by remember { mutableStateOf<String?>(null) }
    var isError by remember { mutableStateOf(false) }
    var isSubmitting by remember { mutableStateOf(false) }

    // Real-Time Email Validation in Forgot Password Screen
    LaunchedEffect(email) {
        if (!emailTouched) return@LaunchedEffect
        delay(750L)
        if (email.isNotBlank()) {
            val valid = SecurityUtils.isValidEmail(email)
            emailHasError = !valid
            if (!valid) {
                val msg = if (isAr) "صيغة البريد الإلكتروني غير صحيحة" else "Invalid email format"
                Toast.makeText(context, msg, Toast.LENGTH_SHORT).show()
            }
        } else {
            emailHasError = false
        }
    }

    // Real-Time New Password Validation in Forgot Password Screen
    LaunchedEffect(newPassword) {
        if (!newPasswordTouched) return@LaunchedEffect
        delay(750L)
        if (newPassword.isNotEmpty() && newPassword.length < 6) {
            newPasswordHasError = true
            val msg = if (isAr) "كلمة المرور يجب أن لا تقل عن 6 أحرف" else "Password must be at least 6 characters"
            Toast.makeText(context, msg, Toast.LENGTH_SHORT).show()
        } else {
            newPasswordHasError = false
        }
    }

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(AvaBlack)
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Box(
            modifier = Modifier
                .size(64.dp)
                .clip(CircleShape)
                .background(AvaPurpleContainer),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = Icons.Default.Security,
                contentDescription = null,
                tint = AvaPurpleLight,
                modifier = Modifier.size(34.dp)
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        Text(
            text = if (isAr) "استعادة كلمة المرور" else "Reset Password",
            color = AvaTextPrimary,
            fontSize = 22.sp,
            fontWeight = FontWeight.Bold
        )

        Spacer(modifier = Modifier.height(6.dp))

        Text(
            text = if (step == 1) {
                if (isAr) "أدخل بريدك الإلكتروني المسجل وسنرسل لك رمز أمان لإعادة تعيين كلمة المرور."
                else "Enter your registered email address to receive a secure password reset code."
            } else {
                if (isAr) "أدخل رمز الأمان المرسل وكلمة المرور الجديدة لحسابك."
                else "Enter the reset code sent to your email and your new password."
            },
            color = AvaTextSecondary,
            fontSize = 12.sp,
            textAlign = TextAlign.Center
        )

        Spacer(modifier = Modifier.height(24.dp))

        if (step == 1) {
            OutlinedTextField(
                value = email,
                onValueChange = {
                    email = it
                    emailTouched = true
                    statusMessage = null
                },
                label = { Text(if (isAr) "البريد الإلكتروني" else "Registered Email") },
                singleLine = true,
                isError = emailHasError,
                leadingIcon = { Icon(Icons.Default.Email, contentDescription = null, tint = if (emailHasError) AvaError else AvaTextSecondary) },
                modifier = Modifier.fillMaxWidth().testTag("reset_email_input"),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = AvaPurplePrimary,
                    unfocusedBorderColor = AvaCardBorder,
                    errorBorderColor = AvaError,
                    focusedTextColor = Color.White,
                    unfocusedTextColor = Color.White
                )
            )
            Spacer(modifier = Modifier.height(16.dp))

            if (statusMessage != null) {
                Text(
                    text = statusMessage ?: "",
                    color = if (isError) AvaError else AvaSuccess,
                    fontSize = 12.sp,
                    modifier = Modifier.padding(bottom = 10.dp)
                )
            }

            Button(
                onClick = {
                    if (email.isBlank()) {
                        val msg = if (isAr) "يرجى إدخال البريد الإلكتروني" else "Please enter your email"
                        statusMessage = msg
                        isError = true
                        Toast.makeText(context, msg, Toast.LENGTH_SHORT).show()
                        return@Button
                    }
                    if (!SecurityUtils.isValidEmail(email)) {
                        val msg = if (isAr) "صيغة البريد الإلكتروني غير صحيحة" else "Invalid email format"
                        statusMessage = msg
                        isError = true
                        emailHasError = true
                        Toast.makeText(context, msg, Toast.LENGTH_SHORT).show()
                        return@Button
                    }
                    isSubmitting = true
                    scope.launch {
                        val result = viewModel.repository.requestPasswordReset(email)
                        isSubmitting = false
                        if (result.isSuccess) {
                            step = 2
                            statusMessage = if (isAr) "تم إنشاء رمز الاستعادة: ${result.getOrNull()}" else "Reset code generated: ${result.getOrNull()}"
                            isError = false
                        } else {
                            statusMessage = result.exceptionOrNull()?.message ?: "فشل طلب الاستعادة"
                            isError = true
                        }
                    }
                },
                enabled = !isSubmitting,
                modifier = Modifier.fillMaxWidth().height(48.dp).testTag("btn_request_reset_code"),
                shape = RoundedCornerShape(12.dp),
                colors = ButtonDefaults.buttonColors(containerColor = AvaPurplePrimary)
            ) {
                if (isSubmitting) {
                    CircularProgressIndicator(modifier = Modifier.size(20.dp), color = Color.White, strokeWidth = 2.dp)
                } else {
                    Text(text = if (isAr) "إرسال رمز الاستعادة" else "Send Reset Code", color = Color.White, fontWeight = FontWeight.Bold)
                }
            }
        } else {
            OutlinedTextField(
                value = resetCode,
                onValueChange = { resetCode = it; statusMessage = null },
                label = { Text(if (isAr) "رمز الاستعادة (6 أرقام)" else "Reset Code") },
                singleLine = true,
                modifier = Modifier.fillMaxWidth().testTag("reset_code_input"),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = AvaPurplePrimary,
                    unfocusedBorderColor = AvaCardBorder,
                    focusedTextColor = Color.White,
                    unfocusedTextColor = Color.White
                )
            )
            Spacer(modifier = Modifier.height(10.dp))

            OutlinedTextField(
                value = newPassword,
                onValueChange = {
                    newPassword = it
                    newPasswordTouched = true
                    statusMessage = null
                },
                label = { Text(if (isAr) "كلمة المرور الجديدة" else "New Password") },
                singleLine = true,
                visualTransformation = if (passwordVisible) VisualTransformation.None else PasswordVisualTransformation(),
                isError = newPasswordHasError,
                trailingIcon = {
                    IconButton(onClick = { passwordVisible = !passwordVisible }) {
                        Icon(
                            imageVector = if (passwordVisible) Icons.Default.Visibility else Icons.Default.VisibilityOff,
                            contentDescription = null,
                            tint = AvaTextSecondary
                        )
                    }
                },
                modifier = Modifier.fillMaxWidth().testTag("reset_new_password_input"),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = AvaPurplePrimary,
                    unfocusedBorderColor = AvaCardBorder,
                    errorBorderColor = AvaError,
                    focusedTextColor = Color.White,
                    unfocusedTextColor = Color.White
                )
            )
            Spacer(modifier = Modifier.height(10.dp))

            OutlinedTextField(
                value = confirmNewPassword,
                onValueChange = { confirmNewPassword = it; statusMessage = null },
                label = { Text(if (isAr) "تأكيد كلمة المرور الجديدة" else "Confirm New Password") },
                singleLine = true,
                visualTransformation = if (passwordVisible) VisualTransformation.None else PasswordVisualTransformation(),
                modifier = Modifier.fillMaxWidth().testTag("reset_confirm_password_input"),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = AvaPurplePrimary,
                    unfocusedBorderColor = AvaCardBorder,
                    focusedTextColor = Color.White,
                    unfocusedTextColor = Color.White
                )
            )
            Spacer(modifier = Modifier.height(14.dp))

            if (statusMessage != null) {
                Text(
                    text = statusMessage ?: "",
                    color = if (isError) AvaError else AvaSuccess,
                    fontSize = 12.sp,
                    modifier = Modifier.padding(bottom = 10.dp)
                )
            }

            Button(
                onClick = {
                    if (resetCode.isBlank() || newPassword.isBlank()) {
                        val msg = if (isAr) "يرجى إدخال الرمز وكلمة المرور" else "Please fill all fields"
                        statusMessage = msg
                        isError = true
                        Toast.makeText(context, msg, Toast.LENGTH_SHORT).show()
                        return@Button
                    }
                    if (newPassword.length < 6) {
                        val msg = if (isAr) "كلمة المرور يجب أن لا تقل عن 6 أحرف" else "Min 6 characters required"
                        statusMessage = msg
                        isError = true
                        newPasswordHasError = true
                        Toast.makeText(context, msg, Toast.LENGTH_SHORT).show()
                        return@Button
                    }
                    if (newPassword != confirmNewPassword) {
                        val msg = if (isAr) "كلمتا المرور غير متطابقتين" else "Passwords do not match"
                        statusMessage = msg
                        isError = true
                        Toast.makeText(context, msg, Toast.LENGTH_SHORT).show()
                        return@Button
                    }
                    isSubmitting = true
                    scope.launch {
                        val res = viewModel.repository.resetPasswordWithCode(email, resetCode, newPassword)
                        isSubmitting = false
                        if (res.isSuccess) {
                            viewModel.statusMessage.value = if (isAr) "تم تعيين كلمة المرور الجديدة بنجاح! يمكنك الآن تسجيل الدخول" else "Password reset successful! You can now log in"
                            viewModel.navigateTo(AppScreen.LOGIN)
                        } else {
                            statusMessage = res.exceptionOrNull()?.message ?: "فشل تعيين كلمة المرور"
                            isError = true
                        }
                    }
                },
                enabled = !isSubmitting,
                modifier = Modifier.fillMaxWidth().height(48.dp).testTag("btn_confirm_reset_password"),
                shape = RoundedCornerShape(12.dp),
                colors = ButtonDefaults.buttonColors(containerColor = AvaPurplePrimary)
            ) {
                if (isSubmitting) {
                    CircularProgressIndicator(modifier = Modifier.size(20.dp), color = Color.White, strokeWidth = 2.dp)
                } else {
                    Text(text = if (isAr) "حفظ كلمة المرور الجديدة" else "Save New Password", color = Color.White, fontWeight = FontWeight.Bold)
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        TextButton(onClick = { viewModel.navigateTo(AppScreen.LOGIN) }) {
            Text(text = if (isAr) "العودة لتسجيل الدخول" else "Back to Sign In", color = AvaPurpleLight)
        }
    }
}

@Composable
fun SocialAuthSection(
    onGoogleClick: () -> Unit,
    onAppleClick: () -> Unit,
    isAr: Boolean,
    isLoading: Boolean,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .weight(1f)
                    .height(1.dp)
                    .background(AvaDivider)
            )
            Text(
                text = if (isAr) "أو المتابعة عبر" else "Or continue with",
                color = AvaTextSecondary,
                fontSize = 12.sp,
                fontWeight = FontWeight.Medium,
                modifier = Modifier.padding(horizontal = 12.dp)
            )
            Box(
                modifier = Modifier
                    .weight(1f)
                    .height(1.dp)
                    .background(AvaDivider)
            )
        }

        // Google Sign-In Button
        OutlinedButton(
            onClick = onGoogleClick,
            enabled = !isLoading,
            modifier = Modifier
                .fillMaxWidth()
                .height(48.dp)
                .testTag("btn_social_google"),
            shape = RoundedCornerShape(12.dp),
            colors = ButtonDefaults.outlinedButtonColors(
                containerColor = Color(0xFF1E1B29),
                contentColor = Color.White
            ),
            border = BorderStroke(1.dp, AvaCardBorder)
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center
            ) {
                Box(
                    modifier = Modifier
                        .size(24.dp)
                        .clip(CircleShape)
                        .background(Color.White),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "G",
                        color = Color(0xFF4285F4),
                        fontWeight = FontWeight.Black,
                        fontSize = 15.sp
                    )
                }
                Spacer(modifier = Modifier.width(12.dp))
                Text(
                    text = if (isAr) "المتابعة باستخدام Google" else "Continue with Google",
                    fontSize = 14.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = Color.White
                )
            }
        }

        Spacer(modifier = Modifier.height(10.dp))

        // Apple Sign-In Button
        Button(
            onClick = onAppleClick,
            enabled = !isLoading,
            modifier = Modifier
                .fillMaxWidth()
                .height(48.dp)
                .testTag("btn_social_apple"),
            shape = RoundedCornerShape(12.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = Color(0xFF0F0E17),
                contentColor = Color.White
            ),
            border = BorderStroke(1.dp, Color(0xFF2E2B3D))
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center
            ) {
                Canvas(modifier = Modifier.size(20.dp)) {
                    val w = size.width
                    val h = size.height
                    val path = androidx.compose.ui.graphics.Path().apply {
                        moveTo(w * 0.5f, h * 0.28f)
                        cubicTo(w * 0.38f, h * 0.28f, w * 0.28f, h * 0.38f, w * 0.28f, h * 0.52f)
                        cubicTo(w * 0.28f, h * 0.72f, w * 0.42f, h * 0.95f, w * 0.5f, h * 0.95f)
                        cubicTo(w * 0.58f, h * 0.95f, w * 0.72f, h * 0.72f, w * 0.72f, h * 0.52f)
                        cubicTo(w * 0.72f, h * 0.38f, w * 0.62f, h * 0.28f, w * 0.5f, h * 0.28f)
                        close()
                    }
                    drawPath(path, Color.White)
                    val leaf = androidx.compose.ui.graphics.Path().apply {
                        moveTo(w * 0.52f, h * 0.12f)
                        cubicTo(w * 0.62f, h * 0.12f, w * 0.64f, h * 0.22f, w * 0.64f, h * 0.22f)
                        cubicTo(w * 0.54f, h * 0.22f, w * 0.52f, h * 0.12f, w * 0.52f, h * 0.12f)
                        close()
                    }
                    drawPath(leaf, Color.White)
                }
                Spacer(modifier = Modifier.width(12.dp))
                Text(
                    text = if (isAr) "المتابعة باستخدام Apple" else "Continue with Apple",
                    fontSize = 14.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = Color.White
                )
            }
        }
    }
}

