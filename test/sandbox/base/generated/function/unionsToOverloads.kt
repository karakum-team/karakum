
@file:JsModule("sandbox-base/function/unionsToOverloads")
@file:Suppress(
    "NON_EXTERNAL_DECLARATION_IN_INAPPROPRIATE_FILE",
)

package sandbox.base.function




external interface A {
var brand: String /* "one" */
}
    


external interface B {
var brand: String /* "two" */
}
    


external interface C {
var brand: String /* "three" */
}
    

external fun exampleFn(firstParam: A, secondParam: B, thirdParam: A): Boolean

external fun exampleFn(firstParam: A, secondParam: B, thirdParam: B): Boolean

external fun exampleFn(firstParam: A, secondParam: B, thirdParam: C): Boolean

external fun exampleFn(firstParam: A, secondParam: C, thirdParam: A): Boolean

external fun exampleFn(firstParam: A, secondParam: C, thirdParam: B): Boolean

external fun exampleFn(firstParam: A, secondParam: C, thirdParam: C): Boolean

external fun exampleFn(firstParam: B, secondParam: B, thirdParam: A): Boolean

external fun exampleFn(firstParam: B, secondParam: B, thirdParam: B): Boolean

external fun exampleFn(firstParam: B, secondParam: B, thirdParam: C): Boolean

external fun exampleFn(firstParam: B, secondParam: C, thirdParam: A): Boolean

external fun exampleFn(firstParam: B, secondParam: C, thirdParam: B): Boolean

external fun exampleFn(firstParam: B, secondParam: C, thirdParam: C): Boolean
    