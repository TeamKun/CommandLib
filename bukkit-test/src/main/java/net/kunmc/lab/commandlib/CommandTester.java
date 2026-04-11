package net.kunmc.lab.commandlib;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import net.kunmc.lab.commandlib.nms.argument.*;
import net.kunmc.lab.commandlib.nms.command.MockNMSCommandListenerWrapper;
import net.kunmc.lab.commandlib.nms.resources.MockNMSCraftParticle;
import net.kunmc.lab.commandlib.nms.world.MockNMSCraftBlockData;
import net.kunmc.lab.commandlib.nms.world.MockNMSCraftEnchantment;
import net.kunmc.lab.commandlib.nms.world.MockNMSCraftItemStack;
import net.kunmc.lab.commandlib.nms.world.MockNMSCraftPotionEffectType;
import net.kunmc.lab.commandlib.util.nms.NMSClassRegistry;
import net.kunmc.lab.commandlib.util.nms.NMSReflection;
import net.kunmc.lab.commandlib.util.nms.command.NMSCommandListenerWrapper;
import net.kunmc.lab.commandlib.util.nms.resources.NMSCraftParticle;
import net.kunmc.lab.commandlib.util.nms.world.NMSCraftBlockData;
import net.kunmc.lab.commandlib.util.nms.world.NMSCraftEnchantment;
import net.kunmc.lab.commandlib.util.nms.world.NMSCraftItemStack;
import net.kunmc.lab.commandlib.util.nms.world.NMSCraftPotionEffectType;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.mockito.MockedStatic;
import org.mockito.Mockito;

import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Supplier;

/**
 * Executes commands in tests without a running Minecraft server.
 * Must be closed after use (use try-with-resources).
 */
public class CommandTester implements AutoCloseable {
    private static CommandTester current;

    private final CommandDispatcher<Object> dispatcher = new CommandDispatcher<>();
    private final Map<String, Entity> fakeEntities = new LinkedHashMap<>();
    private CommandSender currentCommandSender;

    /**
     * For use by mock NMS classes only.
     */
    public static Entity getFakeEntity(String name) {
        if (current == null) {
            throw new IllegalStateException("No active CommandTester");
        }
        return current.fakeEntities.get(name);
    }

    /**
     * For use by mock NMS classes only.
     */
    public static CommandSender getCurrentCommandSender() {
        if (current == null) {
            throw new IllegalStateException("No active CommandTester");
        }
        return current.currentCommandSender;
    }

    private final MockedStatic<NMSReflection> nmsReflectionMock;
    private final MockedStatic<NMSClassRegistry> nmsRegistryMock;

    public CommandTester(Command command, String permissionPrefix) {
        this(List.of(command), permissionPrefix);
    }

    /**
     * Creates a {@code CommandTester} where NMS mocks are active when the command is built.
     * Use this overload when the command contains NMS-based arguments (e.g. {@code PlayerArgument})
     * whose constructors call into {@code NMSClassRegistry} before the tester is ready.
     *
     * <pre>{@code
     * new CommandTester(() -> new Command("heal") {{
     *     argument(new PlayerArgument("target"), (p, ctx) -> { ... });
     * }}, "test.command")
     * }</pre>
     */
    public CommandTester(Supplier<? extends Command> commandSupplier, String permissionPrefix) {
        this(permissionPrefix, () -> List.of(commandSupplier.get()));
    }

    public CommandTester(Collection<? extends Command> commands, String permissionPrefix) {
        this(permissionPrefix, () -> commands);
    }

