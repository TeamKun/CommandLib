package net.kunmc.lab.commandlib.nms.argument;

import com.mojang.brigadier.arguments.ArgumentType;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.context.CommandContext;
import net.kunmc.lab.commandlib.CommandTester;
import net.kunmc.lab.commandlib.util.nms.argument.NMSArgumentEntity;
import org.bukkit.entity.Entity;

public class MockNMSArgumentEntity extends NMSArgumentEntity {
    public MockNMSArgumentEntity() {
        super(null, "Mock");
    }

    @Override
    public ArgumentType<?> argument() {
        return StringArgumentType.word();
    }

    @Override
    protected Entity parseImpl(CommandContext<?> ctx, String name) {
        Entity entity = CommandTester.getFakeEntity(StringArgumentType.getString(ctx, name));
        if (entity == null) {
            throw new IllegalArgumentException("No fake entity registered with name: " + name + ". Call withFakePlayer() or withFakeEntity() before execute().");
        }
        return entity;
    }
}
