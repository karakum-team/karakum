
@file:JsModule("sandbox-base/union/stringEnum")
@file:Suppress(
    "NON_EXTERNAL_DECLARATION_IN_INAPPROPRIATE_FILE",
)

package sandbox.base.union




@Suppress(
    "NAME_CONTAINS_ILLEGAL_CHARS",
    "NESTED_CLASS_IN_EXTERNAL_INTERFACE",
)
@JsName("""(/*union*/{applicationXWwwFormUrlencoded: 'application/x-www-form-urlencoded', multipartFormData: 'multipart/form-data'}/*union*/)""")
sealed external interface FormEncType {
companion object {
val applicationXWwwFormUrlencoded: FormEncType
val multipartFormData: FormEncType
}
}
        
    