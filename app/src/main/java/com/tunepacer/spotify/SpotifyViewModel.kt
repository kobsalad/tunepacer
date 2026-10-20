/*
 * This ViewModel stores the Spotify web session, which needs to be restored if the GeckoView is
 * decomposed and then recomposed.
 */

package com.tunepacer.spotify

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import org.mozilla.geckoview.GeckoResult
import org.mozilla.geckoview.GeckoSession

class SpotifyViewModel(application: Application) : AndroidViewModel(application) {
    val session = GeckoSession().apply {
        // Allow DRM permissions. No other GeckoView settings need to be changed because GeckoView
        // has them set as default.
        permissionDelegate = object : GeckoSession.PermissionDelegate {
            override fun onContentPermissionRequest(
                session: GeckoSession,
                perm: GeckoSession.PermissionDelegate.ContentPermission
            ): GeckoResult<Int>? {
                return if (perm.permission == GeckoSession.PermissionDelegate.PERMISSION_MEDIA_KEY_SYSTEM_ACCESS) {
                    GeckoResult.fromValue(GeckoSession.PermissionDelegate.ContentPermission.VALUE_ALLOW)
                } else {
                    GeckoResult.fromValue(GeckoSession.PermissionDelegate.ContentPermission.VALUE_DENY)
                }
            }
        }

        open(GeckoRuntimeHolder.get(application))
        loadUri("https://open.spotify.com")
    }

    override fun onCleared() {
        session.close()
    }
}
