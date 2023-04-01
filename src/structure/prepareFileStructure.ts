import path from "path";
import ts, {Node, SourceFile} from "typescript";
import {Configuration} from "../configuration/configuration";
import {generateOutputFileInfo} from "./generateOutputFileInfo";
import {NamespaceInfo} from "./namespace/collectNamespaceInfo";
import {TargetFileInfo} from "./createTargetFile";

interface FileStructureItem extends TargetFileInfo {
    nodes: ReadonlyArray<Node>,

    sourceFileName: string | undefined,
    namespaceName: string | undefined,
}

type FileStructure = FileStructureItem[]

interface TopLevelMatch {
    name: string,
    hasRuntime: boolean,
}

type TopLevelMatcher = (node: Node) => TopLevelMatch | null

const classMatcher: TopLevelMatcher = node => {
    if (ts.isClassDeclaration(node)) {
        const name = node.name?.text
        if (!name) return null

        return {
            name,
            hasRuntime: true,
        }
    }

    return null
}

const interfaceMatcher: TopLevelMatcher = node => {
    if (ts.isInterfaceDeclaration(node)) {
        const name = node.name.text;

        return {
            name,
            hasRuntime: false,
        }
    }

    return null
}

const typeAliasMatcher: TopLevelMatcher = node => {
    if (ts.isTypeAliasDeclaration(node)) {
        const name = node.name.text;

        return {
            name,
            hasRuntime: false,
        }
    }

    return null
}

const enumMatcher: TopLevelMatcher = node => {
    if (ts.isEnumDeclaration(node)) {
        const name = node.name.text;

        return {
            name,
            hasRuntime: true,
        }
    }

    return null
}

const functionMatcher: TopLevelMatcher = node => {
    if (ts.isFunctionDeclaration(node)) {
        const name = node.name?.text
        if (!name) return null

        return {
            name,
            hasRuntime: true,
        }
    }

    return null
}

const topLevelMatchers: TopLevelMatcher[] = [
    classMatcher,
    interfaceMatcher,
    typeAliasMatcher,
    enumMatcher,
    functionMatcher,
]


const topLevelMatcher: TopLevelMatcher = node => {
    for (const matcher of topLevelMatchers) {
        const result = matcher(node)
        if (result !== null) return result
    }

    return null
}

function normalizeFileStructure(fileStructure: FileStructure) {
    const result: Map<string, FileStructureItem> = new Map()

    for (const item of fileStructure) {
        const existingItem = result.get(item.outputFileName) ?? {
            outputFileName: item.outputFileName,
            packageName: item.packageName,
            moduleName: item.moduleName,
            qualifier: item.qualifier,
            sourceFileName: item.sourceFileName,
            namespaceName: item.namespaceName,
            nodes: [],
            hasRuntime: false
        }

        if (!result.has(item.outputFileName)) {
            result.set(item.outputFileName, existingItem)
        }

        result.set(item.outputFileName, {
            ...existingItem,
            nodes: [...existingItem.nodes, ...item.nodes],
            hasRuntime: existingItem.hasRuntime || item.hasRuntime
        })
    }

    return Array.from(result.values())
}


export function prepareFileStructure(
    sourceFileRoot: string,
    sourceFiles: SourceFile[],
    configuration: Configuration,
    namespaceInfo: NamespaceInfo,
): FileStructure {
    const granularity = configuration.granularity ?? "file"

    const files: FileStructure = sourceFiles
        .map(it => {
            const {outputFileName, packageName, moduleName} = generateOutputFileInfo(
                sourceFileRoot,
                it.fileName,
                configuration,
            )

            return {
                outputFileName,
                packageName,
                moduleName,
                qualifier: undefined,
                hasRuntime: true,

                nodes: it.statements,

                sourceFileName: it.fileName,
                namespaceName: undefined
            };
        })

    const namespaces: FileStructure = namespaceInfo
        .filter(it => it.strategy === "package")
        .map(it => {
            return {
                outputFileName: it.outputFileName,
                packageName: it.packageName,
                moduleName: it.moduleName,
                qualifier: it.qualifier,
                hasRuntime: true,

                nodes: it.nodes,

                sourceFileName: undefined,
                namespaceName: it.name,
            }
        })

    const fileStructure = [...files, ...namespaces]

    if (granularity === "file") {
        return fileStructure
    }

    if (granularity === "top-level") {
        return normalizeFileStructure(
            fileStructure.flatMap(it => {
                const outputDirName = path.dirname(it.outputFileName)

                const result: FileStructure = []

                for (const node of it.nodes) {
                    let outputFileName = it.outputFileName
                    let hasRuntime = true

                    const topLevelMatch = topLevelMatcher(node)

                    if (topLevelMatch !== null) {
                        outputFileName = path.join(outputDirName, `${topLevelMatch.name}.kt`)
                        hasRuntime = topLevelMatch.hasRuntime
                    }

                    result.push({
                        ...it,

                        outputFileName,
                        hasRuntime,
                        nodes: [node],
                    })
                }

                return result
            })
        )
    }

    throw new Error(`Unknown granularity type: ${granularity}`)
}
