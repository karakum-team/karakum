plugins {
    `kotlin-dsl`
    kotlin("jvm")
    id("com.gradle.plugin-publish")
}

repositories {
    mavenCentral()
}

dependencies {
    api(libs.multiplatform)
    api(libs.plugin.jsPlainObjects)

    testImplementation(libs.test)
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
    val nodeVersion: String? by project
    val ktlintVersion: String? by project
    val arrowKtVersion: String? by project

    val properties = mapOf(
        "karakumVersion" to version,
        "nodeVersion" to nodeVersion,
        "ktlintVersion" to ktlintVersion,
        "arrowKtVersion" to arrowKtVersion,
    )

    inputs.properties(properties)

    expand(properties)
}
