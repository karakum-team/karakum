import {ModuleDeclaration} from "typescript";
import {StructureItem} from "../structure.js";

export interface DerivedDeclaration {
    sourceFileName: string,
    namespace: ModuleDeclaration | undefined,
    fileName: string,
    body: string,
}

export interface DerivedDeclarationStructureItem extends StructureItem {
    body: string,
}
