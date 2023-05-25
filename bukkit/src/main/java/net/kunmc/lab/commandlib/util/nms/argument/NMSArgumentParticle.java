package net.kunmc.lab.commandlib.util.nms.argument;

import com.mojang.brigadier.arguments.ArgumentType;
import com.mojang.brigadier.context.CommandContext;
import net.kunmc.lab.commandlib.util.nms.command.NMSCommandBuildContext;
import net.kunmc.lab.commandlib.util.nms.core.NMSParticleParam;
import net.kunmc.lab.commandlib.util.nms.server.NMSCraftServer;

public class NMSArgumentParticle extends NMSArgument<NMSParticleParam> {
    public NMSArgumentParticle() {
        super("ArgumentParticle", "commands.arguments.ArgumentParticle", "commands.arguments.ParticleArgument");
    }

    @Override
    public ArgumentType<?> argument() {
        try {
            return (ArgumentType<?>) newInstance(new Class<?>[]{}, new Object[]{});
        } catch (Exception e) {
            NMSCommandBuildContext commandBuildContext = new NMSCraftServer().getServer()
                                                                             .getDataPackResources()
                                                                             .getCommandBuildContext();
            return (ArgumentType<?>) newInstance(new Class<?>[]{commandBuildContext.getFoundClass()},
                                                 new Object[]{commandBuildContext.getHandle()});
        }
    }

    @Override
    protected NMSParticleParam parseImpl(CommandContext<?> ctx, String name) {
        return new NMSParticleParam(invokeMethod("a", "getParticle", ctx, name));
    }
}
