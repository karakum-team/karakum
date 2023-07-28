interface Example {
    (param: string): void

    a: number
}

interface Example {
    (param1: string): void

    a: number
    b: string
}

interface ExampleWithOverloads {
    method(param: string): void
    method(param: number): void

    method2(param: string): void
    method2(param: number): void
}
