import ts, {Node} from "typescript";
import {ConverterContext} from "../context.js";
import {Injection} from "../injection.js";
import {ConverterPlugin} from "../plugin.js";
import {GeneratedFile} from "../generated.js";
import {Render} from "../render.js";

export const injectionServiceKey = Symbol()

export class InjectionService {
    constructor(readonly injections: Injection[]) {
    }

    resolveInjections(node: Node, context: ConverterContext): string[] {
        const injections: string[] = []

        for (const injection of this.injections) {
            const result = injection(node, context)

            if (result !== null) injections.push(result)
        }

        return injections
    }
}

export class InjectionPlugin implements ConverterPlugin {
    private readonly injectionService: InjectionService

    constructor(injections: Injection[]) {
        this.injectionService = new InjectionService(injections)
    }

    generate(): GeneratedFile[] {
        return [];
    }

    render(node: Node, context: ConverterContext, next: Render): string | null {
        return null;
    }

    setup(context: ConverterContext): void {
        context.registerService(injectionServiceKey, this.injectionService)
    }

    traverse(node: ts.Node, context: ConverterContext): void {
    }
}
