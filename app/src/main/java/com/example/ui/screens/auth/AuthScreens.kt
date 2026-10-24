package com.example.ui.screens.auth

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
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Checkbox
import androidx.compose.material3.CheckboxDefaults
import androidx.compose.material3.Divider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
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
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.components.BrandSignatureBadge
import com.example.ui.theme.AvaBlack
import com.example.ui.theme.AvaCard
import com.example.ui.theme.AvaCardBorder
import com.example.ui.theme.AvaDivider
import com.example.ui.theme.AvaError
import com.example.ui.theme.AvaPurpleContainer
import com.example.ui.theme.AvaPurpleLight
import com.example.ui.theme.AvaPurplePrimary
import com.example.ui.theme.AvaSuccess
import com.example.ui.theme.AvaTextPrimary
import com.example.ui.theme.AvaTextSecondary
import com.example.ui.viewmodel.AppScreen
import com.example.ui.viewmodel.MainViewModel
import com.example.util.stringRes
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@Composable
fun RegisterScreen(
    viewModel: MainViewModel,
    modifier: Modifier = Modifier
) {
    var displayName by remember { mutableStateOf("") }
    var username by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var confirmPassword by remember { mutableStateOf("") }
    var passwordVisible by remember { mutableStateOf(false) }
    var acceptedTerms by remember { mutableStateOf(false) }

    var errorMessage by remember { mutableStateOf<String?>(null) }
    var isSubmitting by remember { mutableStateOf(false) }

    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .background(AvaBlack)
            .padding(horizontal = 24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        item {
            Spacer(modifier = Modifier.height(24.dp))
            // Logo
            Box(
                modifier = Modifier
                    .size(56.dp)
                    .clip(RoundedCornerShape(16.dp))
                    .background(Brush.linearGradient(listOf(AvaPurplePrimary, Color(0xFF4C1D95)))),
                contentAlignment = Alignment.Center
            ) {
                Text(text = "AVA", color = Color.White, fontSize = 20.sp, fontWeight = FontWeight.Black)
            }
            Spacer(modifier = Modifier.height(12.dp))
            Text(
                text = stringRes("app_name"),
                color = AvaTextPrimary,
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold
            )
            BrandSignatureBadge(modifier = Modifier.padding(top = 4.dp))
            Spacer(modifier = Modifier.height(20.dp))
        }

        // Form Fields
        item {
            OutlinedTextField(
                value = displayName,
                onValueChange = { displayName = it; errorMessage = null },
                label = { Text(stringRes("display_name")) },
                singleLine = true,
                leadingIcon = { Icon(Icons.Default.Person, contentDescription = null, tint = AvaTextSecondary) },
                modifier = Modifier.fillMaxWidth().testTag("reg_display_name"),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = AvaPurplePrimary,
                    unfocusedBorderColor = AvaCardBorder,
                    focusedTextColor = Color.White
                )
            )
            Spacer(modifier = Modifier.height(10.dp))

            OutlinedTextField(
                value = username,
                onValueChange = { username = it; errorMessage = null },
                label = { Text(stringRes("username")) },
                singleLine = true,
                leadingIcon = { Text("@", color = AvaTextSecondary, modifier = Modifier.padding(start = 12.dp)) },
                modifier = Modifier.fillMaxWidth().testTag("reg_username"),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = AvaPurplePrimary,
                    unfocusedBorderColor = AvaCardBorder,
                    focusedTextColor = Color.White
                )
            )
            Spacer(modifier = Modifier.height(10.dp))

            OutlinedTextField(
                value = email,
                onValueChange = { email = it; errorMessage = null },
                label = { Text(stringRes("email")) },
                singleLine = true,
                leadingIcon = { Icon(Icons.Default.Email, contentDescription = null, tint = AvaTextSecondary) },
                modifier = Modifier.fillMaxWidth().testTag("reg_email"),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = AvaPurplePrimary,
                    unfocusedBorderColor = AvaCardBorder,
                    focusedTextColor = Color.White
                )
            )
            Spacer(modifier = Modifier.height(10.dp))

            OutlinedTextField(
                value = password,
                onValueChange = { password = it; errorMessage = null },
                label = { Text(stringRes("password")) },
                singleLine = true,
                visualTransformation = if (passwordVisible) VisualTransformation.None else PasswordVisualTransformation(),
                leadingIcon = { Icon(Icons.Default.Lock, contentDescription = null, tint = AvaTextSecondary) },
                trailingIcon = {
                    IconButton(onClick = { passwordVisible = !passwordVisible }) {
                        Icon(
                            imageVector = if (passwordVisible) Icons.Default.Visibility else Icons.Default.VisibilityOff,
                            contentDescription = null,
                            tint = AvaTextSecondary
                        )
                    }
                },
                modifier = Modifier.fillMaxWidth().testTag("reg_password"),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = AvaPurplePrimary,
                    unfocusedBorderColor = AvaCardBorder,
                    focusedTextColor = Color.White
                )
            )
            Spacer(modifier = Modifier.height(10.dp))

            OutlinedTextField(
                value = confirmPassword,
                onValueChange = { confirmPassword = it; errorMessage = null },
                label = { Text(stringRes("confirm_password")) },
                singleLine = true,
                visualTransformation = if (passwordVisible) VisualTransformation.None else PasswordVisualTransformation(),
                leadingIcon = { Icon(Icons.Default.Lock, contentDescription = null, tint = AvaTextSecondary) },
                modifier = Modifier.fillMaxWidth().testTag("reg_confirm_password"),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = AvaPurplePrimary,
                    unfocusedBorderColor = AvaCardBorder,
                    focusedTextColor = Color.White
                )
            )

            // Terms Checkbox
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
                    text = stringRes("accept_terms"),
                    color = AvaTextSecondary,
                    fontSize = 12.sp
                )
            }

            // Error Display
            if (errorMessage != null) {
                Text(
                    text = errorMessage ?: "",
                    color = AvaError,
                    fontSize = 12.sp,
                    modifier = Modifier.padding(bottom = 8.dp)
                )
            }

            // Sign Up Button
            Button(
                onClick = {
                    // Validations
                    if (displayName.isBlank() || username.isBlank() || email.isBlank() || password.isBlank()) {
                        errorMessage = "Please fill in all fields"
                        return@Button
                    }
                    if (!android.util.Patterns.EMAIL_ADDRESS.matcher(email.trim()).matches()) {
                        errorMessage = "Please enter a valid email address"
                        return@Button
                    }
                    if (password.length < 6) {
                        errorMessage = "Password must be at least 6 characters"
                        return@Button
                    }
                    if (password != confirmPassword) {
                        errorMessage = "Passwords do not match"
                        return@Button
                    }
                    if (!acceptedTerms) {
                        errorMessage = "Please accept the terms and privacy policy"
                        return@Button
                    }

                    isSubmitting = true
                    CoroutineScope(Dispatchers.Main).launch {
                        val result = viewModel.repository.register(
                            displayName = displayName,
                            username = username,
                            email = email,
                            password = password
                        )
                        isSubmitting = false
                        if (result.isSuccess) {
                            viewModel.navigateToBottomDestination(AppScreen.HOME)
                        } else {
                            errorMessage = result.exceptionOrNull()?.message ?: "Registration failed"
                        }
                    }
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(48.dp)
                    .testTag("btn_register_submit"),
                shape = RoundedCornerShape(10.dp),
                colors = ButtonDefaults.buttonColors(containerColor = AvaPurplePrimary)
            ) {
                Text(text = stringRes("sign_up"), color = Color.White, fontWeight = FontWeight.Bold, fontSize = 15.sp)
            }

            Spacer(modifier = Modifier.height(14.dp))

            // Divider
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Box(modifier = Modifier.weight(1f).height(1.dp).background(AvaDivider))
                Text(
                    text = stringRes("or_divider"),
                    color = AvaTextSecondary,
                    fontSize = 12.sp,
                    modifier = Modifier.padding(horizontal = 12.dp)
                )
                Box(modifier = Modifier.weight(1f).height(1.dp).background(AvaDivider))
            }

            Spacer(modifier = Modifier.height(14.dp))

            // Google Sign In Button
            OutlinedButton(
                onClick = {
                    // Sign in with Google simulation/auth integration
                    viewModel.statusMessage.value = "Google Authentication active"
                    viewModel.navigateToBottomDestination(AppScreen.HOME)
                },
                modifier = Modifier.fillMaxWidth().height(48.dp),
                shape = RoundedCornerShape(10.dp),
                colors = ButtonDefaults.outlinedButtonColors(contentColor = AvaTextPrimary)
            ) {
                Text(text = stringRes("google_sign_in"), fontWeight = FontWeight.Medium)
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Switch to Login
            TextButton(onClick = { viewModel.navigateTo(AppScreen.LOGIN) }) {
                Text(
                    text = stringRes("already_have_account"),
                    color = AvaPurpleLight,
                    fontSize = 13.sp
                )
            }

            Spacer(modifier = Modifier.height(24.dp))
        }
    }
}

