package io.github.sgrishchenko.karakum.namespace

import io.github.sgrishchenko.karakum.configuration.NamespaceStrategy
import io.github.sgrishchenko.karakum.configuration.ignore
import io.github.sgrishchenko.karakum.configuration.`object`
import io.github.sgrishchenko.karakum.configuration.`package`
import io.github.sgrishchenko.karakum.generateTests
import io.github.sgrishchenko.karakum.util.manyOf
import io.github.sgrishchenko.karakum.util.ruleOf
import js.objects.recordOf
import kotlinx.coroutines.test.runTest
import kotlin.test.Test

class NamespaceTest {
    @Test
    fun test() = runTest {
        generateTests("namespace") { testOutput ->
            input = manyOf("**/*.d.ts")
            output = testOutput
            libraryName = "sandbox-namespace"
            packageNameMapper = recordOf(
                "will/be/mapped/andthis" to "was/mapped/nested",
                "will/be/mapped" to "was/mapped/main"
            )
            moduleNameMapper = recordOf(
                "will-be-mapped#AndThis" to "was-mapped#Nested",
                "will-be-erased" to ""
            )
            namespaceStrategy = recordOf(
                "package-namespace.NestedNamespace" to NamespaceStrategy.`package`,
                "IgnoreNamespace" to NamespaceStrategy.ignore,
                "will-be-mapped.AndThis" to NamespaceStrategy.`package`,
            )
            importMapper = recordOf(
                "^import-provider" to ruleOf("import.provider"),
                "other-import-provider" to ruleOf(
                    "default" to "other.import.provider.X",
                    "\\*" to "",
                    ".+" to "other.import.provider.",
                )
            )
            importInjector = recordOf(
                "import/consumer/ignored.kt" to arrayOf(
                    "ignored.import.Ignored",
                ),
                "import/consumer/otherIgnored.kt" to arrayOf(
                    "ignored.import.other as OtherIgnored",
                ),
            )
        }
    }
}
