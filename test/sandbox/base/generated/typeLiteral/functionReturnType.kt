
@file:JsModule("sandbox-base/typeLiteral/functionReturnType")
@file:JsNonModule

@file:Suppress(
    "NON_EXTERNAL_DECLARATION_IN_INAPPROPRIATE_FILE",
    "NAME_CONTAINS_ILLEGAL_CHARS",
)

package sandbox.base.typeLiteral



external fun useRevalidator(): UseRevalidatorResult


external class MyClass  {
fun getDerivedStateFromProps(): MyClassGetDerivedStateFromPropsResult
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
        