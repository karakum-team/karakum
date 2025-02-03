plugins {
    kotlin("multiplatform")
    kotlin("plugin.js-plain-objects")
    id("io.github.turansky.seskar")
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
        generateTypeScriptDefinitions()
    }
    
    sourceSets {
        commonMain.dependencies {
            implementation(libs.test)
        }

        jsMain.dependencies {
            implementation(libs.coroutines.core)
            implementation(kotlinWrappers.js)
            implementation(kotlinWrappers.node)
            implementation(kotlinWrappers.typescript)
        }

        jsTest {
//            TODO: fix errors in test
//            kotlin.srcDir(projectDir.resolve("src/jsTest/generated"))
            kotlin.srcDir(projectDir.resolve("src/jsTest/functional"))
        }
    }
}
