import ts, {SourceFile, Statement} from "typescript";
import {Configuration} from "../../configuration/configuration.js";
import {traverse} from "../../utils/traverse.js";
import {createNamespaceInfoItem, NamespaceInfoItem} from "./createNamespaceInfoItem.js";
import {InputStructureItem} from "../structure.js";
import {ImportInfo} from "../import/collectImportInfo.js";

interface InputNamespaceInfoItem extends NamespaceInfoItem, InputStructureItem {
}

export type NamespaceInfo = InputNamespaceInfoItem[]

export function collectNamespaceInfo(
    sourceFiles: SourceFile[],
    importInfo: ImportInfo,
    configuration: Configuration,
): NamespaceInfo {
    const result: NamespaceInfo = []

    sourceFiles.forEach(sourceFile => {
        traverse(sourceFile, node => {
            if (ts.isModuleDeclaration(node)) {
                const imports = importInfo.get(node) ?? []

                const item = createNamespaceInfoItem(
                    node,
                    sourceFile.fileName,
                    imports,
                    configuration,
                )

                let statements: Statement[] = []

                if (node.body && ts.isModuleBlock(node.body)) {
                    statements = Array.from(node.body.statements)
                }

                result.push({
                    ...item,
                    statements,
                    meta: {
                        type: "Namespace",
                        name: item.name
                    }
                })
            }
        })
    })

    return result
}
