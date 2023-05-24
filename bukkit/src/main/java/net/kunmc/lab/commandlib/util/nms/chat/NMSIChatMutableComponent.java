package net.kunmc.lab.commandlib.util.nms.chat;

import net.kunmc.lab.commandlib.util.nms.MinecraftClass;

// 1.16.5には存在しない
public class NMSIChatMutableComponent extends MinecraftClass {
    public NMSIChatMutableComponent(Object handle) {
        super(handle, "network.chat.IChatMutableComponent", "network.chat.MutableComponent");
    }

    public NMSTranslatableContents getContentsAsTranslatable() {
        return new NMSTranslatableContents(invokeMethod("b", "getContents"));
    }
}
