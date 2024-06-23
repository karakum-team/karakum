import {ConverterPlugin} from "../plugin.js";
import ts, {Node} from "typescript";
import {ConverterContext} from "../context.js";
import {Render} from "../render.js";
import {Annotation} from "../annotation.js";
import {GeneratedFile} from "../generated.js";

export const annotationServiceKey = Symbol()

export class AnnotationService {
    constructor(readonly annotations: Annotation[]) {
    }

    resolveAnnotations(node: Node, context: ConverterContext): string[] {
        const annotations: string[] = []

        for (const annotation of this.annotations) {
            const result = annotation(node, context)

            if (result !== null) annotations.push(result)
        }

        return annotations
    }
}

export class AnnotationPlugin implements ConverterPlugin {
    private readonly annotationService: AnnotationService

    constructor(annotations: Annotation[]) {
        this.annotationService = new AnnotationService(annotations)
    }

    generate(): GeneratedFile[] {
        return [];
    }

    render(node: ts.Node, context: ConverterContext, next: Render): string | null {
        const annotations = this.annotationService.resolveAnnotations(node, context)

        if (annotations.length > 0) {
            return `
${annotations.join("\n")}
${next(node)}
            `.trim();
        }

        return null
    }

    setup(context: ConverterContext): void {
        context.registerService(annotationServiceKey, this.annotationService)
    }

    traverse(node: ts.Node, context: ConverterContext): void {
    }
}
