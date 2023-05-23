package net.minecraft.server.v1_16_R3;

import com.mojang.brigadier.StringReader;
import com.mojang.brigadier.arguments.ArgumentType;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;

public class ArgumentItemStack implements ArgumentType {
    public static ArgumentItemStack a() {
        return new ArgumentItemStack();
    }

    public static ArgumentPredicateItemStack a(CommandContext var0, String var1) {
        return null;
    }

    @Override
    public Object parse(StringReader reader) throws CommandSyntaxException {
        return null;
    }
}
