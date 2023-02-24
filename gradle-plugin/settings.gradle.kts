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
        }
    }
}

include("karakum-gradle-plugin")
