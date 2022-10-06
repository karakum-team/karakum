
@file:JsModule("sandbox/function/generic")
@file:JsNonModule

@file:Suppress(
    "NON_EXTERNAL_DECLARATION_IN_INAPPROPRIATE_FILE",
)

package sandbox.function



external fun <T, U : Any> genericFn(firstParam: T, secondParam: Double): Boolean
    