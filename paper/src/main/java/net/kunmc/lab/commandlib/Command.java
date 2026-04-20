package net.kunmc.lab.commandlib;

import net.kunmc.lab.commandlib.exception.CommandPrerequisiteException;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;
import org.bukkit.permissions.Permission;
import org.bukkit.permissions.PermissionDefault;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.stream.Collectors;

public abstract class Command extends CommonCommand<CommandContext, ArgumentBuilder, Command> {
    public Command(@NotNull String name) {
        super(name);
    }

    public final void requirePlayer() {
        addPrerequisite(ctx -> {
            if (!(ctx.getSender() instanceof Player)) {
                throw new CommandPrerequisiteException("This command can only be executed by a player.");
            }
        });
    }

    public final void requireConsole() {
        addPrerequisite(ctx -> {
            if (!(ctx.getSender() instanceof ConsoleCommandSender)) {
                throw new CommandPrerequisiteException("This command can only be executed from the console.");
            }
        });
    }

    final List<Permission> permissions(@NotNull String prefix) {
        return permissionConfigs(prefix).stream()
                .map(c -> new Permission(c.node(), c.description(), toBukkitDefault(c.defaultPermission())))
                .collect(Collectors.toList());
    }

    private static PermissionDefault toBukkitDefault(@NotNull DefaultPermission common) {
        switch (common) {
            case ALL:
                return PermissionDefault.TRUE;
            case NONE:
                return PermissionDefault.FALSE;
            default:
                return PermissionDefault.OP;
        }
    }
}
