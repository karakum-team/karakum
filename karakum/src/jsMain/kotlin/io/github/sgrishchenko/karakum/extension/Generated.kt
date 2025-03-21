package io.github.sgrishchenko.karakum.extension

import js.array.ReadonlyArray
import js.objects.JsPlainObject
import js.objects.ReadonlyRecord
import kotlin.contracts.ExperimentalContracts
import kotlin.contracts.contract

@OptIn(ExperimentalJsExport::class)
@JsExport
@JsPlainObject
external interface GeneratedFile {
    val fileName: String
    val body: String
}

@OptIn(ExperimentalJsExport::class)
@JsExport
@JsPlainObject
external interface DerivedFile : GeneratedFile {
    val `package`: ReadonlyArray<String>
    val imports: ReadonlyArray<String>
}

@OptIn(ExperimentalContracts::class)
@Suppress("CANNOT_CHECK_FOR_EXTERNAL_INTERFACE")
fun isDerivedFile(generatedFile: GeneratedFile): Boolean {
    contract {
        returns(true) implies (generatedFile is DerivedFile)
    }

    val generatedFileRecord = generatedFile.unsafeCast<ReadonlyRecord<String, Any?>>()

    return generatedFileRecord["package"] is ReadonlyArray<*>
            && generatedFileRecord["imports"] is ReadonlyArray<*>
}
