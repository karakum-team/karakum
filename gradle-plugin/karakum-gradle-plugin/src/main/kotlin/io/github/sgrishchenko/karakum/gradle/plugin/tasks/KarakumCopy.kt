package io.github.sgrishchenko.karakum.gradle.plugin.tasks

import org.gradle.api.DefaultTask
import org.gradle.api.file.DirectoryProperty
import org.gradle.api.internal.file.FileFactory
import org.gradle.api.provider.Property
import org.gradle.api.tasks.Input
import org.gradle.api.tasks.InputFiles
import org.gradle.api.tasks.OutputDirectory
import org.gradle.api.tasks.TaskAction
import java.io.File
import java.nio.file.Files
import java.nio.file.Path
import javax.inject.Inject
import kotlin.io.path.exists
import kotlin.io.path.writeText
import kotlin.streams.toList


abstract class KarakumCopy
@Inject
constructor(
    fileFactory: FileFactory,
) : DefaultTask() {
    @get:Input
    abstract val extensionBasePaths: Property<Iterable<String>>

    @get:InputFiles
    abstract val inputExtensions: Property<Iterable<File>>

    @get:OutputDirectory
    abstract val outputExtensions: DirectoryProperty

    init {
        extensionBasePaths.convention(listOf(
            project.rootDir.resolve("buildSrc").resolve("karakum").toPath(),
            project.projectDir.resolve("karakum").toPath()
        ).map(Path::toString))

        inputExtensions.convention(getDefaultInputExtensions())
        outputExtensions.convention(fileFactory.dir(defaultOutputExtensions))
    }

    private val File.basePath
        get() = extensionBasePaths.get()
            .map(Path::of)
            .firstOrNull(toPath()::startsWith)
            ?: run {
                val basePathList = extensionBasePaths.get().joinToString(separator = "\n") { "- $it/" }
                error("Karakum extensions should be placed in one the following directories:\n${basePathList}")
            }

    private fun getDefaultInputExtensions(): Iterable<File> {
        return extensionBasePaths.get()
            .map(Path::of)
            .filter { basePath -> Files.exists(basePath) }
            .flatMap { basePath ->
                Files.walk(basePath).use { paths ->
                    paths
                        .filter { file -> !Files.isDirectory(file) }
                        .toList()
                }
            }
            .map(Path::toFile)
    }

    @TaskAction
    fun copy() {
        val extensionGroups = inputExtensions.get().groupBy { it.basePath }

        for ((basePath, files) in extensionGroups) {
            project.copy {
                from(basePath)
                into(outputExtensions)
                include(
                    files
                        .map(File::toPath)
                        .map(basePath::relativize)
                        .map(Path::toString)
                )
            }
        }

        val extensionPackageJson = outputExtensions.asFile.get().toPath().resolve("package.json")

        if (!extensionPackageJson.exists()) {
            extensionPackageJson.writeText("""
                {
                    "private": true,
                    "type": "module"
                }
            """.trimIndent())
        }
    }
}
