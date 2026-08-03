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
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.hapticfeedback.HapticFeedbackType
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.MissionViewModel
import com.example.ui.components.GlassCard
import com.example.ui.theme.*
import com.example.widget.WidgetDataUpdater

enum class WidgetType(val title: String, val icon: String, val desc: String) {
    MISSION_PROGRESS("Mission Progress", "🎯", "Revenue vs ₹10,00,000 target & days remaining"),
    TODAYS_MISSION("Today's Mission", "✅", "Live checklist & completion percentage"),
    STREAK("Streak Tracker", "🔥", "Current & longest consistency streaks"),
    NAMAZ("Namaz Tracker", "🕌", "5 daily prayers status & countdown"),
    CEO_DASHBOARD("CEO Dashboard", "👑", "5 high-level corporate KPIs & sparkline"),
    CONTENT_CREATOR("Content Creator", "🎬", "Shorts/Clips posted & metrics"),
    HABITS("Habits Tracker", "⚡", "Habits list & completion status"),
    DAILY_QUOTE("Daily Quote", "💬", "Daily mindset typography & categories"),
    FINANCIAL_FREEDOM("Financial Freedom", "💰", "Today, week, month & lifetime revenue"),
    VISION_BOARD("Vision Board", "🦅", "Dream collage & freedom promise"),
    LOCK_SCREEN("Lock Screen", "🔒", "Ultra minimal Samsung lock screen widget")
}

