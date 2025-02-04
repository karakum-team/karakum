package io.github.sgrishchenko.karakum.extension

import io.github.sgrishchenko.karakum.configuration.PartialConfiguration
import io.github.sgrishchenko.karakum.generateTests
import js.objects.recordOf
import kotlinx.coroutines.test.runTest
import kotlin.test.Test

class ExtensionTest {
    @Test
    fun test() = runTest {
        generateTests("extension") { output ->
            recordOf<String, Any?>().apply {
                this["input"] = "**/*.d.ts"
                this["output"] = output
                this["libraryName"] = "extension"
                this["verbose"] = true
                this["extensions"] = "./extensions.js"
            }.unsafeCast<PartialConfiguration>()
        }
    }
}
