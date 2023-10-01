package io.github.sgrishchenko.karakum.gradle.plugin.tasks

import io.github.sgrishchenko.karakum.gradle.plugin.karakumDependency
import io.github.sgrishchenko.karakum.gradle.plugin.typescriptDependency
import org.gradle.api.DefaultTask
import org.gradle.api.file.DirectoryProperty
import org.gradle.api.internal.file.FileFactory
import org.gradle.api.provider.Property
import org.gradle.api.tasks.*
import org.jetbrains.kotlin.gradle.plugin.mpp.KotlinJsCompilation
import org.jetbrains.kotlin.gradle.targets.js.npm.RequiresNpmDependencies
import org.jetbrains.kotlin.gradle.targets.js.npm.npmProject
import java.io.File
import javax.inject.Inject

abstract class KarakumGenerate
@Inject
constructor(
    fileFactory: FileFactory,
) : DefaultTask(), RequiresNpmDependencies {

    @get:InputFile
    abstract val outputConfig: Property<File>

    @get:InputDirectory
    abstract val outputExtensions: DirectoryProperty

    @get:Internal
    override val compilation: KotlinJsCompilation = kotlinJsCompilation

    @get:Internal
    override val requiredNpmDependencies = setOf(
        karakumDependency,
        typescriptDependency,
    )

    init {
        outputConfig.convention(defaultOutputConfig)
        outputExtensions.convention(fileFactory.dir(defaultOutputExtensions))
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
