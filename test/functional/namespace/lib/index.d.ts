declare module "package-namespace" {
    const packageNamespaceValue: number

    function testFn(param: string): string
    function testFn(param: number): string

    interface TestInterface {
        testFn: typeof testFn
    }

    namespace NestedNamespace {
        const nestedNamespaceValue: number
    }

    namespace ObjectNamespace {
        const objectNamespaceValue: number
    }
}

declare namespace ObjectNamespace {
    const objectNamespaceValue: number

    namespace InnerObjectNamespace {
        const innerObjectNamespaceValue: number
    }
}

declare namespace IgnoreNamespace {
    const topLevelValue: number
}

declare module "will-be-mapped" {
    const mappedValue: number

    namespace AndThis {
        const nestedMappedValue: number
    }
}
