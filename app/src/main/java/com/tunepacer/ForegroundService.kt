package com.tunepacer

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.Service
import android.content.Intent
import android.content.pm.ServiceInfo
import android.os.IBinder
import android.os.Process
import com.tunepacer.gps.Gps
import com.tunepacer.spotify.SpotifyBrowser

class ForegroundService : Service() {
    // Create dummy notification to satisfy requirement of foreground service
    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        val channel = NotificationChannel(
            "foreground_channel",
            "TunePacer running",
            NotificationManager.IMPORTANCE_LOW
        )
        getSystemService(NotificationManager::class.java).createNotificationChannel(channel)

        val notification = Notification.Builder(this, "foreground_channel").build()
        startForeground(
            1,
            notification,
            ServiceInfo.FOREGROUND_SERVICE_TYPE_MEDIA_PLAYBACK or ServiceInfo.FOREGROUND_SERVICE_TYPE_LOCATION
        )

        return START_NOT_STICKY
    }

    override fun onBind(intent: Intent?): IBinder? = null

    // Trigger shutdown of everything when the user fully closes the app
    override fun onTaskRemoved(rootIntent: Intent?) {
        SpotifyBrowser.shutdown()
        Gps.stopRun()
        stopSelf()
        Process.killProcess(Process.myPid())
    }
}
