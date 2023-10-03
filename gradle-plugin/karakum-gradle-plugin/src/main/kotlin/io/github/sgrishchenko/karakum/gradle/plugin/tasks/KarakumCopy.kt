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
    abstract val inputExtensions: Property<FileTree>

    @get:OutputDirectory
    abstract val outputExtensions: DirectoryProperty

    init {
        inputExtensions.convention(
            listOf(
                project.rootProject.layout.projectDirectory.dir("buildSrc/karakum"),
                project.layout.projectDirectory.dir("karakum"),
            )
                .map { it.asFileTree }
                .reduce(FileTree::plus)
        )
        outputExtensions.convention(defaultOutputExtensions)
    }

    @TaskAction
    fun copy() {
        project.copy {
            from(inputExtensions)
            into(outputExtensions)
        }

        val extensionPackageJson = outputExtensions.file("package.json").get().asFile

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
