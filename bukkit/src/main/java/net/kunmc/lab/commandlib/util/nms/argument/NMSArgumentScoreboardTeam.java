package net.kunmc.lab.commandlib.util.nms.argument;

import com.mojang.brigadier.arguments.ArgumentType;
import com.mojang.brigadier.context.CommandContext;
import net.kunmc.lab.commandlib.util.nms.world.NMSScoreboardTeam;

public class NMSArgumentScoreboardTeam extends NMSArgument<NMSScoreboardTeam> {
    public NMSArgumentScoreboardTeam() {
        super("ArgumentScoreboardTeam", "commands.arguments.ArgumentScoreboardTeam", "commands.arguments.TeamArgument");
    }

    @Override
    public ArgumentType<?> argument() {
        return ((ArgumentType<?>) newInstance(new Class[]{}, new Object[]{}));
    }

    @Override
    protected NMSScoreboardTeam parseImpl(CommandContext<?> ctx, String name) {
        return new NMSScoreboardTeam(invokeMethod("a", "getTeam", ctx, name));
    }
}
