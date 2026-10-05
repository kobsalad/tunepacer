package com.tunepacer.run

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.saveable.rememberSaveableStateHolder
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation3.runtime.NavKey
import androidx.navigation3.runtime.entryProvider
import androidx.navigation3.runtime.rememberNavBackStack
import androidx.navigation3.ui.NavDisplay
import com.tunepacer.navigation.horizontalTabTransition
import com.tunepacer.ui.components.TopNavBar
import com.tunepacer.ui.components.TopNavButton
import kotlinx.serialization.Serializable

private sealed interface RunNavKey {
    @Serializable
    data object FreeRun: NavKey
    @Serializable
    data object Workout: NavKey
}

private val runTabs = listOf(RunNavKey.FreeRun, RunNavKey.Workout)

@Composable
fun RunScreen() {
    val backStack = rememberNavBackStack(RunNavKey.FreeRun)
    val saveableStateHolder = rememberSaveableStateHolder()

    Scaffold(
        topBar = {
            TopNavBar {
                TopNavButton(
                    onClick = {
                        backStack.clear()
                        backStack.add(RunNavKey.FreeRun)
                    },
                    selected = backStack.lastOrNull() == RunNavKey.FreeRun,
                    label = "Free Run"
                )
                TopNavButton(
                    onClick = {
                        backStack.clear()
                        backStack.add(RunNavKey.Workout)
                    },
                    selected = backStack.lastOrNull() == RunNavKey.Workout,
                    label = "Workout"
                )
            }
        }
    ) { innerPadding ->
        NavDisplay(
            backStack = backStack,
            modifier = Modifier.padding(innerPadding),
            entryProvider = entryProvider {
                entry(RunNavKey.FreeRun, RunNavKey.FreeRun.toString()) {
                    saveableStateHolder.SaveableStateProvider(RunNavKey.FreeRun.toString()) {
                        FreeRunScreen()
                    }
                }
                entry(RunNavKey.Workout, RunNavKey.Workout.toString()) {
                    saveableStateHolder.SaveableStateProvider(RunNavKey.Workout.toString()) {
                        WorkoutScreen()
                    }
                }
            },
            transitionSpec = { horizontalTabTransition(runTabs) },
            popTransitionSpec = { horizontalTabTransition(runTabs) },
            predictivePopTransitionSpec = { horizontalTabTransition(runTabs) }
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun RunScreenPreview() {
    RunScreen()
}
