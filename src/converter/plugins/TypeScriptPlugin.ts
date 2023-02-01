import ts, {EmitHint, Node, Program, ScriptTarget} from "typescript";
import {ConverterPlugin} from "../plugin";
import {ConverterContext} from "../context";
import {Render} from "../render";

export const typeScriptServiceKey = Symbol()

export class TypeScriptService {
    private virtualSourceFile = ts.createSourceFile("virtual.d.ts", "", ScriptTarget.Latest)
    private readonly printer = ts.createPrinter({
        removeComments: true,
        newLine: ts.NewLineKind.LineFeed,
    })

    constructor(public readonly program: Program) {
    }

    printNode(node: Node) {
        const sourceFile = node.getSourceFile() ?? this.virtualSourceFile

        return this.printer.printNode(EmitHint.Unspecified, node, sourceFile)
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
