package com.tunepacer.run.freerun

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.text.TextRange
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.TextFieldValue

@Composable
fun PaceInput(
    label: String,
    seconds: Int,
    onSecondsChange: (Int) -> Unit
) {
    var digits by remember {
        val total = seconds.coerceAtLeast(0)
        val m = total / 60
        val s = total % 60
        mutableStateOf(if (m == 0 && s == 0) "" else "$m${"%02d".format(s)}".dropWhile { it == '0' })
    }

    var isFocused by remember { mutableStateOf(false) }

    val currentCalculatedSec = remember(digits) {
        val m = if (digits.length > 2) digits.dropLast(2).toIntOrNull() ?: 0 else 0
        val s = if (digits.length >= 2) digits.takeLast(2).toIntOrNull() ?: 0 else digits.toIntOrNull() ?: 0
        m * 60 + s
    }

    LaunchedEffect(seconds) {
        if (!isFocused && seconds != currentCalculatedSec) {
            val total = seconds.coerceAtLeast(0)
            val m = total / 60
            val s = total % 60
            digits = if (m == 0 && s == 0) "" else "$m${"%02d".format(s)}".dropWhile { it == '0' }
        }
    }

    // Display string: while focused show raw digits ("0:60"), on unfocus show normalized ("1:00")
    val displayText = if (isFocused) {
        val displayM = if (digits.length > 2) digits.dropLast(2) else "0"
        val displayS = if (digits.length >= 2) digits.takeLast(2) else digits.padStart(2, '0')
        "$displayM:$displayS"
    } else {
        val total = currentCalculatedSec
        val m = total / 60
        val s = total % 60
        "$m:${"%02d".format(s)}"
    }

    Row(
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier.fillMaxWidth()
    ) {
        Text(
            label,
            modifier = Modifier.weight(1f)
        )
        OutlinedTextField(
            value = TextFieldValue(
                text = displayText,
                selection = TextRange(displayText.length)
            ),
            onValueChange = { newValue ->
                val cleanDigits = newValue.text.filter { it.isDigit() }

                if (newValue.text.length < displayText.length) {
                    val updatedDigits = if (digits.isNotEmpty()) digits.dropLast(1) else ""
                    digits = updatedDigits
                    val m = if (updatedDigits.length > 2) updatedDigits.dropLast(2).toIntOrNull() ?: 0 else 0
                    val s = if (updatedDigits.length >= 2) updatedDigits.takeLast(2).toIntOrNull() ?: 0 else updatedDigits.toIntOrNull() ?: 0
                    onSecondsChange(m * 60 + s)
                } else if (cleanDigits.length <= 4) {
                    val updatedDigits = cleanDigits.dropWhile { it == '0' }
                    digits = updatedDigits
                    val m = if (updatedDigits.length > 2) updatedDigits.dropLast(2).toIntOrNull() ?: 0 else 0
                    val s = if (updatedDigits.length >= 2) updatedDigits.takeLast(2).toIntOrNull() ?: 0 else updatedDigits.toIntOrNull() ?: 0
                    onSecondsChange(m * 60 + s)
                }
            },
            singleLine = true,
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
            modifier = Modifier
                .weight(1f)
                .onFocusChanged { focusState ->
                    val wasFocused = isFocused
                    isFocused = focusState.isFocused
                    if (wasFocused && !focusState.isFocused) {
                        val total = currentCalculatedSec
                        val m = total / 60
                        val s = total % 60
                        digits = if (m == 0 && s == 0) "" else "$m${"%02d".format(s)}".dropWhile { it == '0' }
                        onSecondsChange(total)
                    }
                }
        )
    }
}

@Composable
fun NumericInput(
    label: String,
    value: String,
    defaultValue: String,
    onValueChange: (String) -> Unit,
    isDecimal: Boolean = false
) {
    var textValue by remember(value) { mutableStateOf(value) }

    Row(
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier.fillMaxWidth()
    ) {
        Text(
            label,
            modifier = Modifier.weight(1f)
        )
        OutlinedTextField(
            value = textValue,
            onValueChange = { input ->
                var hasDecimal = false
                val filtered = if (isDecimal) {
                    input.filter { ch ->
                        if (ch.isDigit()) true
                        else if (ch == '.' && !hasDecimal) {
                            hasDecimal = true
                            true
                        } else false
                    }
                } else {
                    input.filter { it.isDigit() }
                }

                textValue = filtered
                if (isDecimal) {
                    filtered.toFloatOrNull()?.let { onValueChange(filtered) }
                } else {
                    filtered.toIntOrNull()?.let { onValueChange(filtered) }
                }
            },
            singleLine = true,
            keyboardOptions = KeyboardOptions(
                keyboardType = if (isDecimal) KeyboardType.Decimal else KeyboardType.Number
            ),
            modifier = Modifier
                .weight(1f)
                .onFocusChanged { focusState ->
                    if (!focusState.isFocused) {
                        if (isDecimal) {
                            val parsed = textValue.toFloatOrNull()
                            if (parsed == null || parsed <= 0f) {
                                textValue = defaultValue
                                onValueChange(defaultValue)
                            }
                        } else {
                            val parsed = textValue.toIntOrNull()
                            if (parsed == null || parsed <= 0) {
                                textValue = defaultValue
                                onValueChange(defaultValue)
                            }
                        }
                    }
                }
        )
    }
}
