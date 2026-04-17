package net.kunmc.lab.commandlib.argument;

import net.kunmc.lab.commandlib.AbstractCommandContext;
import net.kunmc.lab.commandlib.exception.ArgumentValidationException;

@FunctionalInterface
public interface ArgumentValidator<T, C extends AbstractCommandContext<?, ?>> {
    void validate(T value, C ctx) throws ArgumentValidationException;
}