    private CommandTester(String permissionPrefix, Supplier<Collection<? extends Command>> commandsSupplier) {
        current = this;

        nmsReflectionMock = Mockito.mockStatic(NMSReflection.class);
        nmsReflectionMock.when(() -> NMSReflection.findMinecraftClass(Mockito.anyString(), Mockito.any()))
                         .thenReturn(Object.class);
        nmsReflectionMock.when(() -> NMSReflection.findCraftBukkitClass(Mockito.anyString()))
                         .thenReturn(Object.class);

        nmsRegistryMock = Mockito.mockStatic(NMSClassRegistry.class);
        nmsRegistryMock.when(() -> NMSClassRegistry.findClass(Mockito.any()))
                       .thenThrow(new UnsupportedOperationException("This NMS class is not supported in CommandTester."));

        // command
        nmsRegistryMock.when(() -> NMSClassRegistry.findClass(NMSCommandListenerWrapper.class))
                       .thenAnswer(inv -> MockNMSCommandListenerWrapper.class);

        // entity/player arguments
        nmsRegistryMock.when(() -> NMSClassRegistry.findClass(NMSArgumentPlayer.class))
                       .thenAnswer(inv -> MockNMSArgumentPlayer.class);
        nmsRegistryMock.when(() -> NMSClassRegistry.findClass(NMSArgumentPlayers.class))
                       .thenAnswer(inv -> MockNMSArgumentPlayers.class);
        nmsRegistryMock.when(() -> NMSClassRegistry.findClass(NMSArgumentEntity.class))
                       .thenAnswer(inv -> MockNMSArgumentEntity.class);
        nmsRegistryMock.when(() -> NMSClassRegistry.findClass(NMSArgumentEntities.class))
                       .thenAnswer(inv -> MockNMSArgumentEntities.class);
        nmsRegistryMock.when(() -> NMSClassRegistry.findClass(NMSArgumentProfile.class))
                       .thenAnswer(inv -> MockNMSArgumentProfile.class);

        // scoreboard
        nmsRegistryMock.when(() -> NMSClassRegistry.findClass(NMSArgumentScoreboardTeam.class))
                       .thenAnswer(inv -> MockNMSArgumentScoreboardTeam.class);

        // coordinates
        nmsRegistryMock.when(() -> NMSClassRegistry.findClass(NMSArgumentVec3D.class))
                       .thenAnswer(inv -> MockNMSArgumentVec3D.class);

        // enchantment
        nmsRegistryMock.when(() -> NMSClassRegistry.findClass(NMSArgumentEnchantment.class))
                       .thenAnswer(inv -> MockNMSArgumentEnchantment.class);
        nmsRegistryMock.when(() -> NMSClassRegistry.findClass(NMSCraftEnchantment.class))
                       .thenAnswer(inv -> MockNMSCraftEnchantment.class);

        // potion effect
        nmsRegistryMock.when(() -> NMSClassRegistry.findClass(NMSArgumentMobEffect.class))
                       .thenAnswer(inv -> MockNMSArgumentMobEffect.class);
        nmsRegistryMock.when(() -> NMSClassRegistry.findClass(NMSCraftPotionEffectType.class))
                       .thenAnswer(inv -> MockNMSCraftPotionEffectType.class);

        // particle
        nmsRegistryMock.when(() -> NMSClassRegistry.findClass(NMSArgumentParticle.class))
                       .thenAnswer(inv -> MockNMSArgumentParticle.class);
        nmsRegistryMock.when(() -> NMSClassRegistry.findClass(NMSCraftParticle.class))
                       .thenAnswer(inv -> MockNMSCraftParticle.class);

        // item stack
        nmsRegistryMock.when(() -> NMSClassRegistry.findClass(NMSArgumentItemStack.class))
                       .thenAnswer(inv -> MockNMSArgumentItemStack.class);
        nmsRegistryMock.when(() -> NMSClassRegistry.findClass(NMSCraftItemStack.class))
                       .thenAnswer(inv -> MockNMSCraftItemStack.class);

        // block data
        nmsRegistryMock.when(() -> NMSClassRegistry.findClass(NMSArgumentTile.class))
                       .thenAnswer(inv -> MockNMSArgumentTile.class);
        nmsRegistryMock.when(() -> NMSClassRegistry.findClass(NMSCraftBlockData.class))
                       .thenAnswer(inv -> MockNMSCraftBlockData.class);

        new CommandNodeCreator<>(commandsSupplier.get(), permissionPrefix).build()
                                                                          .forEach(dispatcher.getRoot()::addChild);
    }

    /**
     * Registers a fake player, looked up by name when resolving
     */
    public CommandTester withFakePlayer(Player player) {
        fakeEntities.put(player.getName(), player);
        return this;
    }

    /**
     * Registers a fake entity by name, looked up when resolving
     */
    public CommandTester withFakeEntity(String name, Entity entity) {
        fakeEntities.put(name, entity);
        return this;
    }

    /**
     * Executes the given command input as the specified sender.
     *
     * @throws RuntimeException if the input does not match any registered command
     */
    public void execute(String input, CommandSender sender) {
        currentCommandSender = sender;
        try {
            dispatcher.execute(input, new Object());
        } catch (CommandSyntaxException e) {
            throw new RuntimeException("Command syntax error: " + e.getMessage(), e);
        }
    }

    /**
     * Executes the given command input as the specified sender.
     * Messages sent during execution are captured in {@link FakeSender#getSentMessages()}.
     *
     * @throws RuntimeException if the input does not match any registered command
     */
    public void execute(String input, FakeSender sender) {
        execute(input, sender.getCommandSender());
    }

    @Override
    public void close() {
        nmsRegistryMock.close();
        nmsReflectionMock.close();
        current = null;
    }
}
