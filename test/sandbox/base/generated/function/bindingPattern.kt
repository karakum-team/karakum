
@file:JsModule("sandbox-base/function/bindingPattern")
@file:JsNonModule

@file:Suppress(
    "NON_EXTERNAL_DECLARATION_IN_INAPPROPRIATE_FILE",
    "NAME_CONTAINS_ILLEGAL_CHARS",
)

package sandbox.base.function




external interface Path {
var pathname: String
var search: String
var hash: String
}
    

external fun createPath(param: Path): String
    