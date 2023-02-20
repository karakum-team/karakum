
@file:JsModule("sandbox-base/union/optional")

@file:Suppress(
    "NON_EXTERNAL_DECLARATION_IN_INAPPROPRIATE_FILE",
    "NAME_CONTAINS_ILLEGAL_CHARS",
)

package sandbox.base.union



external fun nullFn(optionalParam: String?): Boolean

external fun undefinedFn(optionalParam: String?): Boolean
    