package com.nova.browser

import android.app.Application
import android.util.Log

private const val TAG = "NOVABrowserApp"

class NOVABrowserApp : Application() {

    override fun onCreate() {
        super.onCreate()
        Log.i(TAG, "Initializing NOVA Browser Application Context...")

        val defaultHandler = Thread.getDefaultUncaughtExceptionHandler()
        Thread.setDefaultUncaughtExceptionHandler { thread, throwable ->
            Log.e(TAG, "Uncaught Exception in Thread [${thread.name}]!", throwable)
            defaultHandler?.uncaughtException(thread, throwable)
        }
    }
}
