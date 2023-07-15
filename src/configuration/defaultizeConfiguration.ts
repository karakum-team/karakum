import {glob} from "glob";
import defaultPath from "node:path";
import path from "node:path/posix";
import process from "node:process";
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

function normalizePath(input: string) {
    return defaultPath.sep === path.win32.sep
        ? input.replaceAll(path.win32.sep, path.sep)
        : input
}

export function defaultizeConfiguration(configuration: PartialConfiguration): Configuration {
    const cwd = normalizePath(configuration.cwd ?? process.cwd())

    const input = normalizeOption(configuration.input)

    const ignoreInput = normalizeOption(configuration.ignoreInput)
    const ignoreOutput = normalizeOption(configuration.ignoreOutput)

    const inputFileNames = input.flatMap(pattern => glob.sync(pattern, {
        cwd,
        absolute: true,
        posix: true,
        ignore: ignoreInput,
    }))

    const inputPathChunks = inputFileNames.map(fileName => fileName.split(path.sep))

    // TODO: handle non-default relative root (UNC prefix)
    const defaultInputRoot = inputFileNames.length === 1
        ? path.dirname(inputFileNames[0]) + path.sep
        : commonPrefix(...inputPathChunks).join(path.sep) + path.sep

    const absoluteOutput = path.isAbsolute(configuration.output)
        ? configuration.output
        : path.join(cwd, configuration.output)

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

        compilerOptions: configuration.compilerOptions ?? {},

        verbose: configuration.verbose ?? false,
        cwd,
    }
}
