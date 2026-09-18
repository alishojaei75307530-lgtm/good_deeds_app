package com.example.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.History
import androidx.compose.material.icons.filled.Psychology
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.theme.DarkCard
import com.example.ui.theme.DarkSurface
import com.example.ui.theme.NeonCyan
import com.example.ui.theme.NeonWhite
import com.example.ui.theme.TextMuted
import com.example.ui.viewmodel.AppScreen

@Composable
fun AppTopBar(
    currentScreen: AppScreen,
    onBackClick: () -> Unit,
    onNavigate: (AppScreen) -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .background(DarkSurface)
            .border(
                width = 1.dp,
                brush = Brush.verticalGradient(
                    colors = listOf(Color(0x3300E5FF), Color.Transparent)
                ),
                shape = androidx.compose.ui.graphics.RectangleShape
            )
            .padding(horizontal = 16.dp, vertical = 12.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                if (currentScreen == AppScreen.RESULT) {
                    IconButton(
                        onClick = onBackClick,
                        modifier = Modifier
                            .testTag("topbar_back_button")
                            .size(40.dp)
                    ) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "بازگشت",
                            tint = NeonWhite
                        )
                    }
                    Spacer(modifier = Modifier.width(8.dp))
                } else {
                    Box(
                        modifier = Modifier
                            .size(36.dp)
                            .clip(CircleShape)
                            .background(DarkCard)
                            .border(1.dp, NeonCyan, CircleShape),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Default.Psychology,
                            contentDescription = "Nightmare Logo",
                            tint = NeonCyan,
                            modifier = Modifier.size(22.dp)
                        )
                    }
                    Spacer(modifier = Modifier.width(10.dp))
                }

                Column {
                    Text(
                        text = "nightmare",
                        color = NeonWhite,
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold,
                        letterSpacing = 1.sp
                    )
                    Text(
                        text = when (currentScreen) {
                            AppScreen.HOME -> "تحلیل هوشمند فریب در گفتار و ویدیو"
                            AppScreen.RESULT -> "گزارش جامع راستی‌آزمایی"
                            AppScreen.HISTORY -> "تاریخچه پرونده‌های تحلیل"
                            AppScreen.SETTINGS -> "تنظیمات و راهنما"
                        },
                        color = TextMuted,
                        fontSize = 11.sp
                    )
                }
            }

            Row {
                if (currentScreen != AppScreen.HISTORY) {
                    IconButton(
                        onClick = { onNavigate(AppScreen.HISTORY) },
                        modifier = Modifier.testTag("topbar_history_button")
                    ) {
                        Icon(
                            imageVector = Icons.Default.History,
                            contentDescription = "تاریخچه",
                            tint = NeonWhite
                        )
                    }
                }
                if (currentScreen != AppScreen.SETTINGS) {
                    IconButton(
                        onClick = { onNavigate(AppScreen.SETTINGS) },
                        modifier = Modifier.testTag("topbar_settings_button")
                    ) {
                        Icon(
                            imageVector = Icons.Default.Settings,
                            contentDescription = "تنظیمات",
                            tint = NeonWhite
                        )
                    }
                }
            }
        }
    }
}
