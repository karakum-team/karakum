import ts, {Node} from "typescript";
import {ConverterPlugin} from "../plugin.js";
import {ConverterContext} from "../context.js";
import {Render} from "../render.js";
import {NameResolver} from "../nameResolver.js";
import {defaultNameResolvers} from "../defaultNameResolvers.js";
import {GeneratedFile} from "../generated.js";

export const nameResolverServiceKey = Symbol()

export class NameResolverService {
    private readonly nameResolvers: NameResolver[]
    private readonly resolvedNodes = new Map<Node, string>()
    private counter = 0

    constructor(nameResolvers: NameResolver[]) {
        this.nameResolvers = [
            ...nameResolvers,
            ...defaultNameResolvers,
        ]
    }

    tryResolveName(node: Node, context: ConverterContext): string | undefined {
        for (const nameResolver of this.nameResolvers) {
            const result = nameResolver(node, context)

            if (result !== null) {
                this.resolvedNodes.set(node, result)
                return result
            }
        }
    }

    resolveName(node: Node, context: ConverterContext): string {
        const resolvedName = this.resolvedNodes.get(node)
        if (resolvedName) return resolvedName

        const result = this.tryResolveName(node, context) ?? `Temp${this.counter++}`

        this.resolvedNodes.set(node, result)
        return result
    }
}

export class NameResolverPlugin implements ConverterPlugin {
    private readonly nameResolverService: NameResolverService;

    constructor(nameResolvers: NameResolver[]) {
        this.nameResolverService = new NameResolverService(nameResolvers)
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
        context.registerService(nameResolverServiceKey, this.nameResolverService)
    }
}
