// Generated by Karakum - do not modify it manually!

@file:JsModule("sandbox-base/function/bindingPattern")
@file:Suppress(
    "NON_EXTERNAL_DECLARATION_IN_INAPPROPRIATE_FILE",
)

package sandbox.base.function


external interface Path {
var pathname: String
var search: String
var hash: String
}
    

external fun createPath(options: Path): String