enum class WidgetSize { SMALL_2X2, LARGE_4X2, FULL_SHOWCASE }

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

    var selectedType by remember { mutableStateOf(WidgetType.MISSION_PROGRESS) }
    var selectedSize by remember { mutableStateOf(WidgetSize.LARGE_4X2) }
    var selectedAccentColor by remember { mutableStateOf(AccentGold) }
    var isDarkWallpaper by remember { mutableStateOf(true) }
    var showInstructionsDialog by remember { mutableStateOf(false) }

    val accentColors = listOf(
        AccentGold to "Gold",
        AccentGreen to "Green",
        AccentBlue to "Blue",
        AccentPurple to "Purple",
        AccentOrange to "Orange",
        AccentRed to "Red"
    )

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .background(BgPrimary)
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
                            text = "👑 GALAXY S22 WIDGET STUDIO",
                            style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.ExtraBold),
                            color = selectedAccentColor
                        )
                        Text(
                            text = "Minimal Luxury • Samsung One UI & Pixel Inspired",
                            style = MaterialTheme.typography.bodySmall,
                            color = TextSecondary
                        )
                    }

                    Button(
                        onClick = {
                            haptics.performHapticFeedback(HapticFeedbackType.LongPress)
                            WidgetDataUpdater.updateAllWidgets(context)
                            Toast.makeText(context, "System Widgets Updated!", Toast.LENGTH_SHORT).show()
                        },
                        colors = ButtonDefaults.buttonColors(containerColor = selectedAccentColor, contentColor = Color.Black),
                        shape = RoundedCornerShape(18.dp)
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
                    text = "SELECT WIDGET TYPE:",
                    style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold, letterSpacing = 1.sp),
                    color = TextSecondary
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
                                selectedLabelColor = Color.Black,
                                containerColor = WidgetSurface,
                                labelColor = TextPrimary
                            ),
                            shape = RoundedCornerShape(18.dp)
                        )
                    }
                }
            }
        }

        // Customization Options: Size, Accent Color, Wallpaper
        item {
            GlassCard(modifier = Modifier.fillMaxWidth()) {
                Column(verticalArrangement = Arrangement.spacedBy(14.dp)) {
                    Text("Widget Customization Options", style = MaterialTheme.typography.titleSmall, fontWeight = FontWeight.Bold, color = TextPrimary)

                    // Size Switcher
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text("Widget Grid Size:", style = MaterialTheme.typography.bodyMedium, color = TextSecondary)
                        Row(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                            FilterChip(
                                selected = selectedSize == WidgetSize.SMALL_2X2,
                                onClick = { selectedSize = WidgetSize.SMALL_2X2 },
                                label = { Text("2x2") },
                                colors = FilterChipDefaults.filterChipColors(selectedContainerColor = selectedAccentColor, selectedLabelColor = Color.Black)
                            )
                            FilterChip(
                                selected = selectedSize == WidgetSize.LARGE_4X2,
                                onClick = { selectedSize = WidgetSize.LARGE_4X2 },
                                label = { Text("4x2") },
                                colors = FilterChipDefaults.filterChipColors(selectedContainerColor = selectedAccentColor, selectedLabelColor = Color.Black)
                            )
                            FilterChip(
                                selected = selectedSize == WidgetSize.FULL_SHOWCASE,
                                onClick = { selectedSize = WidgetSize.FULL_SHOWCASE },
                                label = { Text("Showcase (4x4)") },
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
                        Text("Accent Theme:", style = MaterialTheme.typography.bodyMedium, color = TextSecondary)
                        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                            accentColors.forEach { (color, label) ->
                                Box(
                                    modifier = Modifier
                                        .size(26.dp)
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
                }
            }
        }

        // Live Galaxy S22 Ultra Home Screen Container
        item {
            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "📱 GALAXY S22 HOME SCREEN PREVIEW",
                        style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold, letterSpacing = 1.sp),
                        color = selectedAccentColor
                    )
                    Text(
                        text = "${selectedSize.name} • ${selectedType.title}",
                        style = MaterialTheme.typography.labelSmall,
                        color = TextSecondary
                    )
                }

                // Galaxy S22 Ultra Phone Frame Container
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(32.dp))
                        .background(
                            Brush.verticalGradient(
                                colors = listOf(Color(0xFF0C0D12), Color(0xFF040507))
                            )
                        )
                        .border(1.5.dp, BorderDark, RoundedCornerShape(32.dp))
                        .padding(16.dp)
                ) {
                    Column(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        // Simulated Android Status Bar
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(horizontal = 8.dp, vertical = 4.dp),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text("10:10", fontSize = 11.sp, fontWeight = FontWeight.Bold, color = TextSecondary)
                            Row(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                                Text("5G", fontSize = 10.sp, fontWeight = FontWeight.Bold, color = TextSecondary)
                                Text("98%", fontSize = 10.sp, fontWeight = FontWeight.Bold, color = TextSecondary)
                            }
                        }

                        Spacer(modifier = Modifier.height(12.dp))

                        // Render Widget Frame
                        Box(
                            modifier = Modifier
                                .fillMaxWidth(if (selectedSize == WidgetSize.SMALL_2X2) 0.55f else 1f)
                                .clip(RoundedCornerShape(28.dp))
                                .background(WidgetSurface)
                                .border(1.dp, BorderDark, RoundedCornerShape(28.dp))
                                .padding(16.dp)
                        ) {
                            when (selectedType) {
                                WidgetType.MISSION_PROGRESS -> MissionProgressWidgetRender(totalRevenue, selectedSize, selectedAccentColor)
                                WidgetType.TODAYS_MISSION -> TodaysMissionWidgetRender(todayMission, selectedSize, selectedAccentColor, viewModel)
                                WidgetType.STREAK -> StreakWidgetRender(streakStats.currentStreak, streakStats.longestStreak, selectedSize, selectedAccentColor)
                                WidgetType.NAMAZ -> NamazWidgetRender(todayMission, selectedSize, selectedAccentColor, viewModel)
                                WidgetType.CEO_DASHBOARD -> CeoDashboardWidgetRender(totalRevenue, todayMission, streakStats.currentStreak, habits.size, todayLogs.size, selectedSize, selectedAccentColor)
                                WidgetType.CONTENT_CREATOR -> ContentCreatorWidgetRender(todayMission, selectedSize, selectedAccentColor)
                                WidgetType.HABITS -> HabitsWidgetRender(habits.size, todayLogs.size, selectedSize, selectedAccentColor)
                                WidgetType.DAILY_QUOTE -> DailyQuoteWidgetRender(quotes.firstOrNull()?.quote ?: "Discipline equals freedom.", "Mission 10L Mindset", selectedSize, selectedAccentColor)
                                WidgetType.FINANCIAL_FREEDOM -> FinancialFreedomWidgetRender(totalRevenue, selectedSize, selectedAccentColor)
                                WidgetType.VISION_BOARD -> VisionBoardWidgetRender(selectedSize, selectedAccentColor)
                                WidgetType.LOCK_SCREEN -> LockScreenWidgetRender(totalRevenue, todayMission, streakStats.currentStreak, selectedAccentColor)
                            }
                        }

                        Spacer(modifier = Modifier.height(20.dp))

                        // Simulated Launcher Icon Dock
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceAround,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            DockIcon("📞", "Phone")
                            DockIcon("💬", "Messages")
                            DockIcon("🌐", "Chrome")
                            DockIcon("📷", "Camera")
                        }
                    }
                }
            }
        }

        // Instructions Button
        item {
            OutlinedButton(
                onClick = { showInstructionsDialog = true },
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(18.dp)
            ) {
                Icon(imageVector = Icons.Default.Widgets, contentDescription = "Add")
                Spacer(modifier = Modifier.width(8.dp))
                Text("How to Add Premium Widgets to Home Screen")
            }
        }
    }

    if (showInstructionsDialog) {
        AlertDialog(
            onDismissRequest = { showInstructionsDialog = false },
            title = { Text("How to Add Mission 10L Widgets", fontWeight = FontWeight.Bold, color = TextPrimary) },
            text = {
                Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    Text("1. Go to your Android Home Screen.", color = TextSecondary)
                    Text("2. Long press on any empty area.", color = TextSecondary)
                    Text("3. Tap 'Widgets' from the pop-up menu.", color = TextSecondary)
                    Text("4. Scroll down and find 'Mission 10L'.", color = TextSecondary)
                    Text("5. Drag your desired widget (Mission Progress, Namaz, Today's Checklist, CEO Dashboard, etc.) to your screen!", color = TextSecondary)
                    Text("6. The widget will automatically sync with your offline database in real-time.", color = TextSecondary)
                }
            },
            confirmButton = {
                Button(onClick = { showInstructionsDialog = false }, colors = ButtonDefaults.buttonColors(containerColor = selectedAccentColor, contentColor = Color.Black)) {
                    Text("Got It!", fontWeight = FontWeight.Bold)
                }
            },
            containerColor = SecondarySurface
        )
    }
}

