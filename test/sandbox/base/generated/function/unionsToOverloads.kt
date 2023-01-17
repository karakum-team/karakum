
@file:JsModule("sandbox-base/function/unionsToOverloads")
@file:JsNonModule

@file:Suppress(
    "NON_EXTERNAL_DECLARATION_IN_INAPPROPRIATE_FILE",
    "NAME_CONTAINS_ILLEGAL_CHARS",
)

package sandbox.base.function




external interface A {
var brand: String
}
    


external interface B {
var brand: String
}
    


external interface C {
var brand: String
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
    