// Automatically generated - do not modify!

@file:JsModule("sandbox-base/indexedAccessType/simple")

package sandbox.base.indexedAccessType

external interface URL {

}

external interface AgnosticDataRouteMatch {
var params: Any /* string | number */
var matches: js.array.ReadonlyArray<String>
}

external interface MyTypeWithAccessType {
var currentUrl: URL
var currentParams: Any /* string | number */
var matches: js.array.ReadonlyArray<String>
}
