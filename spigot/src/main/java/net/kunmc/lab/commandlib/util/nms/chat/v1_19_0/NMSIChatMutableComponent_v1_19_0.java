package net.kunmc.lab.commandlib.util.nms.chat.v1_19_0;

import net.kunmc.lab.commandlib.util.nms.chat.NMSIChatMutableComponent;
import net.kunmc.lab.commandlib.util.nms.chat.NMSTranslatableContents;

public class NMSIChatMutableComponent_v1_19_0 extends NMSIChatMutableComponent {
    public NMSIChatMutableComponent_v1_19_0(Object handle) {
        super(handle, "network.chat.IChatMutableComponent");
    }

    @Override
    public NMSTranslatableContents getContentsAsTranslatable() {
        return NMSTranslatableContents.create(invokeMethod("b"));
    }
}