@Composable
private fun DockIcon(emoji: String, label: String) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Box(
            modifier = Modifier
                .size(46.dp)
                .clip(RoundedCornerShape(16.dp))
                .background(SecondarySurface)
                .border(1.dp, BorderDark, RoundedCornerShape(16.dp)),
            contentAlignment = Alignment.Center
        ) {
            Text(emoji, fontSize = 20.sp)
        }
        Spacer(modifier = Modifier.height(4.dp))
        Text(label, fontSize = 9.sp, color = TextSecondary)
    }
}

// -------------------------------------------------------------
// INDIVIDUAL PIXEL-PERFECT WIDGET RENDERERS
// -------------------------------------------------------------

@Composable
private fun MissionProgressWidgetRender(revenue: Double, size: WidgetSize, accent: Color) {
    val pct = ((revenue / 1000000.0) * 100.0).coerceIn(0.0, 100.0).toFloat()
    val animatedPct by animateFloatAsState(targetValue = pct, animationSpec = tween(1000), label = "pct")

    if (size == WidgetSize.SMALL_2X2) {
        Column(horizontalAlignment = Alignment.CenterHorizontally, verticalArrangement = Arrangement.Center) {
            Text("👑 MISSION 10L", fontSize = 11.sp, fontWeight = FontWeight.Bold, color = accent)
            Spacer(modifier = Modifier.height(8.dp))
            Box(contentAlignment = Alignment.Center) {
                Canvas(modifier = Modifier.size(80.dp)) {
                    drawArc(ProgressBg, 0f, 360f, false, style = Stroke(width = 10.dp.toPx(), cap = StrokeCap.Round))
                    drawArc(accent, -90f, (animatedPct / 100f) * 360f, false, style = Stroke(width = 10.dp.toPx(), cap = StrokeCap.Round))
                }
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text("${animatedPct.toInt()}%", fontSize = 18.sp, fontWeight = FontWeight.ExtraBold, color = TextPrimary)
                    Text("₹${(revenue/1000).toInt()}k", fontSize = 11.sp, fontWeight = FontWeight.Bold, color = accent)
                }
            }
            Spacer(modifier = Modifier.height(6.dp))
            Text("Target ₹10L", fontSize = 10.sp, color = TextSecondary)
        }
    } else {
        Row(modifier = Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically) {
            Box(contentAlignment = Alignment.Center) {
                Canvas(modifier = Modifier.size(96.dp)) {
                    drawArc(ProgressBg, 0f, 360f, false, style = Stroke(width = 12.dp.toPx(), cap = StrokeCap.Round))
                    drawArc(accent, -90f, (animatedPct / 100f) * 360f, false, style = Stroke(width = 12.dp.toPx(), cap = StrokeCap.Round))
                }
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text(String.format("%.0f%%", animatedPct), fontSize = 20.sp, fontWeight = FontWeight.ExtraBold, color = TextPrimary)
                    Text("TARGET", fontSize = 9.sp, color = TextSecondary)
                }
            }
            Spacer(modifier = Modifier.width(16.dp))
            Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text("👑 MISSION 10L", fontSize = 13.sp, fontWeight = FontWeight.Bold, color = accent)
                    Spacer(modifier = Modifier.weight(1f))
                    Text("🔥 23d", fontSize = 10.sp, fontWeight = FontWeight.Bold, color = accent)
                }
                Text(String.format("₹%,.0f", revenue), fontSize = 24.sp, fontWeight = FontWeight.ExtraBold, color = TextPrimary)
                Text("Remaining: ${String.format("₹%,.0f", (1000000.0 - revenue).coerceAtLeast(0.0))}", fontSize = 11.sp, color = TextSecondary)
                Text("✨ Every rupee counts.", fontSize = 11.sp, color = accent.copy(alpha = 0.9f))
            }
        }
    }
}

