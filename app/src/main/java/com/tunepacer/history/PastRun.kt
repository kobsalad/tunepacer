package com.tunepacer.history

data class PastRun(
    val id: Long, // Primary key
    val workoutName: String? = null, // Null means free run
    val date: Long, // Epoch milliseconds
    val distance: Double, // Meters
    val time: Int, // Seconds
    val cadence: Int // SPM
)
