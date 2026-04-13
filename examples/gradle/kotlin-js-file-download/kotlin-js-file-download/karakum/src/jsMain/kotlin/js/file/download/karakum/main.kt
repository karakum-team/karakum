package js.file.download.karakum

import io.github.sgrishchenko.karakum.generate
import js.array.ReadonlyArray
import js.file.download.karakum.plugins.convertSkippedGenerics

suspend fun main(args: ReadonlyArray<String>) {
    generate(args) {
        input = listOf("js-file-download.d.ts")
        isolatedOutputPackage = true
        plugins = listOf(
            convertSkippedGenerics
        )
    }
}
