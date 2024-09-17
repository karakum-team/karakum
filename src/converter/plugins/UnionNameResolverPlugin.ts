import ts, {Node} from "typescript";
import {ConverterPlugin} from "../plugin.js";
import {ConverterContext} from "../context.js";
import {Render} from "../render.js";
import {UnionNameResolver} from "../unionNameResolver.js";
import {GeneratedFile} from "../generated.js";

export const unionNameResolverServiceKey = Symbol()

export class UnionNameResolverService {
    private readonly unionNameResolvers: UnionNameResolver[]
    private readonly resolvedNodes = new Map<Node, string>()

    constructor(unionNameResolvers: UnionNameResolver[]) {
        this.unionNameResolvers = unionNameResolvers
    }

    resolveUnionName(node: Node, context: ConverterContext): string | null {
        const resolvedName = this.resolvedNodes.get(node)
        if (resolvedName) return resolvedName

        for (const unionNameResolver of this.unionNameResolvers) {
            const result = unionNameResolver(node, context)

            if (result !== null) {
                this.resolvedNodes.set(node, result)
                return result
            }
        }

        return null
    }
}

export class UnionNameResolverPlugin implements ConverterPlugin {
    private readonly unionNameResolverService: UnionNameResolverService;

    constructor(unionNameResolvers: UnionNameResolver[]) {
        this.unionNameResolverService = new UnionNameResolverService(unionNameResolvers)
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
        context.registerService(unionNameResolverServiceKey, this.unionNameResolverService)
    }
}
