package net.kunmc.lab.commandlib.util;

import java.lang.reflect.Field;
import java.lang.reflect.Method;

public class ReflectionUtils {
    private ReflectionUtils() {
    }

    public static Field getFieldIncludingSuperclasses(Class<?> clazz, String name) throws NoSuchFieldException {
        Field field;

        try {
            field = clazz.getDeclaredField(name);
        } catch (NoSuchFieldException e) {
            Class<?> superclass = clazz.getSuperclass();
            if (superclass == null) {
                throw e;
            }
            field = getFieldIncludingSuperclasses(superclass, name);
        }

        return field;
    }

    public static Method getMethodIncludingSuperclasses(Class<?> clazz,
                                                        String name,
                                                        Class<?>... parameterTypes) throws NoSuchMethodException {
        Method method;

        try {
            method = clazz.getDeclaredMethod(name, parameterTypes);
        } catch (NoSuchMethodException e) {
            Class<?> superclass = clazz.getSuperclass();
            if (superclass == null) {
                throw e;
            }
            method = getMethodIncludingSuperclasses(superclass, name, parameterTypes);
        }

        return method;
    }
}
