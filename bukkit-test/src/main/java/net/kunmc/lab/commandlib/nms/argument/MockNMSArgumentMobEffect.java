package net.kunmc.lab.commandlib.nms.argument;

import com.mojang.brigadier.arguments.ArgumentType;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.context.CommandContext;
import net.kunmc.lab.commandlib.nms.world.MockNMSMobEffectList;
import net.kunmc.lab.commandlib.util.nms.argument.NMSArgumentMobEffect;
import net.kunmc.lab.commandlib.util.nms.world.NMSMobEffectList;

public class MockNMSArgumentMobEffect extends NMSArgumentMobEffect {
    public MockNMSArgumentMobEffect() {
        super(null, "Mock");
    }

    @Override
    public ArgumentType<?> argument() {
        return StringArgumentType.word();
    }

    @Override
    protected NMSMobEffectList parseImpl(CommandContext<?> ctx, String name) {
        return new MockNMSMobEffectList(StringArgumentType.getString(ctx, name));
    }
}
