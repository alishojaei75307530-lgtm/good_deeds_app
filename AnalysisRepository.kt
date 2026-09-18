package com.example.data.repository

import com.example.data.local.AnalysisDao
import com.example.data.local.AnalysisEntity
import com.example.data.model.AnalysisReport
import com.example.data.remote.GeminiService
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class AnalysisRepository(
    private val dao: AnalysisDao,
    private val geminiService: GeminiService
) {

    val allHistory: Flow<List<AnalysisReport>> = dao.getAllHistory().map { entities ->
        entities.map { it.toReport() }
    }

    suspend fun analyze(
        transcript: String,
        videoNote: String?,
        videoUri: String?,
        customApiKey: String?
    ): AnalysisReport {
        val result = geminiService.analyzeContent(transcript, videoNote, customApiKey)
        val finalReport = result.copy(
            videoUri = videoUri,
            rawTranscript = transcript
        )
        val savedId = dao.insert(AnalysisEntity.fromReport(finalReport))
        return finalReport.copy(id = savedId)
    }

    suspend fun getReportById(id: Long): AnalysisReport? {
        return dao.getById(id)?.toReport()
    }

    suspend fun deleteReport(id: Long) {
        dao.deleteById(id)
    }

    suspend fun clearHistory() {
        dao.clearAll()
    }
}
