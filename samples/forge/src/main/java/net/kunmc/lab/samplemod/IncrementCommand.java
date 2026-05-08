package net.kunmc.lab.samplemod;

import net.kunmc.lab.commandlib.Command;
import net.kunmc.lab.commandlib.CommandContext;
import net.kunmc.lab.commandlib.argument.IntegerArgument;
import net.kunmc.lab.commandlib.argument.StringArgument;

import java.util.HashMap;
import java.util.Map;

/**
 * /increment <target>
 * /increment <target> <count>
 */
public class IncrementCommand extends Command {
    private final Map<String, Integer> values = new HashMap<>();

    public IncrementCommand() {
        super("increment");

        StringArgument target = new StringArgument("target", StringArgument.Type.WORD);
        argument(target).execute((name, ctx) -> increment(name, 1, ctx));

        argument(target, new IntegerArgument("count")).execute((name, count, ctx) -> increment(name, count, ctx));
    }

    private void increment(String name, int count, CommandContext ctx) {
        int value = values.getOrDefault(name, 0) + count;
        values.put(name, value);
        ctx.sendSuccess(name + " = " + value);
    }
}
