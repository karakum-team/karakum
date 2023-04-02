import {SourceFile} from "typescript";
import {Configuration} from "../../configuration/configuration";
import {InputStructureItem} from "../structure";
import {createSourceFileInfoItem, SourceFileInfoItem} from "./createSourceFileInfoItem";

interface InputSourceFileInfoItem extends SourceFileInfoItem, InputStructureItem {
}

export type SourceFileInfo = InputSourceFileInfoItem[]

export function collectSourceFileInfo(
    sourceFileRoot: string,
    sourceFiles: SourceFile[],
    configuration: Configuration,
): SourceFileInfo {
    return sourceFiles.map(sourceFile => {
        const item = createSourceFileInfoItem(
            sourceFileRoot,
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
