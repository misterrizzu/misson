package com.example.ui.screens

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.MissionViewModel
import com.example.ui.components.GlassCard
import com.example.ui.theme.AccentGreen
import com.example.ui.theme.GoldPrimary
import com.example.ui.theme.TextMuted

@Composable
fun HabitsJournalScreen(viewModel: MissionViewModel) {
    var selectedTab by remember { mutableIntStateOf(0) } // 0 = Habits, 1 = Journal

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp)
    ) {
        Spacer(modifier = Modifier.height(16.dp))

        // Header Title
        Text(
            text = "DISCIPLINE ENGINE",
            style = MaterialTheme.typography.labelSmall.copy(
                letterSpacing = 2.sp,
                fontWeight = FontWeight.Bold
            ),
            color = MaterialTheme.colorScheme.primary
        )
        Text(
            text = "Habits & Daily Journal",
            style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold),
            color = MaterialTheme.colorScheme.onSurface
        )

        Spacer(modifier = Modifier.height(12.dp))

        // Tab Row
        TabRow(
            selectedTabIndex = selectedTab,
            containerColor = Color(0xFF141620),
            contentColor = MaterialTheme.colorScheme.primary
        ) {
            Tab(
                selected = (selectedTab == 0),
                onClick = { selectedTab = 0 },
                text = { Text("Habits Tracker", fontWeight = FontWeight.Bold) }
            )
            Tab(
                selected = (selectedTab == 1),
                onClick = { selectedTab = 1 },
                text = { Text("Daily Journal", fontWeight = FontWeight.Bold) }
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        if (selectedTab == 0) {
            HabitsTabContent(viewModel = viewModel)
        } else {
            JournalTabContent(viewModel = viewModel)
        }
    }
}

