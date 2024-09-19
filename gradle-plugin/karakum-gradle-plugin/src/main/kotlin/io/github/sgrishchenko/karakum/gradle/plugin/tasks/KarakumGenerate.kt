package io.github.sgrishchenko.karakum.gradle.plugin.tasks

import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.databind.node.ArrayNode
import com.fasterxml.jackson.databind.node.ObjectNode
import io.github.sgrishchenko.karakum.gradle.plugin.karakumDependency
import io.github.sgrishchenko.karakum.gradle.plugin.kotlinJsCompilation
import io.github.sgrishchenko.karakum.gradle.plugin.service.KtLintService
import io.github.sgrishchenko.karakum.gradle.plugin.typescriptDependency
import io.github.sgrishchenko.karakum.gradle.plugin.worker.KtLintFormatWorker
import org.ec4j.core.Resource.Resources
import org.ec4j.core.ResourcePropertiesService
import org.gradle.api.DefaultTask
import org.gradle.api.file.DirectoryProperty
import org.gradle.api.file.ProjectLayout
import org.gradle.api.file.RegularFileProperty
import org.gradle.api.provider.Property
import org.gradle.api.services.ServiceReference
import org.gradle.api.tasks.*
import org.gradle.kotlin.dsl.submit
import org.gradle.process.ExecOperations
import org.gradle.workers.WorkerExecutor
import org.jetbrains.kotlin.gradle.targets.js.ir.KotlinJsIrCompilation
import org.jetbrains.kotlin.gradle.targets.js.npm.RequiresNpmDependencies
import org.jetbrains.kotlin.gradle.targets.js.npm.npmProject
import java.nio.file.Path
import javax.inject.Inject
import kotlin.text.Charsets.UTF_8

abstract class KarakumGenerate : DefaultTask(), RequiresNpmDependencies {
    @get:Inject
    abstract val exec: ExecOperations

    @get:Inject
    abstract val layout: ProjectLayout

    @get:Inject
    abstract val workerExecutor: WorkerExecutor

    @get:ServiceReference
    abstract val ktlintService: Property<KtLintService>

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

    private val mapper = ObjectMapper()

    @get:InputFiles
    @get:SkipWhenEmpty
    val inputFiles = configFile.map { configFile ->
        val configNode = mapper.readTree(configFile.asFile) as ObjectNode
        val input = requireNotNull(configNode.get("input"))
        val inputGlobs = if (input is ArrayNode) {
            input.elements().asSequence().map { it.textValue() }.toList()
        } else {
            listOf(input.textValue())
        }
        val inputRoots = inputGlobs.map { inputGlob ->
            inputGlob
                .split("/")
                .takeWhile { !it.contains("""[*?\[({]""".toRegex()) }
                .joinToString("/")
        }
        layout.files(inputRoots)
    }

    @get:OutputDirectory
    val destinationDirectory = configFile.map {
        val configNode = mapper.readTree(it.asFile) as ObjectNode
        val output = requireNotNull(configNode.get("output")).textValue()
        if (output.endsWith(".kt")) {
            layout.projectDirectory.dir(output.substringBeforeLast("/"))
        } else {
            layout.projectDirectory.dir(output)
        }
    }

    @get:InputFiles
    @get:Optional
    val editorConfigFiles = destinationDirectory.map { destinationDirectory ->
        ResourcePropertiesService
            .builder()
            .build()
            .queryProperties(Resources.ofPath(destinationDirectory.asFile.absoluteFile.toPath(), UTF_8))
            .editorConfigFiles
            .map { it.getAdapter(Path::class.java) }
            .let { layout.files(it) }
    }

    @TaskAction
    fun generate() {
        exec.exec {
            compilation.npmProject.useTool(
                this,
                "karakum/build/cli.js",
                args = listOf("--config", configFile.get().asFile.absolutePath)
            )
        }

        logger.lifecycle("Formatting generated files")
        destinationDirectory.get().asFileTree.forEach { file ->
            workerExecutor.noIsolation().submit(KtLintFormatWorker::class) {
                this.file.set(file)
            }
        }
    }
}
