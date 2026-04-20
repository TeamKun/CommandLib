package net.kunmc.lab.commandlib.util.nms.world.v1_20_5;

import net.kunmc.lab.commandlib.util.nms.world.NMSScoreboardTeam;

public class NMSScoreboardTeam_v1_20_5 extends NMSScoreboardTeam {
    public NMSScoreboardTeam_v1_20_5(Object handle) {
        super(handle, "world.scores.PlayerTeam");
    }

    @Override
    public String getName() {
        return ((String) invokeMethod("getName"));
    }
}
