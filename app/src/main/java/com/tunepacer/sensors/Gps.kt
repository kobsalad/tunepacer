package com.tunepacer.sensors

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.location.Location
import android.os.Looper
import androidx.core.content.ContextCompat
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationResult
import com.google.android.gms.location.LocationServices
import com.google.android.gms.location.Priority
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow

object Gps {
    private val _speed = MutableStateFlow(0f)
    val speed = _speed.asStateFlow() // [m/s]

    private val _distance = MutableStateFlow(0f)
    val distance = _distance.asStateFlow() // [m]

    private var previousLocation: Location? = null

    private lateinit var fusedLocationClient: FusedLocationProviderClient
    private lateinit var locationRequest: LocationRequest
    private lateinit var locationCallback: LocationCallback

    // Setup objects needed for startRun()
    fun init(context: Context) {
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(context)
        locationRequest = LocationRequest.Builder(Priority.PRIORITY_HIGH_ACCURACY, 1000)
            .setMinUpdateIntervalMillis(500)
            .build()
        locationCallback = object : LocationCallback() {
            override fun onLocationResult(result: LocationResult) {
                result.locations.forEach { location ->
                    // Filter out low-accuracy GPS jitter
                    if (location.hasAccuracy() && location.accuracy > 30f) return@forEach

                    previousLocation?.let {
                        _distance.value += it.distanceTo(location)
                    }

                    previousLocation = location

                    // If pace >= 60 min/mi, set speed to 0
                    _speed.value = if (location.hasSpeed() && location.speed > 0.44704f) location.speed else 0f
                }
            }
        }
    }

    // Start updating speed, distance, and time
    fun startRun(context: Context) {
        _speed.value = 0f
        _distance.value = 0f
        previousLocation = null

        if (
            ContextCompat.checkSelfPermission(
                context, Manifest.permission.ACCESS_FINE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED ||
            ContextCompat.checkSelfPermission(
                context, Manifest.permission.ACCESS_COARSE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED
        ) {
            fusedLocationClient.requestLocationUpdates(
                locationRequest,
                locationCallback,
                Looper.getMainLooper()
            )
        }
    }

    fun stopRun() {
        fusedLocationClient.removeLocationUpdates(locationCallback)
        _speed.value = 0f
    }
}
