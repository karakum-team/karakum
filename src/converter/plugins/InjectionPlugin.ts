import ts, {Node} from "typescript";
import {ConverterContext} from "../context.js";
import {Injection, InjectionContext, InjectionType} from "../injection.js";
import {ConverterPlugin} from "../plugin.js";
import {GeneratedFile} from "../generated.js";
import {Render} from "../render.js";

export const injectionServiceKey = Symbol()

export class InjectionService {
    constructor(readonly injections: Injection[]) {
    }

    resolveInjections(
        node: Node,
        type: InjectionType,
        context: ConverterContext,
        render: Render,
    ): string[] {
        const injectionContext: InjectionContext = {
            ...context,
            static: type === InjectionType.STATIC_MEMBER,
            type,
        }

        return this.internalResolveInjections(node, injectionContext, render)
    }

    private internalResolveInjections(
        node: Node,
        context: InjectionContext,
        render: Render
    ) {
        const injections: string[] = []

        for (const injection of this.injections) {
            const result = injection.inject(node, context, render)

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
