plugins {
    kotlin("multiplatform")
}

repositories {
    mavenCentral()
}

kotlin {
    js {
        moduleName = "karakum"

        nodejs()
        useEsModules()

        compilerOptions {
            target = "es2015"
        }

        binaries.executable()
    }
    
    sourceSets {
        jsMain.dependencies {
        }
    }
}
