package com.securox.antitheft.presentation.screens

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowForward
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Security
import androidx.compose.material.icons.filled.Shield
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.securox.antitheft.R
import com.securox.antitheft.core.theme.SecuroxBg
import com.securox.antitheft.core.theme.SecuroxCard
import com.securox.antitheft.core.theme.SecuroxCardBorder
import com.securox.antitheft.core.theme.SecuroxDanger
import com.securox.antitheft.core.theme.SecuroxMetallicSilver
import com.securox.antitheft.core.theme.SecuroxNeonPrimary
import com.securox.antitheft.core.theme.SecuroxNeonSecondary
import com.securox.antitheft.core.theme.SecuroxSurface
import com.securox.antitheft.core.theme.SecuroxTextPrimary
import com.securox.antitheft.core.theme.SecuroxTextSecondary
import com.securox.antitheft.core.theme.SecuroxWhite
import com.securox.antitheft.presentation.components.CyberCard
import com.securox.antitheft.presentation.components.NeonButton
import com.securox.antitheft.presentation.viewmodel.SecuroxViewModel
import kotlinx.coroutines.delay

@Composable
fun SplashScreen(
    onSplashFinished: () -> Unit
) {
    val scale = remember { Animatable(0.7f) }
    val infiniteTransition = rememberInfiniteTransition(label = "pulse")
    val glowAlpha by infiniteTransition.animateFloat(
        initialValue = 0.3f,
        targetValue = 0.8f,
        animationSpec = infiniteRepeatable(
            animation = tween(1200, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "glow"
    )

    LaunchedEffect(Unit) {
        scale.animateTo(
            targetValue = 1f,
            animationSpec = tween(800, easing = FastOutSlowInEasing)
        )
        delay(1400)
        onSplashFinished()
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(SecuroxBg),
        contentAlignment = Alignment.Center
    ) {
        // Background radial glow
        Box(
            modifier = Modifier
                .size(320.dp)
                .clip(CircleShape)
                .background(
                    Brush.radialGradient(
                        colors = listOf(
                            SecuroxNeonPrimary.copy(alpha = glowAlpha * 0.4f),
                            Color.Transparent
                        )
                    )
                )
        )

        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center,
            modifier = Modifier.scale(scale.value)
        ) {
            // Main SECUROX Logo
            Box(
                modifier = Modifier
                    .size(130.dp)
                    .clip(RoundedCornerShape(28.dp))
                    .background(SecuroxCard)
                    .border(BorderStroke(2.dp, SecuroxNeonPrimary), RoundedCornerShape(28.dp)),
                contentAlignment = Alignment.Center
            ) {
                Image(
                    painter = painterResource(id = R.drawable.securox_logo),
                    contentDescription = "SECUROX Brand Logo",
                    modifier = Modifier.size(110.dp)
                )
            }

            Spacer(modifier = Modifier.height(24.dp))

            Row(verticalAlignment = Alignment.CenterVertically) {
                Text(
                    text = "SECURO",
                    style = MaterialTheme.typography.displayMedium.copy(
                        fontWeight = FontWeight.Black,
                        letterSpacing = 2.sp
                    ),
                    color = SecuroxWhite
                )
                Text(
                    text = "X",
                    style = MaterialTheme.typography.displayMedium.copy(
                        fontWeight = FontWeight.Black,
                        letterSpacing = 2.sp
                    ),
                    color = SecuroxNeonPrimary
                )
            }

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = "Your Phone. Our Protection.",
                style = MaterialTheme.typography.titleMedium.copy(
                    fontWeight = FontWeight.SemiBold,
                    letterSpacing = 0.5.sp
                ),
                color = SecuroxNeonSecondary
            )

            Spacer(modifier = Modifier.height(4.dp))

            Text(
                text = "Secure Today. Safe Tomorrow.",
                style = MaterialTheme.typography.bodyMedium,
                color = SecuroxTextSecondary
            )

            Spacer(modifier = Modifier.height(36.dp))

            CircularProgressIndicator(
                color = SecuroxNeonPrimary,
                strokeWidth = 2.dp,
                modifier = Modifier.size(24.dp)
            )
        }
    }
}

