package net.kunmc.lab.commandlib.util.nms.world.v1_18_0;

import net.kunmc.lab.commandlib.util.nms.world.NMSScoreboardTeam;

public class NMSScoreboardTeam_v1_18_0 extends NMSScoreboardTeam {
    public NMSScoreboardTeam_v1_18_0(Object handle) {
        super(handle, "world.scores.ScoreboardTeam");
    }

    @Override
    public String getName() {
        return ((String) invokeMethod("b"));
    }
}
