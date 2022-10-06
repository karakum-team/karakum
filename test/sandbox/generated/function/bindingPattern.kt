
@file:JsModule("sandbox/function/bindingPattern")
@file:JsNonModule

package sandbox.function


external interface Path {
var pathname: String
var search: String
var hash: String
}
    
external fun createPath(param: Path): String
    