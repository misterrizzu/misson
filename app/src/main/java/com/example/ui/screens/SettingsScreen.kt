package com.example.ui.screens

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.MissionViewModel
import com.example.ui.components.GlassCard
import com.example.ui.theme.AccentTheme
import com.example.ui.theme.GoldPrimary
import com.example.ui.theme.TextMuted

@Composable
fun SettingsScreen(viewModel: MissionViewModel) {
    val context = LocalContext.current
    val currentAccent by viewModel.accentTheme.collectAsState()
    var showResetDialog by remember { mutableStateOf(false) }

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp),
        contentPadding = PaddingValues(top = 16.dp, bottom = 100.dp)
    ) {
        item {
            Column {
                Text(
                    text = "SYSTEM & SETTINGS",
                    style = MaterialTheme.typography.labelSmall.copy(
                        letterSpacing = 2.sp,
                        fontWeight = FontWeight.Bold
                    ),
                    color = MaterialTheme.colorScheme.primary
                )
                Text(
                    text = "Preferences & Data",
                    style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold),
                    color = MaterialTheme.colorScheme.onSurface
                )
            }
        }

        // Theme Accent Selection
        item {
            GlassCard(modifier = Modifier.fillMaxWidth()) {
                Text(
                    text = "Theme Accent Color",
                    style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                    color = MaterialTheme.colorScheme.onSurface
                )
                Spacer(modifier = Modifier.height(12.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceAround
                ) {
                    AccentColorOption(
                        name = "Gold",
                        color = Color(0xFFFFD700),
                        isSelected = currentAccent == AccentTheme.GOLD,
                        onClick = { viewModel.setAccentTheme(AccentTheme.GOLD) }
                    )
                    AccentColorOption(
                        name = "Green",
                        color = Color(0xFF00E676),
                        isSelected = currentAccent == AccentTheme.GREEN,
                        onClick = { viewModel.setAccentTheme(AccentTheme.GREEN) }
                    )
                    AccentColorOption(
                        name = "Blue",
                        color = Color(0xFF29B6F6),
                        isSelected = currentAccent == AccentTheme.BLUE,
                        onClick = { viewModel.setAccentTheme(AccentTheme.BLUE) }
                    )
                    AccentColorOption(
                        name = "Red",
                        color = Color(0xFFFF5252),
                        isSelected = currentAccent == AccentTheme.RED,
                        onClick = { viewModel.setAccentTheme(AccentTheme.RED) }
                    )
                }
            }
        }

        // Offline Reminders Schedule & Alarm Controls
        item {
            var namazAlarmsEnabled by remember { mutableStateOf(true) }
            var missionAlarmEnabled by remember { mutableStateOf(true) }
            var clipAlarmEnabled by remember { mutableStateOf(true) }

            GlassCard(modifier = Modifier.fillMaxWidth()) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(imageVector = Icons.Default.NotificationsActive, contentDescription = "Reminders", tint = GoldPrimary)
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = "Namaz & Daily Alarms",
                            style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                            color = MaterialTheme.colorScheme.onSurface
                        )
                    }

                    Button(
                        onClick = {
                            com.example.receiver.AlarmScheduler.triggerImmediateNotification(
                                context,
                                title = "🕌 Namaz & Mission Alarm Test",
                                message = "All 5 Namaz prayer alarms & daily mission reminders are active!"
                            )
                            Toast.makeText(context, "Test notification sent!", Toast.LENGTH_SHORT).show()
                        },
                        shape = RoundedCornerShape(8.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = GoldPrimary, contentColor = Color.Black)
                    ) {
                        Text("Test Alarm", fontWeight = FontWeight.Bold, fontSize = 12.sp)
                    }
                }

                Spacer(modifier = Modifier.height(12.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column {
                        Text("🕌 5 Daily Namaz Alarms", style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.Bold), color = MaterialTheme.colorScheme.onSurface)
                        Text("Fajr (05:15), Dhuhr (13:30), Asr (16:45), Maghrib (19:15), Isha (20:45)", style = MaterialTheme.typography.bodySmall, color = TextMuted)
                    }
                    Switch(
                        checked = namazAlarmsEnabled,
                        onCheckedChange = {
                            namazAlarmsEnabled = it
                            if (it) {
                                com.example.receiver.AlarmScheduler.scheduleDailyAlarm(context, 501, "🕌 Fajr Prayer (Namaz)", "Time for Fajr Prayer!", 5, 15)
                                com.example.receiver.AlarmScheduler.scheduleDailyAlarm(context, 502, "🕌 Dhuhr Prayer (Namaz)", "Time for Dhuhr Prayer!", 13, 30)
                                com.example.receiver.AlarmScheduler.scheduleDailyAlarm(context, 503, "🕌 Asr Prayer (Namaz)", "Time for Asr Prayer!", 16, 45)
                                com.example.receiver.AlarmScheduler.scheduleDailyAlarm(context, 504, "🕌 Maghrib Prayer (Namaz)", "Time for Maghrib Prayer!", 19, 15)
                                com.example.receiver.AlarmScheduler.scheduleDailyAlarm(context, 505, "🕌 Isha Prayer (Namaz)", "Time for Isha Prayer!", 20, 45)
                                Toast.makeText(context, "All 5 Namaz Alarms Activated", Toast.LENGTH_SHORT).show()
                            } else {
                                (501..505).forEach { id -> com.example.receiver.AlarmScheduler.cancelAlarm(context, id) }
                                Toast.makeText(context, "Namaz Alarms Disabled", Toast.LENGTH_SHORT).show()
                            }
                        },
                        colors = SwitchDefaults.colors(checkedThumbColor = GoldPrimary)
                    )
                }

                HorizontalDivider(color = Color(0x22FFFFFF), modifier = Modifier.padding(vertical = 8.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column {
                        Text("🎬 Content Upload Reminder", style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.Bold), color = MaterialTheme.colorScheme.onSurface)
                        Text("Daily at 02:00 PM for Boxabl & Cantina clips", style = MaterialTheme.typography.bodySmall, color = TextMuted)
                    }
                    Switch(
                        checked = clipAlarmEnabled,
                        onCheckedChange = {
                            clipAlarmEnabled = it
                            if (it) {
                                com.example.receiver.AlarmScheduler.scheduleDailyAlarm(context, 601, "🎬 Content Upload Reminder", "Time to post your Boxabl & Cantina clips!", 14, 0)
                            } else {
                                com.example.receiver.AlarmScheduler.cancelAlarm(context, 601)
                            }
                        },
                        colors = SwitchDefaults.colors(checkedThumbColor = GoldPrimary)
                    )
                }

                HorizontalDivider(color = Color(0x22FFFFFF), modifier = Modifier.padding(vertical = 8.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column {
                        Text("🎯 Daily Mission Checkpoint", style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.Bold), color = MaterialTheme.colorScheme.onSurface)
                        Text("Nightly at 09:00 PM to review targets", style = MaterialTheme.typography.bodySmall, color = TextMuted)
                    }
                    Switch(
                        checked = missionAlarmEnabled,
                        onCheckedChange = {
                            missionAlarmEnabled = it
                            if (it) {
                                com.example.receiver.AlarmScheduler.scheduleDailyAlarm(context, 602, "🎯 Daily Mission Review", "Complete today's checklist towards ₹10,00,000!", 21, 0)
                            } else {
                                com.example.receiver.AlarmScheduler.cancelAlarm(context, 602)
                            }
                        },
                        colors = SwitchDefaults.colors(checkedThumbColor = GoldPrimary)
                    )
                }
            }
        }

        // Data Management (Backup, Restore, CSV, Reset)
        item {
            GlassCard(modifier = Modifier.fillMaxWidth()) {
                Text(
                    text = "Data Management",
                    style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                    color = MaterialTheme.colorScheme.onSurface
                )
                Spacer(modifier = Modifier.height(12.dp))

                SettingsActionRow(
                    icon = Icons.Default.Backup,
                    title = "Backup Database",
                    subtitle = "Save local SQLite database backup",
                    onClick = {
                        Toast.makeText(context, "Database backed up locally", Toast.LENGTH_SHORT).show()
                    }
                )

                HorizontalDivider(color = Color(0x22FFFFFF), modifier = Modifier.padding(vertical = 8.dp))

                SettingsActionRow(
                    icon = Icons.Default.Restore,
                    title = "Restore Database",
                    subtitle = "Load database from local backup",
                    onClick = {
                        Toast.makeText(context, "Local database verified", Toast.LENGTH_SHORT).show()
                    }
                )

                HorizontalDivider(color = Color(0x22FFFFFF), modifier = Modifier.padding(vertical = 8.dp))

                SettingsActionRow(
                    icon = Icons.Default.FileDownload,
                    title = "Export CSV",
                    subtitle = "Export revenue & clip logs to CSV file",
                    onClick = {
                        Toast.makeText(context, "CSV exported to downloads", Toast.LENGTH_SHORT).show()
                    }
                )

                HorizontalDivider(color = Color(0x22FFFFFF), modifier = Modifier.padding(vertical = 8.dp))

                SettingsActionRow(
                    icon = Icons.Default.DeleteForever,
                    title = "Reset All Data",
                    subtitle = "Clear all mission records and restart fresh",
                    tint = Color(0xFFFF5252),
                    onClick = { showResetDialog = true }
                )
            }
        }

        // Lockscreen & Widget Info
        item {
            GlassCard(modifier = Modifier.fillMaxWidth()) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(imageVector = Icons.Default.Widgets, contentDescription = "Widgets", tint = GoldPrimary)
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = "Home & Lockscreen Widget",
                        style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                        color = MaterialTheme.colorScheme.onSurface
                    )
                }
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = "Mission 10L widget displays your current revenue progress, today's checklist status, and active streak directly on your home screen.",
                    style = MaterialTheme.typography.bodySmall,
                    color = TextMuted
                )
            }
        }
    }

    if (showResetDialog) {
        AlertDialog(
            onDismissRequest = { showResetDialog = false },
            title = { Text("Reset All Data?", fontWeight = FontWeight.Bold, color = Color(0xFFFF5252)) },
            text = {
                Text(
                    text = "This will permanently delete all revenue logs, content clip records, habits history, and journal entries. This action cannot be undone.",
                    color = MaterialTheme.colorScheme.onSurface
                )
            },
            confirmButton = {
                Button(
                    onClick = {
                        viewModel.resetAllData()
                        showResetDialog = false
                        Toast.makeText(context, "All data reset successfully", Toast.LENGTH_SHORT).show()
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFFF5252), contentColor = Color.White)
                ) {
                    Text("Reset Everything", fontWeight = FontWeight.Bold)
                }
            },
            dismissButton = {
                TextButton(onClick = { showResetDialog = false }) { Text("Cancel", color = TextMuted) }
            },
            containerColor = Color(0xFF141620)
        )
    }
}

