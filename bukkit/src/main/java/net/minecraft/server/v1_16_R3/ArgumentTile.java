package net.minecraft.server.v1_16_R3;

import com.mojang.brigadier.StringReader;
import com.mojang.brigadier.arguments.ArgumentType;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;

public class ArgumentTile implements ArgumentType<ArgumentTileLocation> {
    public static ArgumentTile a() {
        return null;
    }

    public static ArgumentTileLocation a(CommandContext commandContext, String s) {
        return null;
    }

    @Override
    public ArgumentTileLocation parse(StringReader reader) throws CommandSyntaxException {
        return null;
    }
}
