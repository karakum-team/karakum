package io.github.sgrishchenko.karakum.extension

import io.github.sgrishchenko.karakum.extension.nameResolvers.*
import js.array.ReadonlyArray
import typescript.Node

val defaultNameResolvers: ReadonlyArray<NameResolver<Node>> = arrayOf(
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
).map(::resolveParenthesizedTypeName).toTypedArray()
