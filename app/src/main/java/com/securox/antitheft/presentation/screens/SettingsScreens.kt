package com.securox.antitheft.presentation.screens

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
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Cloud
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.ExitToApp
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.PrivacyTip
import androidx.compose.material.icons.filled.Security
import androidx.compose.material.icons.filled.Shield
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.securox.antitheft.R
import com.securox.antitheft.core.constants.AppConstants
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
import com.securox.antitheft.core.theme.SecuroxWarning
import com.securox.antitheft.core.theme.SecuroxWhite
import com.securox.antitheft.presentation.components.CyberCard
import com.securox.antitheft.presentation.components.NeonButton
import com.securox.antitheft.presentation.viewmodel.SecuroxViewModel

@Composable
fun AccountSettingsScreen(
    viewModel: SecuroxViewModel,
    onNavigateToPrivacyPolicy: () -> Unit,
    onNavigateToAbout: () -> Unit,
    onSignOut: () -> Unit,
    onBackClick: () -> Unit
) {
    val user by viewModel.currentUser.collectAsState()
    val isCloudMode by viewModel.isCloudMode.collectAsState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(SecuroxBg)
            .padding(16.dp)
            .verticalScroll(rememberScrollState())
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.fillMaxWidth()
        ) {
            IconButton(onClick = onBackClick) {
                Icon(Icons.Default.ArrowBack, contentDescription = "Back", tint = SecuroxWhite)
            }
            Spacer(modifier = Modifier.width(6.dp))
            Text(
                text = "Account & Settings",
                style = MaterialTheme.typography.titleLarge,
                color = SecuroxWhite
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Profile Card
        CyberCard {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Box(
                    modifier = Modifier
                        .size(54.dp)
                        .clip(CircleShape)
                        .background(Color(0x3339FF14))
                        .border(BorderStroke(1.5.dp, SecuroxNeonPrimary), CircleShape),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Default.Person,
                        contentDescription = null,
                        tint = SecuroxNeonPrimary,
                        modifier = Modifier.size(28.dp)
                    )
                }

                Spacer(modifier = Modifier.width(14.dp))

                Column {
                    Text(
                        text = user?.displayName?.ifBlank { "Owner Account" } ?: "Owner Account",
                        style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                        color = SecuroxWhite
                    )
                    Text(
                        text = user?.email ?: "owner@securox.io",
                        style = MaterialTheme.typography.bodyMedium,
                        color = SecuroxNeonSecondary
                    )
                    Text(
                        text = "UID: ${user?.uid ?: "local_vault_user"}",
                        style = MaterialTheme.typography.labelSmall,
                        color = SecuroxTextSecondary
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(20.dp))

        Text(
            text = "SYSTEM ARCHITECTURE & SYNC",
            style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold, fontSize = 13.sp),
            color = SecuroxWhite
        )

        Spacer(modifier = Modifier.height(10.dp))

        // Cloud vs Local Mode Toggle
        CyberCard {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        imageVector = Icons.Default.Cloud,
                        contentDescription = null,
                        tint = if (isCloudMode) SecuroxNeonPrimary else SecuroxMetallicSilver,
                        modifier = Modifier.size(22.dp)
                    )
                    Spacer(modifier = Modifier.width(12.dp))
                    Column {
                        Text(
                            text = "Cloud Backend Synchronization",
                            fontWeight = FontWeight.Bold,
                            color = SecuroxWhite,
                            fontSize = 14.sp
                        )
                        Text(
                            text = if (isCloudMode) "Firebase Cloud Firestore & FCM active" else "Encrypted Local Vault Mode (Demo)",
                            color = SecuroxTextSecondary,
                            fontSize = 11.sp
                        )
                    }
                }

                Switch(
                    checked = isCloudMode,
                    onCheckedChange = { viewModel.toggleCloudMode(it) },
                    colors = SwitchDefaults.colors(
                        checkedThumbColor = SecuroxNeonPrimary,
                        checkedTrackColor = Color(0xFF1B3D24),
                        uncheckedThumbColor = SecuroxMetallicSilver,
                        uncheckedTrackColor = SecuroxSurface
                    )
                )
            }
        }

        Spacer(modifier = Modifier.height(20.dp))

        Text(
            text = "LEGAL & COMPLIANCE",
            style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold, fontSize = 13.sp),
            color = SecuroxWhite
        )

        Spacer(modifier = Modifier.height(10.dp))

        CyberCard(onClick = onNavigateToPrivacyPolicy) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(Icons.Default.PrivacyTip, contentDescription = null, tint = SecuroxNeonPrimary)
                    Spacer(modifier = Modifier.width(12.dp))
                    Text(text = "Privacy & Consent Policy", color = SecuroxWhite, fontWeight = FontWeight.SemiBold)
                }
            }
        }

        Spacer(modifier = Modifier.height(10.dp))

        CyberCard(onClick = onNavigateToAbout) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(Icons.Default.Info, contentDescription = null, tint = SecuroxNeonPrimary)
                    Spacer(modifier = Modifier.width(12.dp))
                    Text(text = "About SECUROX", color = SecuroxWhite, fontWeight = FontWeight.SemiBold)
                }
            }
        }

        Spacer(modifier = Modifier.height(24.dp))

        Button(
            onClick = { viewModel.signOut(onSignOut) },
            modifier = Modifier
                .fillMaxWidth()
                .height(50.dp),
            shape = RoundedCornerShape(12.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = SecuroxCard,
                contentColor = SecuroxWhite
            ),
            border = BorderStroke(1.dp, SecuroxCardBorder)
        ) {
            Icon(Icons.Default.ExitToApp, contentDescription = null)
            Spacer(modifier = Modifier.width(8.dp))
            Text("SIGN OUT OF THIS DEVICE", fontWeight = FontWeight.Bold)
        }
    }
}

