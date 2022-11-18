package org.jetbrains.karakum.gradle.plugin.tasks

import com.fasterxml.jackson.databind.ObjectMapper
import org.gradle.api.DefaultTask
import org.gradle.api.tasks.TaskAction


abstract class KarakumConfig : DefaultTask() {

    @TaskAction
    fun process() {
        val configFile = project.projectDir.resolve("karakum.config.json") // TODO: remove hardcode

        val mapper = ObjectMapper()
        val config = mapper.readTree(configFile)

        println(config["input"])
    }
}
