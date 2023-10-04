package io.github.sgrishchenko.karakum.gradle.plugin

import io.github.sgrishchenko.karakum.gradle.plugin.tasks.KarakumConfig
import io.github.sgrishchenko.karakum.gradle.plugin.tasks.KarakumCopy
import io.github.sgrishchenko.karakum.gradle.plugin.tasks.KarakumGenerate
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.file.FileTree
import org.gradle.kotlin.dsl.create
import org.gradle.kotlin.dsl.getValue
import org.gradle.kotlin.dsl.provideDelegate
import org.gradle.kotlin.dsl.registering
import org.jetbrains.kotlin.gradle.targets.js.npm.npmProject

class KarakumPlugin : Plugin<Project> {
    override fun apply(project: Project): Unit = with(project) {
        plugins.withId("org.jetbrains.kotlin.multiplatform") {
            val karakum = extensions.create<KarakumExtension>("karakum").apply {
                configFile.convention(layout.projectDirectory.file(KARAKUM_CONFIG_FILE))

                extensionSource.convention(
                    listOf(
                    rootProject.layout.projectDirectory.dir("buildSrc/karakum"),
                    layout.projectDirectory.dir("karakum"),
                )
                    .map { it.asFileTree }
                    .reduce(FileTree::plus)
                )
            }

            val configureKarakum by tasks.registering(KarakumConfig::class) {
                group = KARAKUM_GRADLE_PLUGIN_GROUP
                description = "Prepares the Karakum configuration using the Gradle project layout."

                configFile.convention(karakum.configFile)

                destinationFile.convention(layout.buildDirectory.file("karakum/$KARAKUM_CONFIG_FILE"))
            }

            val copyKarakumExtensions by tasks.registering(KarakumCopy::class) {
                group = KARAKUM_GRADLE_PLUGIN_GROUP
                description = "Copies the Karakum extensions to the npm project."

                extensionSource.convention(karakum.extensionSource)

                val npmProjectDirectory = provider { kotlinJsCompilation.npmProject.dir }
                destinationDirectory.convention(layout.dir(npmProjectDirectory).map { it.dir("karakum") })
            }

            val generateKarakumExternals by tasks.registering(KarakumGenerate::class) {
                group = KARAKUM_GRADLE_PLUGIN_GROUP
                description = "Generates the Kotlin external declarations using Karakum."

                configFile.convention(configureKarakum.flatMap { it.destinationFile })
                extensionDirectory.convention(copyKarakumExtensions.flatMap { it.destinationDirectory })
            }
        }
    }
}
