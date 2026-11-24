package com.tunepacer.run.freerun

import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.tunepacer.sensors.Gps
import com.tunepacer.sensors.Pedometer
import com.tunepacer.sensors.Run
import com.tunepacer.ui.util.formatCadence
import com.tunepacer.ui.util.formatDistance
import com.tunepacer.ui.util.formatPace
import com.tunepacer.ui.util.formatTime
import com.tunepacer.ui.util.metersToMiles

@Composable
fun FreeRunStatsScreen(
    onEndRun: () -> Unit
) {
    val time by Run.time.collectAsState()
    val speed by Gps.speed.collectAsState()
    val distance by Gps.distance.collectAsState()
    val cadence by Pedometer.cadence.collectAsState()
    val steps by Pedometer.steps.collectAsState()
    val focusManager = LocalFocusManager.current

    Column(
        verticalArrangement = Arrangement.spacedBy(16.dp),
        modifier = Modifier
            .fillMaxSize()
            .clickable(
                interactionSource = remember { MutableInteractionSource() },
                indication = null
            ) {
                focusManager.clearFocus()
            }
            .padding(32.dp, 16.dp)
    ) {
        Column(
            verticalArrangement = Arrangement.spacedBy(16.dp),
            modifier = Modifier
                .weight(1f)
                .verticalScroll(rememberScrollState())
        ) {
            Text(
                "Free Run",
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
                    Text(
                        time.formatTime(),
                        style = MaterialTheme.typography.displayLarge
                    )
                    Text(
                        distance.metersToMiles().formatDistance("mi"),
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
                    StatRow("Current pace", speed.formatPace() + " /mi")
                    StatRow("Average pace", (distance / time).formatPace() + " /mi")
                    StatRow("Current cadence", formatCadence(cadence))
                    StatRow("Average cadence", formatCadence(steps, time))
                }
            }

            FreeRunSettingsPanel()
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
                    Text(
                        "End Run",
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
        onEndRun = {}
    )
}
