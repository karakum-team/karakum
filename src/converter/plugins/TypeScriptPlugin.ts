import {Node, Program} from "typescript";
import {ConverterPlugin} from "../plugin";
import {ConverterContext} from "../context";
import {Render} from "../render";

export const typeScriptServiceKey = Symbol()

export class TypeScriptService {
    constructor(public readonly program: Program) {
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
