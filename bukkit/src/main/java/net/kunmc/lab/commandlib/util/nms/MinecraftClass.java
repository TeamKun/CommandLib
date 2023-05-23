package net.kunmc.lab.commandlib.util.nms;

import java.util.stream.Collectors;
import java.util.stream.Stream;

public abstract class MinecraftClass extends NMSClass {
    public MinecraftClass(Object handle, String className, String... classNames) {
        super(handle,
              NMSReflection.findMinecraftClass(className, classNames)
                           .orElseThrow(() -> new RuntimeException(String.format("cannot find classes %s",
                                                                                 Stream.concat(Stream.of(className),
                                                                                               Stream.of(classNames))
                                                                                       .collect(Collectors.toList())))));
    }
}
