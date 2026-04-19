interface PlainObject1 {
    a: string
    b: boolean
}

interface NotPlainObject1 {
    a: string
    b(): boolean
}

interface PlainObject2 extends PlainObject1 {
    c: string
    d: boolean
}

interface NotPlainObject2 extends NotPlainObject1 {
    c: string
    d: boolean
}

type PlainObject3 = PlainObject1 & { c: string }

type NotPlainObject3 = NotPlainObject1 & { c: string }

type PlainObject4 = PlainObject3

type NotPlainObject4 = NotPlainObject3

interface PlainObject5 extends PlainObject4 {
    e: string
}

interface NotPlainObject5 extends NotPlainObject4 {
    e: string
}

function createPlainObject1(
    config: {
        a: string
        b: boolean
    }
): PlainObject1

function createNotPlainObject1(
    config: {
        a: string
        b(): boolean
    }
): NotPlainObject1

function createPlainObject2(
    config: {
        a: string
        b: boolean
    } & {
        c: string
        d: boolean
    }
): PlainObject2

function createNotPlainObject2(
    config: {
        a: string
        b(): boolean
    } & {
        c: string
        d: boolean
    }
): NotPlainObject2
