
@file:JsModule("sandbox/function/callback")
@file:JsNonModule

@file:Suppress(
    "NON_EXTERNAL_DECLARATION_IN_INAPPROPRIATE_FILE",
)

package sandbox.function



typealias Callback = (error: String) -> Unit

external fun higherOrderFn(cb: Callback): Boolean
    