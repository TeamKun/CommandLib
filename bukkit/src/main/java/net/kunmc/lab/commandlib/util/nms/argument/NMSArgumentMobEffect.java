package net.kunmc.lab.commandlib.util.nms.argument;

import com.mojang.brigadier.arguments.ArgumentType;
import com.mojang.brigadier.context.CommandContext;
import net.kunmc.lab.commandlib.util.bukkit.VersionUtil;
import net.kunmc.lab.commandlib.util.nms.command.NMSCommandBuildContext;
import net.kunmc.lab.commandlib.util.nms.core.NMSHolder;
import net.kunmc.lab.commandlib.util.nms.core.NMSRegistries;
import net.kunmc.lab.commandlib.util.nms.exception.MethodNotFoundException;
import net.kunmc.lab.commandlib.util.nms.resources.NMSResourceKey;
import net.kunmc.lab.commandlib.util.nms.server.NMSCraftServer;
import net.kunmc.lab.commandlib.util.nms.world.NMSMobEffectList;

public class NMSArgumentMobEffect extends NMSArgument<NMSMobEffectList> {
    public NMSArgumentMobEffect() {
        super("ArgumentMobEffect", "commands.arguments.ResourceArgument");
    }

    @Override
    public ArgumentType<?> argument() {
        try {
            return ((ArgumentType<?>) newInstance(new Class[]{}, new Object[]{}));
        } catch (Exception e) {
            NMSCommandBuildContext commandBuildContext = new NMSCraftServer().getServer()
                                                                             .getDataPackResources()
                                                                             .getCommandBuildContext();
            NMSResourceKey resourceKey = new NMSRegistries().mobEffect();

            return ((ArgumentType<?>) newInstance(new Class<?>[]{commandBuildContext.getFoundClass(), resourceKey.getFoundClass()},
                                                  new Object[]{commandBuildContext.getHandle(), resourceKey.getHandle()}));
        }
    }

    @Override
    protected NMSMobEffectList parseImpl(CommandContext<?> ctx, String name) {
        if (VersionUtil.is1_20_x()) {
            return new NMSMobEffectList(new NMSHolder.NMSReference(invokeMethod("f", ctx, name)).value());
        }

        try {
            return new NMSMobEffectList(invokeMethod("a", ctx, name));
        } catch (MethodNotFoundException e) {
            return new NMSMobEffectList(new NMSHolder.NMSReference(invokeMethod("f",
                                                                                "getMobEffect",
                                                                                ctx,
                                                                                name)).value());
        }
    }
}
