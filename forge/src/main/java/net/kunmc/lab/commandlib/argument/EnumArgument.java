package net.kunmc.lab.commandlib.argument;

import com.mojang.brigadier.context.CommandContext;
import net.kunmc.lab.commandlib.Argument;
import net.kunmc.lab.commandlib.ContextAction;
import net.minecraft.command.CommandSource;

public class EnumArgument<T extends Enum<T>> extends Argument<T> {
    private final Class<T> clazz;

    public EnumArgument(String name, Class<T> clazz, ContextAction contextAction) {
        super(name, null, contextAction, net.minecraftforge.server.command.EnumArgument.enumArgument(clazz));

        this.clazz = clazz;
    }

    @Override
    public T parse(CommandContext<CommandSource> ctx) {
        return ctx.getArgument(name, clazz);
    }
}
