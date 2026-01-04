// Automatically generated - do not modify!

package sandbox.base.mappedType.simple

import js.promise.Promise

external interface NamedOptionsFlags {
operator fun <Property : Keys> get(key: KeyWrapper<Property>): Promise<Property>?

operator fun <Property : Keys> set(key: KeyWrapper<Property>, value: Promise<Property>?)
}
