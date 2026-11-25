package com.tunepacer.db

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.tunepacer.run.workout.WorkoutSegment

@Entity(tableName = "workouts")
data class WorkoutEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val workoutName: String,
    val segments: List<WorkoutSegment>
)
