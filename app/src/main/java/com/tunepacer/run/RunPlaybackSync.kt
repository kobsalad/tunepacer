package com.tunepacer.run

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import com.tunepacer.Settings
import com.tunepacer.run.workout.Workout
import com.tunepacer.spotify.SpotifyBrowser

@Composable
fun RunPlaybackSync(
    speed: Float,
    cadence: Int,
    workout: Workout? = null,
    workoutSyncWithCadence: Boolean = false
) {
    if (workout != null) {
        LaunchedEffect(workout, workoutSyncWithCadence) {
            val segment = workout.segments.first()
            if (workoutSyncWithCadence) {
                SpotifyBrowser.setTargetBpm(segment.cadence)
            } else {
                SpotifyBrowser.setPlaybackRate(segment.multiplier)
            }
        }
        return
    }

    val adaptive = Settings.syncTypeAdaptive
    val syncWithCadence = Settings.syncWithCadence
    if (adaptive) {
        val basePace = Settings.adaptivePaceBase
        val fastPace = Settings.adaptivePaceFast

        LaunchedEffect(adaptive, syncWithCadence, speed, cadence, basePace, fastPace) {
            if (syncWithCadence) {
                SpotifyBrowser.setTargetBpm(cadence)
            } else {
                SpotifyBrowser.setPlaybackRate(adaptivePlaybackRate(speed, basePace, fastPace))
            }
        }
    } else if (syncWithCadence) {
        val targetCadence = Settings.constantCadenceTarget
        LaunchedEffect(adaptive, syncWithCadence, targetCadence) {
            SpotifyBrowser.setTargetBpm(targetCadence)
        }
    } else {
        val playbackRate = Settings.constantPaceMultiplier
        LaunchedEffect(adaptive, syncWithCadence, playbackRate) {
            SpotifyBrowser.setPlaybackRate(playbackRate)
        }
    }
}

internal fun adaptivePlaybackRate(speed: Float, basePaceSeconds: Int, fastPaceSeconds: Int): Float {
    if (speed <= 0f) return 1f

    val paceSeconds = 1609.344f / speed
    val paceRange = basePaceSeconds - fastPaceSeconds
    if (paceRange <= 0) return if (paceSeconds <= fastPaceSeconds) 1.5f else 1f

    val progress = ((basePaceSeconds - paceSeconds) / paceRange).coerceIn(0f, 1f)
    return 1f + progress * 0.5f
}
