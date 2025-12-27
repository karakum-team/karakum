package io.github.sgrishchenko.karakum.gradle.plugin

import org.gradle.api.file.DirectoryProperty
import org.gradle.api.provider.Property

interface KarakumExtension {
    val output: DirectoryProperty
    val libraryName: Property<String>
    val libraryVersion: Property<String>

    fun library(block: Library.() -> Unit = {}) {
        val library = object : Library {
            override val name = libraryName
            override val version = libraryVersion
        }
        block(library)
    }

    interface Library {
        val name: Property<String>
        val version: Property<String>
    }
}
