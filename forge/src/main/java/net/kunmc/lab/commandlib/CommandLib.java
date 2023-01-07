package net.kunmc.lab.commandlib;

import com.google.common.collect.Lists;
import com.mojang.brigadier.tree.RootCommandNode;
import net.minecraft.command.CommandSource;
import net.minecraft.server.MinecraftServer;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.event.server.FMLServerStartedEvent;
import net.minecraftforge.fml.server.ServerLifecycleHooks;
import org.jetbrains.annotations.NotNull;

import java.util.Collection;

public final class CommandLib {
    private final Collection<? extends Command> commands;

    public static void register(@NotNull Command command, @NotNull Command... commands) {
        register(Lists.asList(command, commands));
    }

    public static void register(@NotNull Collection<? extends Command> commands) {
        new CommandLib(commands);
    }

    private CommandLib(Collection<? extends Command> commands) {
        this.commands = commands;

        MinecraftServer server = ServerLifecycleHooks.getCurrentServer();
        if (server == null) {
            MinecraftForge.EVENT_BUS.register(this);
        } else {
            register();
        }
    }

    @SubscribeEvent
    public void onRegisterCommands(FMLServerStartedEvent e) {
        register();
    }

    private void register() {
        RootCommandNode<CommandSource> root = ServerLifecycleHooks.getCurrentServer()
                                                                  .getCommandManager()
                                                                  .getDispatcher()
                                                                  .getRoot();

        new CommandNodeCreator<>(new PlatformAdapterImpl(), commands).build()
                                                                     .forEach(root::addChild);
    }
}
