package net.kunmc.lab.commandlib.util.nms.argument;

import com.mojang.brigadier.arguments.ArgumentType;
import com.mojang.brigadier.context.CommandContext;

public class NMSArgumentProfile extends NMSArgument<Object> {
    public NMSArgumentProfile() {
        super("ArgumentProfile", "commands.arguments.ArgumentProfile", "commands.arguments.GameProfileArgument");
    }

    @Override
    public ArgumentType<?> argument() {
        return ((ArgumentType<?>) newInstance(new Class[]{}, new Object[]{}));
    }

    @Override
    protected Object parseImpl(CommandContext<?> ctx, String name) {
        throw new UnsupportedOperationException();
    }
}
