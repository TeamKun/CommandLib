package net.kunmc.lab.commandlib;

import org.jetbrains.annotations.NotNull;

public abstract class Command extends CommonCommand<CommandContext, Arguments, ArgumentBuilder, Command> {
    private int permissionLevel = 4;

    public Command(@NotNull String name) {
        super(name);
    }

    public final void setPermissionLevel(int level) {
        this.permissionLevel = level;
    }

    @Override
    final boolean hasPermission(CommandContext ctx) {
        return ctx.getSender()
                  .hasPermissionLevel(permissionLevel);
    }

    final int permissionLevel() {
        return permissionLevel;
    }

    @Override
    final ArgumentBuilder createArgumentBuilder() {
        return new ArgumentBuilder();
    }
}
