package com.securox.antitheft.presentation.screens

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
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
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.ErrorOutline
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.Security
import androidx.compose.material.icons.filled.Send
import androidx.compose.material.icons.filled.Smartphone
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
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
import com.securox.antitheft.data.model.SecurityCommand
import com.securox.antitheft.data.model.SecurityEvent
import com.securox.antitheft.data.model.TrustedDevice
import com.securox.antitheft.presentation.components.CyberCard
import com.securox.antitheft.presentation.viewmodel.SecuroxViewModel
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@Composable
fun SecurityAlertsScreen(
    viewModel: SecuroxViewModel,
    onBackClick: () -> Unit
) {
    val events by viewModel.events.collectAsState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(SecuroxBg)
            .padding(16.dp)
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
                text = "Security Alert Logs",
                style = MaterialTheme.typography.titleLarge,
                color = SecuroxWhite
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        if (events.isEmpty()) {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "No security events recorded.",
                    color = SecuroxTextSecondary
                )
            }
        } else {
            LazyColumn(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                items(events) { event ->
                    SecurityEventCard(event = event)
                }
            }
        }
    }
}

@Composable
fun SecurityEventCard(event: SecurityEvent) {
    val (borderColor, iconColor) = when (event.severity) {
        "CRITICAL" -> Pair(SecuroxDanger, SecuroxDanger)
        "WARNING" -> Pair(SecuroxWarning, SecuroxWarning)
        else -> Pair(SecuroxCardBorder, SecuroxNeonPrimary)
    }

    CyberCard(borderColor = borderColor) {
        Column {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Box(
                        modifier = Modifier
                            .size(32.dp)
                            .clip(CircleShape)
                            .background(iconColor.copy(alpha = 0.2f)),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = when (event.severity) {
                                "CRITICAL" -> Icons.Default.Warning
                                "WARNING" -> Icons.Default.ErrorOutline
                                else -> Icons.Default.CheckCircle
                            },
                            contentDescription = null,
                            tint = iconColor,
                            modifier = Modifier.size(18.dp)
                        )
                    }
                    Spacer(modifier = Modifier.width(10.dp))
                    Column {
                        Text(
                            text = event.title,
                            style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold, fontSize = 14.sp),
                            color = SecuroxWhite
                        )
                        Text(
                            text = event.deviceName,
                            style = MaterialTheme.typography.bodyMedium.copy(fontSize = 11.sp),
                            color = SecuroxTextSecondary
                        )
                    }
                }

                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(6.dp))
                        .background(iconColor.copy(alpha = 0.15f))
                        .padding(horizontal = 6.dp, vertical = 2.dp)
                ) {
                    Text(
                        text = event.severity,
                        color = iconColor,
                        fontWeight = FontWeight.Bold,
                        fontSize = 10.sp
                    )
                }
            }

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = event.description,
                style = MaterialTheme.typography.bodyMedium.copy(fontSize = 12.sp),
                color = SecuroxTextPrimary,
                lineHeight = 17.sp
            )

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = SimpleDateFormat("MMM dd, yyyy HH:mm:ss", Locale.getDefault()).format(Date(event.timestamp)),
                style = MaterialTheme.typography.labelSmall,
                color = SecuroxTextSecondary
            )
        }
    }
}

@Composable
fun CommandHistoryScreen(
    viewModel: SecuroxViewModel,
    onBackClick: () -> Unit
) {
    val commands by viewModel.commands.collectAsState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(SecuroxBg)
            .padding(16.dp)
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
                text = "Command Audit Trail",
                style = MaterialTheme.typography.titleLarge,
                color = SecuroxWhite
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        if (commands.isEmpty()) {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                Text(text = "No remote commands issued yet.", color = SecuroxTextSecondary)
            }
        } else {
            LazyColumn(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                items(commands) { cmd ->
                    CommandHistoryCard(command = cmd)
                }
            }
        }
    }
}

@Composable
fun CommandHistoryCard(command: SecurityCommand) {
    val (statusColor, statusLabel) = when (command.status) {
        "EXECUTED" -> Pair(SecuroxNeonPrimary, "EXECUTED")
        "DELIVERED" -> Pair(SecuroxNeonSecondary, "DELIVERED")
        "OFFLINE" -> Pair(SecuroxWarning, "TARGET OFFLINE")
        "FAILED" -> Pair(SecuroxDanger, "FAILED")
        "SENDING" -> Pair(SecuroxWarning, "SENDING")
        else -> Pair(SecuroxMetallicSilver, command.status)
    }

    CyberCard {
        Column {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        imageVector = Icons.Default.Lock,
                        contentDescription = null,
                        tint = SecuroxNeonPrimary,
                        modifier = Modifier.size(18.dp)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = command.commandType,
                        style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold, fontSize = 14.sp),
                        color = SecuroxWhite
                    )
                }

                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(6.dp))
                        .background(statusColor.copy(alpha = 0.2f))
                        .padding(horizontal = 8.dp, vertical = 3.dp)
                ) {
                    Text(
                        text = statusLabel,
                        color = statusColor,
                        fontWeight = FontWeight.Bold,
                        fontSize = 10.sp
                    )
                }
            }

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = command.executionResult.ifBlank { "Awaiting execution report from target device..." },
                style = MaterialTheme.typography.bodyMedium.copy(fontSize = 12.sp),
                color = SecuroxTextPrimary
            )

            Spacer(modifier = Modifier.height(8.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = "ID: ${command.commandId}",
                    style = MaterialTheme.typography.labelSmall,
                    color = SecuroxTextSecondary
                )
                Text(
                    text = SimpleDateFormat("MMM dd, HH:mm", Locale.getDefault()).format(Date(command.createdAt)),
                    style = MaterialTheme.typography.labelSmall,
                    color = SecuroxTextSecondary
                )
            }
        }
    }
}

