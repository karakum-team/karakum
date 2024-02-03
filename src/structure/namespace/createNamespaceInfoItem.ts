import ts, {ModuleDeclaration} from "typescript";
import {Configuration, NamespaceStrategy} from "../../configuration/configuration.js";
import {StructureItem} from "../structure.js";
import {moduleNameToPackage} from "../module/moduleNameToPackage.js";
import {extractModuleName} from "../module/extractModuleName.js";

export interface NamespaceInfoItem extends StructureItem {
    name: string
    strategy: NamespaceStrategy
}

interface NamespaceNameChunk {
    detailedName: string
    simpleName: string
    package: string[]
    isAmbient: boolean
}

export const defaultNamespaceStrategy: NamespaceStrategy = "object"

export function extractNamespaceName(
    namespace: ModuleDeclaration,
    suffix: NamespaceNameChunk[] = []
): NamespaceNameChunk[] {
    const simpleName = namespace.name.text
    const detailedName = ts.isIdentifier(namespace.name)
        ? simpleName
        : `"${simpleName}"`
    const packageChunks = moduleNameToPackage(simpleName).map(it => it.toLowerCase())
    const isAmbient = ts.isStringLiteral(namespace.name)

    const nameChunk = {
        detailedName,
        simpleName,
        package: packageChunks,
        isAmbient,
    }

    const name = [nameChunk, ...suffix]

    if (ts.isModuleDeclaration(namespace.parent)) {
        return extractNamespaceName(namespace.parent, name)
    } else if (ts.isModuleBlock(namespace.parent)) {
        return extractNamespaceName(namespace.parent.parent, name)
    } else {
        return name
    }
}

export function createNamespaceInfoItem(
    namespace: ModuleDeclaration,
    sourceFileName: string,
    imports: string[],
    configuration: Configuration,
): NamespaceInfoItem {
    const {namespaceStrategy} = configuration

    const defaultModuleName = extractModuleName(sourceFileName, configuration)
    const name = extractNamespaceName(namespace)

    const detailedName = name.map(it => it.detailedName).join(".")
    const simpleName = name.map(it => it.simpleName).join(".")

    const fileName = "namespace.kt"
    const packageChunks = name.flatMap(it => it.package)
    const hasRuntime = true

    let moduleName = defaultModuleName
    let qualifier: string | undefined = simpleName

    const [firstChunk, ...restChunks] = name

    if (firstChunk.isAmbient) {
        moduleName = firstChunk.simpleName

        if (restChunks.length > 0) {
            qualifier = restChunks.map(it => it.simpleName).join(".")
        } else {
            qualifier = undefined
        }
    }

    for (const [pattern, strategy] of Object.entries(namespaceStrategy)) {
        const regexp = new RegExp(pattern)

        if (regexp.test(detailedName) || regexp.test(simpleName)) {
            return {
                fileName,
                package: packageChunks,
                moduleName,
                qualifier,
                hasRuntime,
                imports,
                name: detailedName,
                strategy
            }
        }
    }

    return {
        fileName,
        package: packageChunks,
        moduleName,
        qualifier,
        hasRuntime,
        imports,
        name: detailedName,
        strategy: defaultNamespaceStrategy
    }
}
