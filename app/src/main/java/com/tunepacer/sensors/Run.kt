package com.tunepacer.sensors

import android.content.Context
import android.os.SystemClock
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch
import kotlin.time.Duration.Companion.milliseconds

object Run {
    private val _time = MutableStateFlow(0)
    val time = _time.asStateFlow() // [s]

    // Initialize both sensors
    fun init(context: Context) {
        Gps.init(context)
        Pedometer.init(context)
    }

    private var startTime = 0L
    private var timerJob: Job? = null
    private val scope = CoroutineScope(SupervisorJob() + Dispatchers.Default)
    // Starts the timer and both sensors
    fun startRun(context: Context) {
        Gps.startRun(context)
        Pedometer.startRun()

        _time.value = 0

        startTime = SystemClock.elapsedRealtime()

        timerJob?.cancel()
        timerJob = scope.launch {
            while (isActive) {
                val elapsed = SystemClock.elapsedRealtime() - startTime
                _time.value = (elapsed / 1000).toInt()
                delay((1000 - elapsed % 1000).milliseconds)
            }
        }
    }

    fun stopRun() {
        timerJob?.cancel()
        timerJob = null

        Gps.stopRun()
        Pedometer.stopRun()
    }
}
