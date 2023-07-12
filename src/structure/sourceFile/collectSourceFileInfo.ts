import {SourceFile} from "typescript";
import {Configuration} from "../../configuration/configuration.js";
import {InputStructureItem} from "../structure.js";
import {createSourceFileInfoItem, SourceFileInfoItem} from "./createSourceFileInfoItem.js";

interface InputSourceFileInfoItem extends SourceFileInfoItem, InputStructureItem {
}

export type SourceFileInfo = InputSourceFileInfoItem[]

export function collectSourceFileInfo(
    sourceFiles: SourceFile[],
    configuration: Configuration,
): SourceFileInfo {
    return sourceFiles.map(sourceFile => {
        const item = createSourceFileInfoItem(
            sourceFile.fileName,
            configuration,
        )

        const statements = sourceFile.statements

        return {
            ...item,
            statements,
            meta: {
                type: "Source File",
                name: sourceFile.fileName
            }
        }
    })
}
