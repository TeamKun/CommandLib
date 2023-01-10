package net.kunmc.lab.commandlib;

import org.jetbrains.annotations.NotNull;

public abstract class Command extends CommonCommand<CommandContext, ArgumentBuilder, Command> {
    private int permissionLevel = 4;

    public Command(@NotNull String name) {
        super(name);
    }

    public final void setPermissionLevel(int level) {
        this.permissionLevel = level;
    }

    final int permissionLevel() {
        return permissionLevel;
    }
}
