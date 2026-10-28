package com.nova.browser.ui

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.viewinterop.AndroidView
import com.nova.browser.engine.GeckoEngine
import org.mozilla.geckoview.GeckoSession
import org.mozilla.geckoview.GeckoView

@Composable
fun GeckoViewContainer(
    session: GeckoSession,
    modifier: Modifier = Modifier
) {
    AndroidView(
        modifier = modifier,
        factory = { context ->
            val geckoView = GeckoView(context)
            val runtime = GeckoEngine.getRuntime(context)
            if (!session.isOpen) {
                session.open(runtime)
            }
            geckoView.setSession(session)
            geckoView
        },
        update = { geckoView ->
            // Ensure session is properly set if needed without calling releaseSession()
        }
    )
}
