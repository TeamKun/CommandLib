package net.minecraft.server.v1_16_R3;

import com.mojang.brigadier.StringReader;
import com.mojang.brigadier.arguments.ArgumentType;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;

import java.util.Collection;

public class ArgumentEntity implements ArgumentType {
    public static ArgumentEntity a() {
        return null;
    }

    public static Entity a(CommandContext commandcontext, String s) throws CommandSyntaxException {
        return null;
    }

    public static ArgumentEntity multipleEntities() {
        return null;
    }

    public static Collection<? extends Entity> b(CommandContext commandcontext,
                                                 String s) throws CommandSyntaxException {
        return null;
    }

    public static Collection<? extends Entity> c(CommandContext commandcontext,
                                                 String s) throws CommandSyntaxException {
        return null;
    }

    public static Collection<EntityPlayer> d(CommandContext commandcontext, String s) throws CommandSyntaxException {
        return null;
    }

    public static ArgumentEntity c() {
        return null;
    }

    public static EntityPlayer e(CommandContext commandcontext, String s) throws CommandSyntaxException {
        return null;
    }

    public static ArgumentEntity d() {
        return null;
    }

    public static Collection<EntityPlayer> f(CommandContext commandcontext, String s) throws CommandSyntaxException {
        return null;
    }

    @Override
    public Object parse(StringReader reader) throws CommandSyntaxException {
        return null;
    }
}
