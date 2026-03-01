package io.github.sgrishchenko.karakum.base.inheritanceModifiers

import io.github.sgrishchenko.karakum.extension.match
import io.github.sgrishchenko.karakum.extension.resolve
import io.github.sgrishchenko.karakum.extension.withFile
import io.github.sgrishchenko.karakum.extension.withName
import typescript.isClassDeclaration

val modifyClassInheritance = resolve(
    "open" to match {
        withFile("**/class/parentConstructors.d.ts").let {
            match(::isClassDeclaration, withName("GrandparentWithConstructor"), it)
            match(::isClassDeclaration, withName("ParentWithoutConstructor"), it)
            match(::isClassDeclaration, withName("ParentWithConstructor"), it)
        }

        match(
            ::isClassDeclaration,
            withFile("**/interface/inheritance.d.ts"),
            withName("ParentClass")
        )
    }
)
