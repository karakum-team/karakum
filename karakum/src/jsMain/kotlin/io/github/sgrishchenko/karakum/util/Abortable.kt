package io.github.sgrishchenko.karakum.util

import kotlinx.coroutines.currentCoroutineContext
import kotlinx.coroutines.job
import web.abort.AbortController
import web.abort.AbortSignal
import web.abort.Abortable
import kotlin.coroutines.CoroutineContext

fun CoroutineContext.asAbortSignal(): AbortSignal {
    val controller = AbortController()

    job.invokeOnCompletion {
        controller.abort()
    }

    return controller.signal
}

fun CoroutineContext.asAbortable(): Abortable = Abortable(asAbortSignal())

suspend fun currentAbortSignal(): AbortSignal = currentCoroutineContext().asAbortSignal()

suspend fun currentAbortable(): Abortable = currentCoroutineContext().asAbortable()
