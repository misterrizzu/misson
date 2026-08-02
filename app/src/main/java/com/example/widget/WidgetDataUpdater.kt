package com.example.widget

import android.app.PendingIntent
import android.appwidget.AppWidgetManager
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.graphics.*
import android.widget.RemoteViews
import com.example.MainActivity
import com.example.R
import com.example.data.local.AppDatabase
import com.example.data.local.entity.DayMissionEntity
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.*

object WidgetDataUpdater {

    fun updateAllWidgets(context: Context) {
        val appContext = context.applicationContext
        CoroutineScope(Dispatchers.IO).launch {
            try {
                updateMissionProgressWidgetOnly(appContext)
                updateTodayMissionWidgetOnly(appContext)
                updateStreakWidgetOnly(appContext)
                updateNamazWidgetOnly(appContext)
                updateCeoDashboardWidgetOnly(appContext)
                updateContentCreatorWidgetOnly(appContext)
                updateHabitsWidgetOnly(appContext)
                updateDailyQuoteWidgetOnly(appContext)
                updateFinancialWidgetOnly(appContext)
                updateVisionBoardWidgetOnly(appContext)
            } catch (e: Throwable) {
                e.printStackTrace()
            }
        }
    }

    fun updateMissionProgressWidgetOnly(context: Context) {
        try {
            val appWidgetManager = AppWidgetManager.getInstance(context)
            val compName = ComponentName(context, MissionProgressWidgetProvider::class.java)
            val widgetIds = appWidgetManager.getAppWidgetIds(compName)
            if (widgetIds == null || widgetIds.isEmpty()) return

            CoroutineScope(Dispatchers.IO).launch {
                try {
                    val db = AppDatabase.getDatabase(context)
                    val dao = db.missionDao()
                    val todayStr = getTodayString()
                    val allRevenues = dao.getAllRevenueSync()
                    val totalRev = allRevenues.sumOf { it.amount }
                    val todayRev = allRevenues.filter { it.dateString == todayStr }.sumOf { it.amount }
                    val monthRev = allRevenues.filter { it.dateString.startsWith(todayStr.take(7)) }.sumOf { it.amount }
                    val allMissions = dao.getAllDayMissionsSync()
                    val streak = calculateStreak(allMissions)

                    val pct = ((totalRev / 1000000.0) * 100.0).coerceIn(0.0, 100.0).toFloat()
                    val formattedRev = String.format("₹%,.0f", totalRev)
                    val remaining = String.format("₹%,.0f", (1000000.0 - totalRev).coerceAtLeast(0.0))

                    for (id in widgetIds) {
                        val views = RemoteViews(context.packageName, R.layout.widget_mission_progress_large)
                        views.setTextViewText(R.id.txt_rev_amount, formattedRev)
                        views.setTextViewText(R.id.txt_percentage, String.format("%.1f%%", pct))
                        views.setTextViewText(R.id.txt_remaining_amount, "Remaining: $remaining")
                        views.setTextViewText(R.id.txt_today_rev, String.format("Today: ₹%,.0f", todayRev))
                        views.setTextViewText(R.id.txt_month_rev, String.format("Month: ₹%,.0f", monthRev))
                        views.setTextViewText(R.id.txt_progress_streak, "🔥 $streak d")
                        views.setImageViewBitmap(R.id.img_ring, createRingBitmap(pct, ringColorHex = Color.parseColor("#FF1744")))
                        views.setOnClickPendingIntent(R.id.root_widget, createOpenAppIntent(context, "revenue"))
                        appWidgetManager.updateAppWidget(id, views)
                    }
                } catch (e: Throwable) {
                    e.printStackTrace()
                }
            }
        } catch (e: Throwable) {
            e.printStackTrace()
        }
    }

