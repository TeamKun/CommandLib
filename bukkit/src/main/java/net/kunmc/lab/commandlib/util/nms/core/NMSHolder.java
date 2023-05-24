package net.kunmc.lab.commandlib.util.nms.core;

import net.kunmc.lab.commandlib.util.nms.MinecraftClass;

// 1.16.5には存在しない
public class NMSHolder extends MinecraftClass {
    public NMSHolder(Object handle) {
        super(handle, "core.Holder");
    }

    public static class NMSReference extends MinecraftClass {
        public NMSReference(Object handle) {
            super(handle, "core.Holder$c", "core.Holder$Reference");
        }

        public Object value() {
            return invokeMethod("a", "value");
        }
    }
}
