plugins {
    kotlin("multiplatform") version "2.1.20"
    kotlin("plugin.js-plain-objects") version "2.1.20"
}

repositories {
    mavenCentral()
}

kotlin {
    js {
        browser()

        compilerOptions {
            target = "es2015"
        }
    }

    sourceSets {
        jsMain {
            dependencies {
                api("org.jetbrains.kotlin-wrappers:kotlin-js:2025.3.19")
                api("org.jetbrains.kotlin-wrappers:kotlin-web:2025.3.19")

                api(npm("js-file-download", "^0.4.12"))
            }
        }
    }
}
