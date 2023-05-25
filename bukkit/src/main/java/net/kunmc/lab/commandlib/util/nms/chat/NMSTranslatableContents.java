package net.kunmc.lab.commandlib.util.nms.chat;

import net.kunmc.lab.commandlib.util.nms.MinecraftClass;

public class NMSTranslatableContents extends MinecraftClass {
    public NMSTranslatableContents(Object handle) {
        super(handle, "network.chat.contents.TranslatableContents");
    }

    public String getKey() {
        return ((String) invokeMethod("a", "getKey"));
    }

    public Object[] getArgs() {
        return ((Object[]) invokeMethod("c", "getArgs"));
    }
}
