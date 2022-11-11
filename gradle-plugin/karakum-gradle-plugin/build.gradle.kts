plugins {
    `java-gradle-plugin`
    kotlin("jvm") version "1.7.20"
    id("com.gradle.plugin-publish") version "1.0.0"
}

repositories {
    mavenCentral()
}

dependencies {
    testImplementation(kotlin("test"))
}

kotlin {
    jvmToolchain(14)
}

group = "org.jetbrains.karakum"
version = "1.0.0"

gradlePlugin {
    val karakum by plugins.creating {
        id = "org.jetbrains.karakum"
        displayName = "Karakum Plugin"
        description = "Converter of TypeScript declaration files to Kotlin declarations"
        implementationClass = "org.jetbrains.karakum.gradle.plugin.KarakumPlugin"
    }
}
