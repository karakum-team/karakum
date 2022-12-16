plugins {
    `kotlin-dsl`
    kotlin("jvm") version "1.7.20"
    id("com.gradle.plugin-publish") version "1.0.0"
}

repositories {
    mavenCentral()
}

group = "org.jetbrains.karakum"
version = "1.0.0"

dependencies {
    compileOnly(kotlin("gradle-plugin"))

    implementation("com.fasterxml.jackson.core:jackson-databind:2.14.0")

    testImplementation(kotlin("test"))
}

kotlin {
    jvmToolchain(14)
}

gradlePlugin {
    val karakum by plugins.creating {
        id = "org.jetbrains.karakum"
        displayName = "Karakum Plugin"
        description = "Converter of TypeScript declaration files to Kotlin declarations"
        implementationClass = "org.jetbrains.karakum.gradle.plugin.KarakumPlugin"
    }
}
