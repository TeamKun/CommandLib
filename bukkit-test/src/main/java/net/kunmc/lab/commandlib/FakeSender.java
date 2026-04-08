package net.kunmc.lab.commandlib;

import net.md_5.bungee.api.chat.BaseComponent;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;
import org.mockito.Mockito;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

public class FakeSender {
    private final CommandSender commandSender;
    private final List<BaseComponent> sentMessages = new ArrayList<>();

    public static FakeSender player(String name) {
        Player player = Mockito.mock(Player.class);
        Player.Spigot spigot = Mockito.mock(Player.Spigot.class);
        Mockito.when(player.getName())
               .thenReturn(name);
        Mockito.when(player.spigot())
               .thenReturn(spigot);
        Mockito.when(player.hasPermission(Mockito.anyString()))
               .thenReturn(true);

        FakeSender fakeSender = new FakeSender(player);
        Mockito.doAnswer(invocation -> {
                   fakeSender.sentMessages.add(invocation.getArgument(0));
                   return null;
               })
               .when(spigot)
               .sendMessage(Mockito.any(BaseComponent.class));

        return fakeSender;
    }

    public static FakeSender console() {
        ConsoleCommandSender console = Mockito.mock(ConsoleCommandSender.class);
        CommandSender.Spigot spigot = Mockito.mock(CommandSender.Spigot.class);
        Mockito.when(console.spigot())
               .thenReturn(spigot);
        Mockito.when(console.hasPermission(Mockito.anyString()))
               .thenReturn(true);

        FakeSender fakeSender = new FakeSender(console);
        Mockito.doAnswer(invocation -> {
                   fakeSender.sentMessages.add(invocation.getArgument(0));
                   return null;
               })
               .when(spigot)
               .sendMessage(Mockito.any(BaseComponent.class));

        return fakeSender;
    }

    private FakeSender(CommandSender commandSender) {
        this.commandSender = commandSender;
    }

    /**
     * Returns the underlying CommandSender mock, useful for additional Mockito setup.
     */
    public CommandSender asSender() {
        return commandSender;
    }

    CommandSender getCommandSender() {
        return commandSender;
    }

    /**
     * Returns all messages sent to this sender during command execution.
     * Use {@link BaseComponent#toPlainText()} to extract text,
     * or {@link BaseComponent#getColor()} to check the message color
     * (e.g. green for sendSuccess, red for sendFailure).
     */
    public List<BaseComponent> getSentMessages() {
        return Collections.unmodifiableList(sentMessages);
    }

    /**
     * Convenience method that returns sent messages as plain text with color codes stripped.
     */
    public List<String> getSentMessageTexts() {
        return sentMessages.stream()
                           .map(c -> c.toPlainText())
                           .collect(Collectors.toList());
    }
}
