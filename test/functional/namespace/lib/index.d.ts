declare module "package-namespace" {
    const packageNamespaceValue: number

    namespace NestedNamespace {
        const nestedNamespaceValue: number
    }
}

declare namespace ObjectNamespace {
    const objectNamespaceValue: number
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
