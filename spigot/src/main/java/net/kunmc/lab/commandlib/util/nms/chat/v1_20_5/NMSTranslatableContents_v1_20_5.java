package net.kunmc.lab.commandlib.util.nms.chat.v1_20_5;

import net.kunmc.lab.commandlib.util.nms.chat.NMSTranslatableContents;

public class NMSTranslatableContents_v1_20_5 extends NMSTranslatableContents {
    public NMSTranslatableContents_v1_20_5(Object handle) {
        super(handle, "network.chat.contents.TranslatableContents");
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
