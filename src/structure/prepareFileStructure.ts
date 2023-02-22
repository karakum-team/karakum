import path from "path";
import ts, {Node, SourceFile} from "typescript";
import {Configuration} from "../configuration/configuration";
import {generateOutputFileInfo, OutputFileInfo} from "./generateOutputFileInfo";

interface FileStructureItem extends OutputFileInfo {
    sourceFileName: string,
    nodes: ReadonlyArray<Node>,
    hasRuntime: boolean,
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
            sourceFileName: item.sourceFileName,
            outputFileName: item.outputFileName,
            packageName: item.packageName,
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
    configuration: Configuration
): FileStructure {
    const granularity = configuration.granularity ?? "file"

    if (granularity === "file") {
        return sourceFiles.map(it => {
            const {outputFileName, packageName} = generateOutputFileInfo(
                sourceFileRoot,
                it.fileName,
                configuration,
            )

            return ({
                sourceFileName: it.fileName,
                outputFileName,
                packageName,
                nodes: it.statements,
                hasRuntime: true,
            });
        })
    }

    if (granularity === "top-level") {
        return normalizeFileStructure(
            sourceFiles.flatMap(it => {
                const {outputFileName, packageName} = generateOutputFileInfo(
                    sourceFileRoot,
                    it.fileName,
                    configuration,
                );
                const outputDirName = path.dirname(outputFileName)

                const result: FileStructure = []

                for (const statement of it.statements) {
                    let currentOutputFileName = outputFileName
                    let hasRuntime = true

                    const topLevelMatch = topLevelMatcher(statement)

                    if (topLevelMatch !== null) {
                        currentOutputFileName = `${outputDirName}/${topLevelMatch.name}.kt`
                        hasRuntime = topLevelMatch.hasRuntime
                    }

                    result.push({
                        sourceFileName: it.fileName,
                        outputFileName: currentOutputFileName,
                        packageName,
                        nodes: [statement],
                        hasRuntime,
                    })
                }

                return result
            })
        )
    }

    throw new Error(`Unknown granularity type: ${granularity}`)
}
