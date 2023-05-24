package net.kunmc.lab.commandlib.util.nms.world;

import net.kunmc.lab.commandlib.util.nms.MinecraftClass;

public class NMSScoreboardTeam extends MinecraftClass {
    public NMSScoreboardTeam(Object handle) {
        super(handle, "ScoreboardTeam", "world.scores.ScoreboardTeam", "world.scores.PlayerTeam");
    }

    public String getName() {
        return ((String) invokeMethod("getName", "b"));
    }
}
