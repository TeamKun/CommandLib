package net.kunmc.lab.commandlib.util.nms.chat.v1_19_0;

import net.kunmc.lab.commandlib.util.nms.chat.NMSTranslatableContents;

public class NMSTranslatableContents_v1_19_0 extends NMSTranslatableContents {
    public NMSTranslatableContents_v1_19_0(Object handle) {
        super(handle, "network.chat.contents.TranslatableContents");
    }

    @Override
    public String getKey() {
        return ((String) invokeMethod("a"));
    }

    @Override
    public Object[] getArgs() {
        return ((Object[]) invokeMethod("c"));
    }
}
