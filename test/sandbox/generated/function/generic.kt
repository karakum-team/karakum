
@file:JsModule("sandbox/function/generic")
@file:JsNonModule

package sandbox.function

external fun <T, U : Any> genericFn(firstParam: T, secondParam: Double): Boolean
    