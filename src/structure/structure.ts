import {Statement} from "typescript";

export interface StructureItem {
    fileName: string
    package: string[]
    moduleName: string
    qualifier: string | undefined
    hasRuntime: boolean
    imports: string[]
}

export interface InputStructureItem extends StructureItem {
    statements: ReadonlyArray<Statement>
    meta: {
        type: string
        name: string
    }
}
