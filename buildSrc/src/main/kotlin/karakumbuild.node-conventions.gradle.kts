import org.jetbrains.kotlin.gradle.targets.js.nodejs.NodeJsEnvSpec
import org.jetbrains.kotlin.gradle.targets.js.nodejs.NodeJsPlugin

plugins.withType<NodeJsPlugin> {
    val nodeVersion: String by project

    the<NodeJsEnvSpec>().version = nodeVersion
}
