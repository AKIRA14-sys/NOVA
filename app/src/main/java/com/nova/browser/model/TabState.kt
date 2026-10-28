package com.nova.browser.model

import org.mozilla.geckoview.GeckoSession
import java.util.UUID

data class TabState(
    val id: String = UUID.randomUUID().toString(),
    var url: String = "about:blank",
    var title: String = "New Tab",
    var isLoading: Boolean = false,
    var progress: Int = 0,
    var isHome: Boolean = true,
    var hasError: Boolean = false,
    var errorMessage: String? = null,
    val geckoSession: GeckoSession = GeckoSession()
)
