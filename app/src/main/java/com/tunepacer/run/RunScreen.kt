package com.tunepacer.run

import androidx.compose.runtime.Composable
import androidx.compose.runtime.saveable.rememberSaveableStateHolder
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation3.runtime.NavKey
import androidx.navigation3.runtime.entryProvider
import androidx.navigation3.runtime.rememberNavBackStack
import androidx.navigation3.ui.NavDisplay
import com.tunepacer.run.freerun.FreeRunStatsScreen
import com.tunepacer.run.workout.WorkoutStatsScreen
import com.tunepacer.sensors.Run
import com.tunepacer.ui.util.horizontalTabTransition
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

    NavDisplay(
        backStack = backStack,
        entryProvider = entryProvider {
            entry(RunNavKey.FreeRunStats, RunNavKey.FreeRunStats.toString()) {
                saveableStateHolder.SaveableStateProvider(RunNavKey.FreeRunStats.toString()) {
                    FreeRunStatsScreen(
                        onEndRun = {
                            backStack.clear()
                            backStack.add(RunNavKey.Setup)
                            Run.stopRun()
                        }
                    )
                }
            }
            entry(RunNavKey.WorkoutStats, RunNavKey.WorkoutStats.toString()) {
                saveableStateHolder.SaveableStateProvider(RunNavKey.WorkoutStats.toString()) {
                    WorkoutStatsScreen(
                        onEndRun = {
                            backStack.clear()
                            backStack.add(RunNavKey.Setup)
                            Run.stopRun()
                        }
                    )
                }
            }
            entry(RunNavKey.Setup, RunNavKey.Setup.toString()) {
                saveableStateHolder.SaveableStateProvider(RunNavKey.Setup.toString()) {
                    RunSetupScreen(
                        onStartFreeRun = {
                            backStack.clear()
                            backStack.add(RunNavKey.FreeRunStats)
                            Run.startRun(context)
                        },
                        onStartWorkout = {
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
