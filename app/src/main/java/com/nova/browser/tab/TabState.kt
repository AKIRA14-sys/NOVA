package com.nova.browser.tab

import org.mozilla.geckoview.GeckoSession

data class TabState(
    val id: String,
    val title: String = "New Tab",
    val url: String = "",
    val geckoSession: GeckoSession,
    val isLoading: Boolean = false,
    val progress: Int = 0,
    val canGoBack: Boolean = false,
    val canGoForward: Boolean = false,
    val error: String? = null,
    val isHome: Boolean = true
)
