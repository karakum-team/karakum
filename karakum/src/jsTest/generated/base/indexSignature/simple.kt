// Automatically generated - do not modify!

@file:JsModule("sandbox-base/indexSignature/simple")

package sandbox.base.indexSignature

external interface RouteData {
operator fun get(key: String): Any?

operator fun set(key: String, value: Any?)
}

external interface ReadonlyRouteData {
operator fun get(key: String): Any?
}

external interface SomeData {
operator fun get(key: js.symbol.Symbol): Double?

operator fun set(key: js.symbol.Symbol, value: Double?)
}

external interface IntersectionSomeData {
var x: String
operator fun get(key: js.symbol.Symbol): Double?

operator fun set(key: js.symbol.Symbol, value: Double?)
}
