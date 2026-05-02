package net.kunmc.lab.commandlib.argument;

import com.mojang.brigadier.exceptions.CommandSyntaxException;
import net.kunmc.lab.commandlib.Argument;
import net.kunmc.lab.commandlib.CommandContext;
import net.kunmc.lab.commandlib.exception.ArgumentParseException;
import net.kunmc.lab.commandlib.util.nms.argument.NMSArgumentNamespacedKey;
import org.bukkit.NamespacedKey;

public class NamespacedKeyArgument extends Argument<NamespacedKey, NamespacedKeyArgument> {
    public NamespacedKeyArgument(String name) {
        super(name,
              NMSArgumentNamespacedKey.create()
                                      .argument());
    }

    @Override
    public NamespacedKey cast(Object parsedArgument) {
        return (NamespacedKey) parsedArgument;
    }

    @Override
    protected NamespacedKey parseImpl(CommandContext ctx) throws ArgumentParseException, CommandSyntaxException {
        String value = NMSArgumentNamespacedKey.create()
                                               .parse(ctx.getHandle(), name());
        NamespacedKey key = value.contains(":") ? NamespacedKey.fromString(value) : NamespacedKey.minecraft(value);
        if (key == null) {
            throw new ArgumentParseException(x -> x.sendFailure(value + " is not valid namespaced key"));
        }
        return key;
    }
}
