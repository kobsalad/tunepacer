/*
 * Contains an embedded GeckoView of the Spotify web version.
 */

package com.tunepacer.spotify

import android.content.Intent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.viewinterop.AndroidView
import androidx.core.content.ContextCompat
import androidx.lifecycle.viewmodel.compose.viewModel
import org.mozilla.geckoview.GeckoView

@Composable
fun SpotifyScreen() {
    // Start foreground service to allow playback when app is minimized
    val context = LocalContext.current
    LaunchedEffect(Unit) {
        ContextCompat.startForegroundService(
            context,
            Intent(context, PlaybackService::class.java)
        )
    }

    // Create GeckoView inside an AndroidView, using a session stored in a ViewModel
    val viewModel: SpotifyViewModel = viewModel()
    AndroidView(
        modifier = Modifier.fillMaxSize(),
        factory = { context ->
            GeckoView(context).apply {
                @Suppress("UsePropertyAccessSyntax")
                setSession(viewModel.session)
            }
        },
        onRelease = { view ->
            view.releaseSession()
        }
    )
}
