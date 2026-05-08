package net.kunmc.lab.sampleplugin;

import net.kunmc.lab.commandlib.Command;

/**
 * /ping
 */
public class PingCommand extends Command {
    public PingCommand() {
        super("ping");

        execute(ctx -> {
            ctx.sendSuccess("pong");
        });
    }
}
