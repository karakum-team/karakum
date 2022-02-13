
@file:JsModule("sandbox/comment/simple")
@file:JsNonModule

package sandbox.comment

// leading comment
external val value1: String// trailing comment

/* leading comment */
external val value2: String/* trailing comment */
/** leading comment */
external val value3: String
    