    fun updateTodayMissionWidgetOnly(context: Context) {
        try {
            val appWidgetManager = AppWidgetManager.getInstance(context)
            val compName = ComponentName(context, TodayMissionWidgetProvider::class.java)
            val widgetIds = appWidgetManager.getAppWidgetIds(compName)
            if (widgetIds == null || widgetIds.isEmpty()) return

            CoroutineScope(Dispatchers.IO).launch {
                try {
                    val db = AppDatabase.getDatabase(context)
                    val dao = db.missionDao()
                    val todayStr = getTodayString()
                    val mission = dao.getTodayMissionSync(todayStr) ?: DayMissionEntity(date = todayStr)
                    val allMissions = dao.getAllDayMissionsSync()
                    val streak = calculateStreak(allMissions)

                    val pct = mission.completionPercentage

                    for (id in widgetIds) {
                        val views = RemoteViews(context.packageName, R.layout.widget_today_mission_large)
                        views.setTextViewText(R.id.txt_today_pct, String.format("%.0f%%", pct))
                        views.setTextViewText(R.id.txt_today_streak, "🔥 $streak STREAK")
                        views.setImageViewBitmap(R.id.img_today_ring, createRingBitmap(pct, ringColorHex = Color.parseColor("#FF1744")))

                        // Namaz
                        views.setTextViewText(R.id.chk_namaz, if (mission.namaz5Prayers) "☑ 5 Daily Prayers" else "☐ 5 Daily Prayers")
                        views.setInt(R.id.chk_namaz, "setBackgroundResource", if (mission.namaz5Prayers) R.drawable.bg_widget_item_checked else R.drawable.bg_widget_item_unchecked)

                        // Boxabl
                        views.setTextViewText(R.id.chk_boxabl, if (mission.boxablClipPosted) "☑ Boxabl Clip Posted" else "☐ Boxabl Clip Posted")
                        views.setInt(R.id.chk_boxabl, "setBackgroundResource", if (mission.boxablClipPosted) R.drawable.bg_widget_item_checked else R.drawable.bg_widget_item_unchecked)

                        // Cantina
                        views.setTextViewText(R.id.chk_cantina, if (mission.cantinaClipPosted) "☑ Cantina Clip Posted" else "☐ Cantina Clip Posted")
                        views.setInt(R.id.chk_cantina, "setBackgroundResource", if (mission.cantinaClipPosted) R.drawable.bg_widget_item_checked else R.drawable.bg_widget_item_unchecked)

                        // Workout
                        views.setTextViewText(R.id.chk_workout, if (mission.workout) "☑ 30 Min Workout" else "☐ 30 Min Workout")
                        views.setInt(R.id.chk_workout, "setBackgroundResource", if (mission.workout) R.drawable.bg_widget_item_checked else R.drawable.bg_widget_item_unchecked)

                        // Sleep
                        views.setTextViewText(R.id.chk_journal, if (mission.sleptBeforeMidnight) "☑ Sleep Before Midnight" else "☐ Sleep Before Midnight")
                        views.setInt(R.id.chk_journal, "setBackgroundResource", if (mission.sleptBeforeMidnight) R.drawable.bg_widget_item_checked else R.drawable.bg_widget_item_unchecked)

                        // Click Listeners for toggles
                        views.setOnClickPendingIntent(R.id.chk_namaz, createActionPendingIntent(context, WidgetActionReceiver.ACTION_TOGGLE, WidgetActionReceiver.EXTRA_FIELD, "namaz"))
                        views.setOnClickPendingIntent(R.id.chk_boxabl, createActionPendingIntent(context, WidgetActionReceiver.ACTION_TOGGLE, WidgetActionReceiver.EXTRA_FIELD, "boxabl"))
                        views.setOnClickPendingIntent(R.id.chk_cantina, createActionPendingIntent(context, WidgetActionReceiver.ACTION_TOGGLE, WidgetActionReceiver.EXTRA_FIELD, "cantina"))
                        views.setOnClickPendingIntent(R.id.chk_workout, createActionPendingIntent(context, WidgetActionReceiver.ACTION_TOGGLE, WidgetActionReceiver.EXTRA_FIELD, "workout"))
                        views.setOnClickPendingIntent(R.id.chk_journal, createActionPendingIntent(context, WidgetActionReceiver.ACTION_TOGGLE, WidgetActionReceiver.EXTRA_FIELD, "sleep"))

                        views.setOnClickPendingIntent(R.id.root_widget, createOpenAppIntent(context, "home"))
                        appWidgetManager.updateAppWidget(id, views)
                    }
                } catch (e: Throwable) {
                    e.printStackTrace()
                }
            }
        } catch (e: Throwable) {
            e.printStackTrace()
        }
    }

