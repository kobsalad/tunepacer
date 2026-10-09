package com.tunepacer.ui.util

import androidx.compose.animation.AnimatedContentTransitionScope
import androidx.compose.animation.ContentTransform
import androidx.compose.animation.core.tween
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.animation.togetherWith
import androidx.navigation3.runtime.NavKey
import androidx.navigation3.scene.Scene

fun AnimatedContentTransitionScope<Scene<NavKey>>.horizontalTabTransition(tabs: List<NavKey>): ContentTransform {
    val strTabs = tabs.map { it.toString() }

    val direction = if (strTabs.indexOf(targetState.key) > strTabs.indexOf(initialState.key)) 1 else -1

    return slideInHorizontally(
        animationSpec = tween(200),
        initialOffsetX = { it * direction }
    ) togetherWith slideOutHorizontally(
        animationSpec = tween(200),
        targetOffsetX = { -it * direction }
    )
}
