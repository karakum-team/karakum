package io.github.sgrishchenko.karakum

import io.github.sgrishchenko.karakum.configuration.PartialConfiguration
import js.coroutines.internal.IsolatedCoroutineScope
import js.coroutines.promise
import js.objects.jso
import js.promise.Promise
import node.buffer.BufferEncoding.Companion.utf8
import node.fs.readFile
import node.process.process
import node.util.ParseArgsConfig
import node.util.ParseArgsOptionConfigType.Companion.string
import node.util.parseArgs

internal suspend fun cli() {
    val config = jso<ParseArgsConfig> {
        args = process.argv.drop(2).toTypedArray()
        options = jso {
            this["config"] = jso {
                type = string
            }
        }
    }

    val results = parseArgs(config)

    val configuration = results.values["config"]
        ?.toString()
        ?.let { readFile(it, utf8) }
        ?.let { JSON.parse<PartialConfiguration>(it) }
        ?: error("Configuration file not found")

    generate(configuration)
}

@OptIn(ExperimentalJsExport::class)
@JsExport
@JsName("cli")
@Suppress("NON_EXPORTABLE_TYPE")
fun cliAsync(): Promise<Unit> =
    IsolatedCoroutineScope()
        .promise { cli() }
