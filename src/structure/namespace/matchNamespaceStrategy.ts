import path from "path";
import ts, {ModuleDeclaration} from "typescript";
import {Configuration, NamespaceStrategy} from "../../configuration/configuration";
import {OutputFileInfo} from "../generateOutputFileInfo";
import {applyMapper, dirNameToPackage} from "../../utils/fileName";

export interface NamespaceMatch extends OutputFileInfo {
    name: string
    qualifier: string | undefined
    strategy: NamespaceStrategy
}

export interface NamespaceNameChunk {
    detailedName: string
    simpleName: string
    dirName: string
    isAmbient: boolean
}

const defaultNamespaceStrategy: NamespaceStrategy = "object"

function namespaceNameToDir(namespaceName: string) {
    // delimiters
    // - - react-router
    // : - node:url
    // . - socket.io
    // / - @remix-run/router

    return namespaceName
        .split(/[-:.\/]/)
        .map(it => it.replace(/\W/g, ""))
        .filter(it => it !== "")
        .map(it => it.toLowerCase())
        .join("/")
}

export function extractNamespaceName(
    namespace: ModuleDeclaration,
    suffix: NamespaceNameChunk[] = []
): NamespaceNameChunk[] {
    const simpleName = namespace.name.text
    const detailedName = ts.isIdentifier(namespace.name)
        ? simpleName
        : `"${simpleName}"`
    const dirName = namespaceNameToDir(simpleName)
    const isAmbient = ts.isStringLiteral(namespace.name)

    const nameChunk = {
        detailedName,
        simpleName,
        dirName,
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

export function matchNamespaceStrategy(
    namespace: ModuleDeclaration,
    configuration: Configuration,
    defaultModuleName: string,
): NamespaceMatch {
    const namespaceStrategy = configuration.namespaceStrategy
    const packageNameMapper = configuration.packageNameMapper
    const moduleNameMapper = configuration.moduleNameMapper

    const name = extractNamespaceName(namespace)

    const detailedName = name.map(it => it.detailedName).join(".")
    const simpleName = name.map(it => it.simpleName).join(".")

    const outputDirName = name.map(it => it.dirName).join("/")
    const preparedOutputDirName = applyMapper(outputDirName, packageNameMapper)
    const packageName = dirNameToPackage(preparedOutputDirName)
    const outputFileName = path.join(preparedOutputDirName, "namespace.kt")

    let moduleName: string | undefined = undefined
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

    if (moduleName !== undefined) {
        moduleName = applyMapper(moduleName, moduleNameMapper)
    } else {
        moduleName = defaultModuleName
    }

    for (const [pattern, strategy] of Object.entries(namespaceStrategy ?? {})) {
        const regexp = new RegExp(pattern)

        if (regexp.test(detailedName) || regexp.test(simpleName)) {
            return {
                name: detailedName,
                outputFileName,
                packageName,
                moduleName,
                qualifier,
                strategy,
            }
        }
    }

    return {
        name: detailedName,
        outputFileName,
        packageName,
        moduleName,
        qualifier,
        strategy: defaultNamespaceStrategy,
    }
}
