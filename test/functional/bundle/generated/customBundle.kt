// Generated by Karakum - do not modify it manually!

@file:JsModule("sandbox-bundle")
@file:Suppress(
    "NON_EXTERNAL_DECLARATION_IN_INAPPROPRIATE_FILE",
)

package sandbox.bundle

external fun myFunction3(firstParam: String, secondParam: MyFunction3SecondParam): Unit

external fun myFunction2(firstParam: String, secondParam: Double): Unit

external fun myFunction1(firstParam: String, secondParam: Double): Unit
sealed external interface MyFunction3SecondParam {
companion object {
@seskar.js.JsValue("basic")
val basic: MyFunction3SecondParam
@seskar.js.JsValue("complete")
val complete: MyFunction3SecondParam
}
}