import ts, {SourceFile, Statement} from "typescript";
import {Configuration} from "../../configuration/configuration";
import {traverse} from "../../utils/traverse";
import {matchNamespaceStrategy, NamespaceMatch} from "./matchNamespaceStrategy";
import {generateModuleName} from "../generateModuleName";

export interface NamespaceInfoItem extends NamespaceMatch {
    nodes: ReadonlyArray<Statement>,
}

export type NamespaceInfo = NamespaceInfoItem[]

function normalizeNamespaceInfo(namespaceInfo: NamespaceInfo) {
    const result: Map<string, NamespaceInfoItem> = new Map()

    for (const item of namespaceInfo) {
        const existingItem = result.get(item.name) ?? {
            name: item.name,
            packageName: item.packageName,
            outputFileName: item.outputFileName,
            moduleName: item.moduleName,
            qualifier: item.qualifier,
            strategy: item.strategy,
            nodes: [],
        }

        if (!result.has(item.name)) {
            result.set(item.name, existingItem)
        }

        result.set(item.name, {
            ...existingItem,
            nodes: [...existingItem.nodes, ...item.nodes],
        })
    }

    return Array.from(result.values())
}

export function collectNamespaceInfo(
    sourceFileRoot: string,
    sourceFiles: SourceFile[],
    configuration: Configuration,
): NamespaceInfo {
    const result: NamespaceInfo = []

    sourceFiles.forEach(sourceFile => {
        const defaultModuleName = generateModuleName(sourceFileRoot, sourceFile.fileName, configuration)

        traverse(sourceFile, node => {
            if (
                ts.isModuleDeclaration(node)
            ) {
                const namespaceMatch = matchNamespaceStrategy(node, configuration, defaultModuleName)

                let statements: Statement[] = []

                if (node.body && ts.isModuleBlock(node.body)) {
                    statements = node.body.statements
                        .filter(statement => !ts.isModuleDeclaration(statement))
                }

                result.push({
                    ...namespaceMatch,
                    nodes: statements
                })
            }
        })
    })

    return normalizeNamespaceInfo(result)
}
