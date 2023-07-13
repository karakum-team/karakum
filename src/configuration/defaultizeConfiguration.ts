import glob from "glob";
import path from "node:path";
import {PartialConfiguration, Configuration} from "./configuration.js";
import {commonPrefix} from "../utils/fileName.js";

const defaultPluginPatterns = [
    "karakum/plugins/*.js"
]

const defaultAnnotationPatterns = [
    "karakum/annotations/*.js"
]

const defaultNameResolverPatterns = [
    "karakum/nameResolvers/*.js"
]

const defaultInheritanceModifierPatterns = [
    "karakum/inheritanceModifiers/*.js"
]

function normalizeOption(
    patterns: string | string[] | undefined,
    defaultValue: string[] = [],
) {
    return patterns !== undefined
        ? typeof patterns === "string" ? [patterns] : patterns
        : defaultValue
}

export function defaultizeConfiguration(configuration: PartialConfiguration): Configuration {
    const input = normalizeOption(configuration.input)

    const ignoreInput = normalizeOption(configuration.ignoreInput)
    const ignoreOutput = normalizeOption(configuration.ignoreOutput)

    const inputFileNames = input.flatMap(pattern => glob.sync(pattern, {
        absolute: true,
        ignore: ignoreInput,
    }))

    const inputPathChunks = inputFileNames.map(fileName => fileName.split("/"))

    const defaultInputRoot = inputFileNames.length === 1
        ? path.dirname(inputFileNames[0]) + "/"
        : commonPrefix(...inputPathChunks).join("/") + "/"

    return {
        ...configuration,

        inputRoots: normalizeOption(configuration.inputRoots, [defaultInputRoot]),
        inputFileNames,

        input,
        output: configuration.output,

        ignoreInput,
        ignoreOutput,

        libraryName: configuration.libraryName ?? "",
        libraryNameOutputPrefix: configuration.libraryNameOutputPrefix ?? false,

        granularity: configuration.granularity ?? "file",

        plugins: normalizeOption(configuration.plugins, defaultPluginPatterns),

        annotations: normalizeOption(configuration.annotations, defaultAnnotationPatterns),

        nameResolvers: normalizeOption(configuration.nameResolvers, defaultNameResolverPatterns),

        inheritanceModifiers: normalizeOption(configuration.inheritanceModifiers, defaultInheritanceModifierPatterns),

        moduleNameMapper: configuration.moduleNameMapper ?? {},
        packageNameMapper: configuration.packageNameMapper ?? {},

        importInjector: configuration.importInjector ?? {},

        namespaceStrategy: configuration.namespaceStrategy ?? {},

        compilerOptions: configuration.compilerOptions ?? {},

        verbose: configuration.verbose ?? false,
    }
}
