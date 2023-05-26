import ts, {Declaration, NamedDeclaration, Program, SignatureDeclaration, SymbolTable, EmitHint, ScriptTarget} from "typescript";
import {ConverterPlugin} from "../plugin";
import {ConverterContext} from "../context";
import {Render} from "../render";
import {DeepMap} from "../../utils/deepMap";

export const declarationMergingServiceKey = Symbol()

export class DeclarationMergingService {
    private readonly virtualSourceFile = ts.createSourceFile("virtual.d.ts", "", ScriptTarget.Latest)
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

    getMembers(node: NamedDeclaration): Declaration[] | undefined {
        const symbol = this.getSymbol(node)
        if (!symbol) return undefined

        const members = this.getUniqMembers(symbol.members)
            .filter(member => !ts.isTypeParameterDeclaration(member))

        const exports = this.getUniqMembers(symbol.exports)
            .filter(member => !ts.isTypeParameterDeclaration(member))

        return [...members, ...exports]
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

        return this.printer.printNode(EmitHint.Unspecified, node, sourceFile)
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

    generate(context: ConverterContext): Record<string, string> {
        return {};
    }
}
