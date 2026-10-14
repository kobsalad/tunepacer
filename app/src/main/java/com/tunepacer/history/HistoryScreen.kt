package com.tunepacer.history

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import kotlin.random.Random

@Composable
fun HistoryScreen() {
    val pastRunsData = MutableList(100) {
        PastRun(
            0,
            if (Random.nextBoolean()) "Workout Name" else null,
            Random.nextLong(1700000000, 1790000000),
            Random.nextDouble(1.0, 100000.0),
            Random.nextInt(1, 100000),
            Random.nextInt(100, 200)
        )
    }
    pastRunsData.sortByDescending { it.date }

    LazyColumn(
        verticalArrangement = Arrangement.spacedBy(8.dp),
        modifier = Modifier.padding(8.dp)
    ) {
        items(pastRunsData) { pastRunData ->
            PastRunItem(pastRunData)
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun HistoryScreenPreview() {
    HistoryScreen()
}
