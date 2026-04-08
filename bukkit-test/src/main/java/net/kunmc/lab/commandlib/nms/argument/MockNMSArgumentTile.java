package net.kunmc.lab.commandlib.nms.argument;

import com.mojang.brigadier.arguments.ArgumentType;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.context.CommandContext;
import net.kunmc.lab.commandlib.util.nms.argument.NMSArgumentTile;
import net.kunmc.lab.commandlib.util.nms.argument.NMSArgumentTileLocation;

public class MockNMSArgumentTile extends NMSArgumentTile {
    public MockNMSArgumentTile() {
        super(null, "Mock");
    }

    @Override
    public ArgumentType<?> argument() {
        return StringArgumentType.word();
    }

    @Override
    protected NMSArgumentTileLocation parseImpl(CommandContext<?> ctx, String name) {
        return new MockNMSArgumentTileLocation(StringArgumentType.getString(ctx, name));
    }
}
