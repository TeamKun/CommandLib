package net.kunmc.lab.commandlib.argument;

import com.mojang.brigadier.exceptions.CommandSyntaxException;
import io.papermc.paper.command.brigadier.argument.ArgumentTypes;
import net.kunmc.lab.commandlib.Argument;
import net.kunmc.lab.commandlib.CommandContext;
import net.kunmc.lab.commandlib.exception.ArgumentParseException;
import org.bukkit.Bukkit;
import org.bukkit.NamespacedKey;
import org.bukkit.Registry;
import org.bukkit.loot.LootTable;

@SuppressWarnings("UnstableApiUsage")
public class LootTableArgument extends Argument<LootTable, LootTableArgument> {
    public LootTableArgument(String name) {
        super(name, ArgumentTypes.namespacedKey());
        suggestionAction(sb -> NamespacedKeyArgumentSupport.suggestKeys(sb, Registry.LOOT_TABLES));
    }

    @Override
    public LootTable cast(Object parsedArgument) {
        return (LootTable) parsedArgument;
    }

    @Override
    protected LootTable parseImpl(CommandContext ctx) throws ArgumentParseException, CommandSyntaxException {
        NamespacedKey key = ctx.getHandle()
                               .getArgument(name(), NamespacedKey.class);
        LootTable lootTable = Bukkit.getLootTable(key);
        if (lootTable == null) {
            throw new ArgumentParseException(x -> x.sendFailure(key + " is not a known loot table"));
        }
        return lootTable;
    }
}
