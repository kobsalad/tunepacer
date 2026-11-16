package com.tunepacer.run.workout

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp

@Composable
fun WorkoutScreen(
    onStartWorkout: (Workout) -> Unit = {}
) {
    val workoutsData = listOf(
        Workout(
            "5k Tempo",
            listOf(
                WorkoutSegment(1600f, 1.1f, 170),
                WorkoutSegment(5000f, 1.5f, 185),
                WorkoutSegment(1600f, 1.1f, 170)
            )
        ),
        Workout(
            "400m Repeats",
            listOf(
                WorkoutSegment(400f, 1.5f, 190),
                WorkoutSegment(200f, 1f, 170),
                WorkoutSegment(400f, 1.5f, 190),
                WorkoutSegment(200f, 1f, 170)
            )
        )
    )
    LazyColumn(
        verticalArrangement = Arrangement.spacedBy(8.dp),
        modifier = Modifier.padding(8.dp)
    ) {
        items(workoutsData) { workoutData ->
            WorkoutItem(
                workoutData = workoutData,
                onStartWorkout = onStartWorkout
            )
        }
        item {
            Row(modifier = Modifier.fillMaxWidth()) {
                Surface(
                    onClick = {},
                    modifier = Modifier.height(48.dp),
                    color = MaterialTheme.colorScheme.tertiary,
                    contentColor = MaterialTheme.colorScheme.onTertiary
                ) {
                    Box(
                        contentAlignment = Alignment.Center,
                        modifier = Modifier.fillMaxSize()
                    ) {
                        Text("New workout",
                            style = MaterialTheme.typography.labelLarge)
                    }
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun WorkoutScreenPreview() {
    WorkoutScreen()
}
