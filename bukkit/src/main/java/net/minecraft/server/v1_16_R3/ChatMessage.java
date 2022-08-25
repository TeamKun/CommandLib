package net.minecraft.server.v1_16_R3;

import com.mojang.brigadier.Message;

public class ChatMessage implements Message {
    public String getKey() {
        return "";
    }

    @Override
    public String getString() {
        return "";
    }

    public Object[] getArgs() {
        return new Object[0];
    }
}
