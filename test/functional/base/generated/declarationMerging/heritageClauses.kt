// Generated by Karakum - do not modify it manually!

@file:JsModule("sandbox-base/declarationMerging/heritageClauses")
@file:Suppress(
    "NON_EXTERNAL_DECLARATION_IN_INAPPROPRIATE_FILE",
)

package sandbox.base.declarationMerging


external interface ExampleParentForInterface {

}
    


external interface SecondExampleParentForInterface {

}
    


external interface ExampleParentForClass {

}
    


external interface ExampleInterfaceWithParent : ExampleParentForInterface, SecondExampleParentForInterface {

}
    






external class ExampleClassWithParent : ExampleParentForClass, ExampleParentForInterface {

}
