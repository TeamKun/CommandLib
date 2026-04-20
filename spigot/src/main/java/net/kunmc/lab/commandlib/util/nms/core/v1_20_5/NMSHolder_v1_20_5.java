package net.kunmc.lab.commandlib.util.nms.core.v1_20_5;

import net.kunmc.lab.commandlib.util.nms.core.NMSHolder;

public class NMSHolder_v1_20_5 extends NMSHolder {
    public NMSHolder_v1_20_5(Object handle) {
        super(handle, "core.Holder");
    }

    public static class NMSReference_v1_20_5 extends NMSReference {
        public NMSReference_v1_20_5(Object handle) {
            super(handle, "core.Holder$Reference");
        }

        @Override
        public Object value() {
            return invokeMethod("value");
        }
    }
}