@Composable
fun OnboardingScreen(
    onFinished: () -> Unit,
    onLoginClick: () -> Unit
) {
    var currentStep by remember { mutableStateOf(0) }

    val steps = listOf(
        Triple(
            "Welcome to SECUROX",
            "Elite Anti-Theft & Hardware Protection",
            "Protect your personal Android device with military-grade remote controls, transparent consent, and real-time security telemetry."
        ),
        Triple(
            "Theft & Loss Protection",
            "Lost or Stolen Device Defense",
            "Use another phone to locate your lost device, trigger immediate remote locking, and detect unauthorized power-offs or SIM swaps."
        ),
        Triple(
            "Remote Emergency Lock",
            "Instant Hardware Lockdown",
            "Trigger real screen locking via Android DevicePolicyManager lockNow(). The lost device executes your authenticated security command instantly."
        ),
        Triple(
            "Device Administrator Mode",
            "Official Android Security API",
            "Enables immediate remote locking and monitors unauthorized failed passcode attempts. Transparent and strictly consent-based."
        ),
        Triple(
            "Enterprise Device Owner",
            "Advanced Factory Reset Defense",
            "Optional Android Enterprise provisioning restricts factory resets, disables safe-boot bypasses, and prevents app uninstallation."
        ),
        Triple(
            "Create Account & Pair",
            "Multi-Device Control Center",
            "Sign in with your SECUROX account to connect your primary phone, secondary tablets, and trusted control devices under one secure dashboard."
        )
    )

    val step = steps[currentStep]

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(SecuroxBg)
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.SpaceBetween
    ) {
        // Top Stepper
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 16.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Row(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                repeat(steps.size) { index ->
                    Box(
                        modifier = Modifier
                            .height(4.dp)
                            .width(if (index == currentStep) 28.dp else 10.dp)
                            .clip(RoundedCornerShape(2.dp))
                            .background(
                                if (index == currentStep) SecuroxNeonPrimary
                                else if (index < currentStep) SecuroxNeonSecondary.copy(alpha = 0.5f)
                                else SecuroxCardBorder
                            )
                    )
                }
            }

            TextButton(onClick = onFinished) {
                Text(
                    text = "SKIP",
                    color = SecuroxTextSecondary,
                    fontWeight = FontWeight.Bold,
                    fontSize = 12.sp
                )
            }
        }

        // Center Content Card
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.padding(vertical = 24.dp)
        ) {
            Box(
                modifier = Modifier
                    .size(90.dp)
                    .clip(CircleShape)
                    .background(Color(0x2239FF14))
                    .border(BorderStroke(2.dp, SecuroxNeonPrimary), CircleShape),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = when (currentStep) {
                        0 -> Icons.Default.Shield
                        1 -> Icons.Default.Security
                        2 -> Icons.Default.Lock
                        3 -> Icons.Default.Shield
                        4 -> Icons.Default.Security
                        else -> Icons.Default.Person
                    },
                    contentDescription = null,
                    tint = SecuroxNeonPrimary,
                    modifier = Modifier.size(44.dp)
                )
            }

            Spacer(modifier = Modifier.height(28.dp))

            Text(
                text = step.first,
                style = MaterialTheme.typography.displaySmall.copy(
                    fontWeight = FontWeight.Black,
                    fontSize = 24.sp
                ),
                color = SecuroxWhite,
                textAlign = TextAlign.Center
            )

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = step.second,
                style = MaterialTheme.typography.titleMedium.copy(
                    fontWeight = FontWeight.Bold
                ),
                color = SecuroxNeonSecondary,
                textAlign = TextAlign.Center
            )

            Spacer(modifier = Modifier.height(16.dp))

            Text(
                text = step.third,
                style = MaterialTheme.typography.bodyLarge,
                color = SecuroxTextSecondary,
                textAlign = TextAlign.Center,
                lineHeight = 22.sp
            )
        }

        // Bottom Actions
        Column(modifier = Modifier.fillMaxWidth()) {
            if (currentStep < steps.size - 1) {
                NeonButton(
                    text = "NEXT",
                    icon = Icons.Default.ArrowForward,
                    onClick = { currentStep++ }
                )
            } else {
                NeonButton(
                    text = "GET STARTED",
                    icon = Icons.Default.Check,
                    onClick = onFinished
                )
            }

            Spacer(modifier = Modifier.height(12.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Already have an account? ",
                    style = MaterialTheme.typography.bodyMedium,
                    color = SecuroxTextSecondary
                )
                Text(
                    text = "Sign In",
                    style = MaterialTheme.typography.bodyMedium.copy(
                        fontWeight = FontWeight.Bold,
                        color = SecuroxNeonPrimary
                    ),
                    modifier = Modifier.clickable { onLoginClick() }
                )
            }
        }
    }
}

