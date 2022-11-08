package net.kunmc.lab.samplemod;

import net.kunmc.lab.commandlib.CommandLib;
import net.minecraftforge.fml.common.Mod;

@Mod("samplemod")
public class SampleMod {
    public SampleMod() {
        CommandLib.register(new ConfigCommand(), new HelloCommand(), new NoArgsCommand());
    }
}
