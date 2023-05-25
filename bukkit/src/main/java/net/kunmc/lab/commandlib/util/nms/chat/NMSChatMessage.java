package net.kunmc.lab.commandlib.util.nms.chat;

import com.mojang.brigadier.Message;
import net.kunmc.lab.commandlib.util.nms.MinecraftClass;

public class NMSChatMessage extends MinecraftClass {
    public NMSChatMessage(Message handle) {
        super(handle, "ChatMessage");
    }

    public String getKey() {
        return ((String) invokeMethod("getKey"));
    }

    public Object[] getArgs() {
        return ((Object[]) invokeMethod("getArgs"));
    }
}
