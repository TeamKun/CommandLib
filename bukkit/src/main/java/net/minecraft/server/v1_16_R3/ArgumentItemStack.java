package net.minecraft.server.v1_16_R3;

import com.mojang.brigadier.arguments.ArgumentType;
import com.mojang.brigadier.context.CommandContext;

public class ArgumentItemStack implements ArgumentType {
    public static ArgumentItemStack a() {
        return new ArgumentItemStack();
    }

    public static <S> ArgumentPredicateItemStack a(CommandContext<S> var0, String var1) {
        return null;
    }
}
