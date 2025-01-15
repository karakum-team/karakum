import org.jetbrains.kotlin.gradle.targets.js.nodejs.NodeJsPlugin
import org.jetbrains.kotlin.gradle.targets.js.nodejs.NodeJsRootExtension

plugins {
    kotlin("multiplatform") apply false
}

plugins.withType<NodeJsPlugin> {
    the<NodeJsRootExtension>().apply {
        version = "22.12.0"

        versions.apply {
            val typescriptVersion: String by project
            typescript.version = typescriptVersion
        }
    }
}
