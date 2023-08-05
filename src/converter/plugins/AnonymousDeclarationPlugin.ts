import path from "node:path";
import ts, {Node} from "typescript";
import {ConverterPlugin} from "../plugin.js";
import {ConverterContext} from "../context.js";
import {Render} from "../render.js";
import {createGeneratedFile} from "../../structure/createGeneratedFile.js";
import {ConfigurationService, configurationServiceKey} from "./ConfigurationPlugin.js";
import {NameResolverService, nameResolverServiceKey} from "./NameResolverPlugin.js";
import {createSourceFileInfoItem} from "../../structure/sourceFile/createSourceFileInfoItem.js";
import {applyPackageNameMapper} from "../../structure/package/applyPackageNameMapper.js";
import {packageToOutputFileName} from "../../structure/package/packageToFileName.js";
import {createBundleInfoItem} from "../../structure/bundle/createBundleInfoItem.js";

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
    private readonly generated: Record<string, { name: string, declaration: string }[]> = {}

    constructor(
        private anonymousDeclarationRender: AnonymousDeclarationRender,
    ) {
    }

    generate(context: ConverterContext): Record<string, string> {
        const configurationService = context.lookupService<ConfigurationService>(configurationServiceKey)
        const configuration = configurationService?.configuration
        if (configuration === undefined) throw new Error("AnonymousDeclarationPlugin can't work without ConfigurationService")

        const {output, granularity} = configuration

        return Object.fromEntries(
            Object.entries(this.generated)
                .flatMap(([sourceFileName, declarations]) => {
                    const sourceFileInfoItem = createSourceFileInfoItem(
                        sourceFileName,
                        configuration,
                    )

                    if (granularity === "top-level") {
                        return declarations
                            .map(({name, declaration}) => {
                                const fileName = `${name}.kt`

                                const packageMappingResult = applyPackageNameMapper(
                                    sourceFileInfoItem.package,
                                    fileName,
                                    configuration,
                                )

                                const outputFileName = packageToOutputFileName(
                                    packageMappingResult.package,
                                    fileName,
                                    configuration,
                                )

                                const generatedFile = createGeneratedFile(
                                    packageMappingResult.package,
                                    packageMappingResult.fileName,
                                    declaration,
                                    configuration,
                                )

                                return [path.resolve(output, outputFileName), generatedFile];
                            })
                    } else if (granularity === "bundle") {
                        const bundleItem = createBundleInfoItem(configuration)

                        const generatedBody = declarations
                            .map(({declaration}) => declaration)
                            .join("\n\n")

                        const packageMappingResult = applyPackageNameMapper(
                            bundleItem.package,
                            bundleItem.fileName,
                            configuration,
                        )

                        const outputFileName = packageToOutputFileName(
                            packageMappingResult.package,
                            packageMappingResult.fileName,
                            configuration,
                        )

                        return [[path.resolve(output, outputFileName), generatedBody]];
                    } else {
                        const generatedBody = declarations
                            .map(({declaration}) => declaration)
                            .join("\n\n")

                        const packageMappingResult = applyPackageNameMapper(
                            sourceFileInfoItem.package,
                            sourceFileInfoItem.fileName,
                            configuration,
                        )

                        const outputFileName = packageToOutputFileName(
                            packageMappingResult.package,
                            packageMappingResult.fileName,
                            configuration,
                        )

                        return [[path.resolve(output, outputFileName), generatedBody]];
                    }
                })
        );
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

        const fileName = node.getSourceFile()?.fileName ?? "generated.d.ts"
        const {name, declaration, reference} = result

        const generatedDeclarations = this.generated[fileName] ?? []

        generatedDeclarations.push({
            name,
            declaration,
        })

        this.generated[fileName] = generatedDeclarations

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
