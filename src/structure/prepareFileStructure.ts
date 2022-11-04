import path from "path";
import ts, {Node, Program} from "typescript";
import {Configuration} from "../configuration/configuration";
import {generateTargetFileName} from "../utils/fileName";

interface FileStructureItem {
    sourceFileName: string,
    targetFileName: string,
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
        const existingItem = result.get(item.targetFileName) ?? {
            sourceFileName: item.sourceFileName,
            targetFileName: item.targetFileName,
            nodes: []
        }

        if (!result.has(item.targetFileName)) {
            result.set(item.targetFileName, existingItem)
        }

        result.set(item.targetFileName, {
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
    const output = configuration.output
    const packageNameMapper = configuration.packageNameMapper

    if (granularity === "file") {
        return sourceFiles.map(it => {
            const targetFileName = generateTargetFileName(sourceFileRoot, output, it.fileName, packageNameMapper)

            return ({
                sourceFileName: it.fileName,
                targetFileName,
                nodes: it.statements,
            });
        })
    }

    if (granularity === "top-level") {
        return normalizeFileStructure(
            sourceFiles.flatMap(it => {
                const targetFileName = generateTargetFileName(sourceFileRoot, output, it.fileName, packageNameMapper)
                const targetDirName = path.dirname(targetFileName)

                const result: FileStructure = []

                for (const statement of it.statements) {
                    let fileName = targetFileName
                    const topLevelName = topLevelMatcher(statement)

                    if (topLevelName !== null) {
                        fileName = `${targetDirName}/${topLevelName}.kt`
                    }

                    result.push({
                        sourceFileName: it.fileName,
                        targetFileName: fileName,
                        nodes: [statement]
                    })
                }

                return result
            })
        )
    }

    throw new Error(`Unknown granularity type: ${granularity}`)
}
