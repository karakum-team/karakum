
@file:JsModule("sandbox-base/declarationMerging/interfaces")
@file:Suppress(
    "NON_EXTERNAL_DECLARATION_IN_INAPPROPRIATE_FILE",
)

package sandbox.base.declarationMerging




external interface Example {

@Suppress("DEPRECATION")
@nativeInvoke
operator fun  invoke(param: String): Unit
            
var a: Double
var b: String
}
    


    