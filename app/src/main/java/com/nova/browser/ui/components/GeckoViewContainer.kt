package com.nova.browser.ui.components

import android.view.ViewGroup
import android.widget.FrameLayout
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.viewinterop.AndroidView
import com.nova.browser.ui.theme.DeepPurpleDark
import org.mozilla.geckoview.GeckoSession
import org.mozilla.geckoview.GeckoView

@Composable
fun GeckoViewContainer(
    geckoSession: GeckoSession,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    val geckoView = remember { GeckoView(context) }

    DisposableEffect(geckoSession) {
        geckoView.setSession(geckoSession)
        onDispose {
            geckoView.releaseSession()
        }
    }

    Box(
        modifier = modifier
            .fillMaxSize()
            .background(DeepPurpleDark)
    ) {
        AndroidView(
            factory = {
                geckoView.apply {
                    layoutParams = FrameLayout.LayoutParams(
                        ViewGroup.LayoutParams.MATCH_PARENT,
                        ViewGroup.LayoutParams.MATCH_PARENT
                    )
                }
            },
            modifier = Modifier.fillMaxSize()
        )
    }
}
