package net.kunmc.lab.commandlib.argument;

import com.mojang.brigadier.exceptions.CommandSyntaxException;
import net.kunmc.lab.commandlib.Argument;
import net.kunmc.lab.commandlib.CommandContext;
import net.kunmc.lab.commandlib.exception.ArgumentParseException;
import net.kunmc.lab.commandlib.util.nms.argument.NMSArgumentNamespacedKey;
import org.bukkit.Bukkit;
import org.bukkit.NamespacedKey;
import org.bukkit.Registry;
import org.bukkit.loot.LootTable;

public class LootTableArgument extends Argument<LootTable, LootTableArgument> {
    public LootTableArgument(String name) {
        super(name,
              NMSArgumentNamespacedKey.create()
                                      .argument());
        suggestionAction(sb -> NamespacedKeyArgumentSupport.suggestKeys(sb, Registry.LOOT_TABLES));
    }

    @Override
    public LootTable cast(Object parsedArgument) {
        return (LootTable) parsedArgument;
    }

    @Override
    protected LootTable parseImpl(CommandContext ctx) throws ArgumentParseException, CommandSyntaxException {
        String value = NMSArgumentNamespacedKey.create()
                                               .parse(ctx.getHandle(), name());
        NamespacedKey key = NamespacedKeyArgumentSupport.parseKey(value);
        LootTable lootTable = Bukkit.getLootTable(key);
        if (lootTable == null) {
            throw new ArgumentParseException(x -> x.sendFailure(value + " is not a known loot table"));
        }
        return lootTable;
    }
}
