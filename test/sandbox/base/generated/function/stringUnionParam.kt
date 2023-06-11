
@file:JsModule("sandbox-base/function/stringUnionParam")
@file:Suppress(
    "NON_EXTERNAL_DECLARATION_IN_INAPPROPRIATE_FILE",
)

package sandbox.base.function



external fun getGPUInfo(infoType: GetGPUInfoInfoType): Any?

external fun getGPUInfo2(infoType: (GetGPUInfo2InfoType)): Any?
    
@Suppress(
    "NAME_CONTAINS_ILLEGAL_CHARS",
    "NESTED_CLASS_IN_EXTERNAL_INTERFACE",
)
@JsName("""(/*union*/{basic: 'basic', complete: 'complete'}/*union*/)""")
sealed external interface GetGPUInfoInfoType {
companion object {
val basic: GetGPUInfoInfoType
val complete: GetGPUInfoInfoType
}
}
    


@Suppress(
    "NAME_CONTAINS_ILLEGAL_CHARS",
    "NESTED_CLASS_IN_EXTERNAL_INTERFACE",
)
@JsName("""(/*union*/{basic: 'basic', complete: 'complete'}/*union*/)""")
sealed external interface GetGPUInfo2InfoType {
companion object {
val basic: GetGPUInfo2InfoType
val complete: GetGPUInfo2InfoType
}
}
    