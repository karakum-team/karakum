package js.file.download.karakum

import io.github.sgrishchenko.karakum.configuration.NamespaceStrategy
import io.github.sgrishchenko.karakum.generate
import io.github.sgrishchenko.karakum.util.manyOf
import js.array.ReadonlyArray
import js.file.download.karakum.plugins.convertSkippedGenerics
import js.import.import
import js.objects.recordOf
import node.path.path
import node.url.fileURLToPath

suspend fun main(args: ReadonlyArray<String>) {
    val jsFileDownloadPackage = import.meta.resolve("js-file-download/package.json")
        .let { fileURLToPath(it) }
        .let { path.dirname(it) }

    generate(args) {
        input = manyOf("$jsFileDownloadPackage/js-file-download.d.ts")
        libraryName = "js-file-download"
        moduleNameMapper = recordOf(
            ".+" to "js-file-download",
        )
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