    fun updateStreakWidgetOnly(context: Context) {
        try {
            val appWidgetManager = AppWidgetManager.getInstance(context)
            val compName = ComponentName(context, StreakWidgetProvider::class.java)
            val widgetIds = appWidgetManager.getAppWidgetIds(compName)
            if (widgetIds == null || widgetIds.isEmpty()) return

            CoroutineScope(Dispatchers.IO).launch {
                try {
                    val db = AppDatabase.getDatabase(context)
                    val dao = db.missionDao()
                    val allMissions = dao.getAllDayMissionsSync()
                    val streak = calculateStreak(allMissions)

                    for (id in widgetIds) {
                        val views = RemoteViews(context.packageName, R.layout.widget_streak_large)
                        views.setTextViewText(R.id.txt_streak_val, "$streak Days")
                        views.setOnClickPendingIntent(R.id.root_widget, createOpenAppIntent(context, "home"))
                        appWidgetManager.updateAppWidget(id, views)
                    }
                } catch (e: Throwable) {
                    e.printStackTrace()
                }
            }
        } catch (e: Throwable) {
            e.printStackTrace()
        }
    }

    fun updateNamazWidgetOnly(context: Context) {
        try {
            val appWidgetManager = AppWidgetManager.getInstance(context)
            val compName = ComponentName(context, NamazWidgetProvider::class.java)
            val widgetIds = appWidgetManager.getAppWidgetIds(compName)
            if (widgetIds == null || widgetIds.isEmpty()) return

            CoroutineScope(Dispatchers.IO).launch {
                try {
                    val db = AppDatabase.getDatabase(context)
                    val dao = db.missionDao()
                    val todayStr = getTodayString()
                    val mission = dao.getTodayMissionSync(todayStr) ?: DayMissionEntity(date = todayStr)

                    val fajrDone = mission.fajr
                    val dhuhrDone = mission.dhuhr
                    val asrDone = mission.asr
                    val maghribDone = mission.maghrib
                    val ishaDone = mission.isha

                    val count = listOf(fajrDone, dhuhrDone, asrDone, maghribDone, ishaDone).count { it }

                    for (id in widgetIds) {
                        val views = RemoteViews(context.packageName, R.layout.widget_namaz_large)
                        views.setTextViewText(R.id.txt_namaz_count, "$count/5 Done")

                        // Fajr
                        views.setTextViewText(R.id.chk_fajr, if (fajrDone) "☑\nFajr" else "☐\nFajr")
                        views.setInt(R.id.chk_fajr, "setBackgroundResource", if (fajrDone) R.drawable.bg_widget_item_checked else R.drawable.bg_widget_item_unchecked)
                        views.setOnClickPendingIntent(R.id.chk_fajr, createActionPendingIntent(context, WidgetActionReceiver.ACTION_TOGGLE, WidgetActionReceiver.EXTRA_FIELD, "fajr"))

                        // Dhuhr
                        views.setTextViewText(R.id.chk_dhuhr, if (dhuhrDone) "☑\nDhuhr" else "☐\nDhuhr")
                        views.setInt(R.id.chk_dhuhr, "setBackgroundResource", if (dhuhrDone) R.drawable.bg_widget_item_checked else R.drawable.bg_widget_item_unchecked)
                        views.setOnClickPendingIntent(R.id.chk_dhuhr, createActionPendingIntent(context, WidgetActionReceiver.ACTION_TOGGLE, WidgetActionReceiver.EXTRA_FIELD, "dhuhr"))

                        // Asr
                        views.setTextViewText(R.id.chk_asr, if (asrDone) "☑\nAsr" else "☐\nAsr")
                        views.setInt(R.id.chk_asr, "setBackgroundResource", if (asrDone) R.drawable.bg_widget_item_checked else R.drawable.bg_widget_item_unchecked)
                        views.setOnClickPendingIntent(R.id.chk_asr, createActionPendingIntent(context, WidgetActionReceiver.ACTION_TOGGLE, WidgetActionReceiver.EXTRA_FIELD, "asr"))

                        // Maghrib
                        views.setTextViewText(R.id.chk_maghrib, if (maghribDone) "☑\nMaghrib" else "☐\nMaghrib")
                        views.setInt(R.id.chk_maghrib, "setBackgroundResource", if (maghribDone) R.drawable.bg_widget_item_checked else R.drawable.bg_widget_item_unchecked)
                        views.setOnClickPendingIntent(R.id.chk_maghrib, createActionPendingIntent(context, WidgetActionReceiver.ACTION_TOGGLE, WidgetActionReceiver.EXTRA_FIELD, "maghrib"))

                        // Isha
                        views.setTextViewText(R.id.chk_isha, if (ishaDone) "☑\nIsha" else "☐\nIsha")
                        views.setInt(R.id.chk_isha, "setBackgroundResource", if (ishaDone) R.drawable.bg_widget_item_checked else R.drawable.bg_widget_item_unchecked)
                        views.setOnClickPendingIntent(R.id.chk_isha, createActionPendingIntent(context, WidgetActionReceiver.ACTION_TOGGLE, WidgetActionReceiver.EXTRA_FIELD, "isha"))

                        appWidgetManager.updateAppWidget(id, views)
                    }
                } catch (e: Throwable) {
                    e.printStackTrace()
                }
            }
        } catch (e: Throwable) {
            e.printStackTrace()
        }
    }


