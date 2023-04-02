import {Statement} from "typescript";

export interface StructureItem {
    fileName: string
    package: string[]
    moduleName: string
    qualifier: string | undefined
    hasRuntime: boolean
}

export interface InputStructureItem extends StructureItem {
    statements: ReadonlyArray<Statement>
    meta: {
        type: string
        name: string
    }
}

export interface OutputStructureItem extends StructureItem {
    body: string
}
