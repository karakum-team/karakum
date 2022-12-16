package org.jetbrains.karakum.gradle.plugin

import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.tasks.Exec
import org.jetbrains.karakum.gradle.plugin.tasks.KarakumConfig
import org.jetbrains.karakum.gradle.plugin.tasks.KarakumPluginsCopy
import org.jetbrains.kotlin.gradle.dsl.kotlinExtension
import org.jetbrains.kotlin.gradle.plugin.KotlinCompilation
import org.jetbrains.kotlin.gradle.plugin.mpp.pm20.util.targets
import org.jetbrains.kotlin.gradle.targets.js.KotlinJsTarget
import org.jetbrains.kotlin.gradle.targets.js.npm.DevNpmDependencyExtension
import org.jetbrains.kotlin.gradle.targets.js.npm.npmProject

class KarakumPlugin : Plugin<Project> {
    override fun apply(project: Project) {
        with(project.dependencies) {
            val devNpm = extensions.getByName("devNpm") as DevNpmDependencyExtension

            add("compileOnly", devNpm("karakum", "1.0.0-alpha.0"))
            add("compileOnly", devNpm("typescript", "^4.9.4"))
        }

        val kotlinJsTarget = project.kotlinExtension.targets.firstNotNullOf { it as? KotlinJsTarget }
        val kotlinJsCompilation = kotlinJsTarget.compilations.first { it.name == KotlinCompilation.MAIN_COMPILATION_NAME }

        project.tasks.register("copyKarakumPlugins", KarakumPluginsCopy::class.java) { task ->
            task.group = KARAKUM_GRADLE_PLUGIN_GROUP
        }

        project.tasks.register("configureKarakum", KarakumConfig::class.java) { task ->
            task.group = KARAKUM_GRADLE_PLUGIN_GROUP

            task.dependsOn("copyKarakumPlugins")
        }

        project.tasks.register("generateKarakumExternals", Exec::class.java) { task ->
            task.group = KARAKUM_GRADLE_PLUGIN_GROUP

            task.dependsOn("configureKarakum")

            kotlinJsCompilation.npmProject.useTool(
                task,
                "karakum/build/cli.js",
                emptyList(),
                listOf("--config", project.buildDir.resolve("karakum/$KARAKUM_CONFIG_FILE").absolutePath)
            )
        }
    }
}
