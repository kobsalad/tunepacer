package com.tunepacer.history

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
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
import androidx.compose.ui.unit.dp
import com.tunepacer.ui.util.formatDate
import com.tunepacer.ui.util.formatDistance
import com.tunepacer.ui.util.formatPace
import com.tunepacer.ui.util.formatTime
import com.tunepacer.ui.util.metersToMiles

@Composable
fun PastRunItem(pastRunData: PastRun) {
    var expanded by rememberSaveable { mutableStateOf(false) }

    Surface (
        onClick = { expanded = !expanded },
        color =
            if (expanded) MaterialTheme.colorScheme.surfaceContainerHigh
            else MaterialTheme.colorScheme.surfaceContainer,
        contentColor = MaterialTheme.colorScheme.onSurface
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            Row (
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(pastRunData.date.formatDate())
                Text(pastRunData.distance.metersToMiles().formatDistance("mi"))
                Text(pastRunData.time.formatTime())
                Text(if (expanded) "▼" else "◀")
            }
            if (expanded) {
                if (pastRunData.workoutName != null) {
                    Text(pastRunData.workoutName)
                }
                Text("Pace: " + formatPace(pastRunData.distance, pastRunData.time))
                Text("Cadence: " + pastRunData.cadence)
            }
        }
    }
}
