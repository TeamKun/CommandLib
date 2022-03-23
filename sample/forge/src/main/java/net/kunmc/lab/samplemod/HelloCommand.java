package net.kunmc.lab.samplemod;

import net.kunmc.lab.commandlib.Command;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.util.Util;
import net.minecraft.util.text.StringTextComponent;
import org.apache.logging.log4j.message.SimpleMessage;

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
                        ((List<PlayerEntity>) ctx.getParsedArg(0)).forEach(p -> {
                            p.sendMessage(new StringTextComponent("Hello " + p.getName().getString() + "!"), Util.DUMMY_UUID);
                        });
                    });
        });
    }
}
