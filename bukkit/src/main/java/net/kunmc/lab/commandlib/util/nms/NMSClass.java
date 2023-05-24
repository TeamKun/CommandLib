package net.kunmc.lab.commandlib.util.nms;

import com.mojang.brigadier.exceptions.CommandSyntaxException;
import net.kunmc.lab.commandlib.util.ReflectionUtils;
import net.kunmc.lab.commandlib.util.nms.exception.MethodNotFoundException;
import net.kunmc.lab.commandlib.util.nms.exception.NMSClassNotAssignableException;
import net.kunmc.lab.commandlib.util.nms.exception.UncheckedCommandSyntaxException;

import java.lang.reflect.Constructor;
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
        if (handle != null && !clazz.isAssignableFrom(handle.getClass())) {
            throw new NMSClassNotAssignableException(handle, clazz);
        }
        this.handle = handle;
        this.clazz = clazz;
    }

    public final Object getHandle() {
        return handle;
    }

    public final Class<?> getFoundClass() {
        return clazz;
    }

    protected final Object newInstance(Class<?>[] parameterTypes, Object[] args) {
        try {
            Constructor<?> constructor = clazz.getConstructor(parameterTypes);
            constructor.setAccessible(true);
            return constructor.newInstance(args);
        } catch (InstantiationException | IllegalAccessException | InvocationTargetException |
                 NoSuchMethodException e) {
            throw new RuntimeException(e);
        }
    }

    protected final Object invokeMethod(String methodName, Object... args) {
        return invokeMethod(new String[]{methodName}, args);
    }

    protected final Object invokeMethod(String methodName, String methodName2, Object... args) {
        return invokeMethod(new String[]{methodName, methodName2}, args);
    }

    protected final Object invokeMethod(String methodName, String methodName2, String methodName3, Object... args) {
        return invokeMethod(new String[]{methodName, methodName2, methodName3}, args);
    }

    protected final Object invokeMethod(String[] methodNames, Object... args) {
        Class<?>[] argClasses = Arrays.stream(args)
                                      .map(Object::getClass)
                                      .toArray(Class[]::new);

        return invokeMethod(methodNames, argClasses, args);
    }

    protected final Object invokeMethod(String[] methodNames, Class<?>[] parameterClasses, Object... args) {
        Method method = null;
        for (String methodName : methodNames) {
            try {
                method = getMethod(methodName, parameterClasses);
                break;
            } catch (NoSuchMethodException ignored) {
            }
        }

        if (method == null) {
            throw new MethodNotFoundException(methodNames);
        }

        try {
            return method.invoke(handle, args);
        } catch (IllegalAccessException e) {
            throw new RuntimeException(e);
        } catch (InvocationTargetException e) {
            Throwable cause = e.getTargetException();

            if (cause instanceof CommandSyntaxException) {
                throw new UncheckedCommandSyntaxException(((CommandSyntaxException) cause));
            }

            throw new RuntimeException(e);
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
}
