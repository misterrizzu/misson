package com.example

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
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
import com.example.ui.components.OpeningQuoteDialog
import com.example.ui.screens.*
import com.example.ui.theme.Mission10LTheme
import com.example.ui.theme.TextMuted

sealed class NavItem(val route: String, val title: String, val icon: androidx.compose.ui.graphics.vector.ImageVector) {
    object Home : NavItem("home", "Home", Icons.Default.Home)
    object Revenue : NavItem("revenue", "Revenue", Icons.Default.AttachMoney)
    object Content : NavItem("content", "Content", Icons.Default.VideoLibrary)
    object Goals : NavItem("goals", "Goals", Icons.Default.Flag)
    object Habits : NavItem("habits", "Habits", Icons.Default.Checklist)
    object Motivation : NavItem("motivation", "Quotes", Icons.Default.FormatQuote)
    object Analytics : NavItem("analytics", "Stats", Icons.Default.BarChart)
    object Badges : NavItem("badges", "Badges", Icons.Default.EmojiEvents)
    object Widgets : NavItem("widgets", "Widgets", Icons.Default.Widgets)
    object Settings : NavItem("settings", "Settings", Icons.Default.Settings)
}

class MainActivity : ComponentActivity() {

    private val viewModel: MissionViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.TIRAMISU) {
            if (checkSelfPermission(android.Manifest.permission.POST_NOTIFICATIONS) != android.content.pm.PackageManager.PERMISSION_GRANTED) {
                requestPermissions(arrayOf(android.Manifest.permission.POST_NOTIFICATIONS), 101)
            }
        }

        setContent {
            val accentTheme by viewModel.accentTheme.collectAsState()
            val showOpeningDialog by viewModel.showOpeningDialog.collectAsState()
            val initialRoute = remember { intent?.getStringExtra("target_route") ?: "home" }
            var currentNavRoute by remember { mutableStateOf(initialRoute) }

            val navItems = listOf(
                NavItem.Home,
                NavItem.Revenue,
                NavItem.Content,
                NavItem.Goals,
                NavItem.Habits,
                NavItem.Motivation,
                NavItem.Analytics,
                NavItem.Badges,
                NavItem.Widgets,
                NavItem.Settings
            )

            Mission10LTheme(darkTheme = true, accentTheme = accentTheme) {
                Scaffold(
                    modifier = Modifier.fillMaxSize(),
                    containerColor = MaterialTheme.colorScheme.background,
                    topBar = {
                        // Top persistent reminder banner: "Mission First."
                        Surface(
                            color = Color(0xFF0F111A),
                            border = androidx.compose.foundation.BorderStroke(0.5.dp, MaterialTheme.colorScheme.outline.copy(alpha = 0.2f))
                        ) {
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .statusBarsPadding()
                                    .padding(horizontal = 16.dp, vertical = 8.dp),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Icon(
                                        imageVector = Icons.Default.Shield,
                                        contentDescription = "Mission Logo",
                                        tint = MaterialTheme.colorScheme.primary,
                                        modifier = Modifier.size(20.dp)
                                    )
                                    Spacer(modifier = Modifier.width(8.dp))
                                    Text(
                                        text = "MISSION 10L",
                                        style = MaterialTheme.typography.titleMedium.copy(
                                            fontWeight = FontWeight.ExtraBold,
                                            letterSpacing = 1.5.sp
                                        ),
                                        color = MaterialTheme.colorScheme.primary
                                    )
                                }

                                Surface(
                                    shape = androidx.compose.foundation.shape.RoundedCornerShape(12.dp),
                                    color = MaterialTheme.colorScheme.primary.copy(alpha = 0.15f)
                                ) {
                                    Text(
                                        text = "MISSION FIRST",
                                        style = MaterialTheme.typography.labelSmall.copy(
                                            fontWeight = FontWeight.Bold,
                                            letterSpacing = 1.sp
                                        ),
                                        color = MaterialTheme.colorScheme.primary,
                                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                                    )
                                }
                            }
                        }
                    },
                    bottomBar = {
                        ScrollableTabRow(
                            selectedTabIndex = navItems.indexOfFirst { it.route == currentNavRoute }.coerceAtLeast(0),
                            containerColor = Color(0xFF0D0E14),
                            contentColor = MaterialTheme.colorScheme.primary,
                            edgePadding = 8.dp,
                            modifier = Modifier.navigationBarsPadding()
                        ) {
                            navItems.forEach { item ->
                                val isSelected = currentNavRoute == item.route
                                Tab(
                                    selected = isSelected,
                                    onClick = { currentNavRoute = item.route },
                                    icon = {
                                        Icon(
                                            imageVector = item.icon,
                                            contentDescription = item.title,
                                            tint = if (isSelected) MaterialTheme.colorScheme.primary else TextMuted
                                        )
                                    },
                                    text = {
                                        Text(
                                            text = item.title,
                                            fontSize = 10.sp,
                                            fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal,
                                            color = if (isSelected) MaterialTheme.colorScheme.primary else TextMuted
                                        )
                                    }
                                )
                            }
                        }
                    }
                ) { innerPadding ->
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(innerPadding)
                    ) {
                        when (currentNavRoute) {
                            NavItem.Home.route -> HomeScreen(viewModel = viewModel)
                            NavItem.Revenue.route -> RevenueScreen(viewModel = viewModel)
                            NavItem.Content.route -> ContentScreen(viewModel = viewModel)
                            NavItem.Goals.route -> GoalsVisionScreen(viewModel = viewModel)
                            NavItem.Habits.route -> HabitsJournalScreen(viewModel = viewModel)
                            NavItem.Motivation.route -> MotivationScreen(viewModel = viewModel)
                            NavItem.Analytics.route -> AnalyticsScreen(viewModel = viewModel)
                            NavItem.Badges.route -> AchievementsScreen(viewModel = viewModel)
                            NavItem.Widgets.route -> WidgetsStudioScreen(viewModel = viewModel)
                            NavItem.Settings.route -> SettingsScreen(viewModel = viewModel)
                        }

                        if (showOpeningDialog) {
                            OpeningQuoteDialog(onDismiss = { viewModel.dismissOpeningDialog() })
                        }
                    }
                }
            }
        }
    }
}