    fun updateCeoDashboardWidgetOnly(context: Context) {
        try {
            val appWidgetManager = AppWidgetManager.getInstance(context)
            val compName = ComponentName(context, CeoDashboardWidgetProvider::class.java)
            val widgetIds = appWidgetManager.getAppWidgetIds(compName)
            if (widgetIds == null || widgetIds.isEmpty()) return

            CoroutineScope(Dispatchers.IO).launch {
                try {
                    val db = AppDatabase.getDatabase(context)
                    val dao = db.missionDao()
                    val todayStr = getTodayString()
                    val mission = dao.getTodayMissionSync(todayStr) ?: DayMissionEntity(date = todayStr)
                    val allRevenues = dao.getAllRevenueSync()
                    val totalRev = allRevenues.sumOf { it.amount }
                    val allMissions = dao.getAllDayMissionsSync()
                    val streak = calculateStreak(allMissions)
                    val habits = dao.getAllHabitsSync()
                    val todayLogs = dao.getHabitLogsForDateSync(todayStr)

                    for (id in widgetIds) {
                        val views = RemoteViews(context.packageName, R.layout.widget_ceo_dashboard_large)
                        views.setTextViewText(R.id.txt_ceo_rev, String.format("₹%,.0f", totalRev))
                        views.setTextViewText(R.id.txt_ceo_mission, String.format("%.0f%%", mission.completionPercentage))
                        views.setTextViewText(R.id.txt_ceo_streak, "$streak 🔥")
                        views.setTextViewText(R.id.txt_ceo_habits, "${todayLogs.size}/${habits.size}")
                        views.setOnClickPendingIntent(R.id.root_widget, createOpenAppIntent(context, "analytics"))
                        appWidgetManager.updateAppWidget(id, views)
                    }
                } catch (e: Throwable) {
                    e.printStackTrace()
                }
            }
        } catch (e: Throwable) {
            e.printStackTrace()
        }
    }

