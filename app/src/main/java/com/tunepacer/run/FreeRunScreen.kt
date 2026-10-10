package com.tunepacer.run

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview

@Composable
fun FreeRunScreen() {
    Text(
        "Free Run",
        modifier = Modifier.fillMaxSize()
    )
}

@Preview(showBackground = true)
@Composable
private fun FreeRunScreenPreview() {
    FreeRunScreen()
}
