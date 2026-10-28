/*
 * Contains an embedded GeckoView of the Spotify web version.
 */

package com.tunepacer.spotify

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.viewinterop.AndroidView
import org.mozilla.geckoview.GeckoView

@Composable
fun SpotifyScreen() {
    AndroidView(
        modifier = Modifier.fillMaxSize(),
        factory = { context ->
            GeckoView(context).apply {
                @Suppress("UsePropertyAccessSyntax")
                setSession(SpotifyBrowser.session)
            }
        },
        onRelease = { view ->
            view.releaseSession()
        }
    )
}
