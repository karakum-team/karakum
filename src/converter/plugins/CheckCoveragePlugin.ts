import {ConverterPlugin} from "../plugin.js";
import ts, {Node} from "typescript";
import {Render} from "../render.js";
import {ConverterContext} from "../context.js";
import {traverse} from "../../utils/traverse.js";
import {ConfigurationService, configurationServiceKey} from "./ConfigurationPlugin.js";
import {TypeScriptService, typeScriptServiceKey} from "./TypeScriptPlugin.js";

export const checkCoverageServiceKey = Symbol()

export class CheckCoverageService {
    private coveredNodes = new Set<Node>()

    private allNodes = new Set<Node>()

    register(node: Node) {
        this.allNodes.add(node)
    }

    cover(node: Node) {
        this.coveredNodes.add(node)
    }

    deepCover(node: Node) {
        traverse(node, it => this.cover(it))
    }

    emit(callback: (uncoveredNode: Node) => void): { coveredNodes: number, uncoveredNodes: number } {
        for (const node of this.allNodes){
            if (!this.coveredNodes.has(node)) {
                callback(node);
            }
        }

        const result = {
            coveredNodes: this.coveredNodes.size,
            uncoveredNodes: this.allNodes.size - this.coveredNodes.size,
        }

        this.coveredNodes = new Set<Node>()
        this.allNodes = new Set<Node>()

        return result
    }
}

export class CheckCoveragePlugin implements ConverterPlugin {
    private readonly checkCoverageService = new CheckCoverageService()

    generate(context: ConverterContext): Record<string, string> {
        const configurationService = context.lookupService<ConfigurationService>(configurationServiceKey)
        const typeScriptService = context.lookupService<TypeScriptService>(typeScriptServiceKey)

        const {coveredNodes, uncoveredNodes} = this.checkCoverageService.emit( uncoveredNode => {
            if (configurationService?.configuration?.verbose) {
                const message = `Node with kind ${ts.SyntaxKind[uncoveredNode.kind]} is uncovered`
                const sourceFile = uncoveredNode.getSourceFile()

                if (sourceFile) {
                    const {line, character} = sourceFile.getLineAndCharacterOfPosition(uncoveredNode.pos)

                    console.error(`${sourceFile.fileName}: (${line + 1}, ${character + 1}): ${message}`)
                } else {
                    console.error(message)
                }

                console.error("--- Node Start ---");
                console.error(typeScriptService?.printNode(uncoveredNode));
                console.error("--- Node End ---");

                console.error();
            }
        })

        console.log(`Covered nodes: ${coveredNodes}`)
        console.log(`Uncovered nodes: ${uncoveredNodes}`)

        return {};
    }

    render(node: Node, context: ConverterContext, next: Render): string | null {
        return null;
    }

    traverse(node: Node): void {
        this.checkCoverageService.register(node)
    }

    setup(context: ConverterContext): void {
        context.registerService(checkCoverageServiceKey, this.checkCoverageService)
    }
}
