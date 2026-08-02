package com.example.ui.screens

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.MissionViewModel
import com.example.ui.components.GlassCard
import com.example.ui.theme.AccentGreen
import com.example.ui.theme.GoldPrimary
import com.example.ui.theme.TextMuted

@Composable
fun AchievementsScreen(viewModel: MissionViewModel) {
    val achievements by viewModel.achievements.collectAsState()
    val totalRevenue by viewModel.totalRevenueAmount.collectAsState()
    val streakStats by viewModel.streakStats.collectAsState()
    val clips by viewModel.allContentClips.collectAsState()

    // Calculate dynamically
    val evaluatedAchievements = achievements.map { ach ->
        val isUnlocked = when (ach.code) {
            "FIRST_100" -> totalRevenue >= 100.0
            "FIRST_1000" -> totalRevenue >= 1000.0
            "FIRST_10000" -> totalRevenue >= 10000.0
            "STREAK_7" -> streakStats.longestStreak >= 7
            "STREAK_30" -> streakStats.longestStreak >= 30
            "CLIPS_100" -> clips.size >= 100
            "CLIPS_500" -> clips.size >= 500
            "CLIPS_1000" -> clips.size >= 1000
            else -> ach.isUnlocked
        }
        ach.copy(isUnlocked = isUnlocked)
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp)
    ) {
        Spacer(modifier = Modifier.height(16.dp))

        Text(
            text = "TROPHY ROOM",
            style = MaterialTheme.typography.labelSmall.copy(
                letterSpacing = 2.sp,
                fontWeight = FontWeight.Bold
            ),
            color = MaterialTheme.colorScheme.primary
        )
        Text(
            text = "Achievements & Badges",
            style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold),
            color = MaterialTheme.colorScheme.onSurface
        )

        Spacer(modifier = Modifier.height(16.dp))

        LazyVerticalGrid(
            columns = GridCells.Fixed(2),
            horizontalArrangement = Arrangement.spacedBy(12.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp),
            contentPadding = PaddingValues(bottom = 100.dp)
        ) {
            items(evaluatedAchievements) { ach ->
                GlassCard(
                    modifier = Modifier.fillMaxWidth().height(160.dp),
                    border = BorderStroke(
                        1.dp,
                        if (ach.isUnlocked) GoldPrimary else Color(0x22FFFFFF)
                    )
                ) {
                    Column(
                        modifier = Modifier.fillMaxSize(),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Center
                    ) {
                        Surface(
                            shape = RoundedCornerShape(12.dp),
                            color = if (ach.isUnlocked) GoldPrimary.copy(alpha = 0.2f) else Color(0xFF1E212D),
                            modifier = Modifier.size(44.dp)
                        ) {
                            Box(contentAlignment = Alignment.Center) {
                                Icon(
                                    imageVector = if (ach.isUnlocked) Icons.Default.Star else Icons.Default.Lock,
                                    contentDescription = "Badge",
                                    tint = if (ach.isUnlocked) GoldPrimary else TextMuted
                                )
                            }
                        }

                        Spacer(modifier = Modifier.height(8.dp))

                        Text(
                            text = ach.title,
                            style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold, fontSize = 14.sp),
                            color = if (ach.isUnlocked) MaterialTheme.colorScheme.onSurface else TextMuted,
                            textAlign = TextAlign.Center
                        )

                        Text(
                            text = ach.description,
                            style = MaterialTheme.typography.bodySmall.copy(fontSize = 11.sp),
                            color = TextMuted,
                            textAlign = TextAlign.Center,
                            maxLines = 2
                        )

                        if (ach.isUnlocked) {
                            Spacer(modifier = Modifier.height(4.dp))
                            Text(
                                text = "UNLOCKED",
                                style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold, fontSize = 9.sp),
                                color = AccentGreen
                            )
                        }
                    }
                }
            }
        }
    }
}
