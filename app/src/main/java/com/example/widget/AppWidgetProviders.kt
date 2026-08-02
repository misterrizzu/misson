package com.example.widget

import android.appwidget.AppWidgetManager
import android.appwidget.AppWidgetProvider
import android.content.Context

class MissionProgressWidgetProvider : AppWidgetProvider() {
    override fun onUpdate(context: Context, appWidgetManager: AppWidgetManager, appWidgetIds: IntArray) {
        WidgetDataUpdater.updateMissionProgressWidgetOnly(context)
    }
}

class TodayMissionWidgetProvider : AppWidgetProvider() {
    override fun onUpdate(context: Context, appWidgetManager: AppWidgetManager, appWidgetIds: IntArray) {
        WidgetDataUpdater.updateTodayMissionWidgetOnly(context)
    }
}

class StreakWidgetProvider : AppWidgetProvider() {
    override fun onUpdate(context: Context, appWidgetManager: AppWidgetManager, appWidgetIds: IntArray) {
        WidgetDataUpdater.updateStreakWidgetOnly(context)
    }
}

class NamazWidgetProvider : AppWidgetProvider() {
    override fun onUpdate(context: Context, appWidgetManager: AppWidgetManager, appWidgetIds: IntArray) {
        WidgetDataUpdater.updateNamazWidgetOnly(context)
    }
}

class CeoDashboardWidgetProvider : AppWidgetProvider() {
    override fun onUpdate(context: Context, appWidgetManager: AppWidgetManager, appWidgetIds: IntArray) {
        WidgetDataUpdater.updateCeoDashboardWidgetOnly(context)
    }
}

class ContentCreatorWidgetProvider : AppWidgetProvider() {
    override fun onUpdate(context: Context, appWidgetManager: AppWidgetManager, appWidgetIds: IntArray) {
        WidgetDataUpdater.updateContentCreatorWidgetOnly(context)
    }
}

class HabitsWidgetProvider : AppWidgetProvider() {
    override fun onUpdate(context: Context, appWidgetManager: AppWidgetManager, appWidgetIds: IntArray) {
        WidgetDataUpdater.updateHabitsWidgetOnly(context)
    }
}

class DailyQuoteWidgetProvider : AppWidgetProvider() {
    override fun onUpdate(context: Context, appWidgetManager: AppWidgetManager, appWidgetIds: IntArray) {
        WidgetDataUpdater.updateDailyQuoteWidgetOnly(context)
    }
}

class FinancialFreedomWidgetProvider : AppWidgetProvider() {
    override fun onUpdate(context: Context, appWidgetManager: AppWidgetManager, appWidgetIds: IntArray) {
        WidgetDataUpdater.updateFinancialWidgetOnly(context)
    }
}

class VisionBoardWidgetProvider : AppWidgetProvider() {
    override fun onUpdate(context: Context, appWidgetManager: AppWidgetManager, appWidgetIds: IntArray) {
        WidgetDataUpdater.updateVisionBoardWidgetOnly(context)
    }
}
