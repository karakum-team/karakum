interface InterfaceWithVoidSignatures {
    (): void
    new (): void
    someMethod(): void
}

type TypeWithVoidSignatures = {
    [key: string]: void
}

class ClassWithVoidDeclarations {
    someMethod(): void
    get someValue(): void
}

function functionWithVoidReturnType(): void

function functionWithPromiseVoidReturnType(): Promise<void>

type functionTypeWithVoidReturnType = () => void

type constructorTypeWithVoidReturnType = new () => void
