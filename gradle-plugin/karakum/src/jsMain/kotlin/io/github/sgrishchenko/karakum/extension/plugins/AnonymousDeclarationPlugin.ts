import ts, {Node} from "typescript";
import {ConverterPlugin} from "../plugin.js";
import {ConverterContext} from "../context.js";
import {Render} from "../render.js";
import {NameResolverService, nameResolverServiceKey} from "./NameResolverPlugin.js";
import {generateDerivedDeclarations} from "../../structure/derived/generateDerivedDeclarations.js";
import {DerivedDeclaration} from "../../structure/derived/derivedDeclaration.js";
import {TypeScriptService, typeScriptServiceKey} from "./TypeScriptPlugin.js";
import {DerivedFile} from "../generated.js";
import {AnnotationService, annotationServiceKey} from "./AnnotationPlugin.js";

export interface AnonymousDeclarationContext extends ConverterContext {
    resolveName(node: Node): string
}

export interface AnonymousDeclarationRenderResult {
    name: string
    declaration: string
    reference: string
}

export type AnonymousDeclarationRender = (
    node: ts.Node,
    context: AnonymousDeclarationContext,
    render: Render,
) => AnonymousDeclarationRenderResult | string | null

class AnonymousDeclarationPlugin<TNode extends Node = Node> implements ConverterPlugin {
    private readonly generated: Map<Node, DerivedDeclaration> = new Map()

    constructor(
        private anonymousDeclarationRender: AnonymousDeclarationRender,
    ) {
    }

    setup(context: ConverterContext): void {
    }

    traverse(node: ts.Node, context: ConverterContext): void {
    }

    render(node: ts.Node, context: ConverterContext, next: Render): string | null {
        const nameResolverService = context.lookupService<NameResolverService>(nameResolverServiceKey)
        if (nameResolverService === undefined) throw new Error("AnonymousDeclarationPlugin can't work without NameResolverService")

        const typeScriptService = context.lookupService<TypeScriptService>(typeScriptServiceKey)
        if (typeScriptService === undefined) throw new Error("AnonymousDeclarationPlugin can't work without TypeScriptService")

        const annotationService = context.lookupService<AnnotationService>(annotationServiceKey)
        if (annotationService === undefined) throw new Error("AnonymousDeclarationPlugin can't work without AnnotationService")

        const resolveName = (node: TNode) => nameResolverService.resolveName(node, context)
        const annotations = annotationService.resolveAnonymousAnnotations(node, context)

        const anonymousDeclarationContext = {
            ...context,
            resolveName,
        }

        const result = this.anonymousDeclarationRender(
            node,
            anonymousDeclarationContext,
            next
        )

        if (result === null || typeof result === "string") return result

        const sourceFileName = node.getSourceFile()?.fileName ?? "generated.d.ts"
        const namespace = typeScriptService.findClosest(node, ts.isModuleDeclaration)

        const {name, declaration, reference} = result

        this.generated.set(node, {
            sourceFileName,
            namespace,
            fileName: `${name}.kt`,
            body: [...annotations, declaration].join("\n"),
        })

        return reference;
    }

    generate(context: ConverterContext): DerivedFile[] {
        return generateDerivedDeclarations(Array.from(this.generated.values()), context)
    }
}

export function createAnonymousDeclarationPlugin<TNode extends Node = Node>(
    render: AnonymousDeclarationRender,
): ConverterPlugin {
    return new AnonymousDeclarationPlugin<TNode>(render)
}
