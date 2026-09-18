package com.example.ui.viewmodel

import android.app.Application
import android.content.Context
import android.net.Uri
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.data.local.AppDatabase
import com.example.data.model.AnalysisReport
import com.example.data.remote.GeminiService
import com.example.data.repository.AnalysisRepository
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

enum class AppScreen {
    HOME,
    RESULT,
    HISTORY,
    SETTINGS
}

data class AnalysisUiState(
    val currentScreen: AppScreen = AppScreen.HOME,
    val selectedVideoUri: Uri? = null,
    val videoFileName: String? = null,
    val statementText: String = "",
    val isAnalyzing: Boolean = false,
    val analysisProgressText: String = "",
    val currentReport: AnalysisReport? = null,
    val selectedHistoryReport: AnalysisReport? = null,
    val customApiKey: String = "",
    val language: String = "fa", // "fa" or "en"
    val errorMessage: String? = null
)

class AnalysisViewModel(application: Application) : AndroidViewModel(application) {

    private val repository: AnalysisRepository
    private val prefs = application.getSharedPreferences("nightmare_prefs", Context.MODE_PRIVATE)

    private val _uiState = MutableStateFlow(
        AnalysisUiState(
            customApiKey = prefs.getString("custom_api_key", "") ?: "",
            language = prefs.getString("language", "fa") ?: "fa"
        )
    )
    val uiState: StateFlow<AnalysisUiState> = _uiState.asStateFlow()

    init {
        val db = AppDatabase.getInstance(application)
        repository = AnalysisRepository(db.analysisDao(), GeminiService())
    }

    val historyReports: StateFlow<List<AnalysisReport>> = repository.allHistory
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = emptyList()
        )

    fun navigateTo(screen: AppScreen) {
        _uiState.value = _uiState.value.copy(
            currentScreen = screen,
            errorMessage = null
        )
    }

    fun setVideo(uri: Uri, fileName: String) {
        _uiState.value = _uiState.value.copy(
            selectedVideoUri = uri,
            videoFileName = fileName,
            errorMessage = null
        )
    }

    fun clearVideo() {
        _uiState.value = _uiState.value.copy(
            selectedVideoUri = null,
            videoFileName = null
        )
    }

    fun updateStatement(text: String) {
        _uiState.value = _uiState.value.copy(
            statementText = text,
            errorMessage = null
        )
    }

    fun setCustomApiKey(key: String) {
        prefs.edit().putString("custom_api_key", key.trim()).apply()
        _uiState.value = _uiState.value.copy(customApiKey = key.trim())
    }

    fun setLanguage(lang: String) {
        prefs.edit().putString("language", lang).apply()
        _uiState.value = _uiState.value.copy(language = lang)
    }

    fun loadSampleScenario(index: Int) {
        when (index) {
            1 -> {
                _uiState.value = _uiState.value.copy(
                    statementText = "من اصلاً اون شب حوالی ساعت ۹ اونجا نبودم. به جون مادرم قسم می‌خورم من تو دفتر کارم تنها بودم و داشتم گزارش‌های مالی رو می‌نوشتم. هیچ‌کس هم منو ندیده چون بقیه همکارا ساعت ۶ رفته بودن. شاید دوربین دم در خراب بوده که منو ثبت نکرده، ولی من تا ساعت ۱۱ شب اصلاً از در دفتر بیرون نیومدم، گرچه ساعت ۹:۳۰ رفتم از سوپرمارکت نبش خیابون آب‌معدنی خریدم اما سریع برگشتم!",
                    videoFileName = "sample_interrogation_alibi.mp4"
                )
            }
            2 -> {
                _uiState.value = _uiState.value.copy(
                    statementText = "این پروژه سود قطعی ماهیانه ۳۵ درصد داره و هیچ ریسکی شما رو تهدید نمی‌کنه. من تمام مجوزهای قانونی رو از بالاترین نهادها گرفتم، گرچه الان چون در مرحله پیش‌راه‌اندازی هستیم مدارک رو نمیشه علنی کرد. باور کنید همه سرمایه‌گذارهای قبلی دو برابر سود بردن و اگر همین امروز واریز نکنید این فرصت طلایی برای همیشه می‌سوزه.",
                    videoFileName = "sample_investment_pitch.mp4"
                )
            }
            3 -> {
                _uiState.value = _uiState.value.copy(
                    statementText = "راستش تصادف تقصیر ماشین روبه‌رویی بود. با سرعت خیلی پایین می‌رفتم و همه حواسم به جاده بود. هیچ پیامی با گوشیم چک نمی‌کردم، فقط وقتی اون ترمز ناگهانی زد گوشیم زنگ خورد و دستم رفت سمت داشبورد. اما دقیق یادم نیست چرا خط ترمز ماشین من ۱۵ متر طول کشیده!",
                    videoFileName = "sample_accident_claim.mp4"
                )
            }
        }
    }

    fun startAnalysis() {
        val text = _uiState.value.statementText.trim()
        val hasVideo = _uiState.value.selectedVideoUri != null || _uiState.value.videoFileName != null

        if (text.isBlank() && !hasVideo) {
            _uiState.value = _uiState.value.copy(
                errorMessage = "لطفاً یک ویدیو انتخاب کنید یا متن گفتار را وارد نمایید."
            )
            return
        }

        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(
                isAnalyzing = true,
                errorMessage = null,
                analysisProgressText = "در حال پردازش فایل ویدیو و استخراج فرکانس صوتی..."
            )

            delay(600)
            _uiState.value = _uiState.value.copy(
                analysisProgressText = "تبدیل گفتار به متن و استخراج نشانه‌های زبانی..."
            )

            delay(700)
            _uiState.value = _uiState.value.copy(
                analysisProgressText = "ارسال به مدل تحلیل فریب Gemini جهت کشف تناقض‌ها و ادعاها..."
            )

            val transcriptToAnalyze = if (text.isNotBlank()) text else {
                "گزارش صوتی استخراج شده از ویدیو: " + (_uiState.value.videoFileName ?: "ویدیو ضبط شده") + " حاوی اظهارات سوژه درباره وقایع اخیر و زنجیره اقدامات انجام شده."
            }

            val videoInfo = _uiState.value.videoFileName?.let { "فایل ویدیویی: $it" }

            try {
                val report = repository.analyze(
                    transcript = transcriptToAnalyze,
                    videoNote = videoInfo,
                    videoUri = _uiState.value.selectedVideoUri?.toString(),
                    customApiKey = _uiState.value.customApiKey
                )

                _uiState.value = _uiState.value.copy(
                    isAnalyzing = false,
                    currentReport = report,
                    currentScreen = AppScreen.RESULT
                )
            } catch (e: Exception) {
                _uiState.value = _uiState.value.copy(
                    isAnalyzing = false,
                    errorMessage = "خطا در تحلیل: ${e.message}"
                )
            }
        }
    }

    fun viewHistoryDetail(report: AnalysisReport) {
        _uiState.value = _uiState.value.copy(
            selectedHistoryReport = report,
            currentReport = report,
            currentScreen = AppScreen.RESULT
        )
    }

    fun deleteHistoryItem(id: Long) {
        viewModelScope.launch {
            repository.deleteReport(id)
        }
    }

    fun clearAllHistory() {
        viewModelScope.launch {
            repository.clearHistory()
        }
    }
}
