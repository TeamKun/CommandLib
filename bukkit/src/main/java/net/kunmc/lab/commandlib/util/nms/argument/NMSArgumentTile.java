package net.kunmc.lab.commandlib.util.nms.argument;

import com.mojang.brigadier.arguments.ArgumentType;
import com.mojang.brigadier.context.CommandContext;
import net.kunmc.lab.commandlib.util.nms.MinecraftClass;
import net.kunmc.lab.commandlib.util.nms.command.NMSCommandBuildContext;
import net.kunmc.lab.commandlib.util.nms.server.NMSCraftServer;

public class NMSArgumentTile extends MinecraftClass {
    public NMSArgumentTile() {
        super(null,
              "ArgumentTile",
              "commands.arguments.blocks.ArgumentTile",
              "commands.arguments.blocks.BlockStateArgument");
    }

    public ArgumentType<?> argument() {
        try {
            return ((ArgumentType<?>) invokeMethod("a"));
        } catch (Exception e) {
            NMSCommandBuildContext commandBuildContext = new NMSCraftServer().getServer()
                                                                             .getDataPackResources()
                                                                             .getCommandBuildContext();
            return ((ArgumentType<?>) newInstance(new Class<?>[]{commandBuildContext.getFoundClass()},
                                                  new Object[]{commandBuildContext.getHandle()}));
        }
    }

    public NMSArgumentTileLocation parse(CommandContext<?> ctx, String name) {
        return new NMSArgumentTileLocation(invokeMethod("a", "getBlock", ctx, name));
    }
}
