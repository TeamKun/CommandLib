package net.kunmc.lab.commandlib.util.nms.chat.v1_20_5;

import net.kunmc.lab.commandlib.util.nms.chat.NMSIChatMutableComponent;
import net.kunmc.lab.commandlib.util.nms.chat.NMSTranslatableContents;

public class NMSIChatMutableComponent_v1_20_5 extends NMSIChatMutableComponent {
    public NMSIChatMutableComponent_v1_20_5(Object handle) {
        super(handle, "network.chat.MutableComponent");
    }

    @Override
    public NMSTranslatableContents getContentsAsTranslatable() {
        return NMSTranslatableContents.create(invokeMethod("getContents"));
    }
}
