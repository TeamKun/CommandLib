package net.kunmc.lab.commandlib.nms.argument;

import com.mojang.brigadier.arguments.ArgumentType;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.context.CommandContext;
import net.kunmc.lab.commandlib.nms.world.MockNMSScoreboardTeam;
import net.kunmc.lab.commandlib.util.nms.argument.NMSArgumentScoreboardTeam;
import net.kunmc.lab.commandlib.util.nms.world.NMSScoreboardTeam;

public class MockNMSArgumentScoreboardTeam extends NMSArgumentScoreboardTeam {
    public MockNMSArgumentScoreboardTeam() {
        super(null, "Mock");
    }

    @Override
    public ArgumentType<?> argument() {
        return StringArgumentType.word();
    }

    @Override
    protected NMSScoreboardTeam parseImpl(CommandContext<?> ctx, String name) {
        return new MockNMSScoreboardTeam(StringArgumentType.getString(ctx, name));
    }
}
