
@file:JsModule("sandbox-bundle")
@file:Suppress(
    "NON_EXTERNAL_DECLARATION_IN_INAPPROPRIATE_FILE",
)

package sandbox.bundle



external fun myFunction3(firstParam: String, secondParam: MyFunction3SecondParam): Unit

external fun myFunction2(firstParam: String, secondParam: Double): Unit

external fun myFunction1(firstParam: String, secondParam: Double): Unit
    
@Suppress(
    "NAME_CONTAINS_ILLEGAL_CHARS",
    "NESTED_CLASS_IN_EXTERNAL_INTERFACE",
)
@JsName("""(/*union*/{basic: 'basic', complete: 'complete'}/*union*/)""")
sealed external interface MyFunction3SecondParam {
companion object {
val basic: MyFunction3SecondParam
val complete: MyFunction3SecondParam
}
}
    