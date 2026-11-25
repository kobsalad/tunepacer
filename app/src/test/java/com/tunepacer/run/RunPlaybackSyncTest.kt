package com.tunepacer.run

import org.junit.Assert.assertEquals
import org.junit.Test

class RunPlaybackSyncTest {
    @Test
    fun adaptiveRateUsesBaseAndFastPaceTargets() {
        assertEquals(1f, adaptivePlaybackRate(speedForPace(540), 540, 360), 0.001f)
        assertEquals(1.5f, adaptivePlaybackRate(speedForPace(360), 540, 360), 0.001f)
        assertEquals(1.25f, adaptivePlaybackRate(speedForPace(450), 540, 360), 0.001f)
    }

    @Test
    fun adaptiveRateStaysAtBaseWhenNotMoving() {
        assertEquals(1f, adaptivePlaybackRate(0f, 540, 360), 0.001f)
    }

    private fun speedForPace(secondsPerMile: Int) = 1609.344f / secondsPerMile
}
