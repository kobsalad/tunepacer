package com.tunepacer.run.workout

data class WorkoutSegment(
    val distance: Float, // Meters
    val multiplier: Float, // Speed multiplier for Spotify
    val tempo: Int // BPM for Spotify
)
