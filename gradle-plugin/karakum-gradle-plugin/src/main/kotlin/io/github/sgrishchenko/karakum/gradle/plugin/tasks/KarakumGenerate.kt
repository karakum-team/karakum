package io.github.sgrishchenko.karakum.gradle.plugin.tasks

import io.github.sgrishchenko.karakum.gradle.plugin.karakumDependency
import io.github.sgrishchenko.karakum.gradle.plugin.typescriptDependency
import org.gradle.api.DefaultTask
import org.gradle.api.provider.Property
import org.gradle.api.tasks.Internal
import org.gradle.api.tasks.OutputFile
import org.gradle.api.tasks.TaskAction
import org.jetbrains.kotlin.gradle.plugin.mpp.KotlinJsCompilation
import org.jetbrains.kotlin.gradle.targets.js.npm.RequiresNpmDependencies
import org.jetbrains.kotlin.gradle.targets.js.npm.npmProject
import java.io.File

abstract class KarakumGenerate : DefaultTask(), RequiresNpmDependencies {

    @get:OutputFile
    abstract val outputConfig: Property<File>

    @get:Internal
    override val compilation: KotlinJsCompilation = kotlinJsCompilation

    @get:Internal
    override val nodeModulesRequired = true

    @get:Internal
    override val requiredNpmDependencies = setOf(
        karakumDependency,
        typescriptDependency,
    )

    init {
        outputConfig.convention(defaultOutputConfig)
    }

    @TaskAction
    fun generate() {
        project.exec {
            compilation.npmProject.useTool(
                this,
                "karakum/build/cli.js",
                emptyList(),
                listOf("--config", outputConfig.get().absolutePath)
            )
        }
    }
}
