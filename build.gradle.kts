import org.jetbrains.kotlin.gradle.targets.js.nodejs.NodeJsEnvSpec
import org.jetbrains.kotlin.gradle.targets.js.nodejs.NodeJsPlugin
import org.jetbrains.kotlin.gradle.targets.js.nodejs.NodeJsRootExtension

plugins {
    kotlin("multiplatform") apply false
}

allprojects {
    project.plugins.withType<NodeJsPlugin> {
        project.the<NodeJsEnvSpec>().version = "22.12.0"
    }
}

plugins.withType<NodeJsPlugin> {
    val typescriptVersion: String by project

    the<NodeJsRootExtension>().versions.typescript.version = typescriptVersion
}
