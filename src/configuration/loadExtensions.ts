import {glob} from "glob";
import {toAbsolute, toModuleName} from "../utils/path.js";
import {ConverterPlugin, createSimplePlugin, SimpleConverterPlugin} from "../converter/plugin.js";
import {createSimpleInjection, Injection, SimpleInjection} from "../converter/injection.js";
import {Annotation} from "../converter/annotation.js";
import {NameResolver} from "../converter/nameResolver.js";
import {InheritanceModifier} from "../converter/inheritanceModifier.js";
import {VarianceModifier} from "../converter/varianceModifier.js";

export interface ExtensionConfiguration {
    plugins: string[]
    injections: string[]
    annotations: string[]
    nameResolvers: string[]
    inheritanceModifiers: string[]
    varianceModifiers: string[]
}

export interface PartialExtensions {
    plugins?: (SimpleConverterPlugin | ConverterPlugin)[]
    injections?: (SimpleInjection | Injection)[]
    annotations?: Annotation[]
    nameResolvers?: NameResolver[]
    inheritanceModifiers?: InheritanceModifier[]
    varianceModifiers?: VarianceModifier[]
}

export interface Extensions {
    plugins: ConverterPlugin[]
    injections: Injection[]
    annotations: Annotation[]
    nameResolvers: NameResolver[]
    inheritanceModifiers: InheritanceModifier[]
    varianceModifiers: VarianceModifier[]
}

export async function loadExtensions<T>(
    name: string,
    patterns: string[],
    cwd: string,
    loader: (extension: unknown) => T = extension => extension as T
): Promise<T[]> {
    const fileNames = (await Promise.all(patterns.map(pattern => glob(pattern, {
        cwd,
        absolute: true,
        posix: true,
    })))).flat()

    const extensions: T[] = []

    for (const fileName of fileNames) {
        const extensionModule: { default: unknown } = await import(toModuleName(fileName))
        const extensionExport = extensionModule.default

        if (Array.isArray(extensionExport)) {
            console.log(`${name} file: ${fileName} [x${extensionExport.length}]`)

            extensions.push(...extensionExport.map(it => loader(it)))
        } else {
            console.log(`${name} file: ${fileName}`)

            extensions.push(loader(extensionExport))
        }
    }

    return extensions
}

export async function loadExtensionsFromGlobs(
    configuration: ExtensionConfiguration,
    cwd: string,
): Promise<Extensions> {

    const plugins = await loadExtensions<ConverterPlugin>(
        "Plugin",
        configuration.plugins,
        cwd,
        plugin => {
            if (typeof plugin === "function") {
                return createSimplePlugin(plugin as SimpleConverterPlugin)
            } else {
                return plugin as ConverterPlugin
            }
        }
    )

    const injections = await loadExtensions<Injection>(
        "Injection",
        configuration.injections,
        cwd,
        injection => {
            if (typeof injection === "function") {
                return createSimpleInjection(injection as SimpleInjection)
            } else {
                return injection as Injection
            }
        }
    )

    const annotations = await loadExtensions<Annotation>(
        "Annotation",
        configuration.annotations,
        cwd,
    )

    const nameResolvers = await loadExtensions<NameResolver>(
        "Name Resolver",
        configuration.nameResolvers,
        cwd,
    )

    const inheritanceModifiers = await loadExtensions<InheritanceModifier>(
        "Inheritance Modifier",
        configuration.inheritanceModifiers,
        cwd,
    )

    const varianceModifiers = await loadExtensions<VarianceModifier>(
        "Variance Modifier",
        configuration.varianceModifiers,
        cwd,
    )

    return {
        plugins,
        injections,
        annotations,
        nameResolvers,
        inheritanceModifiers,
        varianceModifiers,
    }
}

export async function loadExtensionsFromFile(
    extensions: string,
    cwd: string,
): Promise<Extensions> {
    const absoluteExtensions = toAbsolute(extensions, cwd)
    const extensionModule: { default: PartialExtensions } = await import(toModuleName(absoluteExtensions))

    console.log(`Extension file: ${absoluteExtensions}`)

    const plugins = (extensionModule.default.plugins ?? [])
        .map(plugin => {
            if (typeof plugin === "function") {
                return createSimplePlugin(plugin as SimpleConverterPlugin)
            } else {
                return plugin as ConverterPlugin
            }
        })

    const injections = (extensionModule.default.injections ?? [])
        .map(injection => {
            if (typeof injection === "function") {
                return createSimpleInjection(injection as SimpleInjection)
            } else {
                return injection as Injection
            }
        })

    return {
        plugins,
        injections,
        annotations: extensionModule.default.annotations ?? [],
        nameResolvers: extensionModule.default.nameResolvers ?? [],
        inheritanceModifiers: extensionModule.default.inheritanceModifiers ?? [],
        varianceModifiers: extensionModule.default.varianceModifiers ?? [],
    }
}
