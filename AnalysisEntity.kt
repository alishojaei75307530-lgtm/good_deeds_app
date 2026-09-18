package com.example.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.data.model.AnalysisReport
import com.example.data.model.ContradictionItem

@Entity(tableName = "analysis_history")
data class AnalysisEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val title: String,
    val summary: String,
    val credibilityScore: Int,
    val riskLevel: String,
    val verifiableClaims: List<String>,
    val unverifiableClaims: List<String>,
    val internalContradictions: List<ContradictionItem>,
    val behavioralCues: List<String>,
    val suggestedQuestions: List<String>,
    val rawTranscript: String,
    val videoUri: String?,
    val timestamp: Long
) {
    fun toReport(): AnalysisReport = AnalysisReport(
        id = id,
        title = title,
        summary = summary,
        credibilityScore = credibilityScore,
        riskLevel = riskLevel,
        verifiableClaims = verifiableClaims,
        unverifiableClaims = unverifiableClaims,
        internalContradictions = internalContradictions,
        behavioralCues = behavioralCues,
        suggestedQuestions = suggestedQuestions,
        rawTranscript = rawTranscript,
        videoUri = videoUri,
        timestamp = timestamp
    )

    companion object {
        fun fromReport(report: AnalysisReport): AnalysisEntity = AnalysisEntity(
            id = report.id,
            title = report.title,
            summary = report.summary,
            credibilityScore = report.credibilityScore,
            riskLevel = report.riskLevel,
            verifiableClaims = report.verifiableClaims,
            unverifiableClaims = report.unverifiableClaims,
            internalContradictions = report.internalContradictions,
            behavioralCues = report.behavioralCues,
            suggestedQuestions = report.suggestedQuestions,
            rawTranscript = report.rawTranscript,
            videoUri = report.videoUri,
            timestamp = report.timestamp
        )
    }
}
