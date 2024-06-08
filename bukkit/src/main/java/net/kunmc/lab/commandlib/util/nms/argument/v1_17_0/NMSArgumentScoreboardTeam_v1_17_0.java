package net.kunmc.lab.commandlib.util.nms.argument.v1_17_0;

import com.mojang.brigadier.arguments.ArgumentType;
import com.mojang.brigadier.context.CommandContext;
import net.kunmc.lab.commandlib.util.nms.argument.NMSArgumentScoreboardTeam;
import net.kunmc.lab.commandlib.util.nms.world.NMSScoreboardTeam;

public class NMSArgumentScoreboardTeam_v1_17_0 extends NMSArgumentScoreboardTeam {
    public NMSArgumentScoreboardTeam_v1_17_0() {
        super(null, "commands.arguments.ArgumentScoreboardTeam");
    }

    @Override
    public ArgumentType<?> argument() {
        return ((ArgumentType<?>) newInstance(new Class[]{}, new Object[]{}));
    }

    @Override
    protected NMSScoreboardTeam parseImpl(CommandContext<?> ctx, String name) {
        return NMSScoreboardTeam.create(invokeStaticMethod("a", ctx, name));
    }
}
