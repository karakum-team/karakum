rootProject.name = "kotlin-js-file-download"

include("kotlin-js-file-download")
includeBuild("./kotlin-js-file-download/karakum") {
    name = "kotlin-js-file-download-karakum"
}
