package net.kunmc.lab.commandlib.argument;

import com.mojang.brigadier.exceptions.CommandSyntaxException;
import net.kunmc.lab.commandlib.Argument;
import net.kunmc.lab.commandlib.CommandContext;
import net.kunmc.lab.commandlib.exception.IncorrectArgumentInputException;
import net.kunmc.lab.commandlib.util.Location;
import net.minecraft.command.arguments.Vec3Argument;
import net.minecraft.entity.Entity;
import net.minecraft.util.math.vector.Vector3d;

import java.util.function.Consumer;

public class LocationArgument extends Argument<Location> {
    public LocationArgument(String name) {
        this(name, option -> {
        });
    }

    public LocationArgument(String name, Consumer<Option<Location, CommandContext>> options) {
        super(name, Vec3Argument.vec3());
        setOptions(options);
    }

    @Override
    public Location cast(Object parsedArgument) {
        return ((Location) parsedArgument);
    }

    @Override
    protected Location parseImpl(CommandContext ctx) throws IncorrectArgumentInputException, CommandSyntaxException {
        Vector3d vec = Vec3Argument.getVec3(ctx.getHandle(), name());
        Location loc = new Location(ctx.getWorld(), vec.x, vec.y, vec.z);

        Entity sender = ctx.getEntity();
        if (sender != null) {
            loc.setYaw(sender.rotationYaw);
            loc.setPitch(sender.rotationPitch);
        }

        return loc;
    }
}
