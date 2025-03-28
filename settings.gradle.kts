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
            library("test", "org.jetbrains.kotlin", "kotlin-test").version(kotlinVersion)

            val kotlinCoroutinesVersion: String by settings
            library("coroutines-core", "org.jetbrains.kotlinx", "kotlinx-coroutines-core").version(kotlinCoroutinesVersion)
            library("coroutines-test", "org.jetbrains.kotlinx", "kotlinx-coroutines-test").version(kotlinCoroutinesVersion)

            val jacksonVersion: String by settings
            library("jackson-databind", "com.fasterxml.jackson.core", "jackson-databind").version(jacksonVersion)

            val ktlintVersion: String by settings
            library("ktlint-ruleEngine", "com.pinterest.ktlint", "ktlint-rule-engine").version(ktlintVersion)
            library("ktlint-ruleEngine-core", "com.pinterest.ktlint", "ktlint-rule-engine-core").version(ktlintVersion)
            library("ktlint-ruleset-standard", "com.pinterest.ktlint", "ktlint-ruleset-standard").version(ktlintVersion)

            val ec4jVersion: String by settings
            library("ec4j", "org.ec4j.core", "ec4j-core").version(ec4jVersion)

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
