package io.github.sgrishchenko.karakum.namespace

import io.github.sgrishchenko.karakum.configuration.NamespaceStrategy
import io.github.sgrishchenko.karakum.configuration.PartialConfiguration
import io.github.sgrishchenko.karakum.generateTests
import io.github.sgrishchenko.karakum.util.ruleOf
import js.objects.recordOf
import kotlinx.coroutines.test.runTest
import kotlin.test.Test

class NamespaceTest {
    @Test
    fun test() = runTest {
        generateTests("namespace") { output ->
            recordOf<String, Any?>().apply {
                this["input"] = "**/*.d.ts"
                this["output"] = output
                this["libraryName"] = "sandbox-namespace"
                this["granularity"] = "top-level"
                this["packageNameMapper"] = recordOf(
                    "will/be/mapped/andthis" to "was/mapped/nested",
                    "will/be/mapped" to "was/mapped/main"
                )
                this["moduleNameMapper"] = recordOf(
                    "will-be-mapped#AndThis" to "was-mapped#Nested"
                )
                this["namespaceStrategy"] = recordOf(
                    "package-namespace.ObjectNamespace" to NamespaceStrategy.`object`,
                    "package-namespace" to NamespaceStrategy.`package`,
                    "IgnoreNamespace" to NamespaceStrategy.ignore,
                    "will-be-mapped" to NamespaceStrategy.`package`,

                    "import-provider" to NamespaceStrategy.`package`,
                    "other-import-provider" to NamespaceStrategy.`package`,
                    "import-consumer" to NamespaceStrategy.`package`,
                )
                this["importMapper"] = recordOf(
                    "^import-provider" to ruleOf("import.provider"),
                    "other-import-provider" to ruleOf(
                        "default" to "other.import.provider.X",
                        "\\*" to "",
                        ".+" to "other.import.provider.",
                    )
                )
                this["verbose"] = true
            }.unsafeCast<PartialConfiguration>()
        }
    }
}
