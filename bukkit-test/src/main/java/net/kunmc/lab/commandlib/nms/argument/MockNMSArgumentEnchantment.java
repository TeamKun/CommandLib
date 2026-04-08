package net.kunmc.lab.commandlib.nms.argument;

import com.mojang.brigadier.arguments.ArgumentType;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.context.CommandContext;
import net.kunmc.lab.commandlib.nms.world.MockNMSEnchantment;
import net.kunmc.lab.commandlib.util.nms.argument.NMSArgumentEnchantment;
import net.kunmc.lab.commandlib.util.nms.world.NMSEnchantment;

public class MockNMSArgumentEnchantment extends NMSArgumentEnchantment {
    public MockNMSArgumentEnchantment() {
        super(null, "Mock");
    }

    @Override
    public ArgumentType<?> argument() {
        return StringArgumentType.word();
    }

    @Override
    protected NMSEnchantment parseImpl(CommandContext<?> ctx, String name) {
        return new MockNMSEnchantment(StringArgumentType.getString(ctx, name));
    }
}
