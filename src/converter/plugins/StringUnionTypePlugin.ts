import ts, {LiteralTypeNode, StringLiteral, UnionTypeNode} from "typescript";
import {escapeIdentifier, notEscapedIdentifier} from "../../utils/strings.js";
import {CheckCoverageService, checkCoverageServiceKey} from "./CheckCoveragePlugin.js";
import {ConverterContext} from "../context.js";
import {createAnonymousDeclarationPlugin} from "./AnonymousDeclarationPlugin.js";
import {flatUnionTypes, isNullableType} from "./NullableUnionTypePlugin.js";
import {ifPresent, Render} from "../render.js";
import {InjectionService, injectionServiceKey} from "./InjectionPlugin.js";
import {InjectionType} from "../injection.js";
import {TypeScriptService, typeScriptServiceKey} from "./TypeScriptPlugin.js";
import {NamespaceInfoService, namespaceInfoServiceKey} from "./NamespaceInfoPlugin.js";

export function isStringUnionType(node: ts.Node, context: ConverterContext): node is UnionTypeNode {
    return (
        ts.isUnionTypeNode(node)
        && flatUnionTypes(node, context).every(type => (
            ts.isLiteralTypeNode(type)
            && ts.isStringLiteral(type.literal)
        ))
    )
}

export function isNullableStringUnionType(node: ts.Node, context: ConverterContext): node is UnionTypeNode {
    if (!ts.isUnionTypeNode(node)) return false

    const types = flatUnionTypes(node, context)
    const nonNullableTypes = types.filter(type => !isNullableType(type))

    return (
        types.every(type => (
            isNullableType(type)
            || (
                ts.isLiteralTypeNode(type)
                && ts.isStringLiteral(type.literal)
            )
        ))
        && nonNullableTypes.length > 1
    )
}

export function convertStringUnionType(
    node: UnionTypeNode,
    name: string,
    isInlined: boolean,
    context: ConverterContext,
    render: Render,
) {
    const checkCoverageService = context.lookupService<CheckCoverageService>(checkCoverageServiceKey)
    checkCoverageService?.cover(node)

    const typeScriptService = context.lookupService<TypeScriptService>(typeScriptServiceKey)
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
        .filter((literal): literal is StringLiteral => ts.isStringLiteral(literal))
        .map(literal => {
            checkCoverageService?.cover(literal)

            const value = literal.text
            const valueAsIdentifier = notEscapedIdentifier(value)
            const key = (value === "") || (valueAsIdentifier === "")
                ? "_"
                : valueAsIdentifier
            return [key, value] as const
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
@seskar.js.JsValue("${value}")
val ${key}: ${name}
            `.trim()
        ))
        .join("\n")

    const heritageInjections = injectionService?.resolveInjections(node, InjectionType.HERITAGE_CLAUSE, context, render)

    const namespace = typeScriptService?.findClosest(node, ts.isModuleDeclaration)

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

export const stringUnionTypePlugin = createAnonymousDeclarationPlugin(
    (node, context, render) => {
        if (!isNullableStringUnionType(node, context)) return null

        const name = context.resolveName(node)

        const {declaration, nullable} = convertStringUnionType(node, name, false, context, render)

        const reference = nullable ? `${name}?` : name

        return {name, declaration, reference};
    }
)
