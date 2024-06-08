package net.kunmc.lab.commandlib.util.nms.chat.v1_20_4;

import net.kunmc.lab.commandlib.util.nms.chat.NMSTranslatableContents;

public class NMSTranslatableContents_v1_20_4 extends NMSTranslatableContents {
    public NMSTranslatableContents_v1_20_4(Object handle) {
        super(handle, "network.chat.contents.TranslatableContents");
    }

    @Override
    public String getKey() {
        return ((String) invokeMethod("b"));
    }

    @Override
    public Object[] getArgs() {
        return ((Object[]) invokeMethod("d"));
    }
}
