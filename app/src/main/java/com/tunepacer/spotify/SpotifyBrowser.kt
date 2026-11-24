package com.tunepacer.spotify

import android.content.Context
import android.os.Handler
import android.os.Looper
import org.json.JSONObject
import org.mozilla.geckoview.GeckoResult
import org.mozilla.geckoview.GeckoRuntime
import org.mozilla.geckoview.GeckoRuntimeSettings
import org.mozilla.geckoview.GeckoSession
import org.mozilla.geckoview.WebExtension

object SpotifyBrowser {
    // Perform initial setup that doesn't require context
    private lateinit var runtime: GeckoRuntime
    val session = GeckoSession().apply {
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
    }
    private lateinit var port: WebExtension.Port
    private val messageDelegate = object : WebExtension.MessageDelegate {
        override fun onConnect(port: WebExtension.Port) {
            this@SpotifyBrowser.port = port
        }
    }

    private var initStarted = false
    // Perform a one-time initialization for content that requires context
    fun init(applicationContext: Context) {
        if (initStarted) return
        initStarted = true

        runtime = GeckoRuntime.create(
            applicationContext,
            GeckoRuntimeSettings.Builder()
                .remoteDebuggingEnabled(true)
                .build()
        )

        Handler(Looper.getMainLooper()).post {
            session.open(runtime)
            session.loadUri("https://open.spotify.com/")
        }

        // Install SpotSpeeder extension
        runtime.webExtensionController
            .installBuiltIn("resource://android/assets/extensions/spotspeeder/")
            .accept { extension ->
                if (extension != null) {
                    Handler(Looper.getMainLooper()).post {
                        session.webExtensionController.setMessageDelegate(
                            extension, messageDelegate, "tunepacer"
                        )
                    }
                }
            }
    }

    // Set the playback rate of the Spotify browser, for pace mode
    fun setPlaybackRate(playbackRate: Float) {
        if (::port.isInitialized) {
            port.postMessage(JSONObject().apply {
                put("type", "SET_PLAYBACK_RATE")
                put("playbackRate", playbackRate)
            })
        }
    }

    // Set the target BPM of the Spotify browser, for cadence mode
    fun setTargetBpm(targetBpm: Int) {
        if (::port.isInitialized) {
            port.postMessage(JSONObject().apply {
                put("type", "SET_TARGET_BPM")
                put("targetBpm", targetBpm)
            })
        }
    }
}
