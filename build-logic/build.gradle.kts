import java.util.Properties

plugins {
    `kotlin-dsl`
}

repositories {
    gradlePluginPortal()
}

val rootProperties = Properties().apply {
    file("../gradle.properties").inputStream().use { load(it) }
}

dependencies {
    val kotlinVersion: String by rootProperties

    implementation(kotlin("gradle-plugin", kotlinVersion))
}
