
@file:JsModule("sandbox/union/simple")
@file:JsNonModule

package sandbox.union


external interface A {
var brand: one
}
    

external interface B {
var brand: two
}
    

external interface C {
var brand: three
}
    
typealias AorBorC = Any /* A | B | C */
typealias AorB = Any /* A | B */
    