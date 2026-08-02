package com.example.data.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import com.example.data.local.dao.MissionDao
import com.example.data.local.entity.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@Database(
    entities = [
        DayMissionEntity::class,
        RevenueEntity::class,
        ContentClipEntity::class,
        HabitEntity::class,
        HabitLogEntity::class,
        JournalEntryEntity::class,
        MotivationalQuoteEntity::class,
        VisionItemEntity::class,
        MilestoneEntity::class,
        AchievementEntity::class,
        UserSettingEntity::class
    ],
    version = 2,
    exportSchema = false
)
abstract class AppDatabase : RoomDatabase() {

    abstract fun missionDao(): MissionDao

    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun getDatabase(context: Context): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "mission_10l_database"
                )
                    .addCallback(DatabaseCallback(context.applicationContext))
                    .fallbackToDestructiveMigration()
                    .build()
                INSTANCE = instance
                instance
            }
        }

        private class DatabaseCallback(private val appContext: Context) : RoomDatabase.Callback() {
            override fun onCreate(db: SupportSQLiteDatabase) {
                super.onCreate(db)
                CoroutineScope(Dispatchers.IO).launch {
                    try {
                        val database = getDatabase(appContext)
                        prepopulateDatabase(database.missionDao())
                    } catch (e: Exception) {
                        e.printStackTrace()
                    }
                }
            }
        }

        suspend fun prepopulateDatabase(dao: MissionDao) {
            try {
                // Seed 100+ Motivational Quotes
                if (dao.getQuoteCount() == 0) {
                    dao.insertQuotes(InitialData.quotes)
                }
                // Seed Milestones
                dao.insertMilestones(InitialData.milestones)
                // Seed Achievements
                dao.insertAchievements(InitialData.achievements)
                // Seed Vision Items
                dao.insertVisionItems(InitialData.visionItems)
                // Seed Default Habits
                InitialData.defaultHabits.forEach { dao.insertHabit(it) }
                // Seed Personal Mission Statement
                dao.insertSetting(UserSettingEntity("mission_statement", "I am committed to absolute discipline, unwavering focus, and daily relentless execution to reach ₹10,00,000."))
                dao.insertSetting(UserSettingEntity("accent_color", "GOLD"))
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }
}
