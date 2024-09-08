import {SourceFile} from "typescript";
import {Configuration} from "../../configuration/configuration.js";
import {InputStructureItem} from "../structure.js";
import {createSourceFileInfoItem, SourceFileInfoItem} from "./createSourceFileInfoItem.js";
import {ImportInfo} from "../import/collectImportInfo.js";

interface InputSourceFileInfoItem extends SourceFileInfoItem, InputStructureItem {
}

export type SourceFileInfo = InputSourceFileInfoItem[]

export function collectSourceFileInfo(
    sourceFiles: SourceFile[],
    importInfo: ImportInfo,
    configuration: Configuration,
): SourceFileInfo {
    return sourceFiles.map(sourceFile => {
        const imports = importInfo.get(sourceFile) ?? []

        const item = createSourceFileInfoItem(
            sourceFile.fileName,
            imports,
            configuration,
        )

        const nodes = sourceFile.statements

        return {
            ...item,
            nodes,
            meta: {
                type: "Source File",
                name: sourceFile.fileName
            }
        }
    })
}
