package com.tunepacer.history

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.tunepacer.db.AppDatabase
import com.tunepacer.db.PastRunEntity

@Composable
fun HistoryScreen() {
    val context = LocalContext.current
    val pastRunsFlow = remember(context) {
        AppDatabase.getInstance(context).pastRunDao().observeAll()
    }
    val pastRuns by pastRunsFlow.collectAsState(initial = emptyList())

    PastRunsList(pastRuns.map { it.toPastRun() })
}

@Composable
private fun PastRunsList(pastRuns: List<PastRun>) {
    LazyColumn(
        verticalArrangement = Arrangement.spacedBy(8.dp),
        modifier = Modifier.padding(8.dp)
    ) {
        items(pastRuns) { pastRun ->
            PastRunItem(pastRun)
        }
    }
}

private fun PastRunEntity.toPastRun() = PastRun(
    id = id,
    workoutName = workoutName,
    date = date,
    distance = distance,
    time = time,
    cadence = cadence
)

@Preview(showBackground = true)
@Composable
private fun HistoryScreenPreview() {
    PastRunsList(emptyList())
}
