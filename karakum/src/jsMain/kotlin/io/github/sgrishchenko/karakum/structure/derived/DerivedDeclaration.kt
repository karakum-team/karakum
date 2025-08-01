package io.github.sgrishchenko.karakum.structure.derived

import io.github.sgrishchenko.karakum.structure.StructureItem
import kotlinx.js.JsPlainObject
import typescript.ModuleDeclaration

@JsExport
@JsPlainObject
external interface DerivedDeclaration {
    val sourceFileName: String
    val namespace: ModuleDeclaration?
    val fileName: String
    val body: String
}

@JsPlainObject
external interface DerivedDeclarationStructureItem : StructureItem {
    val body: String
}
