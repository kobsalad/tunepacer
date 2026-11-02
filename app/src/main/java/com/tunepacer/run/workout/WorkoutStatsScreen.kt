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
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.tunepacer.run.freerun.SettingSelector
import com.tunepacer.run.freerun.StatRow
import com.tunepacer.ui.util.formatDistance
import com.tunepacer.ui.util.formatPace
import com.tunepacer.ui.util.formatTime
import com.tunepacer.ui.util.metersToMiles

@Composable
fun WorkoutStatsScreen(
    workout: Workout? = null,
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
    val elapsedTime = 754
    val distance = 2253.08f
    val totalDistance = currentWorkout.segments.sumOf { it.distance.toDouble() }.toFloat()
    val currentPace = 520
    val currentCadence = 172
    val averageCadence = 168
    val currentSegmentIndex = 1

    var selectedMode by rememberSaveable { mutableStateOf("Pace") }

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
                        elapsedTime.formatTime(),
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
                        val targetText = if (selectedMode == "Pace") "${segment.multiplier}x" else "${segment.cadence} spm"
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
                    StatRow("Current pace", currentPace.formatTime() + " /mi")
                    StatRow("Average pace", formatPace(distance, elapsedTime) + " /mi")
                    StatRow("Current cadence", "$currentCadence spm")
                    StatRow("Average cadence", "$averageCadence spm")
                }
            }

            SettingSelector(
                name = "Sync with",
                options = listOf("Pace", "Cadence"),
                selectedOption = selectedMode,
                onOptionSelected = { selectedMode = it }
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
