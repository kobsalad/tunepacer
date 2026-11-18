package com.tunepacer.sensors

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import android.os.SystemClock
import androidx.core.content.ContextCompat
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch
import kotlin.math.roundToInt
import kotlin.time.Duration.Companion.milliseconds

object Pedometer : SensorEventListener {
    private val _cadence = MutableStateFlow(0)
    val cadence = _cadence.asStateFlow() // [steps/min]

    private val _steps = MutableStateFlow(0)
    val steps = _steps.asStateFlow() // [steps]

    private lateinit var sensorManager: SensorManager
    private var stepCounterSensor: Sensor? = null
    private var stepDetectorSensor: Sensor? = null

    fun init(context: Context) {
        val appContext = context.applicationContext
        sensorManager = appContext.getSystemService(Context.SENSOR_SERVICE) as SensorManager
        stepCounterSensor = sensorManager.getDefaultSensor(Sensor.TYPE_STEP_COUNTER)
        stepDetectorSensor = sensorManager.getDefaultSensor(Sensor.TYPE_STEP_DETECTOR)
    }

    private var isRunning = false
    private var initialHardwareSteps: Int? = null
    private var lastStepTimestampNs: Long = 0L
    private var timeoutJob: Job? = null
    private val scope = CoroutineScope(SupervisorJob() + Dispatchers.Default)

    fun startRun(context: Context) {
        _steps.value = 0
        _cadence.value = 0
        initialHardwareSteps = null
        lastStepTimestampNs = 0L
        isRunning = true

        timeoutJob?.cancel()
        timeoutJob = scope.launch {
            while (isActive) {
                delay(1000.milliseconds)
                if (lastStepTimestampNs != 0L && SystemClock.elapsedRealtimeNanos() - lastStepTimestampNs > 3_000_000_000L) {
                    _cadence.value = 0
                }
            }
        }

        if (
            ContextCompat.checkSelfPermission(
                context, Manifest.permission.ACTIVITY_RECOGNITION
            ) == PackageManager.PERMISSION_GRANTED
        ) {
            stepCounterSensor?.let {
                sensorManager.registerListener(this, it, SensorManager.SENSOR_DELAY_NORMAL)
            }
            stepDetectorSensor?.let {
                sensorManager.registerListener(this, it, SensorManager.SENSOR_DELAY_FASTEST)
            }
        }
    }

    fun stopRun() {
        isRunning = false
        timeoutJob?.cancel()
        timeoutJob = null
        _cadence.value = 0
        sensorManager.unregisterListener(this)
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
                    if (deltaSeconds in 0.15f..3.0f) {
                        val rawCadence = (60f / deltaSeconds).toInt()
                        // Smoothly transition to the new cadence
                        _cadence.value = if (_cadence.value == 0) rawCadence else (_cadence.value * 0.7f + rawCadence * 0.3f).roundToInt()
                    }
                }
                lastStepTimestampNs = currentTimestampNs
            }
        }
    }

    override fun onAccuracyChanged(sensor: Sensor?, accuracy: Int) {}
}
