plugins {
    id("io.github.sgrishchenko.karakum") version "1.0.0-alpha.71"
}

repositories {
    mavenCentral()
}

dependencies {
    jsMainImplementation(npm("js-file-download", "0.4.12"))
}
