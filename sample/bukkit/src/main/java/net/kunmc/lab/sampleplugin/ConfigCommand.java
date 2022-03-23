package net.kunmc.lab.sampleplugin;

import net.kunmc.lab.commandlib.Command;
import net.kunmc.lab.commandlib.CommandContext;
import org.jetbrains.annotations.NotNull;

/**
 * By registering this, you can use the command below.
 * /config intValue // check intValue's current value.
 * /config intValue <integer> // change intValue's value to <integer>.
 */
public class ConfigCommand extends Command {
    private int intValue = 0;

    public ConfigCommand() {
        super("config");

        addChildren(new Command("intValue") {
            {
                argument(builder ->
                        builder.integerArgument("integer")
                );
            }

            @Override
            protected void execute(@NotNull CommandContext ctx) {
                if (ctx.getParsedArgs().isEmpty()) {
                    ctx.sendSuccess("Current value is " + intValue);
                    return;
                }

                intValue = ctx.getParsedArg(0, Integer.class);
                ctx.sendSuccess("Change intValue to " + intValue);
            }
        });
    }
}
