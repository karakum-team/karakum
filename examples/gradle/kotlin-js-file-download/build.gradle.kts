import org.jetbrains.kotlin.gradle.targets.js.nodejs.NodeJsPlugin
import org.jetbrains.kotlin.gradle.targets.js.nodejs.NodeJsRootExtension

plugins {
    kotlin("multiplatform") version "2.3.20" apply false
}

plugins.withType<NodeJsPlugin> {
    the<NodeJsRootExtension>().versions.apply {
        webpack.version = "^5.105.4"
        webpackCli.version = "^7.0.2"
        webpackDevServer.version = "^5.2.3"
        mocha.version = "^12.0.0-beta-10"
    }
}
