package net.kunmc.lab.commandlib;

import com.mojang.brigadier.tree.RootCommandNode;
import net.minecraft.command.CommandSource;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.RegisterCommandsEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class CommandLib {
    private final List<Command> cmds;

    public static void register(Command cmd, Command... cmds) {
        List<Command> list = new ArrayList<Command>() {{
            add(cmd);
            addAll(Arrays.asList(cmds));
        }};

        new CommandLib(list);
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
