plugins {
    kotlin("multiplatform")
    kotlin("plugin.js-plain-objects")
}

repositories {
    mavenCentral()
}

kotlin {
    js {
        moduleName = "karakum-cli"

        nodejs()

        compilerOptions {
            target = "es2015"
        }

        binaries.executable()

        compilations.named("main") {
            packageJson {
                customField("bin", mapOf("karakum" to "kotlin/karakum-bin.mjs"))
            }
        }
    }
    
    sourceSets {
        jsMain.dependencies {
            implementation(wrappers.js)
            implementation(wrappers.node)

            implementation(project(":karakum"))
        }
    }
}
