package net.kunmc.lab.commandlib.util.nms.argument;

import com.mojang.brigadier.arguments.ArgumentType;
import com.mojang.brigadier.context.CommandContext;
import net.kunmc.lab.commandlib.util.nms.command.NMSCommandBuildContext;
import net.kunmc.lab.commandlib.util.nms.core.NMSHolder;
import net.kunmc.lab.commandlib.util.nms.core.NMSRegistries;
import net.kunmc.lab.commandlib.util.nms.exception.MethodNotFoundException;
import net.kunmc.lab.commandlib.util.nms.resources.NMSResourceKey;
import net.kunmc.lab.commandlib.util.nms.server.NMSCraftServer;
import net.kunmc.lab.commandlib.util.nms.world.NMSEnchantment;

public class NMSArgumentEnchantment extends NMSArgument<NMSEnchantment> {
    public NMSArgumentEnchantment() {
        super(null, "ArgumentEnchantment", "commands.arguments.ResourceArgument");
    }

    @Override
    public ArgumentType<?> argument() {
        try {
            return ((ArgumentType<?>) invokeMethod("a"));
        } catch (Exception e) {
            NMSCommandBuildContext commandBuildContext = new NMSCraftServer().getServer()
                                                                             .getDataPackResources()
                                                                             .getCommandBuildContext();
            NMSResourceKey resourceKey = new NMSRegistries().enchantment();

            return ((ArgumentType<?>) newInstance(new Class<?>[]{commandBuildContext.getFoundClass(), resourceKey.getFoundClass()},
                                                  new Object[]{commandBuildContext.getHandle(), resourceKey.getHandle()}));
        }
    }

    @Override
    protected NMSEnchantment parseImpl(CommandContext<?> ctx, String name) {
        try {
            return new NMSEnchantment(invokeMethod("a", ctx, name));
        } catch (MethodNotFoundException e) {
            return new NMSEnchantment(new NMSHolder.NMSReference(invokeMethod("g",
                                                                              "getEnchantment",
                                                                              ctx,
                                                                              name)).value());
        }
    }
}
