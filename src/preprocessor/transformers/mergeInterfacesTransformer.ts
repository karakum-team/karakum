import ts, {InterfaceDeclaration, SourceFile, Symbol, TransformerFactory, TypeChecker} from "typescript";
import {createTransformer} from "../createTransformer";

export const createMergeInterfacesTransformer = (typeChecker: TypeChecker): TransformerFactory<SourceFile> => {
    const interfaceSymbols = new Set<Symbol>()

    return createTransformer(node => {
        if (ts.isInterfaceDeclaration(node)) {
            const interfaceType = typeChecker.getTypeAtLocation(node)
            const interfaceSymbol = interfaceType.getSymbol()

            if (!interfaceSymbol) return

            // skip handled interfaces
            if (interfaceSymbols.has(interfaceSymbol)) return []

            interfaceSymbols.add(interfaceSymbol)

            const declarations = (interfaceSymbol.getDeclarations() ?? [node]) as InterfaceDeclaration[]
            const [firstDeclaration] = declarations

            const decorators = declarations.flatMap(it => it.decorators ?? [])
            const modifiers = declarations.flatMap(it => it.modifiers ?? [])
            const members = declarations.flatMap(it => it.members)

            return ts.factory.createInterfaceDeclaration(
                decorators,
                modifiers,
                firstDeclaration.name,
                firstDeclaration.typeParameters,
                firstDeclaration.heritageClauses,
                members
            )
        }
    });
}
