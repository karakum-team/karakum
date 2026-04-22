rootProject.name = "karakum-gradle-examples"

include("kotlin-js-file-download")
includeBuild("./kotlin-js-file-download/karakum") {
    name = "kotlin-js-file-download-karakum"
}
