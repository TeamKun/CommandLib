package net.kunmc.lab.sampleplugin;

import net.kunmc.lab.commandlib.Command;
import org.bukkit.entity.Player;

import java.util.List;

/**
 * By registering this, you can use the command below.
 * /hello <players>
 */
public class HelloCommand extends Command {
    public HelloCommand() {
        super("hello");

        argument(builder -> {
            // first boolean means accepting not player entities.
            // second boolean means accepting multiple entities. Even if this is false, parsedArgs contains List.
            builder.entityArgument("players", false, false)
                    .execute(ctx -> {
                        ((List<Player>) ctx.getParsedArg(0)).forEach(p -> {
                            p.sendMessage("Hello " + p.getName() + "!");
                        });
                    });
        });
    }
}
