package com.tunepacer

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.Service
import android.content.Context
import android.content.Intent
import android.content.pm.ServiceInfo
import android.os.IBinder
import androidx.core.content.ContextCompat
import com.tunepacer.sensors.Run

class ForegroundService : Service() {
    companion object {
        fun start(context: Context) {
            ContextCompat.startForegroundService(
                context,
                Intent(context, ForegroundService::class.java)
            )
        }
    }

    // Create dummy notification to satisfy requirement of foreground service
    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        val channel = NotificationChannel(
            "foreground_channel",
            "TunePacer is running",
            NotificationManager.IMPORTANCE_LOW
        )
        getSystemService(NotificationManager::class.java).createNotificationChannel(channel)

        val notification = Notification.Builder(this, "foreground_channel").build()
        startForeground(
            1,
            notification,
            ServiceInfo.FOREGROUND_SERVICE_TYPE_LOCATION or
                ServiceInfo.FOREGROUND_SERVICE_TYPE_HEALTH or
                ServiceInfo.FOREGROUND_SERVICE_TYPE_MEDIA_PLAYBACK
        )

        return START_NOT_STICKY
    }

    override fun onBind(intent: Intent?): IBinder? = null

    // Trigger shutdown of everything when the user fully closes the app
    override fun onTaskRemoved(rootIntent: Intent?) {
        Run.stopRun()
        stopSelf()
    }
}
