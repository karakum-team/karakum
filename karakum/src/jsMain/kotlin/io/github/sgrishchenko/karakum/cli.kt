package io.github.sgrishchenko.karakum

import js.coroutines.promise
import js.promise.Promise
import kotlinx.coroutines.CoroutineScope
import node.process.process
import kotlin.coroutines.EmptyCoroutineContext

internal suspend fun cli() {
    val partialConfiguration = parseArgs(process.argv.drop(2).toTypedArray())

    generate(partialConfiguration)
}

@JsExport
@JsName("cli")
fun cliAsync(): Promise<Unit> =
    CoroutineScope(EmptyCoroutineContext)
        .promise { cli() }
