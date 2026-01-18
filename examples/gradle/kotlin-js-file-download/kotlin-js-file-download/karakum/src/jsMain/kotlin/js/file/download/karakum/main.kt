package js.file.download.karakum

import io.github.sgrishchenko.karakum.generate
import io.github.sgrishchenko.karakum.util.manyOf
import js.array.ReadonlyArray
import js.file.download.karakum.plugins.convertSkippedGenerics
import js.objects.recordOf

suspend fun main(args: ReadonlyArray<String>) {
    generate(args) {
        input = manyOf("js-file-download.d.ts")
        isolatedOutputPackage = true
        plugins = manyOf(
            convertSkippedGenerics
        )
        importInjector = recordOf(
            "fileDownload.kt" to arrayOf(
                "js.buffer.ArrayBuffer",
                "js.buffer.ArrayBufferView",
                "web.blob.Blob",
            )
        )
    }
}
