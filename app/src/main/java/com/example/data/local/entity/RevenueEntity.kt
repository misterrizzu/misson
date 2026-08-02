package com.example.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "revenue_entries")
data class RevenueEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val source: String, // Boxabl, Trailblazers, Cantina, YouTube, Affiliate, Other
    val amount: Double,
    val dateTimestamp: Long = System.currentTimeMillis(),
    val dateString: String, // "YYYY-MM-DD"
    val notes: String = ""
)
