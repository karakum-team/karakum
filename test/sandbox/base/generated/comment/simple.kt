
@file:JsModule("sandbox-base/comment/simple")
@file:JsNonModule

@file:Suppress(
    "NON_EXTERNAL_DECLARATION_IN_INAPPROPRIATE_FILE",
    "NAME_CONTAINS_ILLEGAL_CHARS",
)

package sandbox.base.comment



// leading comment
external val value1: String// trailing comment


/* leading comment */
external val value2: String/* trailing comment */

/** leading comment */
external val value3: String
    