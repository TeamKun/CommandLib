package net.kunmc.lab.commandlib;

import net.minecraft.command.CommandSource;

import java.util.List;

final class Arguments extends AbstractArguments<CommandSource, CommandContext> {
    Arguments(List<? extends CommonArgument<?, CommandContext>> arguments) {
        super(arguments);
    }

    @Override
    CommandContext createCommandContext(com.mojang.brigadier.context.CommandContext<CommandSource> ctx) {
        return new CommandContext(ctx);
    }
}
