package net.kunmc.lab.commandlib.util.nms.chat.v1_16_0;

import com.mojang.brigadier.Message;
import net.kunmc.lab.commandlib.util.nms.chat.NMSChatMessage;

public class NMSChatMessage_v1_16_0 extends NMSChatMessage {
    public NMSChatMessage_v1_16_0(Message handle) {
        super(handle, "ChatMessage");
    }

    @Override
    public String getKey() {
        return ((String) invokeMethod("getKey"));
    }

    @Override
    public Object[] getArgs() {
        return ((Object[]) invokeMethod("getArgs"));
    }
}
