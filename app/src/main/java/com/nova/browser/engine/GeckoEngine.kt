package com.nova.browser.engine

import android.content.Context
import org.mozilla.geckoview.GeckoRuntime
import org.mozilla.geckoview.GeckoSession

object GeckoEngine {
    private var runtime: GeckoRuntime? = null

    @Synchronized
    fun getRuntime(context: Context): GeckoRuntime {
        if (runtime == null) {
            runtime = GeckoRuntime.create(context.applicationContext)
        }
        return runtime!!
    }

    fun createSession(): GeckoSession {
        val session = GeckoSession()
        return session
    }
}
