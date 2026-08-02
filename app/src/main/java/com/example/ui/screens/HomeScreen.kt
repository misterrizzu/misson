package com.example.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.MissionViewModel
import com.example.ui.components.GlassCard
import com.example.ui.components.LargeProgressRing
import com.example.ui.components.StreakHeatmap
import com.example.ui.theme.AccentGreen
import com.example.ui.theme.TextMuted

@Composable
fun HomeScreen(viewModel: MissionViewModel) {
    val todayMission by viewModel.todayMission.collectAsState()
    val revenueStats by viewModel.revenueStats.collectAsState()
    val levelInfo by viewModel.currentLevelInfo.collectAsState()
    val streakStats by viewModel.streakStats.collectAsState()

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp),
        contentPadding = PaddingValues(top = 16.dp, bottom = 100.dp)
    ) {
        // Top Date & Reminder Header
        item {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column {
                    Text(
                        text = "TODAY'S MISSION",
                        style = MaterialTheme.typography.labelSmall.copy(
                            letterSpacing = 2.sp,
                            fontWeight = FontWeight.Bold
                        ),
                        color = MaterialTheme.colorScheme.primary
                    )
                    Text(
                        text = viewModel.todayFormattedDate,
                        style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                        color = MaterialTheme.colorScheme.onSurface
                    )
                }

                Surface(
                    shape = RoundedCornerShape(20.dp),
                    color = MaterialTheme.colorScheme.primary.copy(alpha = 0.15f),
                    border = androidx.compose.foundation.BorderStroke(1.dp, MaterialTheme.colorScheme.primary)
                ) {
                    Row(
                        modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            imageVector = Icons.Default.Whatshot,
                            contentDescription = "Streak",
                            tint = MaterialTheme.colorScheme.primary,
                            modifier = Modifier.size(18.dp)
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(
                            text = "${streakStats.currentStreak} Days",
                            style = MaterialTheme.typography.labelMedium.copy(fontWeight = FontWeight.Bold),
                            color = MaterialTheme.colorScheme.primary
                        )
                    }
                }
            }
        }

        // Hero Mission Card with Progress Ring
        item {
            GlassCard(modifier = Modifier.fillMaxWidth()) {
                Column(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    LargeProgressRing(
                        currentAmount = revenueStats.lifetime,
                        targetAmount = 1000000.0
                    )

                    Spacer(modifier = Modifier.height(16.dp))
                    HorizontalDivider(color = Color(0x22FFFFFF))
                    Spacer(modifier = Modifier.height(16.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceAround
                    ) {
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            Text(
                                text = "Current Level",
                                style = MaterialTheme.typography.labelSmall,
                                color = TextMuted
                            )
                            Text(
                                text = levelInfo.levelTitle,
                                style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                                color = MaterialTheme.colorScheme.primary
                            )
                            Text(
                                text = "Target: ${levelInfo.monthlyTargetFormatted}",
                                style = MaterialTheme.typography.bodySmall,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }

                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            Text(
                                text = "Est. Remaining",
                                style = MaterialTheme.typography.labelSmall,
                                color = TextMuted
                            )
                            Text(
                                text = "${revenueStats.remainingDaysEstimated} Days",
                                style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                                color = MaterialTheme.colorScheme.onSurface
                            )
                            Text(
                                text = "Keep Executing",
                                style = MaterialTheme.typography.bodySmall,
                                color = AccentGreen
                            )
                        }
                    }
                }
            }
        }

        // Today's Mission Checklist
        item {
            GlassCard(modifier = Modifier.fillMaxWidth()) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "TODAY'S CHECKLIST",
                        style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.ExtraBold),
                        color = MaterialTheme.colorScheme.onSurface
                    )

                    Text(
                        text = "${todayMission.completionPercentage.toInt()}%",
                        style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                        color = if (todayMission.isCompleted) AccentGreen else MaterialTheme.colorScheme.primary
                    )
                }

                Spacer(modifier = Modifier.height(12.dp))

                ChecklistItem(
                    label = "5 Daily Prayers (Namaz)",
                    checked = todayMission.namaz5Prayers,
                    category = "Spiritual",
                    onCheckedChange = { viewModel.updateChecklistField("namaz", it) }
                )
                ChecklistItem(
                    label = "Boxabl Clip Posted",
                    checked = todayMission.boxablClipPosted,
                    category = "Content",
                    onCheckedChange = { viewModel.updateChecklistField("boxabl", it) }
                )
                ChecklistItem(
                    label = "Cantina Clip Posted",
                    checked = todayMission.cantinaClipPosted,
                    category = "Content",
                    onCheckedChange = { viewModel.updateChecklistField("cantina", it) }
                )
                ChecklistItem(
                    label = "Islamic Channel Checked",
                    checked = todayMission.islamicChannelChecked,
                    category = "Business",
                    onCheckedChange = { viewModel.updateChecklistField("islamic", it) }
                )
                ChecklistItem(
                    label = "Analytics Reviewed",
                    checked = todayMission.analyticsReviewed,
                    category = "Business",
                    onCheckedChange = { viewModel.updateChecklistField("analytics", it) }
                )
                ChecklistItem(
                    label = "No New Project Started",
                    checked = todayMission.noNewProjectStarted,
                    category = "Focus",
                    onCheckedChange = { viewModel.updateChecklistField("noNewProject", it) }
                )
                ChecklistItem(
                    label = "Workout",
                    checked = todayMission.workout,
                    category = "Health",
                    onCheckedChange = { viewModel.updateChecklistField("workout", it) }
                )
                ChecklistItem(
                    label = "Read 10 Minutes",
                    checked = todayMission.read10Minutes,
                    category = "Mindset",
                    onCheckedChange = { viewModel.updateChecklistField("read10", it) }
                )
                ChecklistItem(
                    label = "Slept Before Midnight",
                    checked = todayMission.sleptBeforeMidnight,
                    category = "Health",
                    onCheckedChange = { viewModel.updateChecklistField("sleep", it) }
                )

                Spacer(modifier = Modifier.height(16.dp))

                Button(
                    onClick = { viewModel.completeDayMission() },
                    modifier = Modifier.fillMaxWidth().height(48.dp),
                    shape = RoundedCornerShape(12.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = AccentGreen,
                        contentColor = Color.Black
                    )
                ) {
                    Icon(
                        imageVector = Icons.Default.CheckCircle,
                        contentDescription = "Complete",
                        modifier = Modifier.size(20.dp)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = "COMPLETE DAY",
                        style = MaterialTheme.typography.titleMedium.copy(
                            fontWeight = FontWeight.ExtraBold,
                            letterSpacing = 1.sp
                        )
                    )
                }
            }
        }

        // Streak & Calendar Heatmap
        item {
            GlassCard(modifier = Modifier.fillMaxWidth()) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Column {
                        Text(
                            text = "Current Streak",
                            style = MaterialTheme.typography.labelMedium,
                            color = TextMuted
                        )
                        Text(
                            text = "${streakStats.currentStreak} Days",
                            style = MaterialTheme.typography.headlineSmall.copy(fontWeight = FontWeight.Bold),
                            color = MaterialTheme.colorScheme.primary
                        )
                    }

                    Column(horizontalAlignment = Alignment.End) {
                        Text(
                            text = "Longest Streak",
                            style = MaterialTheme.typography.labelMedium,
                            color = TextMuted
                        )
                        Text(
                            text = "${streakStats.longestStreak} Days",
                            style = MaterialTheme.typography.headlineSmall.copy(fontWeight = FontWeight.Bold),
                            color = MaterialTheme.colorScheme.onSurface
                        )
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                StreakHeatmap(heatmapData = streakStats.heatmap)
            }
        }
    }
}

