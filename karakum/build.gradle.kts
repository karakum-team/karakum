import org.jetbrains.kotlin.gradle.plugin.KotlinCompilation.Companion.MAIN_COMPILATION_NAME
import org.jetbrains.kotlin.gradle.plugin.KotlinCompilation.Companion.TEST_COMPILATION_NAME
import org.jetbrains.kotlin.gradle.targets.js.nodejs.NodeJsExec
import org.jetbrains.kotlin.gradle.targets.js.npm.npmProject

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

        compilations.named(MAIN_COMPILATION_NAME) {
            packageJson {
                customField("description", "Converter of TypeScript declaration files to Kotlin declarations")
                customField("keywords", listOf("kotlin", "typescript"))
                customField("license", "Apache-2.0")
                customField("readme", "kotlin/README.md")
                customField("exports", mapOf(
                    "." to "./kotlin/karakum.mjs",
                    "./karakum.d.ts" to "./kotlin/karakum.d.ts",
                    "./karakum-types.d.ts" to "./kotlin/karakum-types.d.ts",
                ))
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
            "functionalTestLib" to kotlin.js().compilations.getByName(TEST_COMPILATION_NAME).npmProject.dir.get().asFile.posixPath,
            "functionalTestGenerated" to layout.projectDirectory.dir("src/jsTest/generated").asFile.posixPath,
            "functionalTestOutput" to layout.buildDirectory.dir("karakum/output").get().asFile.posixPath,
        )
    }
}

val npmPublish = NodeJsExec.create(
    compilation = kotlin.js().compilations.getByName(MAIN_COMPILATION_NAME),
    name = "npmPublish",
) {
    group = "publishing"
    args("--run", "publish")
    dependsOn(tasks.build)
}

tasks.publish {
    dependsOn(npmPublish)
}

val File.posixPath
    get() = path.replace('\\', '/')
