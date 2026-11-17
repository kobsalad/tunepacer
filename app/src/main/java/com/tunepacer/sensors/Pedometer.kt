package com.tunepacer.sensors

import android.content.Context
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow

object Pedometer {
    private val _cadence = MutableStateFlow(0)
    val cadence = _cadence.asStateFlow() // [steps/min]

    private val _steps = MutableStateFlow(0)
    val steps = _steps.asStateFlow() // [steps]

    fun init(context: Context) {

    }

    fun startRun(context: Context) {

    }

    fun stopRun() {

    }
}