@Composable
private fun TodaysMissionWidgetRender(mission: com.example.data.local.entity.DayMissionEntity, size: WidgetSize, accent: Color, viewModel: MissionViewModel) {
    Column(modifier = Modifier.fillMaxWidth()) {
        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.CenterVertically) {
            Text("🎯 TODAY'S MISSION", fontSize = 13.sp, fontWeight = FontWeight.Bold, color = accent)
            Text("${mission.completionPercentage.toInt()}% DONE", fontSize = 11.sp, fontWeight = FontWeight.Bold, color = AccentGreen)
        }
        Spacer(modifier = Modifier.height(10.dp))
        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            Column(modifier = Modifier.weight(1f), verticalArrangement = Arrangement.spacedBy(6.dp)) {
                CapsuleCheckItem("✔ 5 Daily Prayers", mission.namaz5Prayers) { viewModel.updateChecklistField("namaz", !mission.namaz5Prayers) }
                CapsuleCheckItem("✔ Boxabl Clip", mission.boxablClipPosted) { viewModel.updateChecklistField("boxabl", !mission.boxablClipPosted) }
                CapsuleCheckItem("✔ Cantina Clip", mission.cantinaClipPosted) { viewModel.updateChecklistField("cantina", !mission.cantinaClipPosted) }
            }
            Column(modifier = Modifier.weight(1f), verticalArrangement = Arrangement.spacedBy(6.dp)) {
                CapsuleCheckItem("☐ 30m Workout", mission.workout) { viewModel.updateChecklistField("workout", !mission.workout) }
                CapsuleCheckItem("✔ Read 10 Min", mission.read10Minutes) { viewModel.updateChecklistField("read10", !mission.read10Minutes) }
                CapsuleCheckItem("☐ Sleep Midnight", mission.sleptBeforeMidnight) { viewModel.updateChecklistField("sleep", !mission.sleptBeforeMidnight) }
            }
        }
    }
}

