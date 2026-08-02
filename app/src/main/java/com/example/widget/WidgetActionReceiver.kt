package com.example.widget

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import com.example.data.local.AppDatabase
import com.example.data.local.entity.DayMissionEntity
import com.example.data.local.entity.HabitLogEntity
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.*

class WidgetActionReceiver : BroadcastReceiver() {

    override fun onReceive(context: Context, intent: Intent) {
        val action = intent.action ?: return
        val appContext = context.applicationContext

        if (action == ACTION_TOGGLE) {
            val field = intent.getStringExtra(EXTRA_FIELD) ?: return
            val pendingResult = goAsync()
            CoroutineScope(Dispatchers.IO).launch {
                try {
                    val db = AppDatabase.getDatabase(appContext)
                    val dao = db.missionDao()
                    val todayStr = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(Date())
                    val current = dao.getTodayMissionSync(todayStr) ?: DayMissionEntity(date = todayStr)

                    val updatedTemp = when (field) {
                        "fajr" -> {
                            val newFajr = !current.fajr
                            val all5 = newFajr && current.dhuhr && current.asr && current.maghrib && current.isha
                            current.copy(fajr = newFajr, namaz5Prayers = all5)
                        }
                        "dhuhr" -> {
                            val newDhuhr = !current.dhuhr
                            val all5 = current.fajr && newDhuhr && current.asr && current.maghrib && current.isha
                            current.copy(dhuhr = newDhuhr, namaz5Prayers = all5)
                        }
                        "asr" -> {
                            val newAsr = !current.asr
                            val all5 = current.fajr && current.dhuhr && newAsr && current.maghrib && current.isha
                            current.copy(asr = newAsr, namaz5Prayers = all5)
                        }
                        "maghrib" -> {
                            val newMaghrib = !current.maghrib
                            val all5 = current.fajr && current.dhuhr && current.asr && newMaghrib && current.isha
                            current.copy(maghrib = newMaghrib, namaz5Prayers = all5)
                        }
                        "isha" -> {
                            val newIsha = !current.isha
                            val all5 = current.fajr && current.dhuhr && current.asr && current.maghrib && newIsha
                            current.copy(isha = newIsha, namaz5Prayers = all5)
                        }
                        "namaz" -> {
                            val newNamaz = !current.namaz5Prayers
                            current.copy(
                                namaz5Prayers = newNamaz,
                                fajr = newNamaz,
                                dhuhr = newNamaz,
                                asr = newNamaz,
                                maghrib = newNamaz,
                                isha = newNamaz
                            )
                        }
                        "boxabl" -> current.copy(boxablClipPosted = !current.boxablClipPosted)
                        "cantina" -> current.copy(cantinaClipPosted = !current.cantinaClipPosted)
                        "islamic" -> current.copy(islamicChannelChecked = !current.islamicChannelChecked)
                        "analytics" -> current.copy(analyticsReviewed = !current.analyticsReviewed)
                        "focus" -> current.copy(noNewProjectStarted = !current.noNewProjectStarted)
                        "workout" -> current.copy(workout = !current.workout)
                        "reading" -> current.copy(read10Minutes = !current.read10Minutes)
                        "sleep" -> current.copy(sleptBeforeMidnight = !current.sleptBeforeMidnight)
                        else -> current
                    }

                    val pct = updatedTemp.calculatePercentage()
                    val updated = updatedTemp.copy(
                        completionPercentage = pct,
                        isCompleted = pct >= 100f
                    )

                    dao.insertOrUpdateDayMission(updated)
                    WidgetDataUpdater.updateAllWidgets(appContext)
                } catch (e: Exception) {
                    e.printStackTrace()
                } finally {
                    pendingResult.finish()
                }
            }
        } else if (action == ACTION_TOGGLE_HABIT) {
            val habitId = intent.getLongExtra(EXTRA_HABIT_ID, -1L)
            if (habitId != -1L) {
                val pendingResult = goAsync()
                CoroutineScope(Dispatchers.IO).launch {
                    try {
                        val db = AppDatabase.getDatabase(appContext)
                        val dao = db.missionDao()
                        val todayStr = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(Date())
                        val existingLogs = dao.getHabitLogsForDateSync(todayStr)
                        val isLogged = existingLogs.any { it.habitId == habitId }

                        if (isLogged) {
                            dao.deleteHabitLog(habitId, todayStr)
                        } else {
                            dao.insertHabitLog(HabitLogEntity(habitId = habitId, dateString = todayStr, completed = true))
                        }
                        WidgetDataUpdater.updateAllWidgets(appContext)
                    } catch (e: Exception) {
                        e.printStackTrace()
                    } finally {
                        pendingResult.finish()
                    }
                }
            }
        } else if (action == ACTION_REFRESH) {
            WidgetDataUpdater.updateAllWidgets(appContext)
        }
    }

    companion object {
        const val ACTION_TOGGLE = "com.example.widget.ACTION_TOGGLE"
        const val ACTION_TOGGLE_HABIT = "com.example.widget.ACTION_TOGGLE_HABIT"
        const val ACTION_REFRESH = "com.example.widget.ACTION_REFRESH"
        const val EXTRA_FIELD = "extra_field"
        const val EXTRA_HABIT_ID = "extra_habit_id"
    }
}

