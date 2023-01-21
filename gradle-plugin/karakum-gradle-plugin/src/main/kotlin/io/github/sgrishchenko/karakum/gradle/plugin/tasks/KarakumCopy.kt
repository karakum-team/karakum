package io.github.sgrishchenko.karakum.gradle.plugin.tasks

import org.gradle.api.DefaultTask
import org.gradle.api.file.DirectoryProperty
import org.gradle.api.internal.file.FileFactory
import org.gradle.api.provider.Property
import org.gradle.api.tasks.InputFiles
import org.gradle.api.tasks.OutputDirectory
import org.gradle.api.tasks.TaskAction
import java.io.File
import java.nio.file.FileSystems
import java.nio.file.Files
import java.nio.file.Path
import javax.inject.Inject
import kotlin.streams.toList


abstract class KarakumCopy
@Inject
constructor(
    fileFactory: FileFactory,
) : DefaultTask() {
    @get:InputFiles
    abstract val inputExtensions: Property<Iterable<File>>

    @get:OutputDirectory
    abstract val outputExtensions: DirectoryProperty

    private val defaultPatterns = listOf(
        "<buildSrc>/karakum/**.js",
        "karakum/**.js",
    )

    init {
        inputExtensions.convention(getDefaultInputExtensions())
        outputExtensions.convention(fileFactory.dir(defaultOutputExtensions))
    }

    private fun getDefaultInputExtensions(): Iterable<File> {
        return defaultPatterns
            .flatMap { pattern ->
                val basePath = when {
                    pattern.startsWith("<buildSrc>/") -> project.rootDir.resolve("buildSrc").toPath()
                    else -> project.projectDir.toPath()
                }

                val preparedPattern = pattern.removePrefix("<buildSrc>/")

                val matcher = FileSystems.getDefault().getPathMatcher("glob:$preparedPattern")

                Files.walk(basePath).use { paths ->
                    paths
                        .map(basePath::relativize)
                        .filter(matcher::matches)
                        .map(basePath::resolve)
                        .toList()
                }
            }
            .map(Path::toFile)
    }

    @TaskAction
    fun copy() {
        val extensions = inputExtensions.get().toList()

        project.copy {
            from(*extensions.toTypedArray())
            into(outputExtensions)
        }
    }
}
