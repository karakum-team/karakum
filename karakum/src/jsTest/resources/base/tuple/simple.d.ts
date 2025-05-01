type SimpleEmptyTuple = []

type SimpleTuple1 = [string]

type SimpleTuple2 = [string, number]

type SimpleTuple3 = [string, number, boolean]

interface TupleWithRest<A extends unknown[]> {
    tuple: [string, number, ...A, boolean]
}

type TupleWithNames = [first: string, second: number, third: boolean]
