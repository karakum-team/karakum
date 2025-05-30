plugins {
    `kotlin-dsl`
    kotlin("jvm")
    id("com.gradle.plugin-publish")
}

repositories {
    mavenCentral()
}

dependencies {
    compileOnly(kotlin("gradle-plugin"))

    api(libs.multiplatform)
    api(libs.plugin.jsPlainObjects)

    testImplementation(kotlin("test"))
}

kotlin {
    jvmToolchain(21)
}

gradlePlugin {
    website = "https://github.com/sgrishchenko/karakum"
    vcsUrl = "https://github.com/sgrishchenko/karakum"

    val karakum by plugins.creating {
        id = "io.github.sgrishchenko.karakum"
        displayName = "Karakum Plugin"
        description = "Converter of TypeScript declaration files to Kotlin declarations"
        tags = listOf("kotlin", "typescript")
        implementationClass = "io.github.sgrishchenko.karakum.gradle.plugin.KarakumPlugin"
    }
}

publishing {
    publications {
        withType<MavenPublication>().configureEach {
            pom {
                licenses {
                    license {
                        name = "The Apache License, Version 2.0"
                        url = "https://www.apache.org/licenses/LICENSE-2.0.txt"
                    }
                }
            }
        }
    }
}

tasks.processResources {
    expand(project.properties)
}
