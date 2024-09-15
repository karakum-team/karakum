import ts from "typescript";
import {createSimplePlugin} from "../plugin.js";
import {CheckCoverageService, checkCoverageServiceKey} from "./CheckCoveragePlugin.js";
import {TypeScriptService, typeScriptServiceKey} from "./TypeScriptPlugin.js";
import {NamespaceInfoService, namespaceInfoServiceKey} from "./NamespaceInfoPlugin.js";
import {CommentService, commentServiceKey} from "./CommentPlugin.js";
import {ConfigurationService, configurationServiceKey} from "./ConfigurationPlugin.js";

export const convertVariableDeclaration = createSimplePlugin((node, context, render) => {
    if (!ts.isVariableDeclaration(node)) return null

    const checkCoverageService = context.lookupService<CheckCoverageService>(checkCoverageServiceKey)
    checkCoverageService?.cover(node)

    // skip initializer
    node.initializer && checkCoverageService?.cover(node.initializer)

    const commentService = context.lookupService<CommentService>(commentServiceKey)
    const configurationService = context.lookupService<ConfigurationService>(configurationServiceKey)
    const typeScriptService = context.lookupService<TypeScriptService>(typeScriptServiceKey)
    const namespaceInfoService = context.lookupService<NamespaceInfoService>(namespaceInfoServiceKey)

    const modifier = node.parent.flags & ts.NodeFlags.Const
        ? "val "
        : "var "

    const name = render(node.name)

    const namespace = typeScriptService?.findClosest(node, ts.isModuleDeclaration)

    let externalModifier = "external "

    if (namespace !== undefined && namespaceInfoService?.resolveNamespaceStrategy(namespace) === "object") {
        externalModifier = ""
    }

    let leadingComment = ""

    if (
        configurationService?.configuration.granularity === "top-level"
        && (
            namespace === undefined
            || namespaceInfoService?.resolveNamespaceStrategy(namespace) === "package"
        )
    ) {
        leadingComment = commentService?.renderLeadingComments(node.parent) ?? ""
    }

    let type: string

    if (node.type) {
        type = render(node.type)
    } else {
        type = "Any? /* should be inferred */" // TODO: infer types
    }

    return `${leadingComment}${externalModifier}${modifier}${name}: ${type}`
})
