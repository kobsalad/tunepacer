package com.tunepacer

import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.saveable.rememberSaveableStateHolder
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation3.runtime.NavKey
import androidx.navigation3.runtime.entryProvider
import androidx.navigation3.runtime.rememberNavBackStack
import androidx.navigation3.ui.NavDisplay
import com.tunepacer.history.HistoryScreen
import com.tunepacer.navigation.horizontalTabTransition
import com.tunepacer.run.RunScreen
import com.tunepacer.spotify.SpotifyScreen
import com.tunepacer.ui.components.BottomNavBar
import com.tunepacer.ui.components.BottomNavButton
import com.tunepacer.ui.theme.TunePacerTheme
import kotlinx.serialization.Serializable

private sealed interface AppNavKey {
    @Serializable
    data object Run: NavKey
    @Serializable
    data object Spotify: NavKey
    @Serializable
    data object History: NavKey
}

private val appTabs = listOf(AppNavKey.Run, AppNavKey.Spotify, AppNavKey.History)

@Composable
fun App() {
    TunePacerTheme {
        val backStack = rememberNavBackStack(AppNavKey.Spotify)
        val saveableStateHolder = rememberSaveableStateHolder()

        Scaffold(
            bottomBar = {
                BottomNavBar(
                    modifier = Modifier.windowInsetsPadding(WindowInsets.navigationBars)
                ) {
                    BottomNavButton(
                        onClick = {
                            backStack.clear()
                            backStack.add(AppNavKey.Spotify)
                            backStack.add(AppNavKey.Run)
                        },
                        selected = backStack.lastOrNull() == AppNavKey.Run,
                        icon = { Text("R") },
                        label = "Run"
                    )
                    BottomNavButton(
                        onClick = {
                            backStack.clear()
                            backStack.add(AppNavKey.Spotify)
                        },
                        selected = backStack.lastOrNull() == AppNavKey.Spotify,
                        icon = { Text("S") },
                        label = "Spotify"
                    )
                    BottomNavButton(
                        onClick = {
                            backStack.clear()
                            backStack.add(AppNavKey.Spotify)
                            backStack.add(AppNavKey.History)
                        },
                        selected = backStack.lastOrNull() == AppNavKey.History,
                        icon = { Text("H") },
                        label = "History"
                    )
                }
            }
        ) { innerPadding ->
            NavDisplay (
                backStack = backStack,
                modifier = Modifier.padding(innerPadding),
                entryProvider = entryProvider {
                    entry(AppNavKey.Run, AppNavKey.Run.toString()) {
                        saveableStateHolder.SaveableStateProvider(AppNavKey.Run.toString()) {
                            RunScreen()
                        }
                    }
                    entry(AppNavKey.Spotify, AppNavKey.Spotify.toString()) {
                        saveableStateHolder.SaveableStateProvider(AppNavKey.Spotify.toString()) {
                            SpotifyScreen()
                        }
                    }
                    entry(AppNavKey.History, AppNavKey.History.toString()) {
                        saveableStateHolder.SaveableStateProvider(AppNavKey.History.toString()) {
                            HistoryScreen()
                        }
                    }
                },
                transitionSpec = { horizontalTabTransition(appTabs) },
                popTransitionSpec = { horizontalTabTransition(appTabs) },
                predictivePopTransitionSpec = { horizontalTabTransition(appTabs) }
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun AppPreview() {
    App()
}
