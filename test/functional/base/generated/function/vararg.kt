// Generated by Karakum - do not modify it manually!

@file:JsModule("sandbox-base/function/vararg")
@file:Suppress(
    "NON_EXTERNAL_DECLARATION_IN_INAPPROPRIATE_FILE",
)

package sandbox.base.function

external fun simpleVararg(vararg args: String): Unit

external fun <T : js.array.ReadonlyArray<String>> genericVararg(vararg args: Any? /* T */): Unit

external fun callbackVararg(fn: Function<Unit> /* (...args: string[]) => void */): Unit
