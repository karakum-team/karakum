package io.github.karakum.gradle.plugin

import org.jetbrains.kotlin.gradle.targets.js.NpmPackageVersion

internal const val KARAKUM_GRADLE_PLUGIN_GROUP = "karakum"
internal const val KARAKUM_CONFIG_FILE = "karakum.config.json"

internal val typescriptDependency = NpmPackageVersion("typescript", "^4.9.4")
internal val karakumDependency = NpmPackageVersion("karakum", "1.0.0-alpha.0")
