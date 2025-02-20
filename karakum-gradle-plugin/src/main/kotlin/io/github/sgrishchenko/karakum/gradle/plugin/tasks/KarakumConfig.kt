package io.github.sgrishchenko.karakum.gradle.plugin.tasks

import com.fasterxml.jackson.databind.JsonNode
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.databind.node.ArrayNode
import com.fasterxml.jackson.databind.node.ObjectNode
import com.fasterxml.jackson.databind.node.TextNode
import org.gradle.api.DefaultTask
import org.gradle.api.file.DirectoryProperty
import org.gradle.api.file.ProjectLayout
import org.gradle.api.file.RegularFileProperty
import org.gradle.api.tasks.Input
import org.gradle.api.tasks.InputFile
import org.gradle.api.tasks.Internal
import org.gradle.api.tasks.OutputFile
import org.gradle.api.tasks.TaskAction
import javax.inject.Inject

abstract class KarakumConfig : DefaultTask() {
    @get:Inject
    abstract val layout: ProjectLayout

    @get:InputFile
    abstract val configFile: RegularFileProperty

    @get:Internal("Only the path matters, handled in cwdPath")
    abstract val cwd: DirectoryProperty

    @get:Input
    val cwdPath = cwd.map { it.asFile.absoluteFile.invariantSeparatorsPath }

    @get:Internal("Only the path matters, handled in buildSrcPath")
    abstract val buildSrc: DirectoryProperty

    @get:Input
    val buildSrcPath = buildSrc.map { it.asFile.absoluteFile.invariantSeparatorsPath }

    @get:Internal("Only the path matters, handled in nodeModulesPath")
    abstract val nodeModules: DirectoryProperty

    @get:Input
    val nodeModulesPath = nodeModules.map { it.asFile.absoluteFile.invariantSeparatorsPath }

    @get:Internal("Only the path matters, handled in packageNodeModulesPath")
    abstract val packageNodeModules: DirectoryProperty

    @get:Input
    val packageNodeModulesPath = packageNodeModules.map { it.asFile.absoluteFile.invariantSeparatorsPath }

    @get:OutputFile
    abstract val destinationFile: RegularFileProperty

    private val mapper = ObjectMapper()

    private val replacements
        get() = mapOf(
            "<buildSrc>" to buildSrcPath.get(),
            "<nodeModules>" to nodeModulesPath.get(),
            "<packageNodeModules>" to packageNodeModulesPath.get(),
        )

    private fun String.replaceTokens() = replacements.entries.fold(this) { acc, (token, replacement) ->
        acc.replace(token, replacement)
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
                        currentNode.isTextual -> arrayNode.set(index, TextNode(currentNode.textValue().replaceTokens()))
                        currentNode.isContainerNode -> replaceTokens(currentNode)
                    }
                }
            }
        }
    }

    private fun replaceCwd(configNode: JsonNode) {
        configNode as ObjectNode
        configNode.put("cwd", cwdPath.get())
    }

    private fun replaceOutput(configNode: JsonNode) {
        configNode as ObjectNode
        val outputNode = requireNotNull(configNode.get("output"))
        val output = outputNode.textValue()
        configNode.put("output", layout.projectDirectory.dir(output).asFile.absoluteFile.invariantSeparatorsPath)
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
