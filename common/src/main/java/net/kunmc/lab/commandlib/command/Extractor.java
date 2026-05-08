package net.kunmc.lab.commandlib.command;

import net.kunmc.lab.commandlib.CommonCommandContext;
import net.kunmc.lab.commandlib.exception.CommandPrerequisiteException;

@FunctionalInterface
public interface Extractor<C extends CommonCommandContext<?, ?>, S> {
    S extract(C ctx) throws CommandPrerequisiteException;
}
