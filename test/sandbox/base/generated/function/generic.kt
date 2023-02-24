
@file:JsModule("sandbox-base/function/generic")

@file:Suppress(
    "NON_EXTERNAL_DECLARATION_IN_INAPPROPRIATE_FILE",
)

package sandbox.base.function



external fun <T, U : Any> genericFn(firstParam: T, secondParam: Double): Boolean
    