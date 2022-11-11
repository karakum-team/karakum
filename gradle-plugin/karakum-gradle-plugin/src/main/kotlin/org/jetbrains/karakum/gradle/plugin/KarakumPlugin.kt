package org.jetbrains.karakum.gradle.plugin

import org.apache.tools.ant.taskdefs.condition.Os
import org.gradle.api.Project
import org.gradle.api.Plugin
import org.gradle.api.tasks.Exec

class KarakumPlugin: Plugin<Project> {
    override fun apply(project: Project) {
        project.tasks.register("karakum", Exec::class.java) { task ->
            task.group = "karakum"

            task.commandLine("pwd")

            task.doLast {

                println(System.getProperty("os.arch"))

                when {
                    Os.isFamily(Os.FAMILY_WINDOWS) -> println("Windows")
                    Os.isFamily(Os.FAMILY_MAC) -> println("Mac")
                    Os.isFamily(Os.FAMILY_UNIX) -> println("Unix")
                    else -> error("Unsupported OS")
                }
            }
        }
    }
}
