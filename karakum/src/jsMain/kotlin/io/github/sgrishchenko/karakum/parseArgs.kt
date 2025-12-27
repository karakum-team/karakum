package io.github.sgrishchenko.karakum

import io.github.sgrishchenko.karakum.configuration.PartialConfiguration
import io.github.sgrishchenko.karakum.configuration.SchemaConfiguration
import io.github.sgrishchenko.karakum.configuration.reifyConfiguration
import io.github.sgrishchenko.karakum.util.Many
import js.array.ReadonlyArray
import js.coroutines.promise
import js.json.parse
import js.objects.unsafeJso
import js.promise.Promise
import kotlinx.coroutines.CoroutineScope
import node.buffer.BufferEncoding.Companion.utf8
import node.fs.readFile
import node.util.ParseArgsConfig
import node.util.ParseArgsOptionsType.Companion.string
import node.util.parseArgs
import kotlin.coroutines.EmptyCoroutineContext

suspend fun parseArgs(args: ReadonlyArray<String>): PartialConfiguration {
    val config = unsafeJso<ParseArgsConfig> {
        this.args = args
        options = unsafeJso {
            this["input"] = unsafeJso {
                type = string
                multiple = true
            }

            this["output"] = unsafeJso {
                type = string
            }

            this["library-name"] = unsafeJso {
                type = string
            }

            this["config"] = unsafeJso {
                type = string
            }
        }
    }

    val results = parseArgs(config)

    val input = results.values["input"]?.unsafeCast<Many<String>>()
    val output = results.values["output"]?.toString()
    val libraryName = results.values["library-name"]?.toString()

    val configuration = results.values["config"]?.toString()
        ?.let { readFile(it, utf8) }
        ?.let { parse<SchemaConfiguration>(it) }
        ?.let { reifyConfiguration(it) }

    return configuration
        ?.let {
            PartialConfiguration.copy(
                it,
                input = input ?: it.input,
                output = output ?: it.output,
                libraryName = libraryName ?: it.libraryName,
            )
        }
        ?: PartialConfiguration(
            input = input,
            output = output,
            libraryName = libraryName
        )
}

@JsExport
@JsName("parseArgs")
fun parseArgsAsync(args: Array<String>): Promise<PartialConfiguration> =
    CoroutineScope(EmptyCoroutineContext)
        .promise { parseArgs(args) }
