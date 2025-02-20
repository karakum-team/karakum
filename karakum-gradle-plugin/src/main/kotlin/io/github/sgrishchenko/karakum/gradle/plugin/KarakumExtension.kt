package io.github.sgrishchenko.karakum.gradle.plugin

import org.gradle.api.file.FileTree
import org.gradle.api.file.RegularFileProperty
import org.gradle.api.provider.Property

interface KarakumExtension {
    val configFile: RegularFileProperty
    val extensionSource: Property<FileTree>
}
