import ts, {Node, Program, TypeNode} from "typescript";
import {ConverterPlugin} from "../plugin.js";
import {ConverterContext} from "../context.js";
import {Render} from "../render.js";
import {setParentNodes} from "../../utils/setParentNodes.js";

export const typeScriptServiceKey = Symbol()

export class TypeScriptService {
    private readonly virtualSourceFile = ts.createSourceFile("virtual.d.ts", "", ts.ScriptTarget.Latest)
    private readonly printer = ts.createPrinter({
        removeComments: true,
        newLine: ts.NewLineKind.LineFeed,
    })

    constructor(public readonly program: Program) {
    }

    printNode(node: Node) {
        const sourceFile = node.getSourceFile() ?? this.virtualSourceFile

        return this.printer.printNode(ts.EmitHint.Unspecified, node, sourceFile)
    }

    resolveType(node: TypeNode) {
        const typeChecker = this.program.getTypeChecker()
        const type = typeChecker.getTypeAtLocation(node)
        const typeNode = typeChecker.typeToTypeNode(type, undefined, ts.NodeBuilderFlags.NoTruncation)
        return typeNode && setParentNodes(typeNode)
    }
}

export class TypeScriptPlugin implements ConverterPlugin {
    private readonly typeScriptService: TypeScriptService;

    constructor(program: Program) {
        this.typeScriptService = new TypeScriptService(program);
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
        context.registerService(typeScriptServiceKey, this.typeScriptService)
    }
}
