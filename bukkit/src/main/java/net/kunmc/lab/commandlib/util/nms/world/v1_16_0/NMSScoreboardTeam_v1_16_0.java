package net.kunmc.lab.commandlib.util.nms.world.v1_16_0;

import net.kunmc.lab.commandlib.util.nms.world.NMSScoreboardTeam;

public class NMSScoreboardTeam_v1_16_0 extends NMSScoreboardTeam {
    public NMSScoreboardTeam_v1_16_0(Object handle) {
        super(handle, "ScoreboardTeam");
    }

    @Override
    public String getName() {
        return ((String) invokeMethod("getName"));
    }
}
