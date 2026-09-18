package com.example.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Key
import androidx.compose.material.icons.filled.Language
import androidx.compose.material.icons.filled.MenuBook
import androidx.compose.material.icons.filled.Security
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
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
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.theme.DarkBg
import com.example.ui.theme.DarkCard
import com.example.ui.theme.DarkCardElevated
import com.example.ui.theme.DarkSurface
import com.example.ui.theme.NeonCyan
import com.example.ui.theme.NeonWhite
import com.example.ui.theme.RiskLow
import com.example.ui.theme.TextMuted
import com.example.ui.theme.TextPrimary
import com.example.ui.theme.TextSecondary

@Composable
fun SettingsScreen(
    currentApiKey: String,
    currentLanguage: String,
    onSaveApiKey: (String) -> Unit,
    onSaveLanguage: (String) -> Unit
) {
    var apiKeyInput by remember(currentApiKey) { mutableStateOf(currentApiKey) }
    var showApiKey by remember { mutableStateOf(false) }
    var keySavedMessage by remember { mutableStateOf(false) }

    val scrollState = rememberScrollState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(DarkBg)
            .verticalScroll(scrollState)
            .padding(16.dp)
    ) {
        // Section: Gemini API Key Configuration
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .border(1.dp, Color(0x33FFFFFF), RoundedCornerShape(14.dp)),
            colors = CardDefaults.cardColors(containerColor = DarkCard),
            shape = RoundedCornerShape(14.dp)
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        imageVector = Icons.Default.Key,
                        contentDescription = "Key",
                        tint = NeonCyan,
                        modifier = Modifier.size(20.dp)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = "تنظیمات کلید Google Gemini API",
                        color = NeonWhite,
                        fontWeight = FontWeight.Bold,
                        fontSize = 14.sp
                    )
                }

                Spacer(modifier = Modifier.height(8.dp))

                Text(
                    text = "می‌توانید کلید اختصاصی خود را وارد نمایید یا در صورت خالی بودن، برنامه از کلید پروژه و الگوریتم تحلیل گفتار محلی استفاده خواهد کرد.",
                    color = TextMuted,
                    fontSize = 11.sp,
                    lineHeight = 16.sp
                )

                Spacer(modifier = Modifier.height(10.dp))

                OutlinedTextField(
                    value = apiKeyInput,
                    onValueChange = {
                        apiKeyInput = it
                        keySavedMessage = false
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .testTag("api_key_input_field"),
                    placeholder = { Text("AIzaSy...", color = TextMuted, fontSize = 12.sp) },
                    visualTransformation = if (showApiKey) VisualTransformation.None else PasswordVisualTransformation(),
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
                    trailingIcon = {
                        IconButton(onClick = { showApiKey = !showApiKey }) {
                            Icon(
                                imageVector = if (showApiKey) Icons.Default.VisibilityOff else Icons.Default.Visibility,
                                contentDescription = "نمایش کلید",
                                tint = TextMuted
                            )
                        }
                    },
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

                Spacer(modifier = Modifier.height(10.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    if (keySavedMessage) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(
                                imageVector = Icons.Default.Check,
                                contentDescription = "Saved",
                                tint = RiskLow,
                                modifier = Modifier.size(16.dp)
                            )
                            Spacer(modifier = Modifier.width(4.dp))
                            Text("ذخیره شد", color = RiskLow, fontSize = 11.sp)
                        }
                    } else {
                        Spacer(modifier = Modifier.width(1.dp))
                    }

                    Button(
                        onClick = {
                            onSaveApiKey(apiKeyInput)
                            keySavedMessage = true
                        },
                        colors = ButtonDefaults.buttonColors(
                            containerColor = NeonWhite,
                            contentColor = DarkBg
                        ),
                        shape = RoundedCornerShape(8.dp),
                        modifier = Modifier.testTag("save_api_key_button")
                    ) {
                        Text("ذخیره کلید", fontSize = 12.sp, fontWeight = FontWeight.Bold)
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(14.dp))

        // Security Warning Card (Mandatory as per skill)
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(12.dp))
                .background(DarkCardElevated)
                .border(1.dp, Color(0x3300E5FF), RoundedCornerShape(12.dp))
                .padding(14.dp)
        ) {
            Row(verticalAlignment = Alignment.Top) {
                Icon(
                    imageVector = Icons.Default.Security,
                    contentDescription = "Security",
                    tint = NeonCyan,
                    modifier = Modifier.size(20.dp)
                )
                Spacer(modifier = Modifier.width(10.dp))
                Column {
                    Text(
                        text = "هشدار امنیتی و حریم خصوصی",
                        color = NeonWhite,
                        fontSize = 13.sp,
                        fontWeight = FontWeight.Bold
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = "فایل‌های APK اندروید ممکن است دیکامپایل شوند. کلیدهای حساس خود را مستقیماً در برنامه‌های عمومی قرار ندهید و فایل APK را در منابع عمومی به اشتراک نگذارید.",
                        color = TextMuted,
                        fontSize = 11.sp,
                        lineHeight = 16.sp
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(14.dp))

        // Section: Language Selection
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .border(1.dp, Color(0x33FFFFFF), RoundedCornerShape(14.dp)),
            colors = CardDefaults.cardColors(containerColor = DarkCard),
            shape = RoundedCornerShape(14.dp)
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        imageVector = Icons.Default.Language,
                        contentDescription = "Language",
                        tint = NeonCyan,
                        modifier = Modifier.size(20.dp)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = "زبان و جهت‌گیری (Language & RTL)",
                        color = NeonWhite,
                        fontWeight = FontWeight.Bold,
                        fontSize = 14.sp
                    )
                }

                Spacer(modifier = Modifier.height(10.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    Button(
                        onClick = { onSaveLanguage("fa") },
                        modifier = Modifier.weight(1f),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = if (currentLanguage == "fa") NeonWhite else DarkSurface,
                            contentColor = if (currentLanguage == "fa") DarkBg else NeonWhite
                        ),
                        shape = RoundedCornerShape(8.dp)
                    ) {
                        Text("فارسی (RTL)", fontWeight = FontWeight.Bold, fontSize = 12.sp)
                    }

                    Button(
                        onClick = { onSaveLanguage("en") },
                        modifier = Modifier.weight(1f),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = if (currentLanguage == "en") NeonWhite else DarkSurface,
                            contentColor = if (currentLanguage == "en") DarkBg else NeonWhite
                        ),
                        shape = RoundedCornerShape(8.dp)
                    ) {
                        Text("English", fontWeight = FontWeight.Bold, fontSize = 12.sp)
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(14.dp))

        // Section: Deception Detection Guide
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .border(1.dp, Color(0x33FFFFFF), RoundedCornerShape(14.dp)),
            colors = CardDefaults.cardColors(containerColor = DarkCard),
            shape = RoundedCornerShape(14.dp)
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        imageVector = Icons.Default.MenuBook,
                        contentDescription = "Guide",
                        tint = NeonCyan,
                        modifier = Modifier.size(20.dp)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = "راهنمای شاخص‌های کشف دروغ و تناقض",
                        color = NeonWhite,
                        fontWeight = FontWeight.Bold,
                        fontSize = 14.sp
                    )
                }

                Spacer(modifier = Modifier.height(8.dp))

                Text(
                    text = "• فاصله زمانی ادعاها: روایت‌های ساختگی معمولاً در نقاط عطف زمانی جهش ناگهانی دارند.\n" +
                        "• تأکید بیش از اندازه بر راستگویی: تکرار عباراتی مانند 'قسم می‌خورم'، 'صادقانه بگم' نشانه افزایش بار شناختی است.\n" +
                        "• تغییر ناگهانی ضمایر: افراد هنگام ارائه بیانات نامطمئن اغلب از ضمایر غیرمستقیم استفاده می‌کنند.\n" +
                        "• سؤالات پیگیرانه معکوس: بازگویی وقایع از انتها به ابتدا مؤثرترین روش کشف شکاف‌های ساختگی است.",
                    color = TextSecondary,
                    fontSize = 11.sp,
                    lineHeight = 18.sp
                )
            }
        }

        Spacer(modifier = Modifier.height(14.dp))

        // About & Version Card
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp),
            contentAlignment = Alignment.Center
        ) {
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Text(
                    text = "nightmare forensic suite v1.0.0",
                    color = TextMuted,
                    fontSize = 11.sp
                )
                Text(
                    text = "Powered by Google Gemini & Room Offline Engine",
                    color = Color(0x55FFFFFF),
                    fontSize = 10.sp
                )
            }
        }

        Spacer(modifier = Modifier.height(20.dp))
    }
}
