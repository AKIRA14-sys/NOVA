package com.nova.browser.engine

import android.content.Context
import android.util.Log
import org.mozilla.geckoview.GeckoRuntime
import org.mozilla.geckoview.GeckoRuntimeSettings

private const val TAG = "GeckoEngineManager"

object GeckoEngineManager {
    @Volatile
    private var runtime: GeckoRuntime? = null

    fun getRuntime(context: Context): GeckoRuntime {
        return runtime ?: synchronized(this) {
            runtime ?: createRuntime(context.applicationContext).also { runtime = it }
        }
    }

    private fun createRuntime(context: Context): GeckoRuntime {
        Log.i(TAG, "Initializing GeckoRuntime singleton...")
        return try {
            val settings = GeckoRuntimeSettings.Builder()
                .build()
            GeckoRuntime.create(context, settings).also {
                Log.i(TAG, "GeckoRuntime successfully created.")
            }
        } catch (e: Exception) {
            Log.e(TAG, "Failed to create GeckoRuntime!", e)
            throw e
        }
    }
}
