package net.kunmc.lab.commandlib;

import net.minecraft.server.v1_16_R3.CommandListenerWrapper;

import java.util.List;

final class Arguments extends AbstractArguments<CommandListenerWrapper, CommandContext> {
    Arguments(List<? extends CommonArgument<?, CommandContext>> commonArguments) {
        super(commonArguments);
    }

    @Override
    CommandContext createCommandContext(com.mojang.brigadier.context.CommandContext<CommandListenerWrapper> ctx) {
        return new CommandContext(ctx);
    }
}
