package net.kunmc.lab.commandlib;

import com.mojang.brigadier.tree.RootCommandNode;
import net.minecraft.command.CommandSource;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.RegisterCommandsEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class CommandLib {
    private final List<Command> cmds;

    public static void register(@NotNull Command cmd, @NotNull Command... cmds) {
        List<Command> list = new ArrayList<Command>() {{
            add(cmd);
            addAll(Arrays.asList(cmds));
        }};

        new CommandLib(list);
    }

    static int executeWithStackTrace(CommandContext ctx, ContextAction contextAction) {
        try {
            contextAction.accept(ctx);
            return 1;
        } catch (Exception e) {
            e.printStackTrace();
            ctx.sendFailure("An unexpected error occurred trying to execute that command.");
            ctx.sendFailure("Check the console for details.");
            return 0;
        }
    }

    private CommandLib(List<Command> cmds) {
        this.cmds = cmds;

        MinecraftForge.EVENT_BUS.register(this);
    }

    @SubscribeEvent
    public void onRegisterCommands(RegisterCommandsEvent e) {
        RootCommandNode<CommandSource> root = e.getDispatcher().getRoot();

        cmds.stream()
                .map(Command::toCommandNodes)
                .reduce(new ArrayList<>(), (x, y) -> {
                    x.addAll(y);
                    return x;
                })
                .forEach(root::addChild);
    }
}
