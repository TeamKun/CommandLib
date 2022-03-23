package net.kunmc.lab.sampleplugin;

import net.kunmc.lab.commandlib.CommandLib;
import org.bukkit.plugin.java.JavaPlugin;

public final class SamplePlugin extends JavaPlugin {
    @Override
    public void onEnable() {
        CommandLib.register(this,
                new ConfigCommand(),
                new HelloCommand(),
                new NoArgsCommand());
    }
}
