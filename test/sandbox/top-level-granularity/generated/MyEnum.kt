
@file:JsModule("sandbox-top-level-granularity")
@file:Suppress(
    "NON_EXTERNAL_DECLARATION_IN_INAPPROPRIATE_FILE",
)

package sandbox.top.level.granularity




@Suppress("NESTED_CLASS_IN_EXTERNAL_INTERFACE")
sealed external interface MyEnum {
companion object {
val FIRST: MyEnum
val SECOND: MyEnum
}
}
    
    