package net.kunmc.lab.commandlib.util.nms.chat;

import net.kunmc.lab.commandlib.util.bukkit.VersionUtil;
import net.kunmc.lab.commandlib.util.nms.MinecraftClass;

public class NMSTranslatableContents extends MinecraftClass {
    public NMSTranslatableContents(Object handle) {
        super(handle, "network.chat.contents.TranslatableContents");
    }

    public String getKey() {
        if (VersionUtil.is1_20_x()) {
            return ((String) invokeMethod("b"));
        }

        return ((String) invokeMethod("a", "getKey"));
    }

    public Object[] getArgs() {
        if (VersionUtil.is1_20_x()) {
            return ((Object[]) invokeMethod("d"));
        }

        return ((Object[]) invokeMethod("c", "getArgs"));
    }
}
