package com.tunepacer.run.workout

import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.tunepacer.run.RunPlaybackSync
import com.tunepacer.run.freerun.SettingSelector
import com.tunepacer.run.freerun.StatRow
import com.tunepacer.sensors.Gps
import com.tunepacer.sensors.Pedometer
import com.tunepacer.sensors.Run
import com.tunepacer.ui.util.formatCadence
import com.tunepacer.ui.util.formatDistance
import com.tunepacer.ui.util.formatPace
import com.tunepacer.ui.util.formatTime
import com.tunepacer.ui.util.metersToMiles

@Composable
fun WorkoutStatsScreen(
    workout: Workout? = null,
    syncWithCadence: Boolean = false,
    onSyncWithCadenceChange: (Boolean) -> Unit = {},
    onEndRun: () -> Unit
) {
    val currentWorkout = workout ?: Workout(
        "5k Tempo",
        listOf(
            WorkoutSegment(1600f, 1.1f, 170),
            WorkoutSegment(5000f, 1.5f, 185),
            WorkoutSegment(1600f, 1.1f, 170)
        )
    )

    // Placeholder data
    val time by Run.time.collectAsState()
    val speed by Gps.speed.collectAsState()
    val distance by Gps.distance.collectAsState()
    val cadence by Pedometer.cadence.collectAsState()
    val steps by Pedometer.steps.collectAsState()
    val totalDistance = currentWorkout.segments.sumOf { it.distance.toDouble() }.toFloat()
    val currentSegmentIndex = activeWorkoutSegmentIndex(currentWorkout.segments, distance)

    currentWorkout.segments.getOrNull(currentSegmentIndex)?.let { activeSegment ->
        RunPlaybackSync(
            speed = speed,
            cadence = cadence,
            workoutSegment = activeSegment,
            workoutSyncWithCadence = syncWithCadence
        )
    }

    LaunchedEffect(currentSegmentIndex) {
        if (currentSegmentIndex == currentWorkout.segments.size) onEndRun()
    }

    Column(
        verticalArrangement = Arrangement.spacedBy(16.dp),
        modifier = Modifier
            .fillMaxHeight()
            .padding(32.dp, 16.dp)
    ) {
        Column(
            verticalArrangement = Arrangement.spacedBy(16.dp),
            modifier = Modifier
                .weight(1f)
                .verticalScroll(rememberScrollState())
        ) {
            Text(
                currentWorkout.workoutName,
                style = MaterialTheme.typography.titleLarge,
                modifier = Modifier.align(Alignment.CenterHorizontally)
            )

            Surface(
                color = MaterialTheme.colorScheme.surfaceContainerHigh,
                contentColor = MaterialTheme.colorScheme.onSurface,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(192.dp)
                    .border(
                        width = 2.dp,
                        color = MaterialTheme.colorScheme.primary
                    )
            ) {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    Text(
                        time.formatTime(),
                        style = MaterialTheme.typography.displayLarge
                    )
                    Text(
                        "%.2f / %.2f mi".format(distance.metersToMiles(), totalDistance.metersToMiles()),
                        style = MaterialTheme.typography.titleLarge
                    )
                }
            }

            Surface(
                color = MaterialTheme.colorScheme.surfaceContainer,
                contentColor = MaterialTheme.colorScheme.onSurface,
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(
                    verticalArrangement = Arrangement.spacedBy(8.dp),
                    modifier = Modifier.padding(16.dp)
                ) {
                    Text(
                        "Segments",
                        style = MaterialTheme.typography.titleMedium
                    )

                    currentWorkout.segments.forEachIndexed { index, segment ->
                        val isActive = index == currentSegmentIndex
                        val targetText = if (syncWithCadence) "${segment.cadence} spm" else "${segment.multiplier}x"
                        val distanceText = segment.distance.metersToMiles().formatDistance("mi")

                        Row(
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Text(
                                text = if (isActive) "$distanceText (Active)" else distanceText,
                                style = if (isActive) MaterialTheme.typography.titleMedium else MaterialTheme.typography.labelLarge,
                                color = if (isActive) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurface
                            )
                            Text(
                                targetText,
                                style = if (isActive) MaterialTheme.typography.titleMedium else MaterialTheme.typography.bodyMedium,
                                color = if (isActive) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurface
                            )
                        }
                    }
                }
            }

            Surface(
                color = MaterialTheme.colorScheme.surfaceContainer,
                contentColor = MaterialTheme.colorScheme.onSurface,
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(
                    verticalArrangement = Arrangement.spacedBy(8.dp),
                    modifier = Modifier.padding(16.dp)
                ) {
                    StatRow("Current pace", speed.formatPace() + " /mi")
                    StatRow("Average pace", (distance / time).formatPace() + " /mi")
                    StatRow("Current cadence", formatCadence(cadence))
                    StatRow("Average cadence", formatCadence(steps, time))
                }
            }

            SettingSelector(
                name = "Sync with",
                options = listOf("Pace", "Cadence"),
                selectedOption = if (syncWithCadence) "Cadence" else "Pace",
                onOptionSelected = { onSyncWithCadenceChange(it == "Cadence") }
            )
        }

        Row(modifier = Modifier.fillMaxWidth()) {
            Spacer(modifier = Modifier.weight(1f))
            Surface(
                onClick = onEndRun,
                color = MaterialTheme.colorScheme.primary,
                contentColor = MaterialTheme.colorScheme.onPrimary,
                modifier = Modifier
                    .weight(3f)
                    .height(64.dp)
            ) {
                Box(contentAlignment = Alignment.Center) {
                    Text(
                        "End Run",
                        style = MaterialTheme.typography.labelLarge
                    )
                }
            }
            Spacer(modifier = Modifier.weight(1f))
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun WorkoutStatsScreenPreview() {
    WorkoutStatsScreen(
        onEndRun = {}
    )
}

internal fun activeWorkoutSegmentIndex(segments: List<WorkoutSegment>, distance: Float): Int {
    var segmentEndDistance = 0f
    segments.forEachIndexed { index, segment ->
        segmentEndDistance += segment.distance
        if (distance < segmentEndDistance) return index
    }
    return segments.size
}
