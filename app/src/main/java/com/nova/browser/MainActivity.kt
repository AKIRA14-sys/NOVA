package com.nova.browser

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.nova.browser.ui.GeckoViewContainer
import com.nova.browser.ui.components.*
import com.nova.browser.ui.theme.CyberDarkBackground
import com.nova.browser.ui.theme.NeonCyan
import com.nova.browser.ui.theme.NovaBrowserTheme
import com.nova.browser.viewmodel.BrowserViewModel

class MainActivity : ComponentActivity() {

    private val viewModel: BrowserViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        viewModel.initEngine(this)

        setContent {
            NovaBrowserTheme {
                MainBrowserScreen(viewModel = viewModel, context = this)
            }
        }
    }
}

@Composable
fun MainBrowserScreen(
    viewModel: BrowserViewModel,
    context: ComponentActivity
) {
    val activeTab = viewModel.activeTab
    var showSearchSheet by remember { mutableStateOf(false) }
    var showTabsSheet by remember { mutableStateOf(false) }
    var showMenuSheet by remember { mutableStateOf(false) }

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        containerColor = CyberDarkBackground,
        topBar = {
            AddressBar(
                currentUrl = activeTab?.url ?: "about:blank",
                isLoading = activeTab?.isLoading == true,
                progress = activeTab?.progress ?: 0,
                currentProvider = viewModel.currentSearchProvider,
                onNavigate = { input -> viewModel.navigateToInput(input) },
                onRefresh = { viewModel.reload() }
            )
        },
        bottomBar = {
            BottomToolbar(
                canGoBack = true,
                canGoForward = true,
                tabCount = viewModel.tabs.size,
                onBack = { viewModel.goBack() },
                onForward = { viewModel.goForward() },
                onHome = { viewModel.goHome() },
                onTabsClick = { showTabsSheet = true },
                onMenuClick = { showMenuSheet = true }
            )
        }
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            if (activeTab != null) {
                if (activeTab.isHome) {
                    AnimeHomepage(
                        currentProvider = viewModel.currentSearchProvider,
                        onSearchProviderClick = { showSearchSheet = true },
                        onShortcutClick = { url -> viewModel.loadUrl(url) }
                    )
                } else {
                    GeckoViewContainer(
                        session = activeTab.geckoSession,
                        modifier = Modifier.fillMaxSize()
                    )

                    // Loading / Error Overlay
                    if (activeTab.hasError) {
                        Surface(
                            modifier = Modifier
                                .fillMaxSize()
                                .padding(24.dp),
                            color = CyberDarkBackground.copy(alpha = 0.95f)
                        ) {
                            Column(
                                modifier = Modifier.fillMaxSize(),
                                horizontalAlignment = Alignment.CenterHorizontally,
                                verticalArrangement = Arrangement.Center
                            ) {
                                Text(
                                    text = "NETWORK DISRUPTION",
                                    color = MaterialTheme.colorScheme.tertiary,
                                    style = MaterialTheme.typography.titleMedium
                                )
                                Spacer(modifier = Modifier.height(8.dp))
                                Text(
                                    text = activeTab.errorMessage ?: "Unable to establish connection to destination server.",
                                    color = MaterialTheme.colorScheme.onBackground,
                                    style = MaterialTheme.typography.bodyMedium
                                )
                                Spacer(modifier = Modifier.height(16.dp))
                                Button(
                                    onClick = { viewModel.reload() },
                                    colors = ButtonDefaults.buttonColors(containerColor = NeonCyan)
                                ) {
                                    Text("Retry Navigation", color = androidx.compose.ui.graphics.Color.Black)
                                }
                            }
                        }
                    }
                }
            }
        }

        // Bottom Sheets
        if (showSearchSheet) {
            SearchProviderSheet(
                currentProvider = viewModel.currentSearchProvider,
                onSelectProvider = { provider -> viewModel.selectSearchProvider(provider) },
                onDismiss = { showSearchSheet = false }
            )
        }

        if (showTabsSheet) {
            TabManagerSheet(
                tabs = viewModel.tabs,
                activeTabId = viewModel.activeTabId,
                onSelectTab = { id -> viewModel.selectTab(id) },
                onCloseTab = { id -> viewModel.closeTab(id, context) },
                onNewTab = { viewModel.addNewTab(context) },
                onDismiss = { showTabsSheet = false }
            )
        }

        if (showMenuSheet) {
            BrowserMenuSheet(
                onDismiss = { showMenuSheet = false },
                onNewTabClick = { viewModel.addNewTab(context) },
                onReloadClick = { viewModel.reload() },
                onHomeClick = { viewModel.goHome() }
            )
        }
    }
}
