/*
 * Provides the current existing Gecko runtime, or creates one if needed. Ensures that duplicate
 * runtimes aren't created.
 */

package com.tunepacer.spotify

import android.app.Application
import org.mozilla.geckoview.GeckoRuntime

object GeckoRuntimeHolder {
    private var runtime: GeckoRuntime? = null

    fun get(context: Application): GeckoRuntime {
        return runtime ?: GeckoRuntime.create(context.applicationContext)
            .also { runtime = it }
    }
}
