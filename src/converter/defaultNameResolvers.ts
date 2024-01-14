import {NameResolver} from "./nameResolver.js";
import {resolveFunctionParameterName} from "./nameResolvers/resolveFunctionParameterName.js";
import {resolveFunctionReturnTypeName} from "./nameResolvers/resolveFunctionReturnTypeName.js";
import {resolveFunctionTypeAliasParameterName} from "./nameResolvers/resolveFunctionTypeAliasParameterName.js";
import {resolveCallSignatureParameterName} from "./nameResolvers/resolveCallSignatureParameterName.js";
import {resolveConstructorParameterName} from "./nameResolvers/resolveConstructorParameterName.js";
import {resolveClassMethodParameterName} from "./nameResolvers/resolveClassMethodParameterName.js";
import {resolveClassMethodReturnTypeName} from "./nameResolvers/resolveClassMethodReturnTypeName.js";
import {resolveInterfaceMethodParameterName} from "./nameResolvers/resolveInterfaceMethodParameterName.js";
import {resolveInterfaceMethodReturnTypeName} from "./nameResolvers/resolveInterfaceMethodReturnTypeName.js";
import {resolveTypeAliasPropertyName} from "./nameResolvers/resolveTypeAliasPropertyName.js";
import {resolveParenthesizedTypeName} from "./nameResolvers/resolveParenthesizedTypeName.js";
import {resolveClassPropertyName} from "./nameResolvers/resolveClassPropertyName.js";
import {resolveInterfacePropertyName} from "./nameResolvers/resolveInterfacePropertyName.js";
import {resolveClassMethodCallbackParameterName} from "./nameResolvers/resolveClassMethodCallbackParameterName.js";
import {resolveInterfaceMethodCallbackParameterName} from "./nameResolvers/resolveInterfaceMethodCallbackParameterName.js";
import {resolveClassPropertyCallbackParameterName} from "./nameResolvers/resolveClassPropertyCallbackParameterName.js";
import {
    resolveInterfacePropertyCallbackParameterName
} from "./nameResolvers/resolveInterfacePropertyCallbackParameterName.js";
import {
    resolveInterfaceCallSignatureReturnTypeName
} from "./nameResolvers/resolveInterfaceCallSignatureReturnTypeName.js";
import {
    resolveInterfaceCallSignatureParameterName
} from "./nameResolvers/resolveInterfaceCallSignatureParameterName.js";
import {resolveVariableName} from "./nameResolvers/resolveVariableName.js";

export const defaultNameResolvers: NameResolver[] = [
    resolveFunctionParameterName,
    resolveFunctionReturnTypeName,
    resolveFunctionTypeAliasParameterName,
    resolveCallSignatureParameterName,
    resolveConstructorParameterName,
    resolveClassMethodParameterName,
    resolveClassMethodReturnTypeName,
    resolveClassMethodCallbackParameterName,
    resolveClassPropertyName,
    resolveClassPropertyCallbackParameterName,
    resolveInterfaceMethodParameterName,
    resolveInterfaceMethodReturnTypeName,
    resolveInterfaceCallSignatureParameterName,
    resolveInterfaceCallSignatureReturnTypeName,
    resolveInterfaceMethodCallbackParameterName,
    resolveInterfacePropertyName,
    resolveInterfacePropertyCallbackParameterName,
    resolveTypeAliasPropertyName,
    resolveVariableName,
].map(resolveParenthesizedTypeName)
