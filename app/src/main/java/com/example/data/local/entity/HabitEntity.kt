package com.example.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "habits")
data class HabitEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val title: String,
    val category: String = "Daily",
    val streakCount: Int = 0,
    val isArchived: Boolean = false,
    val reminderHour: Int? = null,
    val reminderMinute: Int? = null,
    val isReminderEnabled: Boolean = false
)

@Entity(tableName = "habit_logs")
data class HabitLogEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val habitId: Long,
    val dateString: String, // "YYYY-MM-DD"
    val completed: Boolean = true
)
