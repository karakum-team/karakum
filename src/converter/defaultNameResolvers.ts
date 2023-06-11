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

export const defaultNameResolvers: NameResolver[] = [
    resolveFunctionParameterName,
    resolveFunctionReturnTypeName,
    resolveFunctionTypeAliasParameterName,
    resolveCallSignatureParameterName,
    resolveConstructorParameterName,
    resolveClassMethodParameterName,
    resolveClassMethodReturnTypeName,
    resolveInterfaceMethodParameterName,
    resolveInterfaceMethodReturnTypeName,
    resolveTypeAliasPropertyName,
]
