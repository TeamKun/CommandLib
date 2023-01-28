package net.kunmc.lab.sampleplugin;

import net.kunmc.lab.commandlib.Command;
import org.bukkit.entity.Player;

public class PreprocessCommand extends Command {
    public PreprocessCommand() {
        super("preprocesscommand");

        addPreprocess(ctx -> {
            if (ctx.getSender() instanceof Player) {
                return true;
            }
            ctx.sendFailure("player only command");
            return false;
        });

        execute(ctx -> {
            ctx.sendSuccess("success");
        });

        addChildren(new Command("sub") {{
            setInheritParentPreprocess(false);
            execute(ctx -> ctx.sendSuccess("always success"));
        }});
    }
}
