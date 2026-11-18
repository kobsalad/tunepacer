package com.tunepacer.ui.util

import java.time.Instant
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import java.time.format.FormatStyle
import kotlin.math.roundToInt
import kotlin.math.truncate

// Format Long [epoch s] to "Jul 11, 2026"
fun Long.formatDate(): String =
    Instant.ofEpochSecond(this)
        .atZone(ZoneId.systemDefault())
        .format(
            DateTimeFormatter.ofLocalizedDate(FormatStyle.MEDIUM)
        )

// Format Int [s] to "12:31"
fun Int.formatTime(): String {
    var time = ""
    if (this / 3600 > 0) {
        time += "${ this / 3600 }:"
        time += "%02d:".format(this % 3600 / 60)
    } else {
        time += "%d:".format(this % 3600 / 60)
    }
    time += "%02d".format(this % 60)
    return time
}

// Format Float [m/s] to "6:41"
fun Float.formatPace(): String {
    if (!isFinite() || this <= 0f) return "--:--"

    val secondsPerMile = 1609.344f / this
    if (secondsPerMile >= 3600f) return "--:--"

    return secondsPerMile.roundToInt().formatTime()
}

fun formatCadence(cadence: Int): String {
    return "$cadence spm"
}

fun formatCadence(steps: Int, time: Int): String {
    return if (time <= 0) {
        "--- spm"
    } else {
        "${(steps.toFloat() / time * 60).roundToInt()} spm"
    }
}

// Format Float [mi] to "5.34 mi"
fun Float.formatDistance(unit: String): String =
    "%.2f %s".format(truncate(this * 100) / 100, unit)

fun Float.metersToMiles(): Float = this / 1609.344f
