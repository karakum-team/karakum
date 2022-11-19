package org.jetbrains.karakum.gradle.plugin

import org.apache.tools.ant.taskdefs.condition.Os
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.tasks.Exec
import org.jetbrains.karakum.gradle.plugin.tasks.KarakumConfig
import org.jetbrains.karakum.gradle.plugin.tasks.KarakumPluginsCopy

class KarakumPlugin : Plugin<Project> {
    override fun apply(project: Project) {
        val binaryName = when {
            Os.isFamily(Os.FAMILY_WINDOWS) -> "cli-win.exe"
            Os.isFamily(Os.FAMILY_MAC) -> "cli-macos"
            Os.isFamily(Os.FAMILY_UNIX) -> "cli-linux"
            else -> error("Unsupported OS")
        }

        val binaryResource = requireNotNull(KarakumPlugin::class.java.getResource("/$binaryName"))
        val binaryDir = project.buildDir.resolve("karakum/bin")
        val binaryFile = binaryDir.resolve(binaryName)

        project.tasks.register("extractKarakumBinary") { task ->
            task.group = KARAKUM_GRADLE_PLUGIN_GROUP

            task.doLast {
                binaryDir.mkdirs()
                binaryFile.writeBytes(binaryResource.readBytes())
            }
        }

        project.tasks.register("makeKarakumBinaryExecutable", Exec::class.java) { task ->
            task.group = KARAKUM_GRADLE_PLUGIN_GROUP

            task.dependsOn("extractKarakumBinary")

            if (Os.isFamily(Os.FAMILY_MAC) || Os.isFamily(Os.FAMILY_UNIX)) {
                task.commandLine("chmod", "+x", binaryFile)
            } else {
                task.commandLine("echo", "Karakum: Executable rights granted")
            }
        }

        project.tasks.register("copyKarakumPlugins", KarakumPluginsCopy::class.java) { task ->
            task.group = KARAKUM_GRADLE_PLUGIN_GROUP
        }

        project.tasks.register("configureKarakum", KarakumConfig::class.java) { task ->
            task.group = KARAKUM_GRADLE_PLUGIN_GROUP

            task.dependsOn("copyKarakumPlugins")
        }

        project.tasks.register("generateKarakumExternals", Exec::class.java) { task ->
            task.group = KARAKUM_GRADLE_PLUGIN_GROUP

            task.dependsOn(
                "makeKarakumBinaryExecutable",
                "configureKarakum",
            )

            task.commandLine(binaryFile, "--config", project.buildDir.resolve("karakum/$KARAKUM_CONFIG_FILE").absolutePath)
        }
    }
}
