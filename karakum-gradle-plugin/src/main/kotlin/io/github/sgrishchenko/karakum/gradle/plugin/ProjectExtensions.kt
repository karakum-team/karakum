package io.github.sgrishchenko.karakum.gradle.plugin

import org.gradle.api.Project
import org.gradle.kotlin.dsl.getByType
import org.jetbrains.kotlin.gradle.dsl.KotlinMultiplatformExtension
import org.jetbrains.kotlin.gradle.plugin.KotlinCompilation
import org.jetbrains.kotlin.gradle.targets.js.ir.KotlinJsIrCompilation

internal val Project.kotlinJsCompilation: KotlinJsIrCompilation
    get() {
        val extension = extensions.getByType<KotlinMultiplatformExtension>()
        val target = extension.js()

        return target.compilations.getByName(KotlinCompilation.MAIN_COMPILATION_NAME)
    }
