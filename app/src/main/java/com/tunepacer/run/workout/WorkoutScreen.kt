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
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.tunepacer.db.AppDatabase
import com.tunepacer.db.WorkoutEntity
import kotlinx.coroutines.launch

@Composable
fun WorkoutScreen(
    onStartWorkout: (Workout, syncWithCadence: Boolean) -> Unit = { _, _ -> }
) {
    val context = LocalContext.current
    val workoutDao = remember(context) { AppDatabase.getInstance(context).workoutDao() }
    val scope = rememberCoroutineScope()
    val workouts by workoutDao.observeAll().collectAsState(initial = emptyList())

    WorkoutList(
        workouts = workouts,
        onStartWorkout = onStartWorkout,
        onUpdateWorkout = { workout -> scope.launch { workoutDao.update(workout) } },
        onDeleteWorkout = { workout -> scope.launch { workoutDao.delete(workout) } },
        onCreateWorkout = {
            scope.launch {
                workoutDao.insert(
                    WorkoutEntity(
                        workoutName = "New workout",
                        segments = listOf(WorkoutSegment(400f, 1.5f, 180))
                    )
                )
            }
        }
    )
}

@Composable
private fun WorkoutList(
    workouts: List<WorkoutEntity>,
    onStartWorkout: (Workout, syncWithCadence: Boolean) -> Unit,
    onUpdateWorkout: (WorkoutEntity) -> Unit,
    onDeleteWorkout: (WorkoutEntity) -> Unit,
    onCreateWorkout: () -> Unit
) {
    LazyColumn(
        verticalArrangement = Arrangement.spacedBy(8.dp),
        modifier = Modifier.padding(8.dp)
    ) {
        items(workouts, key = { it.id }) { workoutData ->
            WorkoutItem(
                workoutData = workoutData,
                onStartWorkout = onStartWorkout,
                onUpdateWorkout = onUpdateWorkout,
                onDeleteWorkout = { onDeleteWorkout(workoutData) }
            )
        }
        item {
            Row(modifier = Modifier.fillMaxWidth()) {
                Surface(
                    onClick = onCreateWorkout,
                    modifier = Modifier.height(48.dp),
                    color = MaterialTheme.colorScheme.tertiary,
                    contentColor = MaterialTheme.colorScheme.onTertiary
                ) {
                    Box(
                        contentAlignment = Alignment.Center,
                        modifier = Modifier.fillMaxSize()
                    ) {
                        Text(
                            "New workout",
                            style = MaterialTheme.typography.labelLarge
                        )
                    }
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun WorkoutScreenPreview() {
    WorkoutList(
        workouts = emptyList(),
        onStartWorkout = { _, _ -> },
        onUpdateWorkout = {},
        onDeleteWorkout = {},
        onCreateWorkout = {}
    )
}