    fun updateContentCreatorWidgetOnly(context: Context) {
        try {
            val appWidgetManager = AppWidgetManager.getInstance(context)
            val compName = ComponentName(context, ContentCreatorWidgetProvider::class.java)
            val widgetIds = appWidgetManager.getAppWidgetIds(compName)
            if (widgetIds == null || widgetIds.isEmpty()) return

            CoroutineScope(Dispatchers.IO).launch {
                try {
                    val db = AppDatabase.getDatabase(context)
                    val dao = db.missionDao()
                    val todayStr = getTodayString()
                    val mission = dao.getTodayMissionSync(todayStr) ?: DayMissionEntity(date = todayStr)

                    val postedCount = (if (mission.boxablClipPosted) 1 else 0) + (if (mission.cantinaClipPosted) 1 else 0)

                    for (id in widgetIds) {
                        val views = RemoteViews(context.packageName, R.layout.widget_content_creator_large)
                        views.setTextViewText(R.id.txt_clips_count, "$postedCount/2 Clips Posted")
                        views.setOnClickPendingIntent(R.id.root_widget, createOpenAppIntent(context, "content"))
                        appWidgetManager.updateAppWidget(id, views)
                    }
                } catch (e: Throwable) {
                    e.printStackTrace()
                }
            }
        } catch (e: Throwable) {
            e.printStackTrace()
        }
    }

    fun updateHabitsWidgetOnly(context: Context) {
        try {
            val appWidgetManager = AppWidgetManager.getInstance(context)
            val compName = ComponentName(context, HabitsWidgetProvider::class.java)
            val widgetIds = appWidgetManager.getAppWidgetIds(compName)
            if (widgetIds == null || widgetIds.isEmpty()) return

            CoroutineScope(Dispatchers.IO).launch {
                try {
                    val db = AppDatabase.getDatabase(context)
                    val dao = db.missionDao()
                    val todayStr = getTodayString()
                    val habits = dao.getAllHabitsSync()
                    val todayLogs = dao.getHabitLogsForDateSync(todayStr)
                    val loggedHabitIds = todayLogs.map { it.habitId }.toSet()

                    for (id in widgetIds) {
                        val views = RemoteViews(context.packageName, R.layout.widget_habits_large)
                        val doneCount = habits.count { loggedHabitIds.contains(it.id) }
                        views.setTextViewText(R.id.txt_habits_ratio, "$doneCount / ${habits.size} Done")

                        val ids = listOf(R.id.chk_habit_1, R.id.chk_habit_2, R.id.chk_habit_3, R.id.chk_habit_4)
                        if (habits.isEmpty()) {
                            views.setViewVisibility(R.id.txt_empty_habits, android.view.View.VISIBLE)
                            ids.forEach { views.setViewVisibility(it, android.view.View.GONE) }
                        } else {
                            views.setViewVisibility(R.id.txt_empty_habits, android.view.View.GONE)
                            for (i in ids.indices) {
                                val viewId = ids[i]
                                if (i < habits.size) {
                                    val h = habits[i]
                                    val isChecked = loggedHabitIds.contains(h.id)
                                    views.setViewVisibility(viewId, android.view.View.VISIBLE)
                                    views.setTextViewText(viewId, if (isChecked) "☑ ${h.title}" else "☐ ${h.title}")
                                    views.setInt(viewId, "setBackgroundResource", if (isChecked) R.drawable.bg_widget_item_checked else R.drawable.bg_widget_item_unchecked)

                                    val intent = Intent(context, WidgetActionReceiver::class.java).apply {
                                        action = WidgetActionReceiver.ACTION_TOGGLE_HABIT
                                        putExtra(WidgetActionReceiver.EXTRA_HABIT_ID, h.id)
                                    }
                                    val pi = PendingIntent.getBroadcast(
                                        context,
                                        ("habit_" + h.id).hashCode(),
                                        intent,
                                        PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
                                    )
                                    views.setOnClickPendingIntent(viewId, pi)
                                } else {
                                    views.setViewVisibility(viewId, android.view.View.GONE)
                                }
                            }
                        }
                        views.setOnClickPendingIntent(R.id.root_widget, createOpenAppIntent(context, "habits"))
                        appWidgetManager.updateAppWidget(id, views)
                    }
                } catch (e: Throwable) {
                    e.printStackTrace()
                }
            }
        } catch (e: Throwable) {
            e.printStackTrace()
        }
    }


