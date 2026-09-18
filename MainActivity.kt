package com.example

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.unit.LayoutDirection
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.ui.components.AppBottomNav
import com.example.ui.components.AppTopBar
import com.example.ui.screens.HistoryScreen
import com.example.ui.screens.HomeScreen
import com.example.ui.screens.ResultScreen
import com.example.ui.screens.SettingsScreen
import com.example.ui.theme.DarkBg
import com.example.ui.theme.MyApplicationTheme
import com.example.ui.viewmodel.AnalysisViewModel
import com.example.ui.viewmodel.AppScreen

class MainActivity : ComponentActivity() {

    private val viewModel: AnalysisViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        setContent {
            val uiState by viewModel.uiState.collectAsStateWithLifecycle()
            val historyList by viewModel.historyReports.collectAsStateWithLifecycle()

            val layoutDirection = if (uiState.language == "fa") LayoutDirection.Rtl else LayoutDirection.Ltr

            CompositionLocalProvider(LocalLayoutDirection provides layoutDirection) {
                MyApplicationTheme(darkTheme = true) {
                    Scaffold(
                        modifier = Modifier.fillMaxSize(),
                        topBar = {
                            AppTopBar(
                                currentScreen = uiState.currentScreen,
                                onBackClick = { viewModel.navigateTo(AppScreen.HOME) },
                                onNavigate = { viewModel.navigateTo(it) }
                            )
                        },
                        bottomBar = {
                            AppBottomNav(
                                currentScreen = uiState.currentScreen,
                                onSelectScreen = { viewModel.navigateTo(it) }
                            )
                        }
                    ) { innerPadding ->
                        Box(
                            modifier = Modifier
                                .fillMaxSize()
                                .background(DarkBg)
                                .padding(innerPadding)
                        ) {
                            when (uiState.currentScreen) {
                                AppScreen.HOME -> {
                                    HomeScreen(
                                        uiState = uiState,
                                        onVideoSelected = { uri, name -> viewModel.setVideo(uri, name) },
                                        onClearVideo = { viewModel.clearVideo() },
                                        onStatementChange = { viewModel.updateStatement(it) },
                                        onLoadScenario = { viewModel.loadSampleScenario(it) },
                                        onStartAnalysis = { viewModel.startAnalysis() }
                                    )
                                }
                                AppScreen.RESULT -> {
                                    ResultScreen(
                                        report = uiState.currentReport,
                                        onNewAnalysis = { viewModel.navigateTo(AppScreen.HOME) }
                                    )
                                }
                                AppScreen.HISTORY -> {
                                    HistoryScreen(
                                        historyList = historyList,
                                        onSelectReport = { viewModel.viewHistoryDetail(it) },
                                        onDeleteItem = { viewModel.deleteHistoryItem(it) },
                                        onClearAll = { viewModel.clearAllHistory() }
                                    )
                                }
                                AppScreen.SETTINGS -> {
                                    SettingsScreen(
                                        currentApiKey = uiState.customApiKey,
                                        currentLanguage = uiState.language,
                                        onSaveApiKey = { viewModel.setCustomApiKey(it) },
                                        onSaveLanguage = { viewModel.setLanguage(it) }
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun Greeting(name: String, modifier: Modifier = Modifier) {
    Text(text = "nightmare: $name", modifier = modifier)
}
