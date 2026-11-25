package com.tunepacer.run

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.runtime.saveable.rememberSaveableStateHolder
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation3.runtime.NavKey
import androidx.navigation3.runtime.entryProvider
import androidx.navigation3.runtime.rememberNavBackStack
import androidx.navigation3.ui.NavDisplay
import com.tunepacer.db.AppDatabase
import com.tunepacer.db.PastRunEntity
import com.tunepacer.run.workout.Workout
import com.tunepacer.run.freerun.FreeRunStatsScreen
import com.tunepacer.run.workout.WorkoutStatsScreen
import com.tunepacer.sensors.Gps
import com.tunepacer.sensors.Pedometer
import com.tunepacer.sensors.Run
import com.tunepacer.spotify.SpotifyBrowser
import com.tunepacer.ui.util.horizontalTabTransition
import kotlinx.coroutines.launch
import kotlinx.serialization.Serializable

private sealed interface RunNavKey {
    @Serializable
    data object FreeRunStats: NavKey
    @Serializable
    data object WorkoutStats: NavKey
    @Serializable
    data object Setup: NavKey
}

private val runScreens = listOf(RunNavKey.FreeRunStats, RunNavKey.WorkoutStats, RunNavKey.Setup)

@Composable
fun RunScreen() {
    val backStack = rememberNavBackStack(RunNavKey.Setup)
    val saveableStateHolder = rememberSaveableStateHolder()
    val context = LocalContext.current
    val pastRunDao = remember(context) { AppDatabase.getInstance(context).pastRunDao() }
    val coroutineScope = rememberCoroutineScope()
    var selectedWorkout by remember { mutableStateOf<Workout?>(null) }

    fun endRun(workoutName: String?) {
        val time = Run.time.value
        val distance = Gps.distance.value
        val steps = Pedometer.steps.value
        val date = System.currentTimeMillis()
        coroutineScope.launch {
            pastRunDao.insert(
                PastRunEntity(
                    workoutName = workoutName,
                    date = date,
                    distance = distance,
                    time = time,
                    cadence = if (time > 0) steps * 60 / time else 0
                )
            )
        }
        SpotifyBrowser.setPlaybackRate(1f)
        backStack.clear()
        backStack.add(RunNavKey.Setup)
        Run.stopRun()
    }

    NavDisplay(
        backStack = backStack,
        entryProvider = entryProvider {
            entry(RunNavKey.FreeRunStats, RunNavKey.FreeRunStats.toString()) {
                saveableStateHolder.SaveableStateProvider(RunNavKey.FreeRunStats.toString()) {
                    FreeRunStatsScreen(
                        onEndRun = { endRun(workoutName = null) }
                    )
                }
            }
            entry(RunNavKey.WorkoutStats, RunNavKey.WorkoutStats.toString()) {
                saveableStateHolder.SaveableStateProvider(RunNavKey.WorkoutStats.toString()) {
                    WorkoutStatsScreen(
                        workout = selectedWorkout,
                        onEndRun = { endRun(workoutName = selectedWorkout?.workoutName) }
                    )
                }
            }
            entry(RunNavKey.Setup, RunNavKey.Setup.toString()) {
                saveableStateHolder.SaveableStateProvider(RunNavKey.Setup.toString()) {
                    RunSetupScreen(
                        onStartFreeRun = {
                            selectedWorkout = null
                            backStack.clear()
                            backStack.add(RunNavKey.FreeRunStats)
                            Run.startRun(context)
                        },
                        onStartWorkout = { workout ->
                            selectedWorkout = workout
                            backStack.clear()
                            backStack.add(RunNavKey.WorkoutStats)
                            Run.startRun(context)
                        }
                    )
                }
            }
        },
        transitionSpec = { horizontalTabTransition(runScreens) },
        popTransitionSpec = { horizontalTabTransition(runScreens) },
        predictivePopTransitionSpec = { horizontalTabTransition(runScreens) }
    )
}

@Preview(showBackground = true)
@Composable
private fun RunScreenPreview() {
    RunScreen()
}