@Composable
private fun HabitsTabContent(viewModel: MissionViewModel) {
    val habits by viewModel.habits.collectAsState()
    val todayLogs by viewModel.todayHabitLogs.collectAsState()
    val context = androidx.compose.ui.platform.LocalContext.current

    var selectedCategory by remember { mutableStateOf("All") }
    var showAddHabitDialog by remember { mutableStateOf(false) }
    var selectedHabitForReminder by remember { mutableStateOf<com.example.data.local.entity.HabitEntity?>(null) }

    val categories = listOf("All", "Namaz", "Content", "Business", "Health", "Mindset")

    val filteredHabits = remember(habits, selectedCategory) {
        if (selectedCategory == "All") habits
        else habits.filter { it.category.contains(selectedCategory, ignoreCase = true) }
    }

    val completedIds = remember(todayLogs) { todayLogs.map { it.habitId }.toSet() }
    val totalHabits = habits.size
    val completedCount = habits.count { completedIds.contains(it.id) }
    val completionPct = if (totalHabits > 0) (completedCount.toFloat() / totalHabits) * 100f else 0f

    LazyColumn(
        verticalArrangement = Arrangement.spacedBy(12.dp),
        contentPadding = PaddingValues(bottom = 100.dp)
    ) {
        // Habits Overview Card
        item {
            GlassCard(modifier = Modifier.fillMaxWidth()) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column {
                        Text(
                            text = "Today's Habit & Namaz Progress",
                            style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                            color = MaterialTheme.colorScheme.onSurface
                        )
                        Text(
                            text = "$completedCount / $totalHabits Completed",
                            style = MaterialTheme.typography.bodySmall,
                            color = TextMuted
                        )
                    }

                    Text(
                        text = "${completionPct.toInt()}%",
                        style = MaterialTheme.typography.headlineSmall.copy(fontWeight = FontWeight.Bold),
                        color = if (completionPct == 100f) AccentGreen else GoldPrimary
                    )
                }

                Spacer(modifier = Modifier.height(8.dp))

                LinearProgressIndicator(
                    progress = { completionPct / 100f },
                    modifier = Modifier.fillMaxWidth().height(8.dp),
                    color = GoldPrimary,
                    trackColor = Color(0xFF222533)
                )
            }
        }

        // Category Pills Filter
        item {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                categories.forEach { cat ->
                    FilterChip(
                        selected = (selectedCategory == cat),
                        onClick = { selectedCategory = cat },
                        label = {
                            Text(
                                text = if (cat == "Namaz") "🕌 Namaz" else cat,
                                fontWeight = if (selectedCategory == cat) FontWeight.Bold else FontWeight.Normal
                            )
                        },
                        colors = FilterChipDefaults.filterChipColors(
                            selectedContainerColor = MaterialTheme.colorScheme.primary,
                            selectedLabelColor = Color.Black
                        )
                    )
                }
            }
        }

        // Add Habit Header Row
        item {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = if (selectedCategory == "Namaz") "Daily Namaz / Prayers" else "Active Daily Habits",
                    style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                    color = MaterialTheme.colorScheme.onSurface
                )

                Button(
                    onClick = { showAddHabitDialog = true },
                    shape = RoundedCornerShape(10.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primary, contentColor = Color.Black)
                ) {
                    Icon(imageVector = Icons.Default.Add, contentDescription = "Add")
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(text = "Add Habit", fontWeight = FontWeight.Bold)
                }
            }
        }

        items(filteredHabits) { habit ->
            val isChecked = completedIds.contains(habit.id)

            GlassCard(modifier = Modifier.fillMaxWidth()) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.weight(1f).clickable {
                            viewModel.toggleHabit(habit.id, !isChecked)
                        }
                    ) {
                        Checkbox(
                            checked = isChecked,
                            onCheckedChange = { viewModel.toggleHabit(habit.id, it) },
                            colors = CheckboxDefaults.colors(
                                checkedColor = MaterialTheme.colorScheme.primary,
                                checkmarkColor = Color.Black
                            )
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Column {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                if (habit.category.contains("Namaz", ignoreCase = true)) {
                                    Text(text = "🕌 ", fontSize = 14.sp)
                                }
                                Text(
                                    text = habit.title,
                                    style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                                    color = if (isChecked) MaterialTheme.colorScheme.onSurface else TextMuted
                                )
                            }
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Text(
                                    text = habit.category,
                                    style = MaterialTheme.typography.labelSmall,
                                    color = MaterialTheme.colorScheme.primary.copy(alpha = 0.8f)
                                )
                                if (habit.isReminderEnabled && habit.reminderHour != null && habit.reminderMinute != null) {
                                    Spacer(modifier = Modifier.width(8.dp))
                                    Text(
                                        text = "🔔 ${com.example.receiver.AlarmScheduler.formatTime(habit.reminderHour, habit.reminderMinute)}",
                                        style = MaterialTheme.typography.labelSmall,
                                        color = GoldPrimary
                                    )
                                }
                            }
                        }
                    }

                    Row(verticalAlignment = Alignment.CenterVertically) {
                        IconButton(onClick = { selectedHabitForReminder = habit }) {
                            Icon(
                                imageVector = if (habit.isReminderEnabled) Icons.Default.AlarmOn else Icons.Default.AlarmAdd,
                                contentDescription = "Set Reminder Alarm",
                                tint = if (habit.isReminderEnabled) GoldPrimary else TextMuted,
                                modifier = Modifier.size(20.dp)
                            )
                        }

                        IconButton(onClick = { viewModel.deleteHabit(habit.id) }) {
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

    // Alarm / Reminder Dialog for Habit
    selectedHabitForReminder?.let { habit ->
        var hourVal by remember { mutableIntStateOf(habit.reminderHour ?: 12) }
        var minuteVal by remember { mutableIntStateOf(habit.reminderMinute ?: 0) }
        var isEnabled by remember { mutableStateOf(habit.isReminderEnabled) }

        AlertDialog(
            onDismissRequest = { selectedHabitForReminder = null },
            title = { Text("Set Reminder Alarm: ${habit.title}", fontWeight = FontWeight.Bold) },
            text = {
                Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text("Enable Alarm Notification", style = MaterialTheme.typography.bodyMedium)
                        Switch(
                            checked = isEnabled,
                            onCheckedChange = { isEnabled = it },
                            colors = SwitchDefaults.colors(checkedThumbColor = GoldPrimary)
                        )
                    }

                    if (isEnabled) {
                        Text("Pick Alarm Time:", style = MaterialTheme.typography.labelSmall, color = TextMuted)
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceEvenly,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                Text("Hour (0-23)", style = MaterialTheme.typography.labelSmall, color = TextMuted)
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    IconButton(onClick = { hourVal = (hourVal - 1 + 24) % 24 }) {
                                        Text("-", fontSize = 20.sp, color = GoldPrimary)
                                    }
                                    Text(
                                        text = String.format("%02d", hourVal),
                                        style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold)
                                    )
                                    IconButton(onClick = { hourVal = (hourVal + 1) % 24 }) {
                                        Text("+", fontSize = 20.sp, color = GoldPrimary)
                                    }
                                }
                            }

                            Text(":", style = MaterialTheme.typography.titleLarge)

                            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                Text("Minute (0-59)", style = MaterialTheme.typography.labelSmall, color = TextMuted)
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    IconButton(onClick = { minuteVal = (minuteVal - 5 + 60) % 60 }) {
                                        Text("-", fontSize = 20.sp, color = GoldPrimary)
                                    }
                                    Text(
                                        text = String.format("%02d", minuteVal),
                                        style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold)
                                    )
                                    IconButton(onClick = { minuteVal = (minuteVal + 5) % 60 }) {
                                        Text("+", fontSize = 20.sp, color = GoldPrimary)
                                    }
                                }
                            }
                        }

                        Text(
                            text = "Formatted Time: ${com.example.receiver.AlarmScheduler.formatTime(hourVal, minuteVal)}",
                            style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.Bold),
                            color = GoldPrimary
                        )

                        Button(
                            onClick = {
                                com.example.receiver.AlarmScheduler.triggerImmediateNotification(
                                    context,
                                    title = "Test Alarm: ${habit.title}",
                                    message = "This is a test notification for ${habit.title}!"
                                )
                            },
                            modifier = Modifier.fillMaxWidth(),
                            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF222533))
                        ) {
                            Icon(imageVector = Icons.Default.NotificationsActive, contentDescription = "Test")
                            Spacer(modifier = Modifier.width(6.dp))
                            Text("Test Notification Now", color = Color.White)
                        }
                    }
                }
            },
            confirmButton = {
                Button(
                    onClick = {
                        viewModel.updateHabitReminder(habit, hourVal, minuteVal, isEnabled)
                        if (isEnabled) {
                            com.example.receiver.AlarmScheduler.scheduleDailyAlarm(
                                context,
                                habit.id.toInt(),
                                title = "Habit Reminder: ${habit.title}",
                                message = "Time to complete your habit: ${habit.title}!",
                                hour = hourVal,
                                minute = minuteVal
                            )
                        } else {
                            com.example.receiver.AlarmScheduler.cancelAlarm(context, habit.id.toInt())
                        }
                        selectedHabitForReminder = null
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primary, contentColor = Color.Black)
                ) {
                    Text("Save Alarm", fontWeight = FontWeight.Bold)
                }
            },
            dismissButton = {
                TextButton(onClick = { selectedHabitForReminder = null }) { Text("Cancel", color = TextMuted) }
            },
            containerColor = Color(0xFF141620)
        )
    }

    if (showAddHabitDialog) {
        var titleText by remember { mutableStateOf("") }
        var categoryText by remember { mutableStateOf("Namaz") }
        var hourVal by remember { mutableIntStateOf(12) }
        var minuteVal by remember { mutableIntStateOf(0) }
        var enableReminder by remember { mutableStateOf(true) }

        AlertDialog(
            onDismissRequest = { showAddHabitDialog = false },
            title = { Text("Add Custom Habit / Prayer", fontWeight = FontWeight.Bold) },
            text = {
                Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                    OutlinedTextField(
                        value = titleText,
                        onValueChange = { titleText = it },
                        label = { Text("Habit / Prayer Title (e.g. Tahajjud Prayer)") },
                        modifier = Modifier.fillMaxWidth(),
                        singleLine = true
                    )
                    OutlinedTextField(
                        value = categoryText,
                        onValueChange = { categoryText = it },
                        label = { Text("Category (e.g. Namaz, Content, Business)") },
                        modifier = Modifier.fillMaxWidth(),
                        singleLine = true
                    )

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text("Set Daily Alarm Reminder", style = MaterialTheme.typography.bodyMedium)
                        Switch(
                            checked = enableReminder,
                            onCheckedChange = { enableReminder = it },
                            colors = SwitchDefaults.colors(checkedThumbColor = GoldPrimary)
                        )
                    }

                    if (enableReminder) {
                        Text(
                            text = "Time: ${com.example.receiver.AlarmScheduler.formatTime(hourVal, minuteVal)}",
                            style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.Bold),
                            color = GoldPrimary
                        )
                    }
                }
            },
            confirmButton = {
                Button(
                    onClick = {
                        if (titleText.isNotBlank()) {
                            viewModel.addHabit(
                                title = titleText,
                                category = categoryText,
                                reminderHour = if (enableReminder) hourVal else null,
                                reminderMinute = if (enableReminder) minuteVal else null,
                                isReminderEnabled = enableReminder
                            )
                            showAddHabitDialog = false
                        }
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primary, contentColor = Color.Black)
                ) {
                    Text("Add Habit", fontWeight = FontWeight.Bold)
                }
            },
            dismissButton = {
                TextButton(onClick = { showAddHabitDialog = false }) { Text("Cancel", color = TextMuted) }
            },
            containerColor = Color(0xFF141620)
        )
    }
}

