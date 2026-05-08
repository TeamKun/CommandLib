package net.kunmc.lab.sampleplugin;

import net.kunmc.lab.commandlib.Command;
import net.kunmc.lab.commandlib.argument.IntegerArgument;

/**
 * By registering this, you can use the command below.
 * /config intValue // check intValue's current value.
 * /config intValue <integer> // change intValue's value to <integer>.
 */
public class ConfigCommand extends Command {
    private int intValue = 0;

    public ConfigCommand() {
        super("config");

        Command intValueCommand = new Command("intValue");
        intValueCommand.execute(ctx -> {
            ctx.sendSuccess("Current value is " + intValue);
        });

        intValueCommand.argument(new IntegerArgument("number"), (number, ctx) -> {
            intValue = number;
            ctx.sendSuccess("Change intValue to " + intValue);
        });

        addChildren(intValueCommand);
    }
}
