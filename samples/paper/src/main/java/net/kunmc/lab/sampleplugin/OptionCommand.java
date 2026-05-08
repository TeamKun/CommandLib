package net.kunmc.lab.sampleplugin;

import net.kunmc.lab.commandlib.Command;
import net.kunmc.lab.commandlib.CommandContext;
import net.kunmc.lab.commandlib.CommandOption;
import net.kunmc.lab.commandlib.Options;
import net.kunmc.lab.commandlib.argument.StringArgument;

/**
 * /broadcast [-s|--silent] [-r|--repeat <count>] <message>
 */
public class OptionCommand extends Command {
    public OptionCommand() {
        super("broadcast");

        CommandOption<Boolean, CommandContext> silent = option(Options.flag("silent", 's')
                                                                      .description("Do not send the broadcast"));
        CommandOption<Integer, CommandContext> repeat = option(Options.integer("repeat", 'r', 1, 1, 5)
                                                                      .description("Number of times to send"));

        argument(new StringArgument("message", StringArgument.Type.PHRASE)).execute((message, ctx) -> {
            if (ctx.getOption(silent)) {
                ctx.sendWarn("Broadcast skipped: " + message);
                return;
            }
            for (int i = 0; i < ctx.getOption(repeat); i++) {
                ctx.sendSuccess("[Broadcast] " + message);
            }
        });
    }
}
