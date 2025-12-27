package js.file.download.karakum

import io.github.sgrishchenko.karakum.configuration.Granularity
import io.github.sgrishchenko.karakum.configuration.NamespaceStrategy
import io.github.sgrishchenko.karakum.configuration.file
import io.github.sgrishchenko.karakum.configuration.ignore
import io.github.sgrishchenko.karakum.generate
import io.github.sgrishchenko.karakum.util.manyOf
import js.array.ReadonlyArray
import js.file.download.karakum.plugins.convertSkippedGenerics
import js.objects.recordOf

suspend fun main(args: ReadonlyArray<String>) {
    generate(args) {
        input = manyOf("js-file-download.d.ts")
        granularity = Granularity.file
        plugins = manyOf(
            convertSkippedGenerics
        )
        importInjector = recordOf(
            "jsFileDownload.kt" to arrayOf(
                "js.buffer.ArrayBuffer",
                "js.buffer.ArrayBufferView",
                "web.blob.Blob",
            )
        )
        namespaceStrategy = recordOf(
            "js-file-download" to NamespaceStrategy.ignore
        )
    }
}
