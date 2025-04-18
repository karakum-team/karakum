package io.github.sgrishchenko.karakum.gradle.plugin.tasks

import io.github.sgrishchenko.karakum.gradle.plugin.worker.KtLintFormatWorker
import org.ec4j.core.Resource.Resources
import org.ec4j.core.ResourcePropertiesService
import org.gradle.api.DefaultTask
import org.gradle.api.file.DirectoryProperty
import org.gradle.api.file.ProjectLayout
import org.gradle.api.tasks.InputDirectory
import org.gradle.api.tasks.InputFiles
import org.gradle.api.tasks.Optional
import org.gradle.api.tasks.TaskAction
import org.gradle.kotlin.dsl.submit
import org.gradle.workers.WorkerExecutor
import java.nio.file.Path
import javax.inject.Inject
import kotlin.text.Charsets.UTF_8

abstract class KarakumGenerate : DefaultTask() {
    @get:Inject
    abstract val layout: ProjectLayout

    @get:Inject
    abstract val workerExecutor: WorkerExecutor

    @get:InputDirectory
    abstract val output: DirectoryProperty

    @get:InputFiles
    @get:Optional
    val editorConfigFiles = output.map { destinationDirectory ->
        ResourcePropertiesService
            .builder()
            .build()
            .queryProperties(Resources.ofPath(destinationDirectory.asFile.absoluteFile.toPath(), UTF_8))
            .editorConfigFiles
            .map { it.getAdapter(Path::class.java) }
            .let { layout.files(it) }
    }

    init {
        outputs.dirs(output)
    }

    @TaskAction
    fun generate() {
        logger.lifecycle("Formatting generated files")
        output.get().asFileTree.forEach { outputFile ->
            workerExecutor
                // work-around for https://github.com/gradle/gradle/issues/18313
                // and https://github.com/gradle/gradle/issues/8476
                // we have to use process isolation as classloader isolation workers are not reused
                // which then quickly leads to out of memory error and with no isolation
                // there is a Kotlin version compatibility problem in Ktlint
                .processIsolation()
                .submit(KtLintFormatWorker::class) {
                    file.set(outputFile)
                }
        }
    }
}
