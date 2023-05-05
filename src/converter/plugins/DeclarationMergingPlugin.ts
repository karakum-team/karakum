import ts, {Declaration, NamedDeclaration, Node, Program} from "typescript";
import {ConverterPlugin} from "../plugin";
import {ConverterContext} from "../context";
import {Render} from "../render";

export const declarationMergingServiceKey = Symbol()

export class DeclarationMergingService {
    constructor(public readonly program: Program) {
    }

    private coveredSymbols = new Set<ts.Symbol>()

    isCovered(node: Node, declaration: Node): boolean {
        const typeChecker = this.program.getTypeChecker()
        const symbol = typeChecker.getSymbolAtLocation(node)
        if (!symbol) return false

        if (this.coveredSymbols.has(symbol)) return true

        const declarations = symbol.declarations
        // if there are no declarations, it is nothing to merge
        if (!declarations) return false

        const valueDeclaration = symbol.valueDeclaration
        // it is one of the additional declarations, and it will be covered in value declaration
        if (valueDeclaration && declaration !== symbol.valueDeclaration) return true

        this.coveredSymbols.add(symbol)
        return false
    }

    getMembers(node: NamedDeclaration): Declaration[] | undefined {
        if (!node.name) return undefined

        const typeChecker = this.program.getTypeChecker()
        const symbol = typeChecker.getSymbolAtLocation(node.name)

        if (!symbol) return undefined
        if (!symbol.members) return undefined

        return Array.from(symbol.members.values())
            .map(member => member?.declarations?.[0])
            .filter((member): member is Declaration => member !== undefined)
            .filter(member => !ts.isTypeParameterDeclaration(member))
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
