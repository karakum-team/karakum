// Generated by Karakum - do not modify it manually!

@file:JsModule("sandbox-base/typeAlias/functionTypeWithGenerics")
@file:Suppress(
    "NON_EXTERNAL_DECLARATION_IN_INAPPROPRIATE_FILE",
)

package sandbox.base.typeAlias

external interface Response {

}

typealias JsonFunction<Data> = (data: Data, init: (Any)? /* use undefined for default */) -> Response
