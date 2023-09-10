import {ConverterPlugin} from "../plugin.js";
import ts, {Node} from "typescript";
import {ConverterContext} from "../context.js";
import {Render} from "../render.js";
import {InheritanceModifier} from "../inheritanceModifier.js";
import {GeneratedFile} from "../generated.js";

export const inheritanceModifierServiceKey = Symbol()

export class InheritanceModifierService {
    constructor(private readonly inheritanceModifiers: InheritanceModifier[]) {
    }

    resolveInheritanceModifier(node: Node, context: ConverterContext): string | null {
        for (const inheritanceModifier of this.inheritanceModifiers) {
            const result = inheritanceModifier(node, context)

            if (result !== null) return result
        }

        return null
    }
}

export class InheritanceModifierPlugin implements ConverterPlugin {
    private readonly inheritanceModifierService: InheritanceModifierService;

    constructor(inheritanceModifiers: InheritanceModifier[]) {
        this.inheritanceModifierService = new InheritanceModifierService(inheritanceModifiers)
    }

    generate(): GeneratedFile[] {
        return [];
    }

    render(node: Node, context: ConverterContext, next: Render): string | null {
        return null;
    }

    traverse(node: ts.Node, context: ConverterContext): void {
    }

    setup(context: ConverterContext): void {
        context.registerService(inheritanceModifierServiceKey, this.inheritanceModifierService)
    }
}
