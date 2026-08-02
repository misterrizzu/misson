package com.example.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "motivational_quotes")
data class MotivationalQuoteEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val quote: String,
    val category: String, // Discipline, Islamic, Business, Money, Focus, Consistency
    val isFavorite: Boolean = false
)

@Entity(tableName = "vision_items")
data class VisionItemEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val title: String,
    val targetAmountString: String = "",
    val description: String = "",
    val category: String = "Vision",
    val isCompleted: Boolean = false
)

@Entity(tableName = "milestones")
data class MilestoneEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val targetAmount: Double,
    val title: String,
    val isUnlocked: Boolean = false,
    val unlockedTimestamp: Long? = null
)

@Entity(tableName = "achievements")
data class AchievementEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val code: String,
    val title: String,
    val description: String,
    val isUnlocked: Boolean = false,
    val unlockedTimestamp: Long? = null
)

@Entity(tableName = "user_settings")
data class UserSettingEntity(
    @PrimaryKey val key: String,
    val value: String
)
