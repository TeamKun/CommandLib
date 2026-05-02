package net.kunmc.lab.commandlib.argument;

import com.mojang.brigadier.exceptions.CommandSyntaxException;
import io.papermc.paper.command.brigadier.argument.ArgumentTypes;
import net.kunmc.lab.commandlib.Argument;
import net.kunmc.lab.commandlib.CommandContext;
import net.kunmc.lab.commandlib.exception.ArgumentParseException;
import org.bukkit.NamespacedKey;

@SuppressWarnings("UnstableApiUsage")
public class NamespacedKeyArgument extends Argument<NamespacedKey, NamespacedKeyArgument> {
    public NamespacedKeyArgument(String name) {
        super(name, ArgumentTypes.namespacedKey());
    }

    @Override
    public NamespacedKey cast(Object parsedArgument) {
        return (NamespacedKey) parsedArgument;
    }

    @Override
    protected NamespacedKey parseImpl(CommandContext ctx) throws ArgumentParseException, CommandSyntaxException {
        return ctx.getHandle()
                  .getArgument(name(), NamespacedKey.class);
    }
}
