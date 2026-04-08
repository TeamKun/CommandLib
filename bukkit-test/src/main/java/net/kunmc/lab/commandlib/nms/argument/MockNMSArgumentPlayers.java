package net.kunmc.lab.commandlib.nms.argument;

import com.mojang.brigadier.arguments.ArgumentType;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.context.CommandContext;
import net.kunmc.lab.commandlib.CommandTester;
import net.kunmc.lab.commandlib.util.nms.argument.NMSArgumentPlayers;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;

import java.util.List;

public class MockNMSArgumentPlayers extends NMSArgumentPlayers {
    public MockNMSArgumentPlayers() {
        super(null, "Mock");
    }

    @Override
    public ArgumentType<?> argument() {
        return StringArgumentType.word();
    }

    @Override
    protected List<Player> parseImpl(CommandContext<?> ctx, String name) {
        Entity entity = CommandTester.getFakeEntity(StringArgumentType.getString(ctx, name));
        if (!(entity instanceof Player)) {
            throw new IllegalArgumentException("No fake player registered with name: " + name + ". Call withFakePlayer() before execute().");
        }
        return List.of((Player) entity);
    }
}
