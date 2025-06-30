package io.github.sgrishchenko.karakum.schema

import js.promise.await
import node.childProcess.ExecException
import node.childProcess.exec
import node.util.promisify

private val execAsync = promisify { command: String, callback: (error: ExecException?) -> Unit ->
    exec(command) { error, _, _ -> callback(error) }
}

suspend fun exec(command: String) {
    execAsync(command).await()
}
