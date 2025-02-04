package io.github.sgrishchenko.karakum.bundle

import io.github.sgrishchenko.karakum.configuration.PartialConfiguration
import io.github.sgrishchenko.karakum.generateTests
import js.objects.recordOf
import kotlinx.coroutines.test.runTest
import kotlin.test.Test

class BundleTest {
    @Test
    fun test() = runTest {
        generateTests("bundle") { output ->
            recordOf<String, Any?>().apply {
                this["input"] = "**/*.d.ts"
                this["output"] = "${output}/customBundle.kt"
                this["libraryName"] = "sandbox-bundle"
                this["granularity"] = "bundle"
                this["verbose"] = true
            }.unsafeCast<PartialConfiguration>()
        }
    }
}
