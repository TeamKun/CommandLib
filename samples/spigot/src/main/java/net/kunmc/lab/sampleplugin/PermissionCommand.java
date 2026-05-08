package net.kunmc.lab.sampleplugin;

import net.kunmc.lab.commandlib.Command;
import net.kunmc.lab.commandlib.DefaultPermission;

/**
 * /admincheck
 */
public class PermissionCommand extends Command {
    public PermissionCommand() {
        super("admincheck");

        permission("sample.command.admincheck", DefaultPermission.OP, "Run the sample admin command");
        execute(ctx -> {
            ctx.sendSuccess("You can run admin commands.");
        });
    }
}
