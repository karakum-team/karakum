package io.github.sgrishchenko.karakum.base.inheritanceModifiers

import io.github.sgrishchenko.karakum.extension.match
import io.github.sgrishchenko.karakum.extension.resolve
import io.github.sgrishchenko.karakum.extension.withFile
import io.github.sgrishchenko.karakum.extension.withName
import js.reflect.invoke
import typescript.isClassDeclaration

val modifyClassInheritance = resolve(
    "open" to match {
        withFile("**/class/parentConstructors.d.ts").let {
            match(isClassDeclaration::invoke, withName("GrandparentWithConstructor"), it)
            match(isClassDeclaration::invoke, withName("ParentWithoutConstructor"), it)
            match(isClassDeclaration::invoke, withName("ParentWithConstructor"), it)
        }

        match(
            isClassDeclaration::invoke,
            withFile("**/interface/inheritance.d.ts"),
            withName("ParentClass")
        )
    }
)
