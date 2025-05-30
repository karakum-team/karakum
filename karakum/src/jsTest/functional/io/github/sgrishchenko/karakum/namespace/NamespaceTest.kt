package io.github.sgrishchenko.karakum.namespace

import io.github.sgrishchenko.karakum.configuration.Granularity
import io.github.sgrishchenko.karakum.configuration.NamespaceStrategy
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
            granularity = Granularity.topLevel
            packageNameMapper = recordOf(
                "will/be/mapped/andthis" to "was/mapped/nested",
                "will/be/mapped" to "was/mapped/main"
            )
            moduleNameMapper = recordOf(
                "will-be-mapped#AndThis" to "was-mapped#Nested"
            )
            namespaceStrategy = recordOf(
                "package-namespace.ObjectNamespace" to NamespaceStrategy.`object`,
                "package-namespace" to NamespaceStrategy.`package`,
                "IgnoreNamespace" to NamespaceStrategy.ignore,
                "will-be-mapped" to NamespaceStrategy.`package`,

                "import-provider" to NamespaceStrategy.`package`,
                "other-import-provider" to NamespaceStrategy.`package`,
                "ignored-import" to NamespaceStrategy.`package`,
                "import-consumer" to NamespaceStrategy.`package`,
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
