import {ConverterPlugin} from "../plugin";
import ts, {Node} from "typescript";
import {ConverterContext} from "../context";
import {Render} from "../render";
import {NameResolver} from "../nameResolver";

export class JsNamePlugin implements ConverterPlugin {
    private readonly coveredJsNames = new Set<Node>()

    private readonly jsNameResolvers: NameResolver[]

    constructor(jsNameResolvers: NameResolver[]) {
        this.jsNameResolvers = jsNameResolvers
    }

    generate(context: ConverterContext): Record<string, string> {
        return {};
    }

    render(node: ts.Node, context: ConverterContext, next: Render): string | null {
        if (this.coveredJsNames.has(node)) return null
        this.coveredJsNames.add(node)

        const resolvedName = this.resolveName(node, context)

        if (resolvedName !== null) {
            return `
@JsName("${resolvedName}")
${next(node)}
            `;
        }

        return null
    }

    setup(context: ConverterContext): void {
    }

    traverse(node: ts.Node, context: ConverterContext): void {
    }

    private resolveName(node: Node, context: ConverterContext): string | null {
        for (const nameResolver of this.jsNameResolvers) {
            const result = nameResolver(node, context)

            if (result !== null) return result
        }

        return null
    }
}
