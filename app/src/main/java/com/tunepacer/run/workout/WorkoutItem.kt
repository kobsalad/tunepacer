package com.tunepacer.run.workout

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import com.tunepacer.db.WorkoutEntity
import com.tunepacer.ui.components.NumericInput
import com.tunepacer.ui.util.clearFocusOnTap

@Composable
fun WorkoutItem(
    workoutData: WorkoutEntity,
    onStartWorkout: (Workout) -> Unit = {},
    onUpdateWorkout: (WorkoutEntity) -> Unit = {},
    onDeleteWorkout: () -> Unit = {}
) {
    var expanded by rememberSaveable { mutableStateOf(false) }
    var workoutName by remember(workoutData.id) { mutableStateOf(workoutData.workoutName) }
    var segments by remember(workoutData.id) { mutableStateOf(workoutData.segments) }
    val focusManager = LocalFocusManager.current

    fun updateWorkout(name: String = workoutName, updatedSegments: List<WorkoutSegment> = segments) {
        onUpdateWorkout(workoutData.copy(workoutName = name, segments = updatedSegments))
    }

    Surface (
        color = MaterialTheme.colorScheme.surfaceContainer,
        contentColor = MaterialTheme.colorScheme.onSurface
    ) {
        Column {
            Surface(
                onClick = { expanded = !expanded },
                color =
                    if (expanded) MaterialTheme.colorScheme.surfaceContainerHigh
                    else MaterialTheme.colorScheme.surfaceContainer,
                modifier = Modifier.fillMaxWidth()
            ) {
                Row (
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp)
                ) {
                    if (expanded) {
                        OutlinedTextField(
                            value = workoutName,
                            onValueChange = {
                                workoutName = it
                                updateWorkout(name = it)
                            },
                            singleLine = true,
                            keyboardOptions = KeyboardOptions(
                                keyboardType = KeyboardType.Text,
                                imeAction = ImeAction.Done
                            ),
                            keyboardActions = KeyboardActions(
                                onDone = { focusManager.clearFocus() }
                            ),
                            modifier = Modifier.width(160.dp)
                        )
                    } else {
                        Text(workoutName)
                    }
                    Text(if (expanded) "▼" else "◀")
                }
            }
            if (expanded) {
                Column(
                    verticalArrangement = Arrangement.spacedBy(4.dp),
                    modifier = Modifier
                        .padding(16.dp)
                        .clearFocusOnTap(focusManager)
                ) {
                    Row(
                        horizontalArrangement = Arrangement.spacedBy(16.dp),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Surface(
                            onClick = {
                                onStartWorkout(Workout(workoutName, segments))
                            },
                            modifier = Modifier.weight(1f),
                            color = MaterialTheme.colorScheme.primary,
                            contentColor = MaterialTheme.colorScheme.onPrimary
                        ) {
                            Text(
                                "Start Workout\n(Pace Mode)",
                                style = MaterialTheme.typography.labelLarge,
                                textAlign = TextAlign.Center,
                                modifier = Modifier.padding(16.dp)
                            )
                        }
                        Surface(
                            onClick = {
                                onStartWorkout(Workout(workoutName, segments))
                            },
                            modifier = Modifier.weight(1f),
                            color = MaterialTheme.colorScheme.primary,
                            contentColor = MaterialTheme.colorScheme.onPrimary
                        ) {
                            Text(
                                "Start Workout\n(Cadence Mode)",
                                style = MaterialTheme.typography.labelLarge,
                                textAlign = TextAlign.Center,
                                modifier = Modifier.padding(16.dp)
                            )
                        }
                    }
                    Spacer(Modifier.height(8.dp))
                    Row(
                        horizontalArrangement = Arrangement.spacedBy(4.dp),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Spacer(modifier = Modifier.weight(0.5f))
                        Text(
                            "Distance",
                            style = MaterialTheme.typography.labelLarge,
                            textAlign = TextAlign.Center,
                            modifier = Modifier.weight(1f)
                        )
                        Text(
                            "Multiplier",
                            style = MaterialTheme.typography.labelLarge,
                            textAlign = TextAlign.Center,
                            modifier = Modifier.weight(1f)
                        )
                        Text(
                            "Cadence",
                            style = MaterialTheme.typography.labelLarge,
                            textAlign = TextAlign.Center,
                            modifier = Modifier.weight(1f)
                        )
                    }
                    segments.forEachIndexed { index, segment ->
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(4.dp),
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            if (segments.size > 1) {
                                Surface(
                                    onClick = {
                                        val updatedSegments = segments.toMutableList().also { it.removeAt(index) }
                                        segments = updatedSegments
                                        updateWorkout(updatedSegments = updatedSegments)
                                    },
                                    modifier = Modifier
                                        .height(48.dp)
                                        .weight(0.5f),
                                    color = MaterialTheme.colorScheme.secondary,
                                    contentColor = MaterialTheme.colorScheme.onSecondary
                                ) {
                                    Box(
                                        contentAlignment = Alignment.Center,
                                        modifier = Modifier.fillMaxSize()
                                    ) {
                                        Text("x", style = MaterialTheme.typography.labelLarge)
                                    }
                                }
                            } else {
                                Spacer(modifier = Modifier.weight(0.5f))
                            }
                            NumericInput(
                                label = null,
                                value = segment.distance.toString(),
                                defaultValue = "400",
                                isDecimal = true,
                                modifier = Modifier.weight(1f),
                                onValueChange = { value ->
                                    val updatedSegments = segments.toMutableList().also {
                                        it[index] = segment.copy(distance = value.toFloat())
                                    }
                                    segments = updatedSegments
                                    updateWorkout(updatedSegments = updatedSegments)
                                }
                            )
                            NumericInput(
                                label = null,
                                value = segment.multiplier.toString(),
                                defaultValue = "1.5",
                                isDecimal = true,
                                maxValue = 4f,
                                modifier = Modifier.weight(1f),
                                onValueChange = { value ->
                                    val updatedSegments = segments.toMutableList().also {
                                        it[index] = segment.copy(multiplier = value.toFloat())
                                    }
                                    segments = updatedSegments
                                    updateWorkout(updatedSegments = updatedSegments)
                                }
                            )
                            NumericInput(
                                label = null,
                                value = segment.cadence.toString(),
                                defaultValue = "180",
                                maxIntegerValue = 300,
                                modifier = Modifier.weight(1f),
                                onValueChange = { value ->
                                    val updatedSegments = segments.toMutableList().also {
                                        it[index] = segment.copy(cadence = value.toInt())
                                    }
                                    segments = updatedSegments
                                    updateWorkout(updatedSegments = updatedSegments)
                                }
                            )
                        }
                    }
                    Row(modifier = Modifier.fillMaxWidth()) {
                        Surface(
                            onClick = {
                                val updatedSegments = segments + WorkoutSegment(400f, 1.5f, 180)
                                segments = updatedSegments
                                updateWorkout(updatedSegments = updatedSegments)
                            },
                            modifier = Modifier.height(48.dp),
                            color = MaterialTheme.colorScheme.tertiary,
                            contentColor = MaterialTheme.colorScheme.onTertiary
                        ) {
                            Box(
                                contentAlignment = Alignment.Center,
                                modifier = Modifier.fillMaxSize()
                            ) {
                                Text("New segment",
                                    style = MaterialTheme.typography.labelLarge)
                            }
                        }
                    }
                    Spacer(Modifier.height(12.dp))
                    Row(
                        horizontalArrangement = Arrangement.SpaceAround,
                        modifier = Modifier.fillMaxWidth()) {
                        Surface(
                            onClick = onDeleteWorkout,
                            modifier = Modifier.height(48.dp),
                            color = MaterialTheme.colorScheme.secondary,
                            contentColor = MaterialTheme.colorScheme.onSecondary
                        ) {
                            Box(
                                contentAlignment = Alignment.Center,
                                modifier = Modifier
                                    .fillMaxHeight()
                                    .padding(16.dp, 0.dp)
                            ) {
                                Text("Delete workout",
                                    style = MaterialTheme.typography.labelLarge)
                            }
                        }
                    }
                }
            }
        }
    }
}
