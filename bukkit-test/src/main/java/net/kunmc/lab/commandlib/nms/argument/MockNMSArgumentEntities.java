package net.kunmc.lab.commandlib.nms.argument;

import com.mojang.brigadier.arguments.ArgumentType;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.context.CommandContext;
import net.kunmc.lab.commandlib.CommandTester;
import net.kunmc.lab.commandlib.util.nms.argument.NMSArgumentEntities;
import org.bukkit.entity.Entity;

import java.util.List;

public class MockNMSArgumentEntities extends NMSArgumentEntities {
    public MockNMSArgumentEntities() {
        super(null, "Mock");
    }

    @Override
    public ArgumentType<?> argument() {
        return StringArgumentType.word();
    }

    @Override
    protected List<Entity> parseImpl(CommandContext<?> ctx, String name) {
        String entityName = StringArgumentType.getString(ctx, name);
        Entity entity = CommandTester.getFakeEntity(entityName);
        if (entity == null) {
            throw new IllegalArgumentException("No fake entity registered with name: " + entityName + ". Call withFakePlayer() or withFakeEntity() before execute().");
        }
        return List.of(entity);
    }
}
