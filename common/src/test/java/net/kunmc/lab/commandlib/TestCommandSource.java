package net.kunmc.lab.commandlib;

import java.util.Set;

class TestCommandSource {
    private final Set<String> permissions;

    TestCommandSource(String... permissions) {
        this.permissions = Set.of(permissions);
    }

    boolean hasPermission(String permission) {
        return permissions.contains(permission);
    }
}
