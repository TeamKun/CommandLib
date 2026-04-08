package net.kunmc.lab.commandlib.nms.argument;

import com.mojang.brigadier.arguments.ArgumentType;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.context.CommandContext;
import net.kunmc.lab.commandlib.util.nms.argument.NMSArgumentProfile;

public class MockNMSArgumentProfile extends NMSArgumentProfile {
    public MockNMSArgumentProfile() {
        super(null, "Mock");
    }

    @Override
    public ArgumentType<?> argument() {
        // greedyString consumes all remaining input, matching the production NMS profile
        // argument behaviour. UnparsedArgument relies on this to capture multi-word input.
        return StringArgumentType.greedyString();
    }

    @Override
    protected Object parseImpl(CommandContext<?> ctx, String name) {
        throw new UnsupportedOperationException("MockNMSArgumentProfile.parseImpl is not called directly; " + "callers use ctx.getInput() instead.");
    }
}
