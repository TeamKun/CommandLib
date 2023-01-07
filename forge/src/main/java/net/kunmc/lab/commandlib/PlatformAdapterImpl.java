package net.kunmc.lab.commandlib;

import net.minecraft.command.CommandSource;

final class PlatformAdapterImpl implements PlatformAdapter<CommandSource, CommandContext, Arguments, ArgumentBuilder, Command> {
    @Override
    public CommandContext createCommandContext(com.mojang.brigadier.context.CommandContext<CommandSource> ctx) {
        return new CommandContext(ctx);
    }

    @Override
    public boolean hasPermission(Command command, CommandSource commandSource) {
        return commandSource.hasPermissionLevel(command.permissionLevel());
    }
}
