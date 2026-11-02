package com.tunepacer.run

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.runtime.Composable
import androidx.compose.runtime.saveable.rememberSaveableStateHolder
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation3.runtime.NavKey
import androidx.navigation3.runtime.entryProvider
import androidx.navigation3.runtime.rememberNavBackStack
import androidx.navigation3.ui.NavDisplay
import com.tunepacer.run.freerun.FreeRunScreen
import com.tunepacer.run.freerun.FreeRunSettings
import com.tunepacer.run.workout.WorkoutScreen
import com.tunepacer.ui.components.TopNavBar
import com.tunepacer.ui.components.TopNavButton
import com.tunepacer.ui.util.horizontalTabTransition
import kotlinx.serialization.Serializable

private sealed interface RunSetupNavKey {
    @Serializable
    data object FreeRun: NavKey
    @Serializable
    data object Workout: NavKey
}

private val runSetupTabs = listOf(RunSetupNavKey.FreeRun, RunSetupNavKey.Workout)

@Composable
fun RunSetupScreen(
    freeRunSettings: FreeRunSettings,
    onFreeRunSettingsChange: (FreeRunSettings) -> Unit,
    onStartFreeRun: () -> Unit
) {
    val backStack = rememberNavBackStack(RunSetupNavKey.FreeRun)
    val saveableStateHolder = rememberSaveableStateHolder()

    Column(modifier = Modifier.fillMaxHeight()) {
        TopNavBar {
            TopNavButton(
                onClick = {
                    backStack.clear()
                    backStack.add(RunSetupNavKey.FreeRun)
                },
                selected = backStack.lastOrNull() == RunSetupNavKey.FreeRun,
                label = "Free Run"
            )
            TopNavButton(
                onClick = {
                    backStack.clear()
                    backStack.add(RunSetupNavKey.Workout)
                },
                selected = backStack.lastOrNull() == RunSetupNavKey.Workout,
                label = "Workout"
            )
        }

        NavDisplay(
            backStack = backStack,
            entryProvider = entryProvider {
                entry(RunSetupNavKey.FreeRun, RunSetupNavKey.FreeRun.toString()) {
                    saveableStateHolder.SaveableStateProvider(RunSetupNavKey.FreeRun.toString()) {
                        FreeRunScreen(
                            settings = freeRunSettings,
                            onSettingsChange = onFreeRunSettingsChange,
                            onStart = onStartFreeRun
                        )
                    }
                }
                entry(RunSetupNavKey.Workout, RunSetupNavKey.Workout.toString()) {
                    saveableStateHolder.SaveableStateProvider(RunSetupNavKey.Workout.toString()) {
                        WorkoutScreen()
                    }
                }
            },
            transitionSpec = { horizontalTabTransition(runSetupTabs) },
            popTransitionSpec = { horizontalTabTransition(runSetupTabs) },
            predictivePopTransitionSpec = { horizontalTabTransition(runSetupTabs) }
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun RunSetupScreenPreview() {
    RunSetupScreen(
        freeRunSettings = FreeRunSettings(),
        onFreeRunSettingsChange = {},
        onStartFreeRun = {}
    )
}
