package io.github.sgrishchenko.karakum.cli

import io.github.sgrishchenko.karakum.configuration.PartialConfiguration
import js.core.Void
import js.import.import
import js.objects.jso
import js.promise.Promise
import node.buffer.BufferEncoding.Companion.utf8
import node.fs.readFile
import node.process.process
import node.util.ParseArgsConfig
import node.util.ParseArgsOptionConfigType.Companion.string
import node.util.parseArgs

external interface KarakumModule {
    fun generate(partialConfiguration: PartialConfiguration): Promise<Void>
}

suspend fun main() {
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

    // use dynamic import to share the same runtime
    val karakum = import<KarakumModule>("karakum")

    karakum.generate(configuration).await()
}
