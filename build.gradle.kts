import org.jetbrains.kotlin.gradle.targets.js.nodejs.NodeJsPlugin
import org.jetbrains.kotlin.gradle.targets.js.nodejs.NodeJsRootExtension

plugins {
    alias(libs.plugins.kotlin.multiplatform) apply false
    alias(libs.plugins.kfc.latestTools)
}

plugins.withType<NodeJsPlugin> {
    val typescriptVersion: String by project

    the<NodeJsRootExtension>().versions.apply {
        typescript.version = typescriptVersion
    }
}
