package io.github.sgrishchenko.karakum.gradle.plugin

import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.attributes.Bundling
import org.gradle.api.tasks.JavaExec
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

        val ktlint by configurations.creating

        dependencies {
            ktlint("com.pinterest.ktlint:ktlint-cli:$ktlintVersion") {
                attributes {
                    attribute(Bundling.BUNDLING_ATTRIBUTE, objects.named(Bundling.EXTERNAL))
                }
            }
        }

        val karakum = extensions.create<KarakumExtension>("karakum").apply {
            output.convention(layout.projectDirectory.dir("../src/jsMain/kotlin"))
        }

        val jsNodeProductionRun by tasks.named<NodeJsExec>("jsNodeProductionRun") {
            args(karakum.output.asFile.get().absolutePath)
        }

        val ktlintFormat by tasks.registering(JavaExec::class) {
            group = KARAKUM_GRADLE_PLUGIN_GROUP
            description = "Check Kotlin code style and format"
            classpath = ktlint
            mainClass = "com.pinterest.ktlint.Main"
            jvmArgs("--add-opens=java.base/java.lang=ALL-UNNAMED")
            val reporterOutput = layout.buildDirectory.file("reports/ktlint/ktlint-format.txt")
            args(
                "--format",
                "--reporter=plain,output=${reporterOutput.get().asFile.absolutePath}",
                "${karakum.output.asFile.get().absolutePath}/**/*.kt",
            )
            // do not report violations that cannot be auto-corrected
            isIgnoreExitValue = true

            dependsOn(jsNodeProductionRun)
        }

        val generateKarakumExternals by tasks.registering {
            group = KARAKUM_GRADLE_PLUGIN_GROUP
            description = "Generate the Kotlin external declarations using Karakum"

            dependsOn(ktlintFormat)
        }
    }
}
