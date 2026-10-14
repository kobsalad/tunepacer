package com.tunepacer.ui.util

import java.time.Instant
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import java.time.format.FormatStyle

fun Long.formatDate(): String =
    Instant.ofEpochSecond(this)
        .atZone(ZoneId.systemDefault())
        .format(
            DateTimeFormatter.ofLocalizedDate(FormatStyle.MEDIUM)
        )

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

fun formatPace(distance: Double, time: Int): String =
    (time / distance.metersToMiles())
        .toInt().formatTime()

fun Double.formatDistance(unit: String): String =
    "%.2f %s".format(this, unit)

fun Double.metersToMiles(): Double = this / 1609.344
fun Double.milesToMeters(): Double = this * 1609.344
