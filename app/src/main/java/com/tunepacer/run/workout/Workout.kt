package com.tunepacer.run.workout

data class Workout(
    var workoutName: String, // Custom name for the workout
    val segments: List<WorkoutSegment> // List of segments in order
)
