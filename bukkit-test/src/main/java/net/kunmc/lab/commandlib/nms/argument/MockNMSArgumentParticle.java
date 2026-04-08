package net.kunmc.lab.commandlib.nms.argument;

import com.mojang.brigadier.arguments.ArgumentType;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.context.CommandContext;
import net.kunmc.lab.commandlib.nms.core.MockNMSParticleParam;
import net.kunmc.lab.commandlib.util.nms.argument.NMSArgumentParticle;
import net.kunmc.lab.commandlib.util.nms.core.NMSParticleParam;

public class MockNMSArgumentParticle extends NMSArgumentParticle {
    public MockNMSArgumentParticle() {
        super(null, "Mock");
    }

    @Override
    public ArgumentType<?> argument() {
        return StringArgumentType.word();
    }

    @Override
    protected NMSParticleParam parseImpl(CommandContext<?> ctx, String name) {
        return new MockNMSParticleParam(StringArgumentType.getString(ctx, name));
    }
}
