package com.tunepacer.run

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.saveable.rememberSaveableStateHolder
import androidx.compose.runtime.setValue
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation3.runtime.NavKey
import androidx.navigation3.runtime.entryProvider
import androidx.navigation3.runtime.rememberNavBackStack
import androidx.navigation3.ui.NavDisplay
import com.tunepacer.run.freerun.FreeRunSettings
import com.tunepacer.run.freerun.FreeRunStatsScreen
import com.tunepacer.ui.util.horizontalTabTransition
import kotlinx.serialization.Serializable

private sealed interface RunNavKey {
    @Serializable
    data object FreeRunStats: NavKey
    @Serializable
    data object Setup: NavKey
}

private val runScreens = listOf(RunNavKey.FreeRunStats, RunNavKey.Setup)

@Composable
fun RunScreen() {
    val backStack = rememberNavBackStack(RunNavKey.Setup)
    val saveableStateHolder = rememberSaveableStateHolder()
    var freeRunSettings by rememberSaveable(stateSaver = FreeRunSettings.Saver) {
        mutableStateOf(FreeRunSettings())
    }

    NavDisplay(
        backStack = backStack,
        entryProvider = entryProvider {
            entry(RunNavKey.FreeRunStats, RunNavKey.FreeRunStats.toString()) {
                saveableStateHolder.SaveableStateProvider(RunNavKey.FreeRunStats.toString()) {
                    FreeRunStatsScreen(
                        settings = freeRunSettings,
                        onSettingsChange = { freeRunSettings = it },
                        onEndRun = {
                            backStack.removeLastOrNull()
                        }
                    )
                }
            }
            entry(RunNavKey.Setup, RunNavKey.Setup.toString()) {
                saveableStateHolder.SaveableStateProvider(RunNavKey.Setup.toString()) {
                    RunSetupScreen(
                        freeRunSettings = freeRunSettings,
                        onFreeRunSettingsChange = { freeRunSettings = it },
                        onStartFreeRun = {
                            backStack.add(RunNavKey.FreeRunStats)
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
