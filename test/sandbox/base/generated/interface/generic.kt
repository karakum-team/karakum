
@file:JsModule("sandbox-base/interface/generic")
@file:JsNonModule

@file:Suppress(
    "NON_EXTERNAL_DECLARATION_IN_INAPPROPRIATE_FILE",
    "NAME_CONTAINS_ILLEGAL_CHARS",
)

package sandbox.base.`interface`




external interface ExampleInterface {
var firstField: String
var secondField: Double
}
    


external interface GenericInterface<T, U : ExampleInterface>  {
var firstField: T
var secondField: Double
fun firstMethod(firstParam: String, secondParam: U): Unit
}
    


external interface InterfaceWithGenericMethod {
fun <T, U : ExampleInterface> genericMethod(firstParam: T, secondParam: Double): Unit
}
    


external interface ChildGenericInterface<T>  : GenericInterface<T, ExampleInterface> {

}
    
    