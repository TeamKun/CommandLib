package net.kunmc.lab.commandlib.util.nms.argument.v1_16_0;

import com.mojang.brigadier.arguments.ArgumentType;
import com.mojang.brigadier.context.CommandContext;
import net.kunmc.lab.commandlib.util.nms.argument.NMSArgumentTile;
import net.kunmc.lab.commandlib.util.nms.argument.NMSArgumentTileLocation;

public class NMSArgumentTile_v1_16_0 extends NMSArgumentTile {
    public NMSArgumentTile_v1_16_0() {
        super(null, "ArgumentTile");
    }

    @Override
    public ArgumentType<?> argument() {
        return ((ArgumentType<?>) invokeStaticMethod("a"));
    }

    @Override
    protected NMSArgumentTileLocation parseImpl(CommandContext<?> ctx, String name) {
        return NMSArgumentTileLocation.create(invokeStaticMethod("a", ctx, name));
    }
}
