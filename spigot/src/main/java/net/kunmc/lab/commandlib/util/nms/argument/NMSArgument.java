package net.kunmc.lab.commandlib.util.nms.argument;

import com.mojang.brigadier.arguments.ArgumentType;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import net.kunmc.lab.commandlib.util.nms.MinecraftClass;
import net.kunmc.lab.commandlib.util.nms.exception.UncheckedCommandSyntaxException;

public abstract class NMSArgument<T> extends MinecraftClass {
    public NMSArgument(Object handle, String className, String... classNames) {
        super(handle, className, classNames);
    }

    public abstract ArgumentType<?> argument();


    public final T parse(CommandContext<?> ctx, String name) throws CommandSyntaxException {
        try {
            return parseImpl(ctx, name);
        } catch (UncheckedCommandSyntaxException e) {
            throw e.unwrap();
        }
    }

    protected abstract T parseImpl(CommandContext<?> ctx, String name);
}
