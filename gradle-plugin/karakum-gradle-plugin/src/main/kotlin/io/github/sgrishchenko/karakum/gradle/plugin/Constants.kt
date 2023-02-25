package io.github.sgrishchenko.karakum.gradle.plugin

import org.jetbrains.kotlin.gradle.targets.js.NpmPackageVersion
import java.util.Properties

internal const val KARAKUM_GRADLE_PLUGIN_GROUP = "karakum"
internal const val KARAKUM_CONFIG_FILE = "karakum.config.json"

private object Constants

val properties = Properties().apply {
    Constants::class.java.getResourceAsStream("/version.properties").use(::load)
}

internal val typescriptDependency = NpmPackageVersion("typescript", properties.getProperty("typescriptVersion"))
internal val karakumDependency = NpmPackageVersion("karakum", properties.getProperty("karakumVersion"))
