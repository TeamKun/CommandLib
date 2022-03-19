package net.kunmc.lab.commandlib;

import java.util.List;

class Usage {
    private final List<Argument<?>> arguments;
    private final String description;
    private final int permissionLevel;

    Usage(List<Argument<?>> arguments, String description, int permissionLevel) {
        this.arguments = arguments;
        this.description = description;
        this.permissionLevel = permissionLevel;
    }
}
