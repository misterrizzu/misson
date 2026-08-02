package com.example.ui.components

import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.*
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.theme.GoldDark
import com.example.ui.theme.GoldPrimary
import java.text.NumberFormat
import java.util.Locale

@Composable
fun LargeProgressRing(
    currentAmount: Double,
    targetAmount: Double = 1000000.0,
    modifier: Modifier = Modifier,
    strokeWidth: Dp = 16.dp,
    ringSize: Dp = 220.dp
) {
    val progressRatio = (currentAmount / targetAmount).coerceIn(0.0, 1.0).toFloat()
    val animatedProgress by animateFloatAsState(
        targetValue = progressRatio,
        animationSpec = tween(durationMillis = 1200, easing = FastOutSlowInEasing),
        label = "ring_progress"
    )

    val primaryColor = MaterialTheme.colorScheme.primary

    Box(
        modifier = modifier.size(ringSize),
        contentAlignment = Alignment.Center
    ) {
        Canvas(modifier = Modifier.fillMaxSize().padding(strokeWidth / 2)) {
            val strokePx = strokeWidth.toPx()
            val radius = (size.minDimension - strokePx) / 2

            // Background Ring Track
            drawArc(
                color = Color(0xFF222533),
                startAngle = 140f,
                sweepAngle = 260f,
                useCenter = false,
                style = Stroke(width = strokePx, cap = StrokeCap.Round)
            )

            // Active Progress Ring
            drawArc(
                brush = Brush.horizontalGradient(
                    colors = listOf(GoldDark, primaryColor, GoldPrimary)
                ),
                startAngle = 140f,
                sweepAngle = 260f * animatedProgress,
                useCenter = false,
                style = Stroke(width = strokePx, cap = StrokeCap.Round)
            )
        }

        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Text(
                text = "MISSION",
                style = MaterialTheme.typography.labelMedium.copy(
                    letterSpacing = 2.sp,
                    fontWeight = FontWeight.Bold
                ),
                color = primaryColor
            )

            Spacer(modifier = Modifier.height(4.dp))

            val format = NumberFormat.getCurrencyInstance(Locale("en", "IN"))
            format.maximumFractionDigits = 0
            val formattedCurrent = format.format(currentAmount)
            val formattedTarget = format.format(targetAmount)

            Text(
                text = formattedCurrent,
                style = MaterialTheme.typography.headlineLarge.copy(
                    fontWeight = FontWeight.ExtraBold,
                    fontSize = 26.sp
                ),
                color = MaterialTheme.colorScheme.onSurface
            )

            Text(
                text = "/ $formattedTarget",
                style = MaterialTheme.typography.bodyMedium.copy(
                    fontWeight = FontWeight.Medium
                ),
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )

            Spacer(modifier = Modifier.height(6.dp))

            val percentageText = String.format(Locale.getDefault(), "%.1f%%", animatedProgress * 100)
            Text(
                text = percentageText,
                style = MaterialTheme.typography.labelLarge.copy(
                    fontWeight = FontWeight.Bold,
                    fontSize = 14.sp
                ),
                color = primaryColor
            )
        }
    }
}
