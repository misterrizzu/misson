package com.example.data.repository

import com.example.data.local.dao.MissionDao
import com.example.data.local.entity.*
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import java.text.SimpleDateFormat
import java.util.*

class MissionRepository(private val dao: MissionDao) {

    val targetGoalAmount: Double = 1000000.0 // ₹10,00,000

    val allDayMissions: Flow<List<DayMissionEntity>> = dao.getAllDayMissions()
    val allRevenue: Flow<List<RevenueEntity>> = dao.getAllRevenue()
    val totalRevenue: Flow<Double> = dao.getTotalRevenueFlow().map { it ?: 0.0 }
    val allContentClips: Flow<List<ContentClipEntity>> = dao.getAllContentClips()
    val allHabits: Flow<List<HabitEntity>> = dao.getAllHabits()
    val allJournalEntries: Flow<List<JournalEntryEntity>> = dao.getAllJournalEntries()
    val allQuotes: Flow<List<MotivationalQuoteEntity>> = dao.getAllQuotes()
    val favoriteQuotes: Flow<List<MotivationalQuoteEntity>> = dao.getFavoriteQuotes()
    val visionItems: Flow<List<VisionItemEntity>> = dao.getAllVisionItems()
    val milestones: Flow<List<MilestoneEntity>> = dao.getAllMilestones()
    val achievements: Flow<List<AchievementEntity>> = dao.getAllAchievements()

    fun getDayMission(dateString: String): Flow<DayMissionEntity?> = dao.getDayMission(dateString)

    suspend fun updateDayMission(mission: DayMissionEntity) {
        val totalTasks = 8f
        var checkedCount = 0
        if (mission.boxablClipPosted) checkedCount++
        if (mission.cantinaClipPosted) checkedCount++
        if (mission.islamicChannelChecked) checkedCount++
        if (mission.analyticsReviewed) checkedCount++
        if (mission.noNewProjectStarted) checkedCount++
        if (mission.workout) checkedCount++
        if (mission.read10Minutes) checkedCount++
        if (mission.sleptBeforeMidnight) checkedCount++

        val percentage = (checkedCount / totalTasks) * 100f
        val isCompleted = checkedCount == 8

        val updated = mission.copy(
            completionPercentage = percentage,
            isCompleted = isCompleted
        )
        dao.insertOrUpdateDayMission(updated)
    }

    suspend fun addRevenue(revenue: RevenueEntity) {
        dao.insertRevenue(revenue)
    }

    suspend fun deleteRevenue(id: Long) {
        dao.deleteRevenue(id)
    }

    suspend fun addContentClip(clip: ContentClipEntity) {
        dao.insertContentClip(clip)
    }

    suspend fun deleteContentClip(id: Long) {
        dao.deleteContentClip(id)
    }

    suspend fun addHabit(habit: HabitEntity) {
        dao.insertHabit(habit)
    }

    suspend fun deleteHabit(id: Long) {
        dao.deleteHabit(id)
    }

    fun getHabitLogsForDate(dateString: String): Flow<List<HabitLogEntity>> =
        dao.getHabitLogsForDate(dateString)

    suspend fun toggleHabitLog(habitId: Long, dateString: String, completed: Boolean) {
        if (completed) {
            dao.insertHabitLog(HabitLogEntity(habitId = habitId, dateString = dateString, completed = true))
        } else {
            dao.deleteHabitLog(habitId, dateString)
        }
    }

    suspend fun insertJournalEntry(entry: JournalEntryEntity) {
        dao.insertJournalEntry(entry)
    }

    suspend fun deleteJournalEntry(id: Long) {
        dao.deleteJournalEntry(id)
    }

    suspend fun toggleQuoteFavorite(quote: MotivationalQuoteEntity) {
        dao.updateQuote(quote.copy(isFavorite = !quote.isFavorite))
    }

    suspend fun updateVisionItem(item: VisionItemEntity) {
        dao.updateVisionItem(item)
    }

    suspend fun addVisionItem(item: VisionItemEntity) {
        dao.insertVisionItem(item)
    }

    suspend fun deleteVisionItem(id: Long) {
        dao.deleteVisionItem(id)
    }

    suspend fun getSetting(key: String): String? = dao.getSettingValue(key)

    fun getSettingFlow(key: String): Flow<String?> = dao.getSettingValueFlow(key)

    suspend fun saveSetting(key: String, value: String) {
        dao.insertSetting(UserSettingEntity(key, value))
    }

    suspend fun checkAndUpdateMilestones(currentTotalRevenue: Double) {
        val list = dao.getAllMilestonesSync()
        list.forEach { m ->
            if (!m.isUnlocked && currentTotalRevenue >= m.targetAmount) {
                dao.updateMilestone(m.copy(isUnlocked = true, unlockedTimestamp = System.currentTimeMillis()))
            }
        }
    }

    suspend fun resetAllData() {
        dao.clearDayMissions()
        dao.clearAllRevenue()
        dao.clearAllContentClips()
        dao.clearHabitLogs()
        dao.clearJournal()
    }
}
