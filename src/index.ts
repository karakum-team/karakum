import ts, {Node, SyntaxKind} from "typescript";
import {Configuration} from "./configuration/configuration";
import glob from "glob";
import minimatch from "minimatch";
// import {preprocess} from "./preprocessor/preprocessor";
// import {createMergeInterfacesTransformer} from "./preprocessor/transformers/mergeInterfacesTransformer";
import fs from "fs";
import path from "path";
import {commonPrefix} from "./utils/fileName";
import {ConverterPlugin, createSimplePlugin, SimpleConverterPlugin} from "./converter/plugin";
import {CheckKindsPlugin} from "./converter/plugins/CheckKindsPlugin";
import {CheckCoveragePlugin} from "./converter/plugins/CheckCoveragePlugin";
import {convertPrimitive} from "./converter/plugins/convertPrimitive";
import {convertModuleDeclaration} from "./converter/plugins/convertModuleDeclaration";
import {convertModuleBlock} from "./converter/plugins/convertModuleBlock";
import {convertInterfaceDeclaration} from "./converter/plugins/convertInterfaceDeclaration";
import {convertTypeParameterDeclaration} from "./converter/plugins/convertTypeParameterDeclaration";
import {convertParameterDeclaration} from "./converter/plugins/convertParameterDeclaration";
import {convertTypeReferenceNode} from "./converter/plugins/convertTypeReferenceNode";
import {convertHeritageClause} from "./converter/plugins/convertHeritageClause";
import {convertExpressionWithTypeArguments} from "./converter/plugins/convertExpressionWithTypeArguments";
import {convertPropertySignature} from "./converter/plugins/convertPropertySignature";
import {convertMethodSignature} from "./converter/plugins/convertMethodSignature";
import {convertFunctionType} from "./converter/plugins/convertFunctionType";
import {convertLiteralType} from "./converter/plugins/convertLiteralType";
import {convertArrayType} from "./converter/plugins/convertArrayType";
import {convertReadonlyArrayType} from "./converter/plugins/convertReadonlyArrayType";
import {convertTypeAliasDeclaration} from "./converter/plugins/convertTypeAliasDeclaration";
import {convertEnumDeclaration} from "./converter/plugins/convertEnumDeclaration";
import {convertEnumMember} from "./converter/plugins/convertEnumMember";
import {convertVariableStatement} from "./converter/plugins/convertVariableStatement";
import {convertVariableDeclaration} from "./converter/plugins/convertVariableDeclaration";
import {convertQualifiedName} from "./converter/plugins/convertQualifiedName";
import {convertPrefixUnaryExpression} from "./converter/plugins/convertPrefixUnaryExpression";
import {convertParenthesizedType} from "./converter/plugins/convertParenthesizedType";
import {NullableUnionTypePlugin} from "./converter/plugins/NullableUnionTypePlugin";
import {convertUnionTypeHierarchy} from "./converter/plugins/convertUnionTypeHierarchy";
import {convertCallSignature} from "./converter/plugins/convertCallSignature";
import {convertFunctionDeclaration} from "./converter/plugins/convertFunctionDeclaration";
import {convertTypePredicate} from "./converter/plugins/convertTypePredicate";
import {traverse} from "./utils/traverse";
import {createContext} from "./converter/context";
import {ConfigurationPlugin} from "./converter/plugins/ConfigurationPlugin";
import {CommentsPlugin} from "./converter/plugins/CommentsPlugin";
import {convertClassDeclaration} from "./converter/plugins/convertClassDeclaration";
import {convertMethodDeclaration} from "./converter/plugins/convertMethodDeclaration";
import {convertConstructorDeclaration} from "./converter/plugins/convertConstructorDeclaration";
import {convertPropertyDeclaration} from "./converter/plugins/convertPropertyDeclaration";
import {TypeLiteralPlugin} from "./converter/plugins/TypeLiteralPlugin";
import {prepareFileStructure} from "./structure/prepareFileStructure";
import {createTargetFile} from "./structure/createTargetFile";

const hasKind = (kind: SyntaxKind) => (node: Node) => node.kind === kind

