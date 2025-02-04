package io.github.sgrishchenko.karakum.base

import io.github.sgrishchenko.karakum.configuration.PartialConfiguration
import io.github.sgrishchenko.karakum.generateTests
import js.objects.recordOf
import kotlinx.coroutines.test.runTest
import kotlin.test.Test

class BaseTest {
    @Test
    fun test() = runTest {
        generateTests("base") { output ->
            recordOf<String, Any?>().apply {
                this["input"] = "**/*.d.ts"
                this["output"] = output
                this["libraryName"] = "sandbox-base"
                this["verbose"] = true
            }.unsafeCast<PartialConfiguration>()
        }
    }
}
