package org.jetbrains.karakum.gradle.plugin.tasks

import org.gradle.api.DefaultTask
import org.gradle.api.tasks.Internal
import org.gradle.api.tasks.TaskAction
import org.jetbrains.karakum.gradle.plugin.KARAKUM_CONFIG_FILE
import org.jetbrains.karakum.gradle.plugin.karakumDependency
import org.jetbrains.karakum.gradle.plugin.typescriptDependency
import org.jetbrains.kotlin.gradle.dsl.KotlinJsProjectExtension
import org.jetbrains.kotlin.gradle.plugin.KotlinCompilation.Companion.MAIN_COMPILATION_NAME
import org.jetbrains.kotlin.gradle.plugin.mpp.KotlinJsCompilation
import org.jetbrains.kotlin.gradle.targets.js.npm.RequiresNpmDependencies
import org.jetbrains.kotlin.gradle.targets.js.npm.npmProject

abstract class KarakumGenerate : DefaultTask(), RequiresNpmDependencies {
    @get:Internal
    override val compilation: KotlinJsCompilation
        get() {
            val extension = project.extensions.getByType(KotlinJsProjectExtension::class.java)
            val target = extension.js()

            return target.compilations.getByName(MAIN_COMPILATION_NAME)
        }

    @get:Internal
    override val nodeModulesRequired = true

    @get:Internal
    override val requiredNpmDependencies = setOf(
        karakumDependency,
        typescriptDependency,
    )

    @TaskAction
    fun generate() {
        project.exec {
            compilation.npmProject.useTool(
                this,
                "karakum/build/cli.js",
                emptyList(),
                listOf("--config", project.buildDir.resolve("karakum/$KARAKUM_CONFIG_FILE").absolutePath)
            )
        }
    }
}
