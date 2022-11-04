plugins {
    `java-gradle-plugin`
    kotlin("jvm") version "1.7.20"
}

repositories {
    mavenCentral()
}

dependencies {
    testImplementation(kotlin("test"))
}

kotlin {
    jvmToolchain(17)
}

gradlePlugin {
    val karakum by plugins.creating {
        id = "karakum.gradle.plugin"
        implementationClass = "karakum.gradle.plugin.KarakumPlugin"
    }
}
