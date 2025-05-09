rootProject.name = "karakum"

pluginManagement {
    plugins {
        val kotlinVersion: String by settings
        kotlin("jvm") version kotlinVersion
        kotlin("multiplatform") version kotlinVersion
        kotlin("plugin.js-plain-objects") version kotlinVersion

        val mavenPublishVersion: String by settings
        id("com.vanniktech.maven.publish") version mavenPublishVersion

        val gradlePublishVersion: String by settings
        id("com.gradle.plugin-publish") version gradlePublishVersion

        val seskarVersion: String by settings
        id("io.github.turansky.seskar") version seskarVersion
    }
}

dependencyResolutionManagement {
    repositories {
        mavenCentral()
    }

    versionCatalogs {
        val libs by creating {
            val kotlinVersion: String by settings
            library("multiplatform", "org.jetbrains.kotlin.multiplatform", "org.jetbrains.kotlin.multiplatform.gradle.plugin").version(kotlinVersion)
            library("plugin-jsPlainObjects", "org.jetbrains.kotlin.plugin.js-plain-objects", "org.jetbrains.kotlin.plugin.js-plain-objects.gradle.plugin").version(kotlinVersion)

            library("test", "org.jetbrains.kotlin", "kotlin-test").version(kotlinVersion)

            val kotlinCoroutinesVersion: String by settings
            library("coroutines-core", "org.jetbrains.kotlinx", "kotlinx-coroutines-core").version(kotlinCoroutinesVersion)
            library("coroutines-test", "org.jetbrains.kotlinx", "kotlinx-coroutines-test").version(kotlinCoroutinesVersion)

            val typescriptVersion: String by settings
            library("typescript", "npm", "typescript").version(typescriptVersion)

            val typescriptJsonSchemaVersion: String by settings
            library("typescriptJsonSchema", "npm", "typescript-json-schema").version(typescriptJsonSchemaVersion)
        }

        val wrappers by creating {
            val wrappersVersion: String by settings
            from("org.jetbrains.kotlin-wrappers:kotlin-wrappers-catalog:$wrappersVersion")
        }
    }
}

include("karakum")
include("karakum-schema")
include("karakum-gradle-plugin")
