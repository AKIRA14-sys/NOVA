package com.nova.browser.ui.components

import android.util.Log
import android.view.ViewGroup
import android.widget.FrameLayout
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.viewinterop.AndroidView
import com.nova.browser.ui.theme.DeepPurpleDark
import org.mozilla.geckoview.GeckoSession
import org.mozilla.geckoview.GeckoView

private const val TAG = "GeckoViewContainer"

@Composable
fun GeckoViewContainer(
    geckoSession: GeckoSession,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .fillMaxSize()
            .background(DeepPurpleDark)
    ) {
        AndroidView(
            factory = { context ->
                Log.d(TAG, "Creating new GeckoView for session: $geckoSession")
                GeckoView(context).apply {
                    layoutParams = FrameLayout.LayoutParams(
                        ViewGroup.LayoutParams.MATCH_PARENT,
                        ViewGroup.LayoutParams.MATCH_PARENT
                    )
                }
            },
            update = { geckoView ->
                // Crucial fix: Only call setSession if the session is not already attached.
                // Recompositions (such as soft keyboard appearance on search bar tap) trigger
                // update; calling setSession on an already-attached session causes GeckoView
                // to throw IllegalStateException and crash.
                if (geckoView.session != geckoSession) {
                    Log.d(TAG, "Attaching session $geckoSession to GeckoView")
                    try {
                        if (geckoView.session != null) {
                            geckoView.releaseSession()
                        }
                        geckoView.setSession(geckoSession)
                    } catch (e: Exception) {
                        Log.e(TAG, "Error setting GeckoSession on GeckoView", e)
                    }
                }
            },
            onRelease = { geckoView ->
                Log.d(TAG, "Releasing GeckoSession from GeckoView")
                try {
                    geckoView.releaseSession()
                } catch (e: Exception) {
                    Log.e(TAG, "Error releasing GeckoSession", e)
                }
            },
            modifier = Modifier.fillMaxSize()
        )
    }
}