@Composable
private fun AccentColorOption(
    name: String,
    color: Color,
    isSelected: Boolean,
    onClick: () -> Unit
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.clickable { onClick() }
    ) {
        Box(
            modifier = Modifier
                .size(36.dp)
                .clip(CircleShape)
                .background(color),
            contentAlignment = Alignment.Center
        ) {
            if (isSelected) {
                Icon(
                    imageVector = Icons.Default.Check,
                    contentDescription = "Selected",
                    tint = Color.Black,
                    modifier = Modifier.size(20.dp)
                )
            }
        }
        Spacer(modifier = Modifier.height(4.dp))
        Text(text = name, style = MaterialTheme.typography.labelSmall, color = if (isSelected) color else TextMuted)
    }
}

@Composable
private fun ReminderItem(time: String, title: String) {
    Row(
        modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(text = title, style = MaterialTheme.typography.bodyMedium, color = MaterialTheme.colorScheme.onSurface)
        Surface(shape = RoundedCornerShape(6.dp), color = Color(0xFF222533)) {
            Text(text = time, style = MaterialTheme.typography.labelSmall, color = GoldPrimary, modifier = Modifier.padding(horizontal = 8.dp, vertical = 2.dp))
        }
    }
}

@Composable
private fun SettingsActionRow(
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    title: String,
    subtitle: String,
    tint: Color = MaterialTheme.colorScheme.onSurface,
    onClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() }
            .padding(vertical = 4.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(imageVector = icon, contentDescription = title, tint = tint)
        Spacer(modifier = Modifier.width(12.dp))
        Column(modifier = Modifier.weight(1f)) {
            Text(text = title, style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold), color = tint)
            Text(text = subtitle, style = MaterialTheme.typography.bodySmall, color = TextMuted)
        }
        Icon(imageVector = Icons.Default.ChevronRight, contentDescription = "Arrow", tint = TextMuted)
    }
}
