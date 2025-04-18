package io.github.sgrishchenko.karakum.gradle.plugin

import java.util.*

internal const val KARAKUM_GRADLE_PLUGIN_GROUP = "karakum"

private object Constants

val properties = Properties().apply {
    Constants::class.java.getResourceAsStream("/version.properties").use(::load)
}

internal val nodeVersion: String = properties.getProperty("nodeVersion")
internal val karakumVersion: String = properties.getProperty("karakumVersion")
internal val arrowKtVersion: String = properties.getProperty("arrowKtVersion")
