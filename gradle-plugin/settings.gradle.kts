rootProject.name = "karakum-gradle-plugin"

pluginManagement {
    plugins {
        val kotlinVersion: String by settings
        kotlin("jvm") version kotlinVersion

        val publishVersion: String by settings
        id("com.gradle.plugin-publish") version publishVersion
    }
}

dependencyResolutionManagement {
    versionCatalogs {
        val libs by creating {
            val jacksonVersion: String by settings
            library("jackson-databind", "com.fasterxml.jackson.core", "jackson-databind").version(jacksonVersion)
            val ktlintVersion: String by settings
            library("ktlint-ruleEngine", "com.pinterest.ktlint", "ktlint-rule-engine").version(ktlintVersion)
            library("ktlint-ruleEngine-core", "com.pinterest.ktlint", "ktlint-rule-engine-core").version(ktlintVersion)
            library("ktlint-ruleset-standard", "com.pinterest.ktlint", "ktlint-ruleset-standard").version(ktlintVersion)
            val ec4jVersion: String by settings
            library("ec4j", "org.ec4j.core", "ec4j-core").version(ec4jVersion)
        }
    }
}

include("karakum-gradle-plugin")
