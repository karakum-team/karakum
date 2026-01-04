// Automatically generated - do not modify!

package sandbox.base.mappedType.simple

import js.promise.Promise

external interface MappedTypeContainerMappedType {
operator fun <Property : Keys> get(key: Property): Promise<Property>?

operator fun <Property : Keys> set(key: Property, value: Promise<Property>?)
}
