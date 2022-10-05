import ts, {Node, SyntaxKind} from "typescript";
import {Configuration} from "./configuration/configuration";
import glob from "glob";
import minimatch from "minimatch";
// import {preprocess} from "./preprocessor/preprocessor";
// import {createMergeInterfacesTransformer} from "./preprocessor/transformers/mergeInterfacesTransformer";
import fs from "fs";
import path from "path";
import {commonPrefix, generateOutputFileName} from "./utils/fileName";
import {ConverterPlugin} from "./converter/plugin";
import {CheckKindsPlugin} from "./converter/plugins/CheckKindsPlugin";
import {CheckCoveragePlugin} from "./converter/plugins/CheckCoveragePlugin";
import {convertSourceFile} from "./converter/plugins/convertSourceFile";
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
import {convertOptionalUnionType} from "./converter/plugins/convertOptionalUnionType";
import {convertUnionTypeHierarchy} from "./converter/plugins/convertUnionTypeHierarchy";
import {convertCallSignature} from "./converter/plugins/convertCallSignature";
import {convertFunctionDeclaration} from "./converter/plugins/convertFunctionDeclaration";
import {convertTypePredicate} from "./converter/plugins/convertTypePredicate";
import {traverse} from "./utils/traverse";
import {createContext} from "./converter/context";
import {ConfigurationPlugin} from "./converter/plugins/ConfigurationPlugin";
import {CommentsPlugin} from "./converter/plugins/CommentsPlugin";

const hasKind = (kind: SyntaxKind) => (node: Node) => node.kind === kind

const createPlugins = (sourceFileRoot: string, configuration: Configuration): ConverterPlugin[] => [
    new ConfigurationPlugin(configuration),
    new CheckKindsPlugin(),
    new CheckCoveragePlugin(),

    convertSourceFile(sourceFileRoot),

    new CommentsPlugin(),

    convertPrimitive(hasKind(SyntaxKind.DeclareKeyword), () => ""),

    convertPrimitive(hasKind(SyntaxKind.AnyKeyword), () => "Any?"),
    convertPrimitive(hasKind(SyntaxKind.UnknownKeyword), () => "Any?"),
    convertPrimitive(hasKind(SyntaxKind.UndefinedKeyword), () => "null"),
    convertPrimitive(hasKind(SyntaxKind.NullKeyword), () => "null"),
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
    convertTypeParameterDeclaration,
    convertParameterDeclaration,
    convertTypeReferenceNode,
    convertHeritageClause,
    convertExpressionWithTypeArguments,
    convertPropertySignature,
    convertMethodSignature,
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
    convertOptionalUnionType,
    convertUnionTypeHierarchy,
    convertCallSignature,
    convertFunctionDeclaration,
    convertTypePredicate,
]

export function process(configuration: Configuration) {
    const {input, output, ignore, compilerOptions} = configuration

    const normalizedInput = typeof input === "string" ? [input] : input
    const normalizedIgnore = ignore !== undefined
        ? typeof ignore === "string" ? [ignore] : ignore
        : []

    const rootNames = normalizedInput.flatMap(pattern => glob.sync(pattern, {
        absolute: true,
        ignore,
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

    const sourceFileRoot = commonPrefix(...sources).join("/") + "/"

    console.log(`Source files root: ${sourceFileRoot}`)

    if (fs.existsSync(output)) {
        fs.rmSync(output, {recursive: true})
    }
    fs.mkdirSync(output, {recursive: true})

    const context = createContext()

    const plugins = createPlugins(sourceFileRoot, configuration)

    for (const plugin of plugins) {
        plugin.setup(context)
    }

    program.getSourceFiles().forEach(sourceFile => {
        traverse(sourceFile, node => {
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

    program.getSourceFiles()
        .filter(sourceFile => {
            return normalizedIgnore.every(pattern => !minimatch(sourceFile.fileName, pattern))
        })
        .forEach(sourceFile => {
            console.log(`Source file: ${sourceFile.fileName}`)

            const targetFileName = path.resolve(output, generateOutputFileName(sourceFileRoot, sourceFile.fileName))

            console.log(`Target file: ${targetFileName}`)

            // const preprocessedFile = preprocess(sourceFile, [
            //     createMergeInterfacesTransformer(program),
            // ])

            const convertedFile = render(sourceFile)

            fs.mkdirSync(path.dirname(targetFileName), {recursive: true})
            fs.writeFileSync(targetFileName, convertedFile, {})
        })

    for (const plugin of plugins) {
        plugin.generate(context)
    }
}
