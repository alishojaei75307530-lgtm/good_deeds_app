package com.example.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.History
import androidx.compose.material.icons.filled.PlayCircle
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.theme.DarkSurface
import com.example.ui.theme.NeonCyan
import com.example.ui.theme.NeonWhite
import com.example.ui.theme.TextMuted
import com.example.ui.viewmodel.AppScreen

@Composable
fun AppBottomNav(
    currentScreen: AppScreen,
    onSelectScreen: (AppScreen) -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(DarkSurface)
            .border(
                width = 1.dp,
                color = Color(0x22FFFFFF)
            )
            .navigationBarsPadding()
            .padding(vertical = 4.dp),
        horizontalArrangement = Arrangement.SpaceAround
    ) {
        NavigationBarItem(
            selected = currentScreen == AppScreen.HOME,
            onClick = { onSelectScreen(AppScreen.HOME) },
            icon = {
                Icon(
                    imageVector = Icons.Default.PlayCircle,
                    contentDescription = "اسکنر ویدیو"
                )
            },
            label = { Text("تحلیل ویدیو", fontSize = 11.sp) },
            modifier = Modifier.testTag("nav_home_tab"),
            colors = NavigationBarItemDefaults.colors(
                selectedIconColor = NeonWhite,
                selectedTextColor = NeonCyan,
                unselectedIconColor = TextMuted,
                unselectedTextColor = TextMuted,
                indicatorColor = Color(0x2200E5FF)
            )
        )

        NavigationBarItem(
            selected = currentScreen == AppScreen.HISTORY,
            onClick = { onSelectScreen(AppScreen.HISTORY) },
            icon = {
                Icon(
                    imageVector = Icons.Default.History,
                    contentDescription = "تاریخچه"
                )
            },
            label = { Text("تاریخچه", fontSize = 11.sp) },
            modifier = Modifier.testTag("nav_history_tab"),
            colors = NavigationBarItemDefaults.colors(
                selectedIconColor = NeonWhite,
                selectedTextColor = NeonCyan,
                unselectedIconColor = TextMuted,
                unselectedTextColor = TextMuted,
                indicatorColor = Color(0x2200E5FF)
            )
        )

        NavigationBarItem(
            selected = currentScreen == AppScreen.SETTINGS,
            onClick = { onSelectScreen(AppScreen.SETTINGS) },
            icon = {
                Icon(
                    imageVector = Icons.Default.Settings,
                    contentDescription = "تنظیمات"
                )
            },
            label = { Text("تنظیمات", fontSize = 11.sp) },
            modifier = Modifier.testTag("nav_settings_tab"),
            colors = NavigationBarItemDefaults.colors(
                selectedIconColor = NeonWhite,
                selectedTextColor = NeonCyan,
                unselectedIconColor = TextMuted,
                unselectedTextColor = TextMuted,
                indicatorColor = Color(0x2200E5FF)
            )
        )
    }
}
