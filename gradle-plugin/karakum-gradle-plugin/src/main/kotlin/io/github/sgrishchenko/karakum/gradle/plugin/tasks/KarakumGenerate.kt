package io.github.sgrishchenko.karakum.gradle.plugin.tasks

import io.github.sgrishchenko.karakum.gradle.plugin.karakumDependency
import io.github.sgrishchenko.karakum.gradle.plugin.kotlinJsCompilation
import io.github.sgrishchenko.karakum.gradle.plugin.typescriptDependency
import org.gradle.api.DefaultTask
import org.gradle.api.file.DirectoryProperty
import org.gradle.api.file.RegularFileProperty
import org.gradle.api.tasks.*
import org.gradle.process.ExecOperations
import org.jetbrains.kotlin.gradle.targets.js.ir.KotlinJsIrCompilation
import org.jetbrains.kotlin.gradle.targets.js.npm.RequiresNpmDependencies
import org.jetbrains.kotlin.gradle.targets.js.npm.npmProject
import javax.inject.Inject

abstract class KarakumGenerate : DefaultTask(), RequiresNpmDependencies {
    @get:Inject
    abstract val exec: ExecOperations

    @get:InputFile
    abstract val configFile: RegularFileProperty

    @get:InputDirectory
    abstract val extensionDirectory: DirectoryProperty

    @get:Internal
    override val compilation: KotlinJsIrCompilation = project.kotlinJsCompilation

    @get:Internal
    override val requiredNpmDependencies = setOf(
        karakumDependency,
        typescriptDependency,
    )

    @TaskAction
    fun generate() {
        exec.exec {
            compilation.npmProject.useTool(
                this,
                "karakum/build/cli.js",
                args = listOf("--config", configFile.get().asFile.absolutePath)
            )
        }
    }
}
