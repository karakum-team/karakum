// Automatically generated - do not modify!

@file:JsModule("sandbox-base/typeLiteral/property")

package sandbox.base.typeLiteral

external interface FetcherStates<TData /* default is Any? */> {
var Idle: FetcherStatesIdle<TData>
}

external class FetcherClass<TData /* default is Any? */> {
var state: FetcherClassState<TData>
}

external interface FetcherInterface<TData /* default is Any? */> {
var state: FetcherInterfaceState<TData>
}

external interface BaseLiteralType {
var one: String
}

external interface LiteralTypeContainer<TData /* default is Any? */> {
var state: LiteralTypeContainerState<TData>
}
external interface FetcherStatesIdle<TData /* default is Any? */> {
var state: String /* "idle" */
var formMethod: Nothing?
var formAction: Nothing?
var formEncType: Nothing?
var formData: Nothing?
var data: TData?
}

external interface FetcherClassState<TData /* default is Any? */> {
var data: TData?
}

external interface FetcherInterfaceState<TData /* default is Any? */> {
var data: TData?
}

external interface LiteralTypeContainerState<TData /* default is Any? */>  : BaseLiteralType {
var data: TData?
}