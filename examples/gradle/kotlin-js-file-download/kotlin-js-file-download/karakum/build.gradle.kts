plugins {
    id("io.github.sgrishchenko.karakum") version "1.0.0-alpha.67"
}

repositories {
    // TODO: Remove
    mavenLocal()
    mavenCentral()
}

dependencies {
    jsMainImplementation(npm("js-file-download", "0.4.12"))
}
