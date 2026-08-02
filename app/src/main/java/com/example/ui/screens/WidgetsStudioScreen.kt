package com.example.ui.screens

import android.widget.Toast
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.hapticfeedback.HapticFeedbackType
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.receiver.AlarmScheduler
import com.example.ui.MissionViewModel
import com.example.ui.components.GlassCard
import com.example.ui.theme.GoldPrimary
import com.example.ui.theme.TextMuted
import com.example.widget.WidgetDataUpdater

enum class WidgetType(val title: String, val icon: String, val desc: String) {
    MISSION_PROGRESS("Mission Progress", "🎯", "Revenue vs ₹10,00,000 target & days remaining"),
    TODAYS_MISSION("Today's Mission", "✅", "Live checklist & completion percentage"),
    STREAK("Streak Tracker", "🔥", "Current & longest consistency streaks"),
    NAMAZ("Namaz Tracker", "🕌", "5 daily prayers status & countdown"),
    CEO_DASHBOARD("CEO Dashboard", "👑", "5 high-level corporate KPIs"),
    CONTENT_CREATOR("Content Creator", "🎬", "Shorts/Clips posted & metrics"),
    HABITS("Habits Tracker", "⚡", "Habits list & completion status"),
    DAILY_QUOTE("Daily Quote", "💬", "Daily mindset typography & categories"),
    FINANCIAL_FREEDOM("Financial Freedom", "💰", "Today, week, month & lifetime revenue"),
    VISION_BOARD("Vision Board", "🦅", "Dream collage & freedom promise")
}

