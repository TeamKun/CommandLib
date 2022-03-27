package net.minecraft.server.v1_16_R3;

import com.mojang.brigadier.arguments.ArgumentType;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;

public class ArgumentMobEffect implements ArgumentType {
    public static ArgumentMobEffect a() {
        return new ArgumentMobEffect();
    }

    public static MobEffectList a(CommandContext<CommandListenerWrapper> ctx, String s) throws CommandSyntaxException {
        return null;
    }
}
