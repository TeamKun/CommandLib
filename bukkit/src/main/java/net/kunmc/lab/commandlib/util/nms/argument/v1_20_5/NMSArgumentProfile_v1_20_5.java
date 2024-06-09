package net.kunmc.lab.commandlib.util.nms.argument.v1_20_5;

import com.mojang.brigadier.arguments.ArgumentType;
import com.mojang.brigadier.context.CommandContext;
import net.kunmc.lab.commandlib.util.nms.argument.NMSArgumentProfile;

public class NMSArgumentProfile_v1_20_5 extends NMSArgumentProfile {
    public NMSArgumentProfile_v1_20_5() {
        super(null, "commands.arguments.GameProfileArgument");
    }

    @Override
    public ArgumentType<?> argument() {
        return ((ArgumentType<?>) newInstance(new Class[]{}, new Object[]{}));
    }

    @Override
    protected Object parseImpl(CommandContext<?> ctx, String name) {
        throw new UnsupportedOperationException();
    }
}
