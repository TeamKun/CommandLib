package net.kunmc.lab.commandlib.util.nms.argument.v1_19_0;

import com.mojang.brigadier.arguments.ArgumentType;
import com.mojang.brigadier.arguments.StringArgumentType;
import net.kunmc.lab.commandlib.util.nms.argument.NMSArgumentTypeRegistrar;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Proxy;
import java.util.Map;
import java.util.function.Supplier;

/**
 * 1.19.0+ uses {@code ArgumentTypeInfos} instead of the old {@code ArgumentRegistry}.
 * The map is {@code Map<Class<?>, ArgumentTypeInfo<?,?>>}; entries are
 * {@code ArgumentTypeInfo} instances directly, not wrapper objects.
 *
 * <p>To serialize our custom type as {@code brigadier:string GREEDY_PHRASE} we:
 * <ol>
 *   <li>Obtain the string serializer already registered for
 *       {@code StringArgumentType.class}.</li>
 *   <li>Reflectively create its inner Template object for GREEDY_PHRASE.</li>
 *   <li>Create a proxy {@code ArgumentTypeInfo} whose {@code unpack()} returns that
 *       template; the template's own {@code type()} points back to the vanilla
 *       serializer, so the Registry lookup and network write proceed via the
 *       vanilla path with no unknown key.</li>
 * </ol>
 */
public class NMSArgumentTypeRegistrar_v1_19_0 extends NMSArgumentTypeRegistrar {
    public NMSArgumentTypeRegistrar_v1_19_0() {
        super("commands.synchronization.ArgumentTypeInfos");
    }

    @Override
    protected Class<?> serializerInterface() {
        return null;  // unused — registerAsGreedyString is overridden
    }

    @Override
    protected Object brigadierStringKey() {
        return null;  // unused — registerAsGreedyString is overridden
    }

    @Override
    @SuppressWarnings({"unchecked", "rawtypes"})
    public <T extends ArgumentType<?>> void registerAsGreedyString(Class<T> argumentTypeClass, Supplier<T> factory) {
        // Find the Class->ArgumentTypeInfo map by content (field name varies by version).
        Map byClassMap = null;
        for (Field f : clazz.getDeclaredFields()) {
            if (!Map.class.isAssignableFrom(f.getType())) {
                continue;
            }
            f.setAccessible(true);
            try {
                Object val = f.get(null);
                if (val instanceof Map && ((Map) val).containsKey(StringArgumentType.class)) {
                    byClassMap = (Map) val;
                    break;
                }
            } catch (IllegalAccessException ignored) {
            }
        }
        if (byClassMap == null) {
            throw new IllegalStateException("Could not find NMS argument-type Class→ArgumentTypeInfo map in " + clazz.getName());
        }
        if (byClassMap.containsKey(argumentTypeClass)) {
            return;
        }

        // Get the string ArgumentTypeInfo instance already in the map.
        Object stringSerializer = byClassMap.get(StringArgumentType.class);

        // Find its inner Template/a class (non-static inner class).
        Class<?> templateClass = null;
        for (Class<?> inner : stringSerializer.getClass()
                                              .getDeclaredClasses()) {
            templateClass = inner;
            break;
        }
        if (templateClass == null) {
            throw new IllegalStateException("Could not find Template inner class in " + stringSerializer.getClass()
                                                                                                        .getName());
        }

        // Inner-class constructor takes the outer instance as first parameter.
        Constructor<?> templateCtor = null;
        for (Constructor<?> c : templateClass.getDeclaredConstructors()) {
            if (c.getParameterCount() == 2 && c.getParameterTypes()[1] == StringArgumentType.StringType.class) {
                templateCtor = c;
                break;
            }
        }
        if (templateCtor == null) {
            throw new IllegalStateException("Could not find Template(outer, StringType) constructor in " + templateClass.getName());
        }
        templateCtor.setAccessible(true);
        Object greedyTemplate;
        try {
            greedyTemplate = templateCtor.newInstance(stringSerializer, StringArgumentType.StringType.GREEDY_PHRASE);
        } catch (Exception e) {
            throw new RuntimeException("Failed to create GREEDY_PHRASE template", e);
        }

        // Find the ArgumentTypeInfo interface from the stringSerializer instance.
        Class<?> argumentTypeInfoInterface = null;
        for (Class<?> iface : stringSerializer.getClass()
                                              .getInterfaces()) {
            if ("ArgumentTypeInfo".equals(iface.getSimpleName())) {
                argumentTypeInfoInterface = iface;
                break;
            }
        }
        if (argumentTypeInfoInterface == null) {
            throw new IllegalStateException("Could not find ArgumentTypeInfo interface on " + stringSerializer.getClass()
                                                                                                              .getName());
        }

        // Proxy whose unpack() returns the GREEDY_PHRASE template.
        // template.type() returns stringSerializer, which is in the vanilla Registry,
        // so the client receives key "brigadier:string" and extra data GREEDY_PHRASE.
        //
        // Match unpack() by signature (1 arg of ArgumentType), not method name, because
        // pre-1.20.5 servers use obfuscated names where unpack() is also named "a".
        Object proxy = Proxy.newProxyInstance(argumentTypeClass.getClassLoader(),
                                              new Class[]{argumentTypeInfoInterface},
                                              (p, method, args) -> {
                                                  if (args != null && args.length == 1 && args[0] instanceof ArgumentType) {
                                                      return greedyTemplate;
                                                  }
                                                  return null;
                                              });

        byClassMap.put(argumentTypeClass, proxy);
    }
}
