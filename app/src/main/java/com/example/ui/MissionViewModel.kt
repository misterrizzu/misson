package com.example.ui

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.data.local.AppDatabase
import com.example.data.local.entity.*
import com.example.data.repository.MissionRepository
import com.example.ui.theme.AccentTheme
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.*

data class RevenueStats(
    val today: Double = 0.0,
    val thisWeek: Double = 0.0,
    val thisMonth: Double = 0.0,
    val lifetime: Double = 0.0,
    val progressPercentage: Float = 0.0f,
    val remainingDaysEstimated: Int = 365
)

data class StreakStats(
    val currentStreak: Int = 0,
    val longestStreak: Int = 0,
    val heatmap: Map<String, Boolean> = emptyMap() // "YYYY-MM-DD" -> completed
)

data class CurrentLevelInfo(
    val levelNumber: Int = 1,
    val levelTitle: String = "Level 1",
    val monthlyTarget: Double = 25000.0,
    val monthlyTargetFormatted: String = "₹25,000/month"
)

class MissionViewModel(application: Application) : AndroidViewModel(application) {

    private val repository: MissionRepository

    init {
        val db = AppDatabase.getDatabase(application)
        repository = MissionRepository(db.missionDao())
    }

    val todayDateString: String
        get() = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(Date())

    val todayFormattedDate: String
        get() = SimpleDateFormat("EEEE, MMMM d, yyyy", Locale.getDefault()).format(Date())

    // Selected Accent Theme
    private val _accentTheme = MutableStateFlow(AccentTheme.GOLD)
    val accentTheme: StateFlow<AccentTheme> = _accentTheme.asStateFlow()

    // Show Opening Dialog
    private val _showOpeningDialog = MutableStateFlow(true)
    val showOpeningDialog: StateFlow<Boolean> = _showOpeningDialog.asStateFlow()

    fun dismissOpeningDialog() {
        _showOpeningDialog.value = false
    }

