import org.gradle.kotlin.dsl.withType
import org.jetbrains.kotlin.gradle.targets.js.nodejs.NodeJsPlugin
import org.jetbrains.kotlin.gradle.targets.js.nodejs.NodeJsRootExtension
import kotlin.apply

plugins {
    id("io.github.sgrishchenko.karakum") version "1.0.0-alpha.104"
}

repositories {
    mavenCentral()
}

karakum {
    library {
        name = "js-file-download"
        version = "0.4.12"
    }
}

plugins.withType<NodeJsPlugin> {
    the<NodeJsRootExtension>().versions.apply {
        mocha.version = "12.0.0-beta-10"
    }
}