@Composable
private fun JournalTabContent(viewModel: MissionViewModel) {
    val entries by viewModel.journalEntries.collectAsState()
    var searchQuery by remember { mutableStateOf("") }
    var showAddJournalDialog by remember { mutableStateOf(false) }

    val filteredEntries = remember(entries, searchQuery) {
        if (searchQuery.isBlank()) entries
        else entries.filter {
            it.win.contains(searchQuery, ignoreCase = true) ||
            it.mistake.contains(searchQuery, ignoreCase = true) ||
            it.focusTomorrow.contains(searchQuery, ignoreCase = true) ||
            it.notes.contains(searchQuery, ignoreCase = true)
        }
    }

    LazyColumn(
        verticalArrangement = Arrangement.spacedBy(12.dp),
        contentPadding = PaddingValues(bottom = 100.dp)
    ) {
        item {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                OutlinedTextField(
                    value = searchQuery,
                    onValueChange = { searchQuery = it },
                    label = { Text("Search Journal Notes...") },
                    leadingIcon = { Icon(imageVector = Icons.Default.Search, contentDescription = "Search") },
                    modifier = Modifier.weight(1f),
                    singleLine = true
                )

                Spacer(modifier = Modifier.width(8.dp))

                Button(
                    onClick = { showAddJournalDialog = true },
                    shape = RoundedCornerShape(10.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primary, contentColor = Color.Black)
                ) {
                    Icon(imageVector = Icons.Default.EditNote, contentDescription = "Write")
                }
            }
        }

        if (filteredEntries.isEmpty()) {
            item {
                GlassCard(modifier = Modifier.fillMaxWidth()) {
                    Box(
                        modifier = Modifier.fillMaxWidth().padding(24.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = "No journal entries found. Tap the note button to log today's review!",
                            style = MaterialTheme.typography.bodyMedium,
                            color = TextMuted
                        )
                    }
                }
            }
        } else {
            items(filteredEntries) { entry ->
                GlassCard(modifier = Modifier.fillMaxWidth()) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = entry.dateString,
                            style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                            color = GoldPrimary
                        )

                        Row {
                            Text(
                                text = "Mood: ${entry.mood}/5 ⭐",
                                style = MaterialTheme.typography.labelMedium,
                                color = MaterialTheme.colorScheme.onSurface
                            )
                            IconButton(onClick = { viewModel.deleteJournalEntry(entry.id) }) {
                                Icon(
                                    imageVector = Icons.Default.Delete,
                                    contentDescription = "Delete",
                                    tint = TextMuted,
                                    modifier = Modifier.size(18.dp)
                                )
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(8.dp))

                    if (entry.win.isNotEmpty()) {
                        Text(text = "🏆 Today's Win:", style = MaterialTheme.typography.labelSmall, color = AccentGreen)
                        Text(text = entry.win, style = MaterialTheme.typography.bodyMedium, color = MaterialTheme.colorScheme.onSurface)
                        Spacer(modifier = Modifier.height(4.dp))
                    }

                    if (entry.mistake.isNotEmpty()) {
                        Text(text = "⚠️ Today's Mistake:", style = MaterialTheme.typography.labelSmall, color = Color(0xFFFF8A80))
                        Text(text = entry.mistake, style = MaterialTheme.typography.bodyMedium, color = MaterialTheme.colorScheme.onSurface)
                        Spacer(modifier = Modifier.height(4.dp))
                    }

                    if (entry.focusTomorrow.isNotEmpty()) {
                        Text(text = "🎯 Tomorrow's Focus:", style = MaterialTheme.typography.labelSmall, color = GoldPrimary)
                        Text(text = entry.focusTomorrow, style = MaterialTheme.typography.bodyMedium, color = MaterialTheme.colorScheme.onSurface)
                    }
                }
            }
        }
    }

    if (showAddJournalDialog) {
        var winText by remember { mutableStateOf("") }
        var mistakeText by remember { mutableStateOf("") }
        var focusText by remember { mutableStateOf("") }
        var moodVal by remember { mutableIntStateOf(5) }

        AlertDialog(
            onDismissRequest = { showAddJournalDialog = false },
            title = { Text("Log Daily Journal", fontWeight = FontWeight.Bold) },
            text = {
                Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                    Text(text = "Mood (1 to 5)", style = MaterialTheme.typography.labelMedium, color = TextMuted)
                    Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                        (1..5).forEach { m ->
                            FilterChip(
                                selected = (moodVal == m),
                                onClick = { moodVal = m },
                                label = { Text("$m ⭐") }
                            )
                        }
                    }

                    OutlinedTextField(
                        value = winText,
                        onValueChange = { winText = it },
                        label = { Text("Today's Win") },
                        modifier = Modifier.fillMaxWidth()
                    )

                    OutlinedTextField(
                        value = mistakeText,
                        onValueChange = { mistakeText = it },
                        label = { Text("Today's Mistake") },
                        modifier = Modifier.fillMaxWidth()
                    )

                    OutlinedTextField(
                        value = focusText,
                        onValueChange = { focusText = it },
                        label = { Text("Tomorrow's Focus") },
                        modifier = Modifier.fillMaxWidth()
                    )
                }
            },
            confirmButton = {
                Button(
                    onClick = {
                        viewModel.addJournalEntry(winText, mistakeText, focusText, moodVal, "")
                        showAddJournalDialog = false
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primary, contentColor = Color.Black)
                ) {
                    Text("Save Entry", fontWeight = FontWeight.Bold)
                }
            },
            dismissButton = {
                TextButton(onClick = { showAddJournalDialog = false }) { Text("Cancel", color = TextMuted) }
            },
            containerColor = Color(0xFF141620)
        )
    }
}