enum class WidgetSize { SMALL_2X2, LARGE_4X2 }

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun WidgetsStudioScreen(viewModel: MissionViewModel) {
    val context = LocalContext.current
    val haptics = LocalHapticFeedback.current

    val totalRevenue by viewModel.totalRevenueAmount.collectAsState()
    val todayMission by viewModel.todayMission.collectAsState()
    val streakStats by viewModel.streakStats.collectAsState()
    val habits by viewModel.habits.collectAsState()
    val todayLogs by viewModel.todayHabitLogs.collectAsState()
    val quotes by viewModel.allQuotes.collectAsState()
    val visionItems by viewModel.visionItems.collectAsState()

    var selectedType by remember { mutableStateOf(WidgetType.MISSION_PROGRESS) }
    var selectedSize by remember { mutableStateOf(WidgetSize.LARGE_4X2) }
    var selectedAccentColor by remember { mutableStateOf(GoldPrimary) }
    var isTransparentMode by remember { mutableStateOf(false) }
    var showInstructionsDialog by remember { mutableStateOf(false) }

    val accentColors = listOf(
        GoldPrimary to "Gold",
        Color(0xFF4CAF50) to "Green",
        Color(0xFF2196F3) to "Blue",
        Color(0xFFE91E63) to "Red"
    )

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // Header Banner
        item {
            GlassCard(modifier = Modifier.fillMaxWidth()) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column {
                        Text(
                            text = "📱 WIDGET SYSTEM STUDIO",
                            style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.ExtraBold),
                            color = selectedAccentColor
                        )
                        Text(
                            text = "10 Material You Offline System Widgets",
                            style = MaterialTheme.typography.bodySmall,
                            color = TextMuted
                        )
                    }

                    Button(
                        onClick = {
                            haptics.performHapticFeedback(HapticFeedbackType.LongPress)
                            WidgetDataUpdater.updateAllWidgets(context)
                            Toast.makeText(context, "System Widgets Updated!", Toast.LENGTH_SHORT).show()
                        },
                        colors = ButtonDefaults.buttonColors(containerColor = selectedAccentColor, contentColor = Color.Black),
                        shape = RoundedCornerShape(12.dp)
                    ) {
                        Icon(imageVector = Icons.Default.Refresh, contentDescription = "Sync", modifier = Modifier.size(16.dp))
                        Spacer(modifier = Modifier.width(4.dp))
                        Text("Sync All", fontWeight = FontWeight.Bold, fontSize = 12.sp)
                    }
                }
            }
        }

        // Widget Selector Pills
        item {
            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                Text(
                    text = "Select Widget Type (1 of 10):",
                    style = MaterialTheme.typography.labelSmall,
                    color = TextMuted
                )
                LazyRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    items(WidgetType.values()) { type ->
                        val isSelected = selectedType == type
                        FilterChip(
                            selected = isSelected,
                            onClick = {
                                haptics.performHapticFeedback(HapticFeedbackType.TextHandleMove)
                                selectedType = type
                            },
                            label = { Text("${type.icon} ${type.title}") },
                            colors = FilterChipDefaults.filterChipColors(
                                selectedContainerColor = selectedAccentColor,
                                selectedLabelColor = Color.Black
                            )
                        )
                    }
                }
            }
        }

        // Customization Options: Size, Accent Color, Backdrop Mode
        item {
            GlassCard(modifier = Modifier.fillMaxWidth()) {
                Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                    Text("Widget Customization Options", style = MaterialTheme.typography.titleSmall, fontWeight = FontWeight.Bold)

                    // Size Switcher
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text("Widget Size:", style = MaterialTheme.typography.bodyMedium)
                        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                            FilterChip(
                                selected = selectedSize == WidgetSize.SMALL_2X2,
                                onClick = { selectedSize = WidgetSize.SMALL_2X2 },
                                label = { Text("Small (2x2)") },
                                colors = FilterChipDefaults.filterChipColors(selectedContainerColor = selectedAccentColor, selectedLabelColor = Color.Black)
                            )
                            FilterChip(
                                selected = selectedSize == WidgetSize.LARGE_4X2,
                                onClick = { selectedSize = WidgetSize.LARGE_4X2 },
                                label = { Text("Large (4x2)") },
                                colors = FilterChipDefaults.filterChipColors(selectedContainerColor = selectedAccentColor, selectedLabelColor = Color.Black)
                            )
                        }
                    }

                    // Accent Colors
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text("Accent Color:", style = MaterialTheme.typography.bodyMedium)
                        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                            accentColors.forEach { (color, label) ->
                                Box(
                                    modifier = Modifier
                                        .size(28.dp)
                                        .clip(CircleShape)
                                        .background(color)
                                        .border(
                                            width = if (selectedAccentColor == color) 2.5.dp else 0.dp,
                                            color = Color.White,
                                            shape = CircleShape
                                        )
                                        .clickable { selectedAccentColor = color }
                                )
                            }
                        }
                    }

                    // Transparent Mode
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text("Transparent Background Mode:", style = MaterialTheme.typography.bodyMedium)
                        Switch(
                            checked = isTransparentMode,
                            onCheckedChange = { isTransparentMode = it },
                            colors = SwitchDefaults.colors(checkedThumbColor = selectedAccentColor)
                        )
                    }
                }
            }
        }

        // Live Interactive Widget Preview Box
        item {
            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "LIVE INTERACTIVE PREVIEW",
                        style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold, letterSpacing = 1.sp),
                        color = selectedAccentColor
                    )
                    Text(
                        text = "${selectedSize.name} • ${selectedType.title}",
                        style = MaterialTheme.typography.labelSmall,
                        color = TextMuted
                    )
                }

                // Container replicating Home Screen Widget Frame
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(if (selectedSize == WidgetSize.SMALL_2X2) 160.dp else 220.dp)
                        .clip(RoundedCornerShape(24.dp))
                        .background(
                            if (isTransparentMode) Color(0x33141620)
                            else Color(0xFF141620)
                        )
                        .border(
                            width = 1.dp,
                            color = if (isTransparentMode) selectedAccentColor.copy(alpha = 0.5f) else Color(0x33FFFFFF),
                            shape = RoundedCornerShape(24.dp)
                        )
                        .padding(16.dp),
                    contentAlignment = Alignment.Center
                ) {
                    when (selectedType) {
                        WidgetType.MISSION_PROGRESS -> MissionProgressPreview(totalRevenue, selectedSize, selectedAccentColor)
                        WidgetType.TODAYS_MISSION -> TodaysMissionPreview(todayMission, selectedSize, selectedAccentColor, viewModel)
                        WidgetType.STREAK -> StreakPreview(streakStats.currentStreak, selectedSize, selectedAccentColor)
                        WidgetType.NAMAZ -> NamazPreview(todayMission, selectedSize, selectedAccentColor, viewModel)
                        WidgetType.CEO_DASHBOARD -> CeoDashboardPreview(totalRevenue, todayMission, streakStats.currentStreak, habits.size, todayLogs.size, selectedSize, selectedAccentColor)
                        WidgetType.CONTENT_CREATOR -> ContentCreatorPreview(todayMission, selectedSize, selectedAccentColor)
                        WidgetType.HABITS -> HabitsPreview(habits.size, todayLogs.size, selectedSize, selectedAccentColor)
                        WidgetType.DAILY_QUOTE -> DailyQuotePreview(quotes.firstOrNull()?.quote ?: "Discipline equals freedom.", "Mission 10L Mindset", selectedSize, selectedAccentColor)
                        WidgetType.FINANCIAL_FREEDOM -> FinancialFreedomPreview(totalRevenue, selectedSize, selectedAccentColor)
                        WidgetType.VISION_BOARD -> VisionBoardPreview(selectedSize, selectedAccentColor)
                    }
                }
            }
        }

        // How to Add to Home Screen Button
        item {
            OutlinedButton(
                onClick = { showInstructionsDialog = true },
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(12.dp)
            ) {
                Icon(imageVector = Icons.Default.Widgets, contentDescription = "Add")
                Spacer(modifier = Modifier.width(8.dp))
                Text("How to Add Widgets to Home Screen")
            }
        }
    }

    if (showInstructionsDialog) {
        AlertDialog(
            onDismissRequest = { showInstructionsDialog = false },
            title = { Text("How to Add Mission 10L Widgets", fontWeight = FontWeight.Bold) },
            text = {
                Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    Text("1. Go to your Android Home Screen.")
                    Text("2. Long press on any empty area.")
                    Text("3. Tap 'Widgets' from the pop-up menu.")
                    Text("4. Scroll down and find 'Mission 10L'.")
                    Text("5. Drag your desired widget (Mission Progress, Namaz, Today's Checklist, CEO Dashboard, etc.) to your screen!")
                    Text("6. The widget will automatically sync with your database offline in real-time.")
                }
            },
            confirmButton = {
                Button(onClick = { showInstructionsDialog = false }, colors = ButtonDefaults.buttonColors(containerColor = selectedAccentColor, contentColor = Color.Black)) {
                    Text("Got It!", fontWeight = FontWeight.Bold)
                }
            },
            containerColor = Color(0xFF141620)
        )
    }
}

