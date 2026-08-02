package com.example.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "content_clips")
data class ContentClipEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val channel: String, // Boxabl, Cantina, Trailblazers, Islamic
    val clipTitle: String = "",
    val status: String = "Uploaded", // Uploaded, Accepted, Rejected
    val views: Long = 0,
    val revenue: Double = 0.0,
    val dateTimestamp: Long = System.currentTimeMillis(),
    val dateString: String // "YYYY-MM-DD"
)
