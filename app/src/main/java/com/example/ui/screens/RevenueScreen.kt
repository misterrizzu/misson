package com.example.ui.screens

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.MonetizationOn
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.MissionViewModel
import com.example.ui.components.GlassCard
import com.example.ui.theme.AccentGreen
import com.example.ui.theme.GoldDark
import com.example.ui.theme.GoldPrimary
import com.example.ui.theme.TextMuted
import java.text.NumberFormat
import java.util.*

@Composable
fun RevenueScreen(viewModel: MissionViewModel) {
    val revenueStats by viewModel.revenueStats.collectAsState()
    val allRevenue by viewModel.allRevenue.collectAsState()
    var showAddDialog by remember { mutableStateOf(false) }

    val format = NumberFormat.getCurrencyInstance(Locale("en", "IN")).apply { maximumFractionDigits = 0 }

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp),
        contentPadding = PaddingValues(top = 16.dp, bottom = 100.dp)
    ) {
        item {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column {
                    Text(
                        text = "REVENUE TRACKER",
                        style = MaterialTheme.typography.labelSmall.copy(
                            letterSpacing = 2.sp,
                            fontWeight = FontWeight.Bold
                        ),
                        color = MaterialTheme.colorScheme.primary
                    )
                    Text(
                        text = "Offline Income Ledger",
                        style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold),
                        color = MaterialTheme.colorScheme.onSurface
                    )
                }

                Button(
                    onClick = { showAddDialog = true },
                    shape = RoundedCornerShape(12.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = MaterialTheme.colorScheme.primary,
                        contentColor = Color.Black
                    )
                ) {
                    Icon(imageVector = Icons.Default.Add, contentDescription = "Add")
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(text = "Add Income", fontWeight = FontWeight.Bold)
                }
            }
        }

        // Summary Cards Grid
        item {
            Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    SummaryStatCard(
                        title = "Today's Revenue",
                        value = format.format(revenueStats.today),
                        modifier = Modifier.weight(1f),
                        valueColor = AccentGreen
                    )
                    SummaryStatCard(
                        title = "This Week",
                        value = format.format(revenueStats.thisWeek),
                        modifier = Modifier.weight(1f),
                        valueColor = MaterialTheme.colorScheme.primary
                    )
                }

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    SummaryStatCard(
                        title = "This Month",
                        value = format.format(revenueStats.thisMonth),
                        modifier = Modifier.weight(1f),
                        valueColor = MaterialTheme.colorScheme.onSurface
                    )
                    SummaryStatCard(
                        title = "Lifetime Revenue",
                        value = format.format(revenueStats.lifetime),
                        modifier = Modifier.weight(1f),
                        valueColor = GoldPrimary
                    )
                }
            }
        }

        // Progress to ₹10L
        item {
            GlassCard(modifier = Modifier.fillMaxWidth()) {
                Text(
                    text = "Progress to ₹10,00,000",
                    style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                    color = MaterialTheme.colorScheme.onSurface
                )
                Spacer(modifier = Modifier.height(8.dp))

                val progress = revenueStats.progressPercentage / 100f
                val animatedProgress by animateFloatAsState(
                    targetValue = progress,
                    animationSpec = tween(1000),
                    label = "revenue_progress"
                )

                LinearProgressIndicator(
                    progress = { animatedProgress },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(12.dp),
                    color = GoldPrimary,
                    trackColor = Color(0xFF222533)
                )

                Spacer(modifier = Modifier.height(6.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(
                        text = "${String.format(Locale.getDefault(), "%.1f", revenueStats.progressPercentage)}% Achieved",
                        style = MaterialTheme.typography.labelMedium,
                        color = GoldPrimary
                    )
                    Text(
                        text = "Target: ₹10,00,000",
                        style = MaterialTheme.typography.labelMedium,
                        color = TextMuted
                    )
                }
            }
        }

        // Animated Chart
        item {
            GlassCard(modifier = Modifier.fillMaxWidth()) {
                Text(
                    text = "Income Flow Graph",
                    style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                    color = MaterialTheme.colorScheme.onSurface
                )
                Spacer(modifier = Modifier.height(16.dp))

                RevenueBarChart(entries = allRevenue.take(7))
            }
        }

        // Recent Income History List Header
        item {
            Text(
                text = "Income History",
                style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                color = MaterialTheme.colorScheme.onSurface,
                modifier = Modifier.padding(top = 8.dp)
            )
        }

        if (allRevenue.isEmpty()) {
            item {
                GlassCard(modifier = Modifier.fillMaxWidth()) {
                    Box(
                        modifier = Modifier.fillMaxWidth().padding(24.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = "No income logged yet. Tap 'Add Income' to start tracking your journey to ₹10 Lakh!",
                            style = MaterialTheme.typography.bodyMedium,
                            color = TextMuted
                        )
                    }
                }
            }
        } else {
            items(allRevenue) { entry ->
                GlassCard(modifier = Modifier.fillMaxWidth()) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Surface(
                                shape = RoundedCornerShape(10.dp),
                                color = GoldPrimary.copy(alpha = 0.15f),
                                modifier = Modifier.size(40.dp)
                            ) {
                                Box(contentAlignment = Alignment.Center) {
                                    Icon(
                                        imageVector = Icons.Default.MonetizationOn,
                                        contentDescription = "Source",
                                        tint = GoldPrimary
                                    )
                                }
                            }
                            Spacer(modifier = Modifier.width(12.dp))
                            Column {
                                Text(
                                    text = entry.source,
                                    style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                                    color = MaterialTheme.colorScheme.onSurface
                                )
                                Text(
                                    text = entry.dateString + if (entry.notes.isNotEmpty()) " • ${entry.notes}" else "",
                                    style = MaterialTheme.typography.bodySmall,
                                    color = TextMuted
                                )
                            }
                        }

                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Text(
                                text = format.format(entry.amount),
                                style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                                color = AccentGreen
                            )
                            IconButton(onClick = { viewModel.deleteRevenue(entry.id) }) {
                                Icon(
                                    imageVector = Icons.Default.Delete,
                                    contentDescription = "Delete",
                                    tint = TextMuted,
                                    modifier = Modifier.size(18.dp)
                                )
                            }
                        }
                    }
                }
            }
        }
    }

    if (showAddDialog) {
        AddIncomeDialog(
            onDismiss = { showAddDialog = false },
            onSave = { source, amount, notes ->
                viewModel.addRevenue(source, amount, notes)
                showAddDialog = false
            }
        )
    }
}

