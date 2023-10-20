import ts, {GetAccessorDeclaration, SetAccessorDeclaration} from "typescript";
import {ConverterPlugin} from "../plugin.js";
import {ConverterContext} from "../context.js";
import {ifPresent, Render} from "../render.js";
import {CheckCoverageService, checkCoverageServiceKey} from "./CheckCoveragePlugin.js";
import {TypeScriptService, typeScriptServiceKey} from "./TypeScriptPlugin.js";
import {GeneratedFile} from "../generated.js";
import {InheritanceModifierService, inheritanceModifierServiceKey} from "./InheritanceModifierPlugin.js";

interface AccessorInfo {
    getter: GetAccessorDeclaration | undefined
    setter: SetAccessorDeclaration | undefined
}

const emptyAccessorInfo: AccessorInfo = {
    getter: undefined,
    setter: undefined,
}

export class AccessorsPlugin implements ConverterPlugin {
    private accessors = new Map<ts.Symbol, AccessorInfo>()
    private coveredAccessors = new Set<ts.Symbol>()

    setup(context: ConverterContext): void {
    }

    traverse(node: ts.Node, context: ConverterContext): void {
        if (ts.isGetAccessor(node) || ts.isSetAccessor(node)) {
            const typeScriptService = context.lookupService<TypeScriptService>(typeScriptServiceKey)
            const typeChecker = typeScriptService?.program.getTypeChecker()

            const symbol = typeChecker?.getSymbolAtLocation(node.name)
            if (!symbol) return

            const accessorInfo = this.accessors.get(symbol) ?? {...emptyAccessorInfo}

            if (!this.accessors.has(symbol)) {
                this.accessors.set(symbol, accessorInfo)
            }

            if (ts.isGetAccessor(node)) {
                accessorInfo.getter = node
            }

            if (ts.isSetAccessor(node)) {
                accessorInfo.setter = node
            }
        }
    }

    render(node: ts.Node, context: ConverterContext, next: Render): string | null {
        if (ts.isSetAccessor(node) || ts.isGetAccessor(node)) {
            const checkCoverageService = context.lookupService<CheckCoverageService>(checkCoverageServiceKey)
            const typeScriptService = context.lookupService<TypeScriptService>(typeScriptServiceKey)
            const inheritanceModifierService = context.lookupService<InheritanceModifierService>(inheritanceModifierServiceKey)

            const inheritanceModifier = inheritanceModifierService?.resolveInheritanceModifier(node, context)

            const typeChecker = typeScriptService?.program.getTypeChecker()

            const symbol = typeChecker?.getSymbolAtLocation(node.name)
            if (!symbol) return null

            if (this.coveredAccessors.has(symbol)) {
                checkCoverageService?.deepCover(node)
                return ""
            }

            checkCoverageService?.cover(node)

            if (ts.isSetAccessor(node)) {
                checkCoverageService?.deepCover(node.parameters[0])
            }

            this.coveredAccessors.add(symbol)

            const accessorInfo = this.accessors.get(symbol) ?? {...emptyAccessorInfo}

            const modifier = accessorInfo.setter !== undefined
                ? "var "
                : "val "

            const name = next(node.name)

            let type: string

            if (accessorInfo.getter && accessorInfo.getter.type) {
                type = next(accessorInfo.getter.type)
            } else if (accessorInfo.setter && accessorInfo.setter.parameters[0].type) {
                type = next(accessorInfo.setter.parameters[0].type)
            } else {
                type = "Any? /* type isn't declared */"
            }

            return `
${ifPresent(inheritanceModifier, it => `${it} `)}${modifier}${name}: ${type}
            `;
        }

        return null
    }

    generate(): GeneratedFile[] {
        return [];
    }
}
