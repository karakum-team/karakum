// Generated by Karakum - do not modify it manually!

@file:JsModule("sandbox-base/indexedAccessType/simple")
@file:Suppress(
    "NON_EXTERNAL_DECLARATION_IN_INAPPROPRIATE_FILE",
)

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
