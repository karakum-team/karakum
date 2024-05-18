import {ConverterPlugin} from "../plugin.js";
import ts, {Node} from "typescript";
import {ConverterContext} from "../context.js";
import {Render} from "../render.js";
import {GeneratedFile} from "../generated.js";
import {VarianceModifier} from "../varianceModifier.js";

export const varianceModifierServiceKey = Symbol()

export class VarianceModifierService {
    constructor(private readonly varianceModifiers: VarianceModifier[]) {
    }

    resolveVarianceModifier(
        node: Node,
        context: ConverterContext,
    ): string | null {
        for (const varianceModifier of this.varianceModifiers) {
            const result = varianceModifier(node, context)

            if (result !== null) return result
        }

        return null
    }
}

export class VarianceModifierPlugin implements ConverterPlugin {
    private readonly varianceModifierService: VarianceModifierService;

    constructor(varianceModifiers: VarianceModifier[]) {
        this.varianceModifierService = new VarianceModifierService(varianceModifiers)
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
        context.registerService(varianceModifierServiceKey, this.varianceModifierService)
    }
}
