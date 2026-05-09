package net.kunmc.lab.commandlib.argument;

import com.mojang.brigadier.exceptions.CommandSyntaxException;
import net.kunmc.lab.commandlib.Argument;
import net.kunmc.lab.commandlib.CommandContext;
import net.kunmc.lab.commandlib.exception.ArgumentParseException;
import net.kunmc.lab.commandlib.util.nms.argument.NMSArgumentNamespacedKey;
import org.bukkit.Bukkit;
import org.bukkit.NamespacedKey;
import org.bukkit.advancement.Advancement;

public class AdvancementArgument extends Argument<Advancement, AdvancementArgument> {
    public AdvancementArgument(String name) {
        super(name,
              NMSArgumentNamespacedKey.create()
                                      .argument());
        addSuggestionAction(sb -> NamespacedKeyArgumentSupport.suggestKeys(sb, Bukkit.advancementIterator()));
    }

    @Override
    public Advancement cast(Object parsedArgument) {
        return (Advancement) parsedArgument;
    }

    @Override
    protected Advancement parseImpl(CommandContext ctx) throws ArgumentParseException, CommandSyntaxException {
        String value = NMSArgumentNamespacedKey.create()
                                               .parse(ctx.getHandle(), name());
        NamespacedKey key = NamespacedKeyArgumentSupport.parseKey(value);
        Advancement advancement = Bukkit.getAdvancement(key);
        if (advancement == null) {
            throw new ArgumentParseException(x -> x.sendFailure(value + " is not a known advancement"));
        }
        return advancement;
    }
}
