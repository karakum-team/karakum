package io.github.sgrishchenko.karakum.gradle.plugin

import io.github.sgrishchenko.karakum.gradle.plugin.service.KtLintService
import io.github.sgrishchenko.karakum.gradle.plugin.tasks.KarakumConfig
import io.github.sgrishchenko.karakum.gradle.plugin.tasks.KarakumGenerate
import io.github.sgrishchenko.karakum.gradle.plugin.tasks.KarakumSync
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.file.FileTree
import org.gradle.api.services.BuildServiceRegistry
import org.gradle.kotlin.dsl.create
import org.gradle.kotlin.dsl.getValue
import org.gradle.kotlin.dsl.provideDelegate
import org.gradle.kotlin.dsl.registerIfAbsent
import org.gradle.kotlin.dsl.registering
import org.jetbrains.kotlin.gradle.targets.js.npm.npmProject
import javax.inject.Inject

abstract class KarakumPlugin : Plugin<Project> {
    @get:Inject
    abstract val sharedServices: BuildServiceRegistry

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
                cwd.convention(project.kotlinJsCompilation.npmProject.dir)
                buildSrc.convention(project.rootProject.layout.projectDirectory.dir("buildSrc"))
                nodeModules.convention(project.rootProject.layout.buildDirectory.dir("js/node_modules"))
                packageNodeModules.convention(project.rootProject.layout.buildDirectory.dir("js/packages").map { it.dir(project.name).dir("node_modules") })

                destinationFile.convention(layout.buildDirectory.file("karakum/$KARAKUM_CONFIG_FILE"))
            }

            val syncKarakumExtensions by tasks.registering(KarakumSync::class) {
                group = KARAKUM_GRADLE_PLUGIN_GROUP
                description = "Copies the Karakum extensions to the npm project."

                extensionSource.convention(karakum.extensionSource)

                val npmProjectDirectory = kotlinJsCompilation.npmProject.dir.map { it.asFile }
                destinationDirectory.convention(layout.dir(npmProjectDirectory).map { it.dir("karakum") })
            }

            sharedServices.registerIfAbsent("ktlintService", KtLintService::class)

            val generateKarakumExternals by tasks.registering(KarakumGenerate::class) {
                group = KARAKUM_GRADLE_PLUGIN_GROUP
                description = "Generates the Kotlin external declarations using Karakum."

                configFile.convention(configureKarakum.flatMap { it.destinationFile })
                extensionDirectory.convention(syncKarakumExtensions.flatMap { it.destinationDirectory })
            }
        }
    }
}
