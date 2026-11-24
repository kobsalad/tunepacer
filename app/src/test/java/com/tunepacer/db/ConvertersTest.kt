package com.tunepacer.db

import com.tunepacer.run.workout.WorkoutSegment
import org.junit.Assert.assertEquals
import org.junit.Assert.assertThrows
import org.junit.Test

class ConvertersTest {
    private val converters = Converters()

    @Test
    fun workoutSegmentsRoundTrip() {
        val segments = listOf(
            WorkoutSegment(400f, 1.25f, 170),
            WorkoutSegment(800f, 1.5f, 185)
        )

        assertEquals(segments, converters.toWorkoutSegmentList(converters.fromWorkoutSegmentList(segments)))
    }

    @Test
    fun emptyWorkoutSegmentsRoundTrip() {
        assertEquals(emptyList<WorkoutSegment>(), converters.toWorkoutSegmentList(converters.fromWorkoutSegmentList(emptyList())))
    }

    @Test
    fun invalidWorkoutSegmentsFailInsteadOfBeingDropped() {
        assertThrows(IllegalArgumentException::class.java) {
            converters.toWorkoutSegmentList("not valid segment data")
        }
    }
}
