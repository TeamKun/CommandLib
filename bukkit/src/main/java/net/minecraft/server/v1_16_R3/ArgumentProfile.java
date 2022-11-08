package net.minecraft.server.v1_16_R3;

import com.mojang.brigadier.StringReader;
import com.mojang.brigadier.arguments.ArgumentType;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;

public class ArgumentProfile implements ArgumentType<ArgumentProfile.a> {
    public static ArgumentProfile a() {
        return new ArgumentProfile();
    }

    public static IChatBaseComponent a(CommandContext<CommandListenerWrapper> ctx,
                                       String s) throws CommandSyntaxException {
        return null;
    }

    @Override
    public a parse(StringReader reader) throws CommandSyntaxException {
        return null;
    }

    public static class a {

    }
}
