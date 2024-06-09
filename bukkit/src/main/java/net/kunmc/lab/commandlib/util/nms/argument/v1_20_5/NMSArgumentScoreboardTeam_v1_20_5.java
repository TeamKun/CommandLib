package net.kunmc.lab.commandlib.util.nms.argument.v1_20_5;

import com.mojang.brigadier.arguments.ArgumentType;
import com.mojang.brigadier.context.CommandContext;
import net.kunmc.lab.commandlib.util.nms.argument.NMSArgumentScoreboardTeam;
import net.kunmc.lab.commandlib.util.nms.world.NMSScoreboardTeam;

public class NMSArgumentScoreboardTeam_v1_20_5 extends NMSArgumentScoreboardTeam {
    public NMSArgumentScoreboardTeam_v1_20_5() {
        super(null, "commands.arguments.TeamArgument");
    }

    @Override
    public ArgumentType<?> argument() {
        return ((ArgumentType<?>) newInstance(new Class[]{}, new Object[]{}));
    }

    @Override
    protected NMSScoreboardTeam parseImpl(CommandContext<?> ctx, String name) {
        return NMSScoreboardTeam.create(invokeStaticMethod("getTeam", ctx, name));
    }
}
