plugins {
    kotlin("multiplatform")
    kotlin("plugin.js-plain-objects")
    id("io.github.turansky.seskar")
    `maven-publish`
}

repositories {
    mavenCentral()
}

kotlin {
    js {
        moduleName = "karakum"

        nodejs()

        compilerOptions {
            target = "es2015"
        }

        binaries.executable()
        generateTypeScriptDefinitions()
    }
    
    sourceSets {
        jsMain.dependencies {
            implementation(libs.coroutines.core)
            api(wrappers.js)
            api(wrappers.node)
            api(wrappers.typescript)
        }

        jsTest {
//            TODO: fix errors in test and move to sources
            resources.srcDir(layout.projectDirectory.dir("src/jsTest/generated"))
            kotlin.srcDir(layout.projectDirectory.dir("src/jsTest/functional"))

            dependencies {
                implementation(libs.test)
                implementation(libs.coroutines.test)
            }
        }
    }
}

tasks.named<ProcessResources>("jsTestProcessResources") {
    expand(
        "functionalTestLib" to layout.projectDirectory.dir("src/jsTest/resources").asFile.path,
        "functionalTestGenerated" to layout.projectDirectory.dir("src/jsTest/generated").asFile.path,
        "functionalTestOutput" to layout.buildDirectory.dir("karakum/output").get().asFile.path,
    )
}
