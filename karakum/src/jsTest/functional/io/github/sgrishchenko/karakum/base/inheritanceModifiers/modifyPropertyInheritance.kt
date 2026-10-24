package io.github.sgrishchenko.karakum.base.inheritanceModifiers

import io.github.sgrishchenko.karakum.extension.match
import io.github.sgrishchenko.karakum.extension.resolve
import io.github.sgrishchenko.karakum.extension.withFile
import io.github.sgrishchenko.karakum.extension.withName
import js.reflect.invoke
import typescript.isClassDeclaration
import typescript.isPropertyDeclaration

val modifyPropertyInheritance = resolve(
    "override" to match {
        match(
            isClassDeclaration::invoke,
            withFile("**/interface/inheritance.d.ts"),
            withName("ChildClass"),
        ) {
            match(isPropertyDeclaration::invoke, withName("firstField"))
            match(isPropertyDeclaration::invoke, withName("secondField"))
            match(isPropertyDeclaration::invoke, withName("thirdField"))
            match(isPropertyDeclaration::invoke, withName("fourthField"))
            match(isPropertyDeclaration::invoke, withName("otherField"))
        }
    }
)
