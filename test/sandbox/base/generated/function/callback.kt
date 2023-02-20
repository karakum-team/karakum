
@file:JsModule("sandbox-base/function/callback")

@file:Suppress(
    "NON_EXTERNAL_DECLARATION_IN_INAPPROPRIATE_FILE",
    "NAME_CONTAINS_ILLEGAL_CHARS",
)

package sandbox.base.function



typealias Callback = (error: String) -> Unit

external fun higherOrderFn(cb: Callback): Boolean
    