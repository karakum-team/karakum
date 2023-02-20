
@file:JsModule("sandbox-base/indexedAccessType/simple")

@file:Suppress(
    "NON_EXTERNAL_DECLARATION_IN_INAPPROPRIATE_FILE",
    "NAME_CONTAINS_ILLEGAL_CHARS",
)

package sandbox.base.indexedAccessType




external interface AgnosticDataRouteMatch {
var params: Any /* string | number */
var matches: Array<String>
}
        


external interface MyTypeWithAccessType {
var currentUrl: URL
var currentParams: Any /* string | number */
var matches: Array<String>
}
        
    