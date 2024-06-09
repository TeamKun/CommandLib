package net.kunmc.lab.commandlib.util.nms.argument.v1_20_5;

import com.mojang.brigadier.arguments.ArgumentType;
import com.mojang.brigadier.context.CommandContext;
import net.kunmc.lab.commandlib.util.nms.argument.NMSArgumentMobEffect;
import net.kunmc.lab.commandlib.util.nms.command.NMSCommandBuildContext;
import net.kunmc.lab.commandlib.util.nms.core.NMSHolder;
import net.kunmc.lab.commandlib.util.nms.core.NMSRegistries;
import net.kunmc.lab.commandlib.util.nms.resources.NMSResourceKey;
import net.kunmc.lab.commandlib.util.nms.server.NMSCraftServer;
import net.kunmc.lab.commandlib.util.nms.world.NMSMobEffectList;

public class NMSArgumentMobEffect_v1_20_5 extends NMSArgumentMobEffect {
    public NMSArgumentMobEffect_v1_20_5() {
        super(null, "commands.arguments.ResourceArgument");
    }

    @Override
    public ArgumentType<?> argument() {
        NMSCommandBuildContext commandBuildContext = NMSCraftServer.create()
                                                                   .getServer()
                                                                   .getDataPackResources()
                                                                   .getCommandBuildContext();
        NMSResourceKey resourceKey = NMSRegistries.create()
                                                  .mobEffect();

        return ((ArgumentType<?>) newInstance(new Class<?>[]{commandBuildContext.getFoundClass(), resourceKey.getFoundClass()},
                                              new Object[]{commandBuildContext.getHandle(), resourceKey.getHandle()}));
    }

    @Override
    protected NMSMobEffectList parseImpl(CommandContext<?> ctx, String name) {
        return NMSMobEffectList.create(NMSHolder.NMSReference.create(invokeStaticMethod("getMobEffect", ctx, name))
                                                             .value());
    }
}
