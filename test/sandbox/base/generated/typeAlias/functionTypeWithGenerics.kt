
@file:JsModule("sandbox-base/typeAlias/functionTypeWithGenerics")
@file:JsNonModule

@file:Suppress(
    "NON_EXTERNAL_DECLARATION_IN_INAPPROPRIATE_FILE",
)

package sandbox.base.typeAlias



typealias JsonFunction<Data> = (data: Data, init: ResponseInit) -> Response
    