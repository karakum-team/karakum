package io.github.sgrishchenko.karakum.gradle.plugin.tasks

import io.github.sgrishchenko.karakum.gradle.plugin.KARAKUM_CONFIG_FILE
import org.gradle.api.Task
import org.gradle.api.file.Directory
import org.gradle.api.file.RegularFile
import org.gradle.kotlin.dsl.getByType
import org.jetbrains.kotlin.gradle.dsl.KotlinMultiplatformExtension
import org.jetbrains.kotlin.gradle.plugin.KotlinCompilation
import org.jetbrains.kotlin.gradle.plugin.mpp.KotlinJsCompilation
import org.jetbrains.kotlin.gradle.targets.js.npm.npmProject

val Task.kotlinJsCompilation: KotlinJsCompilation
    get() {
        val extension = project.extensions.getByType<KotlinMultiplatformExtension>()
        val target = extension.js()

        return target.compilations.getByName(KotlinCompilation.MAIN_COMPILATION_NAME)
    }

val Task.defaultOutputConfig: RegularFile
    get() = project.layout.buildDirectory.file("karakum/$KARAKUM_CONFIG_FILE").get()

val Task.defaultOutputExtensions: Directory
    get() {
        val npmProjectDirectory = project.provider { kotlinJsCompilation.npmProject.dir }

        return project.layout.dir(npmProjectDirectory).get().dir("karakum")
    }
