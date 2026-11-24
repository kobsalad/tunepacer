package com.tunepacer.run.workout

import kotlinx.serialization.Serializable

@Serializable
data class WorkoutSegment(
    val distance: Float, // Meters
    val multiplier: Float, // Pace multiplier for Spotify
    val cadence: Int // Target steps per minute
)
