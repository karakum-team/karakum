package org.jetbrains.karakum.gradle.plugin.tasks

import com.fasterxml.jackson.databind.JsonNode
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.databind.node.ArrayNode
import com.fasterxml.jackson.databind.node.ObjectNode
import org.gradle.api.DefaultTask
import org.gradle.api.provider.Property
import org.gradle.api.tasks.InputFile
import org.gradle.api.tasks.OutputFile
import org.gradle.api.tasks.TaskAction
import org.jetbrains.karakum.gradle.plugin.KARAKUM_CONFIG_FILE
import java.io.File


abstract class KarakumConfig : DefaultTask() {
    @get:InputFile
    abstract val inputConfig: Property<File>

    @get:OutputFile
    abstract val outputConfig: Property<File>

    private val mapper = ObjectMapper()

    private val replacements = mapOf(
        "<buildSrc>" to project.rootDir.resolve("buildSrc"),
        "<nodeModules>" to project.buildDir.resolve("js/node_modules"),
    )

    init {
        inputConfig.convention(project.projectDir.resolve(KARAKUM_CONFIG_FILE))
        outputConfig.convention(project.buildDir.resolve("karakum/$KARAKUM_CONFIG_FILE"))
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
                    }
                }
            }
            node.isArray -> {
                val arrayNode = node as ArrayNode

                for ((index, currentNode) in node.withIndex()) {
                    when {
                        currentNode.isTextual -> arrayNode.set(index, currentNode.textValue().replaceTokens())
                        currentNode.isObject || currentNode.isArray -> replaceTokens(currentNode)
                    }
                }
            }
        }
    }

    @TaskAction
    fun process() {
        val configNode = mapper.readTree(inputConfig.get())

        replaceTokens(configNode)

        mapper
            .writerWithDefaultPrettyPrinter()
            .writeValue(outputConfig.get(), configNode)
    }
}
