package net.kunmc.lab.commandlib.util.nms.argument.v1_17_0;

import net.kunmc.lab.commandlib.util.nms.NMSReflection;
import net.kunmc.lab.commandlib.util.nms.argument.NMSArgumentTypeRegistrar;

public class NMSArgumentTypeRegistrar_v1_17_0 extends NMSArgumentTypeRegistrar {
    public NMSArgumentTypeRegistrar_v1_17_0() {
        super("commands.synchronization.ArgumentRegistry");
    }

    @Override
    protected Class<?> serializerInterface() {
        return NMSReflection.findMinecraftClass("commands.synchronization.ArgumentSerializer");
    }

    @Override
    protected Object brigadierStringKey() {
        try {
            Class<?> keyClass = NMSReflection.findMinecraftClass("resources.MinecraftKey");
            return keyClass.getConstructor(String.class)
                           .newInstance("brigadier:string");
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
