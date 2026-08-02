package com.example.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.MissionViewModel
import com.example.ui.components.GlassCard
import com.example.ui.theme.AccentGreen
import com.example.ui.theme.GoldPrimary
import com.example.ui.theme.TextMuted
import java.text.NumberFormat
import java.util.Locale

@Composable
fun AnalyticsScreen(viewModel: MissionViewModel) {
    val revenueStats by viewModel.revenueStats.collectAsState()
    val streakStats by viewModel.streakStats.collectAsState()
    val clips by viewModel.allContentClips.collectAsState()
    val habits by viewModel.habits.collectAsState()
    val todayHabitLogs by viewModel.todayHabitLogs.collectAsState()

    val format = NumberFormat.getCurrencyInstance(Locale("en", "IN")).apply { maximumFractionDigits = 0 }

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp),
        contentPadding = PaddingValues(top = 16.dp, bottom = 100.dp)
    ) {
        item {
            Column {
                Text(
                    text = "ANALYTICS & METRICS",
                    style = MaterialTheme.typography.labelSmall.copy(
                        letterSpacing = 2.sp,
                        fontWeight = FontWeight.Bold
                    ),
                    color = MaterialTheme.colorScheme.primary
                )
                Text(
                    text = "Executive Performance Report",
                    style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold),
                    color = MaterialTheme.colorScheme.onSurface
                )
            }
        }

        // Weekly vs Monthly Comparison
        item {
            GlassCard(modifier = Modifier.fillMaxWidth()) {
                Text(
                    text = "Revenue Comparison",
                    style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                    color = MaterialTheme.colorScheme.onSurface
                )
                Spacer(modifier = Modifier.height(12.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    MetricBox(title = "This Week", value = format.format(revenueStats.thisWeek), color = GoldPrimary)
                    MetricBox(title = "This Month", value = format.format(revenueStats.thisMonth), color = AccentGreen)
                    MetricBox(title = "Lifetime", value = format.format(revenueStats.lifetime), color = MaterialTheme.colorScheme.onSurface)
                }
            }
        }

        // Content Clips Analytics
        item {
            val totalClips = clips.size
            val acceptedClips = clips.count { it.status == "Accepted" }
            val approvalRate = if (totalClips > 0) (acceptedClips.toFloat() / totalClips) * 100f else 0f
            val totalViews = clips.sumOf { it.views }

            GlassCard(modifier = Modifier.fillMaxWidth()) {
                Text(
                    text = "Content & Distribution Metrics",
                    style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                    color = MaterialTheme.colorScheme.onSurface
                )
                Spacer(modifier = Modifier.height(12.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    MetricBox(title = "Total Posted", value = "$totalClips Clips", color = MaterialTheme.colorScheme.onSurface)
                    MetricBox(title = "Approval Rate", value = "${approvalRate.toInt()}%", color = AccentGreen)
                    MetricBox(title = "Total Views", value = "$totalViews", color = GoldPrimary)
                }
            }
        }

        // Consistency & Habits Analytics
        item {
            val totalHabits = habits.size
            val todayCompletedHabits = todayHabitLogs.size
            val habitPct = if (totalHabits > 0) (todayCompletedHabits.toFloat() / totalHabits) * 100f else 0f

            GlassCard(modifier = Modifier.fillMaxWidth()) {
                Text(
                    text = "Consistency & Habits Metrics",
                    style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                    color = MaterialTheme.colorScheme.onSurface
                )
                Spacer(modifier = Modifier.height(12.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    MetricBox(title = "Current Streak", value = "${streakStats.currentStreak} Days", color = GoldPrimary)
                    MetricBox(title = "Best Streak", value = "${streakStats.longestStreak} Days", color = MaterialTheme.colorScheme.onSurface)
                    MetricBox(title = "Today's Habits", value = "${habitPct.toInt()}%", color = AccentGreen)
                }
            }
        }
    }
}

@Composable
private fun MetricBox(title: String, value: String, color: androidx.compose.ui.graphics.Color) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Text(text = title, style = MaterialTheme.typography.labelSmall, color = TextMuted)
        Spacer(modifier = Modifier.height(4.dp))
        Text(
            text = value,
            style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
            color = color
        )
    }
}
