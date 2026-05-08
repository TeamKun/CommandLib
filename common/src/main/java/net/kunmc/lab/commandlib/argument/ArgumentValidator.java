package net.kunmc.lab.commandlib.argument;

import net.kunmc.lab.commandlib.CommonCommandContext;
import net.kunmc.lab.commandlib.exception.ArgumentValidationException;

@FunctionalInterface
public interface ArgumentValidator<T, C extends CommonCommandContext<?, ?>> {
    void validate(T value, C ctx) throws ArgumentValidationException;
}
