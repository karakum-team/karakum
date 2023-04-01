
@file:JsModule("sandbox-base/comment/simple")
@file:Suppress(
    "NON_EXTERNAL_DECLARATION_IN_INAPPROPRIATE_FILE",
)

package sandbox.base.comment



// leading comment
external val value1: String// trailing comment


/* leading comment */
external val value2: String/* trailing comment */

/** leading comment */
external val value3: String
    