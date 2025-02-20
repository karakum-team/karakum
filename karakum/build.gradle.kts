import org.jetbrains.kotlin.gradle.targets.js.nodejs.NodeJsExec

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

        compilations.named("main") {
            packageJson {
                customField("description", "Converter of TypeScript declaration files to Kotlin declarations")
                customField("keywords", listOf("kotlin", "typescript"))
                customField("license", "Apache-2.0")
                customField("exports", "./kotlin/karakum.mjs")
                customField("bin", mapOf("karakum" to "kotlin/karakum-bin.mjs"))
                customField("scripts", mapOf("publish" to "npm publish"))
            }
        }
    }

    sourceSets {
        jsMain.dependencies {
            implementation(libs.coroutines.core)

            api(wrappers.js)
            api(wrappers.node)
            api(wrappers.typescript)

            api(libs.typescript.get().let {
                peerNpm(it.name, requireNotNull(it.version))
            })
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

tasks.named<ProcessResources>("jsProcessResources") {
    from(
        rootProject.layout.projectDirectory.file("LICENSE"),
        rootProject.layout.projectDirectory.file("README.md"),
    )
}

tasks.named<ProcessResources>("jsTestProcessResources") {
    filesMatching("test.config.json") {
        expand(
            "functionalTestLib" to layout.projectDirectory.dir("src/jsTest/resources").asFile.path,
            "functionalTestGenerated" to layout.projectDirectory.dir("src/jsTest/generated").asFile.path,
            "functionalTestOutput" to layout.buildDirectory.dir("karakum/output").get().asFile.path,
        )
    }
}

val npmPublish = NodeJsExec.create(
    compilation = kotlin.js().compilations.getByName("main"),
    name = "npmPublish",
) {
    group = "publishing"
    args("--run", "publish")
    dependsOn(tasks.build)
}

tasks.publish {
    dependsOn(npmPublish)
}
