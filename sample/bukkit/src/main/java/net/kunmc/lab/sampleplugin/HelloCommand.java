package net.kunmc.lab.sampleplugin;

import net.kunmc.lab.commandlib.Command;
import net.kunmc.lab.commandlib.argument.PlayersArgument;

/**
 * By registering this, you can use the command below.
 * /hello <players>
 */
public class HelloCommand extends Command {
    public HelloCommand() {
        super("hello");

        argument(new PlayersArgument("players"),
                (players, ctx) -> {
                    players.forEach(x -> x.sendMessage("Hello " + x.getName() + "!"));
                });
    }
}
