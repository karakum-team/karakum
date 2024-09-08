import {Node} from "typescript";

export interface StructureItem {
    fileName: string
    package: string[]
    moduleName: string
    qualifier: string | undefined
    hasRuntime: boolean
    imports: string[]
}

export interface InputStructureItem extends StructureItem {
    nodes: ReadonlyArray<Node>
    meta: {
        type: string
        name: string
    }
}
