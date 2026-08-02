package com.example.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "journal_entries")
data class JournalEntryEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val dateString: String, // "YYYY-MM-DD"
    val mood: Int = 5, // 1-5
    val win: String = "",
    val mistake: String = "",
    val focusTomorrow: String = "",
    val notes: String = "",
    val timestamp: Long = System.currentTimeMillis()
)
