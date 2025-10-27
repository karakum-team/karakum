import org.jetbrains.kotlin.gradle.targets.js.nodejs.NodeJsExec

plugins {
    kotlin("multiplatform")
    kotlin("plugin.js-plain-objects")
    `node-conventions`
}

repositories {
    mavenCentral()
}

kotlin {
    js {
        outputModuleName = "karakum-schema"

        nodejs()

        compilerOptions {
            target = "es2015"
        }

        binaries.executable()
    }
    
    sourceSets {
        jsMain.dependencies {
            implementation(wrappers.js)
            implementation(wrappers.node)

            implementation(project(":karakum"))

            implementation(libs.tsJsonSchemaGenerator.get().let {
                npm(it.name, requireNotNull(it.version))
            })
        }
    }
}

val outputPath: String = layout.projectDirectory.asFile.path

tasks.withType<NodeJsExec>().configureEach {
    args(outputPath)
}
