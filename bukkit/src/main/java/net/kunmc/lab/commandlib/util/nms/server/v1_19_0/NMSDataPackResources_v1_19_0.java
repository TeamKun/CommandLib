package net.kunmc.lab.commandlib.util.nms.server.v1_19_0;

import net.kunmc.lab.commandlib.util.nms.command.NMSCommandBuildContext;
import net.kunmc.lab.commandlib.util.nms.server.NMSDataPackResources;

public class NMSDataPackResources_v1_19_0 extends NMSDataPackResources {
    public NMSDataPackResources_v1_19_0(Object handle) {
        super(handle, "server.DataPackResources");
    }

    @Override
    public NMSCommandBuildContext getCommandBuildContext() {
        return NMSCommandBuildContext.create(getValue(Object.class, "c", "commandBuildContext"));
    }
}
