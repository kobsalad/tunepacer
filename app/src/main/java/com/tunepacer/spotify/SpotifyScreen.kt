package com.tunepacer.spotify

import android.app.Application
import androidx.activity.ComponentActivity
import androidx.activity.compose.LocalActivity
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.viewinterop.AndroidView
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewmodel.compose.viewModel
import org.mozilla.geckoview.GeckoResult
import org.mozilla.geckoview.GeckoRuntime
import org.mozilla.geckoview.GeckoSession
import org.mozilla.geckoview.GeckoSessionSettings
import org.mozilla.geckoview.GeckoView

class SpotifyViewModel(app: Application) : AndroidViewModel(app) {
    private val runtime: GeckoRuntime = GeckoRuntime.create(app)

    val session: GeckoSession = GeckoSession(
        GeckoSessionSettings.Builder()
            .userAgentMode(GeckoSessionSettings.USER_AGENT_MODE_MOBILE)
            .build()
    ).also { s ->
        s.permissionDelegate = object : GeckoSession.PermissionDelegate {
            override fun onContentPermissionRequest(
                session: GeckoSession,
                perm: GeckoSession.PermissionDelegate.ContentPermission
            ): GeckoResult<Int> {
                return if (perm.permission == GeckoSession.PermissionDelegate.PERMISSION_MEDIA_KEY_SYSTEM_ACCESS) {
                    GeckoResult.fromValue(GeckoSession.PermissionDelegate.ContentPermission.VALUE_ALLOW)
                } else {
                    GeckoResult.fromValue(GeckoSession.PermissionDelegate.ContentPermission.VALUE_DENY)
                }
            }
        }
        s.open(runtime)
        s.loadUri("https://open.spotify.com")
    }

    override fun onCleared() {
        session.close()
    }
}

@Composable
fun SpotifyScreen() {
    val activity = LocalActivity.current as ComponentActivity
    val viewModel: SpotifyViewModel = viewModel(viewModelStoreOwner = activity)

    AndroidView(
        modifier = Modifier.fillMaxSize(),
        factory = { context ->
            GeckoView(context).apply {
                @Suppress("UsePropertyAccessSyntax")
                setSession(viewModel.session)
            }
        },
        onRelease = { geckoView ->
            geckoView.releaseSession()
        }
    )
}