    fun updateDailyQuoteWidgetOnly(context: Context) {
        try {
            val appWidgetManager = AppWidgetManager.getInstance(context)
            val compName = ComponentName(context, DailyQuoteWidgetProvider::class.java)
            val widgetIds = appWidgetManager.getAppWidgetIds(compName)
            if (widgetIds == null || widgetIds.isEmpty()) return

            CoroutineScope(Dispatchers.IO).launch {
                try {
                    val db = AppDatabase.getDatabase(context)
                    val dao = db.missionDao()
                    val quotes = dao.getAllQuotesSync()
                    val randomQuote = if (quotes.isNotEmpty()) quotes[Calendar.getInstance().get(Calendar.DAY_OF_YEAR) % quotes.size] else null

                    val body = randomQuote?.quote ?: "Discipline equals freedom."
                    val author = "Mission 10L Mindset"
                    val category = randomQuote?.category ?: "Discipline"

                    for (id in widgetIds) {
                        val views = RemoteViews(context.packageName, R.layout.widget_daily_quote_large)
                        views.setTextViewText(R.id.txt_quote_body, "\"$body\"")
                        views.setTextViewText(R.id.txt_quote_author, "— $author")
                        views.setTextViewText(R.id.txt_quote_category, category)
                        views.setOnClickPendingIntent(R.id.root_widget, createOpenAppIntent(context, "motivation"))
                        appWidgetManager.updateAppWidget(id, views)
                    }
                } catch (e: Throwable) {
                    e.printStackTrace()
                }
            }
        } catch (e: Throwable) {
            e.printStackTrace()
        }
    }

    fun updateFinancialWidgetOnly(context: Context) {
        try {
            val appWidgetManager = AppWidgetManager.getInstance(context)
            val compName = ComponentName(context, FinancialFreedomWidgetProvider::class.java)
            val widgetIds = appWidgetManager.getAppWidgetIds(compName)
            if (widgetIds == null || widgetIds.isEmpty()) return

            CoroutineScope(Dispatchers.IO).launch {
                try {
                    val db = AppDatabase.getDatabase(context)
                    val dao = db.missionDao()
                    val todayStr = getTodayString()
                    val allRevenues = dao.getAllRevenueSync()
                    val totalRev = allRevenues.sumOf { it.amount }
                    val todayRev = allRevenues.filter { it.dateString == todayStr }.sumOf { it.amount }
                    val monthRev = allRevenues.filter { it.dateString.startsWith(todayStr.take(7)) }.sumOf { it.amount }
                    val weekRev = allRevenues.filter { isThisWeek(it.dateString) }.sumOf { it.amount }

                    for (id in widgetIds) {
                        val views = RemoteViews(context.packageName, R.layout.widget_financial_freedom_large)
                        views.setTextViewText(R.id.txt_fin_today, String.format("Today: ₹%,.0f", todayRev))
                        views.setTextViewText(R.id.txt_fin_week, String.format("Week: ₹%,.0f", weekRev))
                        views.setTextViewText(R.id.txt_fin_month, String.format("Month: ₹%,.0f", monthRev))
                        views.setTextViewText(R.id.txt_fin_total, String.format("Total: ₹%,.0f", totalRev))
                        views.setOnClickPendingIntent(R.id.root_widget, createOpenAppIntent(context, "revenue"))
                        appWidgetManager.updateAppWidget(id, views)
                    }
                } catch (e: Throwable) {
                    e.printStackTrace()
                }
            }
        } catch (e: Throwable) {
            e.printStackTrace()
        }
    }

