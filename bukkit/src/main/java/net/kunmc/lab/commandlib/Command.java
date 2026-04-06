package net.kunmc.lab.commandlib;

import net.kunmc.lab.commandlib.exception.CommandPrerequisiteException;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;
import org.bukkit.permissions.Permission;
import org.bukkit.permissions.PermissionDefault;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

public abstract class Command extends CommonCommand<CommandContext, ArgumentBuilder, Command> {
    private String permissionNode = null;
    private PermissionDefault defaultPermission = PermissionDefault.OP;

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

    public final void permission(@NotNull PermissionDefault defaultPermission) {
        this.defaultPermission = Objects.requireNonNull(defaultPermission);
    }

    public final void permission(@NotNull String node) {
        this.permissionNode = Objects.requireNonNull(node);
    }

    public final void permission(@NotNull String node, @NotNull PermissionDefault defaultPermission) {
        this.permissionNode = Objects.requireNonNull(node);
        this.defaultPermission = Objects.requireNonNull(defaultPermission);
    }

    public final String permissionName(@NotNull String prefix) {
        if (permissionNode != null) {
            return permissionNode;
        }
        return prefix + "." + permissionNameWithoutPrefix();
    }

    private String permissionNameWithoutPrefix() {
        if (parent() == null) {
            return name();
        }
        return parent().permissionNameWithoutPrefix() + "." + name();
    }

    final List<Permission> permissions(@NotNull String prefix) {
        List<Permission> permissions = new ArrayList<>();
        permissions.add(new Permission(permissionName(prefix), defaultPermission));
        permissions.addAll(children().stream()
                                     .flatMap(x -> x.permissions(prefix)
                                                    .stream())
                                     .collect(Collectors.toList()));
        return permissions;
    }
}