@Composable
private fun SummaryStatCard(
    title: String,
    value: String,
    modifier: Modifier = Modifier,
    valueColor: Color
) {
    GlassCard(modifier = modifier) {
        Text(text = title, style = MaterialTheme.typography.labelSmall, color = TextMuted)
        Spacer(modifier = Modifier.height(4.dp))
        Text(
            text = value,
            style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
            color = valueColor
        )
    }
}

@Composable
private fun RevenueBarChart(entries: List<com.example.data.local.entity.RevenueEntity>) {
    val primaryColor = MaterialTheme.colorScheme.primary
    val maxVal = (entries.maxOfOrNull { it.amount } ?: 1000.0).coerceAtLeast(100.0)

    Canvas(modifier = Modifier.fillMaxWidth().height(120.dp)) {
        val barWidth = (size.width / (entries.size.coerceAtLeast(1) * 2)).coerceIn(16.dp.toPx(), 40.dp.toPx())
        val spacing = (size.width - (barWidth * entries.size)) / (entries.size + 1).coerceAtLeast(1)

        entries.reversed().forEachIndexed { index, entry ->
            val barHeight = ((entry.amount / maxVal) * (size.height - 20.dp.toPx())).toFloat()
            val x = spacing + index * (barWidth + spacing)
            val y = size.height - barHeight

            drawRoundRect(
                brush = Brush.verticalGradient(listOf(GoldPrimary, GoldDark)),
                topLeft = androidx.compose.ui.geometry.Offset(x, y),
                size = Size(barWidth, barHeight),
                cornerRadius = CornerRadius(8f, 8f)
            )
        }
    }
}

@Composable
private fun AddIncomeDialog(
    onDismiss: () -> Unit,
    onSave: (source: String, amount: Double, notes: String) -> Unit
) {
    val sources = listOf("Boxabl", "Trailblazers", "Cantina", "YouTube", "Affiliate", "Other")
    var selectedSource by remember { mutableStateOf(sources[0]) }
    var amountText by remember { mutableStateOf("") }
    var notesText by remember { mutableStateOf("") }
    var expanded by remember { mutableStateOf(false) }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = {
            Text(
                text = "Add Offline Revenue",
                style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold),
                color = MaterialTheme.colorScheme.onSurface
            )
        },
        text = {
            Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                Text(text = "Source", style = MaterialTheme.typography.labelMedium, color = TextMuted)
                Box {
                    Button(
                        onClick = { expanded = true },
                        modifier = Modifier.fillMaxWidth(),
                        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF222533))
                    ) {
                        Text(text = selectedSource, color = Color.White)
                    }
                    DropdownMenu(expanded = expanded, onDismissRequest = { expanded = false }) {
                        sources.forEach { s ->
                            DropdownMenuItem(
                                text = { Text(text = s) },
                                onClick = {
                                    selectedSource = s
                                    expanded = false
                                }
                            )
                        }
                    }
                }

                OutlinedTextField(
                    value = amountText,
                    onValueChange = { amountText = it },
                    label = { Text("Amount (₹)") },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                    modifier = Modifier.fillMaxWidth(),
                    singleLine = true
                )

                OutlinedTextField(
                    value = notesText,
                    onValueChange = { notesText = it },
                    label = { Text("Notes (Optional)") },
                    modifier = Modifier.fillMaxWidth(),
                    singleLine = true
                )
            }
        },
        confirmButton = {
            Button(
                onClick = {
                    val amount = amountText.toDoubleOrNull() ?: 0.0
                    if (amount > 0) {
                        onSave(selectedSource, amount, notesText)
                    }
                },
                colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primary, contentColor = Color.Black)
            ) {
                Text("Save Entry", fontWeight = FontWeight.Bold)
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("Cancel", color = TextMuted)
            }
        },
        containerColor = Color(0xFF141620)
    )
}