@Composable
fun LoginScreen(
    viewModel: SecuroxViewModel,
    onLoginSuccess: () -> Unit,
    onNavigateToRegister: () -> Unit,
    onNavigateToForgotPassword: () -> Unit
) {
    var email by remember { mutableStateOf("owner@securox.io") }
    var password by remember { mutableStateOf("SecuroxMaster2026!") }
    var passwordVisible by remember { mutableStateOf(false) }
    var isLoading by remember { mutableStateOf(false) }
    var errorMessage by remember { mutableStateOf<String?>(null) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(SecuroxBg)
            .padding(24.dp)
            .verticalScroll(rememberScrollState()),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Box(
            modifier = Modifier
                .size(72.dp)
                .clip(RoundedCornerShape(16.dp))
                .background(SecuroxCard)
                .border(BorderStroke(1.5.dp, SecuroxNeonPrimary), RoundedCornerShape(16.dp)),
            contentAlignment = Alignment.Center
        ) {
            Image(
                painter = painterResource(id = R.drawable.securox_logo),
                contentDescription = null,
                modifier = Modifier.size(60.dp)
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        Text(
            text = "Welcome Back",
            style = MaterialTheme.typography.headlineMedium.copy(fontWeight = FontWeight.Bold),
            color = SecuroxWhite
        )
        Text(
            text = "Sign in to control and protect your devices",
            style = MaterialTheme.typography.bodyMedium,
            color = SecuroxTextSecondary
        )

        Spacer(modifier = Modifier.height(28.dp))

        // Email Field
        OutlinedTextField(
            value = email,
            onValueChange = { email = it; errorMessage = null },
            label = { Text("Email Address", color = SecuroxTextSecondary) },
            leadingIcon = {
                Icon(Icons.Default.Email, contentDescription = null, tint = SecuroxNeonPrimary)
            },
            singleLine = true,
            modifier = Modifier.fillMaxWidth(),
            colors = OutlinedTextFieldDefaults.colors(
                focusedBorderColor = SecuroxNeonPrimary,
                unfocusedBorderColor = SecuroxCardBorder,
                focusedTextColor = SecuroxWhite,
                unfocusedTextColor = SecuroxWhite
            ),
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email)
        )

        Spacer(modifier = Modifier.height(16.dp))

        // Password Field
        OutlinedTextField(
            value = password,
            onValueChange = { password = it; errorMessage = null },
            label = { Text("Password", color = SecuroxTextSecondary) },
            leadingIcon = {
                Icon(Icons.Default.Lock, contentDescription = null, tint = SecuroxNeonPrimary)
            },
            trailingIcon = {
                IconButton(onClick = { passwordVisible = !passwordVisible }) {
                    Icon(
                        imageVector = if (passwordVisible) Icons.Default.Visibility else Icons.Default.VisibilityOff,
                        contentDescription = null,
                        tint = SecuroxTextSecondary
                    )
                }
            },
            visualTransformation = if (passwordVisible) VisualTransformation.None else PasswordVisualTransformation(),
            singleLine = true,
            modifier = Modifier.fillMaxWidth(),
            colors = OutlinedTextFieldDefaults.colors(
                focusedBorderColor = SecuroxNeonPrimary,
                unfocusedBorderColor = SecuroxCardBorder,
                focusedTextColor = SecuroxWhite,
                unfocusedTextColor = SecuroxWhite
            )
        )

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.End
        ) {
            TextButton(onClick = onNavigateToForgotPassword) {
                Text(
                    text = "Forgot Password?",
                    color = SecuroxNeonSecondary,
                    fontSize = 12.sp
                )
            }
        }

        if (errorMessage != null) {
            Text(
                text = errorMessage!!,
                color = SecuroxDanger,
                fontSize = 12.sp,
                modifier = Modifier.padding(bottom = 8.dp)
            )
        }

        Spacer(modifier = Modifier.height(8.dp))

        NeonButton(
            text = if (isLoading) "AUTHENTICATING..." else "SIGN IN",
            enabled = !isLoading && email.isNotBlank() && password.isNotBlank(),
            onClick = {
                isLoading = true
                viewModel.authenticate(email, password, isSignUp = false) { success, err ->
                    isLoading = false
                    if (success) {
                        onLoginSuccess()
                    } else {
                        errorMessage = err
                    }
                }
            }
        )

        Spacer(modifier = Modifier.height(16.dp))

        Row(
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text("Don't have an account? ", color = SecuroxTextSecondary, fontSize = 13.sp)
            Text(
                text = "Register Now",
                color = SecuroxNeonPrimary,
                fontWeight = FontWeight.Bold,
                fontSize = 13.sp,
                modifier = Modifier.clickable { onNavigateToRegister() }
            )
        }
    }
}

