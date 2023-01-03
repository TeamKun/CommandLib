package net.kunmc.lab.commandlib;

import com.google.common.collect.Lists;

import java.util.Collection;
import java.util.Map;
import java.util.stream.Collectors;

public interface Nameable {
    static <T extends Nameable> Map<String, T> toMap(T object, T... objects) {
        return toMap(Lists.asList(object, objects));
    }

    static <T extends Nameable> Map<String, T> toMap(Collection<? extends T> objects) {
        return objects.stream()
                      .collect(Collectors.toMap(Nameable::tabCompleteName, x -> x));
    }

    String tabCompleteName();
}
