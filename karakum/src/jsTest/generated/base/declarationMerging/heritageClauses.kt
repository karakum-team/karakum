// Automatically generated - do not modify!

@file:JsModule("sandbox-base/declarationMerging/heritageClauses")

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
