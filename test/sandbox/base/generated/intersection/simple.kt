
@file:JsModule("sandbox-base/intersection/simple")
@file:Suppress(
    "NON_EXTERNAL_DECLARATION_IN_INAPPROPRIATE_FILE",
)

package sandbox.base.intersection




external interface AgnosticNonIndexRouteObject : AgnosticBaseRouteObject {
var children: Array<AgnosticRouteObject>?
var index: Boolean?
}
        
    