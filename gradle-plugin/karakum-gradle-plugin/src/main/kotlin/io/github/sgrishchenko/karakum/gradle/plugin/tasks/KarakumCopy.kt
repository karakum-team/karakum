package io.github.sgrishchenko.karakum.gradle.plugin.tasks

import org.gradle.api.DefaultTask
import org.gradle.api.file.DirectoryProperty
import org.gradle.api.file.FileTree
import org.gradle.api.internal.file.FileFactory
import org.gradle.api.provider.Property
import org.gradle.api.tasks.InputFiles
import org.gradle.api.tasks.OutputDirectory
import org.gradle.api.tasks.TaskAction
import javax.inject.Inject
import kotlin.io.path.exists
import kotlin.io.path.writeText


abstract class KarakumCopy
@Inject
constructor(
    fileFactory: FileFactory,
) : DefaultTask() {
    @get:InputFiles
    abstract val inputExtensions: Property<FileTree>

    @get:OutputDirectory
    abstract val outputExtensions: DirectoryProperty

    init {
        inputExtensions.convention(
            listOf(
                project.rootDir.resolve("buildSrc").resolve("karakum"),
                project.projectDir.resolve("karakum"),
            )
                .map { project.fileTree(it).asFileTree }
                .reduce(FileTree::plus)
        )
        outputExtensions.convention(fileFactory.dir(defaultOutputExtensions))
    }

    @TaskAction
    fun copy() {
        project.copy {
            from(inputExtensions)
            into(outputExtensions)
        }

        val extensionPackageJson = outputExtensions.asFile.get().toPath().resolve("package.json")

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
