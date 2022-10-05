
@file:JsModule("sandbox/function/callback")
@file:JsNonModule

package sandbox.function

typealias Callback = (error: String) -> Unit
external fun higherOrderFn(cb: Callback): Boolean
    