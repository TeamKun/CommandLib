package net.kunmc.lab.commandlib.util.nms.server.v1_20_5;

import net.kunmc.lab.commandlib.util.nms.command.NMSCommandBuildContext;
import net.kunmc.lab.commandlib.util.nms.server.NMSCraftServer;
import net.kunmc.lab.commandlib.util.nms.server.NMSDataPackResources;
import net.kunmc.lab.commandlib.util.reflection.ReflectionUtil;

import java.lang.reflect.InvocationTargetException;
import java.util.Arrays;

public class NMSDataPackResources_v1_20_5 extends NMSDataPackResources {
    public NMSDataPackResources_v1_20_5(Object handle) {
        super(handle, "server.ReloadableServerResources");
    }

    @Override
    public NMSCommandBuildContext getCommandBuildContext() {
        try {
            NMSCommandBuildContext buildContext = NMSCommandBuildContext.create(null);
            Object registryLookup = getValue("registryLookup");
            Object worldData = NMSCraftServer.create()
                                             .getServer()
                                             .getValue("worldData");
            Object enabledFeatures = Arrays.stream(worldData.getClass()
                                                            .getInterfaces())
                                           .flatMap(x -> Arrays.stream(x.getDeclaredMethods()))
                                           .filter(x -> x.getName()
                                                         .equals("enabledFeatures"))
                                           .findFirst()
                                           .orElseThrow(() -> new NoSuchMethodException("enabledFeatures"))
                                           .invoke(worldData);

            Object newBuildContext = ReflectionUtil.getMethodsByNameIncludingSuperclasses(buildContext.getFoundClass(),
                                                                                          "simple")
                                                   .get(0)
                                                   .invoke(null, registryLookup, enabledFeatures);

            return NMSCommandBuildContext.create(newBuildContext);
        } catch (InvocationTargetException | IllegalAccessException | NoSuchMethodException e) {
            throw new RuntimeException(e);
        }
    }
}
