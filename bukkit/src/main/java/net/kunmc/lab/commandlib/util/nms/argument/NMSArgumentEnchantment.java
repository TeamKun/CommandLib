package net.kunmc.lab.commandlib.util.nms.argument;

import com.mojang.brigadier.arguments.ArgumentType;
import com.mojang.brigadier.context.CommandContext;
import net.kunmc.lab.commandlib.util.nms.MinecraftClass;
import net.kunmc.lab.commandlib.util.nms.command.NMSCommandBuildContext;
import net.kunmc.lab.commandlib.util.nms.core.NMSHolder;
import net.kunmc.lab.commandlib.util.nms.core.NMSRegistries;
import net.kunmc.lab.commandlib.util.nms.exception.InvokeMethodException;
import net.kunmc.lab.commandlib.util.nms.resources.NMSResourceKey;
import net.kunmc.lab.commandlib.util.nms.server.NMSCraftServer;
import net.kunmc.lab.commandlib.util.nms.world.NMSEnchantment;

public class NMSArgumentEnchantment extends MinecraftClass {
    public NMSArgumentEnchantment() {
        super(null, "ArgumentEnchantment", "commands.arguments.ResourceArgument");
    }

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

    public NMSEnchantment parse(CommandContext<?> ctx, String name) {
        try {
            return new NMSEnchantment(invokeMethod("a", ctx, name));
        } catch (InvokeMethodException e) {
            return new NMSEnchantment(new NMSHolder.NMSReference(invokeMethod("g",
                                                                              "getEnchantment",
                                                                              ctx,
                                                                              name)).value());
        }
    }
}
