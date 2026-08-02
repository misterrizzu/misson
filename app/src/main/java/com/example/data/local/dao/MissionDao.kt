package com.example.data.local.dao

import androidx.room.*
import com.example.data.local.entity.*
import kotlinx.coroutines.flow.Flow

@Dao
interface MissionDao {

    // Day Mission
    @Query("SELECT * FROM day_missions WHERE date = :dateString")
    fun getDayMission(dateString: String): Flow<DayMissionEntity?>

    @Query("SELECT * FROM day_missions WHERE date = :dateString")
    suspend fun getTodayMissionSync(dateString: String): DayMissionEntity?

    @Query("SELECT * FROM day_missions WHERE date = :dateString")
    suspend fun getDayMissionDirect(dateString: String): DayMissionEntity?

    @Query("SELECT * FROM day_missions ORDER BY date DESC")
    fun getAllDayMissions(): Flow<List<DayMissionEntity>>

    @Query("SELECT * FROM day_missions ORDER BY date DESC")
    suspend fun getAllDayMissionsSync(): List<DayMissionEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertOrUpdateDayMission(dayMission: DayMissionEntity)

    // Revenue Entries
    @Query("SELECT * FROM revenue_entries ORDER BY dateTimestamp DESC")
    fun getAllRevenue(): Flow<List<RevenueEntity>>

    @Query("SELECT * FROM revenue_entries ORDER BY dateTimestamp DESC")
    suspend fun getAllRevenueSync(): List<RevenueEntity>

    @Query("SELECT SUM(amount) FROM revenue_entries")
    fun getTotalRevenueFlow(): Flow<Double?>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertRevenue(revenue: RevenueEntity)

    @Query("DELETE FROM revenue_entries WHERE id = :id")
    suspend fun deleteRevenue(id: Long)

    @Query("DELETE FROM revenue_entries")
    suspend fun clearAllRevenue()

    // Content Clips
    @Query("SELECT * FROM content_clips ORDER BY dateTimestamp DESC")
    fun getAllContentClips(): Flow<List<ContentClipEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertContentClip(clip: ContentClipEntity)

    @Query("DELETE FROM content_clips WHERE id = :id")
    suspend fun deleteContentClip(id: Long)

    @Query("DELETE FROM content_clips")
    suspend fun clearAllContentClips()

    // Habits
    @Query("SELECT * FROM habits WHERE isArchived = 0 ORDER BY id ASC")
    fun getAllHabits(): Flow<List<HabitEntity>>

    @Query("SELECT * FROM habits WHERE isArchived = 0 ORDER BY id ASC")
    suspend fun getAllHabitsSync(): List<HabitEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertHabit(habit: HabitEntity)

    @Query("DELETE FROM habits WHERE id = :id")
    suspend fun deleteHabit(id: Long)

    @Query("SELECT * FROM habit_logs WHERE dateString = :dateString")
    fun getHabitLogsForDate(dateString: String): Flow<List<HabitLogEntity>>

    @Query("SELECT * FROM habit_logs WHERE dateString = :dateString")
    suspend fun getHabitLogsForDateDirect(dateString: String): List<HabitLogEntity>

    @Query("SELECT * FROM habit_logs WHERE dateString = :dateString")
    suspend fun getHabitLogsForDateSync(dateString: String): List<HabitLogEntity>

    @Query("SELECT * FROM habit_logs WHERE habitId = :habitId")
    fun getLogsForHabit(habitId: Long): Flow<List<HabitLogEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertHabitLog(log: HabitLogEntity)

    @Query("DELETE FROM habit_logs WHERE habitId = :habitId AND dateString = :dateString")
    suspend fun deleteHabitLog(habitId: Long, dateString: String)

    // Journal
    @Query("SELECT * FROM journal_entries ORDER BY timestamp DESC")
    fun getAllJournalEntries(): Flow<List<JournalEntryEntity>>

    @Query("SELECT * FROM journal_entries WHERE dateString = :dateString")
    fun getJournalForDate(dateString: String): Flow<JournalEntryEntity?>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertJournalEntry(entry: JournalEntryEntity)

    @Query("DELETE FROM journal_entries WHERE id = :id")
    suspend fun deleteJournalEntry(id: Long)

    // Motivational Quotes
    @Query("SELECT * FROM motivational_quotes")
    fun getAllQuotes(): Flow<List<MotivationalQuoteEntity>>

    @Query("SELECT * FROM motivational_quotes")
    suspend fun getAllQuotesSync(): List<MotivationalQuoteEntity>

    @Query("SELECT * FROM motivational_quotes WHERE category = :category")
    fun getQuotesByCategory(category: String): Flow<List<MotivationalQuoteEntity>>

    @Query("SELECT * FROM motivational_quotes WHERE isFavorite = 1")
    fun getFavoriteQuotes(): Flow<List<MotivationalQuoteEntity>>

    @Query("SELECT COUNT(*) FROM motivational_quotes")
    suspend fun getQuoteCount(): Int

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertQuotes(quotes: List<MotivationalQuoteEntity>)

    @Update
    suspend fun updateQuote(quote: MotivationalQuoteEntity)

    // Vision Items
    @Query("SELECT * FROM vision_items ORDER BY id ASC")
    fun getAllVisionItems(): Flow<List<VisionItemEntity>>

    @Query("SELECT * FROM vision_items ORDER BY id ASC")
    suspend fun getVisionItemsSync(): List<VisionItemEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertVisionItem(item: VisionItemEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertVisionItems(items: List<VisionItemEntity>)

    @Update
    suspend fun updateVisionItem(item: VisionItemEntity)

    @Query("DELETE FROM vision_items WHERE id = :id")
    suspend fun deleteVisionItem(id: Long)

    // Milestones
    @Query("SELECT * FROM milestones ORDER BY targetAmount ASC")
    fun getAllMilestones(): Flow<List<MilestoneEntity>>

    @Query("SELECT * FROM milestones ORDER BY targetAmount ASC")
    suspend fun getAllMilestonesSync(): List<MilestoneEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertMilestones(milestones: List<MilestoneEntity>)

    @Update
    suspend fun updateMilestone(milestone: MilestoneEntity)

    // Achievements
    @Query("SELECT * FROM achievements ORDER BY id ASC")
    fun getAllAchievements(): Flow<List<AchievementEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAchievements(achievements: List<AchievementEntity>)

    @Update
    suspend fun updateAchievement(achievement: AchievementEntity)

    // Settings
    @Query("SELECT value FROM user_settings WHERE `key` = :key")
    suspend fun getSettingValue(key: String): String?

    @Query("SELECT value FROM user_settings WHERE `key` = :key")
    fun getSettingValueFlow(key: String): Flow<String?>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertSetting(setting: UserSettingEntity)

    // Reset database
    @Query("DELETE FROM day_missions")
    suspend fun clearDayMissions()

    @Query("DELETE FROM habit_logs")
    suspend fun clearHabitLogs()

    @Query("DELETE FROM journal_entries")
    suspend fun clearJournal()
}
