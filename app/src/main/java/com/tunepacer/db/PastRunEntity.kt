package com.tunepacer.db

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "past_runs")
data class PastRunEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val workoutName: String? = null, // Null indicates a free run
    val date: Long, // Epoch milliseconds
    val distance: Float, // Meters
    val time: Int, // Seconds
    val cadence: Int // Steps per minute
)
