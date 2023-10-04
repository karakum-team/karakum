package io.github.sgrishchenko.karakum.gradle.plugin.tasks

import org.gradle.api.DefaultTask
import org.gradle.api.file.DirectoryProperty
import org.gradle.api.file.FileTree
import org.gradle.api.provider.Property
import org.gradle.api.tasks.InputFiles
import org.gradle.api.tasks.OutputDirectory
import org.gradle.api.tasks.TaskAction


abstract class KarakumCopy : DefaultTask() {
    @get:InputFiles
    abstract val extensionSource: Property<FileTree>

    @get:OutputDirectory
    abstract val destinationDirectory: DirectoryProperty

    @TaskAction
    fun copy() {
        project.copy {
            from(extensionSource)
            into(destinationDirectory)
        }

        val extensionPackageJson = destinationDirectory.file("package.json").get().asFile

        if (!extensionPackageJson.exists()) {
            extensionPackageJson.writeText(
                // language=JSON
                """
                    {
                        "private": true,
                        "type": "module"
                    }
                """.trimIndent()
            )
        }
    }
}
