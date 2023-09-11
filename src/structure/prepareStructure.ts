import ts, {Node} from "typescript";
import {Configuration} from "../configuration/configuration.js";
import {InputStructureItem} from "./structure.js";
import {applyPackageNameMapper} from "./package/applyPackageNameMapper.js";
import {applyModuleNameMapper} from "./module/applyModuleNameMapper.js";
import {createBundleInfoItem} from "./bundle/createBundleInfoItem.js";

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
            statements: items.flatMap(item => item.statements),
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

            for (const statement of item.statements) {
                let fileName = item.fileName
                let hasRuntime = item.hasRuntime

                const topLevelMatch = topLevelMatcher(statement)

                if (topLevelMatch !== null) {
                    fileName = `${topLevelMatch.name}.kt`
                    hasRuntime = topLevelMatch.hasRuntime
                }

                result.push({
                    ...item,

                    fileName,
                    hasRuntime,
                    statements: [statement],
                })
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
