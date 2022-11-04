
@file:JsModule("sandbox-top-level-granularity")
@file:JsNonModule

@file:Suppress(
    "NON_EXTERNAL_DECLARATION_IN_INAPPROPRIATE_FILE",
)

package sandbox.top.level.granularity



external fun myFunction1(firstParam: String, secondParam: Double): Unit

external fun myFunction1(firstParam: Boolean, secondParam: Double): Unit
    