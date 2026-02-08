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

    /** my favorite constant */
    const objectNamespaceValueWithComment: number

    type objectNamespaceUnion = "0" | "1";

    interface ObjectNamespaceInterface {
        someProperty: "3" | "4"
    }

    namespace InnerObjectNamespace {
        const innerObjectNamespaceValue: number

        type innerObjectNamespaceUnion = "5" | "6";
    }
}

declare namespace IgnoreNamespace {
    const topLevelValue: number

    /** my favorite constant */
    const topLevelValueWithComment: number
}

declare module "will-be-mapped" {
    const mappedValue: number

    namespace AndThis {
        const nestedMappedValue: number
    }
}

declare module "will-be-erased" {
    const topLevelConst: number
}
