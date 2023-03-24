import {ConverterPlugin} from "../plugin";
import ts, {Node} from "typescript";
import {ConverterContext} from "../context";
import {Render} from "../render";
import {Annotation} from "../annotation";

export class AnnotationsPlugin implements ConverterPlugin {
    private readonly coveredAnnotations = new Set<Node>()

    private readonly annotations: Annotation[]

    constructor(annotations: Annotation[]) {
        this.annotations = annotations
    }

    generate(context: ConverterContext): Record<string, string> {
        return {};
    }

    render(node: ts.Node, context: ConverterContext, next: Render): string | null {
        if (this.coveredAnnotations.has(node)) return null
        this.coveredAnnotations.add(node)

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