@Composable
private fun CapsuleCheckItem(label: String, checked: Boolean, onClick: () -> Unit) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(18.dp))
            .background(if (checked) AccentGreen.copy(alpha = 0.15f) else SecondarySurface)
            .border(1.dp, if (checked) AccentGreen else BorderDark, RoundedCornerShape(18.dp))
            .clickable { onClick() }
            .padding(horizontal = 10.dp, vertical = 6.dp)
    ) {
        Text(label, fontSize = 11.sp, color = TextPrimary, fontWeight = FontWeight.Medium)
    }
}

@Composable
private fun StreakWidgetRender(current: Int, longest: Int, size: WidgetSize, accent: Color) {
    Row(modifier = Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically) {
        Column(modifier = Modifier.weight(1f), horizontalAlignment = Alignment.CenterHorizontally) {
            Text("🔥", fontSize = 28.sp)
            Text("$current", fontSize = 32.sp, fontWeight = FontWeight.ExtraBold, color = TextPrimary)
            Text("Current Streak", fontSize = 11.sp, color = TextSecondary)
        }
        Box(modifier = Modifier.width(1.dp).height(60.dp).background(BorderDark))
        Column(modifier = Modifier.weight(1.3f).padding(start = 12.dp)) {
            Text("LONGEST STREAK", fontSize = 10.sp, fontWeight = FontWeight.Bold, color = accent)
            Text("🏆 $longest Days", fontSize = 18.sp, fontWeight = FontWeight.Bold, color = TextPrimary)
            Spacer(modifier = Modifier.height(4.dp))
            Text("“Don't break the chain. Great things take time.”", fontSize = 10.sp, color = TextSecondary)
        }
    }
}

@Composable
private fun NamazWidgetRender(mission: com.example.data.local.entity.DayMissionEntity, size: WidgetSize, accent: Color, viewModel: MissionViewModel) {
    Column(modifier = Modifier.fillMaxWidth()) {
        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
            Text("🕌 NAMAZ TIME", fontSize = 13.sp, fontWeight = FontWeight.Bold, color = accent)
            Text("3 / 5 Completed", fontSize = 11.sp, fontWeight = FontWeight.Bold, color = AccentGreen)
        }
        Spacer(modifier = Modifier.height(10.dp))
        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(4.dp)) {
            NamazCard("FAJR", "05:15 AM", true, false)
            NamazCard("DHUHR", "01:30 PM", true, false)
            NamazCard("ASR", "04:45 PM", false, true)
            NamazCard("MAGHRIB", "07:15 PM", false, false)
            NamazCard("ISHA", "08:45 PM", false, false)
        }
        Spacer(modifier = Modifier.height(6.dp))
        Text("“Don't delay prayer, for prayer prohibits immorality.”", fontSize = 9.sp, color = TextSecondary, textAlign = TextAlign.Center, modifier = Modifier.fillMaxWidth())
    }
}