@Composable
fun TrustedDevicesScreen(
    viewModel: SecuroxViewModel,
    onBackClick: () -> Unit
) {
    val trustedList by viewModel.trustedDevices.collectAsState()
    var showAddDialog by remember { mutableStateOf(false) }
    var newName by remember { mutableStateOf("") }
    var newModel by remember { mutableStateOf("") }
    var newEmail by remember { mutableStateOf("") }

    if (showAddDialog) {
        AlertDialog(
            onDismissRequest = { showAddDialog = false },
            containerColor = SecuroxCard,
            title = {
                Text(text = "Add Trusted Control Device", color = SecuroxWhite, fontWeight = FontWeight.Bold)
            },
            text = {
                Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                    OutlinedTextField(
                        value = newName,
                        onValueChange = { newName = it },
                        label = { Text("Device Name (e.g. Work Laptop)", color = SecuroxTextSecondary) },
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedTextColor = SecuroxWhite,
                            unfocusedTextColor = SecuroxWhite,
                            focusedBorderColor = SecuroxNeonPrimary
                        )
                    )
                    OutlinedTextField(
                        value = newModel,
                        onValueChange = { newModel = it },
                        label = { Text("Model / OS", color = SecuroxTextSecondary) },
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedTextColor = SecuroxWhite,
                            unfocusedTextColor = SecuroxWhite,
                            focusedBorderColor = SecuroxNeonPrimary
                        )
                    )
                    OutlinedTextField(
                        value = newEmail,
                        onValueChange = { newEmail = it },
                        label = { Text("Authorized Owner Email", color = SecuroxTextSecondary) },
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedTextColor = SecuroxWhite,
                            unfocusedTextColor = SecuroxWhite,
                            focusedBorderColor = SecuroxNeonPrimary
                        )
                    )
                }
            },
            confirmButton = {
                Button(
                    onClick = {
                        if (newName.isNotBlank()) {
                            viewModel.addTrustedContactDevice(newName, newModel, newEmail)
                            showAddDialog = false
                            newName = ""
                            newModel = ""
                            newEmail = ""
                        }
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = SecuroxNeonPrimary)
                ) {
                    Text("ADD DEVICE", color = SecuroxBg, fontWeight = FontWeight.Bold)
                }
            },
            dismissButton = {
                TextButton(onClick = { showAddDialog = false }) {
                    Text("CANCEL", color = SecuroxTextSecondary)
                }
            }
        )
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(SecuroxBg)
            .padding(16.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                IconButton(onClick = onBackClick) {
                    Icon(Icons.Default.ArrowBack, contentDescription = "Back", tint = SecuroxWhite)
                }
                Spacer(modifier = Modifier.width(6.dp))
                Text(
                    text = "Trusted Control Devices",
                    style = MaterialTheme.typography.titleLarge,
                    color = SecuroxWhite
                )
            }
            IconButton(onClick = { showAddDialog = true }) {
                Icon(Icons.Default.Add, contentDescription = "Add", tint = SecuroxNeonPrimary)
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        Text(
            text = "These authorized devices and terminals can issue remote emergency commands if your phone is lost or stolen.",
            style = MaterialTheme.typography.bodyMedium,
            color = SecuroxTextSecondary
        )

        Spacer(modifier = Modifier.height(16.dp))

        LazyColumn(verticalArrangement = Arrangement.spacedBy(10.dp)) {
            items(trustedList) { item ->
                CyberCard {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Text(
                                    text = item.deviceName,
                                    style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                                    color = SecuroxWhite
                                )
                                if (item.isPrimary) {
                                    Spacer(modifier = Modifier.width(6.dp))
                                    Text(
                                        text = "(PRIMARY)",
                                        fontSize = 10.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = SecuroxNeonPrimary
                                    )
                                }
                            }
                            Text(
                                text = "${item.model} • ${item.ownerEmail}",
                                style = MaterialTheme.typography.bodyMedium.copy(fontSize = 12.sp),
                                color = SecuroxTextSecondary
                            )
                        }

                        IconButton(onClick = { viewModel.removeTrustedContactDevice(item.id) }) {
                            Icon(Icons.Default.Delete, contentDescription = "Remove", tint = SecuroxDanger)
                        }
                    }
                }
            }
        }
    }
}