@Composable
fun RegisterScreen(
    viewModel: SecuroxViewModel,
    onRegisterSuccess: () -> Unit,
    onNavigateToLogin: () -> Unit
) {
    var name by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var confirmPassword by remember { mutableStateOf("") }
    var passwordVisible by remember { mutableStateOf(false) }
    var isLoading by remember { mutableStateOf(false) }
    var errorMessage by remember { mutableStateOf<String?>(null) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(SecuroxBg)
            .padding(24.dp)
            .verticalScroll(rememberScrollState()),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            text = "Create SECUROX Account",
            style = MaterialTheme.typography.headlineMedium.copy(fontWeight = FontWeight.Bold),
            color = SecuroxWhite
        )
        Text(
            text = "Protect all your Android phones under one master shield",
            style = MaterialTheme.typography.bodyMedium,
            color = SecuroxTextSecondary,
            textAlign = TextAlign.Center
        )

        Spacer(modifier = Modifier.height(24.dp))

        OutlinedTextField(
            value = name,
            onValueChange = { name = it; errorMessage = null },
            label = { Text("Full Name", color = SecuroxTextSecondary) },
            leadingIcon = { Icon(Icons.Default.Person, contentDescription = null, tint = SecuroxNeonPrimary) },
            singleLine = true,
            modifier = Modifier.fillMaxWidth(),
            colors = OutlinedTextFieldDefaults.colors(
                focusedBorderColor = SecuroxNeonPrimary,
                unfocusedBorderColor = SecuroxCardBorder,
                focusedTextColor = SecuroxWhite,
                unfocusedTextColor = SecuroxWhite
            )
        )

        Spacer(modifier = Modifier.height(12.dp))

        OutlinedTextField(
            value = email,
            onValueChange = { email = it; errorMessage = null },
            label = { Text("Email Address", color = SecuroxTextSecondary) },
            leadingIcon = { Icon(Icons.Default.Email, contentDescription = null, tint = SecuroxNeonPrimary) },
            singleLine = true,
            modifier = Modifier.fillMaxWidth(),
            colors = OutlinedTextFieldDefaults.colors(
                focusedBorderColor = SecuroxNeonPrimary,
                unfocusedBorderColor = SecuroxCardBorder,
                focusedTextColor = SecuroxWhite,
                unfocusedTextColor = SecuroxWhite
            ),
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email)
        )

        Spacer(modifier = Modifier.height(12.dp))

        OutlinedTextField(
            value = password,
            onValueChange = { password = it; errorMessage = null },
            label = { Text("Password", color = SecuroxTextSecondary) },
            leadingIcon = { Icon(Icons.Default.Lock, contentDescription = null, tint = SecuroxNeonPrimary) },
            visualTransformation = if (passwordVisible) VisualTransformation.None else PasswordVisualTransformation(),
            singleLine = true,
            modifier = Modifier.fillMaxWidth(),
            colors = OutlinedTextFieldDefaults.colors(
                focusedBorderColor = SecuroxNeonPrimary,
                unfocusedBorderColor = SecuroxCardBorder,
                focusedTextColor = SecuroxWhite,
                unfocusedTextColor = SecuroxWhite
            )
        )

        Spacer(modifier = Modifier.height(12.dp))

        OutlinedTextField(
            value = confirmPassword,
            onValueChange = { confirmPassword = it; errorMessage = null },
            label = { Text("Confirm Password", color = SecuroxTextSecondary) },
            leadingIcon = { Icon(Icons.Default.Lock, contentDescription = null, tint = SecuroxNeonPrimary) },
            visualTransformation = if (passwordVisible) VisualTransformation.None else PasswordVisualTransformation(),
            singleLine = true,
            modifier = Modifier.fillMaxWidth(),
            colors = OutlinedTextFieldDefaults.colors(
                focusedBorderColor = SecuroxNeonPrimary,
                unfocusedBorderColor = SecuroxCardBorder,
                focusedTextColor = SecuroxWhite,
                unfocusedTextColor = SecuroxWhite
            )
        )

        if (errorMessage != null) {
            Spacer(modifier = Modifier.height(8.dp))
            Text(text = errorMessage!!, color = SecuroxDanger, fontSize = 12.sp)
        }

        Spacer(modifier = Modifier.height(20.dp))

        NeonButton(
            text = if (isLoading) "CREATING ACCOUNT..." else "REGISTER ACCOUNT",
            enabled = !isLoading && email.isNotBlank() && password.isNotBlank(),
            onClick = {
                if (password != confirmPassword) {
                    errorMessage = "Passwords do not match"
                    return@NeonButton
                }
                isLoading = true
                viewModel.authenticate(email, password, isSignUp = true, name = name) { success, err ->
                    isLoading = false
                    if (success) {
                        onRegisterSuccess()
                    } else {
                        errorMessage = err
                    }
                }
            }
        )

        Spacer(modifier = Modifier.height(16.dp))

        Row(verticalAlignment = Alignment.CenterVertically) {
            Text("Already registered? ", color = SecuroxTextSecondary, fontSize = 13.sp)
            Text(
                text = "Sign In",
                color = SecuroxNeonPrimary,
                fontWeight = FontWeight.Bold,
                fontSize = 13.sp,
                modifier = Modifier.clickable { onNavigateToLogin() }
            )
        }
    }
}

