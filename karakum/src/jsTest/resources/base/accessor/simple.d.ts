declare class Simple {
    get property(): string

    set property(value: string)
}

declare class DifferentTypes {
    get property(): string

    set property(value: string | undefined)
}

declare class Readonly {
    get property(): string
}

declare class Setteronly {
    set property(value: string)
}

interface Split {
    get property(): string
}

interface Split {
    set property(value: string)
}

type Anonymous = {
    get property(): string

    set property(value: string)
}
