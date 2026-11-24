package com.tunepacer.run.freerun

import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.unit.dp
import com.tunepacer.Settings

@Composable
fun FreeRunSettingsPanel() {
    val currentSyncType = if (Settings.syncTypeAdaptive) "Adaptive" else "Constant"
    val currentSyncWith = if (Settings.syncWithCadence) "Cadence" else "Pace"
    val focusManager = LocalFocusManager.current

    Column(
        verticalArrangement = Arrangement.spacedBy(16.dp),
        modifier = Modifier.pointerInput(Unit) {
            detectTapGestures(onTap = { focusManager.clearFocus() })
        }
    ) {
        SettingSelector(
            "Sync type",
            listOf("Constant", "Adaptive"),
            currentSyncType,
            onOptionSelected = { selected ->
                focusManager.clearFocus()
                Settings.syncTypeAdaptive = (selected == "Adaptive")
            }
        )
        SettingSelector(
            "Sync with",
            listOf("Pace", "Cadence"),
            currentSyncWith,
            onOptionSelected = { selected ->
                focusManager.clearFocus()
                Settings.syncWithCadence = (selected == "Cadence")
            }
        )

        HorizontalDivider(
            thickness = 1.dp,
            color = MaterialTheme.colorScheme.primary
        )

        when (!Settings.syncTypeAdaptive to Settings.syncWithCadence) {
            // Constant Pace
            true to false -> {
                NumericInput(
                    label = "Multiplier",
                    value = Settings.constantPaceMultiplier.toString(),
                    defaultValue = "1.5",
                    isDecimal = true,
                    onValueChange = { input ->
                        input.toFloatOrNull()?.let { Settings.constantPaceMultiplier = it }
                    }
                )
            }
            // Adaptive Pace
            false to false -> {
                PaceInput(
                    label = "1.0x",
                    seconds = Settings.adaptivePaceBase,
                    onSecondsChange = { newSec ->
                        Settings.adaptivePaceBase = newSec
                    }
                )
                PaceInput(
                    label = "1.5x",
                    seconds = Settings.adaptivePaceFast,
                    onSecondsChange = { newSec ->
                        Settings.adaptivePaceFast = newSec
                    }
                )
            }
            // Constant Cadence
            true to true -> {
                NumericInput(
                    label = "Cadence",
                    value = Settings.constantCadenceTarget.toString(),
                    defaultValue = "180",
                    isDecimal = false,
                    onValueChange = { input ->
                        input.toIntOrNull()?.let { Settings.constantCadenceTarget = it }
                    }
                )
            }
            // Adaptive Cadence
            false to true -> {
                // No extra settings for this mode
            }
        }
    }
}
