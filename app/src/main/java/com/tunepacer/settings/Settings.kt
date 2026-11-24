package com.tunepacer.settings

import android.content.Context
import android.content.SharedPreferences
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.core.content.edit

object Settings {
    private const val PREFS_NAME = "tunepacer_settings"
    private lateinit var prefs: SharedPreferences

    // Main free run settings
    private val _syncTypeAdaptive = mutableStateOf(true)
    var syncTypeAdaptive: Boolean
        get() = _syncTypeAdaptive.value
        set(value) {
            _syncTypeAdaptive.value = value
            prefs.edit { putBoolean("sync_type_adaptive", value) }
        }

    private val _syncWithCadence = mutableStateOf(true)
    var syncWithCadence: Boolean
        get() = _syncWithCadence.value
        set(value) {
            _syncWithCadence.value = value
            prefs.edit { putBoolean("sync_with_cadence", value) }
        }

    // Constant pace
    private val _constantPaceMultiplier = mutableFloatStateOf(1.5f)
    var constantPaceMultiplier: Float
        get() = _constantPaceMultiplier.floatValue
        set(value) {
            _constantPaceMultiplier.floatValue = value
            prefs.edit { putFloat("constant_pace_multiplier", value) }
        }

    // Adaptive pace
    private val _adaptivePaceBase = mutableIntStateOf(540)
    var adaptivePaceBase: Int
        get() = _adaptivePaceBase.intValue
        set(value) {
            _adaptivePaceBase.intValue = value
            prefs.edit { putInt("adaptive_pace_base", value) }
        }

    private val _adaptivePaceFast = mutableIntStateOf(360)
    var adaptivePaceFast: Int
        get() = _adaptivePaceFast.intValue
        set(value) {
            _adaptivePaceFast.intValue = value
            prefs.edit { putInt("adaptive_pace_fast", value) }
        }

    // Constant cadence
    private val _constantCadenceTarget = mutableIntStateOf(180)
    var constantCadenceTarget: Int
        get() = _constantCadenceTarget.intValue
        set(value) {
            _constantCadenceTarget.intValue = value
            prefs.edit { putInt("constant_cadence_target", value) }
        }

    // Adaptive cadence (none)

    // Initialize the shared preferences system using the context
    fun init(context: Context) {
        prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)

        _syncTypeAdaptive.value = prefs.getBoolean("sync_type_adaptive", true)
        _syncWithCadence.value = prefs.getBoolean("sync_with_cadence", true)

        _constantPaceMultiplier.floatValue = prefs.getFloat("constant_pace_multiplier", 1.5f)
        _adaptivePaceBase.intValue = prefs.getInt("adaptive_pace_base", 540)
        _adaptivePaceFast.intValue = prefs.getInt("adaptive_pace_fast", 360)
        _constantCadenceTarget.intValue = prefs.getInt("constant_cadence_target", 180)
    }
}
