
@file:JsModule("sandbox-top-level-granularity")
@file:Suppress(
    "NON_EXTERNAL_DECLARATION_IN_INAPPROPRIATE_FILE",
)

package sandbox.top.level.granularity



external fun myFunction2(firstParam: String, secondParam: Double): Unit

external fun myFunction2(firstParam: Boolean, secondParam: Double): Unit
    