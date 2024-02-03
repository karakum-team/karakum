declare module "import-provider" {
    export const someValue: string
}

declare module "other-import-provider" {
    export const x: string
    export const y: string
    export const z: string
    export default x
}

declare module "import-consumer" {
    import {someValue} from "import-provider"

    import {x, y as myY} from "other-import-provider"
    import myX from "other-import-provider"
    import * as xyz from "other-import-provider"

    import {ignored, other as otherIgnored} from "ignored-import"
}
