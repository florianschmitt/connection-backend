package de.florianschmitt.system.util;

import org.springframework.core.ResolvableType;

public class ReflectionUtils {
    public static <ENTITY> Class<ENTITY> classFromFirstGenericParameter(Object obj) {
        return classFromNthGenericParameter(obj, 0);
    }

    public static <ENTITY> Class<ENTITY> classFromSecondGenericParameter(Object obj) {
        return classFromNthGenericParameter(obj, 1);
    }

    public static <ENTITY> Class<ENTITY> classFromThirdGenericParameter(Object obj) {
        return classFromNthGenericParameter(obj, 2);
    }

    private static <ENTITY> Class<ENTITY> classFromNthGenericParameter(Object obj, int n) {
        @SuppressWarnings("unchecked")
        Class<ENTITY> entityClass = (Class<ENTITY>) ResolvableType.forClass(obj.getClass())//
                .getSuperType()//
                .resolveGeneric(n);
        return entityClass;
    }
}
