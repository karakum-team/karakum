
@file:JsModule("sandbox-base/typeLiteral/callbackParameter")
@file:Suppress(
    "NON_EXTERNAL_DECLARATION_IN_INAPPROPRIATE_FILE",
)

package sandbox.base.typeLiteral




external class MyClass {
var conflictHandler: ((conflictType: MyClassConflictTypeConflictType) -> Boolean)?
fun method(cb: (options: MyClassMethodCbOptions) -> Unit): String
}
    


external interface MyInterface {
var conflictHandler: ((conflictType: MyInterfaceConflictTypeConflictType) -> Boolean)?
fun method(cb: (options: MyInterfaceMethodCbOptions) -> Unit): String
}
    
    
external interface MyClassConflictTypeConflictType {
var third: Boolean
var fourth: Array<String>
}
        


external interface MyClassMethodCbOptions {
var third: Boolean
var fourth: Array<String>
}
        


external interface MyInterfaceConflictTypeConflictType {
var first: Boolean
var second: Array<String>
}
        


external interface MyInterfaceMethodCbOptions {
var first: Boolean
var second: Array<String>
}
        