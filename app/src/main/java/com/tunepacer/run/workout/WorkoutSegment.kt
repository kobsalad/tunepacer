package com.tunepacer.run.workout

data class WorkoutSegment(
    val distance: Float, // Meters
    val multiplier: Float, // Pace multiplier for Spotify
    val cadence: Int // Target cadence in SPM
)
