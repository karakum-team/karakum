import ts, {SourceFile, Statement} from "typescript";
import {Configuration} from "../../configuration/configuration";
import {traverse} from "../../utils/traverse";
import {createNamespaceInfoItem, NamespaceInfoItem} from "./createNamespaceInfoItem";
import {InputStructureItem} from "../structure";
import {extractModuleName} from "../module/extractModuleName";

interface InputNamespaceInfoItem extends NamespaceInfoItem, InputStructureItem {
}

export type NamespaceInfo = InputNamespaceInfoItem[]

export function collectNamespaceInfo(
    sourceFileRoots: string[],
    sourceFiles: SourceFile[],
    configuration: Configuration,
): NamespaceInfo {
    const result: NamespaceInfo = []

    sourceFiles.forEach(sourceFile => {
        const defaultModuleName = extractModuleName(sourceFileRoots, sourceFile.fileName, configuration)

        traverse(sourceFile, node => {
            if (
                ts.isModuleDeclaration(node)
            ) {
                const item = createNamespaceInfoItem(node, defaultModuleName, configuration)

                let statements: Statement[] = []

                if (node.body && ts.isModuleBlock(node.body)) {
                    statements = node.body.statements
                        .filter(statement => !ts.isModuleDeclaration(statement))
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
