package io.github.sgrishchenko.karakum.extension

import js.array.ReadonlyArray
import js.objects.ReadonlyRecord
import kotlinx.js.JsPlainObject
import kotlin.contracts.contract

@JsExport
@JsPlainObject
external interface GeneratedFile {
    val fileName: String
    val body: String
}

@JsExport
@JsPlainObject
external interface DerivedFile : GeneratedFile {
    val `package`: ReadonlyArray<String>
    val imports: ReadonlyArray<String>
}

@Suppress("CANNOT_CHECK_FOR_EXTERNAL_INTERFACE")
fun isDerivedFile(generatedFile: GeneratedFile): Boolean {
    contract {
        returns(true) implies (generatedFile is DerivedFile)
    }

    val generatedFileRecord = generatedFile.unsafeCast<ReadonlyRecord<String, Any?>>()

    return generatedFileRecord["package"] is ReadonlyArray<*>
            && generatedFileRecord["imports"] is ReadonlyArray<*>
}
