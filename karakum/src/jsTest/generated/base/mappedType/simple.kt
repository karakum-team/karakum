// Automatically generated - do not modify!

@file:JsModule("sandbox-base/mappedType/simple")

package sandbox.base.mappedType

import js.promise.Promise

sealed external interface Keys {
companion object {
@seskar.js.JsValue("one")
val one: Keys
@seskar.js.JsValue("two")
val two: Keys
}
}

external interface OptionsFlags {
operator fun <Property : Keys> get(key: Property): Promise<Property>?

operator fun <Property : Keys> set(key: Property, value: Promise<Property>?)
}

external interface ReadonlyOptionsFlags {
operator fun <Property : Keys> get(key: Property): Promise<Property>?
}

external interface OptionsFlagsWithTypeLiteral {
operator fun <Property : Keys> get(key: Property): Promise<Property>?

operator fun <Property : Keys> set(key: Property, value: Promise<Property>?)
var three: String
}

external interface KeyWrapper<T> {
var key: T
}

external interface NamedOptionsFlags {
operator fun <Property : Keys> get(key: KeyWrapper<Property>): Promise<Property>?

operator fun <Property : Keys> set(key: KeyWrapper<Property>, value: Promise<Property>?)
}

external interface OptionalOptionsFlags {
operator fun <Property : Keys> get(key: KeyWrapper<Property>): Promise<Property>?
}

external interface MappedTypeContainer {
var mappedType: MappedTypeContainerMappedType
}
external interface MappedTypeContainerMappedType {
operator fun <Property : Keys> get(key: Property): Promise<Property>?

operator fun <Property : Keys> set(key: Property, value: Promise<Property>?)
}