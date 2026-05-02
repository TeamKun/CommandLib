package net.kunmc.lab.commandlib.util.nms.argument.v1_17_0;

import com.mojang.brigadier.arguments.ArgumentType;
import com.mojang.brigadier.context.CommandContext;
import net.kunmc.lab.commandlib.util.nms.argument.NMSArgumentNamespacedKey;

public class NMSArgumentNamespacedKey_v1_17_0 extends NMSArgumentNamespacedKey {
    public NMSArgumentNamespacedKey_v1_17_0() {
        super(null, "commands.arguments.ArgumentMinecraftKeyRegistered");
    }

    @Override
    public ArgumentType<?> argument() {
        return (ArgumentType<?>) invokeStaticMethod("a");
    }

    @Override
    protected String parseImpl(CommandContext<?> ctx, String name) {
        return invokeStaticMethod("e", ctx, name).toString();
    }
}
