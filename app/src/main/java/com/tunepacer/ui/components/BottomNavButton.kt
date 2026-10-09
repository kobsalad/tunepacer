package com.tunepacer.ui.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.height
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawWithContent
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.unit.dp

@Composable
fun RowScope.BottomNavButton(
    onClick: () -> Unit,
    selected: Boolean,
    icon: @Composable () -> Unit,
    label: String
) {
    val primaryColor = MaterialTheme.colorScheme.primary
    Button(
        onClick = onClick,
        modifier = Modifier
            .height(56.dp)
            .weight(1f)
            .drawWithContent {
                drawContent()

                val strokeWidthPx = (if (selected) 3.dp else 1.dp).toPx()

                drawLine(
                    color = primaryColor,
                    start = Offset(x = 0f, y = strokeWidthPx / 2),
                    end = Offset(x = size.width, y = strokeWidthPx / 2),
                    strokeWidth = strokeWidthPx
                )
            },
        shape = RectangleShape,
        colors =
            if (selected) ButtonDefaults.buttonColors(
                containerColor = MaterialTheme.colorScheme.surfaceContainerHigh,
                contentColor = MaterialTheme.colorScheme.onSurface
            ) else ButtonDefaults.buttonColors(
                containerColor = MaterialTheme.colorScheme.surfaceContainer,
                contentColor = MaterialTheme.colorScheme.onSurface
            )
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            icon()
            Text(label)
        }
    }
}
