
@file:JsModule("sandbox/union/simple")
@file:JsNonModule

package sandbox.union


external interface A {
var brand: String
}
    

external interface B {
var brand: String
}
    

external interface C {
var brand: String
}
    
typealias AorBorC = Any /* A | B | C */
typealias AorB = Any /* A | B */
    