    fun updateVisionBoardWidgetOnly(context: Context) {
        try {
            val appWidgetManager = AppWidgetManager.getInstance(context)
            val compName = ComponentName(context, VisionBoardWidgetProvider::class.java)
            val widgetIds = appWidgetManager.getAppWidgetIds(compName)
            if (widgetIds == null || widgetIds.isEmpty()) return

            for (id in widgetIds) {
                val views = RemoteViews(context.packageName, R.layout.widget_vision_board_large)
                views.setOnClickPendingIntent(R.id.root_widget, createOpenAppIntent(context, "goals"))
                appWidgetManager.updateAppWidget(id, views)
            }
        } catch (e: Throwable) {
            e.printStackTrace()
        }
    }

    private fun getTodayString(): String {
        return SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(Date())
    }

    private fun isThisWeek(dateStr: String): Boolean {
        return try {
            val sdf = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
            val date = sdf.parse(dateStr) ?: return false
            val cal1 = Calendar.getInstance()
            val cal2 = Calendar.getInstance()
            cal2.time = date
            cal1.get(Calendar.WEEK_OF_YEAR) == cal2.get(Calendar.WEEK_OF_YEAR) && cal1.get(Calendar.YEAR) == cal2.get(Calendar.YEAR)
        } catch (e: Throwable) {
            false
        }
    }

    private fun calculateStreak(missions: List<DayMissionEntity>): Int {
        if (missions.isEmpty()) return 0
        val sorted = missions.filter { it.completionPercentage >= 50f }.map { it.date }.sortedDescending()
        var streak = 0
        val cal = Calendar.getInstance()
        val sdf = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())

        for (i in 0..365) {
            val dateStr = sdf.format(cal.time)
            if (sorted.contains(dateStr)) {
                streak++
                cal.add(Calendar.DAY_OF_YEAR, -1)
            } else {
                if (i == 0) {
                    cal.add(Calendar.DAY_OF_YEAR, -1)
                    val yesterdayStr = sdf.format(cal.time)
                    if (sorted.contains(yesterdayStr)) {
                        streak++
                        cal.add(Calendar.DAY_OF_YEAR, -1)
                        continue
                    }
                }
                break
            }
        }
        return streak
    }

    private fun createOpenAppIntent(context: Context, route: String = "home"): PendingIntent {
        val intent = Intent(context, MainActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TOP
            putExtra("target_route", route)
        }
        return PendingIntent.getActivity(
            context,
            route.hashCode(),
            intent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )
    }

    private fun createActionPendingIntent(context: Context, action: String, extraKey: String? = null, extraVal: String? = null): PendingIntent {
        val intent = Intent(context, WidgetActionReceiver::class.java).apply {
            this.action = action
            if (extraKey != null && extraVal != null) {
                putExtra(extraKey, extraVal)
            }
        }
        return PendingIntent.getBroadcast(
            context,
            (action + (extraVal ?: "")).hashCode(),
            intent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )
    }

    fun createRingBitmap(progressPct: Float, widthPx: Int = 100, heightPx: Int = 100, ringColorHex: Int = Color.parseColor("#FF1744")): Bitmap {
        val bitmap = Bitmap.createBitmap(widthPx, heightPx, Bitmap.Config.ARGB_8888)
        val canvas = Canvas(bitmap)

        val strokeWidth = widthPx * 0.12f
        val padding = strokeWidth / 2f + 4f
        val rectF = RectF(padding, padding, widthPx - padding, heightPx - padding)

        val bgPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
            style = Paint.Style.STROKE
            color = Color.parseColor("#331010")
            this.strokeWidth = strokeWidth
            strokeCap = Paint.Cap.ROUND
        }

        val fgPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
            style = Paint.Style.STROKE
            color = ringColorHex
            this.strokeWidth = strokeWidth
            strokeCap = Paint.Cap.ROUND
        }

        canvas.drawArc(rectF, 0f, 360f, false, bgPaint)
        val sweepAngle = (progressPct / 100f).coerceIn(0f, 1f) * 360f
        canvas.drawArc(rectF, -90f, sweepAngle, false, fgPaint)

        return bitmap
    }
}
