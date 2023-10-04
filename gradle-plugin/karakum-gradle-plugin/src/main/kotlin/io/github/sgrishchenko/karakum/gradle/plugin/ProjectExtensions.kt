package io.github.sgrishchenko.karakum.gradle.plugin

import org.gradle.api.Project
import org.gradle.kotlin.dsl.getByType
import org.jetbrains.kotlin.gradle.dsl.KotlinMultiplatformExtension
import org.jetbrains.kotlin.gradle.plugin.KotlinCompilation
import org.jetbrains.kotlin.gradle.plugin.mpp.KotlinJsCompilation

internal val Project.kotlinJsCompilation: KotlinJsCompilation
    get() {
        val extension = extensions.getByType<KotlinMultiplatformExtension>()
        val target = extension.js()

        return target.compilations.getByName(KotlinCompilation.MAIN_COMPILATION_NAME)
    }
