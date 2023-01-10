package net.kunmc.lab.commandlib;

import org.bukkit.permissions.Permission;
import org.bukkit.permissions.PermissionDefault;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public abstract class Command extends CommonCommand<CommandContext, ArgumentBuilder, Command> {
    private PermissionDefault defaultPermission = PermissionDefault.OP;

    public Command(@NotNull String name) {
        super(name);
    }

    public final void setPermission(@NotNull PermissionDefault defaultPermission) {
        this.defaultPermission = defaultPermission;
    }

    public final String permissionName() {
        return "minecraft.command." + permissionNameWithoutPrefix();
    }

    private String permissionNameWithoutPrefix() {
        if (parent() == null) {
            return name();
        }
        return parent().permissionNameWithoutPrefix() + "." + name();
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
}