    // Today's Mission State
    val todayMission: StateFlow<DayMissionEntity> = repository.getDayMission(todayDateString)
        .map { it ?: DayMissionEntity(date = todayDateString) }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = DayMissionEntity(date = todayDateString)
        )

    // All Day Missions for Streak & Heatmap
    val allDayMissions: StateFlow<List<DayMissionEntity>> = repository.allDayMissions
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = emptyList()
        )

    // Streak Calculations
    val streakStats: StateFlow<StreakStats> = allDayMissions.map { list ->
        calculateStreakStats(list)
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = StreakStats()
    )

    // Revenue List & Stats
    val allRevenue: StateFlow<List<RevenueEntity>> = repository.allRevenue
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = emptyList()
        )

    val totalRevenueAmount: StateFlow<Double> = repository.totalRevenue
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = 0.0
        )

    val revenueStats: StateFlow<RevenueStats> = combine(allRevenue, totalRevenueAmount) { list, total ->
        calculateRevenueStats(list, total)
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = RevenueStats()
    )

    // Current Level Info
    val currentLevelInfo: StateFlow<CurrentLevelInfo> = revenueStats.map { stats ->
        calculateLevel(stats.thisMonth, stats.lifetime)
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = CurrentLevelInfo()
    )

    // Content Clips
    val allContentClips: StateFlow<List<ContentClipEntity>> = repository.allContentClips
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = emptyList()
        )

    // Habits & Habits Log
    val habits: StateFlow<List<HabitEntity>> = repository.allHabits
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = emptyList()
        )

    val todayHabitLogs: StateFlow<List<HabitLogEntity>> = repository.getHabitLogsForDate(todayDateString)
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = emptyList()
        )

    // Journal
    val journalEntries: StateFlow<List<JournalEntryEntity>> = repository.allJournalEntries
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = emptyList()
        )

    // Quotes
    val allQuotes: StateFlow<List<MotivationalQuoteEntity>> = repository.allQuotes
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = emptyList()
        )

    val favoriteQuotes: StateFlow<List<MotivationalQuoteEntity>> = repository.favoriteQuotes
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = emptyList()
        )

    val quoteOfTheDay: StateFlow<MotivationalQuoteEntity?> = allQuotes.map { list ->
        if (list.isNotEmpty()) {
            val dayOfYear = Calendar.getInstance().get(Calendar.DAY_OF_YEAR)
            list[dayOfYear % list.size]
        } else null
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = null
    )

    // Vision Items
    val visionItems: StateFlow<List<VisionItemEntity>> = repository.visionItems
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = emptyList()
        )

    // Milestones & Achievements
    val milestones: StateFlow<List<MilestoneEntity>> = repository.milestones
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = emptyList()
        )

    val achievements: StateFlow<List<AchievementEntity>> = repository.achievements
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = emptyList()
        )

    // User Settings
    val missionStatement: StateFlow<String> = repository.getSettingFlow("mission_statement")
        .map { it ?: "I am committed to absolute discipline, daily relentless execution, and zero distractions until ₹10,00,000 is reality." }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = "I am committed to absolute discipline, daily relentless execution, and zero distractions until ₹10,00,000 is reality."
        )

    // Actions
    fun updateChecklistField(field: String, value: Boolean) {
        viewModelScope.launch {
            val current = todayMission.value
            val temp = when (field) {
                "namaz" -> current.copy(namaz5Prayers = value)
                "boxabl" -> current.copy(boxablClipPosted = value)
                "cantina" -> current.copy(cantinaClipPosted = value)
                "islamic" -> current.copy(islamicChannelChecked = value)
                "analytics" -> current.copy(analyticsReviewed = value)
                "noNewProject" -> current.copy(noNewProjectStarted = value)
                "workout" -> current.copy(workout = value)
                "read10" -> current.copy(read10Minutes = value)
                "sleep" -> current.copy(sleptBeforeMidnight = value)
                else -> current
            }
            val pct = calculateChecklistPercentage(temp)
            val updated = temp.copy(
                completionPercentage = pct,
                isCompleted = pct >= 100f
            )
            repository.updateDayMission(updated)
            com.example.widget.WidgetDataUpdater.updateAllWidgets(getApplication())
        }
    }

    private fun calculateChecklistPercentage(m: DayMissionEntity): Float {
        val items = listOf(
            m.namaz5Prayers,
            m.boxablClipPosted,
            m.cantinaClipPosted,
            m.islamicChannelChecked,
            m.analyticsReviewed,
            m.noNewProjectStarted,
            m.workout,
            m.read10Minutes,
            m.sleptBeforeMidnight
        )
        val completedCount = items.count { it }
        return (completedCount.toFloat() / items.size.toFloat()) * 100f
    }

    fun completeDayMission() {
        viewModelScope.launch {
            val current = todayMission.value
            val completed = current.copy(
                namaz5Prayers = true,
                boxablClipPosted = true,
                cantinaClipPosted = true,
                islamicChannelChecked = true,
                analyticsReviewed = true,
                noNewProjectStarted = true,
                workout = true,
                read10Minutes = true,
                sleptBeforeMidnight = true,
                isCompleted = true,
                completionPercentage = 100f
            )
            repository.updateDayMission(completed)
        }
    }

    fun addRevenue(source: String, amount: Double, notes: String) {
        viewModelScope.launch {
            val entry = RevenueEntity(
                source = source,
                amount = amount,
                dateString = todayDateString,
                notes = notes
            )
            repository.addRevenue(entry)
            repository.checkAndUpdateMilestones(totalRevenueAmount.value + amount)
            com.example.widget.WidgetDataUpdater.updateAllWidgets(getApplication())
        }
    }

    fun deleteRevenue(id: Long) {
        viewModelScope.launch {
            repository.deleteRevenue(id)
        }
    }

    fun addContentClip(channel: String, title: String, status: String, views: Long, revenue: Double) {
        viewModelScope.launch {
            val clip = ContentClipEntity(
                channel = channel,
                clipTitle = title,
                status = status,
                views = views,
                revenue = revenue,
                dateString = todayDateString
            )
            repository.addContentClip(clip)
        }
    }

    fun deleteContentClip(id: Long) {
        viewModelScope.launch {
            repository.deleteContentClip(id)
        }
    }

    fun toggleHabit(habitId: Long, completed: Boolean) {
        viewModelScope.launch {
            repository.toggleHabitLog(habitId, todayDateString, completed)
            com.example.widget.WidgetDataUpdater.updateAllWidgets(getApplication())
        }
    }

    fun addHabit(
        title: String,
        category: String,
        reminderHour: Int? = null,
        reminderMinute: Int? = null,
        isReminderEnabled: Boolean = false
    ) {
        viewModelScope.launch {
            repository.addHabit(
                HabitEntity(
                    title = title,
                    category = category,
                    reminderHour = reminderHour,
                    reminderMinute = reminderMinute,
                    isReminderEnabled = isReminderEnabled
                )
            )
        }
    }

    fun updateHabitReminder(habit: HabitEntity, hour: Int?, minute: Int?, enabled: Boolean) {
        viewModelScope.launch {
            repository.addHabit(
                habit.copy(
                    reminderHour = hour,
                    reminderMinute = minute,
                    isReminderEnabled = enabled
                )
            )
        }
    }

    fun deleteHabit(id: Long) {
        viewModelScope.launch {
            repository.deleteHabit(id)
        }
    }

    fun addJournalEntry(win: String, mistake: String, focusTomorrow: String, mood: Int, notes: String) {
        viewModelScope.launch {
            val entry = JournalEntryEntity(
                dateString = todayDateString,
                mood = mood,
                win = win,
                mistake = mistake,
                focusTomorrow = focusTomorrow,
                notes = notes
            )
            repository.insertJournalEntry(entry)
        }
    }

    fun deleteJournalEntry(id: Long) {
        viewModelScope.launch {
            repository.deleteJournalEntry(id)
        }
    }

    fun toggleQuoteFavorite(quote: MotivationalQuoteEntity) {
        viewModelScope.launch {
            repository.toggleQuoteFavorite(quote)
        }
    }

    fun saveMissionStatement(statement: String) {
        viewModelScope.launch {
            repository.saveSetting("mission_statement", statement)
        }
    }

    fun updateVisionItem(item: VisionItemEntity) {
        viewModelScope.launch {
            repository.updateVisionItem(item)
        }
    }

    fun addVisionItem(title: String, targetAmount: String, description: String, category: String) {
        viewModelScope.launch {
            repository.addVisionItem(
                VisionItemEntity(
                    title = title,
                    targetAmountString = targetAmount,
                    description = description,
                    category = category
                )
            )
        }
    }

    fun deleteVisionItem(id: Long) {
        viewModelScope.launch {
            repository.deleteVisionItem(id)
        }
    }

    fun setAccentTheme(theme: AccentTheme) {
        _accentTheme.value = theme
        viewModelScope.launch {
            repository.saveSetting("accent_color", theme.name)
        }
    }

    fun resetAllData() {
        viewModelScope.launch {
            repository.resetAllData()
        }
    }

    // Helper Calculations
    private fun calculateRevenueStats(list: List<RevenueEntity>, total: Double): RevenueStats {
        val cal = Calendar.getInstance()
        val todayStr = todayDateString

        cal.set(Calendar.DAY_OF_WEEK, cal.firstDayOfWeek)
        val startOfWeekMs = cal.timeInMillis

        cal.set(Calendar.DAY_OF_MONTH, 1)
        val startOfMonthMs = cal.timeInMillis

        var todaySum = 0.0
        var weekSum = 0.0
        var monthSum = 0.0

        list.forEach { item ->
            if (item.dateString == todayStr) {
                todaySum += item.amount
            }
            if (item.dateTimestamp >= startOfWeekMs) {
                weekSum += item.amount
            }
            if (item.dateTimestamp >= startOfMonthMs) {
                monthSum += item.amount
            }
        }

        val progressPct = ((total / 1000000.0) * 100.0).coerceIn(0.0, 100.0).toFloat()

        // Estimate remaining days based on average daily revenue (or default 365)
        val remainingAmount = (1000000.0 - total).coerceAtLeast(0.0)
        val estimatedDays = if (monthSum > 0) {
            val dailyAvg = monthSum / 30.0
            (remainingAmount / dailyAvg).toInt().coerceIn(1, 3650)
        } else {
            365
        }

        return RevenueStats(
            today = todaySum,
            thisWeek = weekSum,
            thisMonth = monthSum,
            lifetime = total,
            progressPercentage = progressPct,
            remainingDaysEstimated = estimatedDays
        )
    }

    private fun calculateStreakStats(missions: List<DayMissionEntity>): StreakStats {
        val heatmap = mutableMapOf<String, Boolean>()
        missions.forEach { m ->
            heatmap[m.date] = m.isCompleted
        }

        var currentStreak = 0
        var longestStreak = 0
        var tempStreak = 0

        val cal = Calendar.getInstance()
        val sdf = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())

        // Check last 90 days
        for (i in 0..90) {
            val dateStr = sdf.format(cal.time)
            val isComp = heatmap[dateStr] == true
            if (isComp) {
                tempStreak++
                if (i == 0 || currentStreak > 0) {
                    currentStreak++
                }
                if (tempStreak > longestStreak) {
                    longestStreak = tempStreak
                }
            } else {
                if (i == 0) {
                    // Check if yesterday was completed
                    cal.add(Calendar.DAY_OF_YEAR, -1)
                    val yestStr = sdf.format(cal.time)
                    if (heatmap[yestStr] == true) {
                        currentStreak = 0 // Will count from yesterday
                    }
                    cal.add(Calendar.DAY_OF_YEAR, 1)
                }
                tempStreak = 0
            }
            cal.add(Calendar.DAY_OF_YEAR, -1)
        }

        return StreakStats(
            currentStreak = currentStreak,
            longestStreak = maxOf(currentStreak, longestStreak),
            heatmap = heatmap
        )
    }

    private fun calculateLevel(monthlyRevenue: Double, lifetimeRevenue: Double): CurrentLevelInfo {
        return when {
            lifetimeRevenue >= 1000000.0 -> CurrentLevelInfo(6, "Level 6 - Goal Achieved!", 1000000.0, "₹10,00,000 / month")
            monthlyRevenue >= 500000.0 -> CurrentLevelInfo(5, "Level 5", 1000000.0, "₹10,00,000 / month")
            monthlyRevenue >= 250000.0 -> CurrentLevelInfo(4, "Level 4", 500000.0, "₹5,00,000 / month")
            monthlyRevenue >= 100000.0 -> CurrentLevelInfo(3, "Level 3", 250000.0, "₹2,50,000 / month")
            monthlyRevenue >= 50000.0 -> CurrentLevelInfo(2, "Level 2", 100000.0, "₹1,00,000 / month")
            else -> CurrentLevelInfo(1, "Level 1", 25000.0, "₹25,000 / month")
        }
    }
}
