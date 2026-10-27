import org.jetbrains.kotlin.gradle.targets.js.nodejs.NodeJsPlugin
import org.jetbrains.kotlin.gradle.targets.js.nodejs.NodeJsRootExtension

plugins {
    kotlin("multiplatform") version "2.4.21" apply false
}

plugins.withType<NodeJsPlugin> {
    the<NodeJsRootExtension>().versions.apply {
        webpack.version = "^5.112.2"
        webpackCli.version = "^7.2.3"
        webpackDevServer.version = "^6.1.0"
        mocha.version = "12.0.0-beta-10"
    }
}
