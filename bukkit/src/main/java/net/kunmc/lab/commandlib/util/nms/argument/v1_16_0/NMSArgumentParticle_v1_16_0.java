package net.kunmc.lab.commandlib.util.nms.argument.v1_16_0;

import com.mojang.brigadier.arguments.ArgumentType;
import com.mojang.brigadier.context.CommandContext;
import net.kunmc.lab.commandlib.util.nms.argument.NMSArgumentParticle;
import net.kunmc.lab.commandlib.util.nms.core.NMSParticleParam;

public class NMSArgumentParticle_v1_16_0 extends NMSArgumentParticle {
    public NMSArgumentParticle_v1_16_0() {
        super(null, "ArgumentParticle");
    }

    @Override
    public ArgumentType<?> argument() {
        return (ArgumentType<?>) newInstance(new Class<?>[]{}, new Object[]{});
    }

    @Override
    protected NMSParticleParam parseImpl(CommandContext<?> ctx, String name) {
        return NMSParticleParam.create(invokeStaticMethod("a", ctx, name));
    }
}
