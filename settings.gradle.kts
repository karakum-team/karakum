rootProject.name = "karakum"

dependencyResolutionManagement {
    repositories {
        mavenCentral()
    }

    versionCatalogs {
        create("libs") {
            val typescriptVersion: String by settings
            library("typescript", "npm", "typescript").version(typescriptVersion)

            val tsJsonSchemaGeneratorVersion: String by settings
            library("tsJsonSchemaGenerator", "npm", "ts-json-schema-generator").version(tsJsonSchemaGeneratorVersion)
        }

        create("wrappers") {
            val wrappersVersion: String by settings
            from("org.jetbrains.kotlin-wrappers:kotlin-wrappers-catalog:$wrappersVersion")
        }
    }
}

includeBuild("build-logic")

include("karakum")
include("karakum-schema")
include("karakum-gradle-plugin")
