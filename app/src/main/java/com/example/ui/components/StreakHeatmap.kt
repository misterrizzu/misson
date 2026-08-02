package com.example.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.theme.AccentGreen
import com.example.ui.theme.AccentRed
import java.text.SimpleDateFormat
import java.util.*

@Composable
fun StreakHeatmap(
    heatmapData: Map<String, Boolean>, // "YYYY-MM-DD" -> completed
    modifier: Modifier = Modifier,
    daysCount: Int = 28 // 4 weeks
) {
    val datesList = rememberDatesList(daysCount)

    Column(modifier = modifier) {
        Text(
            text = "Calendar Heatmap (Last 28 Days)",
            style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold),
            color = MaterialTheme.colorScheme.onSurface,
            modifier = Modifier.padding(bottom = 8.dp)
        )

        LazyVerticalGrid(
            columns = GridCells.Fixed(7),
            verticalArrangement = Arrangement.spacedBy(6.dp),
            horizontalArrangement = Arrangement.spacedBy(6.dp),
            modifier = Modifier.fillMaxWidth().height(180.dp)
        ) {
            items(datesList.size) { index ->
                val dateItem = datesList[index]
                val status = heatmapData[dateItem.dateString]

                val boxColor = when (status) {
                    true -> AccentGreen
                    false -> AccentRed
                    null -> Color(0xFF1E212F)
                }

                Box(
                    modifier = Modifier
                        .aspectRatio(1f)
                        .clip(RoundedCornerShape(6.dp))
                        .background(boxColor)
                        .border(
                            width = 1.dp,
                            color = if (dateItem.isToday) MaterialTheme.colorScheme.primary else Color(0x22FFFFFF),
                            shape = RoundedCornerShape(6.dp)
                        ),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = dateItem.dayNum,
                        style = MaterialTheme.typography.labelSmall.copy(
                            fontWeight = if (dateItem.isToday) FontWeight.ExtraBold else FontWeight.Normal,
                            fontSize = 11.sp
                        ),
                        color = if (status != null) Color.Black else MaterialTheme.colorScheme.onSurfaceVariant,
                        textAlign = TextAlign.Center
                    )
                }
            }
        }

        Row(
            modifier = Modifier.fillMaxWidth().padding(top = 8.dp),
            horizontalArrangement = Arrangement.End,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(modifier = Modifier.size(10.dp).clip(RoundedCornerShape(2.dp)).background(AccentGreen))
            Text(
                text = " Completed  ",
                style = MaterialTheme.typography.labelSmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
            Spacer(modifier = Modifier.width(8.dp))
            Box(modifier = Modifier.size(10.dp).clip(RoundedCornerShape(2.dp)).background(AccentRed))
            Text(
                text = " Missed",
                style = MaterialTheme.typography.labelSmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    }
}

data class DateGridItem(
    val dateString: String,
    val dayNum: String,
    val isToday: Boolean
)

@Composable
private fun rememberDatesList(count: Int): List<DateGridItem> {
    val list = mutableListOf<DateGridItem>()
    val cal = Calendar.getInstance()
    val sdfKey = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
    val sdfDay = SimpleDateFormat("d", Locale.getDefault())
    val todayKey = sdfKey.format(cal.time)

    // Go back `count - 1` days
    cal.add(Calendar.DAY_OF_YEAR, -(count - 1))

    for (i in 0 until count) {
        val key = sdfKey.format(cal.time)
        val day = sdfDay.format(cal.time)
        list.add(DateGridItem(dateString = key, dayNum = day, isToday = (key == todayKey)))
        cal.add(Calendar.DAY_OF_YEAR, 1)
    }

    return list
}
