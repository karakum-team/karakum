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

    implementation(libs.jackson.databind)

    testImplementation(kotlin("test"))
}

kotlin {
    jvmToolchain(17)
}

gradlePlugin {
    website.set("https://github.com/karakum-team/karakum")
    vcsUrl.set("https://github.com/karakum-team/karakum")

    val karakum by plugins.creating {
        id = "io.github.sgrishchenko.karakum"
        displayName = "Karakum Plugin"
        description = "Converter of TypeScript declaration files to Kotlin declarations"
        tags.set(listOf("kotlin", "typescript"))
        implementationClass = "io.github.sgrishchenko.karakum.gradle.plugin.KarakumPlugin"
    }
}

tasks.processResources {
    expand(project.properties)
}

afterEvaluate {
    tasks.withType<GenerateMavenPom> {
        pom.licenses {
            license {
                name.set("The Apache License, Version 2.0")
                url.set("https://www.apache.org/licenses/LICENSE-2.0.txt")
            }
        }
    }
}
