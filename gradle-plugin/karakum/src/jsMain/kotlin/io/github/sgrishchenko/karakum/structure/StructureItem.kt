package io.github.sgrishchenko.karakum.structure

import js.array.ReadonlyArray
import js.objects.JsPlainObject
import typescript.Node

@JsPlainObject
external interface StructureItem {
    val fileName: String
    val `package`: ReadonlyArray<String>
    val moduleName: String
    val qualifier: String?
    val hasRuntime: Boolean
    val imports: ReadonlyArray<String>
}

@JsPlainObject
external interface InputStructureItemMeta {
    val type: String
    val name: String
}

@JsPlainObject
external interface InputStructureItem : StructureItem {
    val nodes: ReadonlyArray<Node>
    val meta: InputStructureItemMeta
}
