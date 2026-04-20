package net.kunmc.lab.commandlib.util.nms.argument.v1_20_5;

import com.mojang.brigadier.arguments.ArgumentType;
import com.mojang.brigadier.context.CommandContext;
import net.kunmc.lab.commandlib.util.nms.argument.NMSArgumentParticle;
import net.kunmc.lab.commandlib.util.nms.command.NMSCommandBuildContext;
import net.kunmc.lab.commandlib.util.nms.core.NMSParticleParam;
import net.kunmc.lab.commandlib.util.nms.server.NMSCraftServer;

public class NMSArgumentParticle_v1_20_5 extends NMSArgumentParticle {
    public NMSArgumentParticle_v1_20_5() {
        super(null, "commands.arguments.ParticleArgument");
    }

    @Override
    public ArgumentType<?> argument() {
        NMSCommandBuildContext commandBuildContext = NMSCraftServer.create()
                                                                   .getServer()
                                                                   .getDataPackResources()
                                                                   .getCommandBuildContext();
        return (ArgumentType<?>) newInstance(new Class<?>[]{commandBuildContext.getFoundClass()},
                                             new Object[]{commandBuildContext.getHandle()});
    }

    @Override
    protected NMSParticleParam parseImpl(CommandContext<?> ctx, String name) {
        return NMSParticleParam.create(invokeStaticMethod("getParticle", ctx, name));
    }
}
