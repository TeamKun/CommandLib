package net.kunmc.lab.commandlib.argument;

import com.mojang.brigadier.exceptions.CommandSyntaxException;
import net.kunmc.lab.commandlib.Argument;
import net.kunmc.lab.commandlib.CommandContext;
import net.kunmc.lab.commandlib.exception.IncorrectArgumentInputException;
import net.kunmc.lab.commandlib.util.nms.argument.NMSArgumentEntities;
import org.bukkit.entity.Entity;

import java.util.List;
import java.util.function.Consumer;

public class EntitiesArgument extends Argument<List<Entity>> {
    public EntitiesArgument(String name) {
        this(name, option -> {
        });
    }

    public EntitiesArgument(String name, Consumer<Option<List<Entity>, CommandContext>> options) {
        super(name,
              NMSArgumentEntities.create()
                                 .argument());
        applyOptions(options);
    }

    @Override
    public List<Entity> cast(Object parsedArgument) {
        return ((List<Entity>) parsedArgument);
    }

    @Override
    protected List<Entity> parseImpl(CommandContext ctx) throws IncorrectArgumentInputException, CommandSyntaxException {
        return NMSArgumentEntities.create()
                                  .parse(ctx.getHandle(), name());
    }
}
