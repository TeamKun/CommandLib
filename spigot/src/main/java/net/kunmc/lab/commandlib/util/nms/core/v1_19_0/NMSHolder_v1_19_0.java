package net.kunmc.lab.commandlib.util.nms.core.v1_19_0;

import net.kunmc.lab.commandlib.util.nms.core.NMSHolder;

public class NMSHolder_v1_19_0 extends NMSHolder {
    public NMSHolder_v1_19_0(Object handle) {
        super(handle, "core.Holder");
    }

    public static class NMSReference_v1_19_0 extends NMSHolder.NMSReference {
        public NMSReference_v1_19_0(Object handle) {
            super(handle, "core.Holder$c");
        }

        @Override
        public Object value() {
            return invokeMethod("a");
        }
    }
}
