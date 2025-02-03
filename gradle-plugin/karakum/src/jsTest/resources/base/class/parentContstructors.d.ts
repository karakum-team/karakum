declare class GrandparentWithConstructor {
    constructor(param: number)
    constructor(param: string)
}

declare class ParentWithoutConstructor extends GrandparentWithConstructor {
}

declare class ChildWithoutConstructor extends ParentWithoutConstructor {
}

declare namespace NSWithParent {
    class ParentWithConstructor {
        constructor(param: number)
        constructor(param: boolean)
    }
}

declare class ChildWithoutConstructor2 extends NSWithParent.ParentWithConstructor {
}
