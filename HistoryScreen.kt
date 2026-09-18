package com.example.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.DeleteSweep
import androidx.compose.material.icons.filled.History
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.AnalysisReport
import com.example.ui.theme.DarkBg
import com.example.ui.theme.DarkCard
import com.example.ui.theme.DarkCardElevated
import com.example.ui.theme.DarkSurface
import com.example.ui.theme.NeonCyan
import com.example.ui.theme.NeonWhite
import com.example.ui.theme.RiskHigh
import com.example.ui.theme.RiskLow
import com.example.ui.theme.RiskMedium
import com.example.ui.theme.TextMuted
import com.example.ui.theme.TextPrimary
import com.example.ui.theme.TextSecondary
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@Composable
fun HistoryScreen(
    historyList: List<AnalysisReport>,
    onSelectReport: (AnalysisReport) -> Unit,
    onDeleteItem: (Long) -> Unit,
    onClearAll: () -> Unit
) {
    var searchQuery by remember { mutableStateOf("") }
    var showClearDialog by remember { mutableStateOf(false) }

    val filteredList = remember(historyList, searchQuery) {
        if (searchQuery.isBlank()) {
            historyList
        } else {
            historyList.filter {
                it.title.contains(searchQuery, ignoreCase = true) ||
                    it.summary.contains(searchQuery, ignoreCase = true) ||
                    it.rawTranscript.contains(searchQuery, ignoreCase = true)
            }
        }
    }

    val dateFormatter = remember {
        SimpleDateFormat("yyyy/MM/dd HH:mm", Locale.getDefault())
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(DarkBg)
            .padding(16.dp)
    ) {
        // Top Header with Clear All
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(
                    imageVector = Icons.Default.History,
                    contentDescription = "History",
                    tint = NeonCyan,
                    modifier = Modifier.size(22.dp)
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = "پرونده‌های ذخیره‌شده (${historyList.size})",
                    color = NeonWhite,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold
                )
            }

            if (historyList.isNotEmpty()) {
                IconButton(
                    onClick = { showClearDialog = true },
                    modifier = Modifier.testTag("clear_all_history_button")
                ) {
                    Icon(
                        imageVector = Icons.Default.DeleteSweep,
                        contentDescription = "پاک کردن همه",
                        tint = Color(0xFFFF8888)
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(10.dp))

        // Search Field
        OutlinedTextField(
            value = searchQuery,
            onValueChange = { searchQuery = it },
            modifier = Modifier
                .fillMaxWidth()
                .testTag("history_search_input"),
            placeholder = { Text("جستجو در پرونده‌ها...", color = TextMuted, fontSize = 12.sp) },
            leadingIcon = {
                Icon(
                    imageVector = Icons.Default.Search,
                    contentDescription = "Search",
                    tint = TextMuted
                )
            },
            singleLine = true,
            colors = OutlinedTextFieldDefaults.colors(
                focusedTextColor = TextPrimary,
                unfocusedTextColor = TextPrimary,
                focusedBorderColor = NeonCyan,
                unfocusedBorderColor = Color(0x22FFFFFF),
                focusedContainerColor = DarkSurface,
                unfocusedContainerColor = DarkSurface
            ),
            shape = RoundedCornerShape(10.dp)
        )

        Spacer(modifier = Modifier.height(12.dp))

        if (filteredList.isEmpty()) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(32.dp),
                contentAlignment = Alignment.Center
            ) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Icon(
                        imageVector = Icons.Default.History,
                        contentDescription = "Empty",
                        tint = TextMuted,
                        modifier = Modifier.size(48.dp)
                    )
                    Spacer(modifier = Modifier.height(12.dp))
                    Text(
                        text = if (searchQuery.isNotBlank()) "موردی با این عبارت یافت نشد." else "هنوز هیچ تحلیلی ذخیره نشده است.",
                        color = TextSecondary,
                        fontSize = 13.sp
                    )
                    Text(
                        text = "تحلیل‌های شما به صورت آفلاین در دستگاه نگهداری می‌شوند.",
                        color = TextMuted,
                        fontSize = 11.sp
                    )
                }
            }
        } else {
            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                verticalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                items(filteredList, key = { it.id }) { item ->
                    val itemColor = when {
                        item.credibilityScore < 45 -> RiskHigh
                        item.credibilityScore < 72 -> RiskMedium
                        else -> RiskLow
                    }

                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .border(1.dp, Color(0x22FFFFFF), RoundedCornerShape(12.dp))
                            .clickable { onSelectReport(item) }
                            .testTag("history_item_${item.id}"),
                        colors = CardDefaults.cardColors(containerColor = DarkCard),
                        shape = RoundedCornerShape(12.dp)
                    ) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(14.dp),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Column(modifier = Modifier.weight(1f)) {
                                Text(
                                    text = item.title,
                                    color = NeonWhite,
                                    fontSize = 14.sp,
                                    fontWeight = FontWeight.Bold,
                                    maxLines = 1
                                )
                                Spacer(modifier = Modifier.height(4.dp))
                                Text(
                                    text = item.summary,
                                    color = TextMuted,
                                    fontSize = 11.sp,
                                    maxLines = 2,
                                    lineHeight = 16.sp
                                )
                                Spacer(modifier = Modifier.height(6.dp))
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Text(
                                        text = dateFormatter.format(Date(item.timestamp)),
                                        color = TextMuted,
                                        fontSize = 10.sp
                                    )
                                    Spacer(modifier = Modifier.width(10.dp))
                                    Text(
                                        text = "تناقض‌ها: ${item.internalContradictions.size}",
                                        color = NeonCyan,
                                        fontSize = 10.sp
                                    )
                                }
                            }

                            Spacer(modifier = Modifier.width(10.dp))

                            Column(horizontalAlignment = Alignment.End) {
                                Box(
                                    modifier = Modifier
                                        .clip(RoundedCornerShape(6.dp))
                                        .background(itemColor.copy(alpha = 0.15f))
                                        .border(1.dp, itemColor, RoundedCornerShape(6.dp))
                                        .padding(horizontal = 8.dp, vertical = 4.dp)
                                ) {
                                    Text(
                                        text = "${item.credibilityScore}%",
                                        color = itemColor,
                                        fontSize = 12.sp,
                                        fontWeight = FontWeight.Bold
                                    )
                                }

                                Spacer(modifier = Modifier.height(6.dp))

                                IconButton(
                                    onClick = { onDeleteItem(item.id) },
                                    modifier = Modifier.size(32.dp)
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.Delete,
                                        contentDescription = "حذف",
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

        if (showClearDialog) {
            AlertDialog(
                onDismissRequest = { showClearDialog = false },
                containerColor = DarkCardElevated,
                title = {
                    Text(text = "پاک‌سازی تاریخچه", color = NeonWhite, fontWeight = FontWeight.Bold)
                },
                text = {
                    Text(
                        text = "آیا از حذف تمام سوابق و پرونده‌های تحلیل اطمینان دارید؟ این عملیات غیرقابل بازگشت است.",
                        color = TextPrimary,
                        fontSize = 13.sp
                    )
                },
                confirmButton = {
                    TextButton(
                        onClick = {
                            onClearAll()
                            showClearDialog = false
                        }
                    ) {
                        Text(text = "حذف همه", color = Color(0xFFFF8888), fontWeight = FontWeight.Bold)
                    }
                },
                dismissButton = {
                    TextButton(onClick = { showClearDialog = false }) {
                        Text(text = "انصراف", color = TextSecondary)
                    }
                }
            )
        }
    }
}
