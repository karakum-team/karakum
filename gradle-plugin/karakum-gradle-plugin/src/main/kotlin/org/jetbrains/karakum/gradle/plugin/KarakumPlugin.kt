package org.jetbrains.karakum.gradle.plugin

import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.getValue
import org.gradle.kotlin.dsl.provideDelegate
import org.gradle.kotlin.dsl.registering
import org.gradle.kotlin.dsl.withType
import org.jetbrains.karakum.gradle.plugin.tasks.KarakumConfig
import org.jetbrains.karakum.gradle.plugin.tasks.KarakumGenerate
import org.jetbrains.karakum.gradle.plugin.tasks.KarakumPluginsCopy
import org.jetbrains.kotlin.gradle.dsl.KotlinJsCompile

class KarakumPlugin : Plugin<Project> {
    override fun apply(project: Project): Unit = with(project) {
        val copyKarakumPlugins by tasks.registering(KarakumPluginsCopy::class) {
            group = KARAKUM_GRADLE_PLUGIN_GROUP
        }

        val configureKarakum by tasks.registering(KarakumConfig::class) {
            group = KARAKUM_GRADLE_PLUGIN_GROUP

            dependsOn(copyKarakumPlugins)
        }

        val generateKarakumExternals by tasks.registering(KarakumGenerate::class) {
            group = KARAKUM_GRADLE_PLUGIN_GROUP

            dependsOn(configureKarakum)
        }

        tasks.withType<KotlinJsCompile> {
            dependsOn(generateKarakumExternals)
        }
    }
}
