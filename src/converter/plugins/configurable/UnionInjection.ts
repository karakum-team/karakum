import ts, {NamedDeclaration, Node, TypeReferenceNode, UnionTypeNode} from "typescript";
import {ifPresent, Render} from "../../render.js";
import {createAnonymousDeclarationPlugin} from "../AnonymousDeclarationPlugin.js";
import {extractTypeParameters, renderDeclaration, renderReference} from "../../extractTypeParameters.js";
import {Injection, InjectionContext, InjectionType} from "../../injection.js";
import {ConverterContext} from "../../context.js";
import {NameResolverService, nameResolverServiceKey} from "../NameResolverPlugin.js";
import {TypeScriptService, typeScriptServiceKey} from "../TypeScriptPlugin.js";
import {InjectionService, injectionServiceKey} from "../InjectionPlugin.js";

export const unionServiceKey = Symbol()

export class UnionService {
    private readonly unionParents = new Map<ts.Symbol, string[]>()
    private readonly coveredUnionParents = new Set<ts.Symbol>()

    get uncoveredUnionParents(): Map<ts.Symbol, string[]> {
        return new Map(
            Array.from(this.unionParents.entries())
                .filter(([symbol]) => !this.coveredUnionParents.has(symbol))
        )
    }

    constructor(private readonly context: ConverterContext) {

    }

    isCovered(node: NamedDeclaration): boolean {
        const symbol = this.getSymbol(node)
        if (!symbol) return false

        return this.coveredUnionParents.has(symbol)
    }

    cover(node: NamedDeclaration) {
        const symbol = this.getSymbol(node)
        if (!symbol) return

        this.coveredUnionParents.add(symbol)
    }

    register(union: UnionTypeNode, reference: TypeReferenceNode) {
        const nameResolverService = this.context.lookupService<NameResolverService>(nameResolverServiceKey)

        const typeScriptService = this.context.lookupService<TypeScriptService>(typeScriptServiceKey)
        const typeChecker = typeScriptService?.program.getTypeChecker()

        const symbol = typeChecker?.getSymbolAtLocation(reference.typeName)

        let name: string

        if (
            union.parent
            && ts.isTypeAliasDeclaration(union.parent)
            && union.parent.type === union
        ) {
            name = union.parent.name.text
        } else {
            name = nameResolverService?.resolveName(union, this.context) ?? "Anonymous"
        }

        if (symbol) {
            const parentNames = this.unionParents.get(symbol) ?? []

            parentNames.push(name)

            this.unionParents.set(symbol, parentNames)
        }
    }

    getParents(node: NamedDeclaration): string[] {
        const symbol = this.getSymbol(node)
        if (!symbol) return []

        return this.unionParents.get(symbol) ?? []
    }

    private getSymbol(node: NamedDeclaration): ts.Symbol | undefined {
        if (!node.name) return undefined

        const typeScriptService = this.context.lookupService<TypeScriptService>(typeScriptServiceKey)
        const typeChecker = typeScriptService?.program.getTypeChecker()
        return typeChecker?.getSymbolAtLocation(node.name)
    }
}

export class UnionInjection implements Injection {
    private unionService: UnionService | undefined

    private readonly anonymousUnionDeclarationPlugin = createAnonymousDeclarationPlugin(
        (node, context, render) => {
            if (
                ts.isUnionTypeNode(node)
                && node.types.every(type => ts.isTypeReferenceNode(type) && !type.typeArguments)
            ) {
                const typeParameters = extractTypeParameters(node, context)

                const renderedTypeParameters = renderDeclaration(typeParameters, render)

                const name = context.resolveName(node)

                const injectionService = context.lookupService<InjectionService>(injectionServiceKey)
                const heritageInjections = injectionService?.resolveInjections(node, InjectionType.HERITAGE_CLAUSE, context, render)

                const injectedHeritageClauses = heritageInjections
                    ?.filter(Boolean)
                    ?.join(", ")

                // TODO: support template literals
                // TODO: support nullable unions
                const declaration = `
sealed external interface ${name}${ifPresent(renderedTypeParameters, it => `<${it}>`)}${ifPresent(injectedHeritageClauses, it => ` : ${it}`)} {
}
                `.trim()

                const reference = `${name}${ifPresent(renderReference(typeParameters, render), it => `<${it}>`)}`

                return {name, declaration, reference};
            }

            return null
        }
    )

    setup(context: ConverterContext) {
        this.unionService = new UnionService(context)
        context.registerService(unionServiceKey, this.unionService)
    }

