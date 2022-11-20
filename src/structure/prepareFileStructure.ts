import path from "path";
import ts, {Node, Program} from "typescript";
import {Configuration} from "../configuration/configuration";
import {generateOutputFileName} from "../utils/fileName";

interface FileStructureItem {
    sourceFileName: string,
    outputFileName: string,
    nodes: ReadonlyArray<Node>
}

type FileStructure = FileStructureItem[]

type TopLevelMatcher = (node: Node) => string | null

const classMatcher: TopLevelMatcher = node => {
    if (ts.isClassDeclaration(node)) {
        return node.name?.text ?? null
    }

    return null
}

const interfaceMatcher: TopLevelMatcher = node => {
    if (ts.isInterfaceDeclaration(node)) {
        return node.name.text
    }

    return null
}

const typeAliasMatcher: TopLevelMatcher = node => {
    if (ts.isTypeAliasDeclaration(node)) {
        return node.name.text
    }

    return null
}

const enumMatcher: TopLevelMatcher = node => {
    if (ts.isEnumDeclaration(node)) {
        return node.name.text
    }

    return null
}

const functionMatcher: TopLevelMatcher = node => {
    if (ts.isFunctionDeclaration(node)) {
        return node.name?.text ?? null
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
            nodes: []
        }

        if (!result.has(item.outputFileName)) {
            result.set(item.outputFileName, existingItem)
        }

        result.set(item.outputFileName, {
            ...existingItem,
            nodes: [...existingItem.nodes, ...item.nodes]
        })
    }

    return Array.from(result.values())
}


export function prepareFileStructure(
    sourceFileRoot: string,
    program: Program,
    configuration: Configuration
): FileStructure {
    const sourceFiles = program.getSourceFiles()
    const granularity = configuration.granularity ?? "file"
    const packageNameMapper = configuration.packageNameMapper

    if (granularity === "file") {
        return sourceFiles.map(it => {
            const outputFileName = generateOutputFileName(sourceFileRoot, it.fileName, packageNameMapper)

            return ({
                sourceFileName: it.fileName,
                outputFileName,
                nodes: it.statements,
            });
        })
    }

    if (granularity === "top-level") {
        return normalizeFileStructure(
            sourceFiles.flatMap(it => {
                const outputFileName = generateOutputFileName(sourceFileRoot, it.fileName, packageNameMapper)
                const outputDirName = path.dirname(outputFileName)

                const result: FileStructure = []

                for (const statement of it.statements) {
                    let currentOutputFileName = outputFileName

                    const topLevelName = topLevelMatcher(statement)

                    if (topLevelName !== null) {
                        currentOutputFileName = `${outputDirName}/${topLevelName}.kt`
                    }

                    result.push({
                        sourceFileName: it.fileName,
                        outputFileName: currentOutputFileName,
                        nodes: [statement]
                    })
                }

                return result
            })
        )
    }

    throw new Error(`Unknown granularity type: ${granularity}`)
}
