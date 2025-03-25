import com.vanniktech.maven.publish.SonatypeHost
import org.jetbrains.kotlin.gradle.plugin.KotlinCompilation.Companion.MAIN_COMPILATION_NAME
import org.jetbrains.kotlin.gradle.plugin.KotlinCompilation.Companion.TEST_COMPILATION_NAME
import org.jetbrains.kotlin.gradle.targets.js.nodejs.NodeJsExec
import org.jetbrains.kotlin.gradle.targets.js.npm.npmProject

plugins {
    kotlin("multiplatform")
    kotlin("plugin.js-plain-objects")
    id("io.github.turansky.seskar")
    id("com.vanniktech.maven.publish")
    `node-conventions`
}

repositories {
    mavenCentral()
}

kotlin {
    js {
        outputModuleName = "karakum"

        nodejs()

        compilerOptions {
            target = "es2015"

            optIn.addAll(
                "kotlin.contracts.ExperimentalContracts",
                "kotlin.js.ExperimentalJsExport",
            )
        }

        binaries.executable()
        generateTypeScriptDefinitions()

        compilations.named(MAIN_COMPILATION_NAME) {
            packageJson {
                customField("description", description)
                customField("keywords", listOf("kotlin", "typescript"))
                customField("license", "Apache-2.0")
                customField("exports", "./kotlin/karakum.mjs")
                customField("bin", mapOf("karakum" to "kotlin/karakum-bin.mjs"))
                customField("scripts", mapOf("distribute" to "npm publish"))
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

mavenPublishing {
    pom {
        name = project.name
        description = project.description
        url = "https://github.com/sgrishchenko/karakum"

        licenses {
            license {
                name = "The Apache License, Version 2.0"
                url = "https://www.apache.org/licenses/LICENSE-2.0.txt"
            }
        }

        developers {
            developer {
                id = "sgrishchenko"
                name = "Sergei Grishchenko"
                email = "s.i.grishchenko@gmail.com"
            }
        }

        scm {
            connection = "scm:git:git://github.com/sgrishchenko/karakum.git"
            developerConnection = "scm:git:git@github.com:sgrishchenko/karakum.git"
            url = "https://github.com/sgrishchenko/karakum"
        }
    }

    publishToMavenCentral(SonatypeHost.CENTRAL_PORTAL)

    signAllPublications()
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

val copyNpmResources by tasks.registering(Copy::class) {
    group = "publishing"
    from(
        rootProject.layout.projectDirectory.file("LICENSE"),
        rootProject.layout.projectDirectory.file("README.md"),
    )
    into(kotlin.js().compilations.getByName(MAIN_COMPILATION_NAME).npmProject.dir)
    dependsOn(tasks.named("jsProductionExecutableCompileSync"))
}

val npmPublish = NodeJsExec.register(
    compilation = kotlin.js().compilations.getByName(MAIN_COMPILATION_NAME),
    name = "npmPublish",
) {
    val npmAuthToken: String? by project

    group = "publishing"
    args("--run", "distribute", "--", "--//registry.npmjs.org/:_authToken=$npmAuthToken")
    dependsOn(
        copyNpmResources,
        tasks.build,
    )
}

val File.posixPath
    get() = path.replace('\\', '/')
