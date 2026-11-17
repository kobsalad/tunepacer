package com.tunepacer.sensors

import android.content.Context
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow

object Pedometer : SensorEventListener {
    private val _cadence = MutableStateFlow(0)
    val cadence = _cadence.asStateFlow() // [steps/min]

    private val _steps = MutableStateFlow(0)
    val steps = _steps.asStateFlow() // [steps]

    private var sensorManager: SensorManager? = null
    private var stepCounterSensor: Sensor? = null
    private var stepDetectorSensor: Sensor? = null

    private var isRunning = false
    private var initialHardwareSteps: Int? = null
    private var lastStepTimestampNs: Long = 0L

    fun init(context: Context) {
        val appContext = context.applicationContext
        sensorManager = appContext.getSystemService(Context.SENSOR_SERVICE) as SensorManager
        stepCounterSensor = sensorManager?.getDefaultSensor(Sensor.TYPE_STEP_COUNTER)
        stepDetectorSensor = sensorManager?.getDefaultSensor(Sensor.TYPE_STEP_DETECTOR)
    }

    fun startRun() {
        _steps.value = 0
        _cadence.value = 0
        initialHardwareSteps = null
        lastStepTimestampNs = 0L
        isRunning = true

        stepCounterSensor?.let {
            sensorManager?.registerListener(this, it, SensorManager.SENSOR_DELAY_NORMAL)
        }
        stepDetectorSensor?.let {
            sensorManager?.registerListener(this, it, SensorManager.SENSOR_DELAY_FASTEST)
        }
    }

    fun stopRun() {
        sensorManager?.unregisterListener(this)
    }

    override fun onSensorChanged(event: SensorEvent?) {
        if (!isRunning || event == null) return

        when (event.sensor.type) {
            Sensor.TYPE_STEP_COUNTER -> {
                val totalHardwareSteps = event.values[0].toInt()
                if (initialHardwareSteps == null) {
                    initialHardwareSteps = totalHardwareSteps
                }
                _steps.value = totalHardwareSteps - (initialHardwareSteps ?: totalHardwareSteps)
            }

            Sensor.TYPE_STEP_DETECTOR -> {
                val currentTimestampNs = event.timestamp
                if (lastStepTimestampNs != 0L) {
                    val deltaSeconds = (currentTimestampNs - lastStepTimestampNs) / 1_000_000_000f
                    if (deltaSeconds > 0f) {
                        _cadence.value = (60f / deltaSeconds).toInt()
                    }
                }
                lastStepTimestampNs = currentTimestampNs
            }
        }
    }

    override fun onAccuracyChanged(sensor: Sensor?, accuracy: Int) {}
}
