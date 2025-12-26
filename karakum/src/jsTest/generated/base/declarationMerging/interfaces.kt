// Automatically generated - do not modify!

@file:JsModule("sandbox-base/declarationMerging/interfaces")

package sandbox.base.declarationMerging

external interface Example {
operator fun  invoke(param1: String): Unit
var a: Double
var b: String
}



external interface ExampleWithOverloads {
fun method(param: String): Unit
fun method(param: Double): Unit
fun method2(param: String): Unit
fun method2(param: Double): Unit
}
