import ts, {Node} from "typescript";
import {ConverterPlugin} from "../plugin.js";
import {ConverterContext} from "../context.js";
import {Render} from "../render.js";
import {ConfigurationService, configurationServiceKey} from "./ConfigurationPlugin.js";
import {NameResolverService, nameResolverServiceKey} from "./NameResolverPlugin.js";
import {NamespaceInfoService, namespaceInfoServiceKey} from "./NamespaceInfoPlugin.js";
import {generateDerivedDeclarations} from "../../structure/derived/generateDerivedDeclarations.js";
import {DerivedDeclaration} from "../../structure/derived/derivedDeclaration.js";
import {TypeScriptService, typeScriptServiceKey} from "./TypeScriptPlugin.js";
import {DerivedFile} from "../generated.js";

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
    private readonly generated: DerivedDeclaration[] = []

    constructor(
        private anonymousDeclarationRender: AnonymousDeclarationRender,
    ) {
    }

    generate(context: ConverterContext): DerivedFile[] {
        const configurationService = context.lookupService<ConfigurationService>(configurationServiceKey)
        const configuration = configurationService?.configuration
        if (configuration === undefined) throw new Error("AnonymousDeclarationPlugin can't work without ConfigurationService")

        const namespaceInfoService = context.lookupService<NamespaceInfoService>(namespaceInfoServiceKey)
        const resolveNamespaceStrategy = namespaceInfoService?.resolveNamespaceStrategy?.bind(namespaceInfoService)
        if (resolveNamespaceStrategy === undefined) throw new Error("AnonymousDeclarationPlugin can't work without NamespaceInfoService")

        return generateDerivedDeclarations(this.generated, configuration, resolveNamespaceStrategy)
    }

    render(node: ts.Node, context: ConverterContext, next: Render): string | null {
        const nameResolverService = context.lookupService<NameResolverService>(nameResolverServiceKey)
        if (nameResolverService === undefined) throw new Error("AnonymousDeclarationPlugin can't work without NameResolverService")

        const typeScriptService = context.lookupService<TypeScriptService>(typeScriptServiceKey)
        if (typeScriptService === undefined) throw new Error("AnonymousDeclarationPlugin can't work without TypeScriptService")

        const resolveName = (node: TNode) => nameResolverService.resolveName(node, context)

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

        this.generated.push({
            sourceFileName,
            namespace,
            fileName: `${name}.kt`,
            body: declaration,
        })

        return reference;
    }

    setup(context: ConverterContext): void {
    }

    traverse(node: ts.Node, context: ConverterContext): void {
    }
}

export function createAnonymousDeclarationPlugin<TNode extends Node = Node>(
    render: AnonymousDeclarationRender,
): ConverterPlugin {
    return new AnonymousDeclarationPlugin<TNode>(render)
}
