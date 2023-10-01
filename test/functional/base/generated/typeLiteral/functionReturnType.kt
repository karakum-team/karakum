
@file:JsModule("sandbox-base/typeLiteral/functionReturnType")
@file:Suppress(
    "NON_EXTERNAL_DECLARATION_IN_INAPPROPRIATE_FILE",
)

package sandbox.base.typeLiteral



external fun useRevalidator(): UseRevalidatorResult


external class MyClass {

        
companion object {
fun getDerivedStateFromProps(): MyClassGetDerivedStateFromPropsResult
}
        
}
    


external interface MyInterface {
fun getDerivedStateFromProps(): MyInterfaceGetDerivedStateFromPropsResult
}
    
    
external interface UseRevalidatorResult {
var revalidate: () -> Unit
}
    


external interface MyClassGetDerivedStateFromPropsResult {
var error: Any?
var location: Location
}
    


external interface MyInterfaceGetDerivedStateFromPropsResult {
var error: Any?
var location: Location
}
    