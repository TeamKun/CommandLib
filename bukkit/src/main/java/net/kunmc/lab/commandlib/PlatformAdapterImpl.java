package net.kunmc.lab.commandlib;

import net.minecraft.server.v1_16_R3.CommandListenerWrapper;

final class PlatformAdapterImpl implements PlatformAdapter<CommandListenerWrapper, CommandContext, Arguments, ArgumentBuilder, Command> {
    @Override
    public CommandContext createCommandContext(com.mojang.brigadier.context.CommandContext<CommandListenerWrapper> ctx) {
        return new CommandContext(ctx);
    }

    @Override
    public boolean hasPermission(Command command, CommandListenerWrapper commandSource) {
        return commandSource.getBukkitSender()
                            .hasPermission(command.permissionName());
    }
}
