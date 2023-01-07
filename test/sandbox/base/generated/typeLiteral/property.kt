
@file:JsModule("sandbox-base/typeLiteral/property")
@file:JsNonModule

@file:Suppress(
    "NON_EXTERNAL_DECLARATION_IN_INAPPROPRIATE_FILE",
)

package sandbox.base.typeLiteral




external interface FetcherStates<TData /* default is Any? */>  {
var Idle: FetcherStatesIdle<TData>
}
        
    
external interface FetcherStatesIdle<TData> {
var state: String
var formMethod: Any? /* some expression */
var formAction: Any? /* some expression */
var formEncType: Any? /* some expression */
var formData: Any? /* some expression */
var data: TData?
}
        