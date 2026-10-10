package com.tunepacer.ui.components

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

@Composable
fun BottomNavBar(
    content: @Composable RowScope.() -> Unit
) {
    Row(
        modifier = Modifier
            .windowInsetsPadding(WindowInsets.navigationBars)
            .fillMaxWidth()
    ) {
        content()
    }
}
