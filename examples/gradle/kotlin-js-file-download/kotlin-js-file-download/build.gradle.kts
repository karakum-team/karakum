plugins {
    kotlin("multiplatform") version "2.2.0"
    kotlin("plugin.js-plain-objects") version "2.2.0"
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
                api("org.jetbrains.kotlin-wrappers:kotlin-js:2025.6.15")
                api("org.jetbrains.kotlin-wrappers:kotlin-web:2025.6.15")

                api(npm("js-file-download", "^0.4.12"))
            }
        }
    }
}
