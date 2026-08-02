package com.example.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Movie
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.MissionViewModel
import com.example.ui.components.GlassCard
import com.example.ui.theme.AccentGreen
import com.example.ui.theme.AccentRed
import com.example.ui.theme.GoldPrimary
import com.example.ui.theme.TextMuted
import java.text.NumberFormat
import java.util.Locale

@Composable
fun ContentScreen(viewModel: MissionViewModel) {
    val clips by viewModel.allContentClips.collectAsState()
    var showAddDialog by remember { mutableStateOf(false) }

    val channels = listOf("Boxabl", "Cantina", "Trailblazers", "Islamic")
    val format = NumberFormat.getCurrencyInstance(Locale("en", "IN")).apply { maximumFractionDigits = 0 }

    // Summary calculations
    val totalUploaded = clips.size
    val totalAccepted = clips.count { it.status == "Accepted" }
    val totalRejected = clips.count { it.status == "Rejected" }
    val totalViews = clips.sumOf { it.views }
    val totalRevenue = clips.sumOf { it.revenue }

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp),
        contentPadding = PaddingValues(top = 16.dp, bottom = 100.dp)
    ) {
        item {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column {
                    Text(
                        text = "CONTENT TRACKER",
                        style = MaterialTheme.typography.labelSmall.copy(
                            letterSpacing = 2.sp,
                            fontWeight = FontWeight.Bold
                        ),
                        color = MaterialTheme.colorScheme.primary
                    )
                    Text(
                        text = "Daily Clips & Media",
                        style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold),
                        color = MaterialTheme.colorScheme.onSurface
                    )
                }

                Button(
                    onClick = { showAddDialog = true },
                    shape = RoundedCornerShape(12.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = MaterialTheme.colorScheme.primary,
                        contentColor = Color.Black
                    )
                ) {
                    Icon(imageVector = Icons.Default.Add, contentDescription = "Add Clip")
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(text = "Log Clip", fontWeight = FontWeight.Bold)
                }
            }
        }

        // Overview Stats Grid
        item {
            GlassCard(modifier = Modifier.fillMaxWidth()) {
                Text(
                    text = "Overall Content Metrics",
                    style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                    color = MaterialTheme.colorScheme.onSurface
                )
                Spacer(modifier = Modifier.height(12.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceAround
                ) {
                    MetricItem(label = "Total Clips", value = "$totalUploaded", color = MaterialTheme.colorScheme.onSurface)
                    MetricItem(label = "Accepted", value = "$totalAccepted", color = AccentGreen)
                    MetricItem(label = "Rejected", value = "$totalRejected", color = AccentRed)
                }

                Spacer(modifier = Modifier.height(12.dp))
                HorizontalDivider(color = Color(0x22FFFFFF))
                Spacer(modifier = Modifier.height(12.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceAround
                ) {
                    MetricItem(label = "Total Views", value = formatCompactNumber(totalViews), color = GoldPrimary)
                    MetricItem(label = "Clip Revenue", value = format.format(totalRevenue), color = AccentGreen)
                }
            }
        }

        // Channels Break-down Cards
        item {
            Text(
                text = "Channel Performance",
                style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                color = MaterialTheme.colorScheme.onSurface
            )
        }

        items(channels) { channelName ->
            val channelClips = clips.filter { it.channel == channelName }
            val cUploaded = channelClips.size
            val cAccepted = channelClips.count { it.status == "Accepted" }
            val cViews = channelClips.sumOf { it.views }
            val cRev = channelClips.sumOf { it.revenue }

            GlassCard(modifier = Modifier.fillMaxWidth()) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Surface(
                            shape = RoundedCornerShape(10.dp),
                            color = MaterialTheme.colorScheme.primary.copy(alpha = 0.15f),
                            modifier = Modifier.size(36.dp)
                        ) {
                            Box(contentAlignment = Alignment.Center) {
                                Icon(
                                    imageVector = Icons.Default.Movie,
                                    contentDescription = channelName,
                                    tint = MaterialTheme.colorScheme.primary
                                )
                            }
                        }
                        Spacer(modifier = Modifier.width(12.dp))
                        Text(
                            text = channelName,
                            style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                            color = MaterialTheme.colorScheme.onSurface
                        )
                    }

                    Text(
                        text = "$cUploaded Clips ($cAccepted Approved)",
                        style = MaterialTheme.typography.bodySmall,
                        color = TextMuted
                    )
                }

                Spacer(modifier = Modifier.height(8.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(
                        text = "Views: ${formatCompactNumber(cViews)}",
                        style = MaterialTheme.typography.labelSmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                    Text(
                        text = "Revenue: ${format.format(cRev)}",
                        style = MaterialTheme.typography.labelSmall,
                        color = AccentGreen
                    )
                }
            }
        }

        // Clip Upload History
        item {
            Text(
                text = "Recent Clips History",
                style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                color = MaterialTheme.colorScheme.onSurface,
                modifier = Modifier.padding(top = 8.dp)
            )
        }

        if (clips.isEmpty()) {
            item {
                GlassCard(modifier = Modifier.fillMaxWidth()) {
                    Box(
                        modifier = Modifier.fillMaxWidth().padding(24.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = "No clips logged yet. Start logging your posted content to track performance!",
                            style = MaterialTheme.typography.bodyMedium,
                            color = TextMuted
                        )
                    }
                }
            }
        } else {
            items(clips) { clip ->
                GlassCard(modifier = Modifier.fillMaxWidth()) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Text(
                                    text = clip.channel,
                                    style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                                    color = MaterialTheme.colorScheme.onSurface
                                )
                                Spacer(modifier = Modifier.width(8.dp))
                                StatusBadge(status = clip.status)
                            }
                            Spacer(modifier = Modifier.height(2.dp))
                            Text(
                                text = (if (clip.clipTitle.isNotEmpty()) "${clip.clipTitle} • " else "") + clip.dateString,
                                style = MaterialTheme.typography.bodySmall,
                                color = TextMuted
                            )
                        }

                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Column(horizontalAlignment = Alignment.End) {
                                Text(
                                    text = "${formatCompactNumber(clip.views)} views",
                                    style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.Bold),
                                    color = GoldPrimary
                                )
                                if (clip.revenue > 0) {
                                    Text(
                                        text = format.format(clip.revenue),
                                        style = MaterialTheme.typography.bodySmall,
                                        color = AccentGreen
                                    )
                                }
                            }
                            IconButton(onClick = { viewModel.deleteContentClip(clip.id) }) {
                                Icon(
                                    imageVector = Icons.Default.Delete,
                                    contentDescription = "Delete",
                                    tint = TextMuted,
                                    modifier = Modifier.size(18.dp)
                                )
                            }
                        }
                    }
                }
            }
        }
    }

    if (showAddDialog) {
        AddClipDialog(
            channels = channels,
            onDismiss = { showAddDialog = false },
            onSave = { channel, title, status, views, revenue ->
                viewModel.addContentClip(channel, title, status, views, revenue)
                showAddDialog = false
            }
        )
    }
}

