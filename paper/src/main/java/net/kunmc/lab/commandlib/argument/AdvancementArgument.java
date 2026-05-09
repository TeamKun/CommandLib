package net.kunmc.lab.commandlib.argument;

import com.mojang.brigadier.exceptions.CommandSyntaxException;
import io.papermc.paper.command.brigadier.argument.ArgumentTypes;
import net.kunmc.lab.commandlib.Argument;
import net.kunmc.lab.commandlib.CommandContext;
import net.kunmc.lab.commandlib.exception.ArgumentParseException;
import org.bukkit.Bukkit;
import org.bukkit.NamespacedKey;
import org.bukkit.advancement.Advancement;

@SuppressWarnings("UnstableApiUsage")
public class AdvancementArgument extends Argument<Advancement, AdvancementArgument> {
    public AdvancementArgument(String name) {
        super(name, ArgumentTypes.namespacedKey());
        suggestionAction(sb -> NamespacedKeyArgumentSupport.suggestKeys(sb, Bukkit.advancementIterator()));
    }

    @Override
    public Advancement cast(Object parsedArgument) {
        return (Advancement) parsedArgument;
    }

    @Override
    protected Advancement parseImpl(CommandContext ctx) throws ArgumentParseException, CommandSyntaxException {
        NamespacedKey key = ctx.getHandle()
                               .getArgument(name(), NamespacedKey.class);
        Advancement advancement = Bukkit.getAdvancement(key);
        if (advancement == null) {
            throw new ArgumentParseException(x -> x.sendFailure(key + " is not a known advancement"));
        }
        return advancement;
    }
}
