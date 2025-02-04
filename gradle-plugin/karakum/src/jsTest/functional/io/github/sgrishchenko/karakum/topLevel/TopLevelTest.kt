package io.github.sgrishchenko.karakum.topLevel

import io.github.sgrishchenko.karakum.configuration.PartialConfiguration
import io.github.sgrishchenko.karakum.generateTests
import js.objects.recordOf
import kotlinx.coroutines.test.runTest
import kotlin.test.Test

class TopLevelTest {
    @Test
    fun test() = runTest {
        generateTests("topLevel") { output ->
            recordOf<String, Any?>().apply {
                this["input"] = "**/*.d.ts"
                this["output"] = output
                this["libraryName"] = "sandbox-top-level"
                this["granularity"] = "top-level"
                this["moduleNameMapper"] = recordOf(
                    "^(?!sandbox-top-level/myFunction1$).*" to "sandbox-top-level"
                )
                this["verbose"] = true
            }.unsafeCast<PartialConfiguration>()
        }
    }
}
