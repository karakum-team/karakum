package js.file.download.karakum

import io.github.sgrishchenko.karakum.generate
import js.array.ReadonlyArray
import js.file.download.karakum.plugins.convertSkippedGenerics
import js.objects.unsafeJso

suspend fun main(args: ReadonlyArray<String>) {
    generate(args) {
        input = listOf("js-file-download.d.ts")
        isolatedOutputPackage = true
        plugins = listOf(
            convertSkippedGenerics
        )
        compilerOptions = unsafeJso {
            lib = arrayOf(
                "lib.esnext.d.ts",
                "lib.dom.d.ts"
            )
        }
    }
}
