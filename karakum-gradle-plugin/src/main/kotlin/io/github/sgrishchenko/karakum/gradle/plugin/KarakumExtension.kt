package io.github.sgrishchenko.karakum.gradle.plugin

import org.gradle.api.artifacts.Dependency
import org.gradle.api.file.DirectoryProperty
import org.gradle.api.provider.Property
import org.gradle.api.provider.Provider

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

    fun library(dependency: Provider<Dependency>) {
        libraryName.set(dependency.map { it.name })
        libraryVersion.set(dependency.map { it.version })
    }

    interface Library {
        val name: Property<String>
        val version: Property<String>
    }
}
