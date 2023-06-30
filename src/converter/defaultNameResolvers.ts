import {NameResolver} from "./nameResolver";
import {resolveFunctionParameterName} from "./nameResolvers/resolveFunctionParameterName";
import {resolveFunctionReturnTypeName} from "./nameResolvers/resolveFunctionReturnTypeName";
import {resolveFunctionTypeAliasParameterName} from "./nameResolvers/resolveFunctionTypeAliasParameterName";
import {resolveCallSignatureParameterName} from "./nameResolvers/resolveCallSignatureParameterName";
import {resolveConstructorParameterName} from "./nameResolvers/resolveConstructorParameterName";
import {resolveClassMethodParameterName} from "./nameResolvers/resolveClassMethodParameterName";
import {resolveClassMethodReturnTypeName} from "./nameResolvers/resolveClassMethodReturnTypeName";
import {resolveInterfaceMethodParameterName} from "./nameResolvers/resolveInterfaceMethodParameterName";
import {resolveInterfaceMethodReturnTypeName} from "./nameResolvers/resolveInterfaceMethodReturnTypeName";
import {resolveTypeAliasPropertyName} from "./nameResolvers/resolveTypeAliasPropertyName";
import {resolveParenthesizedTypeName} from "./nameResolvers/resolveParenthesizedTypeName";
import {resolveClassPropertyName} from "./nameResolvers/resolveClassPropertyName";
import {resolveInterfacePropertyName} from "./nameResolvers/resolveInterfacePropertyName";
import {resolveClassMethodCallbackParameterName} from "./nameResolvers/resolveClassMethodCallbackParameterName";
import {resolveInterfaceMethodCallbackParameterName} from "./nameResolvers/resolveInterfaceMethodCallbackParameterName";
import {resolveClassPropertyCallbackParameterName} from "./nameResolvers/resolveClassPropertyCallbackParameterName";
import {
    resolveInterfacePropertyCallbackParameterName
} from "./nameResolvers/resolveInterfacePropertyCallbackParameterName";

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
    resolveInterfaceMethodCallbackParameterName,
    resolveInterfacePropertyName,
    resolveInterfacePropertyCallbackParameterName,
    resolveTypeAliasPropertyName,
].map(resolveParenthesizedTypeName)
