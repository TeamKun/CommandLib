package net.kunmc.lab.commandlib;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.serializer.plain.PlainTextComponentSerializer;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.mockito.Mockito;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.UUID;

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
        Mockito.when(player.getUniqueId())
               .thenReturn(UUID.randomUUID());
        Mockito.when(player.getLocale())
               .thenReturn(locale);

        FakeSender fakeSender = new FakeSender(player);
        fakeSender.op(false);
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
        Mockito.when(console.getName())
               .thenReturn("Console");

        FakeSender fakeSender = new FakeSender(console);
        fakeSender.op(true);
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
        Mockito.when(commandSender.hasPermission(Mockito.anyString()))
               .thenReturn(true);
    }

    /**
     * Returns the underlying CommandSender mock, useful for additional Mockito setup.
     */
    public CommandSender asSender() {
        return commandSender;
    }

    public FakeSender name(@NotNull String name) {
        Mockito.when(commandSender.getName())
               .thenReturn(name);
        return this;
    }

    public FakeSender uniqueId(@NotNull UUID uniqueId) {
        if (!(commandSender instanceof Entity)) {
            throw new IllegalStateException("sender is not an entity");
        }
        Mockito.when(((Entity) commandSender).getUniqueId())
               .thenReturn(uniqueId);
        return this;
    }

    public FakeSender op(boolean op) {
        Mockito.when(commandSender.isOp())
               .thenReturn(op);
        return this;
    }

    public FakeSender permissions(@NotNull String... permissions) {
        return permissions(Set.of(permissions));
    }

    public FakeSender permissions(@NotNull Set<String> permissions) {
        Set<String> permissionSet = new HashSet<>(permissions);
        Mockito.when(commandSender.hasPermission(Mockito.anyString()))
               .thenAnswer(invocation -> permissionSet.contains(invocation.getArgument(0)));
        return this;
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
