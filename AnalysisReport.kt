package com.example.data.model

data class ContradictionItem(
    val statement1: String,
    val statement2: String,
    val explanation: String
)

data class AnalysisReport(
    val id: Long = 0,
    val title: String,
    val summary: String,
    val credibilityScore: Int, // 0 - 100 (higher means more credible/less deceit)
    val riskLevel: String, // "LOW", "MEDIUM", "HIGH"
    val verifiableClaims: List<String>,
    val unverifiableClaims: List<String>,
    val internalContradictions: List<ContradictionItem>,
    val behavioralCues: List<String>,
    val suggestedQuestions: List<String>,
    val rawTranscript: String,
    val videoUri: String? = null,
    val timestamp: Long = System.currentTimeMillis()
)
