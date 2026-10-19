package com.tunepacer.run.freerun

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp

@Composable
fun FreeRunScreen() {
    var selectedSyncType by rememberSaveable { mutableStateOf("Adaptive") }
    var selectedSyncWith by rememberSaveable { mutableStateOf("Cadence") }

    Column(
        verticalArrangement = Arrangement.spacedBy(16.dp),
        modifier = Modifier.padding(32.dp, 16.dp)
    ) {
        Column(verticalArrangement = Arrangement.spacedBy(16.dp)) {
            SettingSelector(
                "Sync type",
                listOf("Constant", "Adaptive"),
                selectedSyncType,
                onOptionSelected = { selected ->
                    selectedSyncType = selected
                }
            )
            SettingSelector(
                "Sync with",
                listOf("Pace", "Cadence"),
                selectedSyncWith,
                onOptionSelected = { selected ->
                    selectedSyncWith = selected
                }
            )
        }

        HorizontalDivider(
            thickness = 1.dp,
            color = MaterialTheme.colorScheme.primary
        )

        when (selectedSyncType to selectedSyncWith) {
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

        Spacer(modifier = Modifier.weight(1f))

        Row(modifier = Modifier.fillMaxWidth()) {
            Spacer(modifier = Modifier.weight(1f))
            Column(modifier = Modifier.weight(3f)) {
                Surface(
                    onClick = {},
                    color = MaterialTheme.colorScheme.primary,
                    contentColor = MaterialTheme.colorScheme.onPrimary,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(64.dp)
                ) {
                    Box(contentAlignment = Alignment.Center) {
                        Text("Start Free Run",
                            style = MaterialTheme.typography.labelLarge
                        )
                    }

                }
                Surface(
                    color = MaterialTheme.colorScheme.tertiary,
                    contentColor = MaterialTheme.colorScheme.onTertiary,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(32.dp)
                ) {
                    Box(
                        contentAlignment = Alignment.Center
                    ) {
                        Text("GPS Ready",
                            style = MaterialTheme.typography.labelLarge
                        )
                    }

                }
            }
            Spacer(modifier = Modifier.weight(1f))
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun FreeRunScreenPreview() {
    FreeRunScreen()
}