// Preview Components
@Composable
private fun MissionProgressPreview(revenue: Double, size: WidgetSize, accent: Color) {
    val pct = ((revenue / 1000000.0) * 100.0).coerceIn(0.0, 100.0).toFloat()
    val animatedPct by animateFloatAsState(targetValue = pct, animationSpec = tween(1000), label = "pct")

    if (size == WidgetSize.SMALL_2X2) {
        Column(horizontalAlignment = Alignment.CenterHorizontally, verticalArrangement = Arrangement.Center) {
            Box(contentAlignment = Alignment.Center) {
                Canvas(modifier = Modifier.size(70.dp)) {
                    drawArc(Color(0xFF223344), 0f, 360f, false, style = Stroke(width = 8.dp.toPx(), cap = StrokeCap.Round))
                    drawArc(accent, -90f, (animatedPct / 100f) * 360f, false, style = Stroke(width = 8.dp.toPx(), cap = StrokeCap.Round))
                }
                Text("${animatedPct.toInt()}%", fontSize = 14.sp, fontWeight = FontWeight.Bold, color = Color.White)
            }
            Spacer(modifier = Modifier.height(6.dp))
            Text(String.format("₹%,.0f", revenue), fontSize = 12.sp, fontWeight = FontWeight.Bold, color = accent)
        }
    } else {
        Row(modifier = Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically) {
            Box(contentAlignment = Alignment.Center) {
                Canvas(modifier = Modifier.size(100.dp)) {
                    drawArc(Color(0xFF223344), 0f, 360f, false, style = Stroke(width = 12.dp.toPx(), cap = StrokeCap.Round))
                    drawArc(accent, -90f, (animatedPct / 100f) * 360f, false, style = Stroke(width = 12.dp.toPx(), cap = StrokeCap.Round))
                }
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text(String.format("%.1f%%", animatedPct), fontSize = 16.sp, fontWeight = FontWeight.ExtraBold, color = Color.White)
                    Text("TARGET", fontSize = 9.sp, color = TextMuted)
                }
            }
            Spacer(modifier = Modifier.width(16.dp))
            Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
                Text("🎯 MISSION 10L", fontSize = 12.sp, fontWeight = FontWeight.Bold, color = accent)
                Text(String.format("₹%,.0f", revenue), fontSize = 20.sp, fontWeight = FontWeight.ExtraBold, color = Color.White)
                Text("Remaining: ${String.format("₹%,.0f", (1000000.0 - revenue).coerceAtLeast(0.0))}", fontSize = 12.sp, color = TextMuted)
                Text("✨ Every rupee counts.", fontSize = 11.sp, color = accent.copy(alpha = 0.8f))
            }
        }
    }
}