    traverse(node: Node, context: ConverterContext) {
        if (
            ts.isUnionTypeNode(node)
            && node.types.every(type => ts.isTypeReferenceNode(type) && !type.typeArguments)

            // ignore parameters, because they are expanded as overloads
            && !ts.isParameter(node.parent)
        ) {
            for (const type of node.types) {
                if (!ts.isTypeReferenceNode(type)) continue

                this.unionService?.register(node, type)
            }
        }
    }

    render(node: Node, context: ConverterContext, next: Render) {
        const anonymousUnionDeclaration = this.anonymousUnionDeclarationPlugin.render(node, context, next)
        if (anonymousUnionDeclaration != null) return anonymousUnionDeclaration

        if (
            ts.isTypeAliasDeclaration(node)
            && ts.isUnionTypeNode(node.type)
            && node.type.types.every(type => ts.isTypeReferenceNode(type) && !type.typeArguments)
        ) {
            const name = next(node.name)

            const typeParameters = node.typeParameters
                ?.map(typeParameter => next(typeParameter))
                ?.filter(Boolean)
                ?.join(", ")

            const injectionService = context.lookupService<InjectionService>(injectionServiceKey)
            const heritageInjections = injectionService?.resolveInjections(node.type, InjectionType.HERITAGE_CLAUSE, context, next)

            const injectedHeritageClauses = heritageInjections
                ?.filter(Boolean)
                ?.join(", ")

            // TODO: support template literals
            // TODO: support nullable unions
            return `
sealed external interface ${name}${ifPresent(typeParameters, it => `<${it}>`)}${ifPresent(injectedHeritageClauses, it => ` : ${it}`)} {
}
            `.trim()
        }

        if (
            ts.isTypeAliasDeclaration(node)
            && ts.isTypeReferenceNode(node.type)
            && !node.type.typeArguments
        ) {
            const name = next(node.name)

            const typeParameters = node.typeParameters
                ?.map(typeParameter => next(typeParameter))
                ?.filter(Boolean)
                ?.join(", ")

            const type = next(node.type)

            const injectionService = context.lookupService<InjectionService>(injectionServiceKey)
            const heritageInjections = injectionService?.resolveInjections(node.type, InjectionType.HERITAGE_CLAUSE, context, next)

            const injectedHeritageClauses = heritageInjections
                ?.filter(Boolean)
                ?.join(", ")

            // TODO: invert logic, inherit type from typealias
            const fullHeritageClauses = [type, injectedHeritageClauses]
                .filter(Boolean)
                .join(", ")

            if (injectedHeritageClauses === "") return null

            // TODO: support template literals
            // TODO: support nullable unions
            return `
sealed external interface ${name}${ifPresent(typeParameters, it => `<${it}>`)}${ifPresent(fullHeritageClauses, it => ` : ${it}`)} {
}
            `.trim()
        }

        return null
    }

    inject(node: Node, context: InjectionContext, render: Render) {
        if (context.type === InjectionType.HERITAGE_CLAUSE) {
            if (ts.isClassDeclaration(node)) {
                const parentNames = this.unionService?.getParents(node) ?? []
                this.unionService?.cover(node)

                return parentNames
            }

            if (ts.isInterfaceDeclaration(node)) {
                const parentNames = this.unionService?.getParents(node) ?? []
                this.unionService?.cover(node)

                return parentNames
            }

            if (ts.isEnumDeclaration(node)) {
                const parentNames = this.unionService?.getParents(node) ?? []
                this.unionService?.cover(node)

                return parentNames
            }

            if (ts.isEnumMember(node)) {
                const parentNames = this.unionService?.getParents(node) ?? []
                this.unionService?.cover(node)

                return parentNames
            }

            if (
                node.parent
                && ts.isTypeAliasDeclaration(node.parent)
                && node.parent.type === node
            ) {
                const parentNames = this.unionService?.getParents(node.parent) ?? []
                this.unionService?.cover(node.parent)

                return parentNames
            }
        }

        return null
    }

    generate(context: ConverterContext, render: Render) {
        const typeScriptService = context.lookupService<TypeScriptService>(typeScriptServiceKey)

        for (const [symbol, parentNames] of this.unionService?.uncoveredUnionParents ?? new Map) {
            const firstDeclaration = symbol.declarations?.[0] as NamedDeclaration | undefined
            const name = firstDeclaration?.name ? typeScriptService?.printNode(firstDeclaration?.name) : "Anonymous"

            console.log(`Declaration ${name} was not handled by Union Injection.`)
            console.log(`Please, consider providing conversions for next types:`)

            for (const parentName of parentNames) {
                console.log(`  ${name} -> ${parentName}`)
            }
        }

        return this.anonymousUnionDeclarationPlugin.generate(context, render)
    }
}
