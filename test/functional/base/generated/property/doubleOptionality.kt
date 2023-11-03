
@file:JsModule("sandbox-base/property/doubleOptionality")
@file:Suppress(
    "NON_EXTERNAL_DECLARATION_IN_INAPPROPRIATE_FILE",
)

package sandbox.base.property



typealias SomeNullableType = String?

typealias SomeUnionNullableType = Any? /* number | string | null */


external interface BaseAgnosticBaseRouteObject {
var params: String?
var params2: String?
var params3: SomeNullableType
var params4: SomeUnionNullableType
}
    


external interface AgnosticBaseRouteObject {
var handle1: Any?
var handle2: Any?
var handle3: String?
var handle4: Nothing?
var handle5: Nothing?
var handle6: Any? /* type isn't declared */
var handle7: String?
var handle8: String?
var handle9: SomeNullableType
var handle10: SomeNullableType
var handle11: SomeUnionNullableType
var handle12: SomeUnionNullableType
}
    


external class AgnosticBaseRouteClass {
var handle1: Any?
var handle2: Any?
var handle3: String?
var handle4: Nothing?
var handle5: Nothing?
var handle6: Any? /* type isn't declared */
var handle7: String?
var handle8: String?
var handle9: SomeNullableType
var handle10: SomeNullableType
var handle11: SomeUnionNullableType
var handle12: SomeUnionNullableType
}
    
    