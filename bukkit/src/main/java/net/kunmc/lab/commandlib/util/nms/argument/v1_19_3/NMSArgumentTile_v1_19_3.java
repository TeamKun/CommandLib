package net.kunmc.lab.commandlib.util.nms.argument.v1_19_3;

import com.mojang.brigadier.arguments.ArgumentType;
import com.mojang.brigadier.context.CommandContext;
import net.kunmc.lab.commandlib.util.nms.argument.NMSArgumentTile;
import net.kunmc.lab.commandlib.util.nms.argument.NMSArgumentTileLocation;
import net.kunmc.lab.commandlib.util.nms.command.NMSCommandBuildContext;
import net.kunmc.lab.commandlib.util.nms.server.NMSCraftServer;

public class NMSArgumentTile_v1_19_3 extends NMSArgumentTile {
    public NMSArgumentTile_v1_19_3() {
        super(null, "commands.arguments.blocks.ArgumentTile");
    }

    @Override
    public ArgumentType<?> argument() {
        NMSCommandBuildContext commandBuildContext = NMSCraftServer.create()
                                                                   .getServer()
                                                                   .getDataPackResources()
                                                                   .getCommandBuildContext();
        return ((ArgumentType<?>) newInstance(new Class<?>[]{commandBuildContext.getFoundClass()},
                                              new Object[]{commandBuildContext.getHandle()}));
    }

    @Override
    protected NMSArgumentTileLocation parseImpl(CommandContext<?> ctx, String name) {
        return NMSArgumentTileLocation.create(invokeStaticMethod("a", ctx, name));
    }
}
