
@file:JsModule("sandbox-base/class/parentContstructors")
@file:Suppress(
    "NON_EXTERNAL_DECLARATION_IN_INAPPROPRIATE_FILE",
)

package sandbox.base.`class`




external class GrandparentWithConstructor {
constructor (param: Double)
constructor (param: String)
}
    


external class ParentWithoutConstructor : GrandparentWithConstructor {
constructor (param: Double)
constructor (param: String)
}
    


external class ChildWithoutConstructor : ParentWithoutConstructor {
constructor (param: Double)
constructor (param: String)
}
    


external object NSWithParent {

external class ParentWithConstructor {
constructor (param: Double)
constructor (param: Boolean)
}
    
}
        


external class ChildWithoutConstructor2 : NSWithParent.ParentWithConstructor {
constructor (param: Double)
constructor (param: Boolean)
}
    
    