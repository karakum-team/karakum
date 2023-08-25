import path from "node:path";
import ts, {ModuleDeclaration, Node} from "typescript";
import {ConverterPlugin} from "../plugin.js";
import {ConverterContext} from "../context.js";
import {Render} from "../render.js";
import {createGeneratedFile} from "../../structure/createGeneratedFile.js";
import {ConfigurationService, configurationServiceKey} from "./ConfigurationPlugin.js";
import {NameResolverService, nameResolverServiceKey} from "./NameResolverPlugin.js";
import {createNamespaceInfoItem} from "../../structure/namespace/createNamespaceInfoItem.js";
import {createSourceFileInfoItem} from "../../structure/sourceFile/createSourceFileInfoItem.js";
import {normalizeStructure} from "../../structure/normalizeStructure.js";
import {StructureItem} from "../../structure/structure.js";
import {applyPackageNameMapper} from "../../structure/package/applyPackageNameMapper.js";
import {packageToOutputFileName} from "../../structure/package/packageToFileName.js";
import {createBundleInfoItem} from "../../structure/bundle/createBundleInfoItem.js";
import {findClosest} from "../../utils/findClosest.js";
import {NamespaceInfoService, namespaceInfoServiceKey} from "./NamespaceInfoPlugin.js";

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

export interface AnonymousStructureItem  extends StructureItem {
    declaration: string,
}

class AnonymousDeclarationPlugin<TNode extends Node = Node> implements ConverterPlugin {
    private readonly generated: {
        name: string,
        sourceFileName: string,
        namespace: ModuleDeclaration | undefined,
        declaration: string,
    }[] = []

    constructor(
        private anonymousDeclarationRender: AnonymousDeclarationRender,
    ) {
    }

    generate(context: ConverterContext): Record<string, string> {
        const configurationService = context.lookupService<ConfigurationService>(configurationServiceKey)
        const configuration = configurationService?.configuration
        if (configuration === undefined) throw new Error("AnonymousDeclarationPlugin can't work without ConfigurationService")

        const namespaceInfoService = context.lookupService<NamespaceInfoService>(namespaceInfoServiceKey)

        const {output, granularity} = configuration

        const structureItems: AnonymousStructureItem[] = this.generated.map(generatedInfo => {
            const {name, sourceFileName, namespace, declaration} = generatedInfo

            if (granularity === "bundle") {
                const item = createBundleInfoItem(configuration)

                return {
                    ...item,
                    declaration,
                }
            }

            let item: StructureItem

            if (namespace && namespaceInfoService?.resolveNamespaceStrategy(namespace) === "package") {
                item = createNamespaceInfoItem(namespace, sourceFileName, configuration)
            } else {
                item = createSourceFileInfoItem(sourceFileName, configuration)
            }

            if (granularity === "top-level") {
                return {
                    ...item,
                    fileName: `${name}.kt`,
                    declaration,
                }
            } else {
                return {
                    ...item,
                    declaration,
                }
            }
        })

        const normalizedStructureItems = normalizeStructure(structureItems, (item, other) => ({
            ...item,
            declaration: `${item.declaration}\n\n${other.declaration}`
        }))

        return Object.fromEntries(
            normalizedStructureItems.map(item => {
                const packageMappingResult = applyPackageNameMapper(
                    item.package,
                    item.fileName,
                    configuration,
                )

                const outputFileName = packageToOutputFileName(
                    packageMappingResult.package,
                    packageMappingResult.fileName,
                    configuration,
                )

                const generated = granularity === "top-level"
                    ? createGeneratedFile(
                        packageMappingResult.package,
                        packageMappingResult.fileName,
                        item.declaration,
                        configuration,
                    )
                    : item.declaration

                return [path.resolve(output, outputFileName), generated]
            })
        )
    }

    render(node: ts.Node, context: ConverterContext, next: Render): string | null {
        const nameResolverService = context.lookupService<NameResolverService>(nameResolverServiceKey)
        if (nameResolverService === undefined) throw new Error("AnonymousDeclarationPlugin can't work without NameResolverService")

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
        const {name, declaration, reference} = result

        const namespace = findClosest(node, ts.isModuleDeclaration)

        this.generated.push({
            name,
            sourceFileName,
            declaration,
            namespace,
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