@Composable
fun LoginScreen(
    viewModel: MainViewModel,
    modifier: Modifier = Modifier
) {
    var identifier by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var passwordVisible by remember { mutableStateOf(false) }
    var errorMessage by remember { mutableStateOf<String?>(null) }
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
                .size(56.dp)
                .clip(RoundedCornerShape(16.dp))
                .background(Brush.linearGradient(listOf(AvaPurplePrimary, Color(0xFF4C1D95)))),
            contentAlignment = Alignment.Center
        ) {
            Text(text = "AVA", color = Color.White, fontSize = 20.sp, fontWeight = FontWeight.Black)
        }
        Spacer(modifier = Modifier.height(12.dp))
        Text(
            text = stringRes("app_name"),
            color = AvaTextPrimary,
            fontSize = 24.sp,
            fontWeight = FontWeight.Bold
        )
        BrandSignatureBadge(modifier = Modifier.padding(top = 4.dp))

        Spacer(modifier = Modifier.height(28.dp))

        OutlinedTextField(
            value = identifier,
            onValueChange = { identifier = it; errorMessage = null },
            label = { Text("Email or @username") },
            singleLine = true,
            leadingIcon = { Icon(Icons.Default.Person, contentDescription = null, tint = AvaTextSecondary) },
            modifier = Modifier.fillMaxWidth().testTag("login_identifier"),
            colors = OutlinedTextFieldDefaults.colors(
                focusedBorderColor = AvaPurplePrimary,
                unfocusedBorderColor = AvaCardBorder,
                focusedTextColor = Color.White
            )
        )
        Spacer(modifier = Modifier.height(12.dp))

        OutlinedTextField(
            value = password,
            onValueChange = { password = it; errorMessage = null },
            label = { Text(stringRes("password")) },
            singleLine = true,
            visualTransformation = if (passwordVisible) VisualTransformation.None else PasswordVisualTransformation(),
            leadingIcon = { Icon(Icons.Default.Lock, contentDescription = null, tint = AvaTextSecondary) },
            trailingIcon = {
                IconButton(onClick = { passwordVisible = !passwordVisible }) {
                    Icon(
                        imageVector = if (passwordVisible) Icons.Default.Visibility else Icons.Default.VisibilityOff,
                        contentDescription = null,
                        tint = AvaTextSecondary
                    )
                }
            },
            modifier = Modifier.fillMaxWidth().testTag("login_password"),
            colors = OutlinedTextFieldDefaults.colors(
                focusedBorderColor = AvaPurplePrimary,
                unfocusedBorderColor = AvaCardBorder,
                focusedTextColor = Color.White
            )
        )

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.End
        ) {
            TextButton(onClick = { viewModel.navigateTo(AppScreen.FORGOT_PASSWORD) }) {
                Text(text = stringRes("forgot_password"), color = AvaPurpleLight, fontSize = 12.sp)
            }
        }

        if (errorMessage != null) {
            Text(
                text = errorMessage ?: "",
                color = AvaError,
                fontSize = 12.sp,
                modifier = Modifier.padding(bottom = 8.dp)
            )
        }

        Button(
            onClick = {
                if (identifier.isBlank() || password.isBlank()) {
                    errorMessage = "Please enter email/username and password"
                    return@Button
                }
                isSubmitting = true
                CoroutineScope(Dispatchers.Main).launch {
                    val result = viewModel.repository.login(identifier, password)
                    isSubmitting = false
                    if (result.isSuccess) {
                        viewModel.navigateToBottomDestination(AppScreen.HOME)
                    } else {
                        errorMessage = result.exceptionOrNull()?.message ?: "Sign in failed"
                    }
                }
            },
            modifier = Modifier
                .fillMaxWidth()
                .height(48.dp)
                .testTag("btn_login_submit"),
            shape = RoundedCornerShape(10.dp),
            colors = ButtonDefaults.buttonColors(containerColor = AvaPurplePrimary)
        ) {
            Text(text = stringRes("sign_in"), color = Color.White, fontWeight = FontWeight.Bold, fontSize = 15.sp)
        }

        Spacer(modifier = Modifier.height(16.dp))

        TextButton(onClick = { viewModel.navigateTo(AppScreen.REGISTER) }) {
            Text(
                text = stringRes("dont_have_account"),
                color = AvaPurpleLight,
                fontSize = 13.sp
            )
        }
    }
}