@Composable
private fun RowScope.NamazCard(title: String, time: String, isDone: Boolean, isActive: Boolean) {
    Box(
        modifier = Modifier
            .weight(1f)
            .clip(RoundedCornerShape(14.dp))
            .background(if (isActive) AccentGold.copy(alpha = 0.2f) else SecondarySurface)
            .border(1.dp, if (isActive) AccentGold else BorderDark, RoundedCornerShape(14.dp))
            .padding(vertical = 6.dp),
        contentAlignment = Alignment.Center
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Text(title, fontSize = 8.sp, color = if (isActive) AccentGold else TextSecondary, fontWeight = FontWeight.Bold)
            Text(time, fontSize = 8.sp, color = TextPrimary, fontWeight = FontWeight.Bold)
            Text(if (isDone) "✔" else if (isActive) "⏳" else "○", fontSize = 10.sp, color = if (isDone) AccentGreen else if (isActive) AccentGold else DisabledGray)
        }
    }
}

@Composable
private fun CeoDashboardWidgetRender(rev: Double, mission: com.example.data.local.entity.DayMissionEntity, streak: Int, habits: Int, doneHabits: Int, size: WidgetSize, accent: Color) {
    Column(modifier = Modifier.fillMaxWidth()) {
        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
            Text("👑 CEO DASHBOARD", fontSize = 13.sp, fontWeight = FontWeight.Bold, color = accent)
            Text("LIVE TERMINAL", fontSize = 10.sp, fontWeight = FontWeight.Bold, color = AccentGreen)
        }
        Spacer(modifier = Modifier.height(10.dp))
        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(4.dp)) {
            KpiBox("REVENUE", "₹2.45L", accent, Modifier.weight(1f))
            KpiBox("MISSION", "24%", AccentBlue, Modifier.weight(1f))
            KpiBox("TODAY", "68%", AccentGreen, Modifier.weight(1f))
            KpiBox("STREAK", "23d 🔥", AccentOrange, Modifier.weight(1f))
            KpiBox("CLIPS", "2/2 🎬", AccentPurple, Modifier.weight(1f))
        }
        Spacer(modifier = Modifier.height(8.dp))
        Text("★ Discipline Today. Freedom Tomorrow. ★", fontSize = 10.sp, color = TextSecondary, textAlign = TextAlign.Center, modifier = Modifier.fillMaxWidth())
    }
}

@Composable
private fun KpiBox(label: String, value: String, color: Color, modifier: Modifier) {
    Box(
        modifier = modifier
            .clip(RoundedCornerShape(14.dp))
            .background(SecondarySurface)
            .border(1.dp, BorderDark, RoundedCornerShape(14.dp))
            .padding(vertical = 8.dp),
        contentAlignment = Alignment.Center
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Text(label, fontSize = 8.sp, color = TextSecondary)
            Text(value, fontSize = 11.sp, fontWeight = FontWeight.Bold, color = color)
        }
    }
}

@Composable
private fun ContentCreatorWidgetRender(mission: com.example.data.local.entity.DayMissionEntity, size: WidgetSize, accent: Color) {
    Column(horizontalAlignment = Alignment.CenterHorizontally, modifier = Modifier.fillMaxWidth()) {
        Text("🎬 CONTENT CREATOR", fontSize = 13.sp, fontWeight = FontWeight.Bold, color = accent)
        Spacer(modifier = Modifier.height(8.dp))
        Text("Boxabl Clip: ${if (mission.boxablClipPosted) "Posted ✔" else "Pending ⏳"}", fontSize = 13.sp, color = TextPrimary)
        Text("Cantina Clip: ${if (mission.cantinaClipPosted) "Posted ✔" else "Pending ⏳"}", fontSize = 13.sp, color = TextPrimary)
    }
}

@Composable
private fun HabitsWidgetRender(total: Int, done: Int, size: WidgetSize, accent: Color) {
    Column(horizontalAlignment = Alignment.CenterHorizontally, modifier = Modifier.fillMaxWidth()) {
        Text("⚡ DAILY HABITS", fontSize = 13.sp, fontWeight = FontWeight.Bold, color = accent)
        Spacer(modifier = Modifier.height(4.dp))
        Text("$done / $total", fontSize = 32.sp, fontWeight = FontWeight.ExtraBold, color = TextPrimary)
        Text("Keep the momentum going!", fontSize = 11.sp, color = TextSecondary)
    }
}

