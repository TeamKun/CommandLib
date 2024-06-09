package net.kunmc.lab.commandlib.util.nms.server.v1_20_6;

import net.kunmc.lab.commandlib.util.nms.NMSReflection;
import net.kunmc.lab.commandlib.util.nms.command.NMSCommandBuildContext;
import net.kunmc.lab.commandlib.util.nms.server.NMSDataPackResources;

import java.lang.reflect.InvocationTargetException;

public class NMSDataPackResources_v1_20_6 extends NMSDataPackResources {
    public NMSDataPackResources_v1_20_6(Object handle) {
        super(handle, "server.ReloadableServerResources");
    }

    @Override
    public NMSCommandBuildContext getCommandBuildContext() {
        try {
            Class<?> paperCommandsClass = NMSReflection.findClass("io.papermc.paper.command.brigadier.PaperCommands");
            Object paperCommandsInstance = paperCommandsClass.getField("INSTANCE")
                                                             .get(null);
            Object buildContext = paperCommandsClass.getMethod("getBuildContext")
                                                    .invoke(paperCommandsInstance);
            return NMSCommandBuildContext.create(buildContext);
        } catch (NoSuchFieldException | InvocationTargetException | IllegalAccessException | NoSuchMethodException e) {
            throw new RuntimeException(e);
        }
    }
}
