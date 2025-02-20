interface ExampleParentForInterface {
}

interface SecondExampleParentForInterface {
}

interface ExampleParentForClass {
}

interface ExampleInterfaceWithParent extends ExampleParentForInterface {
}

interface ExampleInterfaceWithParent extends SecondExampleParentForInterface {
}

interface ExampleClassWithParent extends ExampleParentForInterface {
}

declare class ExampleClassWithParent extends ExampleParentForClass {
}
