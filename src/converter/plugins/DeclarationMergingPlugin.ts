import ts, {
    Declaration, HeritageClause, InterfaceDeclaration,
    ModuleDeclaration,
    NamedDeclaration,
    Program,
    SignatureDeclaration,
    SymbolTable, TypeParameterDeclaration
} from "typescript";
import {ConverterPlugin} from "../plugin.js";
import {ConverterContext} from "../context.js";
import {Render} from "../render.js";
import {DeepMap} from "../../utils/deepMap.js";
import {GeneratedFile} from "../generated.js";
import {NamespaceStrategy} from "../../configuration/configuration.js";

export const declarationMergingServiceKey = Symbol()

export class DeclarationMergingService {
    private readonly virtualSourceFile = ts.createSourceFile("virtual.d.ts", "", ts.ScriptTarget.Latest)
    private readonly printer = ts.createPrinter({
        removeComments: true,
        newLine: ts.NewLineKind.LineFeed,
    })

    constructor(public readonly program: Program) {
    }

    private coveredSymbols = new Set<ts.Symbol>()

    isCovered(node: NamedDeclaration): boolean {
        const symbol = this.getSymbol(node)
        if (!symbol) return false

        if (this.coveredSymbols.has(symbol)) return true

        const valueDeclaration = symbol.valueDeclaration

        // it is one of the additional declarations, and it will be covered in value declaration
        return valueDeclaration !== undefined
            && ts.isClassDeclaration(valueDeclaration)
            && node !== valueDeclaration;
    }

    cover(node: NamedDeclaration) {
        const symbol = this.getSymbol(node)
        if (!symbol) return

        this.coveredSymbols.add(symbol)
    }

    getTypeParameters(node: NamedDeclaration): TypeParameterDeclaration[] | undefined {
        const symbol = this.getSymbol(node)
        if (!symbol) return undefined

        const declarationTypeParameters = symbol.declarations
            ?.filter((it): it is InterfaceDeclaration => ts.isInterfaceDeclaration(it))
            ?.map(it => Array.from(it.typeParameters ?? []))

        const valueDeclarationTypeParameters =
            symbol.valueDeclaration !== undefined && ts.isClassDeclaration(symbol.valueDeclaration)
                ? Array.from(symbol.valueDeclaration.typeParameters ?? [])
                : undefined

        return [
            ...(declarationTypeParameters ?? []),
            valueDeclarationTypeParameters ?? [],
        ].reduce((typeParameters, result) => (
            typeParameters.length > result.length
                ? typeParameters
                : result
        ))
    }

    getHeritageClauses(node: NamedDeclaration): HeritageClause[] | undefined {
        const symbol = this.getSymbol(node)
        if (!symbol) return undefined

        const heritageClauses = symbol.declarations
                ?.filter((it): it is InterfaceDeclaration => ts.isInterfaceDeclaration(it))
                ?.flatMap(it => it.heritageClauses)
                ?.filter((it): it is HeritageClause => Boolean(it))

        if (ts.isClassDeclaration(node)) {
            const mainHeritageClauses = node.heritageClauses

            return [
                ...(mainHeritageClauses ?? []),
                ...(heritageClauses ?? []),
            ]
        } else {
            return heritageClauses
        }
    }

    getMembers(
        node: NamedDeclaration,
        resolveNamespaceStrategy: ((node: ModuleDeclaration) => NamespaceStrategy) | undefined,
    ): Declaration[] | undefined {
        const symbol = this.getSymbol(node)
        if (!symbol) return undefined

        const members = this.getUniqMembers(symbol.members)
            .filter(member => !ts.isTypeParameterDeclaration(member))

        const exports = this.getUniqMembers(symbol.exports)
            .filter(member => !ts.isTypeParameterDeclaration(member))

        return [...members, ...exports]
            .filter(member => {
                if (
                    ts.isModuleBlock(member.parent)
                    && resolveNamespaceStrategy?.(member.parent.parent) === "package"
                ) {
                    return false
                }

                if (
                    ts.isVariableDeclarationList(member.parent)
                    && ts.isVariableStatement(member.parent.parent)
                    && ts.isModuleBlock(member.parent.parent.parent)
                    && resolveNamespaceStrategy?.(member.parent.parent.parent.parent) === "package"
                ) {
                    return false
                }

                return true
            })
    }

    private getUniqMembers(symbolTable: SymbolTable | undefined): Declaration[] {
        const typeChecker = this.program.getTypeChecker()

        return Array.from(symbolTable?.values() ?? [])
            .flatMap(symbol => {
                const declarations = symbol.declarations ?? []
                const [firstDeclaration] = declarations

                if (!firstDeclaration) return []
                if (!this.isSignatureDeclaration(firstDeclaration)) return [firstDeclaration]

                const declarationMap = declarations.reduce((acc, declaration) => {
                    if (!this.isSignatureDeclaration(declaration)) return acc

                    const parameterSymbols = declaration.parameters
                        .map(parameter => {
                            const typeNode = parameter.type
                            if (!typeNode) return "any"

                            const type = typeChecker.getTypeFromTypeNode(typeNode)

                            return type?.symbol ?? this.printNode(typeNode)
                        })

                    acc.set(parameterSymbols, declaration)

                    return acc
                }, new DeepMap<ts.Symbol | string, Declaration>())

                return Array.from(declarationMap.values())
            })
    }

    private isSignatureDeclaration(declaration: Declaration): declaration is SignatureDeclaration {
        return ts.isCallSignatureDeclaration(declaration)
            || ts.isConstructSignatureDeclaration(declaration)
            || ts.isConstructorDeclaration(declaration)
            || ts.isMethodSignature(declaration)
            || ts.isMethodDeclaration(declaration)
    }

    private getSymbol(node: NamedDeclaration): ts.Symbol | undefined {
        if (!node.name) return undefined

        const typeChecker = this.program.getTypeChecker()
        return typeChecker.getSymbolAtLocation(node.name)
    }

    private printNode(node: ts.Node) {
        const sourceFile = node.getSourceFile() ?? this.virtualSourceFile

        return this.printer.printNode(ts.EmitHint.Unspecified, node, sourceFile)
    }
}

export class DeclarationMergingPlugin implements ConverterPlugin {
    private readonly declarationMergingService: DeclarationMergingService

    constructor(program: Program) {
        this.declarationMergingService = new DeclarationMergingService(program);
    }

    setup(context: ConverterContext): void {
        context.registerService(declarationMergingServiceKey, this.declarationMergingService)
    }

    traverse(node: ts.Node, context: ConverterContext): void {
    }

    render(node: ts.Node, context: ConverterContext, next: Render): string | null {
        return null;
    }

    generate(): GeneratedFile[] {
        return [];
    }
}
