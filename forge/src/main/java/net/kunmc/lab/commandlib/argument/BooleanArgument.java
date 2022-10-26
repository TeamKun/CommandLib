package net.kunmc.lab.commandlib.argument;

import com.mojang.brigadier.arguments.BoolArgumentType;
import com.mojang.brigadier.context.CommandContext;
import net.kunmc.lab.commandlib.Argument;
import net.minecraft.command.CommandSource;

import java.util.function.Consumer;

public class BooleanArgument extends Argument<Boolean> {
    public BooleanArgument(String name) {
        this(name, option -> {
        });
    }

    public BooleanArgument(String name, Consumer<Option<Boolean>> options) {
        super(name, BoolArgumentType.bool());
        setOptions(options);
    }

    @Override
    protected Boolean cast(Object parsedArgument) {
        return ((Boolean) parsedArgument);
    }

    @Override
    public Boolean parse(CommandContext<CommandSource> ctx) {
        return BoolArgumentType.getBool(ctx, name);
    }
}
