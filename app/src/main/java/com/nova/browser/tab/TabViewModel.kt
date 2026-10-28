package com.nova.browser.tab

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import com.nova.browser.engine.GeckoEngineManager
import com.nova.browser.search.SearchEngine
import com.nova.browser.search.SearchEngineRegistry
import com.nova.browser.utils.UrlUtils
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import org.mozilla.geckoview.GeckoResult
import org.mozilla.geckoview.GeckoSession
import org.mozilla.geckoview.WebRequestError
import java.util.UUID

class TabViewModel(application: Application) : AndroidViewModel(application) {

    private val runtime = GeckoEngineManager.getRuntime(application)

    private val _tabs = MutableStateFlow<List<TabState>>(emptyList())
    val tabs: StateFlow<List<TabState>> = _tabs.asStateFlow()

    private val _activeTabId = MutableStateFlow<String?>(null)
    val activeTabId: StateFlow<String?> = _activeTabId.asStateFlow()

    private val _currentSearchEngine = MutableStateFlow<SearchEngine>(SearchEngineRegistry.DEFAULT)
    val currentSearchEngine: StateFlow<SearchEngine> = _currentSearchEngine.asStateFlow()

    val activeTab: TabState?
        get() = _tabs.value.find { it.id == _activeTabId.value }

    init {
        // Create initial home tab
        addNewTab()
    }

    fun setSearchEngine(searchEngine: SearchEngine) {
        _currentSearchEngine.value = searchEngine
    }

    fun addNewTab(initialUrl: String = ""): String {
        val tabId = UUID.randomUUID().toString()
        val session = GeckoSession()

        session.open(runtime)

        val isHome = initialUrl.isEmpty()
        val title = if (isHome) "NOVA Home" else initialUrl

        val newTab = TabState(
            id = tabId,
            title = title,
            url = initialUrl,
            geckoSession = session,
            isHome = isHome
        )

        setupSessionListeners(tabId, session)

        _tabs.update { it + newTab }
        _activeTabId.value = tabId

        if (initialUrl.isNotEmpty()) {
            val targetUrl = UrlUtils.parseInput(initialUrl, _currentSearchEngine.value)
            session.loadUri(targetUrl)
        }

        return tabId
    }

    fun selectTab(tabId: String) {
        if (_tabs.value.any { it.id == tabId }) {
            _activeTabId.value = tabId
        }
    }

    fun closeTab(tabId: String) {
        val currentList = _tabs.value
        val targetTab = currentList.find { it.id == tabId } ?: return

        targetTab.geckoSession.close()

        val updatedList = currentList.filter { it.id != tabId }

        if (updatedList.isEmpty()) {
            _tabs.value = emptyList()
            _activeTabId.value = null
            // Always keep at least one tab open
            addNewTab()
        } else {
            _tabs.value = updatedList
            if (_activeTabId.value == tabId) {
                _activeTabId.value = updatedList.last().id
            }
        }
    }

    fun navigateTo(input: String) {
        val activeId = _activeTabId.value ?: return
        val parsedUrl = UrlUtils.parseInput(input, _currentSearchEngine.value)
        if (parsedUrl.isEmpty()) return

        updateTab(activeId) { tab ->
            tab.geckoSession.loadUri(parsedUrl)
            tab.copy(
                url = parsedUrl,
                title = parsedUrl,
                isLoading = true,
                error = null,
                isHome = false
            )
        }
    }

    fun goBack() {
        activeTab?.let { tab ->
            tab.geckoSession.goBack()
        }
    }

    fun goForward() {
        activeTab?.let { tab ->
            tab.geckoSession.goForward()
        }
    }

    fun refresh() {
        activeTab?.let { tab ->
            if (tab.isHome) {
                // If home, re-trigger home view state
                updateTab(tab.id) { it.copy(isLoading = false, error = null) }
            } else {
                tab.geckoSession.reload()
            }
        }
    }

    fun goHome() {
        val activeId = _activeTabId.value ?: return
        updateTab(activeId) { tab ->
            tab.copy(
                url = "",
                title = "NOVA Home",
                isLoading = false,
                progress = 0,
                error = null,
                isHome = true
            )
        }
    }

    private fun setupSessionListeners(tabId: String, session: GeckoSession) {
        session.progressDelegate = object : GeckoSession.ProgressDelegate {
            override fun onPageStart(session: GeckoSession, url: String) {
                updateTab(tabId) { tab ->
                    tab.copy(
                        url = url,
                        isLoading = true,
                        progress = 0,
                        error = null,
                        isHome = false
                    )
                }
            }

            override fun onPageStop(session: GeckoSession, success: Boolean) {
                updateTab(tabId) { tab ->
                    tab.copy(isLoading = false, progress = 100)
                }
            }

            override fun onProgressChange(session: GeckoSession, progress: Int) {
                updateTab(tabId) { tab ->
                    tab.copy(progress = progress)
                }
            }
        }

        session.navigationDelegate = object : GeckoSession.NavigationDelegate {
            override fun onLocationChange(session: GeckoSession, url: String?, perms: List<GeckoSession.PermissionDelegate.ContentPermission>) {
                if (!url.isNullOrEmpty()) {
                    updateTab(tabId) { tab ->
                        tab.copy(url = url, isHome = false)
                    }
                }
            }

            override fun onCanGoBack(session: GeckoSession, canGoBack: Boolean) {
                updateTab(tabId) { tab ->
                    tab.copy(canGoBack = canGoBack)
                }
            }

            override fun onCanGoForward(session: GeckoSession, canGoForward: Boolean) {
                updateTab(tabId) { tab ->
                    tab.copy(canGoForward = canGoForward)
                }
            }

            override fun onLoadError(session: GeckoSession, uri: String?, error: WebRequestError): GeckoResult<String>? {
                val errorMsg = "Failed to load page (${error.code})"
                updateTab(tabId) { tab ->
                    tab.copy(isLoading = false, error = errorMsg)
                }
                return super.onLoadError(session, uri, error)
            }
        }

        session.contentDelegate = object : GeckoSession.ContentDelegate {
            override fun onTitleChange(session: GeckoSession, title: String?) {
                if (!title.isNullOrEmpty()) {
                    updateTab(tabId) { tab ->
                        tab.copy(title = title)
                    }
                }
            }
        }
    }

    private fun updateTab(tabId: String, transform: (TabState) -> TabState) {
        _tabs.update { list ->
            list.map { tab ->
                if (tab.id == tabId) transform(tab) else tab
            }
        }
    }

    override fun onCleared() {
        super.onCleared()
        _tabs.value.forEach { tab ->
            tab.geckoSession.close()
        }
    }
}
