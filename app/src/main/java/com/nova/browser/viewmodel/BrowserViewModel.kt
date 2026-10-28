package com.nova.browser.viewmodel

import android.content.Context
import android.os.Handler
import android.os.Looper
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.nova.browser.engine.GeckoEngine
import com.nova.browser.model.SearchEngineRegistry
import com.nova.browser.model.SearchProvider
import com.nova.browser.model.TabState
import com.nova.browser.util.UrlParser
import org.mozilla.geckoview.AllowOrDeny
import org.mozilla.geckoview.GeckoResult
import org.mozilla.geckoview.GeckoSession
import org.mozilla.geckoview.WebRequestError

class BrowserViewModel : ViewModel() {

    private val mainHandler = Handler(Looper.getMainLooper())

    private val _tabs = mutableStateListOf<TabState>()
    val tabs: List<TabState> get() = _tabs

    var activeTabId by mutableStateOf<String>("")
        private set

    var currentSearchProvider by mutableStateOf<SearchProvider>(SearchEngineRegistry.defaultProvider)
        private set

    val activeTab: TabState?
        get() = _tabs.find { it.id == activeTabId }

    private fun runOnMainThread(action: () -> Unit) {
        if (Looper.myLooper() == Looper.getMainLooper()) {
            action()
        } else {
            mainHandler.post(action)
        }
    }

    fun initEngine(context: Context) {
        if (_tabs.isEmpty()) {
            addNewTab(context)
        }
    }

    fun selectSearchProvider(provider: SearchProvider) {
        currentSearchProvider = provider
    }

    fun addNewTab(context: Context, initialUrl: String? = null) {
        val runtime = GeckoEngine.getRuntime(context)
        val session = GeckoEngine.createSession()

        val newTab = TabState(
            url = initialUrl ?: "about:blank",
            isHome = initialUrl == null,
            geckoSession = session
        )

        setupNavigationDelegate(newTab)

        if (!session.isOpen) {
            session.open(runtime)
        }

        _tabs.add(newTab)
        activeTabId = newTab.id

        if (initialUrl != null) {
            loadUrl(initialUrl)
        }
    }

    fun closeTab(tabId: String, context: Context) {
        val index = _tabs.indexOfFirst { it.id == tabId }
        if (index == -1) return

        val tabToRemove = _tabs[index]
        tabToRemove.geckoSession.close()
        _tabs.removeAt(index)

        if (_tabs.isEmpty()) {
            addNewTab(context)
        } else if (activeTabId == tabId) {
            val newActiveIndex = if (index >= _tabs.size) _tabs.size - 1 else index
            activeTabId = _tabs[newActiveIndex].id
        }
    }

    fun selectTab(tabId: String) {
        if (_tabs.any { it.id == tabId }) {
            activeTabId = tabId
        }
    }

    fun navigateToInput(input: String) {
        val parsedUrl = UrlParser.parseInput(input, currentSearchProvider)
        if (parsedUrl.isNotEmpty()) {
            loadUrl(parsedUrl)
        }
    }

    fun loadUrl(url: String) {
        val current = activeTab ?: return
        val index = _tabs.indexOfFirst { it.id == current.id }
        if (index != -1) {
            val updated = current.copy(
                url = url,
                isHome = false,
                hasError = false,
                errorMessage = null,
                isLoading = true
            )
            _tabs[index] = updated
            updated.geckoSession.loadUri(url)
        }
    }

    fun goBack() {
        activeTab?.geckoSession?.goBack()
    }

    fun goForward() {
        activeTab?.geckoSession?.goForward()
    }

    fun reload() {
        activeTab?.geckoSession?.reload()
    }

    fun goHome() {
        val current = activeTab ?: return
        val index = _tabs.indexOfFirst { it.id == current.id }
        if (index != -1) {
            val updated = current.copy(
                url = "about:blank",
                title = "New Tab",
                isHome = true,
                isLoading = false,
                hasError = false,
                errorMessage = null
            )
            _tabs[index] = updated
            updated.geckoSession.loadUri("about:blank")
        }
    }

    private fun setupNavigationDelegate(tab: TabState) {
        tab.geckoSession.navigationDelegate = object : GeckoSession.NavigationDelegate {
            override fun onLocationChange(session: GeckoSession, url: String?, permissions: List<GeckoSession.PermissionDelegate.ContentPermission>) {
                runOnMainThread {
                    val index = _tabs.indexOfFirst { it.id == tab.id }
                    if (index != -1 && url != null) {
                        val isHome = url == "about:blank"
                        _tabs[index] = _tabs[index].copy(
                            url = url,
                            isHome = isHome
                        )
                    }
                }
            }

            override fun onLoadError(session: GeckoSession, uri: String?, error: WebRequestError): GeckoResult<String>? {
                runOnMainThread {
                    val index = _tabs.indexOfFirst { it.id == tab.id }
                    if (index != -1) {
                        _tabs[index] = _tabs[index].copy(
                            isLoading = false,
                            hasError = true,
                            errorMessage = error.localizedMessage ?: "Failed to load page"
                        )
                    }
                }
                return null
            }
        }

        tab.geckoSession.progressDelegate = object : GeckoSession.ProgressDelegate {
            override fun onPageStart(session: GeckoSession, url: String) {
                runOnMainThread {
                    val index = _tabs.indexOfFirst { it.id == tab.id }
                    if (index != -1) {
                        _tabs[index] = _tabs[index].copy(
                            isLoading = true,
                            hasError = false,
                            errorMessage = null,
                            url = url,
                            isHome = url == "about:blank"
                        )
                    }
                }
            }

            override fun onPageStop(session: GeckoSession, success: Boolean) {
                runOnMainThread {
                    val index = _tabs.indexOfFirst { it.id == tab.id }
                    if (index != -1) {
                        _tabs[index] = _tabs[index].copy(
                            isLoading = false,
                            progress = 100
                        )
                    }
                }
            }

            override fun onProgressChange(session: GeckoSession, progress: Int) {
                runOnMainThread {
                    val index = _tabs.indexOfFirst { it.id == tab.id }
                    if (index != -1) {
                        _tabs[index] = _tabs[index].copy(
                            progress = progress
                        )
                    }
                }
            }

            override fun onSecurityChange(session: GeckoSession, securityInfo: GeckoSession.ProgressDelegate.SecurityInformation) {}
        }

        tab.geckoSession.contentDelegate = object : GeckoSession.ContentDelegate {
            override fun onTitleChange(session: GeckoSession, title: String?) {
                runOnMainThread {
                    val index = _tabs.indexOfFirst { it.id == tab.id }
                    if (index != -1 && !title.isNullOrEmpty()) {
                        _tabs[index] = _tabs[index].copy(
                            title = title
                        )
                    }
                }
            }
        }
    }
}
