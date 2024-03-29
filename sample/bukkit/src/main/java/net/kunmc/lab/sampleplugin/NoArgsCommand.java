package net.kunmc.lab.sampleplugin;

import net.kunmc.lab.commandlib.Command;

/**
 * By registering this, you can use the command below.
 * /noargs
 * <p>
 * When you want to create a command that doesn't require arguments,
 * you must override "execute" method.
 */
public class NoArgsCommand extends Command {
    public NoArgsCommand() {
        super("noargs");

        execute(ctx -> {
            ctx.sendSuccess("This is NoArgs Command's Message!");
        });
    }
}
