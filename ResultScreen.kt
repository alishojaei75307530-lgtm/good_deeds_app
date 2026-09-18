package com.example.ui.screens

import android.content.Intent
import androidx.compose.animation.core.animateIntAsState
import androidx.compose.animation.core.tween
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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.CompareArrows
import androidx.compose.material.icons.filled.HelpOutline
import androidx.compose.material.icons.filled.QuestionAnswer
import androidx.compose.material.icons.filled.Share
import androidx.compose.material.icons.filled.VerifiedUser
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
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

@Composable
fun ResultScreen(
    report: AnalysisReport?,
    onNewAnalysis: () -> Unit
) {
    val context = LocalContext.current
    val scrollState = rememberScrollState()

    if (report == null) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(DarkBg),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = "هیچ گزارشی برای نمایش وجود ندارد.",
                color = TextSecondary,
                fontSize = 14.sp
            )
        }
        return
    }

    val score = report.credibilityScore
    val animatedScore by animateIntAsState(
        targetValue = score,
        animationSpec = tween(durationMillis = 1000),
        label = "score_anim"
    )

    val (scoreColor, riskTitle, riskDesc) = when {
        score < 45 -> Triple(RiskHigh, "ریسک فریب بالا (غیرقابل اعتماد)", "روایت دارای تناقضات بارز و نشانه‌های مکرر طفره‌روی کلامی است.")
        score < 72 -> Triple(RiskMedium, "ریسک فریب متوسط (مشکوک)", "برخی ادعاها مبهم بوده و نیازمند بررسی دقیق‌تر با مستندات است.")
        else -> Triple(RiskLow, "ریسک فریب پایین (همگام و منطقی)", "روایت ساختار منسجم داشته و ادعاها تطابق مناسبی با واقعیت دارند.")
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(DarkBg)
            .verticalScroll(scrollState)
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // Top Gauge & Summary Card
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .border(1.dp, Color(0x3300E5FF), RoundedCornerShape(16.dp)),
            colors = CardDefaults.cardColors(containerColor = DarkCard),
            shape = RoundedCornerShape(16.dp)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(18.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = report.title,
                    color = NeonWhite,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold
                )

                Spacer(modifier = Modifier.height(16.dp))

                // Score Circular Meter
                Box(
                    modifier = Modifier.size(130.dp),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator(
                        progress = { 1.0f },
                        color = Color(0x22FFFFFF),
                        strokeWidth = 10.dp,
                        modifier = Modifier.fillMaxSize()
                    )
                    CircularProgressIndicator(
                        progress = { animatedScore / 100f },
                        color = scoreColor,
                        strokeWidth = 10.dp,
                        modifier = Modifier.fillMaxSize()
                    )
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Text(
                            text = "$animatedScore%",
                            color = NeonWhite,
                            fontSize = 28.sp,
                            fontWeight = FontWeight.Bold
                        )
                        Text(
                            text = "شاخص صداقت",
                            color = TextMuted,
                            fontSize = 10.sp
                        )
                    }
                }

                Spacer(modifier = Modifier.height(14.dp))

                // Risk Badge
                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(8.dp))
                        .background(scoreColor.copy(alpha = 0.15f))
                        .border(1.dp, scoreColor, RoundedCornerShape(8.dp))
                        .padding(horizontal = 14.dp, vertical = 6.dp)
                ) {
                    Text(
                        text = riskTitle,
                        color = scoreColor,
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Bold
                    )
                }

                Spacer(modifier = Modifier.height(10.dp))

                Text(
                    text = report.summary,
                    color = TextPrimary,
                    fontSize = 13.sp,
                    lineHeight = 20.sp
                )
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Card 1: Internal Contradictions
        if (report.internalContradictions.isNotEmpty()) {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .border(1.dp, Color(0x33EF4444), RoundedCornerShape(14.dp)),
                colors = CardDefaults.cardColors(containerColor = DarkCard),
                shape = RoundedCornerShape(14.dp)
            ) {
                Column(modifier = Modifier.padding(14.dp)) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            imageVector = Icons.Default.CompareArrows,
                            contentDescription = "Contradictions",
                            tint = RiskHigh,
                            modifier = Modifier.size(20.dp)
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = "تناقض‌های درونی کشف‌شده (${report.internalContradictions.size})",
                            color = NeonWhite,
                            fontWeight = FontWeight.Bold,
                            fontSize = 14.sp
                        )
                    }

                    Spacer(modifier = Modifier.height(10.dp))

                    report.internalContradictions.forEachIndexed { index, item ->
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 4.dp)
                                .clip(RoundedCornerShape(10.dp))
                                .background(DarkCardElevated)
                                .border(1.dp, Color(0x22EF4444), RoundedCornerShape(10.dp))
                                .padding(10.dp)
                        ) {
                            Column {
                                Row(verticalAlignment = Alignment.Top) {
                                    Text(
                                        text = "ادعای الف:",
                                        color = NeonCyan,
                                        fontSize = 11.sp,
                                        fontWeight = FontWeight.Bold
                                    )
                                    Spacer(modifier = Modifier.width(6.dp))
                                    Text(
                                        text = item.statement1,
                                        color = TextPrimary,
                                        fontSize = 12.sp
                                    )
                                }
                                Spacer(modifier = Modifier.height(4.dp))
                                Row(verticalAlignment = Alignment.Top) {
                                    Text(
                                        text = "ادعای ب:",
                                        color = Color(0xFFFF8888),
                                        fontSize = 11.sp,
                                        fontWeight = FontWeight.Bold
                                    )
                                    Spacer(modifier = Modifier.width(6.dp))
                                    Text(
                                        text = item.statement2,
                                        color = TextPrimary,
                                        fontSize = 12.sp
                                    )
                                }
                                Spacer(modifier = Modifier.height(6.dp))
                                Text(
                                    text = "توضیح تناقض: ${item.explanation}",
                                    color = TextMuted,
                                    fontSize = 11.sp,
                                    lineHeight = 16.sp
                                )
                            }
                        }
                    }
                }
            }
            Spacer(modifier = Modifier.height(14.dp))
        }

        // Card 2: Behavioral & Deception Indicators
        if (report.behavioralCues.isNotEmpty()) {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .border(1.dp, Color(0x33F59E0B), RoundedCornerShape(14.dp)),
                colors = CardDefaults.cardColors(containerColor = DarkCard),
                shape = RoundedCornerShape(14.dp)
            ) {
                Column(modifier = Modifier.padding(14.dp)) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            imageVector = Icons.Default.Visibility,
                            contentDescription = "Cues",
                            tint = RiskMedium,
                            modifier = Modifier.size(20.dp)
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = "نشانه‌های کلامی و رفتاری فریب و طفره‌روی",
                            color = NeonWhite,
                            fontWeight = FontWeight.Bold,
                            fontSize = 14.sp
                        )
                    }

                    Spacer(modifier = Modifier.height(10.dp))

                    report.behavioralCues.forEach { cue ->
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 3.dp),
                            verticalAlignment = Alignment.Top
                        ) {
                            Box(
                                modifier = Modifier
                                    .padding(top = 5.dp)
                                    .size(6.dp)
                                    .clip(CircleShape)
                                    .background(RiskMedium)
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                text = cue,
                                color = TextPrimary,
                                fontSize = 12.sp,
                                lineHeight = 18.sp
                            )
                        }
                    }
                }
            }
            Spacer(modifier = Modifier.height(14.dp))
        }

        // Card 3: Verifiable Claims vs Unverifiable Claims
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .border(1.dp, Color(0x33FFFFFF), RoundedCornerShape(14.dp)),
            colors = CardDefaults.cardColors(containerColor = DarkCard),
            shape = RoundedCornerShape(14.dp)
        ) {
            Column(modifier = Modifier.padding(14.dp)) {
                // Verifiable
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        imageVector = Icons.Default.CheckCircle,
                        contentDescription = "Verifiable",
                        tint = RiskLow,
                        modifier = Modifier.size(18.dp)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = "ادعاهای عینی قابل راستی‌آزمایی",
                        color = NeonWhite,
                        fontWeight = FontWeight.Bold,
                        fontSize = 13.sp
                    )
                }

                Spacer(modifier = Modifier.height(6.dp))

                report.verifiableClaims.forEach { claim ->
                    Text(
                        text = "• $claim",
                        color = TextPrimary,
                        fontSize = 12.sp,
                        modifier = Modifier.padding(vertical = 2.dp)
                    )
                }

                Spacer(modifier = Modifier.height(12.dp))

                // Unverifiable
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        imageVector = Icons.Default.HelpOutline,
                        contentDescription = "Unverifiable",
                        tint = Color(0xFFA855F7),
                        modifier = Modifier.size(18.dp)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = "ادعاهای مبهم یا غیرمستند",
                        color = NeonWhite,
                        fontWeight = FontWeight.Bold,
                        fontSize = 13.sp
                    )
                }

                Spacer(modifier = Modifier.height(6.dp))

                report.unverifiableClaims.forEach { claim ->
                    Text(
                        text = "• $claim",
                        color = TextMuted,
                        fontSize = 12.sp,
                        modifier = Modifier.padding(vertical = 2.dp)
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(14.dp))

        // Card 4: Suggested Investigative Questions
        if (report.suggestedQuestions.isNotEmpty()) {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .border(1.dp, Color(0x3300E5FF), RoundedCornerShape(14.dp)),
                colors = CardDefaults.cardColors(containerColor = DarkCard),
                shape = RoundedCornerShape(14.dp)
            ) {
                Column(modifier = Modifier.padding(14.dp)) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            imageVector = Icons.Default.QuestionAnswer,
                            contentDescription = "Questions",
                            tint = NeonCyan,
                            modifier = Modifier.size(20.dp)
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = "سؤالات پیشنهادی برای کشف حقیقت",
                            color = NeonWhite,
                            fontWeight = FontWeight.Bold,
                            fontSize = 14.sp
                        )
                    }

                    Spacer(modifier = Modifier.height(10.dp))

                    report.suggestedQuestions.forEachIndexed { idx, q ->
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 3.dp)
                                .clip(RoundedCornerShape(8.dp))
                                .background(DarkSurface)
                                .padding(horizontal = 10.dp, vertical = 8.dp)
                        ) {
                            Text(
                                text = "${idx + 1}. $q",
                                color = NeonCyan,
                                fontSize = 12.sp,
                                lineHeight = 18.sp,
                                fontWeight = FontWeight.Medium
                            )
                        }
                    }
                }
            }
            Spacer(modifier = Modifier.height(16.dp))
        }

        // Action Buttons: Share & New Analysis
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            OutlinedButton(
                onClick = {
                    val shareText = """
                        گزارش تحلیل فریب nightmare:
                        عنوان: ${report.title}
                        شاخص صداقت: ${report.credibilityScore}% (${report.riskLevel})
                        خلاصه: ${report.summary}
                        تعداد تناقض‌های درونی: ${report.internalContradictions.size}
                    """.trimIndent()
                    val sendIntent = Intent().apply {
                        action = Intent.ACTION_SEND
                        putExtra(Intent.EXTRA_TEXT, shareText)
                        type = "text/plain"
                    }
                    context.startActivity(Intent.createChooser(sendIntent, "اشتراک‌گذاری گزارش nightmare"))
                },
                modifier = Modifier
                    .weight(1f)
                    .height(48.dp)
                    .testTag("share_report_button"),
                colors = ButtonDefaults.outlinedButtonColors(
                    contentColor = NeonWhite
                ),
                border = ButtonDefaults.outlinedButtonBorder.copy(
                    brush = Brush.horizontalGradient(listOf(Color(0x66FFFFFF), Color(0x6600E5FF)))
                ),
                shape = RoundedCornerShape(12.dp)
            ) {
                Icon(
                    imageVector = Icons.Default.Share,
                    contentDescription = "Share",
                    tint = NeonWhite,
                    modifier = Modifier.size(18.dp)
                )
                Spacer(modifier = Modifier.width(6.dp))
                Text("اشتراک‌گذاری", fontSize = 12.sp)
            }

            Button(
                onClick = onNewAnalysis,
                modifier = Modifier
                    .weight(1f)
                    .height(48.dp)
                    .testTag("new_analysis_button"),
                colors = ButtonDefaults.buttonColors(
                    containerColor = NeonWhite,
                    contentColor = DarkBg
                ),
                shape = RoundedCornerShape(12.dp)
            ) {
                Text(
                    text = "تحلیل جدید",
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Bold,
                    color = DarkBg
                )
            }
        }

        Spacer(modifier = Modifier.height(24.dp))
    }
}
