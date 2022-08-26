package net.kunmc.lab.commandlib.argument;

import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import net.kunmc.lab.commandlib.Argument;
import net.kunmc.lab.commandlib.ContextAction;
import net.kunmc.lab.commandlib.SuggestionAction;
import net.kunmc.lab.commandlib.argument.exception.IncorrectArgumentInputException;
import net.minecraft.server.v1_16_R3.ArgumentVec3;
import net.minecraft.server.v1_16_R3.CommandListenerWrapper;
import net.minecraft.server.v1_16_R3.Vec3D;
import org.bukkit.Location;

import java.util.function.Predicate;

public class LocationArgument extends Argument<Location> {
    private final Predicate<? super Location> filter;

    public LocationArgument(String name, SuggestionAction suggestionAction, Predicate<? super Location> filter, ContextAction contextAction) {
        super(name, suggestionAction, contextAction, ArgumentVec3.a());
        this.filter = filter;
    }

    @Override
    public Location parse(CommandContext<CommandListenerWrapper> ctx) throws IncorrectArgumentInputException {
        try {
            Vec3D vec = ArgumentVec3.a(ctx, name);
            Location location = new Location(ctx.getSource().getBukkitWorld(), vec.x, vec.y, vec.z);
            if (filter == null || filter.test(location)) {
                return location;
            }
            throw new IncorrectArgumentInputException(this, ctx, getInputString(ctx, name));
        } catch (CommandSyntaxException e) {
            throw convertSyntaxException(e);
        }
    }
}
