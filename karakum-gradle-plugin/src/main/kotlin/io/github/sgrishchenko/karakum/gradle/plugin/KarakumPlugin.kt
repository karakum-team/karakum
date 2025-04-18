package io.github.sgrishchenko.karakum.gradle.plugin

import io.github.sgrishchenko.karakum.gradle.plugin.tasks.KarakumGenerate
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.*
import org.jetbrains.kotlin.gradle.dsl.KotlinMultiplatformExtension
import org.jetbrains.kotlin.gradle.targets.js.nodejs.NodeJsEnvSpec
import org.jetbrains.kotlin.gradle.targets.js.nodejs.NodeJsExec
import org.jetbrains.kotlin.gradle.targets.js.nodejs.NodeJsPlugin

class KarakumPlugin : Plugin<Project> {
    override fun apply(project: Project): Unit = with(project) {
        pluginManager.apply("org.jetbrains.kotlin.multiplatform")
        pluginManager.apply("org.jetbrains.kotlin.plugin.js-plain-objects")
        pluginManager.apply(NodeJsPlugin::class)

        the<NodeJsEnvSpec>().version.convention(nodeVersion)

        val kotlin = the<KotlinMultiplatformExtension>()

        kotlin.js {
            nodejs()

            compilerOptions {
                target.convention("es2015")
            }

            binaries.executable()
        }

        kotlin.sourceSets.named("jsMain") {
            dependencies {
                implementation("io.github.sgrishchenko:karakum:$karakumVersion")
                implementation("io.arrow-kt:arrow-core:$arrowKtVersion")
            }
        }

        val karakum = extensions.create<KarakumExtension>("karakum").apply {
            output.convention(layout.projectDirectory.dir("../src/jsMain/kotlin"))
        }

        val jsNodeProductionRun by tasks.named<NodeJsExec>("jsNodeProductionRun") {
            args(karakum.output.asFile.get().absolutePath)
        }

        val generateKarakumExternals by tasks.registering(KarakumGenerate::class) {
            group = KARAKUM_GRADLE_PLUGIN_GROUP
            description = "Generates the Kotlin external declarations using Karakum."

            dependsOn(jsNodeProductionRun)
            output.convention(karakum.output)
        }
    }
}
