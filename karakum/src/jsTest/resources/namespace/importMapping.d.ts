declare module "import-provider" {
    export interface SomeValue {
    }
}

declare module "other-import-provider" {
    export interface X {
    }

    export interface Y {
    }

    export interface Z {
    }

    export default X
}

declare module "ignored-import" {
    export interface Ignored {
    }

    export interface other {
    }
}

declare module "import-consumer" {
    import {SomeValue} from "import-provider"

    import {X, Y as MyY} from "other-import-provider"
    import MyX from "other-import-provider"
    import * as xyz from "other-import-provider"

    import {Ignored, other as OtherIgnored} from "ignored-import"

    const someValue: SomeValue

    const x: X
    const myX: MyX
    const myY: MyY
    const myXyz: typeof xyz

    const ignored: Ignored
    const otherIgnored: OtherIgnored

    namespace Nested {
        import {Z} from "other-import-provider"

        const x: X
        const z: Z
    }
}
