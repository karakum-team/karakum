import ts, {LiteralTypeNode, Node, NumericLiteral, PrefixUnaryExpression, SyntaxKind, UnionTypeNode} from "typescript";
import {escapeIdentifier} from "../../utils/strings.js";
import {CheckCoverageService, checkCoverageServiceKey} from "./CheckCoveragePlugin.js";
import {UnionNameResolverService, unionNameResolverServiceKey} from "./UnionNameResolverPlugin.js";
import {ConverterContext} from "../context.js";
import {createAnonymousDeclarationPlugin} from "./AnonymousDeclarationPlugin.js";
import {flatUnionTypes, isNullableType} from "./NullableUnionTypePlugin.js";
import {ifPresent, Render} from "../render.js";
import {InjectionService, injectionServiceKey} from "./InjectionPlugin.js";
import {InjectionType} from "../injection.js";
import {TypeScriptService, typeScriptServiceKey} from "./TypeScriptPlugin.js";
import {NamespaceInfoService, namespaceInfoServiceKey} from "./NamespaceInfoPlugin.js";

export function isNumericUnionType(node: ts.Node, context: ConverterContext): node is UnionTypeNode {
    return (
        ts.isUnionTypeNode(node)
        && flatUnionTypes(node, context).every(type => (
            ts.isLiteralTypeNode(type)
            && (ts.isNumericLiteral(type.literal)
                || (ts.isPrefixUnaryExpression(type.literal)
                    && ts.isNumericLiteral(type.literal.operand)))
        ))
    )
}

export function isNullableNumericUnionType(node: ts.Node, context: ConverterContext): node is UnionTypeNode {
    if (!ts.isUnionTypeNode(node)) return false

    const types = flatUnionTypes(node, context)
    const nonNullableTypes = types.filter(type => !isNullableType(type))

    return (
        types.every(type => (
            isNullableType(type)
            || (
                ts.isLiteralTypeNode(type)
                && (ts.isNumericLiteral(type.literal)
                    || (ts.isPrefixUnaryExpression(type.literal)
                        && ts.isNumericLiteral(type.literal.operand)))
            )
        ))
        && nonNullableTypes.length > 1
    )
}

export function convertNumericUnionType(
    node: UnionTypeNode,
    name: string,
    isInlined: boolean,
    context: ConverterContext,
    render: Render,
) {
    const checkCoverageService = context.lookupService<CheckCoverageService>(checkCoverageServiceKey)
    checkCoverageService?.cover(node)

    const unionNameResolverService = context.lookupService<UnionNameResolverService>(unionNameResolverServiceKey)
    if (unionNameResolverService === undefined) throw new Error("UnionNameResolverService required")
    const typeScriptService = context.lookupService<TypeScriptService>(typeScriptServiceKey)
    if (typeScriptService === undefined) throw new Error("TypeScriptService required")
    const namespaceInfoService = context.lookupService<NamespaceInfoService>(namespaceInfoServiceKey)
    const injectionService = context.lookupService<InjectionService>(injectionServiceKey)

    const types = flatUnionTypes(node, context)

    const nonNullableTypes = types.filter(type => !isNullableType(type))
    const nullableTypes = types.filter(type => isNullableType(type))

    const entries = nonNullableTypes
        .filter((type): type is LiteralTypeNode => ts.isLiteralTypeNode(type))
        .map(type => {
            checkCoverageService?.cover(type)

            return type.literal
        })
        .filter((literal): literal is NumericLiteral | PrefixUnaryExpression =>
            ts.isNumericLiteral(literal)
            || (ts.isPrefixUnaryExpression(literal)
                && ts.isNumericLiteral(literal.operand))
        )
        .map(literal => {
            checkCoverageService?.cover(literal)

            const value = typeScriptService.printNode(literal)

            let key = unionNameResolverService.resolveUnionName(literal, context)

            if (key) {
                return [key, value] as const
            }

            if (ts.isNumericLiteral(literal)) {
                return [`VALUE_${toIdentifierPart(typeScriptService, literal)}`, value] as const
            } else {
                return [`VALUE_MINUS_${toIdentifierPart(typeScriptService, literal.operand)}`, value] as const
            }
        })

    const keyDisambiguators: Map<string, number> = new Map()
    const disambiguatedEntries = entries.map(([key, value]) => {
        const keyDisambiguator = (keyDisambiguators.get(key) ?? 0) + 1
        keyDisambiguators.set(key, keyDisambiguator)
        if (keyDisambiguator > 1) {
            return [escapeIdentifier(`${key}_${keyDisambiguator}`), value] as const;
        } else {
            return [escapeIdentifier(key), value] as const;
        }
    });

    const body = disambiguatedEntries
        .map(([key, value]) => (
            `
@seskar.js.${value.match(/^-?\d+$/) ? `JsIntValue(${value})` : `JsValue("${value}")`}
val ${key}: ${name}
            `.trim()
        ))
        .join("\n")

    const heritageInjections = injectionService?.resolveInjections(node, InjectionType.HERITAGE_CLAUSE, context, render)

    const namespace = typeScriptService.findClosest(node, ts.isModuleDeclaration)

    let externalModifier = "external "

    if (isInlined && namespace !== undefined && namespaceInfoService?.resolveNamespaceStrategy(namespace) === "object") {
        externalModifier = ""
    }

    const injectedHeritageClauses = heritageInjections
        ?.filter(Boolean)
        ?.join(", ")

    const declaration = `
sealed ${externalModifier}interface ${name}${ifPresent(injectedHeritageClauses, it => ` : ${it}`)} {
companion object {
${body}
}
}
    `.trim()

    const nullable = nullableTypes.length > 0

    return {
        declaration,
        nullable,
    }
}

function toIdentifierPart(typeScriptService: TypeScriptService, node: Node): string {
    return typeScriptService.printNode(node).replaceAll(".", "_")
}

export const numericUnionTypePlugin = createAnonymousDeclarationPlugin(
    (node, context, render) => {
        if (!isNullableNumericUnionType(node, context)) return null

        const name = context.resolveName(node)

        const {declaration, nullable} = convertNumericUnionType(node, name, false, context, render)

        const reference = nullable ? `${name}?` : name

        return {name, declaration, reference};
    }
)
