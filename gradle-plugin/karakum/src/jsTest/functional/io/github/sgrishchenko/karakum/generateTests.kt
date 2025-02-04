package io.github.sgrishchenko.karakum

import io.github.sgrishchenko.karakum.configuration.PartialConfiguration
import js.array.ReadonlyArray
import js.objects.Object
import js.objects.jso
import js.objects.recordOf
import node.buffer.BufferEncoding
import node.fs.*
import node.path.path
import kotlin.test.assertEquals
import kotlin.test.assertTrue
import io.github.sgrishchenko.karakum.run as karakumRun

private val isUpdate = false // TODO: Boolean(process.env['KARAKUM_TEST_UPDATE']);

private suspend fun readDir(dirName: String): ReadonlyArray<String> {
    return readdir(dirName, ReaddirWithFileTypesAsyncOptions(withFileTypes = true, recursive = true))
        .filter { it.isFile() }
        .map { path.resolve(it.parentPath, it.name) }
        .toTypedArray()
}

suspend fun generateTests(
    dirName: String,
    createConfiguration: (output: String) -> PartialConfiguration,
) {
    val testConfig = loadTestConfig()

    val cwd = path.resolve(testConfig.lib, dirName)
    val expectedOutputDirName = path.resolve(testConfig.generated, dirName)
    val actualOutputDirName = path.resolve(testConfig.output, dirName)

    val output = if (isUpdate) expectedOutputDirName else actualOutputDirName
    val configuration = createConfiguration(output)

    karakumRun(
        // TODO: create ticket for JsPlainObject
        Object.assign(configuration, recordOf<String, Any?>().apply {
            this["cwd"] = cwd
            this["verbose"] = false
        }.unsafeCast<PartialConfiguration>()),
    )

    if (isUpdate) {
        rm(actualOutputDirName, jso<RmOptions> {
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
