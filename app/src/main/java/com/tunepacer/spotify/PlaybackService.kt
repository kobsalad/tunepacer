/*
 * This foreground service is required to allow playback when the app is out of focus. It creates
 * a notification as required. The app currently doesn't use notification permissions, so the
 * notification isn't actually shown.
 */

package com.tunepacer.spotify

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.Service
import android.content.Intent
import android.os.IBinder

class PlaybackService : Service() {
    // Create dummy notification to satisfy requirement of foreground service
    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        val channel = NotificationChannel(
            "playback_channel",
            "Music playback",
            NotificationManager.IMPORTANCE_LOW
        )
        getSystemService(NotificationManager::class.java).createNotificationChannel(channel)

        val notification = Notification.Builder(this, "playback_channel").build()
        startForeground(1, notification)

        return START_NOT_STICKY
    }

    override fun onBind(intent: Intent?): IBinder? = null

    // Trigger shutdown of everything when the user fully closes the app
    override fun onTaskRemoved(rootIntent: Intent?) {
        SpotifyBrowser.shutdown()
        stopSelf()
        android.os.Process.killProcess(android.os.Process.myPid())
    }
}
