package com.tunepacer.db

import androidx.room.TypeConverter
import com.tunepacer.run.workout.WorkoutSegment
import kotlinx.serialization.json.Json

class Converters {
    @TypeConverter
    fun fromWorkoutSegmentList(segments: List<WorkoutSegment>): String =
        Json.encodeToString(segments)

    @TypeConverter
    fun toWorkoutSegmentList(data: String): List<WorkoutSegment> =
        Json.decodeFromString(data)
}
