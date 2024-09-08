import ts, {Node, Declaration} from "typescript";
import {Configuration} from "../configuration/configuration.js";
import {InputStructureItem} from "./structure.js";
import {applyPackageNameMapper} from "./package/applyPackageNameMapper.js";
import {applyModuleNameMapper} from "./module/applyModuleNameMapper.js";
import {createBundleInfoItem} from "./bundle/createBundleInfoItem.js";

interface TopLevelMatch {
    name: string,
    node: Declaration,
    hasRuntime: boolean,
}

type TopLevelMatcher = (node: Node) => TopLevelMatch[] | null

const classMatcher: TopLevelMatcher = node => {
    if (ts.isClassDeclaration(node)) {
        const name = node.name?.text
        if (!name) return null

        return [{
            name,
            node,
            hasRuntime: true,
        }]
    }

    return null
}

const interfaceMatcher: TopLevelMatcher = node => {
    if (ts.isInterfaceDeclaration(node)) {
        const name = node.name.text;

        return [{
            name,
            node,
            hasRuntime: false,
        }]
    }

    return null
}

const typeAliasMatcher: TopLevelMatcher = node => {
    if (ts.isTypeAliasDeclaration(node)) {
        const name = node.name.text;

        return [{
            name,
            node,
            hasRuntime: false,
        }]
    }

    return null
}

const enumMatcher: TopLevelMatcher = node => {
    if (ts.isEnumDeclaration(node)) {
        const name = node.name.text;

        return [{
            name,
            node,
            hasRuntime: true,
        }]
    }

    return null
}

const functionMatcher: TopLevelMatcher = node => {
    if (ts.isFunctionDeclaration(node)) {
        const name = node.name?.text
        if (!name) return null

        return [{
            name,
            node,
            hasRuntime: true,
        }]
    }

    return null
}

const variableMatcher: TopLevelMatcher = node => {
    if (ts.isVariableStatement(node)) {
        return node.declarationList.declarations
            .map(declaration => {
                const nameNode = declaration.name
                if (!ts.isIdentifier(nameNode)) return null

                return {
                    name: nameNode.text,
                    node: declaration as Node,
                    hasRuntime: true,
                }
            })
            .filter((match): match is TopLevelMatch => match != null)
    }

    return null
}

const topLevelMatchers: TopLevelMatcher[] = [
    classMatcher,
    interfaceMatcher,
    typeAliasMatcher,
    enumMatcher,
    functionMatcher,
    variableMatcher,
]

const topLevelMatcher: TopLevelMatcher = node => {
    for (const matcher of topLevelMatchers) {
        const result = matcher(node)
        if (result !== null) return result
    }

    return null
}

function applyGranularity(
    items: InputStructureItem[],
    configuration: Configuration,
): InputStructureItem[] {
    const {granularity} = configuration

    if (granularity === "file") {
        return items
    }

    if (granularity === "bundle") {
        const bundleItem = createBundleInfoItem(configuration)

        return [{
            ...bundleItem,
            nodes: items.flatMap(item => item.nodes),
            meta: {
                type: "Bundle",
                name: items
                    .map(item => `\n\t${item.meta.name} [${item.meta.type}]`)
                    .join("")
            }
        }]
    }

    if (granularity === "top-level") {
        return items.flatMap(item => {
            const result: InputStructureItem[] = []

            for (const node of item.nodes) {
                const topLevelMatches = topLevelMatcher(node)

                if (topLevelMatches !== null) {
                    for (const topLevelMatch of topLevelMatches) {
                        result.push({
                            ...item,
                            fileName: `${topLevelMatch.name}.kt`,
                            hasRuntime: topLevelMatch.hasRuntime,
                            nodes: [topLevelMatch.node],
                        })
                    }
                } else {
                    result.push({
                        ...item,
                        nodes: [node],
                    })
                }
            }

            return result
        })
    }

    throw new Error(`Unknown granularity type: ${granularity}`)
}

function applyMappers(
    items: InputStructureItem[],
    configuration: Configuration,
): InputStructureItem[] {
    return items.map(item => {
        const packageMappingResult = applyPackageNameMapper(
            item.package,
            item.fileName,
            configuration,
        )

        const moduleMappingResult = applyModuleNameMapper(
            item.moduleName,
            item.qualifier,
            configuration,
        )

        return {
            ...item,
            ...packageMappingResult,
            ...moduleMappingResult,
        }
    })
}

export function prepareStructure(
    items: InputStructureItem[],
    configuration: Configuration,
): InputStructureItem[] {
    const granularItems = applyGranularity(items, configuration)
    return applyMappers(granularItems, configuration)
}
