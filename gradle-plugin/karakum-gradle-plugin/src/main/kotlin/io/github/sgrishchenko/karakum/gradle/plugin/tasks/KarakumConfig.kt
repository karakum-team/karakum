package io.github.sgrishchenko.karakum.gradle.plugin.tasks

import com.fasterxml.jackson.databind.JsonNode
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.databind.node.ArrayNode
import com.fasterxml.jackson.databind.node.ObjectNode
import io.github.sgrishchenko.karakum.gradle.plugin.kotlinJsCompilation
import org.gradle.api.DefaultTask
import org.gradle.api.file.RegularFileProperty
import org.gradle.api.tasks.InputFile
import org.gradle.api.tasks.OutputFile
import org.gradle.api.tasks.TaskAction
import org.jetbrains.kotlin.gradle.targets.js.npm.npmProject


abstract class KarakumConfig : DefaultTask() {

    @get:InputFile
    abstract val configFile: RegularFileProperty

    @get:OutputFile
    abstract val destinationFile: RegularFileProperty

    private val mapper = ObjectMapper()

    private val replacements
        get() = mapOf(
            "<buildSrc>" to project.rootProject.layout.projectDirectory.dir("buildSrc"),
            "<nodeModules>" to project.rootProject.layout.buildDirectory.dir("js/node_modules").get(),
        ).mapValues { it.value.asFile }

    private fun String.replaceTokens() = replacements.entries.fold(this) { acc, (token, file) ->
        val posixPath = file.absolutePath.replace('\\', '/')
        acc.replace(token, posixPath)
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

    private fun replaceCwd(configNode: JsonNode) {
        configNode as ObjectNode
        configNode.put("cwd", project.kotlinJsCompilation.npmProject.dir.absolutePath)
    }

    private fun replaceOutput(configNode: JsonNode) {
        configNode as ObjectNode
        val outputNode = requireNotNull(configNode.get("output"))
        val output = outputNode.textValue()
        configNode.put("output", project.layout.projectDirectory.dir(output).asFile.absolutePath)
    }

    @TaskAction
    fun configure() {
        val configNode = mapper.readTree(configFile.get().asFile)

        replaceCwd(configNode)
        replaceTokens(configNode)
        replaceOutput(configNode)

        mapper
            .writerWithDefaultPrettyPrinter()
            .writeValue(destinationFile.get().asFile, configNode)
    }
}