@Composable
fun PrivacyPolicyScreen(onBackClick: () -> Unit) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(SecuroxBg)
            .padding(16.dp)
            .verticalScroll(rememberScrollState())
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            IconButton(onClick = onBackClick) {
                Icon(Icons.Default.ArrowBack, contentDescription = "Back", tint = SecuroxWhite)
            }
            Spacer(modifier = Modifier.width(6.dp))
            Text(
                text = "Privacy & Security Policy",
                style = MaterialTheme.typography.titleLarge,
                color = SecuroxWhite
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        CyberCard {
            Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                Text(
                    text = "SECUROX Privacy Guarantee",
                    style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                    color = SecuroxNeonPrimary
                )

                Text(
                    text = "1. Personal Protection Only:\nSECUROX is designed solely to protect the owner's personally owned or officially managed enterprise hardware. It must never be used for covert surveillance or unauthorized tracking of another individual.",
                    color = SecuroxTextPrimary,
                    fontSize = 12.sp,
                    lineHeight = 17.sp
                )

                Text(
                    text = "2. No Spyware or Secret Surveillance:\nSECUROX never records calls, intercepts SMS messages, accesses keystrokes, or activates cameras or microphones remotely. All anti-theft functions are strictly transparent and consent-based.",
                    color = SecuroxTextPrimary,
                    fontSize = 12.sp,
                    lineHeight = 17.sp
                )

                Text(
                    text = "3. Zero Personal Data Deletion:\nSECUROX never implements destructive remote wipe of personal files, documents, contacts, or gallery photos. Your personal memories and data are strictly preserved.",
                    color = SecuroxTextPrimary,
                    fontSize = 12.sp,
                    lineHeight = 17.sp
                )

                Text(
                    text = "4. End-to-End Cryptographic Security:\nSensitive session keys and device telemetry are secured using Android Keystore hardware-backed AES-256-GCM encryption and authenticated Firebase Firestore security rules.",
                    color = SecuroxTextPrimary,
                    fontSize = 12.sp,
                    lineHeight = 17.sp
                )
            }
        }
    }
}

@Composable
fun AboutScreen(onBackClick: () -> Unit) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(SecuroxBg)
            .padding(16.dp)
            .verticalScroll(rememberScrollState()),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.fillMaxWidth()
        ) {
            IconButton(onClick = onBackClick) {
                Icon(Icons.Default.ArrowBack, contentDescription = "Back", tint = SecuroxWhite)
            }
            Spacer(modifier = Modifier.width(6.dp))
            Text(
                text = "About SECUROX",
                style = MaterialTheme.typography.titleLarge,
                color = SecuroxWhite
            )
        }

        Spacer(modifier = Modifier.height(28.dp))

        Box(
            modifier = Modifier
                .size(90.dp)
                .clip(RoundedCornerShape(20.dp))
                .background(SecuroxCard)
                .border(BorderStroke(1.5.dp, SecuroxNeonPrimary), RoundedCornerShape(20.dp)),
            contentAlignment = Alignment.Center
        ) {
            Image(
                painter = painterResource(id = R.drawable.securox_logo),
                contentDescription = null,
                modifier = Modifier.size(76.dp)
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        Text(
            text = AppConstants.APP_FULL_NAME,
            style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
            color = SecuroxWhite
        )
        Text(
            text = "Version ${AppConstants.APP_VERSION} (Build 1) • Debug APK",
            style = MaterialTheme.typography.bodyMedium,
            color = SecuroxNeonSecondary
        )

        Spacer(modifier = Modifier.height(24.dp))

        CyberCard {
            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                DetailItem("UI Framework", "Jetpack Compose + Material 3")
                DetailItem("Architecture", "Clean Architecture + MVVM")
                DetailItem("Hardware Defense", "DevicePolicyManager (DPM)")
                DetailItem("Enterprise Mode", "Android Enterprise Device Owner")
                DetailItem("Location Engine", "Google Fused Location Provider")
                DetailItem("Cryptography", "Android Keystore (AES-GCM-256)")
                DetailItem("Cloud Backend", "Firebase Auth / Firestore / FCM")
            }
        }

        Spacer(modifier = Modifier.height(24.dp))

        Text(
            text = "Your Phone. Our Protection.\nSecure Today. Safe Tomorrow.",
            color = SecuroxMetallicSilver,
            fontSize = 13.sp,
            fontWeight = FontWeight.SemiBold,
            textAlign = androidx.compose.ui.text.style.TextAlign.Center
        )
    }
}
