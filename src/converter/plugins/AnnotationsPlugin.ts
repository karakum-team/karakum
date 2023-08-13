import {ConverterPlugin} from "../plugin.js";
import ts, {Node} from "typescript";
import {ConverterContext} from "../context.js";
import {Render} from "../render.js";
import {Annotation} from "../annotation.js";

export class AnnotationsPlugin implements ConverterPlugin {
    private readonly annotations: Annotation[]

    constructor(annotations: Annotation[]) {
        this.annotations = annotations
    }

    generate(context: ConverterContext): Record<string, string> {
        return {};
    }

    render(node: ts.Node, context: ConverterContext, next: Render): string | null {
        const annotations = this.resolveAnnotations(node, context)

        if (annotations.length > 0) {
            return `
${annotations.join("\n")}
${next(node)}
            `;
        }

        return null
    }

    setup(context: ConverterContext): void {
    }

    traverse(node: ts.Node, context: ConverterContext): void {
    }

    private resolveAnnotations(node: Node, context: ConverterContext): string[] {
        const annotations: string[] = []

        for (const annotation of this.annotations) {
            const result = annotation(node, context)

            if (result !== null) annotations.push(result)
        }

        return annotations
    }
}
