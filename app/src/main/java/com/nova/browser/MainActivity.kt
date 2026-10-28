package com.nova.browser

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.nova.browser.tab.TabViewModel
import com.nova.browser.ui.components.AddressBar
import com.nova.browser.ui.components.BottomToolbar
import com.nova.browser.ui.components.GeckoViewContainer
import com.nova.browser.ui.components.HomeScreen
import com.nova.browser.ui.components.TabSwitcherSheet
import com.nova.browser.ui.theme.DeepPurpleDark
import com.nova.browser.ui.theme.NOVABrowserTheme
import com.nova.browser.ui.theme.NeonPink

class MainActivity : ComponentActivity() {

    private val tabViewModel: TabViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            NOVABrowserTheme {
                MainBrowserScreen(viewModel = tabViewModel)
            }
        }
    }
}

@Composable
fun MainBrowserScreen(viewModel: TabViewModel) {
    val tabs by viewModel.tabs.collectAsState()
    val activeTabId by viewModel.activeTabId.collectAsState()
    val currentSearchEngine by viewModel.currentSearchEngine.collectAsState()

    // Reactively derive activeTab whenever tabs or activeTabId changes
    val activeTab = remember(tabs, activeTabId) {
        tabs.find { it.id == activeTabId }
    }

    var showTabSwitcher by remember { mutableStateOf(false) }

    Scaffold(
        topBar = {
            AddressBar(
                currentUrl = activeTab?.url ?: "",
                isLoading = activeTab?.isLoading ?: false,
                progress = activeTab?.progress ?: 0,
                currentSearchEngine = currentSearchEngine,
                onSearchEngineSelected = { viewModel.setSearchEngine(it) },
                onNavigate = { viewModel.navigateTo(it) }
            )
        },
        bottomBar = {
            BottomToolbar(
                canGoBack = activeTab?.canGoBack ?: false,
                canGoForward = activeTab?.canGoForward ?: false,
                tabCount = tabs.size,
                onBack = { viewModel.goBack() },
                onForward = { viewModel.goForward() },
                onRefresh = { viewModel.refresh() },
                onHome = { viewModel.goHome() },
                onOpenTabs = { showTabSwitcher = true }
            )
        },
        containerColor = DeepPurpleDark
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            if (activeTab != null) {
                if (activeTab.isHome) {
                    HomeScreen(
                        currentSearchEngine = currentSearchEngine,
                        onNavigate = { viewModel.navigateTo(it) }
                    )
                } else {
                    GeckoViewContainer(geckoSession = activeTab.geckoSession)

                    if (activeTab.error != null) {
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .background(DeepPurpleDark.copy(alpha = 0.9f))
                                .padding(16.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = activeTab.error ?: "Failed to load page",
                                color = NeonPink,
                                fontSize = 14.sp
                            )
                        }
                    }
                }
            }
        }

        if (showTabSwitcher) {
            TabSwitcherSheet(
                tabs = tabs,
                activeTabId = activeTabId,
                onSelectTab = { viewModel.selectTab(it) },
                onCloseTab = { viewModel.closeTab(it) },
                onNewTab = { viewModel.addNewTab() },
                onDismiss = { showTabSwitcher = false }
            )
        }
    }
}
