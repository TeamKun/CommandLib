package net.kunmc.lab.commandlib.util.nms.argument.v1_19_3;

import com.mojang.brigadier.arguments.ArgumentType;
import com.mojang.brigadier.context.CommandContext;
import net.kunmc.lab.commandlib.util.nms.argument.NMSArgumentEnchantment;
import net.kunmc.lab.commandlib.util.nms.command.NMSCommandBuildContext;
import net.kunmc.lab.commandlib.util.nms.core.NMSHolder;
import net.kunmc.lab.commandlib.util.nms.core.NMSRegistries;
import net.kunmc.lab.commandlib.util.nms.resources.NMSResourceKey;
import net.kunmc.lab.commandlib.util.nms.server.NMSCraftServer;
import net.kunmc.lab.commandlib.util.nms.world.NMSEnchantment;

public class NMSArgumentEnchantment_v1_19_3 extends NMSArgumentEnchantment {
    public NMSArgumentEnchantment_v1_19_3() {
        super(null, "commands.arguments.ResourceArgument");
    }

    @Override
    public ArgumentType<?> argument() {
        NMSCommandBuildContext commandBuildContext = NMSCraftServer.create()
                                                                   .getServer()
                                                                   .getDataPackResources()
                                                                   .getCommandBuildContext();
        NMSResourceKey resourceKey = NMSRegistries.create()
                                                  .enchantment();

        return ((ArgumentType<?>) newInstance(new Class<?>[]{commandBuildContext.getFoundClass(), resourceKey.getFoundClass()},
                                              new Object[]{commandBuildContext.getHandle(), resourceKey.getHandle()}));
    }

    @Override
    protected NMSEnchantment parseImpl(CommandContext<?> ctx, String name) {
        return NMSEnchantment.create(NMSHolder.NMSReference.create(invokeStaticMethod("g", "getEnchantment", ctx, name))
                                                           .value());
    }
}