@Composable
private fun StatusBadge(status: String) {
    val color = when (status) {
        "Accepted" -> AccentGreen
        "Rejected" -> AccentRed
        else -> GoldPrimary
    }
    Surface(
        shape = RoundedCornerShape(6.dp),
        color = color.copy(alpha = 0.2f),
        border = androidx.compose.foundation.BorderStroke(0.8.dp, color)
    ) {
        Text(
            text = status,
            style = MaterialTheme.typography.labelSmall.copy(fontSize = 10.sp, fontWeight = FontWeight.Bold),
            color = color,
            modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
        )
    }
}

@Composable
private fun MetricItem(label: String, value: String, color: Color) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Text(text = label, style = MaterialTheme.typography.labelSmall, color = TextMuted)
        Text(
            text = value,
            style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
            color = color
        )
    }
}

private fun formatCompactNumber(number: Long): String {
    return when {
        number >= 1_000_000 -> String.format(Locale.getDefault(), "%.1fM", number / 1_000_000.0)
        number >= 1_000 -> String.format(Locale.getDefault(), "%.1fK", number / 1_000.0)
        else -> number.toString()
    }
}

@Composable
private fun AddClipDialog(
    channels: List<String>,
    onDismiss: () -> Unit,
    onSave: (channel: String, title: String, status: String, views: Long, revenue: Double) -> Unit
) {
    var selectedChannel by remember { mutableStateOf(channels[0]) }
    var titleText by remember { mutableStateOf("") }
    val statuses = listOf("Uploaded", "Accepted", "Rejected")
    var selectedStatus by remember { mutableStateOf(statuses[0]) }
    var viewsText by remember { mutableStateOf("") }
    var revenueText by remember { mutableStateOf("") }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Log Content Clip", fontWeight = FontWeight.Bold) },
        text = {
            Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                Text(text = "Channel", style = MaterialTheme.typography.labelMedium, color = TextMuted)
                Row(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                    channels.forEach { ch ->
                        FilterChip(
                            selected = (selectedChannel == ch),
                            onClick = { selectedChannel = ch },
                            label = { Text(ch, fontSize = 11.sp) }
                        )
                    }
                }

                OutlinedTextField(
                    value = titleText,
                    onValueChange = { titleText = it },
                    label = { Text("Clip Title / Topic") },
                    modifier = Modifier.fillMaxWidth(),
                    singleLine = true
                )

                Text(text = "Status", style = MaterialTheme.typography.labelMedium, color = TextMuted)
                Row(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                    statuses.forEach { st ->
                        FilterChip(
                            selected = (selectedStatus == st),
                            onClick = { selectedStatus = st },
                            label = { Text(st, fontSize = 11.sp) }
                        )
                    }
                }

                OutlinedTextField(
                    value = viewsText,
                    onValueChange = { viewsText = it },
                    label = { Text("Views Count") },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                    modifier = Modifier.fillMaxWidth(),
                    singleLine = true
                )

                OutlinedTextField(
                    value = revenueText,
                    onValueChange = { revenueText = it },
                    label = { Text("Revenue (₹)") },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                    modifier = Modifier.fillMaxWidth(),
                    singleLine = true
                )
            }
        },
        confirmButton = {
            Button(
                onClick = {
                    val views = viewsText.toLongOrNull() ?: 0L
                    val rev = revenueText.toDoubleOrNull() ?: 0.0
                    onSave(selectedChannel, titleText, selectedStatus, views, rev)
                },
                colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primary, contentColor = Color.Black)
            ) {
                Text("Save Clip", fontWeight = FontWeight.Bold)
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) { Text("Cancel", color = TextMuted) }
        },
        containerColor = Color(0xFF141620)
    )
}
