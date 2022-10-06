
@file:JsModule("sandbox/union/optional")
@file:JsNonModule

@file:Suppress(
    "NON_EXTERNAL_DECLARATION_IN_INAPPROPRIATE_FILE",
)

package sandbox.union



external fun nullFn(optionalParam: String?): Boolean

external fun undefinedFn(optionalParam: String?): Boolean
    