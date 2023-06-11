
@file:JsModule("sandbox-base/function/vararg")
@file:Suppress(
    "NON_EXTERNAL_DECLARATION_IN_INAPPROPRIATE_FILE",
)

package sandbox.base.function



external fun simpleVararg(vararg args: String): Unit

external fun <T : Array<String>> genericVararg(vararg args: Any? /* T */): Unit
    