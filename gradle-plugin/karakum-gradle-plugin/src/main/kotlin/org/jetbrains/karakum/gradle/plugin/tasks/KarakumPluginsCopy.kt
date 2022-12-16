package org.jetbrains.karakum.gradle.plugin.tasks

import com.fasterxml.jackson.databind.ObjectMapper
import org.gradle.api.DefaultTask
import org.gradle.api.file.DirectoryProperty
import org.gradle.api.internal.file.FileFactory
import org.gradle.api.provider.Property
import org.gradle.api.tasks.InputFile
import org.gradle.api.tasks.InputFiles
import org.gradle.api.tasks.OutputDirectory
import org.gradle.api.tasks.TaskAction
import org.jetbrains.karakum.gradle.plugin.KARAKUM_CONFIG_FILE
import java.io.File
import java.nio.file.FileSystems
import java.nio.file.Files
import java.nio.file.Path
import javax.inject.Inject
import kotlin.streams.toList


abstract class KarakumPluginsCopy
@Inject
constructor(
    fileFactory: FileFactory,
) : DefaultTask() {

    @get:InputFile
    abstract val inputConfig: Property<File>

    @get:InputFiles
    abstract val inputPlugins: Property<Iterable<File>>

    @get:OutputDirectory
    abstract val outputPlugins: DirectoryProperty

    private val mapper = ObjectMapper()

    private val defaultPluginPatterns = listOf(
        "<buildSrc>/karakum/**/*.js",
        "karakum/**/*.js",
    )

    init {
        inputConfig.convention(project.projectDir.resolve(KARAKUM_CONFIG_FILE))
        inputPlugins.convention(getDefaultInputPlugins())
        outputPlugins.convention(fileFactory.dir(project.rootProject.buildDir.resolve("js/packages/${project.name}/karakum")))
    }

    private fun getDefaultInputPlugins(): Iterable<File> {
        val configNode = mapper.readTree(inputConfig.get())

        val pluginsNode = configNode.path("plugins")

        val pluginPatterns = when {
            pluginsNode.isMissingNode -> defaultPluginPatterns
            pluginsNode.isTextual -> listOf(pluginsNode.textValue())
            pluginsNode.isArray -> pluginsNode.map { it.textValue() }
            else -> error("Plugins in Karakum config should be string or array of strings")
        }

        return pluginPatterns
            // JavaScript code uses handles /**/ as zero or more folders, while Java handles it as at least one folder.
            .map { pattern -> pattern.replace("/**/", "/**") }
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
        val plugins = inputPlugins.get().toList()

        project.copy {
            from(*plugins.toTypedArray())
            into(outputPlugins)
        }
    }
}
