package net.kunmc.lab.commandlib.argument;

import com.mojang.brigadier.exceptions.CommandSyntaxException;
import io.papermc.paper.command.brigadier.argument.ArgumentTypes;
import io.papermc.paper.registry.RegistryKey;
import net.kunmc.lab.commandlib.Argument;
import net.kunmc.lab.commandlib.CommandContext;
import net.kunmc.lab.commandlib.exception.ArgumentParseException;
import org.bukkit.attribute.Attribute;

@SuppressWarnings("UnstableApiUsage")
public class AttributeArgument extends Argument<Attribute, AttributeArgument> {
    public AttributeArgument(String name) {
        super(name, ArgumentTypes.resource(RegistryKey.ATTRIBUTE));
    }

    @Override
    public Attribute cast(Object parsedArgument) {
        return (Attribute) parsedArgument;
    }

    @Override
    protected Attribute parseImpl(CommandContext ctx) throws ArgumentParseException, CommandSyntaxException {
        return ctx.getHandle()
                  .getArgument(name(), Attribute.class);
    }
}
