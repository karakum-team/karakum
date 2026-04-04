import org.jetbrains.kotlin.gradle.targets.js.nodejs.NodeJsPlugin
import org.jetbrains.kotlin.gradle.targets.js.nodejs.NodeJsRootExtension

plugins {
    kotlin("multiplatform") apply false
    `node-conventions`
}

plugins.withType<NodeJsPlugin> {
    val typescriptVersion: String by project
    val mochaVersion: String by project

    the<NodeJsRootExtension>().versions.apply {
        typescript.version = typescriptVersion
        mocha.version = mochaVersion
    }
}
