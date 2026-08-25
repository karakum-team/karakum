package io.github.sgrishchenko.karakum

import io.github.sgrishchenko.karakum.configuration.PartialConfiguration
import io.github.sgrishchenko.karakum.configuration.SchemaConfiguration
import io.github.sgrishchenko.karakum.configuration.reifyConfiguration
import io.github.sgrishchenko.karakum.util.Many
import js.array.ReadonlyArray
import js.coroutines.promise
import js.json.parse
import js.objects.recordOf
import js.promise.Promise
import node.buffer.BufferEncoding
import node.buffer.utf8
import node.fs.readFile
import node.util.*
import web.abort.Abortable
import web.abort.asCoroutineScope

suspend fun parseArgs(args: ReadonlyArray<String>): PartialConfiguration {
    val config = ParseArgsConfig(
        args = args,
        options = recordOf(
            "input" to ParseArgsOptionDescriptor(
                type = ParseArgsOptionsType.string,
                multiple = true
            ),

            "output" to ParseArgsOptionDescriptor(
                type = ParseArgsOptionsType.string,
            ),

            "library-name" to ParseArgsOptionDescriptor(
                type = ParseArgsOptionsType.string,
            ),

            "config" to ParseArgsOptionDescriptor(
                type = ParseArgsOptionsType.string,
            ),
        )
    )

    val results = parseArgs(config)

    val input = results.values["input"]?.unsafeCast<Many<String>>()
    val output = results.values["output"]?.toString()
    val libraryName = results.values["library-name"]?.toString()

    val configuration = results.values["config"]?.toString()
        ?.let { readFile(it, BufferEncoding.utf8) }
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
fun parseArgsAsync(args: Array<String>, options: Abortable = Abortable()): Promise<PartialConfiguration> =
    options.asCoroutineScope()
        .promise { parseArgs(args) }
