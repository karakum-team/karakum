const myConst1: string

const myConst2: number

/** my favorite constant */
const myConstWithComment: number

function myFunction1(firstParam: string, secondParam: number): void

function myFunction1(firstParam: boolean, secondParam: number): void

function myFunction2(firstParam: string, secondParam: number): void

function myFunction2(firstParam: boolean, secondParam: number): void

// to provoke a case-only file name conflict with MyClass
function myClass(firstParam: string, secondParam: number): void

class MyClass {
    field: boolean
}

interface MyInterface {
    field: boolean
}

type MyTypeAlias = string

enum MyEnum {
    FIRST,
    SECOND,
}
