import {glob} from "glob";
import path from "node:path";
import process from "node:process";
import {PartialConfiguration, Configuration} from "./configuration.js";
import {commonPrefix} from "../utils/fileName.js";
import {toPosix, toAbsolute} from "../utils/path.js";

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

export async function defaultizeConfiguration(configuration: PartialConfiguration): Promise<Configuration> {
    const cwd = toPosix(configuration.cwd ?? process.cwd())

    const input = normalizeOption(configuration.input)

    const ignoreInput = normalizeOption(configuration.ignoreInput)
    const ignoreOutput = normalizeOption(configuration.ignoreOutput)

    const inputFileNames = (await Promise.all(input.map(pattern => glob(pattern, {
        cwd,
        absolute: true,
        posix: true,
        ignore: ignoreInput,
    })))).flat()

    const inputPathChunks = inputFileNames.map(fileName => fileName.split(path.posix.sep))

    // TODO: handle non-default relative root (UNC prefix)
    const defaultInputRoot = inputFileNames.length === 1
        ? path.dirname(inputFileNames[0]) + path.posix.sep
        : commonPrefix(...inputPathChunks).join(path.posix.sep) + path.posix.sep

    const absoluteOutput = toAbsolute(configuration.output, cwd)

    let output = absoluteOutput
    let outputFileName = undefined

    if (output.endsWith(".kt")) {
        output = path.dirname(absoluteOutput)
        outputFileName = absoluteOutput
    }

    return {
        ...configuration,

        inputRoots: normalizeOption(configuration.inputRoots, [defaultInputRoot]),
        inputFileNames,

        input,
        output,

        outputFileName,

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

        conflictResolutionStrategy: configuration.conflictResolutionStrategy ?? {},

        compilerOptions: configuration.compilerOptions ?? {},

        verbose: configuration.verbose ?? false,
        cwd,
    }
}
