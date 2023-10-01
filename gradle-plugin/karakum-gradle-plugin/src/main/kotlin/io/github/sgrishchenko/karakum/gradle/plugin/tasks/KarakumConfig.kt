package io.github.sgrishchenko.karakum.gradle.plugin.tasks

import com.fasterxml.jackson.databind.JsonNode
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.databind.node.ArrayNode
import com.fasterxml.jackson.databind.node.ObjectNode
import io.github.sgrishchenko.karakum.gradle.plugin.KARAKUM_CONFIG_FILE
import org.gradle.api.DefaultTask
import org.gradle.api.provider.Property
import org.gradle.api.tasks.InputFile
import org.gradle.api.tasks.OutputFile
import org.gradle.api.tasks.TaskAction
import org.jetbrains.kotlin.gradle.targets.js.npm.npmProject
import java.io.File


abstract class KarakumConfig : DefaultTask() {

    @get:InputFile
    abstract val inputConfig: Property<File>

    @get:OutputFile
    abstract val outputConfig: Property<File>

    private val mapper = ObjectMapper()

    private val replacements
        get() = mapOf(
            "<buildSrc>" to project.rootProject.layout.projectDirectory.asFile.resolve("buildSrc"),
            "<nodeModules>" to project.rootProject.layout.buildDirectory.asFile.get().resolve("js/node_modules"),
        )

    init {
        inputConfig.convention(project.layout.projectDirectory.asFile.resolve(KARAKUM_CONFIG_FILE))
        outputConfig.convention(defaultOutputConfig)
    }

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
        configNode.put("cwd", kotlinJsCompilation.npmProject.dir.absolutePath)
    }

    private fun replaceOutput(configNode: JsonNode) {
        configNode as ObjectNode
        val outputNode = requireNotNull(configNode.get("output"))
        val output = outputNode.textValue()
        configNode.put("output", project.layout.projectDirectory.asFile.resolve(output).absolutePath)
    }

    @TaskAction
    fun configure() {
        val configNode = mapper.readTree(inputConfig.get())

        replaceCwd(configNode)
        replaceTokens(configNode)
        replaceOutput(configNode)

        mapper
            .writerWithDefaultPrettyPrinter()
            .writeValue(outputConfig.get(), configNode)
    }
}
