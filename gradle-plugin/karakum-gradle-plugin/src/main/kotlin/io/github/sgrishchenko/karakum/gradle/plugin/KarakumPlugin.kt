package io.github.sgrishchenko.karakum.gradle.plugin

import io.github.sgrishchenko.karakum.gradle.plugin.tasks.KarakumConfig
import io.github.sgrishchenko.karakum.gradle.plugin.tasks.KarakumCopy
import io.github.sgrishchenko.karakum.gradle.plugin.tasks.KarakumGenerate
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.getValue
import org.gradle.kotlin.dsl.provideDelegate
import org.gradle.kotlin.dsl.registering

class KarakumPlugin : Plugin<Project> {
    override fun apply(project: Project): Unit = with(project) {
        val copyKarakumExtensions by tasks.registering(KarakumCopy::class) {
            group = KARAKUM_GRADLE_PLUGIN_GROUP
        }

        val configureKarakum by tasks.registering(KarakumConfig::class) {
            group = KARAKUM_GRADLE_PLUGIN_GROUP
        }

        val generateKarakumExternals by tasks.registering(KarakumGenerate::class) {
            group = KARAKUM_GRADLE_PLUGIN_GROUP

            dependsOn(copyKarakumExtensions)
            dependsOn(configureKarakum)
        }
    }
}
