package net.kunmc.lab.commandlib.util.nms.argument.v1_17_0;

import com.mojang.brigadier.arguments.ArgumentType;
import com.mojang.brigadier.context.CommandContext;
import net.kunmc.lab.commandlib.util.nms.argument.NMSArgumentEnchantment;
import net.kunmc.lab.commandlib.util.nms.world.NMSEnchantment;

public class NMSArgumentEnchantment_v1_17_0 extends NMSArgumentEnchantment {
    public NMSArgumentEnchantment_v1_17_0() {
        super(null, "commands.arguments.ArgumentEnchantment");
    }

    @Override
    public ArgumentType<?> argument() {
        return ((ArgumentType<?>) invokeStaticMethod("a"));
    }

    @Override
    protected NMSEnchantment parseImpl(CommandContext<?> ctx, String name) {
        return NMSEnchantment.create(invokeStaticMethod("a", ctx, name));
    }
}
