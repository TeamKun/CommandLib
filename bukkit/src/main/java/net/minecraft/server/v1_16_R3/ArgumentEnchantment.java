package net.minecraft.server.v1_16_R3;

import com.mojang.brigadier.StringReader;
import com.mojang.brigadier.arguments.ArgumentType;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;

public class ArgumentEnchantment implements ArgumentType {
    public static ArgumentEnchantment a() {
        return null;
    }

    public static Enchantment a(CommandContext ctx, String s) {
        return null;
    }

    @Override
    public Object parse(StringReader reader) throws CommandSyntaxException {
        return null;
    }
}
