
@file:JsModule("sandbox/interface/simple")
@file:JsNonModule

package sandbox.`interface`


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
    
    