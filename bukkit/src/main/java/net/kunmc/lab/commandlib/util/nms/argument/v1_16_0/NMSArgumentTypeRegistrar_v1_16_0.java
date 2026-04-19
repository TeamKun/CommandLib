package net.kunmc.lab.commandlib.util.nms.argument.v1_16_0;

import net.kunmc.lab.commandlib.util.nms.NMSReflection;
import net.kunmc.lab.commandlib.util.nms.argument.NMSArgumentTypeRegistrar;

public class NMSArgumentTypeRegistrar_v1_16_0 extends NMSArgumentTypeRegistrar {
    public NMSArgumentTypeRegistrar_v1_16_0() {
        super("ArgumentRegistry");
    }

    @Override
    protected Class<?> serializerInterface() {
        return NMSReflection.findMinecraftClass("ArgumentSerializer");
    }

    @Override
    protected Object brigadierStringKey() {
        try {
            Class<?> keyClass = NMSReflection.findMinecraftClass("MinecraftKey");
            return keyClass.getConstructor(String.class)
                           .newInstance("brigadier:string");
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
