package io.github.sgrishchenko.karakum.gradle.plugin

import org.gradle.api.file.DirectoryProperty

interface KarakumExtension {
    val output: DirectoryProperty
}
