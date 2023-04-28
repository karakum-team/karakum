
@file:JsModule("sandbox-top-level")
@file:Suppress(
    "NON_EXTERNAL_DECLARATION_IN_INAPPROPRIATE_FILE",
)

package sandbox.top.level




@Suppress("NESTED_CLASS_IN_EXTERNAL_INTERFACE")
sealed external interface MyEnum {
companion object {
val FIRST: MyEnum
val SECOND: MyEnum
}
}
    
    