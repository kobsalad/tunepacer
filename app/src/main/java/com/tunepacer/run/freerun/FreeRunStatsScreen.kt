package com.tunepacer.run.freerun

import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.tunepacer.ui.util.formatDistance
import com.tunepacer.ui.util.formatPace
import com.tunepacer.ui.util.formatTime
import com.tunepacer.ui.util.metersToMiles

@Composable
fun FreeRunStatsScreen(
    settings: FreeRunSettings,
    onSettingsChange: (FreeRunSettings) -> Unit,
    onEndRun: () -> Unit
) {
    // Placeholder data
    val distance = 2253.08
    val time = 754
    val currentPace = 520
    val currentCadence = 172
    val averageCadence = 168

    Column(
        verticalArrangement = Arrangement.spacedBy(16.dp),
        modifier = Modifier
            .fillMaxHeight()
            .padding(32.dp, 16.dp)
    ) {
        Column(
            verticalArrangement = Arrangement.spacedBy(16.dp),
            modifier = Modifier
                .weight(1f)
                .verticalScroll(rememberScrollState())
        ) {
            Text("Free Run",
                style = MaterialTheme.typography.titleLarge,
                modifier = Modifier.align(Alignment.CenterHorizontally)
            )

            Surface(
                color = MaterialTheme.colorScheme.surfaceContainerHigh,
                contentColor = MaterialTheme.colorScheme.onSurface,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(192.dp)
                    .border(
                        width = 2.dp,
                        color = MaterialTheme.colorScheme.primary
                    )
            ) {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    Text(time.formatTime(),
                        style = MaterialTheme.typography.displayLarge
                    )
                    Text(distance.metersToMiles().formatDistance("mi"),
                        style = MaterialTheme.typography.titleLarge
                    )
                }
            }

            Surface(
                color = MaterialTheme.colorScheme.surfaceContainer,
                contentColor = MaterialTheme.colorScheme.onSurface,
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(
                    verticalArrangement = Arrangement.spacedBy(8.dp),
                    modifier = Modifier.padding(16.dp)
                ) {
                    StatRow("Current pace", currentPace.formatTime() + " /mi")
                    StatRow("Average pace", formatPace(distance, time) + " /mi")
                    StatRow("Current cadence", "$currentCadence spm")
                    StatRow("Average cadence", "$averageCadence spm")
                }
            }

            FreeRunSettingsPanel(settings, onSettingsChange)
        }

        Row(modifier = Modifier.fillMaxWidth()) {
            Spacer(modifier = Modifier.weight(1f))
            Surface(
                onClick = onEndRun,
                color = MaterialTheme.colorScheme.primary,
                contentColor = MaterialTheme.colorScheme.onPrimary,
                modifier = Modifier
                    .weight(3f)
                    .height(64.dp)
            ) {
                Box(contentAlignment = Alignment.Center) {
                    Text("End Run",
                        style = MaterialTheme.typography.labelLarge
                    )
                }
            }
            Spacer(modifier = Modifier.weight(1f))
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun FreeRunStatsScreenPreview() {
    FreeRunStatsScreen(
        settings = FreeRunSettings(),
        onSettingsChange = {},
        onEndRun = {}
    )
}
