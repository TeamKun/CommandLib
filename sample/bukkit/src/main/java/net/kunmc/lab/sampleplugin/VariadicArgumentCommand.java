package net.kunmc.lab.sampleplugin;

import net.kunmc.lab.commandlib.Command;
import net.kunmc.lab.commandlib.argument.BooleanArgument;
import net.kunmc.lab.commandlib.argument.IntegerArgument;

public class VariadicArgumentCommand extends Command {
    public VariadicArgumentCommand() {
        super("variadic");

        argument(new IntegerArgument("number"), (number, ctx) -> {
            ctx.sendSuccess(number);
        });

        argument(new IntegerArgument("number"), new BooleanArgument("bool"), (number, bool, ctx) -> {
            ctx.sendSuccess(number);
            ctx.sendSuccess(bool);
        });
    }
}