@Composable
private fun TodaysMissionPreview(mission: com.example.data.local.entity.DayMissionEntity, size: WidgetSize, accent: Color, viewModel: MissionViewModel) {
    if (size == WidgetSize.SMALL_2X2) {
        Column(horizontalAlignment = Alignment.CenterHorizontally, verticalArrangement = Arrangement.Center) {
            Text("✅ TODAY", fontSize = 12.sp, fontWeight = FontWeight.Bold, color = accent)
            Spacer(modifier = Modifier.height(8.dp))
            Text("${mission.completionPercentage.toInt()}%", fontSize = 32.sp, fontWeight = FontWeight.ExtraBold, color = Color.White)
            Text("Completed", fontSize = 11.sp, color = TextMuted)
        }
    } else {
        Column(modifier = Modifier.fillMaxWidth()) {
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                Text("✅ TODAY'S MISSION CHECKLIST", fontSize = 12.sp, fontWeight = FontWeight.Bold, color = accent)
                Text("${mission.completionPercentage.toInt()}%", fontSize = 12.sp, fontWeight = FontWeight.Bold, color = Color.White)
            }
            Spacer(modifier = Modifier.height(8.dp))
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                WidgetCheckItem("🕌 5 Namaz", mission.namaz5Prayers) { viewModel.updateChecklistField("namaz", !mission.namaz5Prayers) }
                WidgetCheckItem("🎬 Boxabl", mission.boxablClipPosted) { viewModel.updateChecklistField("boxabl", !mission.boxablClipPosted) }
            }
            Spacer(modifier = Modifier.height(4.dp))
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                WidgetCheckItem("🎬 Cantina", mission.cantinaClipPosted) { viewModel.updateChecklistField("cantina", !mission.cantinaClipPosted) }
                WidgetCheckItem("💪 Workout", mission.workout) { viewModel.updateChecklistField("workout", !mission.workout) }
            }
            Spacer(modifier = Modifier.height(4.dp))
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                WidgetCheckItem("📖 Read 10m", mission.read10Minutes) { viewModel.updateChecklistField("read10", !mission.read10Minutes) }
                WidgetCheckItem("😴 Sleep 12m", mission.sleptBeforeMidnight) { viewModel.updateChecklistField("sleep", !mission.sleptBeforeMidnight) }
            }
        }
    }
}

@Composable
private fun WidgetCheckItem(label: String, checked: Boolean, onClick: () -> Unit) {
    Row(
        modifier = Modifier
            .clickable { onClick() }
            .padding(vertical = 2.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(text = if (checked) "☑ " else "□ ", fontSize = 14.sp, color = if (checked) Color(0xFF4CAF50) else Color.Gray)
        Text(text = label, fontSize = 11.sp, color = Color.White)
    }
}

@Composable
private fun StreakPreview(streak: Int, size: WidgetSize, accent: Color) {
    Column(horizontalAlignment = Alignment.CenterHorizontally, verticalArrangement = Arrangement.Center) {
        Text("🔥 DAILY STREAK", fontSize = 12.sp, fontWeight = FontWeight.Bold, color = accent)
        Text("$streak Days", fontSize = 32.sp, fontWeight = FontWeight.ExtraBold, color = Color.White)
        Text("\"Don't break the chain.\"", fontSize = 12.sp, color = TextMuted)
    }
}

@Composable
private fun NamazPreview(mission: com.example.data.local.entity.DayMissionEntity, size: WidgetSize, accent: Color, viewModel: MissionViewModel) {
    Column(horizontalAlignment = Alignment.CenterHorizontally, verticalArrangement = Arrangement.Center) {
        Text("🕌 PRAYERS / NAMAZ", fontSize = 12.sp, fontWeight = FontWeight.Bold, color = accent)
        Spacer(modifier = Modifier.height(6.dp))
        Text(
            text = if (mission.namaz5Prayers) "✔ All 5 Prayers Completed!" else "⏳ Prayers Pending Today",
            fontSize = 14.sp,
            fontWeight = FontWeight.Bold,
            color = Color.White
        )
        Spacer(modifier = Modifier.height(8.dp))
        Button(
            onClick = { viewModel.updateChecklistField("namaz", !mission.namaz5Prayers) },
            colors = ButtonDefaults.buttonColors(containerColor = accent, contentColor = Color.Black),
            shape = RoundedCornerShape(8.dp)
        ) {
            Text(if (mission.namaz5Prayers) "Mark Pending" else "Quick Complete All Namaz", fontSize = 10.sp, fontWeight = FontWeight.Bold)
        }
    }
}

@Composable
private fun CeoDashboardPreview(rev: Double, mission: com.example.data.local.entity.DayMissionEntity, streak: Int, totalHabits: Int, doneHabits: Int, size: WidgetSize, accent: Color) {
    Column(modifier = Modifier.fillMaxWidth()) {
        Text("👑 CEO DASHBOARD KPIs", fontSize = 12.sp, fontWeight = FontWeight.Bold, color = accent)
        Spacer(modifier = Modifier.height(12.dp))
        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
            KpiMiniCard("Revenue", String.format("₹%,.0f", rev), accent)
            KpiMiniCard("Mission", "${mission.completionPercentage.toInt()}%", accent)
            KpiMiniCard("Streak", "$streak 🔥", accent)
            KpiMiniCard("Habits", "$doneHabits/$totalHabits", accent)
        }
    }
}