@Composable
private fun DailyQuoteWidgetRender(quote: String, author: String, size: WidgetSize, accent: Color) {
    Column(horizontalAlignment = Alignment.CenterHorizontally, modifier = Modifier.fillMaxWidth()) {
        Text("💬 DAILY MINDSET", fontSize = 11.sp, fontWeight = FontWeight.Bold, color = accent)
        Spacer(modifier = Modifier.height(6.dp))
        Text("“$quote”", fontSize = 14.sp, fontWeight = FontWeight.Bold, color = TextPrimary, textAlign = TextAlign.Center)
        Spacer(modifier = Modifier.height(4.dp))
        Text("— $author", fontSize = 11.sp, color = TextSecondary)
    }
}

@Composable
private fun FinancialFreedomWidgetRender(rev: Double, size: WidgetSize, accent: Color) {
    Column(horizontalAlignment = Alignment.CenterHorizontally, modifier = Modifier.fillMaxWidth()) {
        Text("💰 FINANCIAL FREEDOM", fontSize = 13.sp, fontWeight = FontWeight.Bold, color = accent)
        Spacer(modifier = Modifier.height(6.dp))
        Text(String.format("₹%,.0f", rev), fontSize = 28.sp, fontWeight = FontWeight.ExtraBold, color = TextPrimary)
        Text("Target: ₹10,00,000", fontSize = 11.sp, color = TextSecondary)
    }
}

@Composable
private fun VisionBoardWidgetRender(size: WidgetSize, accent: Color) {
    Column(modifier = Modifier.fillMaxWidth()) {
        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
            Text("🦅 VISION BOARD", fontSize = 13.sp, fontWeight = FontWeight.Bold, color = accent)
            Text("10L → FREEDOM", fontSize = 10.sp, fontWeight = FontWeight.Bold, color = TextSecondary)
        }
        Spacer(modifier = Modifier.height(8.dp))
        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(4.dp)) {
            VisionTile("👨‍👩‍👦", "Parents", Modifier.weight(1f))
            VisionTile("🏍️", "Dream Bike", Modifier.weight(1f))
            VisionTile("🏢", "Dream Office", Modifier.weight(1f))
            VisionTile("🌅", "Freedom", Modifier.weight(1f))
        }
    }
}

@Composable
private fun VisionTile(emoji: String, title: String, modifier: Modifier) {
    Box(
        modifier = modifier
            .clip(RoundedCornerShape(14.dp))
            .background(SecondarySurface)
            .border(1.dp, BorderDark, RoundedCornerShape(14.dp))
            .padding(vertical = 8.dp),
        contentAlignment = Alignment.Center
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Text(emoji, fontSize = 18.sp)
            Text(title, fontSize = 9.sp, fontWeight = FontWeight.Bold, color = TextPrimary)
        }
    }
}

@Composable
private fun LockScreenWidgetRender(rev: Double, mission: com.example.data.local.entity.DayMissionEntity, streak: Int, accent: Color) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceEvenly
    ) {
        Text("👑 24%", fontSize = 12.sp, fontWeight = FontWeight.Bold, color = accent)
        Text("•", fontSize = 12.sp, color = BorderDark)
        Text(String.format("₹%,.0f", rev), fontSize = 12.sp, fontWeight = FontWeight.Bold, color = TextPrimary)
        Text("•", fontSize = 12.sp, color = BorderDark)
        Text("🔥 ${streak}d", fontSize = 12.sp, fontWeight = FontWeight.Bold, color = AccentOrange)
        Text("•", fontSize = 12.sp, color = BorderDark)
        Text("Next: 🎬 Boxabl Clip", fontSize = 11.sp, color = TextSecondary)
    }
}
