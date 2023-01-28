package net.kunmc.lab.samplemod;

import net.kunmc.lab.commandlib.Command;
import net.minecraft.entity.player.PlayerEntity;

public class PreprocessCommand extends Command {
    public PreprocessCommand() {
        super("preprocesscommand");

        addPreprocess(ctx -> {
            if (ctx.getSender()
                   .getEntity() instanceof PlayerEntity) {
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
