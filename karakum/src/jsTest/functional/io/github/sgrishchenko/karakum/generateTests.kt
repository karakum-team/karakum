package io.github.sgrishchenko.karakum

import io.github.sgrishchenko.karakum.configuration.MutableConfiguration
import js.array.ReadonlyArray
import js.objects.Object
import js.objects.unsafeJso
import node.buffer.BufferEncoding
import node.fs.*
import node.path.path
import kotlin.test.assertEquals
import kotlin.test.assertTrue

private suspend fun readDir(dirName: String): ReadonlyArray<String> {
    return readdir(dirName, ReaddirWithFileTypesAsyncOptions(withFileTypes = true, recursive = true))
        .filter { it.isFile() }
        .map { it.unsafeCast<Dirent<String>>() }
        .map { path.resolve(it.parentPath, it.name) }
        .toTypedArray()
}

suspend fun generateTests(
    dirName: String,
    createConfiguration: MutableConfiguration.(output: String) -> Unit,
) {
    val testConfig = loadTestConfig()

    val isUpdate = testConfig.update
    val cwd = path.resolve(testConfig.lib, "kotlin", dirName)
    val expectedOutputDirName = path.resolve(testConfig.generated, dirName)
    val actualOutputDirName = path.resolve(testConfig.output, dirName)

    val output = if (isUpdate) expectedOutputDirName else actualOutputDirName

    generate {
        this.cwd = cwd
        verbose = true
        Object.assign(this, createConfiguration(output))
    }

    if (isUpdate) {
        rm(actualOutputDirName, unsafeJso<RmOptions> {
            recursive = true
            force = true
        })
        return
    }

    val expectedFileNames = readDir(expectedOutputDirName)
    val actualFileNames = readDir(actualOutputDirName)

    assertEquals(
        expectedFileNames.map { path.relative(expectedOutputDirName, it) }.toSet(),
        actualFileNames.map { path.relative(actualOutputDirName, it) }.toSet(),
        message = "File structures in $dirName are not equal"
    )

    val report = mutableListOf<Triple<String, String, String>>()

    for (actualFileName in actualFileNames) {
        val relativeFileName = path.relative(actualOutputDirName, actualFileName)
        val expectedFileName = path.resolve(expectedOutputDirName, relativeFileName)

        val expectedFile = readFile(expectedFileName, BufferEncoding.utf8).replace("\r\n", "\n")
        val actualFile = readFile(actualFileName, BufferEncoding.utf8)

        if (expectedFile != actualFile) {
            report += Triple(relativeFileName, expectedFile, actualFile)
        }
    }

    assertTrue(
        report.isEmpty(),
        report.joinToString("\n\n") { (fileName, expected, actual) ->
            """
file: $fileName

expected: 
$expected

actual:
$actual
            """.trimEnd()
        }
    )
}
