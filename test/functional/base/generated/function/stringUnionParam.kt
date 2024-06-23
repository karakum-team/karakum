// Generated by Karakum - do not modify it manually!

@file:JsModule("sandbox-base/function/stringUnionParam")
@file:Suppress(
    "NON_EXTERNAL_DECLARATION_IN_INAPPROPRIATE_FILE",
)

package sandbox.base.function

external fun getGPUInfo(infoType: GetGPUInfoInfoType): Any?

external fun getGPUInfo2(infoType: (GetGPUInfo2InfoType)): Any?

external fun getGPUInfo3(infoType: ((GetGPUInfo3InfoType))): Any?

external fun getGPUInfo4(infoType: (((GetGPUInfo4InfoType)))): Any?

external fun setVibrancy(type: SetVibrancyType?): Unit

sealed external interface GetGPUInfoInfoType {
companion object {
@seskar.js.JsValue("basic")
val basic: GetGPUInfoInfoType
@seskar.js.JsValue("complete")
val complete: GetGPUInfoInfoType
}
}
    


sealed external interface GetGPUInfo2InfoType {
companion object {
@seskar.js.JsValue("basic")
val basic: GetGPUInfo2InfoType
@seskar.js.JsValue("complete")
val complete: GetGPUInfo2InfoType
}
}
    


sealed external interface GetGPUInfo3InfoType {
companion object {
@seskar.js.JsValue("basic")
val basic: GetGPUInfo3InfoType
@seskar.js.JsValue("complete")
val complete: GetGPUInfo3InfoType
}
}
    


sealed external interface GetGPUInfo4InfoType {
companion object {
@seskar.js.JsValue("basic")
val basic: GetGPUInfo4InfoType
@seskar.js.JsValue("complete")
val complete: GetGPUInfo4InfoType
}
}
    


sealed external interface SetVibrancyType {
companion object {
@seskar.js.JsValue("light")
val light: SetVibrancyType
@seskar.js.JsValue("dark")
val dark: SetVibrancyType
}
}
    