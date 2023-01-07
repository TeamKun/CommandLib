package net.kunmc.lab.commandlib;

import org.bukkit.permissions.Permission;
import org.bukkit.permissions.PermissionDefault;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public abstract class Command extends CommonCommand<CommandContext, Arguments, ArgumentBuilder, Command> {
    private PermissionDefault defaultPermission = PermissionDefault.OP;

    public Command(@NotNull String name) {
        super(name);
    }

    public final void setPermission(@NotNull PermissionDefault defaultPermission) {
        this.defaultPermission = defaultPermission;
    }

    @Override
    final boolean hasPermission(CommandContext ctx) {
        return ctx.getSender()
                  .hasPermission(permissionName());
    }

    public final String permissionName() {
        return "minecraft.command." + permissionNameWithoutPrefix();
    }

    final List<Permission> permissions() {
        List<Permission> permissions = new ArrayList<>();
        permissions.add(new Permission(permissionName(), defaultPermission));
        permissions.addAll(children().stream()
                                     .flatMap(x -> x.permissions()
                                                    .stream())
                                     .collect(Collectors.toList()));
        return permissions;
    }

    private String permissionNameWithoutPrefix() {
        if (parent() == null) {
            return name();
        }
        return parent().permissionNameWithoutPrefix() + "." + name();
    }

    @Override
    final ArgumentBuilder createArgumentBuilder() {
        return new ArgumentBuilder();
    }
}
