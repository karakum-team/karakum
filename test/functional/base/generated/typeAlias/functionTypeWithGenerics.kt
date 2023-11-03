
@file:JsModule("sandbox-base/typeAlias/functionTypeWithGenerics")
@file:Suppress(
    "NON_EXTERNAL_DECLARATION_IN_INAPPROPRIATE_FILE",
)

package sandbox.base.typeAlias



typealias JsonFunction<Data> = (data: Data, init: (Any)? /* use undefined for default */) -> Response
    