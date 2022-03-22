package net.kunmc.lab.commandlib.argument;

import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import net.kunmc.lab.commandlib.Argument;
import net.kunmc.lab.commandlib.ContextAction;
import net.kunmc.lab.commandlib.SuggestionAction;
import net.minecraft.server.v1_16_R3.ArgumentVec3;
import net.minecraft.server.v1_16_R3.CommandListenerWrapper;
import net.minecraft.server.v1_16_R3.Vec3D;
import org.bukkit.Location;

public class LocationArgument extends Argument<Location> {
    public LocationArgument(String name, SuggestionAction suggestionAction, ContextAction contextAction) {
        super(name, suggestionAction, contextAction, ArgumentVec3.a());
    }

    @Override
    public Location parse(CommandContext<CommandListenerWrapper> ctx) {
        try {
            Vec3D vec = ArgumentVec3.a(ctx, name);
            return new Location(ctx.getSource().getBukkitWorld(), vec.x, vec.y, vec.z);
        } catch (CommandSyntaxException e) {
            return null;
        }
    }
}
