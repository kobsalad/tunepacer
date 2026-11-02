package com.tunepacer.run.freerun

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.saveable.listSaver
import androidx.compose.ui.unit.dp

data class FreeRunSettings(
    val syncType: String = "Adaptive",
    val syncWith: String = "Cadence"
) {
    companion object {
        val Saver = listSaver<FreeRunSettings, String>(
            save = { listOf(it.syncType, it.syncWith) },
            restore = { FreeRunSettings(it[0], it[1]) }
        )
    }
}

@Composable
fun FreeRunSettingsPanel(
    settings: FreeRunSettings,
    onSettingsChange: (FreeRunSettings) -> Unit
) {
    Column(verticalArrangement = Arrangement.spacedBy(16.dp)) {
        SettingSelector(
            "Sync type",
            listOf("Constant", "Adaptive"),
            settings.syncType,
            onOptionSelected = { selected ->
                onSettingsChange(settings.copy(syncType = selected))
            }
        )
        SettingSelector(
            "Sync with",
            listOf("Pace", "Cadence"),
            settings.syncWith,
            onOptionSelected = { selected ->
                onSettingsChange(settings.copy(syncWith = selected))
            }
        )

        HorizontalDivider(
            thickness = 1.dp,
            color = MaterialTheme.colorScheme.primary
        )

        when (settings.syncType to settings.syncWith) {
            "Constant" to "Pace" -> {
                SettingInput("Multiplier", "1.5")
            }
            "Adaptive" to "Pace" -> {
                SettingInput("1.0x", "9:00")
                SettingInput("1.5x", "7:00")
            }
            "Constant" to "Cadence" -> {
                SettingInput("Cadence", "180")
            }
            "Adaptive" to "Cadence" -> {
                // No settings for this mode
            }
        }
    }
}
