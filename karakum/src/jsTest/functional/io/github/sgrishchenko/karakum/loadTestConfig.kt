package io.github.sgrishchenko.karakum

import js.json.parse
import node.buffer.BufferEncoding
import node.fs.readFile

external interface TestConfig {
    val lib: String
    val generated: String
    val output: String
}

suspend fun loadTestConfig(): TestConfig {
    val configFile = readFile("kotlin/test.config.json", BufferEncoding.utf8)
    return parse(configFile)
}
