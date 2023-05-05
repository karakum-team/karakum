import ts, {Declaration, NamedDeclaration, Program} from "typescript";
import {ConverterPlugin} from "../plugin";
import {ConverterContext} from "../context";
import {Render} from "../render";

export const declarationMergingServiceKey = Symbol()

export class DeclarationMergingService {
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

        const members = Array.from(symbol.members?.values() ?? [])
            .map(member => member?.declarations?.[0])
            .filter((member): member is Declaration => member !== undefined)
            .filter(member => !ts.isTypeParameterDeclaration(member))

        const exports = Array.from(symbol.exports?.values() ?? [])
            .map(member => member?.declarations?.[0])
            .filter((member): member is Declaration => member !== undefined)

        return [...members, ...exports]
    }

    private getSymbol(node: NamedDeclaration): ts.Symbol | undefined {
        if (!node.name) return undefined

        const typeChecker = this.program.getTypeChecker()
        return typeChecker.getSymbolAtLocation(node.name)
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