const createPlugins = (sourceFileRoot: string, configuration: Configuration): ConverterPlugin[] => [
    new ConfigurationPlugin(configuration),
    new CheckKindsPlugin(),
    new CheckCoveragePlugin(),

    new NullableUnionTypePlugin(),
    new TypeLiteralPlugin(sourceFileRoot),

    convertPrimitive(hasKind(SyntaxKind.DeclareKeyword), () => ""),

    convertPrimitive(hasKind(SyntaxKind.AnyKeyword), () => "Any?"),
    convertPrimitive(hasKind(SyntaxKind.UnknownKeyword), () => "Any?"),
    convertPrimitive(hasKind(SyntaxKind.UndefinedKeyword), () => ""), // covered by nullability
    convertPrimitive(hasKind(SyntaxKind.NullKeyword), () => ""), // covered by nullability
    convertPrimitive(hasKind(SyntaxKind.ObjectKeyword), () => "Any"),
    convertPrimitive(hasKind(SyntaxKind.StringKeyword), () => "String"),
    convertPrimitive(hasKind(SyntaxKind.NumberKeyword), () => "Double"),
    convertPrimitive(hasKind(SyntaxKind.BooleanKeyword), () => "Boolean"),
    convertPrimitive(hasKind(SyntaxKind.FalseKeyword), () => "Boolean"),
    convertPrimitive(hasKind(SyntaxKind.TrueKeyword), () => "Boolean"),
    convertPrimitive(hasKind(SyntaxKind.VoidKeyword), () => "Unit"),
    convertPrimitive(hasKind(SyntaxKind.NeverKeyword), () => "Nothing"),

    convertPrimitive(ts.isIdentifier, node => node.text),
    convertPrimitive(ts.isStringLiteral, () => "String"),
    convertPrimitive(ts.isNumericLiteral, () => "Double"),

    convertModuleDeclaration,
    convertModuleBlock,
    convertInterfaceDeclaration,
    convertClassDeclaration,
    convertTypeParameterDeclaration,
    convertParameterDeclaration,
    convertTypeReferenceNode,
    convertHeritageClause,
    convertExpressionWithTypeArguments,
    convertPropertySignature,
    convertPropertyDeclaration,
    convertMethodSignature,
    convertMethodDeclaration,
    convertConstructorDeclaration,
    convertFunctionType,
    convertLiteralType,
    convertArrayType,
    convertReadonlyArrayType,
    convertTypeAliasDeclaration,
    convertEnumDeclaration,
    convertEnumMember,
    convertVariableStatement,
    convertVariableDeclaration,
    convertQualifiedName,
    convertPrefixUnaryExpression,
    convertParenthesizedType,
    convertUnionTypeHierarchy,
    convertCallSignature,
    convertFunctionDeclaration,
    convertTypePredicate,
]

export const defaultPluginPatterns = [
    "karakum/**/*.js"
]

export async function process(configuration: Configuration) {
    const {input, output, ignore, plugins: pluginPatterns, compilerOptions} = configuration

    const normalizedInput = typeof input === "string" ? [input] : input
    const normalizedIgnore = ignore !== undefined
        ? typeof ignore === "string" ? [ignore] : ignore
        : []
    const normalizedPluginPatterns = pluginPatterns !== undefined
        ? typeof pluginPatterns === "string" ? [pluginPatterns] : pluginPatterns
        : defaultPluginPatterns

    const rootNames = normalizedInput.flatMap(pattern => glob.sync(pattern, {
        absolute: true,
        ignore,
    }))

    const pluginFileNames = normalizedPluginPatterns.flatMap(pattern => glob.sync(pattern, {
        absolute: true,
    }))

    const preparedCompilerOptions = {
        lib: [],
        types: [],
        ...compilerOptions
    }

    const compilerHost = ts.createCompilerHost(preparedCompilerOptions, /* setParentNodes */ true);

    const program = ts.createProgram(rootNames, preparedCompilerOptions, compilerHost)

    console.log(`Source files count: ${program.getSourceFiles().length}`)

    const sources = rootNames.map(fileName => fileName.split("/"))

    const sourceFileRoot = rootNames.length === 1
        ? path.dirname(rootNames[0]) + "/"
        : commonPrefix(...sources).join("/") + "/"

    console.log(`Source files root: ${sourceFileRoot}`)

    if (fs.existsSync(output)) {
        fs.rmSync(output, {recursive: true})
    }
    fs.mkdirSync(output, {recursive: true})

    const context = createContext()

    const customPlugins: ConverterPlugin[] = []

    for (const pluginFileName of pluginFileNames) {
        console.log(`Plugin file: ${pluginFileName}`)

        const pluginModule: { default: unknown } = await import(pluginFileName)
        const plugin = pluginModule.default

        if (typeof plugin === "function") {
            customPlugins.push(createSimplePlugin(plugin as SimpleConverterPlugin))
        } else {
            customPlugins.push(plugin as ConverterPlugin)
        }
    }

    const defaultPlugins = createPlugins(sourceFileRoot, configuration)

    const plugins = [
        // it is important to handle comments at first
        new CommentsPlugin(),

        ...customPlugins,
        ...defaultPlugins
    ]

    for (const plugin of plugins) {
        plugin.setup(context)
    }

    const fileStructure = prepareFileStructure(sourceFileRoot, program, configuration)

    fileStructure
        .flatMap(it => it.nodes)
        .forEach(node => {
            traverse(node, node => {
                for (const plugin of plugins) {
                    plugin.traverse(node, context)
                }
            })
        })

    const render = (node: Node) => {
        for (const plugin of plugins) {
            const result = plugin.render(node, context, render)

            if (result !== null) return result
        }

        return `/* ${node.getText()} */`
    }

    fileStructure
        .filter(item => {
            return normalizedIgnore.every(pattern => !minimatch(item.sourceFileName, pattern))
        })
        .forEach(item => {
            const targetFileName = path.resolve(output, item.outputFileName)

            console.log(`Source file: ${item.sourceFileName}`)
            console.log(`Target file: ${targetFileName}`)

            const convertedBody = item.nodes
                .map(node => render(node))
                .join("\n\n")

            const targetFile = createTargetFile(
                sourceFileRoot,
                item.sourceFileName,
                item.outputFileName,
                convertedBody,
                configuration,
            )

            fs.mkdirSync(path.dirname(targetFileName), {recursive: true})
            fs.writeFileSync(targetFileName, targetFile)
        })

    for (const plugin of plugins) {
        const generated = plugin.generate(context)

        for (const [fileName, content] of Object.entries(generated)) {
            if (fs.existsSync(fileName)) {
                fs.appendFileSync(fileName, content)
            } else {
                fs.writeFileSync(fileName, content)
            }
        }
    }
}