@Composable
private fun ChecklistItem(
    label: String,
    checked: Boolean,
    category: String = "Daily",
    onCheckedChange: (Boolean) -> Unit
) {
    val context = androidx.compose.ui.platform.LocalContext.current

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(8.dp))
            .clickable { onCheckedChange(!checked) }
            .padding(vertical = 6.dp, horizontal = 4.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.weight(1f)
        ) {
            Checkbox(
                checked = checked,
                onCheckedChange = onCheckedChange,
                colors = CheckboxDefaults.colors(
                    checkedColor = MaterialTheme.colorScheme.primary,
                    checkmarkColor = Color.Black
                )
            )
            Spacer(modifier = Modifier.width(8.dp))
            Column {
                Text(
                    text = label,
                    style = MaterialTheme.typography.bodyMedium.copy(
                        fontWeight = if (checked) FontWeight.SemiBold else FontWeight.Normal
                    ),
                    color = if (checked) MaterialTheme.colorScheme.onSurface else TextMuted
                )
                Text(
                    text = category,
                    style = MaterialTheme.typography.labelSmall,
                    color = MaterialTheme.colorScheme.primary.copy(alpha = 0.7f)
                )
            }
        }

        IconButton(
            onClick = {
                com.example.receiver.AlarmScheduler.triggerImmediateNotification(
                    context,
                    title = "Reminder: $label",
                    message = "Time for your daily $label mission!"
                )
            },
            modifier = Modifier.size(32.dp)
        ) {
            Icon(
                imageVector = Icons.Default.NotificationsActive,
                contentDescription = "Test Reminder Alarm",
                tint = MaterialTheme.colorScheme.primary.copy(alpha = 0.8f),
                modifier = Modifier.size(16.dp)
            )
        }
    }
}
