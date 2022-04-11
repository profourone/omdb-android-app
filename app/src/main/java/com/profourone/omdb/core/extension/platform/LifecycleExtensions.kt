package com.profourone.omdb.core.extension.platform

import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch

/**
 * Taken and modified from
 * https://medium.com/androiddevelopers/repeatonlifecycle-api-design-story-8670d1a7d333
*/

private fun LifecycleOwner.addRepeatingJob(
    state: Lifecycle.State,
    block: suspend CoroutineScope.() -> Unit
): Job = lifecycleScope.launch {
    repeatOnLifecycle(state, block)
}

fun LifecycleOwner.repeatOnStarted(
    block: suspend CoroutineScope.() -> Unit
) = addRepeatingJob(Lifecycle.State.STARTED, block)
