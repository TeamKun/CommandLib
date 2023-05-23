package net.kunmc.lab.commandlib.util.nms;

import net.kunmc.lab.commandlib.util.ReflectionUtils;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public abstract class NMSClass {
    private final Object handle;
    protected final Class<?> clazz;

    public NMSClass(Object handle, Class<?> clazz) {
        this.handle = handle;
        this.clazz = clazz;
    }

    public final Object getHandle() {
        return handle;
    }

    protected final Object invokeMethod(String methodName, Object... args) {
        try {
            Method method = getMethod(methodName,
                                      Arrays.stream(args)
                                            .map(Object::getClass)
                                            .toArray(Class[]::new));
            return method.invoke(handle, args);
        } catch (IllegalAccessException | InvocationTargetException | NoSuchMethodException e) {
            throw new RuntimeException(e);
        }
    }

    protected final Object invokeMethod(String methodName, String methodName2, Object... args) {
        try {
            Method method = getMethod(methodName,
                                      Arrays.stream(args)
                                            .map(Object::getClass)
                                            .toArray(Class[]::new));
            return method.invoke(handle, args);
        } catch (IllegalAccessException | InvocationTargetException | NoSuchMethodException e) {
            try {
                Method method = getMethod(methodName2,
                                          Arrays.stream(args)
                                                .map(Object::getClass)
                                                .toArray(Class[]::new));
                return method.invoke(handle, args);
            } catch (IllegalAccessException | InvocationTargetException | NoSuchMethodException ex) {
                throw new RuntimeException(ex);
            }
        }
    }

    protected final <T> T getValue(Class<T> tClass, String name, String... names) {
        List<String> list = Stream.concat(Stream.of(name), Stream.of(names))
                                  .collect(Collectors.toList());
        return list.stream()
                   .map(x -> {
                       try {
                           return ReflectionUtils.getFieldIncludingSuperclasses(clazz, x);
                       } catch (NoSuchFieldException e) {
                           return null;
                       }
                   })
                   .filter(Objects::nonNull)
                   .map(x -> {
                       x.setAccessible(true);
                       try {
                           return tClass.cast(x.get(handle));
                       } catch (IllegalAccessException e) {
                           return null;
                       }
                   })
                   .filter(Objects::nonNull)
                   .findFirst()
                   .orElseThrow(() -> new RuntimeException(String.format("Could not find fields %s", list)));
    }

    private Method getMethod(String methodName, Class<?>... parameterTypes) throws NoSuchMethodException {
        Method method = ReflectionUtils.getMethodIncludingSuperclasses(clazz, methodName, parameterTypes);
        method.setAccessible(true);
        return method;
    }

    private Method getMethod(String methodName,
                             String methodName2,
                             Class<?>... parameterTypes) throws NoSuchMethodException {
        try {
            return getMethod(methodName, parameterTypes);
        } catch (NoSuchMethodException e) {
            return getMethod(methodName2, parameterTypes);
        }
    }
}
