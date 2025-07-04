package io.github.sgrishchenko.karakum

import io.github.sgrishchenko.karakum.configuration.SchemaConfiguration
import io.github.sgrishchenko.karakum.configuration.reifyConfiguration
import js.coroutines.internal.IsolatedCoroutineScope
import js.coroutines.promise
import js.json.parse
import js.objects.unsafeJso
import js.promise.Promise
import node.buffer.BufferEncoding.Companion.utf8
import node.fs.readFile
import node.process.process
import node.util.ParseArgsConfig
import node.util.ParseArgsOptionConfigType.Companion.string
import node.util.parseArgs

internal suspend fun cli() {
    val config = unsafeJso<ParseArgsConfig> {
        args = process.argv.drop(2).toTypedArray()
        options = unsafeJso {
            this["config"] = unsafeJso {
                type = string
            }
        }
    }

    val results = parseArgs(config)

    val configuration = results.values["config"]
        ?.toString()
        ?.let { readFile(it, utf8) }
        ?.let { parse<SchemaConfiguration>(it) }
        ?: error("Configuration file not found")

    val partialConfiguration = reifyConfiguration(configuration)

    generate(partialConfiguration)
}

@JsExport
@JsName("cli")
fun cliAsync(): Promise<Unit> =
    IsolatedCoroutineScope()
        .promise { cli() }
