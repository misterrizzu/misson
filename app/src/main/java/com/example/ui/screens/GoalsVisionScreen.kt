package com.example.ui.screens

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
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
fun GoalsVisionScreen(viewModel: MissionViewModel) {
    val milestones by viewModel.milestones.collectAsState()
    val visionItems by viewModel.visionItems.collectAsState()
    val missionStatement by viewModel.missionStatement.collectAsState()
    val totalRevenue by viewModel.totalRevenueAmount.collectAsState()

    var isEditingStatement by remember { mutableStateOf(false) }
    var statementInput by remember(missionStatement) { mutableStateOf(missionStatement) }

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
                    text = "GOALS & VISION",
                    style = MaterialTheme.typography.labelSmall.copy(
                        letterSpacing = 2.sp,
                        fontWeight = FontWeight.Bold
                    ),
                    color = MaterialTheme.colorScheme.primary
                )
                Text(
                    text = "Mission Control & Roadmap",
                    style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold),
                    color = MaterialTheme.colorScheme.onSurface
                )
            }
        }

        // Main Goal Card
        item {
            GlassCard(
                modifier = Modifier.fillMaxWidth(),
                border = BorderStroke(1.5.dp, GoldPrimary)
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        imageVector = Icons.Default.EmojiEvents,
                        contentDescription = "Trophy",
                        tint = GoldPrimary,
                        modifier = Modifier.size(36.dp)
                    )
                    Spacer(modifier = Modifier.width(12.dp))
                    Column {
                        Text(
                            text = "MAIN GOAL",
                            style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold, letterSpacing = 2.sp),
                            color = GoldPrimary
                        )
                        Text(
                            text = "₹10,00,000",
                            style = MaterialTheme.typography.headlineLarge.copy(fontWeight = FontWeight.ExtraBold),
                            color = MaterialTheme.colorScheme.onSurface
                        )
                    }
                }
            }
        }

        // Editable Personal Mission Statement
        item {
            GlassCard(modifier = Modifier.fillMaxWidth()) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "PERSONAL MISSION STATEMENT",
                        style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold, letterSpacing = 1.sp),
                        color = MaterialTheme.colorScheme.primary
                    )

                    IconButton(onClick = {
                        if (isEditingStatement) {
                            viewModel.saveMissionStatement(statementInput)
                            isEditingStatement = false
                        } else {
                            isEditingStatement = true
                        }
                    }) {
                        Icon(
                            imageVector = if (isEditingStatement) Icons.Default.Check else Icons.Default.Edit,
                            contentDescription = "Edit",
                            tint = GoldPrimary
                        )
                    }
                }

                Spacer(modifier = Modifier.height(8.dp))

                if (isEditingStatement) {
                    OutlinedTextField(
                        value = statementInput,
                        onValueChange = { statementInput = it },
                        modifier = Modifier.fillMaxWidth(),
                        maxLines = 4
                    )
                } else {
                    Text(
                        text = "\"$missionStatement\"",
                        style = MaterialTheme.typography.bodyLarge.copy(
                            fontWeight = FontWeight.Medium,
                            lineHeight = 24.sp
                        ),
                        color = MaterialTheme.colorScheme.onSurface
                    )
                }
            }
        }

        // Revenue Milestones Progress
        item {
            Text(
                text = "Revenue Milestones",
                style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                color = MaterialTheme.colorScheme.onSurface
            )
        }

        items(milestones) { milestone ->
            val isAchieved = totalRevenue >= milestone.targetAmount

            GlassCard(
                modifier = Modifier.fillMaxWidth(),
                border = BorderStroke(
                    1.dp,
                    if (isAchieved) AccentGreen else Color(0x22FFFFFF)
                )
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            imageVector = if (isAchieved) Icons.Default.CheckCircle else Icons.Default.Lock,
                            contentDescription = "Lock",
                            tint = if (isAchieved) AccentGreen else TextMuted,
                            modifier = Modifier.size(24.dp)
                        )
                        Spacer(modifier = Modifier.width(12.dp))
                        Column {
                            Text(
                                text = milestone.title,
                                style = MaterialTheme.typography.titleMedium.copy(
                                    fontWeight = if (isAchieved) FontWeight.Bold else FontWeight.Normal
                                ),
                                color = if (isAchieved) MaterialTheme.colorScheme.onSurface else TextMuted
                            )
                            Text(
                                text = "Target: ${format.format(milestone.targetAmount)}",
                                style = MaterialTheme.typography.bodySmall,
                                color = TextMuted
                            )
                        }
                    }

                    if (isAchieved) {
                        Surface(
                            shape = RoundedCornerShape(8.dp),
                            color = AccentGreen.copy(alpha = 0.2f)
                        ) {
                            Text(
                                text = "UNLOCKED",
                                style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold),
                                color = AccentGreen,
                                modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                            )
                        }
                    } else {
                        val pct = ((totalRevenue / milestone.targetAmount) * 100).coerceAtMost(100.0)
                        Text(
                            text = "${pct.toInt()}%",
                            style = MaterialTheme.typography.labelMedium.copy(fontWeight = FontWeight.Bold),
                            color = GoldPrimary
                        )
                    }
                }
            }
        }

        // Vision Board Section
        item {
            Text(
                text = "VISION BOARD",
                style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                color = MaterialTheme.colorScheme.onSurface,
                modifier = Modifier.padding(top = 8.dp)
            )
        }

        items(visionItems) { item ->
            GlassCard(modifier = Modifier.fillMaxWidth()) {
                Column {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = item.title,
                            style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                            color = GoldPrimary
                        )

                        Surface(
                            shape = RoundedCornerShape(6.dp),
                            color = Color(0xFF222533)
                        ) {
                            Text(
                                text = item.targetAmountString,
                                style = MaterialTheme.typography.labelMedium.copy(fontWeight = FontWeight.Bold),
                                color = MaterialTheme.colorScheme.onSurface,
                                modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                            )
                        }
                    }

                    if (item.description.isNotEmpty()) {
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = item.description,
                            style = MaterialTheme.typography.bodySmall,
                            color = TextMuted
                        )
                    }
                }
            }
        }
    }
}
