
@file:JsModule("sandbox-base/interface/simple")
@file:Suppress(
    "NON_EXTERNAL_DECLARATION_IN_INAPPROPRIATE_FILE",
)

package sandbox.base.`interface`




external interface ExampleInterface {
var firstField: String
var secondField: Double
}
    


external interface SimpleInterface {
var firstField: String
var secondField: Double
var thirdField: ExampleInterface
fun firstMethod(firstParam: String, secondParam: Double): Unit
fun secondMethod(firstParam: String, secondParam: Double): Boolean
}
    
    