plugins {
    id("io.github.sgrishchenko.karakum") version "1.0.0-alpha.89"
}

repositories {
    mavenCentral()
}

karakum {
    library {
        name = "js-file-download"
        version = "0.4.12"
    }
}
