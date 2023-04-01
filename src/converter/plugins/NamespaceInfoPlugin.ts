import {ConverterPlugin} from "../plugin";
import {ModuleDeclaration, Node} from "typescript";
import {ConverterContext} from "../context";
import {Render} from "../render";
import {NamespaceStrategy} from "../../configuration/configuration";
import {NamespaceInfo} from "../../structure/namespace/collectNamespaceInfo";
import {extractNamespaceName} from "../../structure/namespace/matchNamespaceStrategy";

export const namespaceInfoServiceKey = Symbol()

export class NamespaceInfoService {
    private readonly namespaceInfo: Record<string, NamespaceStrategy> = {}


    constructor(namespaceInfo: NamespaceInfo) {
        namespaceInfo.forEach(item => {
            this.namespaceInfo[item.name] = item.strategy
        })
    }

    resolveNamespaceStrategy(node: ModuleDeclaration): NamespaceStrategy {
        const name = extractNamespaceName(node)
        const detailedName = name.map(it => it.detailedName).join(".")

        return this.namespaceInfo[detailedName]
    }
}

export class NamespaceInfoPlugin implements ConverterPlugin {
    private readonly namespaceInfoService: NamespaceInfoService;

    constructor(namespaceInfo: NamespaceInfo) {
        this.namespaceInfoService = new NamespaceInfoService(namespaceInfo);
    }

    generate(): Record<string, string> {
        return {};
    }

    render(node: Node, context: ConverterContext, next: Render): string | null {
        return null;
    }

    traverse(node: Node): void {
    }

    setup(context: ConverterContext): void {
        context.registerService(namespaceInfoServiceKey, this.namespaceInfoService)
    }
}
