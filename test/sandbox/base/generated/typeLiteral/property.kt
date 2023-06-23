
@file:JsModule("sandbox-base/typeLiteral/property")
@file:Suppress(
    "NON_EXTERNAL_DECLARATION_IN_INAPPROPRIATE_FILE",
)

package sandbox.base.typeLiteral




external interface FetcherStates<TData /* default is Any? */>  {
var Idle: FetcherStatesIdle<TData>
}
        
    
external interface FetcherStatesIdle<TData> {
var state: String /* "idle" */
var formMethod: Nothing?
var formAction: Nothing?
var formEncType: Nothing?
var formData: Nothing?
var data: TData?
}
        