@Composable
fun ForgotPasswordScreen(
    viewModel: MainViewModel,
    modifier: Modifier = Modifier
) {
    var email by remember { mutableStateOf("") }
    var sentMessage by remember { mutableStateOf<String?>(null) }

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(AvaBlack)
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            text = stringRes("reset_password"),
            color = AvaTextPrimary,
            fontSize = 22.sp,
            fontWeight = FontWeight.Bold
        )
        Spacer(modifier = Modifier.height(8.dp))
        Text(
            text = "Enter your registered email address to receive password reset instructions.",
            color = AvaTextSecondary,
            fontSize = 13.sp
        )

        Spacer(modifier = Modifier.height(24.dp))

        OutlinedTextField(
            value = email,
            onValueChange = { email = it },
            label = { Text(stringRes("email")) },
            singleLine = true,
            leadingIcon = { Icon(Icons.Default.Email, contentDescription = null, tint = AvaTextSecondary) },
            modifier = Modifier.fillMaxWidth(),
            colors = OutlinedTextFieldDefaults.colors(
                focusedBorderColor = AvaPurplePrimary,
                unfocusedBorderColor = AvaCardBorder,
                focusedTextColor = Color.White
            )
        )

        Spacer(modifier = Modifier.height(16.dp))

        if (sentMessage != null) {
            Text(text = sentMessage ?: "", color = AvaSuccess, fontSize = 13.sp)
            Spacer(modifier = Modifier.height(12.dp))
        }

        Button(
            onClick = {
                if (email.isNotBlank()) {
                    sentMessage = "Password reset instructions sent to $email"
                }
            },
            modifier = Modifier.fillMaxWidth().height(48.dp),
            shape = RoundedCornerShape(10.dp),
            colors = ButtonDefaults.buttonColors(containerColor = AvaPurplePrimary)
        ) {
            Text(text = "Send Reset Link", color = Color.White, fontWeight = FontWeight.Bold)
        }

        Spacer(modifier = Modifier.height(16.dp))

        TextButton(onClick = { viewModel.navigateBack() }) {
            Text(text = stringRes("previous_step"), color = AvaPurpleLight)
        }
    }
}
