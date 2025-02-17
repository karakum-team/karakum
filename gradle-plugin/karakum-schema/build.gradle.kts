import org.jetbrains.kotlin.gradle.targets.js.nodejs.NodeJsExec

plugins {
    kotlin("multiplatform")
    kotlin("plugin.js-plain-objects")
}

repositories {
    mavenCentral()
}

kotlin {
    js {
        moduleName = "karakum-schema"

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

            implementation(libs.typescript.jsonSchema.get().let {
                npm(it.name, requireNotNull(it.version))
            })
        }
    }
}

val outputPath: String = layout.projectDirectory.asFile.path

tasks.withType<NodeJsExec>().configureEach {
    args(outputPath)
}
