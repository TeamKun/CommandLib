package net.kunmc.lab.commandlib;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.serializer.plain.PlainTextComponentSerializer;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.mockito.Mockito;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public final class FakeSender {
    private final CommandSender commandSender;
    private final List<Component> sentMessages = new ArrayList<>();

    public static FakeSender player(@NotNull String name) {
        return player(name, "en_us");
    }

    public static FakeSender player(@NotNull String name, @NotNull String locale) {
        Player player = Mockito.mock(Player.class);
        Mockito.when(player.getName())
               .thenReturn(name);
        Mockito.when(player.getLocale())
               .thenReturn(locale);
        Mockito.when(player.hasPermission(Mockito.anyString()))
               .thenReturn(true);

        FakeSender fakeSender = new FakeSender(player);
        Mockito.doAnswer(invocation -> {
                   fakeSender.sentMessages.add(invocation.getArgument(0));
                   return null;
               })
               .when(player)
               .sendMessage(Mockito.any(Component.class));

        return fakeSender;
    }

    public static FakeSender console() {
        ConsoleCommandSender console = Mockito.mock(ConsoleCommandSender.class);
        Mockito.when(console.hasPermission(Mockito.anyString()))
               .thenReturn(true);

        FakeSender fakeSender = new FakeSender(console);
        Mockito.doAnswer(invocation -> {
                   fakeSender.sentMessages.add(invocation.getArgument(0));
                   return null;
               })
               .when(console)
               .sendMessage(Mockito.any(Component.class));

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

    /**
     * Returns all Adventure components sent to this sender during command execution.
     */
    public List<Component> getSentMessages() {
        return Collections.unmodifiableList(sentMessages);
    }

    /**
     * Convenience method that returns sent messages as plain text.
     */
    public List<String> getSentMessageTexts() {
        PlainTextComponentSerializer serializer = PlainTextComponentSerializer.plainText();
        List<String> result = new ArrayList<>();
        for (Component component : sentMessages) {
            result.add(serializer.serialize(component));
        }
        return result;
    }
}
