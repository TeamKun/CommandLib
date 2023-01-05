package net.kunmc.lab.commandlib;

import net.minecraft.command.CommandSource;
import org.jetbrains.annotations.NotNull;

public abstract class Command extends CommonCommand<CommandSource, CommandContext, Arguments, ArgumentBuilder, Command> {
    private int permissionLevel = 4;

    public Command(@NotNull String name) {
        super(name);
    }

    public final void setPermissionLevel(int level) {
        this.permissionLevel = level;
    }

    @Override
    boolean hasPermission(CommandSource commandSource) {
        return commandSource.hasPermissionLevel(permissionLevel);
    }

    @Override
    boolean hasPermission(CommandContext ctx) {
        return ctx.getSender()
                  .hasPermissionLevel(permissionLevel);
    }

    @Override
    ArgumentBuilder createArgumentBuilder() {
        return new ArgumentBuilder();
    }

    @Override
    CommandContext createCommandContext(com.mojang.brigadier.context.CommandContext<CommandSource> ctx) {
        return new CommandContext(ctx);
    }
}
