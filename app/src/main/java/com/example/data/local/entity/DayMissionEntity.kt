package com.example.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "day_missions")
data class DayMissionEntity(
    @PrimaryKey val date: String, // "YYYY-MM-DD"
    val fajr: Boolean = false,
    val dhuhr: Boolean = false,
    val asr: Boolean = false,
    val maghrib: Boolean = false,
    val isha: Boolean = false,
    val namaz5Prayers: Boolean = false,
    val boxablClipPosted: Boolean = false,
    val cantinaClipPosted: Boolean = false,
    val islamicChannelChecked: Boolean = false,
    val analyticsReviewed: Boolean = false,
    val noNewProjectStarted: Boolean = false,
    val workout: Boolean = false,
    val read10Minutes: Boolean = false,
    val sleptBeforeMidnight: Boolean = false,
    val isCompleted: Boolean = false,
    val completionPercentage: Float = 0f
) {
    fun calculatePercentage(): Float {
        val prayerDone = namaz5Prayers || (fajr && dhuhr && asr && maghrib && isha)
        val items = listOf(
            prayerDone,
            boxablClipPosted,
            cantinaClipPosted,
            islamicChannelChecked,
            analyticsReviewed,
            noNewProjectStarted,
            workout,
            read10Minutes,
            sleptBeforeMidnight
        )
        val done = items.count { it }
        return (done.toFloat() / items.size.toFloat()) * 100f
    }
}
