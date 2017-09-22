package de.florianschmitt.system.util

import org.springframework.core.ResolvableType

object ReflectionUtils {
    fun <ENTITY> classFromFirstGenericParameter(obj: Any): Class<ENTITY> {
        return classFromNthGenericParameter(obj, 0)
    }

    fun <ENTITY> classFromSecondGenericParameter(obj: Any): Class<ENTITY> {
        return classFromNthGenericParameter(obj, 1)
    }

    private fun <ENTITY> classFromNthGenericParameter(obj: Any, n: Int): Class<ENTITY> {
        return ResolvableType.forClass(obj.javaClass)//
                .superType//
                .resolveGeneric(n) as Class<ENTITY>
    }
}