@Composable
private fun KpiMiniCard(label: String, valStr: String, accent: Color) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Text(label, fontSize = 9.sp, color = TextMuted)
        Text(valStr, fontSize = 12.sp, fontWeight = FontWeight.Bold, color = Color.White)
    }
}

@Composable
private fun ContentCreatorPreview(mission: com.example.data.local.entity.DayMissionEntity, size: WidgetSize, accent: Color) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Text("🎬 CONTENT CREATOR", fontSize = 12.sp, fontWeight = FontWeight.Bold, color = accent)
        Spacer(modifier = Modifier.height(8.dp))
        Text("Boxabl Clip: ${if (mission.boxablClipPosted) "Posted ✔" else "Pending ⏳"}", fontSize = 12.sp, color = Color.White)
        Text("Cantina Clip: ${if (mission.cantinaClipPosted) "Posted ✔" else "Pending ⏳"}", fontSize = 12.sp, color = Color.White)
    }
}

@Composable
private fun HabitsPreview(total: Int, done: Int, size: WidgetSize, accent: Color) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Text("⚡ DAILY HABITS", fontSize = 12.sp, fontWeight = FontWeight.Bold, color = accent)
        Text("$done / $total", fontSize = 28.sp, fontWeight = FontWeight.Bold, color = Color.White)
        Text("Keep the momentum going!", fontSize = 11.sp, color = TextMuted)
    }
}

@Composable
private fun DailyQuotePreview(quote: String, author: String, size: WidgetSize, accent: Color) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Text("💬 DAILY MINDSET", fontSize = 10.sp, fontWeight = FontWeight.Bold, color = accent)
        Spacer(modifier = Modifier.height(4.dp))
        Text("\"$quote\"", fontSize = 12.sp, fontWeight = FontWeight.Bold, color = Color.White, textAlign = TextAlign.Center)
        Spacer(modifier = Modifier.height(4.dp))
        Text("— $author", fontSize = 10.sp, color = TextMuted)
    }
}

@Composable
private fun FinancialFreedomPreview(rev: Double, size: WidgetSize, accent: Color) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Text("💰 FINANCIAL FREEDOM", fontSize = 12.sp, fontWeight = FontWeight.Bold, color = accent)
        Spacer(modifier = Modifier.height(8.dp))
        Text(String.format("₹%,.0f", rev), fontSize = 24.sp, fontWeight = FontWeight.ExtraBold, color = Color.White)
        Text("Target: ₹10,00,000", fontSize = 11.sp, color = TextMuted)
    }
}

@Composable
private fun VisionBoardPreview(size: WidgetSize, accent: Color) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Text("🦅 VISION BOARD", fontSize = 12.sp, fontWeight = FontWeight.Bold, color = accent)
        Spacer(modifier = Modifier.height(6.dp))
        Text("Freedom • Family • ₹10,00,000", fontSize = 14.sp, fontWeight = FontWeight.Bold, color = Color.White)
        Spacer(modifier = Modifier.height(4.dp))
        Text("\"Build the life you promised yourself.\"", fontSize = 11.sp, color = TextMuted)
    }
}
