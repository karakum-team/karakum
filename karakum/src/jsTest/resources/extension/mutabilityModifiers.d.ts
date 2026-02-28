export var defaultMutability: string
export var customMutability: string

interface MutableContainerInterface {
    defaultMutability: string
    customMutability: string

    get defaultMutability2(): string
    set defaultMutability2(value: string)

    get customMutability2(): string
    set customMutability2(value: string)
}

interface MutableContainerClass {
    defaultMutability: string
    customMutability: string
}