@Composable
fun ForgotPasswordScreen(
    viewModel: SecuroxViewModel,
    onBackClick: () -> Unit
) {
    var email by remember { mutableStateOf("") }
    var resultMessage by remember { mutableStateOf<String?>(null) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(SecuroxBg)
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.SpaceBetween
    ) {
        Column {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.fillMaxWidth()
            ) {
                IconButton(onClick = onBackClick) {
                    Icon(Icons.Default.ArrowBack, contentDescription = "Back", tint = SecuroxWhite)
                }
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = "Recover Password",
                    style = MaterialTheme.typography.titleLarge,
                    color = SecuroxWhite
                )
            }

            Spacer(modifier = Modifier.height(24.dp))

            Text(
                text = "Enter your registered email address. We will transmit an authenticated password reset link to verify your identity.",
                style = MaterialTheme.typography.bodyMedium,
                color = SecuroxTextSecondary,
                lineHeight = 20.sp
            )

            Spacer(modifier = Modifier.height(24.dp))

            OutlinedTextField(
                value = email,
                onValueChange = { email = it; resultMessage = null },
                label = { Text("Account Email", color = SecuroxTextSecondary) },
                leadingIcon = { Icon(Icons.Default.Email, contentDescription = null, tint = SecuroxNeonPrimary) },
                singleLine = true,
                modifier = Modifier.fillMaxWidth(),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = SecuroxNeonPrimary,
                    unfocusedBorderColor = SecuroxCardBorder,
                    focusedTextColor = SecuroxWhite,
                    unfocusedTextColor = SecuroxWhite
                )
            )

            if (resultMessage != null) {
                Spacer(modifier = Modifier.height(12.dp))
                Text(
                    text = resultMessage!!,
                    color = SecuroxNeonPrimary,
                    fontSize = 13.sp,
                    fontWeight = FontWeight.Medium
                )
            }
        }

        NeonButton(
            text = "SEND RECOVERY LINK",
            enabled = email.isNotBlank(),
            onClick = {
                viewModel.resetPassword(email) { success, msg ->
                    resultMessage = msg
                }
            }
        )
    }
}
