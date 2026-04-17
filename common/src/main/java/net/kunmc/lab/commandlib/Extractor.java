package net.kunmc.lab.commandlib;

import net.kunmc.lab.commandlib.exception.CommandPrerequisiteException;

@FunctionalInterface
public interface Extractor<C extends AbstractCommandContext<?, ?>, S> {
    S extract(C ctx) throws CommandPrerequisiteException;
}
