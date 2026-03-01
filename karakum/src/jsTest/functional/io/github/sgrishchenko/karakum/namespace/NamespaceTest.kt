package io.github.sgrishchenko.karakum.namespace

import io.github.sgrishchenko.karakum.configuration.NamespaceStrategy
import io.github.sgrishchenko.karakum.configuration.ignore
import io.github.sgrishchenko.karakum.configuration.`package`
import io.github.sgrishchenko.karakum.generateTests
import io.github.sgrishchenko.karakum.util.ruleOf
import kotlinx.coroutines.test.runTest
import kotlin.test.Test

class NamespaceTest {
    @Test
    fun test() = runTest {
        generateTests("namespace") { testOutput ->
            input = listOf("**/*.d.ts")
            output = testOutput
            libraryName = "sandbox-namespace"
            packageNameMapper = mapOf(
                "will/be/mapped/andthis" to "was/mapped/nested",
                "will/be/mapped" to "was/mapped/main"
            )
            moduleNameMapper = mapOf(
                "will-be-mapped#AndThis" to "was-mapped#Nested",
                "will-be-erased" to ""
            )
            namespaceStrategy = mapOf(
                "package-namespace.NestedNamespace" to NamespaceStrategy.`package`,
                "IgnoreNamespace" to NamespaceStrategy.ignore,
                "will-be-mapped.AndThis" to NamespaceStrategy.`package`,
            )
            importMapper = mapOf(
                "^import-provider" to ruleOf("import.provider"),
                "other-import-provider" to ruleOf(
                    "default" to "other.import.provider.X",
                    "\\*" to "",
                    ".+" to "other.import.provider.",
                )
            )
            importInjector = mapOf(
                "import/consumer/ignored.kt" to listOf(
                    "ignored.import.Ignored",
                ),
                "import/consumer/otherIgnored.kt" to listOf(
                    "ignored.import.other as OtherIgnored",
                ),
            )
        }
    }
}
