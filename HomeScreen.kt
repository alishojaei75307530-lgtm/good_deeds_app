package com.example.ui.screens

import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.GraphicEq
import androidx.compose.material.icons.filled.Lightbulb
import androidx.compose.material.icons.filled.Movie
import androidx.compose.material.icons.filled.Psychology
import androidx.compose.material.icons.filled.UploadFile
import androidx.compose.material.icons.filled.VideoFile
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.SuggestionChip
import androidx.compose.material3.SuggestionChipDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.R
import com.example.ui.theme.BorderGlow
import com.example.ui.theme.DarkBg
import com.example.ui.theme.DarkCard
import com.example.ui.theme.DarkCardElevated
import com.example.ui.theme.DarkSurface
import com.example.ui.theme.NeonCyan
import com.example.ui.theme.NeonWhite
import com.example.ui.theme.TextMuted
import com.example.ui.theme.TextPrimary
import com.example.ui.theme.TextSecondary
import com.example.ui.viewmodel.AnalysisUiState

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun HomeScreen(
    uiState: AnalysisUiState,
    onVideoSelected: (Uri, String) -> Unit,
    onClearVideo: () -> Unit,
    onStatementChange: (String) -> Unit,
    onLoadScenario: (Int) -> Unit,
    onStartAnalysis: () -> Unit
) {
    val context = LocalContext.current
    val scrollState = rememberScrollState()

    val videoPickerLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.PickVisualMedia()
    ) { uri: Uri? ->
        if (uri != null) {
            val name = uri.lastPathSegment ?: "selected_video.mp4"
            onVideoSelected(uri, name)
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(DarkBg)
            .verticalScroll(scrollState)
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // Hero Visual Banner
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(150.dp)
                .clip(RoundedCornerShape(16.dp))
                .border(1.dp, BorderGlow, RoundedCornerShape(16.dp))
        ) {
            Image(
                painter = painterResource(id = R.drawable.img_hero_nightmare),
                contentDescription = "Nightmare Deception Scanner",
                contentScale = ContentScale.Crop,
                modifier = Modifier.fillMaxSize()
            )
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(
                        Brush.verticalGradient(
                            colors = listOf(Color.Transparent, Color(0xE6070A0F))
                        )
                    )
            )
            Column(
                modifier = Modifier
                    .align(Alignment.BottomStart)
                    .padding(14.dp)
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Box(
                        modifier = Modifier
                            .size(8.dp)
                            .clip(CircleShape)
                            .background(NeonCyan)
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(
                        text = "AI FORENSIC DECEPTION SCANNER",
                        color = NeonCyan,
                        fontSize = 10.sp,
                        fontWeight = FontWeight.Bold,
                        letterSpacing = 1.sp
                    )
                }
                Text(
                    text = "تشخیص نشانه‌های فریب در گفتار و ویدیو",
                    color = NeonWhite,
                    fontSize = 17.sp,
                    fontWeight = FontWeight.Bold
                )
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Step 1: Video Upload / Selection
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .border(1.dp, Color(0x33FFFFFF), RoundedCornerShape(14.dp)),
            colors = CardDefaults.cardColors(containerColor = DarkCard),
            shape = RoundedCornerShape(14.dp)
        ) {
            Column(modifier = Modifier.padding(14.dp)) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            imageVector = Icons.Default.Movie,
                            contentDescription = "Video",
                            tint = NeonCyan,
                            modifier = Modifier.size(20.dp)
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = "۱. انتخاب ویدیو یا کلیپ صوتی",
                            color = NeonWhite,
                            fontWeight = FontWeight.Bold,
                            fontSize = 14.sp
                        )
                    }

                    Text(
                        text = "MP4 / MKV / MOV",
                        color = TextMuted,
                        fontSize = 10.sp
                    )
                }

                Spacer(modifier = Modifier.height(10.dp))

                if (uiState.selectedVideoUri == null && uiState.videoFileName == null) {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(95.dp)
                            .clip(RoundedCornerShape(10.dp))
                            .background(DarkSurface)
                            .border(1.dp, Color(0x2200E5FF), RoundedCornerShape(10.dp))
                            .clickable {
                                videoPickerLauncher.launch(
                                    PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.VideoOnly)
                                )
                            }
                            .testTag("video_picker_button"),
                        contentAlignment = Alignment.Center
                    ) {
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            Icon(
                                imageVector = Icons.Default.UploadFile,
                                contentDescription = "Upload",
                                tint = NeonWhite,
                                modifier = Modifier.size(28.dp)
                            )
                            Spacer(modifier = Modifier.height(4.dp))
                            Text(
                                text = "برای انتخاب ویدیو از گالری لمس کنید",
                                color = TextPrimary,
                                fontSize = 13.sp,
                                fontWeight = FontWeight.Medium
                            )
                            Text(
                                text = "استفاده از Android Photo Picker ایمن",
                                color = TextMuted,
                                fontSize = 10.sp
                            )
                        }
                    }
                } else {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(10.dp))
                            .background(DarkCardElevated)
                            .border(1.dp, BorderGlow, RoundedCornerShape(10.dp))
                            .padding(horizontal = 12.dp, vertical = 10.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(
                                imageVector = Icons.Default.VideoFile,
                                contentDescription = "File",
                                tint = NeonCyan,
                                modifier = Modifier.size(24.dp)
                            )
                            Spacer(modifier = Modifier.width(10.dp))
                            Column {
                                Text(
                                    text = uiState.videoFileName ?: "ویدیو انتخاب‌شده",
                                    color = NeonWhite,
                                    fontSize = 13.sp,
                                    fontWeight = FontWeight.SemiBold
                                )
                                Text(
                                    text = "آماده استخراج صوت و تحلیل فریم‌ها",
                                    color = NeonCyan,
                                    fontSize = 10.sp
                                )
                            }
                        }

                        IconButton(
                            onClick = onClearVideo,
                            modifier = Modifier.testTag("clear_video_button")
                        ) {
                            Icon(
                                imageVector = Icons.Default.Close,
                                contentDescription = "حذف فایل",
                                tint = TextMuted
                            )
                        }
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(14.dp))

        // Step 2: Statement / Transcript Input
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .border(1.dp, Color(0x33FFFFFF), RoundedCornerShape(14.dp)),
            colors = CardDefaults.cardColors(containerColor = DarkCard),
            shape = RoundedCornerShape(14.dp)
        ) {
            Column(modifier = Modifier.padding(14.dp)) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            imageVector = Icons.Default.GraphicEq,
                            contentDescription = "Transcript",
                            tint = NeonCyan,
                            modifier = Modifier.size(20.dp)
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = "۲. متن گفتار یا روایت اظهارات",
                            color = NeonWhite,
                            fontWeight = FontWeight.Bold,
                            fontSize = 14.sp
                        )
                    }

                    Text(
                        text = "تبدیل صوت به متن",
                        color = TextMuted,
                        fontSize = 10.sp
                    )
                }

                Spacer(modifier = Modifier.height(8.dp))

                OutlinedTextField(
                    value = uiState.statementText,
                    onValueChange = onStatementChange,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(120.dp)
                        .testTag("statement_input_field"),
                    placeholder = {
                        Text(
                            text = "متن ادعاها، گفتگوی ویدیو یا اظهارات سوژه را اینجا وارد کنید (یا از سناریوهای آماده زیر استفاده نمایید)...",
                            color = TextMuted,
                            fontSize = 12.sp
                        )
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

                // Fast Scenario Buttons
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        imageVector = Icons.Default.Lightbulb,
                        contentDescription = "Samples",
                        tint = Color(0xFFF59E0B),
                        modifier = Modifier.size(16.dp)
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(
                        text = "بارگذاری سریع سناریوهای تستی:",
                        color = TextSecondary,
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Medium
                    )
                }

                Spacer(modifier = Modifier.height(6.dp))

                FlowRow(
                    horizontalArrangement = Arrangement.spacedBy(6.dp),
                    verticalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    SuggestionChip(
                        onClick = { onLoadScenario(1) },
                        label = { Text("صحنه جرم (ادعای غیبت)", fontSize = 11.sp) },
                        colors = SuggestionChipDefaults.suggestionChipColors(
                            containerColor = DarkCardElevated,
                            labelColor = NeonWhite
                        ),
                        border = SuggestionChipDefaults.suggestionChipBorder(
                            enabled = true,
                            borderColor = Color(0x33FFFFFF)
                        )
                    )

                    SuggestionChip(
                        onClick = { onLoadScenario(2) },
                        label = { Text("پیشنهاد مالی مشکوک", fontSize = 11.sp) },
                        colors = SuggestionChipDefaults.suggestionChipColors(
                            containerColor = DarkCardElevated,
                            labelColor = NeonWhite
                        ),
                        border = SuggestionChipDefaults.suggestionChipBorder(
                            enabled = true,
                            borderColor = Color(0x33FFFFFF)
                        )
                    )

                    SuggestionChip(
                        onClick = { onLoadScenario(3) },
                        label = { Text("روایت تصادف خودرو", fontSize = 11.sp) },
                        colors = SuggestionChipDefaults.suggestionChipColors(
                            containerColor = DarkCardElevated,
                            labelColor = NeonWhite
                        ),
                        border = SuggestionChipDefaults.suggestionChipBorder(
                            enabled = true,
                            borderColor = Color(0x33FFFFFF)
                        )
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(14.dp))

        // Error message if any
        if (uiState.errorMessage != null) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(8.dp))
                    .background(Color(0x33EF4444))
                    .border(1.dp, Color(0xFFEF4444), RoundedCornerShape(8.dp))
                    .padding(10.dp)
            ) {
                Text(
                    text = uiState.errorMessage,
                    color = Color(0xFFFF8888),
                    fontSize = 12.sp
                )
            }
            Spacer(modifier = Modifier.height(12.dp))
        }

        // Analysis progress indicator or Analyze Button
        if (uiState.isAnalyzing) {
            val infiniteTransition = rememberInfiniteTransition(label = "pulse")
            val angle by infiniteTransition.animateFloat(
                initialValue = 0f,
                targetValue = 360f,
                animationSpec = infiniteRepeatable(
                    animation = tween(1500, easing = LinearEasing),
                    repeatMode = RepeatMode.Restart
                ),
                label = "rotation"
            )

            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .border(1.dp, NeonCyan, RoundedCornerShape(14.dp)),
                colors = CardDefaults.cardColors(containerColor = DarkCardElevated),
                shape = RoundedCornerShape(14.dp)
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(20.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Box(
                        modifier = Modifier.size(60.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        CircularProgressIndicator(
                            color = NeonCyan,
                            strokeWidth = 3.dp,
                            modifier = Modifier
                                .size(56.dp)
                                .rotate(angle)
                        )
                        Icon(
                            imageVector = Icons.Default.Psychology,
                            contentDescription = "Processing",
                            tint = NeonWhite,
                            modifier = Modifier.size(28.dp)
                        )
                    }

                    Spacer(modifier = Modifier.height(14.dp))

                    Text(
                        text = "موتور پردازش فریب در حال کار است...",
                        color = NeonWhite,
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Bold
                    )

                    Spacer(modifier = Modifier.height(6.dp))

                    Text(
                        text = uiState.analysisProgressText,
                        color = NeonCyan,
                        fontSize = 12.sp
                    )
                }
            }
        } else {
            Button(
                onClick = onStartAnalysis,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(54.dp)
                    .border(1.dp, NeonCyan, RoundedCornerShape(14.dp))
                    .testTag("start_analysis_button"),
                colors = ButtonDefaults.buttonColors(
                    containerColor = NeonWhite,
                    contentColor = DarkBg
                ),
                shape = RoundedCornerShape(14.dp)
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        imageVector = Icons.Default.AutoAwesome,
                        contentDescription = "Scan",
                        tint = DarkBg,
                        modifier = Modifier.size(20.dp)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = "شروع تحلیل فریب و تناقض‌ها",
                        fontSize = 15.sp,
                        fontWeight = FontWeight.Bold,
                        color = DarkBg
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(20.dp))
    }
}
