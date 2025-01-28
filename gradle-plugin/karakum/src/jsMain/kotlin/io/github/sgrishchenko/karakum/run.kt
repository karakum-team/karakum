package io.github.sgrishchenko.karakum

import io.github.sgrishchenko.karakum.configuration.PartialConfiguration
import io.github.sgrishchenko.karakum.configuration.reifyConfiguration
import js.coroutines.internal.IsolatedCoroutineScope
import js.coroutines.promise
import js.promise.Promise

suspend fun run(partialConfiguration: PartialConfiguration) {
    val configuration = reifyConfiguration(partialConfiguration)

    generate(configuration)
}

@OptIn(ExperimentalJsExport::class)
@JsExport
@JsName("run")
@Suppress("NON_EXPORTABLE_TYPE")
fun runAsync(partialConfiguration: PartialConfiguration): Promise<Unit> =
    IsolatedCoroutineScope()
        .promise { run(partialConfiguration) }
