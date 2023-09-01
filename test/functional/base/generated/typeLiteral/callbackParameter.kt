
@file:JsModule("sandbox-base/typeLiteral/callbackParameter")
@file:Suppress(
    "NON_EXTERNAL_DECLARATION_IN_INAPPROPRIATE_FILE",
)

package sandbox.base.typeLiteral




external class MyClass {
var conflictHandler: ((conflictType: MyClassConflictHandlerConflictType) -> Boolean)?
fun method(cb: (options: MyClassMethodCbOptions) -> Unit): String
}
    


external interface MyInterface {
var conflictHandler: ((conflictType: MyInterfaceConflictHandlerConflictType) -> Boolean)?
fun method(cb: (options: MyInterfaceMethodCbOptions) -> Unit): String
}
    
    
external interface MyClassConflictHandlerConflictType {
var third: Boolean
var fourth: Array<String>
}
        


external interface MyClassMethodCbOptions {
var third: Boolean
var fourth: Array<String>
}
        


external interface MyInterfaceConflictHandlerConflictType {
var first: Boolean
var second: Array<String>
}
        


external interface MyInterfaceMethodCbOptions {
var first: Boolean
var second: Array<String>
}
        