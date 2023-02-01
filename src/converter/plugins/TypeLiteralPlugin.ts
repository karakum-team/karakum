import path from "path";
import ts, {ConditionalTypeNode, TypeLiteralNode} from "typescript";
import {ConverterPlugin} from "../plugin";
import {ConverterContext} from "../context";
import {ifPresent, Render} from "../render";
import {generateOutputFileInfo} from "../../structure/generateOutputFileInfo";
import {createGeneratedFile} from "../../structure/createGeneratedFile";
import {ConfigurationService, configurationServiceKey} from "./ConfigurationPlugin";
import {CheckCoverageService, checkCoverageServiceKey} from "./CheckCoveragePlugin";
import {TypeScriptService, typeScriptServiceKey} from "./TypeScriptPlugin";
import {traverse} from "../../utils/traverse";
import {findClosest} from "../../utils/findClosest";
import {NameResolver} from "../nameResolver";
import {resolveFunctionTypeAliasParameterName} from "../nameResolvers/resolveFunctionTypeAliasParameterName";
import {resolveFunctionParameterName} from "../nameResolvers/resolveFunctionParameterName";
import {resolveTypeAliasPropertyName} from "../nameResolvers/resolveTypeAliasPropertyName";
import {resolveCallSignatureParameterName} from "../nameResolvers/resolveCallSignatureParameterName";
import {resolveConstructorParameterName} from "../nameResolvers/resolveConstructorParameterName";
import {resolveClassMethodParameterName} from "../nameResolvers/resolveClassMethodParameterName";
import {resolveInterfaceMethodParameterName} from "../nameResolvers/resolveInterfaceMethodParameterName";
import {resolveFunctionReturnTypeName} from "../nameResolvers/resolveFunctionReturnTypeName";
import {resolveInterfaceMethodReturnTypeName} from "../nameResolvers/resolveInterfaceMethodReturnTypeName";
import {resolveClassMethodReturnTypeName} from "../nameResolvers/resolveClassMethodReturnTypeName";

const defaultNameResolvers: NameResolver<TypeLiteralNode>[] = [
    resolveFunctionParameterName,
    resolveFunctionReturnTypeName,
    resolveFunctionTypeAliasParameterName,
    resolveCallSignatureParameterName,
    resolveConstructorParameterName,
    resolveClassMethodParameterName,
    resolveClassMethodReturnTypeName,
    resolveInterfaceMethodParameterName,
    resolveInterfaceMethodReturnTypeName,
    resolveTypeAliasPropertyName,
]

export class TypeLiteralPlugin implements ConverterPlugin {
    private counter = 0
    private readonly generated: Record<string, { name: string, declaration: string }[]> = {}
    private readonly nameResolvers: NameResolver<TypeLiteralNode>[]

    constructor(
        private sourceFileRoot: string,
        nameResolvers: NameResolver[],
    ) {
        this.nameResolvers = [
            ...nameResolvers,
            ...defaultNameResolvers,
        ]
    }

    generate(context: ConverterContext): Record<string, string> {
        const configurationService = context.lookupService<ConfigurationService>(configurationServiceKey)
        const configuration = configurationService?.configuration
        if (configuration === undefined) throw new Error("TypeLiteralPlugin can't work without ConfigurationService")

        const output = configuration.output
        const granularity = configuration.granularity ?? "file"

        return Object.fromEntries(
            Object.entries(this.generated)
                .flatMap(([sourceFileName, declarations]) => {
                    const {outputFileName, packageName} = generateOutputFileInfo(
                        this.sourceFileRoot,
                        sourceFileName,
                        configuration,
                    );

                    if (granularity === "top-level") {
                        return declarations
                            .map(({name, declaration}) => {
                                const outputDirName = path.dirname(outputFileName)
                                const currentOutputFileName = `${outputDirName}/${name}.kt`
                                const generatedFile = createGeneratedFile(
                                    this.sourceFileRoot,
                                    currentOutputFileName,
                                    packageName,
                                    declaration,
                                    configuration,
                                )

                                return [path.resolve(output, currentOutputFileName), generatedFile];
                            })
                    } else {
                        const generatedFile = declarations
                            .map(({declaration}) => declaration)
                            .join("\n\n")

                        return [[path.resolve(output, outputFileName), generatedFile]];
                    }
                })
        );
    }

    render(node: ts.Node, context: ConverterContext, next: Render): string | null {
        if (!ts.isTypeLiteralNode(node)) return null

        const checkCoverageService = context.lookupService<CheckCoverageService>(checkCoverageServiceKey)
        checkCoverageService?.cover(node)

        // handle empty type literal
        if (node.members.length === 0) return "Any"

        const name = this.resolveName(node, context) ?? `Temp${this.counter++}`

        const typeParameters = this.extractTypeParameters(node, context).join(", ")

        const fileName = node.getSourceFile()?.fileName ?? "generated.d.ts"
        const generatedDeclarations = this.generated[fileName] ?? []

        const members = node.members
            .map(member => next(member))
            .join("\n")

        const declaration = `
external interface ${name}${ifPresent(typeParameters, it => `<${it}>`)} {
${members}
}
        `

        generatedDeclarations.push({
            name,
            declaration,
        })

        this.generated[fileName] = generatedDeclarations

        return `${name}${ifPresent(typeParameters, it => `<${it}>`)}`;
    }

    setup(context: ConverterContext): void {
    }

    traverse(node: ts.Node, context: ConverterContext): void {
    }

    private resolveName(typeLiteralNode: TypeLiteralNode, context: ConverterContext): string | null {
        for (const nameResolver of this.nameResolvers) {
            const result = nameResolver(typeLiteralNode, context)

            if (result !== null) return result
        }

        return null
    }


    private extractTypeParameters(typeLiteralNode: TypeLiteralNode, context: ConverterContext): string[] {
        const result: string[] = []

        const typeScriptService = context.lookupService<TypeScriptService>(typeScriptServiceKey)
        const typeChecker = typeScriptService?.program.getTypeChecker()

        traverse(typeLiteralNode, node => {
            if (ts.isIdentifier(node)) {
                const symbol = typeChecker?.getSymbolAtLocation(node)
                const typeParameterDeclarations = (symbol?.declarations ?? [])
                    .filter(declaration => ts.isTypeParameterDeclaration(declaration))

                for (const declaration of typeParameterDeclarations) {
                    let typeParameterContainer = declaration.parent

                    if (ts.isInferTypeNode(declaration.parent)) {
                        const conditionalType = findClosest(
                            declaration.parent,
                            (node): node is ConditionalTypeNode => ts.isConditionalTypeNode(node)
                        )

                        if (conditionalType != undefined) {
                            typeParameterContainer = conditionalType.trueType
                        }
                    }

                    const foundParent = findClosest(typeLiteralNode, node => node === typeParameterContainer)

                    if (foundParent !== undefined) {
                        result.push(node.text)
                    }
                }
            }
        })

        return result
    }
}
