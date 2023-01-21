package io.github.sgrishchenko.karakum.gradle.plugin.tasks

import com.fasterxml.jackson.databind.JsonNode
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.databind.node.ArrayNode
import com.fasterxml.jackson.databind.node.ObjectNode
import org.gradle.api.DefaultTask
import org.gradle.api.file.DirectoryProperty
import org.gradle.api.internal.file.FileFactory
import org.gradle.api.provider.Property
import org.gradle.api.tasks.InputFile
import org.gradle.api.tasks.OutputDirectory
import org.gradle.api.tasks.OutputFile
import org.gradle.api.tasks.TaskAction
import io.github.sgrishchenko.karakum.gradle.plugin.KARAKUM_CONFIG_FILE
import java.io.File
import javax.inject.Inject


abstract class KarakumConfig
@Inject
constructor(
    fileFactory: FileFactory,
) : DefaultTask() {

    @get:InputFile
    abstract val inputConfig: Property<File>

    @get:OutputFile
    abstract val outputConfig: Property<File>

    @get:OutputDirectory
    abstract val outputPlugins: DirectoryProperty

    private val mapper = ObjectMapper()

    private val replacements = mapOf(
        "<buildSrc>" to project.rootDir.resolve("buildSrc"),
        "<nodeModules>" to project.rootProject.buildDir.resolve("js/node_modules"),
    )

    init {
        inputConfig.convention(project.projectDir.resolve(KARAKUM_CONFIG_FILE))
        outputConfig.convention(project.buildDir.resolve("karakum/$KARAKUM_CONFIG_FILE"))
        outputPlugins.convention(fileFactory.dir(project.rootProject.buildDir.resolve("js/packages/${project.name}/karakum")))
    }

    private fun String.replaceTokens() = replacements.entries.fold(this) { acc, (from, to) ->
        acc.replace(from, to.absolutePath)
    }

    private fun replaceTokens(node: JsonNode) {
        when {
            node.isObject -> {
                val objectNode = node as ObjectNode

                for ((key, currentNode) in node.fields()) {
                    when {
                        currentNode.isTextual -> objectNode.put(key, currentNode.textValue().replaceTokens())
                        currentNode.isContainerNode -> replaceTokens(currentNode)
                    }
                }
            }

            node.isArray -> {
                val arrayNode = node as ArrayNode

                for ((index, currentNode) in node.withIndex()) {
                    when {
                        currentNode.isTextual -> arrayNode.set(index, currentNode.textValue().replaceTokens())
                        currentNode.isContainerNode -> replaceTokens(currentNode)
                    }
                }
            }
        }
    }

    private fun replacePlugins(configNode: JsonNode) {
        (configNode as ObjectNode).put("plugins", outputPlugins.asFile.get().absolutePath + "/**/*.js")
    }

    private fun replaceOutput(configNode: JsonNode) {
        val outputNode = requireNotNull((configNode as ObjectNode).get("output"))
        val output = outputNode.textValue()
        configNode.put("output", project.projectDir.resolve(output).absolutePath)
    }

    @TaskAction
    fun configure() {
        val configNode = mapper.readTree(inputConfig.get())

        replacePlugins(configNode)
        replaceTokens(configNode)
        replaceOutput(configNode)

        mapper
            .writerWithDefaultPrettyPrinter()
            .writeValue(outputConfig.get(), configNode)
    }
}
