import ts, {Node} from "typescript";
import {ConverterPlugin} from "../plugin.js";
import {ConverterContext} from "../context.js";
import {Render} from "../render.js";
import {NameResolver} from "../nameResolver.js";
import {defaultNameResolvers} from "../defaultNameResolvers.js";

export const nameResolverServiceKey = Symbol()

export class NameResolverService {
    private readonly nameResolvers: NameResolver[]
    private counter = 0

    constructor(nameResolvers: NameResolver[]) {
        this.nameResolvers = [
            ...nameResolvers,
            ...defaultNameResolvers,
        ]
    }

    resolveName(node: Node, context: ConverterContext): string {
        for (const nameResolver of this.nameResolvers) {
            const result = nameResolver(node, context)

            if (result !== null) return result
        }

        return `Temp${this.counter++}`
    }
}

export class NameResolverPlugin implements ConverterPlugin {
    private readonly nameResolverService: NameResolverService;

    constructor(nameResolvers: NameResolver[]) {
        this.nameResolverService = new NameResolverService(nameResolvers)
    }

    generate(context: ConverterContext): Record<string, string> {
        return {};
    }

    render(node: Node, context: ConverterContext, next: Render): string | null {
        return null;
    }

    traverse(node: ts.Node, context: ConverterContext): void {
    }

    setup(context: ConverterContext): void {
        context.registerService(nameResolverServiceKey, this.nameResolverService)
    }
}
