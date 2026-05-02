package net.kunmc.lab.commandlib.util.nms.argument.v1_20_5;

import com.mojang.brigadier.arguments.ArgumentType;
import com.mojang.brigadier.context.CommandContext;
import net.kunmc.lab.commandlib.util.nms.argument.NMSArgumentNamespacedKey;

public class NMSArgumentNamespacedKey_v1_20_5 extends NMSArgumentNamespacedKey {
    public NMSArgumentNamespacedKey_v1_20_5() {
        super(null, "commands.arguments.ResourceLocationArgument");
    }

    @Override
    public ArgumentType<?> argument() {
        return (ArgumentType<?>) invokeStaticMethod("id");
    }

    @Override
    protected String parseImpl(CommandContext<?> ctx, String name) {
        return invokeStaticMethod("getId", ctx, name).toString();
    }
}
